package com.atm.hardware;

import com.atm.model.Receipt;

public class ReceiptPrinter {
    public void printReceipt(Receipt receipt) {
        System.out.println("Printing receipt...");
        System.out.println(receipt.toString());
    }
}
