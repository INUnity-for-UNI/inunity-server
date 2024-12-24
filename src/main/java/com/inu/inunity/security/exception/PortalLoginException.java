package com.inu.inunity.security.exception;

public class PortalLoginException extends RuntimeException {
    public PortalLoginException(ExceptionMessage m){
        super(m.getMessage());
    }
}
