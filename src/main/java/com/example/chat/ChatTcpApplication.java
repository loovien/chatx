package com.example.chat;

import com.example.chat.tcp.ChatTcpDecoder;
import com.example.chat.tcp.ChatTcpEncoder;
import com.example.chat.tcp.ChatTcpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
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
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(chatInitializr.getMaster(), chatInitializr.getWorker())
                .channel(NioServerSocketChannel.class)
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
