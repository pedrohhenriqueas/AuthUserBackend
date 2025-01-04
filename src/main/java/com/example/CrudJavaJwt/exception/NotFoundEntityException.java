package com.example.CrudJavaJwt.exception;

public class NotFoundEntityException extends RuntimeException{
    public NotFoundEntityException(String msg) {
        super(msg);
    }
}
