package com.atm.factory;

import com.atm.hardware.*;
import com.atm.service.BankService;
import com.atm.service.RemoteBankProxy;

public class DefaultATMFactory implements ATMFactory {
    private Keyboard keyboard; // Cache the keyboard to ensure a single instance is used

    @Override
    public BankService createBankService() {
        return new RemoteBankProxy();
    }

    @Override
    public CardReader createCardReader() {
        return new CardReader(createKeyboard()); // Inject the keyboard
    }

    @Override
    public Keyboard createKeyboard() {
        if (this.keyboard == null) {
            this.keyboard = new DigitalKeyboard();
        }
        return this.keyboard;
    }

    @Override
    public ReceiptPrinter createReceiptPrinter() {
        return new ReceiptPrinter();
    }

    @Override
    public CashDispenser createCashDispenser() {
        return CashDispenser.getInstance();
    }
}
