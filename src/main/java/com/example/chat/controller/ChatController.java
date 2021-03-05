package com.example.chat.controller;

import com.example.chat.ChatInitializr;
import com.example.chat.comm.Result;
import com.example.chat.controller.handlers.Handler;
import com.example.chat.controller.handlers.Handlers;
import com.example.chat.dto.BizDTO;
import com.example.chat.dto.ChatDTO;
import com.example.chat.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
@ChannelHandler.Sharable
public class ChatController extends AbstractController implements Handler {

    public ChatController(ChatInitializr initializr) {
        super(initializr);
    }

    @Override
    public String handler(ChannelHandlerContext ctx, BizDTO bizDTO) throws Exception {
        try {
            ChatDTO chatDTO = objectMapper.readValue(bizDTO.getBody(), ChatDTO.class);
            log.info("receive chat message: {}, broadcast to: {}", chatDTO, initializr.getChannels().size());
            broadcast(bizDTO);
        } catch (IOException e) {
            log.error("channel receive data error: {}", new String(bizDTO.getBody()));
        }
        return Result.wrapEmptyResult();
    }


    public void send(ChatDTO chatDTO) {
        initializr.getChannels().parallelStream().filter((channel) -> {
            UserDTO userDTO = (UserDTO) channel.attr(AttributeKey.valueOf(channel.id().asLongText())).get();
            return userDTO != null && userDTO.getUid().equals(chatDTO.getTo().getUid());
        }).forEach((channel) -> {
            try {
                byte[] body = objectMapper.writeValueAsString(chatDTO).getBytes(StandardCharsets.UTF_8);
                BizDTO bizDTO = BizDTO.builder()
                        .length(body.length)
                        .biz(Handlers.HANDLER_CHAT.getBiz())
                        .version(0)
                        .body(body)
                        .build();
                channel.writeAndFlush(bizDTO);
            } catch (JsonProcessingException e) {
                log.error("serialize object: {} failure, err: {}", chatDTO, e.getMessage());
            }
        });

    }
}
