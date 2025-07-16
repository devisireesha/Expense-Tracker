package com.expensetracker.exceptions;

public class InvalidFilterException extends Exception {

    public InvalidFilterException(String message) {
        super(message);
    }

    public InvalidFilterException(String message, Throwable cause) {
        super(message, cause);
    }
}