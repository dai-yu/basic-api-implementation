package com.thoughtworks.rslist.exception;

public class RsEventNotVaildException extends RuntimeException{
    private String message;

    public RsEventNotVaildException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
