package com.example.chat.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties(WsOpts.class)
@ConfigurationProperties(prefix = "chat.server.ws")
public class WsOpts {

    private String addr;

    private Integer port;

    private String path;

}
