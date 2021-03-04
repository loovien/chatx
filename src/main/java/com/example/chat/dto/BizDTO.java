package com.example.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BizDTO {

    private Integer length;

    private Integer biz;

    private Integer version;

    private byte[] body;
}
