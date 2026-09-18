package com.sdet.lessons.chapter63;

/**
 * Chapter 63 — {@code try} / {@code catch}.
 * Catch an exception so the program can explain the problem instead of crashing immediately.
 */
public class TryCatchDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 63: try / catch ===");

        String[] browsers = {"Chrome", "Firefox"};
        try {
            System.out.println("Reading browsers[5]...");
            System.out.println(browsers[5]);
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println("Caught " + exception.getClass().getSimpleName());
            System.out.println("Message: " + exception.getMessage());
        }

        System.out.println("The program continues after catch.");
    }
}
