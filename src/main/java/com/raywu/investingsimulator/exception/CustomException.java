package com.raywu.investingsimulator.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    public CustomException(String message) { super(message); }
    public abstract ErrorResponse serializeError();
    public abstract HttpStatus getStatus();
}

// I am creating this "CustomException" the same way I did in the Node.js
// All the custom exceptions will implement this "CustomException" and handle
// the errorResponse accordingly