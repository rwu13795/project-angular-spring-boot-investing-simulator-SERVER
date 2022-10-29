package com.raywu.investingsimulator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    // Handle the specific "NotFoundException"
    public ResponseEntity<ErrorResponse> handleException(NotFoundException exc) {

        // create a studentErrorResponse
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());			// pass the message from the exception to the errorResponse
        error.setTimeStamp(System.currentTimeMillis());

        // return ResponseEntity. Jackson will convert the StudentErrorResponse object to JSON automatically
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // Handle any exception that is not handled by the other handlers
    public ResponseEntity<ErrorResponse> handleException(Exception exc) {

        // create a studentErrorResponse
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());    // use BAD_REQUEST code 400
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
