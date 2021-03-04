package com.example.chat.handlers;

import com.example.chat.dto.BizDTO;

public interface Handler {
    String handler(BizDTO bizDTO) throws Exception;
}
