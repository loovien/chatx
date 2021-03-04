package com.example.chat.controller;

import com.example.chat.configs.Json;
import com.example.chat.controller.handlers.Handlers;
import com.example.chat.dto.BizDTO;
import com.example.chat.ChatInitializr;
import com.example.chat.dto.ChatDTO;
import com.example.chat.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.DefaultChannelId;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class AbstractController {

    protected final ChatInitializr initializr;

    protected final ObjectMapper objectMapper = Json.getMapper();

    public AbstractController(ChatInitializr initializr) {
        this.initializr = initializr;
    }

    public void broadcast(BizDTO bizDTO) {
        initializr.getChannels().writeAndFlush(bizDTO);
    }

}
