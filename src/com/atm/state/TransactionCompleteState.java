package com.atm.state;

import com.atm.ATMOrchestrator;

public class TransactionCompleteState implements State {
    private final boolean exitChosen;

    public TransactionCompleteState(boolean exitChosen) {
        this.exitChosen = exitChosen;
    }

    @Override
    public void handle(ATMOrchestrator atm) {
        if (exitChosen) {
            System.out.println("Ejecting card. Thank you for using our ATM.");
            atm.getCardReader().ejectCard();
            atm.setCurrentCard(null);
            atm.setState(new IdleState());
            return;
        }

        System.out.println("Would you like to perform another transaction? (yes/no)");
        String choice = atm.getKeyboard().getInput().toLowerCase();

        if (choice.equals("yes")) {
            atm.setState(new TransactionSelectionState());
        } else {
            System.out.println("Ejecting card. Thank you for using our ATM.");
            atm.getCardReader().ejectCard();
            atm.setCurrentCard(null);
            atm.setState(new IdleState());
        }
    }
}
