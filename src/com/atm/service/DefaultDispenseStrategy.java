package com.atm.service;

import com.atm.exception.HardwareFailureException;

public class DefaultDispenseStrategy implements DispenseStrategy {
    private double cashAvailable;

    public DefaultDispenseStrategy(double initialCash) {
        this.cashAvailable = initialCash;
    }

    @Override
    public void dispense(double amount) {
        if (amount <= 0 || cashAvailable < amount) {
            System.out.println("Could not dispense cash. Not enough money in dispenser or invalid amount.");
            throw new HardwareFailureException("Could not dispense cash. Not enough money in dispenser or invalid amount.");
        }
        
        cashAvailable -= amount;
        System.out.println("Dispensing $" + String.format("%.2f", amount));
    }
}
