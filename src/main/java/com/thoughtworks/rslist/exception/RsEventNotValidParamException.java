package com.thoughtworks.rslist.exception;

public class RsEventNotValidParamException extends RuntimeException{
    private String message;

    public RsEventNotValidParamException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
