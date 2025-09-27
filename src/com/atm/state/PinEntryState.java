package com.atm.state;

import com.atm.ATMOrchestrator;
import com.atm.exception.InvalidCardException;

public class PinEntryState implements State {
    private int pinAttempts = 0;
    private static final int MAX_PIN_ATTEMPTS = 3;

    @Override
    public void handle(ATMOrchestrator atm) {
        System.out.println("Please enter your PIN:");
        String pin = atm.getKeyboard().getInput();

        try {
            boolean authenticated = atm.getBankService().authenticate(atm.getCurrentCard(), pin);

            if (authenticated) {
                System.out.println("PIN accepted.");
                atm.setState(new TransactionSelectionState());
            } else {
                handlePinFailure(atm);
            }
        } catch (InvalidCardException e) {
            System.err.println("Error: " + e.getMessage());
            System.out.println("This card is invalid or not supported. Ejecting card.");
            atm.getCardReader().ejectCard();
            atm.setCurrentCard(null);
            atm.setState(new IdleState());
        }
    }

    private void handlePinFailure(ATMOrchestrator atm) {
        pinAttempts++;
        System.out.println("Invalid PIN. Please try again.");
        if (pinAttempts >= MAX_PIN_ATTEMPTS) {
            System.out.println("Too many failed PIN attempts. Card will be ejected.");
            atm.getCardReader().ejectCard();
            atm.setCurrentCard(null);
            atm.setState(new IdleState());
        }
    }
}
