package com.raywu.investingsimulator.exception.exceptions;

import com.raywu.investingsimulator.exception.CustomException;
import com.raywu.investingsimulator.exception.ErrorResponse;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends CustomException {
    private final HttpStatus status = HttpStatus.UNAUTHORIZED;
    private final ErrorResponse errorResponse = new ErrorResponse();

    public InvalidTokenException(String message) {
        super(message);

        errorResponse.setMessage(message);
        errorResponse.setField(ErrorResponse.ErrorField.token.name());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Override
    public ErrorResponse serializeError() {
        return errorResponse;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
