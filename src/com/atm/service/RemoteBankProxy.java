package com.atm.service;

import com.atm.exception.InsufficientFundsException;
import com.atm.exception.InvalidCardException;
import com.atm.model.Account;
import com.atm.model.Card;

public class RemoteBankProxy implements BankService {
    private final RealBankService realBankService;
    private int retryCount = 0;
    private static final int MAX_RETRIES = 3;

    public RemoteBankProxy() {
        this.realBankService = new RealBankService();
    }

    @Override
    public Account getAccount(Card card) throws InvalidCardException {
        // Could add caching, logging, etc. here
        return realBankService.getAccount(card);
    }

    @Override
    public boolean authenticate(Card card, String pin) throws InvalidCardException {
        // Could add logging, security checks
        return realBankService.authenticate(card, pin);
    }

    @Override
    public double getBalance(Account account) {
        return realBankService.getBalance(account);
    }

    @Override
    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        // Adding a dummy retry logic
        boolean success = false;
        retryCount = 0;
        while (!success && retryCount < MAX_RETRIES) {
            try {
                System.out.println("Proxy: Attempting withdrawal (" + (retryCount + 1) + "/" + MAX_RETRIES + ")");
                // Simulate network delay or failure
                if (Math.random() > 0.8 && retryCount < 1) { // 20% chance of failure on first try
                     throw new RuntimeException("Network error");
                }
                realBankService.withdraw(account, amount);
                success = true; // If no exception is thrown, it's a success
            } catch (InsufficientFundsException e) {
                // Don't retry for insufficient funds, just re-throw
                throw e;
            } catch (Exception e) {
                System.err.println("Proxy: Caught exception: " + e.getMessage() + ". Retrying...");
                retryCount++;
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (!success) {
            // This could be a more specific exception if needed
            throw new InsufficientFundsException("Could not process withdrawal after multiple retries.");
        }
    }
}
