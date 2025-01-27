package com.Bank.AccountHub.Exceptions;

public class CurrencyBalanceNotFound extends RuntimeException {
    public CurrencyBalanceNotFound(String message) {
        super(message);
    }
}
