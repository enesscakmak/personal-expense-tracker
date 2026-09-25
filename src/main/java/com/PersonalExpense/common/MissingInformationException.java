package com.PersonalExpense.common;

public class MissingInformationException extends RuntimeException{
    public MissingInformationException(String message){
        super(message);
    }
}
