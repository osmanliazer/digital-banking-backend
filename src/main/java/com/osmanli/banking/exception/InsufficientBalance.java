package com.osmanli.banking.exception;

public class InsufficientBalance extends RuntimeException{
    public InsufficientBalance(String message){
        super(message);
    }
}
