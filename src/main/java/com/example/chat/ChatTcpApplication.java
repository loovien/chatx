package com.example.chat;

import com.example.chat.tcp.ChatTcpDecoder;
import com.example.chat.tcp.ChatTcpEncoder;
import com.example.chat.tcp.ChatTcpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 50)
@Component
public class ChatTcpApplication implements ApplicationRunner {

    private final ChatInitializr chatInitializr;

    private final ChatTcpHandler chatTcpHandler;

    @Autowired
    public ChatTcpApplication(ChatInitializr chatInitializr, ChatTcpHandler chatTcpHandler) {
        this.chatInitializr = chatInitializr;
        this.chatTcpHandler = chatTcpHandler;
    }

    @Override
    public void run(ApplicationArguments args) {
        new Thread(this::boot).start();
    }

    private void boot() {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(chatInitializr.getMaster(), chatInitializr.getWorker())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline();
                        channelPipeline.addLast(new ChatTcpDecoder());
                        channelPipeline.addLast(new ChatTcpEncoder());
                        channelPipeline.addLast(chatTcpHandler);
                    }
                }).option(ChannelOption.SO_BACKLOG, 1024 * 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        String addr = chatInitializr.getTcpOps().getAddr();
        Integer port = chatInitializr.getTcpOps().getPort();
        log.info("tcp server listen at: {}：{}", addr, port);
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(addr, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("receive interrupter signal: {}", e.getMessage());
        } finally {
            chatInitializr.getMaster().shutdownGracefully();
            chatInitializr.getWorker().shutdownGracefully();
        }
    }
}
