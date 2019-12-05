package sample.netty.v4;

import com.google.common.base.Charsets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.ImmediateEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class NioServer {
	private int port;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

	public NioServer(int port) {
		this.port = port;
	}

	public void start() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ReqDecoder());
                        ch.pipeline().addLast(new ReqProcessor());
                    }
                })
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture f = b.bind(port).sync();

        //f.channel().closeFuture().sync();
        log.info("Start nio server successfully, listen to " + port);
	}


	public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
		log.info("Stop nio server successfully");
	}

	public static void main(String[] args) throws Exception {
		new NioServer(8080).start();
	}
}

@Slf4j
class ReqProcessor extends ChannelInboundHandlerAdapter {
    private final ExecutorService serviceExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Req req = (Req) msg;
        serviceExecutor.submit(new Runnable() {

            public void run() {
                try {
                    String reqMsg = new String(req.getBody(), Charsets.UTF_8);
                    log.info("Read request:{}", reqMsg);
                    String respMsg = "(" + reqMsg + ")";
                    log.info("Write response:{}", respMsg);

                    byte[] resp = respMsg.getBytes(Charsets.UTF_8);
                    ByteBuf out = ctx.alloc().buffer(4 + 8 + resp.length);
                    out.writeInt(8 + resp.length);
                    out.writeLong(req.getReqId());
                    out.writeBytes(resp);
                    ChannelFuture writeFuture = ctx.channel().writeAndFlush(out);

                    writeFuture.addListener(new ChannelFutureListener() {
                        public void operationComplete(ChannelFuture future)
                                throws Exception {
                            if (future.isSuccess()) {
                                return;
                            }
                            log.error("Write response failed, reqId:{}, cause:{}", req.getReqId(), future.cause());
                        }
                    });
                } catch (Exception e) {
                    log.error("process failed", e);
                }
            }

        });
    }
}

class ReqDecoder extends ByteToMessageDecoder {
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

        Req req = new Req();
        req.setReqId(reqId);
        req.setBody(b);

        out.add(req);
    }
}
