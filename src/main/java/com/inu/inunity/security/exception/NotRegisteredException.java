package com.inu.inunity.security.exception;

public class NotRegisteredException extends RuntimeException {
    public NotRegisteredException(ExceptionMessage m){
        super(m.getMessage());
    }
}
