package com.sdet.lessons.chapter44;

/**
 * Chapter 44 — Encapsulation.
 * Keep fields {@code private} and change them only through methods that can enforce rules.
 */
public class EncapsulationDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 44: Encapsulation ===");

        Account account = new Account("qa.tester", "Test123");
        System.out.println("username via getter = " + account.getUsername());
        System.out.println("balance via getter = " + account.getBalance());

        account.deposit(50);
        System.out.println("after deposit(50), balance = " + account.getBalance());

        boolean weakPasswordAccepted = account.setPassword("x");
        System.out.println("setPassword(\"x\") accepted? " + weakPasswordAccepted);

        boolean demoPasswordAccepted = account.setPassword("Test123");
        System.out.println("setPassword(\"Test123\") accepted? " + demoPasswordAccepted);
        System.out.println("Callers cannot write account.password or account.balance directly.");
    }
}
