package com.sdet.projects.bank;

public class BankSimulatorApp {

    public static void main(String[] args) {
        BankAccount aisha = new BankAccount("Aisha", 100.00);
        BankAccount ben = new BankAccount("Ben", 25.00);

        System.out.println(aisha.getOwner() + " opening balance: " + aisha.getBalance());
        aisha.deposit(50.00);
        System.out.println(aisha.getOwner() + " after deposit 50.0: " + aisha.getBalance());
        aisha.withdraw(20.00);
        System.out.println(aisha.getOwner() + " after withdraw 20.0: " + aisha.getBalance());

        try {
            aisha.withdraw(500.00);
        } catch (IllegalStateException exception) {
            System.out.println(aisha.getOwner() + " withdraw 500.0 -> " + exception.getMessage());
        }

        System.out.println(ben.getOwner() + " opening balance: " + ben.getBalance());
        ben.deposit(75.00);
        System.out.println(ben.getOwner() + " after deposit 75.0: " + ben.getBalance());
    }
}
