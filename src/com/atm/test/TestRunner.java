package com.atm.test;

import com.atm.exception.InsufficientFundsException;
import com.atm.exception.InvalidCardException;
import com.atm.hardware.CashDispenser;
import com.atm.hardware.ReceiptPrinter;
import com.atm.model.Account;
import com.atm.model.Card;
import com.atm.service.RealBankService;
import com.atm.transaction.Transaction;
import com.atm.transaction.WithdrawalTransaction;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("--- Starting ATM System Manual Test ---");

        testSuccessfulWithdrawal();
        System.out.println("\n----------------------------------------\n");
        testInsufficientFundsWithdrawal();
        System.out.println("\n----------------------------------------\n");
        testInvalidCardFormat();
        System.out.println("\n----------------------------------------\n");
        testBlacklistedCard();

        System.out.println("\n--- ATM System Manual Test Finished ---");
    }

    public static void testSuccessfulWithdrawal() {
        System.out.println(">>> Test Case: Successful Withdrawal");
        RealBankService bankService = new RealBankService();
        CashDispenser dispenser = CashDispenser.getInstance();
        dispenser.setDispenseStrategy(new com.atm.service.DefaultDispenseStrategy(20000));
        ReceiptPrinter printer = new ReceiptPrinter();

        // Get the dummy account which has a balance of 1500.0
        try {
            Account account = bankService.getAccount(new Card("5555-4444-3333-2222", "John Doe"));
            double initialBalance = account.getBalance();
            double amountToWithdraw = 500.0;

            System.out.println("Initial Balance: " + initialBalance);
            System.out.println("Attempting to withdraw: " + amountToWithdraw);

            Transaction withdrawal = new WithdrawalTransaction(amountToWithdraw, bankService, dispenser, printer);
            withdrawal.execute(account);

            double finalBalance = account.getBalance();
            System.out.println("Final Balance: " + finalBalance);

            if (finalBalance == initialBalance - amountToWithdraw) {
                System.out.println(">>> Test PASSED");
            } else {
                System.out.println(">>> Test FAILED. Expected balance: " + (initialBalance - amountToWithdraw) + ", but got: " + finalBalance);
            }
        } catch (InvalidCardException e) {
            System.out.println(">>> Test FAILED. Unexpected exception: " + e.getMessage());
        }
    }

    public static void testInsufficientFundsWithdrawal() {
        System.out.println(">>> Test Case: Insufficient Funds Withdrawal");
        RealBankService bankService = new RealBankService();
        CashDispenser dispenser = CashDispenser.getInstance();
        dispenser.setDispenseStrategy(new com.atm.service.DefaultDispenseStrategy(20000));
        ReceiptPrinter printer = new ReceiptPrinter();

        try {
            Account account = bankService.getAccount(new Card("5555-4444-3333-2222", "John Doe"));
            double initialBalance = account.getBalance();
            double amountToWithdraw = 5000.0; // More than the initial 3500.0

            System.out.println("Initial Balance: " + initialBalance);
            System.out.println("Attempting to withdraw: " + amountToWithdraw);

            Transaction withdrawal = new WithdrawalTransaction(amountToWithdraw, bankService, dispenser, printer);
            withdrawal.execute(account);

            double finalBalance = account.getBalance();
            System.out.println("Final Balance: " + finalBalance);

            if (finalBalance == initialBalance) {
                System.out.println(">>> Test PASSED");
            } else {
                System.out.println(">>> Test FAILED. Expected balance to remain: " + initialBalance + ", but got: " + finalBalance);
            }
        } catch (InvalidCardException e) {
            System.out.println(">>> Test FAILED. Unexpected exception: " + e.getMessage());
        }
    }

    public static void testInvalidCardFormat() {
        System.out.println(">>> Test Case: Invalid Card Format");
        RealBankService bankService = new RealBankService();
        try {
            bankService.getAccount(new Card("1234-5678-9876", "Invalid Card"));
            System.out.println(">>> Test FAILED. Expected InvalidCardException was not thrown.");
        } catch (InvalidCardException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
            if (e.getMessage().contains("Invalid card number format")) {
                System.out.println(">>> Test PASSED");
            } else {
                System.out.println(">>> Test FAILED. Exception message was not as expected.");
            }
        }
    }

    public static void testBlacklistedCard() {
        System.out.println(">>> Test Case: Blacklisted Card");
        RealBankService bankService = new RealBankService();
        try {
            bankService.getAccount(new Card("1111-2222-3333-4444", "Blacklisted User"));
            System.out.println(">>> Test FAILED. Expected InvalidCardException was not thrown for blacklisted card.");
        } catch (InvalidCardException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
            if (e.getMessage().contains("Card is blacklisted")) {
                System.out.println(">>> Test PASSED");
            } else {
                System.out.println(">>> Test FAILED. Exception message was not as expected.");
            }
        }
    }
}
