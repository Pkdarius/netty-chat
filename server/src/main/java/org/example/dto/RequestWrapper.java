package org.example.dto;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class RequestWrapper implements Comparable<RequestWrapper>  {
    private ChannelHandlerContext context;
    private Request request;

    public RequestWrapper(ChannelHandlerContext context, Object request) {
        this.request = (Request) request;
        this.context = context;
    }

    @Override
    public int compareTo(RequestWrapper o) {
        return 0;
    }
}
