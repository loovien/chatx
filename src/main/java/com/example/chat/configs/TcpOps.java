package com.example.chat.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "chat.server.tcp")
@EnableConfigurationProperties(TcpOps.class)
public class TcpOps {

    private String addr;

    private Integer port;
}
