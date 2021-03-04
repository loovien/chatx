package com.example.chat;

import com.example.chat.configs.Json;
import com.example.chat.dto.ChatDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonMapperTest {

    @Test
    public void readValue() throws IOException {

        ObjectMapper mapper = Json.getMapper();
        byte[] body = "{\"message\":\"luowen\", \"chatAt\":\"2021-03-05 00:50:00\"}"
                .getBytes(StandardCharsets.UTF_8);
        ChatDTO chatDTO = mapper.readValue(body, ChatDTO.class);
        System.out.println(chatDTO);
    }
}
