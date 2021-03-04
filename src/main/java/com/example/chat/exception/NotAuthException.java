package com.example.chat.exception;


public class NotAuthException extends BizException {

    public NotAuthException() {
        this.code = -1000;
        this.message = "authorization required!";
    }
}
