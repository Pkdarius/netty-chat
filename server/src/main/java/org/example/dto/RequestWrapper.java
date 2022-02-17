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
        if (this.request.getTime() - o.request.getTime() > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "RequestWrapper{}";
    }
}
