package com.example.chat.comm;

import com.example.chat.configs.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class Result<T> {

    private int code;

    private String message;

    private T data;

    private final static ObjectMapper objectMapper = Json.getMapper();

    public static <T> String wrapOkResult(T data) {
        try {
            return objectMapper.writeValueAsString(new Result<>(0, "success", data));
        } catch (JsonProcessingException e) {
            log.error("json serialize data: {} failure: {}", data, e);
        }
        return null;
    }

    public static <T> String wrapErrResult(int code, String message, T data) {
        try {
            return objectMapper.writeValueAsString(new Result<>(code, message, data));
        } catch (JsonProcessingException e) {
            log.error("json serialize data: {} failure: {}", data, e);
        }
        return null;
    }

    public static <T> String wrapEmptyResult() {
        return "";
    }

}
