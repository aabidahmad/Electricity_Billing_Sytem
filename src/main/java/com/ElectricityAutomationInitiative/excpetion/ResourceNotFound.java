package com.ElectricityAutomationInitiative.excpetion;


public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(String msg){
        super(msg);
    }
}
