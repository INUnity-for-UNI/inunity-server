package com.inu.inunity.security.exception;

public class NotSchoolEmailException extends RuntimeException{
    public NotSchoolEmailException(ExceptionMessage m) {
        super(m.getMessage());
    }
}
