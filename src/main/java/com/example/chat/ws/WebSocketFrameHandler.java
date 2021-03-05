package com.example.chat.ws;

import com.example.chat.ChatInitializr;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final ChatInitializr chatInitializr;

    public WebSocketFrameHandler(ChatInitializr chatInitializr) {
        this.chatInitializr = chatInitializr;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("websocket channel add in ChannelGroup: {}", ctx.channel().id().asLongText());
        chatInitializr.getChannels().add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        if (msg instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) msg).text();
            log.info("receive from text websocket: {}", text);
            ctx.writeAndFlush(new TextWebSocketFrame(text.toUpperCase()));
            return;
        }
        if (msg instanceof BinaryWebSocketFrame) {
            ByteBuf byteBuf = msg.content();
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            String result = new String(bytes).toUpperCase();
            log.info("receive from binary websocket: {}", result);
            return;
        }
        String message = "unsupported frame type: " + msg.getClass().getName();
        throw new UnsupportedOperationException(message);
    }

}
