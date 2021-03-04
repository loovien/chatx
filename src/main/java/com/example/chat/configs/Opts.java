package com.example.chat.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties(Opts.class)
@ConfigurationProperties(prefix = "chat.application")
public class Opts {

    private Integer masterThreadNum;

    private Integer workThreadNum;

}
