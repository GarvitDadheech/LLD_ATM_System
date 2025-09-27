package com.atm.service;

import com.atm.exception.InsufficientFundsException;
import com.atm.exception.InvalidCardException;
import com.atm.model.Account;
import com.atm.model.Card;

public interface BankService {
    Account getAccount(Card card) throws InvalidCardException;
    boolean authenticate(Card card, String pin) throws InvalidCardException;
    double getBalance(Account account);
    void withdraw(Account account, double amount) throws InsufficientFundsException;
}
