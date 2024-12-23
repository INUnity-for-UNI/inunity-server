package com.inu.inunity.common.exception;

public class PortalLoginException extends RuntimeException {
    public PortalLoginException(ExceptionMessage m){
        super(m.getMessage());
    }
}
