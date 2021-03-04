package com.example.chat.controller;

import com.example.chat.controller.handlers.Handler;
import com.example.chat.dto.BizDTO;
import io.netty.channel.ChannelHandlerContext;

public class ChatController implements Handler {
    @Override
    public String handler(ChannelHandlerContext ctx, BizDTO bizDTO) throws Exception {
        return null;
    }
}
