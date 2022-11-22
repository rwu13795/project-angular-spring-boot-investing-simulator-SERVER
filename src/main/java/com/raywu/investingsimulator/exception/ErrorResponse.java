package com.raywu.investingsimulator.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private long timeStamp;
    private String field;
    public ErrorResponse() {}

    public enum ErrorField {
        password, email, new_password, confirm_password, token
    }
}
