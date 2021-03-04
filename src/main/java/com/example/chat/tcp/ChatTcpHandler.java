package com.example.chat.tcp;

import com.example.chat.dto.BizDTO;
import com.example.chat.exception.BizException;
import com.example.chat.handlers.Handlers;
import com.example.chat.server.ChatInitializr;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ChatTcpHandler extends SimpleChannelInboundHandler<BizDTO> {
    private final ChatInitializr chatInitializr;

    public ChatTcpHandler(ChatInitializr chatInitializr) {
        this.chatInitializr = chatInitializr;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("client address: {} connected channel id: {}", channel.remoteAddress().toString(), channel.id());
        chatInitializr.getChannels().add(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BizDTO msg) throws Exception {
        String result = null;
        try {
            if (msg.getBiz() == Handlers.HANDLER_LOGIN.getBiz()) {
                result = Handlers.HANDLER_LOGIN.getHandlerClass()
                        .getConstructor(ChatInitializr.class)
                        .newInstance(chatInitializr).handler(msg);
                msg.setBody(result.getBytes(StandardCharsets.UTF_8));
                ctx.writeAndFlush(msg);
                return;
            }
            Handlers[] handlers = Handlers.values();
            Handlers bizHandlerClazz = null;
            for (Handlers handler : handlers) {
                if (msg.getBiz() == handler.getBiz()) {
                    bizHandlerClazz = handler;
                    break;
                }
            }
            if (bizHandlerClazz == null) {
                log.error("handler: {} not implemented", msg);
                ctx.channel().close();
                return;
            }
            result = bizHandlerClazz.getHandlerClass()
                    .getConstructor(ChatInitializr.class)
                    .newInstance(chatInitializr).handler(msg);
        } catch (BizException bizException) {
            log.error("biz handler failure: {}", bizException.getMessage());
        } catch (Exception exception) {
            ctx.channel().close();
            log.error("fatal exception: {}", exception.getMessage());
            throw exception;
        }
        if (result == null || result.equals("")) {
            return;
        }
        msg.setBody(result.getBytes(StandardCharsets.UTF_8));
        log.info("send response to channel: {}", msg);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception: {}", cause.getMessage());
        cause.printStackTrace();
        ctx.channel().close();
    }
}