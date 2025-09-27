package com.atm.state;

import com.atm.ATMOrchestrator;
import com.atm.exception.InvalidCardException;
import com.atm.model.Account;
import com.atm.transaction.Transaction;
import com.atm.transaction.WithdrawalTransaction;

public class TransactionSelectionState implements State {
    @Override
    public void handle(ATMOrchestrator atm) {
        System.out.println("Select transaction:");
        System.out.println("1. Withdrawal");
        System.out.println("2. Check Balance");
        System.out.println("3. Exit");

        String choice = atm.getKeyboard().getInput();

        switch (choice) {
            case "1":
                handleWithdrawal(atm);
                break;
            case "2":
                handleBalanceCheck(atm);
                break;
            case "3":
                atm.setState(new TransactionCompleteState(true)); // User chose to exit
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
                break;
        }
    }

    private void handleWithdrawal(ATMOrchestrator atm) {
        System.out.println("Enter amount to withdraw:");
        try {
            double amount = Double.parseDouble(atm.getKeyboard().getInput());
            if (amount <= 0) {
                System.out.println("Invalid amount.");
                return;
            }
            Account account = atm.getBankService().getAccount(atm.getCurrentCard());
            Transaction withdrawal = new WithdrawalTransaction(amount, atm.getBankService(), atm.getCashDispenser(), atm.getReceiptPrinter());
            withdrawal.execute(account);
            atm.setState(new TransactionCompleteState(false));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (InvalidCardException e) {
            System.err.println("Critical Error: " + e.getMessage());
            atm.setState(new TransactionCompleteState(true));
        }
    }

    private void handleBalanceCheck(ATMOrchestrator atm) {
        try {
            Account account = atm.getBankService().getAccount(atm.getCurrentCard());
            double balance = atm.getBankService().getBalance(account);
            System.out.println("Your current balance is: $" + String.format("%.2f", balance));
            atm.setState(new TransactionCompleteState(false));
        } catch (InvalidCardException e) {
            System.err.println("Critical Error: " + e.getMessage());
            atm.setState(new TransactionCompleteState(true));
        }
    }
}
