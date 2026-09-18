package com.sdet.lessons.chapter53;

/**
 * CheckoutPage HAS-A PaymentComponent. Composition: a field, not {@code extends}.
 */
public class CheckoutPage {

    private final PaymentComponent paymentComponent;

    public CheckoutPage(PaymentComponent paymentComponent) {
        this.paymentComponent = paymentComponent;
    }

    public void completePurchase(double amount) {
        System.out.println("CheckoutPage reviewing cart");
        paymentComponent.pay(amount);
        System.out.println("CheckoutPage showing confirmation");
    }
}
