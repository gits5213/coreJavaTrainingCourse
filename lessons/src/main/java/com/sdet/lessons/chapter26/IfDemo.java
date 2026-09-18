package com.sdet.lessons.chapter26;

/**
 * Chapter 26 — {@code if}.
 * An {@code if} block runs only when its condition is true.
 */
public class IfDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 26: if ===");

        int statusCode = 200;
        System.out.println("statusCode = " + statusCode);

        if (statusCode == 200) {
            System.out.println("Condition is true. This block runs.");
            System.out.println("PASS: API returned OK.");
        }

        statusCode = 500;
        System.out.println();
        System.out.println("statusCode = " + statusCode);
        if (statusCode == 200) {
            System.out.println("This line will not print.");
        }
        System.out.println("The if was skipped because the condition was false.");
    }
}
