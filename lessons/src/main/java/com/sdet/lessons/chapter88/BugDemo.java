package com.sdet.lessons.chapter88;

/**
 * Chapter 88 — What is a bug?
 * A bug is when the program's behavior does not match the intention.
 * Here the intention is {@code price * quantity}, but the first line uses {@code -}.
 */
public class BugDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 88: What is a Bug? ===");

        double price = 10.0;
        int quantity = 2;

        double wrongTotal = price - quantity;
        System.out.println("Intention: total = price * quantity = 20.0");
        System.out.println("Buggy code used minus: price - quantity = " + wrongTotal);
        System.out.println("The JVM did what it was told. The algorithm did not match the intention.");

        double correctTotal = price * quantity;
        System.out.println("Corrected code uses multiply: price * quantity = " + correctTotal);
        System.out.println("Debug loop: Run → Breakpoint → Pause → Inspect → Understand → Fix.");
    }
}
