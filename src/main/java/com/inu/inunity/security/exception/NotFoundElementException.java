package com.inu.inunity.security.exception;

public class NotFoundElementException extends RuntimeException {
    public NotFoundElementException(ExceptionMessage m) {
        super(m.getMessage());
    }
}
