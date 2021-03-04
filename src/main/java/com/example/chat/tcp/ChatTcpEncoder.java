package com.example.chat.tcp;

import com.example.chat.dto.BizDTO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ChatTcpEncoder extends MessageToMessageEncoder<BizDTO> {
    @Override
    protected void encode(ChannelHandlerContext ctx, BizDTO msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer(Protocol.getLength() + msg.getBody().length);
        buffer.writeInt(msg.getLength());
        buffer.writeInt(msg.getBiz());
        buffer.writeInt(msg.getVersion());
        buffer.writeBytes(msg.getBody());
        log.info("encode message to dist: {}, length: {} biz:{} version: {} data:{}",
                ctx.channel().remoteAddress().toString(), msg.getLength(), msg.getBiz(),
                msg.getVersion(), new String(msg.getBody()));
        out.add(buffer);
    }
}
