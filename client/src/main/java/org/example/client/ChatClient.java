package org.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class ChatClient {

    static final String HOST = "127.0.0.1";
    static final int PORT = 8007;
    static String clientName;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        if (scanner.hasNext()) {
            clientName = scanner.nextLine();
            System.out.println("Welcome " + clientName);
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());

                            pipeline.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(HOST, PORT).sync();

            while (scanner.hasNext()) {
                String input = scanner.nextLine();
                Channel channel = future.sync().channel();
                channel.writeAndFlush("[" + clientName + "]: " + input);
                channel.flush();
            }

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
