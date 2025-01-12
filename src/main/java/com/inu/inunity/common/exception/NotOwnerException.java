package com.inu.inunity.common.exception;

public class NotOwnerException extends RuntimeException{
    public NotOwnerException(ExceptionMessage m){
        super(m.getMessage());
    }
}
