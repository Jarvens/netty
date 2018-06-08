package com.kunlun.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * TODO:
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/9 上午12:46
 */
public class WebSocketInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //http协议解码器
        pipeline.addLast(new HttpServerCodec());
        //ChunkedWriteHandler块
        pipeline.addLast(new ChunkedWriteHandler());
        //netty 是分段读取  HttpObjectAggregator主要作用是聚合  为完成的http request  或者 http response
        pipeline.addLast(new HttpObjectAggregator(8192));
        //服务器协议处理器
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(null);
    }
}
