package com.example.chat.controller;

import com.example.chat.configs.Json;
import com.example.chat.dto.AuthDTO;
import com.example.chat.dto.BizDTO;
import com.example.chat.ChatInitializr;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractController {

    protected final ChatInitializr initializr;

    protected final ObjectMapper objectMapper = Json.getMapper();

    public AbstractController(ChatInitializr initializr) {
        this.initializr = initializr;
    }

    public void broadcast(BizDTO bizDTO) {
        initializr.getChannels().writeAndFlush(bizDTO);
    }

    public void send(Integer uid) {
        AuthDTO authDTO = initializr.getUserSocket().get(uid);
    }
}
