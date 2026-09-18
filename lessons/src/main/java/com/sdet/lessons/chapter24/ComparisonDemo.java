package com.sdet.lessons.chapter24;

/**
 * Chapter 24 — Comparison operators.
 * Comparisons produce {@code boolean} values: true or false.
 */
public class ComparisonDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 24: Comparison ===");

        int statusCode = 200;
        int expectedStatus = 200;
        double responseTime = 1.8;
        double maxTime = 2.0;

        System.out.println("statusCode == expectedStatus → " + (statusCode == expectedStatus));
        System.out.println("statusCode != 404 → " + (statusCode != 404));
        System.out.println("statusCode > 199 → " + (statusCode > 199));
        System.out.println("statusCode < 300 → " + (statusCode < 300));
        System.out.println("statusCode >= 200 → " + (statusCode >= 200));
        System.out.println("statusCode <= 200 → " + (statusCode <= 200));
        System.out.println("responseTime < maxTime → " + (responseTime < maxTime));
        System.out.println("Use == for primitives. Use equals() for String text.");
    }
}
