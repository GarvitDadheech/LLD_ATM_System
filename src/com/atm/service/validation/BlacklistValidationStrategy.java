package com.atm.service.validation;

import com.atm.exception.InvalidCardException;
import com.atm.model.Card;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class BlacklistValidationStrategy implements CardValidationStrategy {
    private static final String[] BLOCKED_CARD_NUMBERS = {
        "1111-2222-3333-4444",
        "5555-6666-7777-8888",
        "9999-0000-1111-2222",
        "1234-1234-1234-1234",
        "0000-0000-0000-0000"
    };
    
    private static final Set<String> BLACKLISTED_CARDS = new HashSet<>(Arrays.asList(BLOCKED_CARD_NUMBERS));

    @Override
    public void validate(Card card) throws InvalidCardException {
        System.out.println("Checking card against blacklist...");
        if (BLACKLISTED_CARDS.contains(card.getCardNumber())) {
            throw new InvalidCardException("Card is blacklisted and cannot be used.");
        }
        System.out.println("Card is not blacklisted.");
    }
}
