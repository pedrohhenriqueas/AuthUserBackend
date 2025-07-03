package com.example.auth_user_backend.exception;

public class NotFoundEntityException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundEntityException(String msg) {
        super(msg);
    }
}
