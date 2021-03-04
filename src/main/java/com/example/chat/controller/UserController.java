package com.example.chat.controller;

import com.example.chat.comm.Result;
import com.example.chat.controller.handlers.Handler;
import com.example.chat.dto.BizDTO;
import com.example.chat.dto.UserDTO;
import com.example.chat.exception.NotAuthException;
import com.example.chat.ChatInitializr;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserController extends AbstractController implements Handler {
    public UserController(ChatInitializr initializr) {
        super(initializr);
    }

    @Override
    public String handler(ChannelHandlerContext ctx, BizDTO bizDTO) throws Exception {
        UserDTO userDTO = objectMapper.readerFor(UserDTO.class).readValue(bizDTO.getBody(), UserDTO.class);
        if (!userDTO.getToken().equals("admin")) {
            log.error("user:{} can't not authorization.", userDTO);
            throw new NotAuthException();
        }
        Channel channel = ctx.channel();
        channel.attr(AttributeKey.valueOf(channel.id().asLongText())).set(userDTO);
        return Result.wrapOkResult(null);
    }
}
