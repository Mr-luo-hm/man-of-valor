package com.luoyingjie.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;

/**
 * @author lyj
 */
public class NettyClientFilter extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline entries = socketChannel.pipeline();
        entries.addLast(new IdleStateHandler(0,4,0,TimeUnit.SECONDS));
        entries.addLast("decoder",new StringDecoder());
        entries.addLast("encoder",new StringEncoder());
        entries.addLast("handler",new NettyClientHandler());
    }
}
