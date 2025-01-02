package com.inu.inunity.common.exception;

public class NotFoundElementException extends RuntimeException {
    public NotFoundElementException(ExceptionMessage m) {
        super(m.getMessage());
    }
}
