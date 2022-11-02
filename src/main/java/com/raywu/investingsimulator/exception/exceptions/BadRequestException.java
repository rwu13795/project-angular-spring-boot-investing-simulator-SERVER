package com.raywu.investingsimulator.exception.exceptions;

import com.raywu.investingsimulator.exception.CustomException;
import com.raywu.investingsimulator.exception.ErrorResponse;
import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private ErrorResponse errorResponse = new ErrorResponse();

    public BadRequestException(String message, String field) {
        super(message);

        errorResponse.setMessage(message);
        errorResponse.setField(field);
        errorResponse.setTimeStamp(System.currentTimeMillis());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    public ErrorResponse serializeError() {
        return errorResponse;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

// I should use "BadRequestException" to handle any exception that is not handled by the
// other custom exception.