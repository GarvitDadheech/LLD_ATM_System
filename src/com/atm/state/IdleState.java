package com.atm.state;

import com.atm.ATMOrchestrator;
import com.atm.model.Card;

public class IdleState implements State {
    @Override
    public void handle(ATMOrchestrator atm) {
        System.out.println("ATM is idle. Please insert your card to begin.");
        System.out.println("Press Enter when you have inserted your card...");
        atm.getKeyboard().getInput(); // Wait for user to press Enter
        
        Card card = atm.getCardReader().readCard();
        atm.setCurrentCard(card);
        
        if (atm.getCurrentCard() != null) {
            atm.setState(new PinEntryState());
        }
    }
}
