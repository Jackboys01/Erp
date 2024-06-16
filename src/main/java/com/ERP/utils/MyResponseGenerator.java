package com.ERP.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class MyResponseGenerator
{
    public static ResponseEntity<Object> generateResponse(HttpStatus httpStatus,boolean isError,String msg,Object responseBody)
    {
        Map<String,Object> map = new HashMap<>();
        try
        {
            map.put("status",httpStatus.value());
            map.put("isSuccess",isError);
            map.put("message",msg);
            map.put("data",responseBody);
            return new ResponseEntity<>(map,httpStatus);
        }
        catch ( Exception exception)
        {
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            map.put("isSuccess",false);
            map.put("message",exception.getMessage());
            map.put("data",null);
            return new ResponseEntity<>(map,httpStatus);
        }
    }

}
