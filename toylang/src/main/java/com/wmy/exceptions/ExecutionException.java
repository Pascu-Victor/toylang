package com.wmy.exceptions;

public class ExecutionException extends Exception {
    public ExecutionException(String message) {
        super(message);
    }
    
    @Override
    public String toString() {
        return super.getMessage();
    }
}
