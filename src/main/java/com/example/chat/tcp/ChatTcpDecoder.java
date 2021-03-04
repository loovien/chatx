package com.example.chat.tcp;

import com.example.chat.dto.BizDTO;
import com.example.chat.exception.BizException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ChatTcpDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < Protocol.getLength()) {
            return;
        }
        String from = ctx.channel().remoteAddress().toString();
        BizDTO bizDTO = BizDTO.builder()
                .length(in.readInt())
                .biz(in.readInt())
                .version(in.readInt()).build();
        if (bizDTO.getLength() <= 0) {
            log.error("receive from: {} packet invalid: {}", from, bizDTO);
            ctx.close();
            throw new BizException("packet invalid.");
        }
        if (in.readableBytes() < bizDTO.getLength()) {
            in.resetReaderIndex();
            return;
        }
        byte[] body = new byte[bizDTO.getLength()];
        in.readBytes(body);
        bizDTO.setBody(body);
        if (body.length <= 0) {
            log.error("receive from: {} packet invalid: {}", from, bizDTO);
            ctx.close();
            throw new BizException("packet invalid.");
        }
        log.info("receive from: {}, length:{} biz:{} version:{} body:{}", from,
                bizDTO.getLength(), bizDTO.getBiz(), bizDTO.getVersion(), new String(bizDTO.getBody()));
        out.add(bizDTO);
    }
}
