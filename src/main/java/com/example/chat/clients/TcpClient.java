package com.example.chat.clients;

import com.example.chat.controller.handlers.Handlers;
import com.example.chat.dto.BizDTO;
import com.example.chat.tcp.ChatTcpDecoder;
import com.example.chat.tcp.ChatTcpEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TcpClient {

    public static class TcpClientHandler extends SimpleChannelInboundHandler<BizDTO> {

        private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            byte[] body = "{\"name\":\"luowen\",\"uid\": 1,\"token\":\"admin\"}".getBytes(StandardCharsets.UTF_8);
            BizDTO bizDto = BizDTO.builder().length(body.length).biz(1).version(0).body(body).build();
            log.info("send to server: {}", new String(body));
            ctx.writeAndFlush(bizDto);
            // every five second send hi message
            executorService.scheduleWithFixedDelay(() -> {
                log.info("==========> send 'hello chatx!' to server.");
//                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                byte[] chatMsg = "{\"message\":\"luowen\", \"chatAt\":\"2021-03-05 15:32:00\"}"
                        .getBytes(StandardCharsets.UTF_8);
                BizDTO chatDto = BizDTO.builder()
                        .length(chatMsg.length)
                        .biz(Handlers.HANDLER_CHAT.getBiz())
                        .version(0)
                        .body(chatMsg)
                        .build();
                ctx.writeAndFlush(chatDto);
            }, 0, 5, TimeUnit.SECONDS);
        }


        @Override
        protected void channelRead0(ChannelHandlerContext ctx, BizDTO msg) throws Exception {
            log.info("receive: length: {}, biz: {}, version: {}, data: {}", msg.getLength(), msg.getBiz(),
                    msg.getVersion(), new String(msg.getBody()));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
            executorService.shutdown();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        TcpClientHandler tcpClientHandler = new TcpClientHandler();
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(2);
        Bootstrap bootstrap = new Bootstrap().group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ChatTcpDecoder())
                                .addLast(new ChatTcpEncoder())
                                .addLast(tcpClientHandler);
                    }
                }).option(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture localhost = bootstrap.connect("0.0.0.0", 8000).sync();
        localhost.channel().closeFuture().sync();
        eventExecutors.shutdownGracefully();
    }

}
