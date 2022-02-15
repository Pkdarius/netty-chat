package org.example.queue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.example.dto.Request;
import org.example.dto.RequestWrapper;
import org.example.dto.Response;
import org.example.handler.Channels;
import org.example.model.Message;
import org.example.repository.MessageRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class RequestHandler implements Runnable {
    private final Map<String, Channel> channels;
    private final MessageRepository messageRepository;
    private final RequestWrapper requestWrapper;

    public RequestHandler(RequestWrapper requestWrapper) {
        channels = Channels.getChannels();
        messageRepository = MessageRepository.getInstance();
        this.requestWrapper = requestWrapper;
    }

    @Override
    public void run() {
        var ctx = requestWrapper.getContext();
        var request = requestWrapper.getRequest();
        switch (request.getType()) {
            case LOGIN -> handleLogin(ctx, request);
            case CHAT -> handleSendMessage(request);
            case GET_MESSAGE -> handleGetMessage(ctx, request);
        }
    }

    private void handleLogin(ChannelHandlerContext ctx, Request request) {
        var from = request.getFrom();
        var channel = channels.get(from);
        var status = Response.Status.FAILURE;
        if (channel == null) {
            channels.put(from, ctx.channel());
            status = Response.Status.SUCCESS;
        }

        var response = Response.builder()
                .type(Response.MessageType.LOGIN)
                .status(status)
                .build();
        ctx.writeAndFlush(response);
    }

    private void handleSendMessage(Request request) {
        String to = request.getTo();
        Channel toChannel = channels.get(to);

        if (toChannel != null) {
            var response = Response.builder()
                    .type(Response.MessageType.CHAT)
                    .from(request.getFrom())
                    .to(request.getTo())
                    .message(request.getMessage())
                    .build();
            toChannel.writeAndFlush(response);
            var message = Message.builder()
                    .frm(request.getFrom())
                    .to(request.getTo())
                    .content(request.getMessage())
                    .sentTime(Instant.now())
                    .build();
            messageRepository.saveMessage(message);
        }
    }

    private void handleGetMessage(ChannelHandlerContext ctx, Request request) {
        String from = request.getFrom();
        String to = request.getTo();

        List<Message> history = messageRepository.getAllMessages(from, to);
        ctx.writeAndFlush(Response.builder()
                .type(Response.MessageType.GET_MESSAGE)
                .messages(history)
                .build());
    }
}
