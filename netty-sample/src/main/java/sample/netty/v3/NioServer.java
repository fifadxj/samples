package sample.netty.v3;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class NioServer {
	private final ExecutorService bossExecutor = Executors.newCachedThreadPool();
	private final ExecutorService workerExecutor = Executors.newCachedThreadPool();
    private ChannelFactory factory = new NioServerSocketChannelFactory(
            bossExecutor, workerExecutor, Runtime.getRuntime().availableProcessors() + 1);
    private final ExecutorService serviceExecutor = Executors.newCachedThreadPool();

	private int port;

	private ServerBootstrap bootstrap;

	private Channel channel;

	public NioServer(int port) {
		this.port = port;
	}

	public void start() {
		bootstrap = new ServerBootstrap(factory);

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
            public void messageReceived(ChannelHandlerContext ctx,
                                        MessageEvent e) throws Exception {

                final ChannelBuffer cb = (ChannelBuffer) e.getMessage();
                final Channel subChannel = e.getChannel();
                serviceExecutor.submit(new Runnable() {

                    public void run() {
                        long reqId = cb.readLong();
                        byte[] b = new byte[cb.readableBytes()];
                        cb.readBytes(b);
                        try {
                            String reqMsg = new String(b, Charsets.UTF_8);
                            log.info("Read request:{}", reqMsg);
                            String respMsg = reqMsg.toUpperCase();
                            log.info("Write response:{}", respMsg);

                            byte[] resp = respMsg.getBytes(Charsets.UTF_8);
                            ChannelBuffer respCb = ChannelBuffers.buffer(4 + 8 + resp.length);
                            respCb.writeInt(8 + resp.length);
                            respCb.writeLong(reqId);
                            respCb.writeBytes(resp);
                            subChannel.write(respCb);
                        } catch (Exception e) {
                            log.error("process failed", e);
                        }
                    }

                });
            }
        };

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(decoder, processor);
		    }
		});

		bootstrap.setOption("child.tcpNoDelay", false);
		bootstrap.setOption("child.keepAlive", true);

		channel = bootstrap.bind(new InetSocketAddress(port));
		log.info("Start nio server successfully, listen to " + port);
	}

	public void stop() {
		ChannelFuture future = channel.close();
		future.awaitUninterruptibly();

		bootstrap.releaseExternalResources();
		serviceExecutor.shutdown();
		log.info("Stop nio server successfully");
	}

	public static void main(String[] args) {
		new NioServer(8080).start();
	}
}
