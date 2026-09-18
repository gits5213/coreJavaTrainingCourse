package com.sdet.lessons.chapter30;

/**
 * Chapter 30 — {@code for} loops.
 * Repeat a block a known number of times, or walk every item in an array.
 */
public class ForLoopDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 30: for loop ===");

        System.out.println("Count from 1 to 3:");
        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.println("  attempt " + attempt);
        }

        String[] browsers = {"Chrome", "Firefox", "Edge"};
        System.out.println("Walk a browser list:");
        for (int i = 0; i < browsers.length; i++) {
            System.out.println("  index " + i + " = " + browsers[i]);
        }

        System.out.println("Enhanced for (for-each):");
        for (String browser : browsers) {
            System.out.println("  run tests in " + browser);
        }
    }
}
