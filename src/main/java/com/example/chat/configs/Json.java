package com.example.chat.configs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class Json {
    private final static ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

    public static ObjectMapper getMapper() {
        return objectMapper;
    }


}
