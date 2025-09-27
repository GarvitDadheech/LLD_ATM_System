package com.atm.model;

public class Receipt {
    private final String transactionId;
    private final String transactionType;
    private final double amount;
    private final boolean success;
    private final String message;

    private Receipt(Builder builder) {
        this.transactionId = builder.transactionId;
        this.transactionType = builder.transactionType;
        this.amount = builder.amount;
        this.success = builder.success;
        this.message = builder.message;
    }

    @Override
    public String toString() {
        return "--- Receipt ---\n" +
                "Transaction ID: " + transactionId + "\n" +
                "Type: " + transactionType + "\n" +
                "Amount: $" + String.format("%.2f", amount) + "\n" +
                "Status: " + (success ? "Success" : "Failed") + "\n" +
                "Message: " + message + "\n" +
                "-----------------";
    }

    public static class Builder {
        private final String transactionId;
        private String transactionType;
        private double amount;
        private boolean success;
        private String message;

        public Builder(String transactionId) {
            this.transactionId = transactionId;
        }

        public Builder type(String transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Receipt build() {
            return new Receipt(this);
        }
    }
}
