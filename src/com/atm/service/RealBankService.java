package com.atm.service;

import com.atm.exception.InsufficientFundsException;
import com.atm.exception.InvalidCardException;
import com.atm.model.Account;
import com.atm.model.Card;
import com.atm.service.validation.BlacklistValidationStrategy;
import com.atm.service.validation.CardFormatValidationStrategy;
import com.atm.service.validation.CardValidationStrategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealBankService implements BankService {
    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<String, String> cardPinMap = new HashMap<>();
    private final List<CardValidationStrategy> validationStrategies;

    public RealBankService() {
        // Dummy data for simulation
        Account account1 = new Account("1111222233334444", 1500.0);
        accounts.put("1234-5678-9876-5432", account1);
        cardPinMap.put("1234-5678-9876-5432", "1234");

        // Initialize validation strategies
        this.validationStrategies = Arrays.asList(
            new CardFormatValidationStrategy(),
            new BlacklistValidationStrategy()
        );
    }

    private void runAllValidations(Card card) throws InvalidCardException {
        for (CardValidationStrategy strategy : validationStrategies) {
            strategy.validate(card);
        }
    }

    @Override
    public Account getAccount(Card card) throws InvalidCardException {
        runAllValidations(card); // Run validations first
        System.out.println("RealBankService: Getting account for card " + card.getCardNumber());
        Account account = accounts.get(card.getCardNumber());
        if (account == null) {
            throw new InvalidCardException("Card not found.");
        }
        return account;
    }

    @Override
    public boolean authenticate(Card card, String pin) throws InvalidCardException {
        runAllValidations(card); 
        System.out.println("RealBankService: Authenticating card " + card.getCardNumber());
        if (!cardPinMap.containsKey(card.getCardNumber())) {
            throw new InvalidCardException("Card not registered with the bank.");
        }
        return cardPinMap.get(card.getCardNumber()).equals(pin);
    }

    @Override
    public double getBalance(Account account) {
        System.out.println("RealBankService: Getting balance for account " + account.getAccountNumber());
        return account.getBalance();
    }

    @Override
    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        System.out.println("RealBankService: Withdrawing " + amount + " from account " + account.getAccountNumber());
        if (!account.withdraw(amount)) {
            throw new InsufficientFundsException("Insufficient funds for this withdrawal.");
        }
    }
}
