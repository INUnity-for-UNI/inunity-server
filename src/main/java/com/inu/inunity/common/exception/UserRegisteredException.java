package com.inu.inunity.common.exception;

public class UserRegisteredException extends RuntimeException {
    public UserRegisteredException(ExceptionMessage m) {
        super(m.getMessage());
    }
}
