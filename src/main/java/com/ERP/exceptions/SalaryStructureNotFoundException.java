package com.ERP.exceptions;

public class SalaryStructureNotFoundException extends Exception{
    public SalaryStructureNotFoundException(){
        super();
    }
    public SalaryStructureNotFoundException(String message){
        super(message);
    }

    public SalaryStructureNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
    public SalaryStructureNotFoundException(Throwable cause){
        super(cause);
    }
    protected SalaryStructureNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
