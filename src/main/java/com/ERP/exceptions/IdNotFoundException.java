package com.ERP.exceptions;

public class IdNotFoundException extends RuntimeException
{
    public IdNotFoundException(){
        super();
    }
    public IdNotFoundException(String message){
        super(message);
    }

    public IdNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
    public IdNotFoundException(Throwable cause){
        super(cause);
    }
    protected IdNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);}
}
