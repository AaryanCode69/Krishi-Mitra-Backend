package com.example.krishimitrabackend.exception;


public class RateLimitException extends RuntimeException {
    public RateLimitException(String message) {
        super(message);
    }
}
