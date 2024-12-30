package com.inu.inunity.common.exception;

public class NotRegisteredException extends RuntimeException {
    public NotRegisteredException(ExceptionMessage m){
        super(m.getMessage());
    }
}
