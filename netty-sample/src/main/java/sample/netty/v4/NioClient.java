package sample.netty.v4;

import com.google.common.base.Charsets;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import sample.netty.Callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class NioClient {
	private Map<Long, Callback> waitingMap = new ConcurrentHashMap<Long, Callback>();

	private String ip;

	private int port;

	private int maxWaitingNum = 10000;

	private int readTimeout = 5;

	private volatile boolean isAvaiable = true;

	private AtomicLong reqIdGenerator = new AtomicLong(1);

	private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

	public NioClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public boolean connect() throws Exception {
		Bootstrap b = new Bootstrap();
		b.group(workerGroup);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.TCP_NODELAY, true);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new RespDecoder());
				ch.pipeline().addLast(new RespProcessor(waitingMap));
			}
		});

        ChannelFuture future = b.connect(new InetSocketAddress(ip, port)).sync();
        channel = future.awaitUninterruptibly().channel();

		if (!future.isSuccess()) {
			log.error("Connect to " + ip + ":" + port + " failed.",
					future.cause());
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
            ByteBuf out = channel.alloc().buffer(4 + 8 + req.length);
            out.writeInt(8 + req.length);
            out.writeLong(reqId);
            out.writeBytes(req);
			Callback callback = new Callback(reqId, readTimeout);
			waitingMap.put(reqId, callback);
			ChannelFuture writeFuture = channel.writeAndFlush(out);
			writeFuture.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future)
						throws Exception {
					if (future.isSuccess()) {
						return;
					}
					log.error("Write request failed, reqId:{}, cause:{}", reqId, future.cause());
				}
			});
            //writeFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);

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
		return channel != null && channel.isOpen() && isAvaiable;
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
        workerGroup.shutdownGracefully();

		if (!future.isSuccess()) {
			log.error("Close socket client to " + ip + ":" + port + " failed.", future.cause());
		} else {
			log.info("Close socket client to " + ip + ":" + port + " successfully.");
		}
	}

    public static void main(String[] args) throws Exception {
        NioClient client = new NioClient("localhost", 8080);
        if(client.connect()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String line = null;

            while (!(line = reader.readLine()).equals("exit")) {
                String resp = client.send(line);
                System.out.println(resp);
            }

            client.closeGracefully();
        }

/*        if (client.connect()) {
            ExecutorService service = Executors.newFixedThreadPool(100);
            AtomicInteger ai = new AtomicInteger(1);
            int i = 1;
            while (i++ <= 10000) {
                service.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        String resp = client.send(ai.getAndIncrement() + "");
                        //System.out.println(resp);

                        return null;
                    }
                });
            }
        }*/
    }
}

@Slf4j
class RespProcessor extends ChannelInboundHandlerAdapter {
    private Map<Long, Callback> waitingMap;
    public RespProcessor(Map<Long, Callback> waitingMap) {
        this.waitingMap = waitingMap;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Resp resp = (Resp) msg;
        long reqId = resp.getReqId();
        Callback callback = waitingMap.get(reqId);
        if (callback != null) {
            callback.setResp(resp.getBody());
        } else {
            String respText = new String(resp.getBody(), Charsets.UTF_8.toString());
            log.warn("Late arrived response, reqId:{}, respMsg:{}", reqId, respText);
        }

    }
}

class RespDecoder extends ByteToMessageDecoder {
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        if (buf.readableBytes() < 4) {
            return;
        }

        buf.markReaderIndex();

        int length = buf.readInt();

        if (buf.readableBytes() < length) {
            buf.resetReaderIndex();
            return;
        }

        long reqId = buf.readLong();
        byte[] b = new byte[length - 8];
        buf.readBytes(b);

        Resp resp = new Resp();
        resp.setReqId(reqId);
        resp.setBody(b);

        out.add(resp);
    }
}
