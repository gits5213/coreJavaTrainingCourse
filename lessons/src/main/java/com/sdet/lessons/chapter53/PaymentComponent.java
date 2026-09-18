package com.sdet.lessons.chapter53;

/**
 * A reusable payment widget. CheckoutPage HAS-A PaymentComponent; it does not extend it.
 */
public class PaymentComponent {

    public void pay(double amount) {
        System.out.println("PaymentComponent charging " + amount);
    }
}
