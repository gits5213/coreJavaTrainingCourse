package com.sdet.lessons.chapter51;

/**
 * One concrete payment path. You cannot {@code new} the abstract parent, only this class.
 */
public class CardPaymentService extends PaymentService {

    @Override
    protected void charge(double amount) {
        System.out.println("Charging card for " + amount);
    }
}
