package com.ERP.exceptionHandler;

import com.ERP.entities.ExceptionMessage;
import com.ERP.exceptions.*;
import com.ERP.utils.MyResponseGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(value= IdNotFoundException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> exceptionHandler()
    {
        return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Id is not found",null);
    }

    @ExceptionHandler(value= NullPointerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> nullPointerHandler()
    {
        return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Null Pointer Exception",null);
    }

//    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Object> exceptionHandle()
//    {
//        return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"There is Exception",null);
//    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleArgumentExp(MethodArgumentNotValidException methodArgumentNotValidException)
    {
        Map<String,String> errorsMap= new HashMap<>();
        methodArgumentNotValidException.getBindingResult()
                .getAllErrors()
                .forEach((error)->
        {
            String fieldName=((FieldError)error).getField();
            String errorMessage=((FieldError)error).getDefaultMessage();
            errorsMap.put(fieldName,errorMessage);
        });

        return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Server side error",errorsMap);
    }

    @ExceptionHandler(SalaryStructureNotFoundException.class)
    public ResponseEntity<ExceptionMessage> salaryStructureNotFoundException(SalaryStructureNotFoundException exception, WebRequest request){
        ExceptionMessage errorMessage = new ExceptionMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(HRNotFoundException.class)
    public ResponseEntity<ExceptionMessage> hrNotFoundException(HRNotFoundException exception,
                                                                WebRequest request){
        ExceptionMessage errorMessage = new ExceptionMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ExceptionMessage> taskNotFoundException(TaskNotFoundException exception,
                                                                  WebRequest request){
        ExceptionMessage errorMessage = new ExceptionMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ExceptionMessage> employeeNotFoundException(EmployeeNotFoundException exception,
                                                                  WebRequest request){
        ExceptionMessage errorMessage = new ExceptionMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
