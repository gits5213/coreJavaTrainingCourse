package com.sdet.lessons.chapter31;

/**
 * Chapter 31 — {@code while} loops.
 * Repeat while a condition stays true. A retry loop of 1..3 is a common SDET pattern.
 */
public class WhileLoopDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 31: while loop ===");

        int attempt = 1;
        int maxAttempts = 3;
        boolean passed = false;

        while (attempt <= maxAttempts && !passed) {
            System.out.println("Retry " + attempt + " of " + maxAttempts);
            passed = attempt == 3;
            if (!passed) {
                System.out.println("  still failing. try again.");
            } else {
                System.out.println("  passed on attempt " + attempt);
            }
            attempt++;
        }

        System.out.println("Loop stopped because passed=" + passed + " or attempts were used up.");
    }
}
