package com.ElectricityAutomationInitiative.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class APIException extends RuntimeException{
    public APIException(HttpStatus badRequest, String msg){
        super(msg);
    }
}
