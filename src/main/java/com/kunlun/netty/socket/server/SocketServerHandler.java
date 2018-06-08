package com.kunlun.netty.socket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * TODO:
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/8 上午12:57
 */
public class SocketServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("远程地址" + ctx.channel().remoteAddress());
        System.out.println("接收到的消息：" + msg);
        //回写给客户端的数据应该在read0方法内部写

        ctx.writeAndFlush("from server" + UUID.randomUUID());
    }

    /**
     * 重写异常  如果出现异常 一般会把连接关闭掉
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
