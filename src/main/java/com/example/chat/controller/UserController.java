package com.example.chat.controller;

import com.example.chat.comm.Result;
import com.example.chat.controller.handlers.Handler;
import com.example.chat.dto.BizDTO;
import com.example.chat.dto.UserDTO;
import com.example.chat.exception.NotAuthException;
import com.example.chat.ChatInitializr;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@ChannelHandler.Sharable
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
        String socketId = channel.id().asLongText();
        channel.attr(AttributeKey.valueOf(socketId)).set(userDTO);
        initializr.getChannels().add(channel);
        return Result.wrapOkResult(null);
    }
}
