package com.epam.gymsystem.common;

public class AccessDeniedRuntimeException extends RuntimeException {
    public AccessDeniedRuntimeException(String message) {
        super(message);
    }
}
