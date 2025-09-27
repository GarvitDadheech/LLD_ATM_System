package com.atm;

import com.atm.factory.ATMFactory;
import com.atm.factory.DefaultATMFactory;
import com.atm.hardware.*;
import com.atm.model.Card;
import com.atm.service.BankService;
import com.atm.service.RemoteBankProxy;
import com.atm.state.IdleState;
import com.atm.state.State;

public class ATMOrchestrator {
    private State currentState;
    private final CardReader cardReader;
    private final Keyboard keyboard;
    private final BankService bankService;
    private final CashDispenser cashDispenser;
    private final ReceiptPrinter receiptPrinter;

    private Card currentCard;

    public ATMOrchestrator(ATMFactory factory) {
        this.currentState = new IdleState();
        this.cardReader = factory.createCardReader();
        this.keyboard = factory.createKeyboard();
        this.bankService = factory.createBankService();
        this.cashDispenser = factory.createCashDispenser();
        this.receiptPrinter = factory.createReceiptPrinter();
    }
    
    public void run() {
        System.out.println("ATM is now operational. Welcome!");
        while (true) {
            currentState.handle(this);
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("ATM operation was interrupted.");
                break;
            }
        }
    }

    // Getters for components
    public CardReader getCardReader() { return cardReader; }
    public Keyboard getKeyboard() { return keyboard; }
    public BankService getBankService() { return bankService; }
    public CashDispenser getCashDispenser() { return cashDispenser; }
    public ReceiptPrinter getReceiptPrinter() { return receiptPrinter; }
    
    // Getters and setters for state
    public Card getCurrentCard() { return currentCard; }
    public void setCurrentCard(Card card) { this.currentCard = card; }
    public void setState(State newState) { this.currentState = newState; }

    public static void main(String[] args) {
        System.out.println("Initializing ATM System...");
        ATMFactory factory = new DefaultATMFactory();
        ATMOrchestrator atm = new ATMOrchestrator(factory);
        atm.run();
    }
}
