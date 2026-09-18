package com.sdet.projects.bank;

public class BankAccount {

    private final String owner;
    private double balance;

    public BankAccount(String owner, double openingBalance) {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("owner is required");
        }
        if (openingBalance < 0) {
            throw new IllegalArgumentException("opening balance cannot be negative");
        }
        this.owner = owner;
        this.balance = openingBalance;
    }

    public void deposit(double amount) {
        requirePositive(amount);
        balance += amount;
    }

    public void withdraw(double amount) {
        requirePositive(amount);
        if (amount > balance) {
            throw new IllegalStateException("Insufficient funds for " + owner);
        }
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    public String getOwner() {
        return owner;
    }

    private static void requirePositive(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }
}
