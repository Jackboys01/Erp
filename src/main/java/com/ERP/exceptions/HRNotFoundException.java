package com.ERP.exceptions;

public class HRNotFoundException extends Exception{
    public HRNotFoundException(){
        super();
    }
    public HRNotFoundException(String message){
        super(message);
    }

    public HRNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
    public HRNotFoundException(Throwable cause){
        super(cause);
    }
    protected HRNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
