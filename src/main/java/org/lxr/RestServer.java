package org.lxr;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class RestServer
{
    private final int port;

    public RestServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
        if(args.length!=1){
            System.err.println("Usage: "+ RestServer.class.getSimpleName()+"<port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        System.out.println("Echo Server start! port:"+port);
        new RestServer(port).start();

    }

    private void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(65535));
                            //ch.pipeline().addLast(new HttpRequestEncoder());
                            ch.pipeline().addLast(new HttpServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            group.shutdownGracefully().sync();
        }
    }
}
