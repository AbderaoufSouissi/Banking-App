package com.ars.accountservice.exception;

public class UnsufficientBalanceException extends RuntimeException {
    public UnsufficientBalanceException(String message) {
        super(message);
    }
}
