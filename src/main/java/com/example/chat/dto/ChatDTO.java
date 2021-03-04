package com.example.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date chatAt;

    private Boolean broadcast;

    private UserDTO to;

    private UserDTO from;
}
