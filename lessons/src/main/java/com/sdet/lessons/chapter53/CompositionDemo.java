package com.sdet.lessons.chapter53;

/**
 * Chapter 53 — Composition.
 * Prefer HAS-A when English is "a checkout page has a payment widget," not "a checkout page is a payment widget."
 */
public class CompositionDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 53: Composition ===");

        PaymentComponent payment = new PaymentComponent();
        CheckoutPage checkoutPage = new CheckoutPage(payment);
        checkoutPage.completePurchase(19.99);
        System.out.println("CheckoutPage holds a PaymentComponent field. That is HAS-A.");
    }
}
