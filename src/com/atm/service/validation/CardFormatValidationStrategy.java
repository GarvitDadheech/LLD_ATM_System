package com.atm.service.validation;

import com.atm.exception.InvalidCardException;
import com.atm.model.Card;

public class CardFormatValidationStrategy implements CardValidationStrategy {
    @Override
    public void validate(Card card) throws InvalidCardException {
        System.out.println("Validating card format...");
        String cardNumber = card.getCardNumber();
        if (cardNumber == null || !cardNumber.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
            throw new InvalidCardException("Invalid card number format.");
        }
        System.out.println("Card format is valid.");
    }
}
