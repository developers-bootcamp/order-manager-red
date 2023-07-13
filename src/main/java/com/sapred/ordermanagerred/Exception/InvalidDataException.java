package com.sapred.ordermanagerred.exception;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String massage){
        super(massage);
    }
}
