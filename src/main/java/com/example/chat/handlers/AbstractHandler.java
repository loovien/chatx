package com.example.chat.handlers;

import com.example.chat.configs.Json;
import com.example.chat.server.ChatInitializr;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractHandler {

    protected final ChatInitializr initializr;

    protected final ObjectMapper objectMapper = Json.getMapper();

    public AbstractHandler(ChatInitializr initializr) {
        this.initializr = initializr;
    }
}
