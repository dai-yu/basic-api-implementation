package com.thoughtworks.rslist.exception;

public class BaseException extends RuntimeException{
    private String message;

    public BaseException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
