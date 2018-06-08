package com.kunlun.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * TODO:
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/8 下午10:07
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 利用ChannelGroup去保存Channel
     * <p>
     * GlobalEventExecutor 单线程  单例的Event实例  自动启动线程 如果超过1秒钟没有线程启动就会挂起
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            //如果channel不等于ch  则正常收到消息通知
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + "发送的消息:" + msg);
            } else {
                ch.writeAndFlush("[自己] -" + msg + "\n");
            }
        });
    }


    /**
     * 连接建立时处理
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //对所有channel进行广播
        channelGroup.writeAndFlush("[服务器] -" + channel.remoteAddress() + "加入\n");
        //保存当前channel
        channelGroup.add(channel);
        super.handlerAdded(ctx);
    }

    /**
     * 连接断掉处理
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[服务器] -" + channel.remoteAddress() + "离开\n");
        //即使不手动移除channel  Netty会自动移除断掉的channel
        //channelGroup.remove(channel);
        super.handlerRemoved(ctx);
    }

    /**
     * 连接处于活动状态
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "上线了 ");
        super.channelActive(ctx);
    }

    /**
     * 处于不活动状态
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "下线了");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
