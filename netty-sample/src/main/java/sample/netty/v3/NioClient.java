package sample.netty.v3;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import sample.netty.Callback;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class NioClient {

	private ExecutorService bossExecutor = Executors.newCachedThreadPool();
	private ExecutorService workerExecutor = Executors.newCachedThreadPool();
	private ChannelFactory factory = new NioClientSocketChannelFactory(
			bossExecutor, workerExecutor, Runtime.getRuntime().availableProcessors() + 1);

	private Map<Long, Callback> waitingMap = new ConcurrentHashMap<Long, Callback>();

	private ClientBootstrap bootstrap;

	private Channel channel;

	private String ip;

	private int port;

	private int maxWaitingNum = 10000;

	private int readTimeout = 5;

	private volatile boolean isAvaiable = true;

	private AtomicLong reqIdGenerator = new AtomicLong(1);

	public NioClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public boolean connect() {
		bootstrap = new ClientBootstrap(factory);

        final ChannelHandler decoder = new FrameDecoder() {
            @Override
            protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buf) throws Exception {
                if (buf.readableBytes() < 4) {
                    return null;
                }

                buf.markReaderIndex();

                int length = buf.readInt();

                if (buf.readableBytes() < length) {
                    buf.resetReaderIndex();
                    return null;
                }

                ChannelBuffer frame = buf.readBytes(length);

                return frame;
            }
        };

        final ChannelHandler processor = new SimpleChannelHandler() {
            @Override
            public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
                ChannelBuffer cb = (ChannelBuffer) e.getMessage();
                long reqId = cb.readLong();
                byte[] resp = new byte[cb.readableBytes()];
                cb.readBytes(resp);
                Callback callback = waitingMap.get(reqId);
                if (callback != null) {
                    callback.setResp(resp);
                } else {
                    String respText = new String(resp, Charsets.UTF_8.toString());
                    log.warn("Late arrived response, reqId:{}, respMsg:{}", reqId, respText);
                }

            }
        };

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(decoder, processor);
			}
		});

		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);

		final ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
		channel = future.awaitUninterruptibly().getChannel();

		if (!future.isSuccess()) {
			log.error("Connect to " + ip + ":" + port + " failed.",
					future.getCause());
			return false;
		} else {
			log.info("Connect to " + ip + ":" + port + " successfully.");
			return true;
		}

	}


	public String send(String reqMsg) {
	   return send(reqIdGenerator.getAndIncrement(), reqMsg, this.readTimeout);
	}

    public String send(String reqMsg, int readTimeout) {
        return send(reqIdGenerator.getAndIncrement(), reqMsg, readTimeout);
    }

	private String send(final long reqId, String reqMsg, int readTimeout) {
		if (waitingMap.size() > maxWaitingNum) {
			log.error("限流, reqId:{}, 目前等待结果的请求数量:{}", reqId, waitingMap.size());
			return null;
		}
		log.info("Write request, reqId:{}, reqMsg:{}", reqId, reqMsg);
		try {
			byte[] req = reqMsg.getBytes(Charsets.UTF_8.toString());
			ChannelBuffer cb = ChannelBuffers.buffer(4 + 8 + req.length);
			cb.writeInt(8 + req.length);
			cb.writeLong(reqId);
			cb.writeBytes(req);
			Callback callback = new Callback(reqId, readTimeout);
			waitingMap.put(reqId, callback);
			final ChannelFuture writeFuture = channel.write(cb);
			writeFuture.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future)
						throws Exception {
					if (future.isSuccess()) {
						return;
					}
					log.error("Write request failed, reqId:{}, cause:{}", reqId, future.getCause());
				}
			});
			byte[] resp = callback.getResp();
			if (resp != null) {
				String respMsg = new String(resp, Charsets.UTF_8.toString());
				log.info("Read response, reqId:{}, respMsg:{}", reqId, respMsg);

				return respMsg;
			} else {
                log.warn("Read timeout, reqId:{}", reqId);
            }
		} catch (Exception e) {
			log.error("reqId: " + reqId, e);
		} finally {
			waitingMap.remove(reqId);
		}

		return null;
	}

	/**
	 * 检测长连接是否可用。
	 */
	public boolean isAvaiable() {
		return channel != null && channel.isConnected() && isAvaiable;
	}

	/**
	 * 设置请求发送的最大并发数，即同步等待应答报文的请求数，超过该上限新交易请求即被丢弃。
	 */
	public void setMaxWaitingNum(int maxWaitingNum) {
		this.maxWaitingNum = maxWaitingNum;
	}

	/**
	 * 设置读超时时间，即系统异步等待的超时时间。
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * 等待已发送的请求全部获得应答后关闭长连接，并清理资源。
	 */
	public void closeGracefully() {
		this.isAvaiable = false;
		if (!waitingMap.isEmpty()) {
			try {
				Thread.sleep((readTimeout + 1) * 1000);
			} catch (InterruptedException e) {
			}
		}
		if (!waitingMap.isEmpty()) {
			log.error("Still has waiting request " + waitingMap.keySet() + " when closed.");
		}
		closeNow();
	}

	/**
	 * 立即关闭长连接，并清理资源。
	 */
	public void closeNow() {
		this.isAvaiable = false;
		ChannelFuture future = channel.close();
		future.awaitUninterruptibly();
		bootstrap.releaseExternalResources();

		if (!future.isSuccess()) {
			log.error("Close socket client to " + ip + ":" + port + " failed.", future.getCause());
		} else {
			log.info("Close socket client to " + ip + ":" + port + " successfully.");
		}
	}

    public static void main(String[] args) throws IOException {
        NioClient client = new NioClient("127.0.0.1", 8080);
        if(client.connect()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String line = null;
            while (!(line = reader.readLine()).equals("exit")) {
                String resp = client.send(line);
                System.out.println(resp);
            }

            client.closeGracefully();
        }
    }
}
