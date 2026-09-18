package com.sdet.lessons.chapter51;

/**
 * Chapter 51 — Abstract classes.
 * {@code PaymentService} defines the flow. Subclasses implement the missing {@code charge} step.
 */
public class AbstractClassDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 51: Abstract Class ===");

        PaymentService paymentService = new CardPaymentService();
        paymentService.process(42.50);
        System.out.println("You cannot write new PaymentService() because it is abstract.");
    }
}
