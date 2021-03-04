package com.example.chat.handlers;

import lombok.Getter;

@Getter
public enum Handlers {
    HANDLER_LOGIN(1, LoginHandler.class);

    private final int biz;

    private final Class<? extends Handler> handlerClass;

    Handlers(int biz, Class<? extends Handler> handlerClass) {
        this.biz = biz;
        this.handlerClass = handlerClass;
    }

}
