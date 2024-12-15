package com.inu.inunity.security.exception;

public class NotInformationMajorException extends RuntimeException {
    public NotInformationMajorException(ExceptionMessage m){
        super(m.getMessage());
    }
}
