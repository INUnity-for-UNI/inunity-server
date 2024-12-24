package com.inu.inunity.security.exception;

public class MismatchTokenTypeException extends RuntimeException {
    public MismatchTokenTypeException(ExceptionMessage m) {
        super(m.getMessage());
    }
}

