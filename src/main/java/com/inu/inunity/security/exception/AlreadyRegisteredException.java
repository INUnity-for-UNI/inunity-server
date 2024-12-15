package com.inu.inunity.security.exception;

public class AlreadyRegisteredException extends RuntimeException {
    public AlreadyRegisteredException(ExceptionMessage m){
        super(m.getMessage());
    }
}
