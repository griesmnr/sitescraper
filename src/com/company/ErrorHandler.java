package com.company;

/**
 * Created by nicoleg on 12/6/15.
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
        System.out.println(this.message + " ");
        exception.printStackTrace();
    }
}
