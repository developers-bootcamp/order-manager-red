package com.sapred.ordermanagerred.exception;

public class ObjectDoesNotExistException  extends RuntimeException{
    public ObjectDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }

}
