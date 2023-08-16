package com.sapred.ordermanagerred.Exception;

public class ObjectDoesNotExistException  extends RuntimeException {
    public ObjectDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }
}