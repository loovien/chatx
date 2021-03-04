package com.example.chat.tcp;

import com.example.chat.dto.BizDTO;
import com.example.chat.exception.BizException;
import com.example.chat.exception.NotAuthException;
import com.example.chat.controller.handlers.Handlers;
import com.example.chat.ChatInitializr;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
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
        Handlers bizHandlerClazz = null;
        byte[] resultBytes = null;
        Channel channel = ctx.channel();
        try {
            if (msg.getBiz() == Handlers.HANDLER_LOGIN.getBiz()) { // first authority
                resultBytes = Handlers.HANDLER_LOGIN.getHandlerClass()
                        .getConstructor(ChatInitializr.class)
                        .newInstance(chatInitializr).handler(ctx, msg)
                        .getBytes(StandardCharsets.UTF_8);

                if (resultBytes.length <= 0) {
                    msg.setLength(resultBytes.length);
                    msg.setBody(resultBytes);
                    ctx.writeAndFlush(msg);
                }
                return;
            }
            if (!channel.hasAttr(AttributeKey.valueOf(channel.id().asLongText()))) {
                log.error("channe: {} communicate without authorization information", channel.id().asLongText());
                throw new NotAuthException();
            }
            Handlers[] handlers = Handlers.values();
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
            resultBytes = bizHandlerClazz.getHandlerClass()
                    .getConstructor(ChatInitializr.class)
                    .newInstance(chatInitializr).handler(ctx, msg)
                    .getBytes(StandardCharsets.UTF_8);
        } catch (NotAuthException exception) {
            log.error("communicate required auth first. err: {}", exception.getMessage());
            ctx.close(); // close channel
        } catch (BizException bizException) {
            log.error("biz handler failure: {}", bizException.getMessage());
        } catch (Exception exception) {
            ctx.channel().close();
            log.error("fatal exception: {}", exception.getMessage());
            throw exception;
        }
        if (resultBytes == null || resultBytes.length <= 0) {
            log.info("handler: {} return empty.", bizHandlerClazz);
            return;
        }
        msg.setLength(resultBytes.length);
        msg.setBody(resultBytes);
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
