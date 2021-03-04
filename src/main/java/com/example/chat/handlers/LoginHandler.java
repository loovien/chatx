package com.example.chat.handlers;

import com.example.chat.comm.Result;
import com.example.chat.dto.BizDTO;
import com.example.chat.dto.UserDTO;
import com.example.chat.server.ChatInitializr;

public class LoginHandler extends AbstractHandler implements Handler {
    public LoginHandler(ChatInitializr initializr) {
        super(initializr);
    }

    @Override
    public String handler(BizDTO bizDTO) throws Exception {
        return Result.wrapOkResult(null);
        /*UserDTO userDTO = objectMapper.readerFor(UserDTO.class).readValue(bizDTO.getBody(), UserDTO.class);
        if (userDTO.getToken().equals("admin")) {
            return Result.wrapOkResult(null);
        }
        throw new Exception("Not Authorization!");*/
    }
}
