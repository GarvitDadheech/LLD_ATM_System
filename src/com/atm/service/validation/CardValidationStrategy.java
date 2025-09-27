package com.atm.service.validation;

import com.atm.exception.InvalidCardException;
import com.atm.model.Card;

public interface CardValidationStrategy {
    void validate(Card card) throws InvalidCardException;
}
