package com.inu.inunity.security.exception;

public class UserRegisteredException extends RuntimeException {
    public UserRegisteredException(ExceptionMessage m) {
        super(m.getMessage());
    }
}
