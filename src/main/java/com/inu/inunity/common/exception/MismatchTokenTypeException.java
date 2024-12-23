package com.inu.inunity.common.exception;

public class MismatchTokenTypeException extends RuntimeException {
    public MismatchTokenTypeException(ExceptionMessage m) {
        super(m.getMessage());
    }
}

