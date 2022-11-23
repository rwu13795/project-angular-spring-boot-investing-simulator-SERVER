package com.raywu.investingsimulator.exception.exceptions;

import com.raywu.investingsimulator.exception.CustomException;
import com.raywu.investingsimulator.exception.ErrorResponse;
import com.raywu.investingsimulator.portfolio.dto.TransactionType;
import org.springframework.http.HttpStatus;

public class PriceLimitException extends CustomException {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
    private final ErrorResponse errorResponse = new ErrorResponse();

    public PriceLimitException(String transactionType) {
        super("Price limit not satisfied");

        String message = "The current market price is lower than the limit";
        if(transactionType.equals(TransactionType.BUY.name())
                || transactionType.equals(TransactionType.BUY_TO_COVER.name())) {
            message = "The current market price is higher than the limit";
        }

        errorResponse.setMessage(message);
        errorResponse.setField("PRICE_LIMIT");
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
