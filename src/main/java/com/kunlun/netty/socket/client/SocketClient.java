package com.kunlun.netty.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * TODO:客户端只需要一个事件循环组
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/8 上午1:03
 */
public class SocketClient {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            //服务器端为childHandler 客户端为 handler
            //其实 服务器也可以使用handler 和childHandler
            //服务端如果使用handler 则由boosGroup处理  否则交给workGroup处理
            bootstrap.group(clientGroup).channel(NioSocketChannel.class).handler(new SocketClientInitializer());
            //进行连接
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            clientGroup.shutdownGracefully();
        }

    }
}
