package com.example.chat.clients;

import com.example.chat.dto.BizDTO;
import com.example.chat.tcp.ChatTcpDecoder;
import com.example.chat.tcp.ChatTcpEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class TcpClient {

    public static class TcpClientHandler extends SimpleChannelInboundHandler<BizDTO> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            byte[] body = "{\"name\":\"luowen\",\"uid\": 1,\"token\":\"admin\"}".getBytes(StandardCharsets.UTF_8);
            BizDTO bizDto = BizDTO.builder().length(body.length).biz(1).version(0).body(body).build();
            log.info("send to server: {}", new String(body));
            ctx.writeAndFlush(bizDto);
            super.channelActive(ctx);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, BizDTO msg) throws Exception {
            log.info("receive: length: {}, biz: {}, version: {}, data: {}", msg.getLength(), msg.getBiz(),
                    msg.getVersion(), new String(msg.getBody()));
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap().group(new NioEventLoopGroup(1))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ChatTcpDecoder())
                                .addLast(new ChatTcpEncoder())
                                .addLast(new TcpClientHandler());
                    }
                }).option(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture localhost = bootstrap.connect("0.0.0.0", 8000).sync();
        localhost.channel().closeFuture();
    }

}
