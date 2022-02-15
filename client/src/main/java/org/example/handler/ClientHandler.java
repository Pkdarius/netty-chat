package org.example.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.dto.Response;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private Response response = null;
    private boolean isNewMessage = false;

    public Response getMessage() {
        isNewMessage = false;
        return response;
    }

    public boolean isNewMessage() {
        return isNewMessage;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) {
        isNewMessage = true;
        response = (Response) object;
    }
}
