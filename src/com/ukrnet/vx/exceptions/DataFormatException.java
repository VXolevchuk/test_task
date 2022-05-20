package com.ukrnet.vx.exceptions;

public class DataFormatException extends Exception{
    private String message;

    public DataFormatException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
