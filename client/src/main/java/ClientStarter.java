import codce.MyProtocolDecoder;
import codce.MyProtocolEncoder;
import handle.ClientTestHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelGroupFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

/**
 * Created by tangjiaqi on 2018/5/17.
 */
public class ClientStarter {

    private static Channel channel;

    public static void main(String[] args){
        final EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MyProtocolEncoder());
                        ch.pipeline().addLast(new MyProtocolDecoder());
                        ch.pipeline().addLast(new ClientTestHandle());
                    }
                });

        final ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 12345);

        channel = channelFuture.channel();

        channel.closeFuture().addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                eventLoopGroup.shutdownGracefully();
                System.exit(0);
            }
        });

        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws IOException {
                if (future.isSuccess()){
                    System.out.println(String.format("connect server: %s:%d success", "127.0.0.1", 12345));
                }else {
                    String msg = String.format("connect server: %s:%d failed", "127.0.0.1", 12345);
                    System.out.println(msg);
                    System.exit(-1);
                }
            }
        });
    }
}
