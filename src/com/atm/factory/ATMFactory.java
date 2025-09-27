package com.atm.factory;

import com.atm.hardware.CardReader;
import com.atm.hardware.CashDispenser;
import com.atm.hardware.Keyboard;
import com.atm.hardware.ReceiptPrinter;
import com.atm.service.BankService;

public interface ATMFactory {
    BankService createBankService();
    CardReader createCardReader();
    Keyboard createKeyboard();
    ReceiptPrinter createReceiptPrinter();
    CashDispenser createCashDispenser();
}
