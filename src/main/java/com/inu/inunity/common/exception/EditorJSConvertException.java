package com.inu.inunity.common.exception;

public class EditorJSConvertException extends RuntimeException{
    public EditorJSConvertException(ExceptionMessage m){
        super(m.getMessage());
    }
}
