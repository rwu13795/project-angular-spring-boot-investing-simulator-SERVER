package com.raywu.investingsimulator.exception.exceptions;

import com.raywu.investingsimulator.exception.CustomException;
import com.raywu.investingsimulator.exception.ErrorResponse;
import com.raywu.investingsimulator.portfolio.dto.TransactionType;
import org.springframework.http.HttpStatus;

public class PriceLimitException extends CustomException {
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private ErrorResponse errorResponse = new ErrorResponse();

    public PriceLimitException(String type) {
        super("Price limit not satisfied");

        errorResponse.setMessage("Price limit not satisfied");
        errorResponse.setField(type);
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
