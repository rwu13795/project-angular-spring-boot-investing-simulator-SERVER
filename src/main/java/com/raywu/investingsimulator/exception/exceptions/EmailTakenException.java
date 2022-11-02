package com.raywu.investingsimulator.exception.exceptions;

import com.raywu.investingsimulator.exception.CustomException;
import com.raywu.investingsimulator.exception.ErrorResponse;
import org.springframework.http.HttpStatus;

public class EmailTakenException extends CustomException {
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private ErrorResponse errorResponse = new ErrorResponse();

    public EmailTakenException() {
        super("This email address is already used by another user");

        errorResponse.setMessage("This email address is already used by another user");
        errorResponse.setField(ErrorResponse.ErrorField.email.name());
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
