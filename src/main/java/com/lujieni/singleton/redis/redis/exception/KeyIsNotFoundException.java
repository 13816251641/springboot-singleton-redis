package com.lujieni.singleton.redis.redis.exception;

public class KeyIsNotFoundException extends RuntimeException {
    public KeyIsNotFoundException(String message) {
        super(message);
    }

}