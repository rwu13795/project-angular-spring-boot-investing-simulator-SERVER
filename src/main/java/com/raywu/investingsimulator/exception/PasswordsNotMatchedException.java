package com.raywu.investingsimulator.exception;

public class PasswordsNotMatchedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // I can only pass the "message" string to RuntimeException, I don't know if
    // there is a way to pass multiple option
    public PasswordsNotMatchedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordsNotMatchedException(String message) {
        super(message);
    }

    public PasswordsNotMatchedException(Throwable cause) {
        super(cause);
    }
}
