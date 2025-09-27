package com.atm.transaction;

import com.atm.exception.InsufficientFundsException;
import com.atm.exception.HardwareFailureException;
import com.atm.hardware.CashDispenser;
import com.atm.hardware.ReceiptPrinter;
import com.atm.model.Account;
import com.atm.model.Receipt;
import com.atm.service.BankService;

public class WithdrawalTransaction extends Transaction {
    private final double amount;

    public WithdrawalTransaction(double amount, BankService bankService, CashDispenser cashDispenser, ReceiptPrinter receiptPrinter) {
        super(bankService, cashDispenser, receiptPrinter);
        this.amount = amount;
    }

    @Override
    public void execute(Account account) {
        System.out.println("Executing withdrawal of $" + String.format("%.2f", amount));
        
        try {
            bankService.withdraw(account, amount);
            
            try {
                cashDispenser.dispenseCash(amount);
                
                // Success case
                Receipt receipt = new Receipt.Builder(transactionId)
                        .type("Withdrawal")
                        .amount(amount)
                        .success(true)
                        .message("Withdrawal successful.")
                        .build();
                receiptPrinter.printReceipt(receipt);
                
            } catch (HardwareFailureException e) {
                // Critical issue: bank debited but cash not dispensed.
                System.err.println("CRITICAL ERROR: Failed to dispense cash after withdrawal. Please contact the bank.");
                // Attempt to refund the account
                account.deposit(amount); // Simplified reversal
                Receipt receipt = new Receipt.Builder(transactionId)
                        .type("Withdrawal")
                        .amount(amount)
                        .success(false)
                        .message("Failed to dispense cash. A reversal has been issued.")
                        .build();
                receiptPrinter.printReceipt(receipt);
            }
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
            Receipt receipt = new Receipt.Builder(transactionId)
                    .type("Withdrawal")
                    .amount(amount)
                    .success(false)
                    .message("Insufficient funds or bank error.")
                    .build();
            receiptPrinter.printReceipt(receipt);
        }
    }
}
