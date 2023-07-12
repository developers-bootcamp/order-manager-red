package com.sapred.ordermanagerred.exception;

public class ObjectAlreadyExists extends RuntimeException {
    public ObjectAlreadyExists(String errorMessage) {
        super(errorMessage);
    }
}
