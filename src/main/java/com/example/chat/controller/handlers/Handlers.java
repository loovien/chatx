package com.example.chat.controller.handlers;

import com.example.chat.controller.ChatController;
import com.example.chat.controller.UserController;
import lombok.Getter;

@Getter
public enum Handlers {
    HANDLER_LOGIN(1, UserController.class),

    HANDLER_CHAT(2, ChatController.class);


    private final int biz;

    private final Class<? extends Handler> handlerClass;

    Handlers(int biz, Class<? extends Handler> handlerClass) {
        this.biz = biz;
        this.handlerClass = handlerClass;
    }

}
