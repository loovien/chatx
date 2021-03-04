package com.example.chat.controller.handlers;

import com.example.chat.dto.BizDTO;
import io.netty.channel.ChannelHandlerContext;

public interface Handler {
    String handler(ChannelHandlerContext ctx, BizDTO bizDTO) throws Exception;
}
