package com.sdet.lessons.chapter65;

/**
 * Chapter 65 — {@code throw}.
 * You can create and throw an exception when a rule is broken, such as a bad status code.
 */
public class ThrowDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 65: throw ===");

        try {
            assertOk(200);
            assertOk(500);
        } catch (IllegalStateException exception) {
            System.out.println("Caught: " + exception.getMessage());
        }
    }

    private static void assertOk(int statusCode) {
        if (statusCode != 200) {
            throw new IllegalStateException("Expected 200 but was " + statusCode);
        }
        System.out.println("status " + statusCode + " is OK");
    }
}
