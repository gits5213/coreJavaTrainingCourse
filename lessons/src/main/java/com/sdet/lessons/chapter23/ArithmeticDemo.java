package com.sdet.lessons.chapter23;

/**
 * Chapter 23 — Arithmetic operators.
 * +, -, *, /, and % work on numbers. Integer division drops the fraction.
 */
public class ArithmeticDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 23: Arithmetic ===");

        int passed = 8;
        int failed = 2;
        int total = passed + failed;
        int remainingRetries = 3 - 1;
        int doubledWait = 2 * 5;
        int splitBatch = 10 / 4;
        int leftover = 10 % 4;
        double averageTime = 9.0 / 4.0;

        System.out.println("passed + failed = " + total);
        System.out.println("3 - 1 remainingRetries = " + remainingRetries);
        System.out.println("2 * 5 doubledWait = " + doubledWait);
        System.out.println("10 / 4 integer division = " + splitBatch + " (fraction is dropped)");
        System.out.println("10 % 4 leftover = " + leftover);
        System.out.println("9.0 / 4.0 averageTime = " + averageTime);
        System.out.println("Use double when you need a decimal result.");
    }
}
