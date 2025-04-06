package com.example.auth_user_backend.exception;

public class NotFoundEntityException extends RuntimeException{
    public NotFoundEntityException(String msg) {
        super(msg);
    }
}
