package com.sdet.lessons.chapter51;

/**
 * Abstract payment service: shared steps, subclass fills in {@code charge}.
 */
public abstract class PaymentService {

    public final void process(double amount) {
        System.out.println("Starting payment for " + amount);
        charge(amount);
        System.out.println("Payment complete.");
    }

    protected abstract void charge(double amount);
}
