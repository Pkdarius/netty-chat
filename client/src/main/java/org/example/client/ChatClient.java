package org.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.example.dto.Request;
import org.example.handler.ClientHandler;

public class ChatClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8007;
    private final ClientHandler clientHandler;
    private ChannelFuture future;
    private EventLoopGroup group;

    public ChatClient(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setup() throws Exception {
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        pipeline.addLast(new ObjectEncoder(),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                clientHandler);
                    }
                });
        future = bootstrap.connect(HOST, PORT).sync();
    }

    public void login(String username) throws InterruptedException {
        future.sync().channel().writeAndFlush(
                Request.builder()
                        .type(Request.MessageType.LOGIN)
                        .from(username)
                        .build());
    }

    public void sendMessage(String from, String to, String message) throws Exception {
        future.sync().channel().writeAndFlush(
                Request.builder()
                        .type(Request.MessageType.CHAT)
                        .from(from)
                        .to(to)
                        .message(message)
                        .build()
        );
    }

    public void getHistoryMessages(String from, String to) throws Exception {
        future.sync().channel().writeAndFlush(
                Request.builder()
                        .type(Request.MessageType.GET_MESSAGE)
                        .from(from)
                        .to(to)
                        .build()
        );
    }

    public void tearDown() {
        try {
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
