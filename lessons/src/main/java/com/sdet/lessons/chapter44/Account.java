package com.sdet.lessons.chapter44;

/**
 * Encapsulated account: private data, public doors (methods).
 */
public class Account {

    private final String username;
    private String password;
    private double balance;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0;
    }

    public String getUsername() {
        return username;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("deposit rejected: amount must be positive");
            return;
        }
        balance += amount;
    }

    public boolean setPassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            return false;
        }
        password = newPassword;
        return true;
    }
}
