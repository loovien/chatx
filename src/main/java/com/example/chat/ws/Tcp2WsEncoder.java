package com.example.chat.ws;

import com.example.chat.dto.BizDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class Tcp2WsEncoder extends MessageToMessageEncoder<BizDTO> {
    @Override
    protected void encode(ChannelHandlerContext ctx, BizDTO msg, List<Object> out) throws Exception {
        log.info("Tcp2WS:>>>>>>>>>>>>>>>>>: {}", new String(msg.getBody()));
        out.add(new TextWebSocketFrame(new String(msg.getBody(), StandardCharsets.UTF_8)));
    }
}
