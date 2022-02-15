package org.example.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.dto.RequestWrapper;
import org.example.queue.MessageQueue;

import java.util.Map;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private final Map<String, Channel> channels;
    private final MessageQueue messageQueue;

    public ServerHandler() {
        channels = Channels.getChannels();
        messageQueue = MessageQueue.getInstance();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) {
        var requestWrapper = new RequestWrapper(ctx, object);
        messageQueue.put(requestWrapper);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Closing connection for client - " + ctx);
        ctx.close();
        for (var entry : channels.entrySet()) {
            if (entry.getValue() == ctx.channel()) {
                channels.remove(entry.getKey());
                break;
            }
        }
    }
}
