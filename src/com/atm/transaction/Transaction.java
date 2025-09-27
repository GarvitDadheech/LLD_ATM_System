package com.atm.transaction;

import com.atm.hardware.CashDispenser;
import com.atm.hardware.ReceiptPrinter;
import com.atm.model.Account;
import com.atm.service.BankService;

import java.util.UUID;

public abstract class Transaction {
    protected final String transactionId;
    protected final BankService bankService;
    protected final CashDispenser cashDispenser;
    protected final ReceiptPrinter receiptPrinter;

    public Transaction(BankService bankService, CashDispenser cashDispenser, ReceiptPrinter receiptPrinter) {
        this.transactionId = UUID.randomUUID().toString();
        this.bankService = bankService;
        this.cashDispenser = cashDispenser;
        this.receiptPrinter = receiptPrinter;
    }

    public abstract void execute(Account account);
}
