package com.example.chat.dto;

import io.netty.handler.codec.string.StringEncoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String name;

    private Integer uid;

    private String token;

}
