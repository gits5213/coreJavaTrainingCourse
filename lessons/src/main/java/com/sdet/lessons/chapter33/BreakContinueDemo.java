package com.sdet.lessons.chapter33;

/**
 * Chapter 33 — {@code break} and {@code continue}.
 * {@code break} leaves the loop. {@code continue} skips the rest of this round.
 */
public class BreakContinueDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 33: break and continue ===");

        int[] statusCodes = {200, 200, 500, 200};

        System.out.println("continue skips a passing status:");
        for (int statusCode : statusCodes) {
            if (statusCode == 200) {
                continue;
            }
            System.out.println("  investigate failure: " + statusCode);
        }

        System.out.println("break stops at the first server error:");
        for (int statusCode : statusCodes) {
            System.out.println("  saw " + statusCode);
            if (statusCode >= 500) {
                System.out.println("  stopping the run.");
                break;
            }
        }
    }
}
