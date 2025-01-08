package com.inu.inunity.common.exception;

public class ImageServerException extends RuntimeException{
    public ImageServerException(ExceptionMessage m){
        super(m.getMessage());
    }
}
