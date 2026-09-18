package com.sdet.lessons.chapter25;

/**
 * Chapter 25 — Logical operators.
 * Combine boolean checks with {@code &&} (and), {@code ||} (or), and {@code !} (not).
 */
public class LogicalOperatorsDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 25: Logical Operators ===");

        int statusCode = 200;
        double responseTime = 1.5;

        boolean healthy = statusCode == 200 && responseTime < 2;
        System.out.println("statusCode = " + statusCode + ", responseTime = " + responseTime);
        System.out.println("statusCode == 200 && responseTime < 2 → " + healthy);

        boolean clientErrorOrServerError = statusCode == 404 || statusCode == 500;
        System.out.println("statusCode == 404 || statusCode == 500 → " + clientErrorOrServerError);

        boolean notFailed = !(statusCode >= 400);
        System.out.println("!(statusCode >= 400) → " + notFailed);

        statusCode = 500;
        responseTime = 0.4;
        boolean stillHealthy = statusCode == 200 && responseTime < 2;
        System.out.println();
        System.out.println("After a 500: statusCode == 200 && responseTime < 2 → " + stillHealthy);
        System.out.println("&& needs both sides true. || needs only one side true.");
    }
}
