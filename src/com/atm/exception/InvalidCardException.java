package com.atm.exception;

public class InvalidCardException extends TransactionException {
    public InvalidCardException(String message) {
        super(message);
    }
}
