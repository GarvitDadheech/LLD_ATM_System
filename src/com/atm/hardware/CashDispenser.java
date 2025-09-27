package com.atm.hardware;

import com.atm.service.DefaultDispenseStrategy;
import com.atm.service.DispenseStrategy;

public class CashDispenser {
    private static final CashDispenser INSTANCE = new CashDispenser();
    private DispenseStrategy dispenseStrategy;

    private CashDispenser() {
        // Initialize with a default strategy
        this.dispenseStrategy = new DefaultDispenseStrategy(10000.0); // $10,000 initial cash
    }

    public static CashDispenser getInstance() {
        return INSTANCE;
    }

    public void setDispenseStrategy(DispenseStrategy dispenseStrategy) {
        this.dispenseStrategy = dispenseStrategy;
    }

    public void dispenseCash(double amount) {
        dispenseStrategy.dispense(amount);
    }
}
