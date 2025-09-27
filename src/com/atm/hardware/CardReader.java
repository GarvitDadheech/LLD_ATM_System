package com.atm.hardware;

import com.atm.model.Card;

public class CardReader {
    private final Keyboard keyboard;

    public CardReader(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public Card readCard() {
        System.out.println("Please enter your card number (e.g., 1234-5678-9876-5432):");
        String cardNumber = keyboard.getInput();
        System.out.println("Please enter your cardholder name (e.g., John Doe):");
        String cardHolderName = keyboard.getInput();
        System.out.println("Card inserted. Reading card...");
        return new Card(cardNumber, cardHolderName);
    }

    public void ejectCard() {
        System.out.println("Card ejected.");
    }
}
