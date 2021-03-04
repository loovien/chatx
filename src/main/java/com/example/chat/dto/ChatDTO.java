package com.example.chat.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public class ChatDTO {

    private String message;

    private Date chatAt;

    private Boolean broadcast;

    private UserDTO to;

    private UserDTO from;
}
