package com.raywu.investingsimulator.exception.exceptions;

import com.raywu.investingsimulator.exception.CustomException;
import com.raywu.investingsimulator.exception.ErrorResponse;
import org.springframework.http.HttpStatus;

public class IncorrectEmailException extends CustomException {
    private HttpStatus status = HttpStatus.NOT_FOUND;
    private ErrorResponse errorResponse = new ErrorResponse();

    public IncorrectEmailException() {
        super("This email does not exist in our record");

        errorResponse.setMessage("This email does not exist in our record");
        errorResponse.setField(ErrorResponse.ErrorField.email.name());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
    }

    @Override
    public ErrorResponse serializeError() {
        return errorResponse;
    }

    public HttpStatus getStatus() {
        return status;
    }
}