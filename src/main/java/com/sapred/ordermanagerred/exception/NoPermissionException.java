package com.sapred.ordermanagerred.exception;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException(String errorMessage) {
        super(errorMessage);
    }
}
