package com.raywu.investingsimulator.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNullApi;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    // handle all the exceptions caught by javax.validation.constraints in the request body
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid (
            MethodArgumentNotValidException exc, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(Objects.requireNonNull
                (exc.getFieldError()).getDefaultMessage());  // message set inside the validation annotation
        error.setField(exc.getFieldError().getField());     // the variable name
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // handle all the custom exceptions
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(CustomException exc) {
        return new ResponseEntity<>(exc.serializeError(), exc.getStatus());
    }

    // Handle any exception that is not handled by the custom exception handler
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(Exception exc) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
