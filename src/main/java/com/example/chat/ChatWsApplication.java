package com.example.chat;

import com.example.chat.ws.Tcp2WsEncoder;
import com.example.chat.ws.WebSocketFrameHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@Component
public class ChatWsApplication implements ApplicationRunner {

    final private ChatInitializr chatInitializr;

    final private WebSocketFrameHandler webSocketFrameHandler;

    public ChatWsApplication(ChatInitializr chatInitializr, WebSocketFrameHandler webSocketFrameHandler) {
        this.chatInitializr = chatInitializr;
        this.webSocketFrameHandler = webSocketFrameHandler;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(this::bootstrap).start();
    }

    public void bootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(chatInitializr.getMaster(), chatInitializr.getWorker())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(1048576));
                        pipeline.addLast(new WebSocketServerProtocolHandler(chatInitializr.getWsOpts().getPath(),
                                null, true));
                        pipeline.addLast(new Tcp2WsEncoder());
                        pipeline.addLast(webSocketFrameHandler);
                    }
                }).option(ChannelOption.SO_BACKLOG, 1000 * 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        String addr = chatInitializr.getWsOpts().getAddr();
        Integer port = chatInitializr.getWsOpts().getPort();
        log.info("chat server ws listen at {}:{}", addr, port);
        ChannelFuture channelFuture = null;
        try {
            channelFuture = serverBootstrap.bind(addr, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("chat server ws exception: {}", Arrays.toString(e.getStackTrace()));
        } finally {
            chatInitializr.getMaster().shutdownGracefully();
            chatInitializr.getWorker().shutdownGracefully();
        }
    }
}
