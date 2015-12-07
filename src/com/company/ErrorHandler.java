package com.company;

/**
 * CResponsible for handling errors throughout the application.
 */
public class ErrorHandler {
    private Exception exception;
    private String message;

    public ErrorHandler(String msg, Exception e){
        this.exception = e;
        this.message = msg;
        this.throwError();
    }

    public void throwError(){
        exception.printStackTrace();
        System.out.println(this.message);
    }
}
