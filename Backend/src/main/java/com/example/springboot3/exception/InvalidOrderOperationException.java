package com.example.springboot3.exception;

public class InvalidOrderOperationException extends RuntimeException {
    public InvalidOrderOperationException(String message) {
        super(message);
    }
}
