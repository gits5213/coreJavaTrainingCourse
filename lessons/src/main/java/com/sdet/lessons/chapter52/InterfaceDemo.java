package com.sdet.lessons.chapter52;

/**
 * Chapter 52 — Interfaces.
 * {@code TestDataProvider} is the contract. {@code JsonDataProvider} is one implementation.
 */
public class InterfaceDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 52: Interface ===");

        TestDataProvider data = new JsonDataProvider();
        System.out.println("standard user = " + data.getUser("standard"));
        System.out.println("admin user = " + data.getUser("admin"));
        System.out.println("The test talks to TestDataProvider, not to JSON details.");
    }
}
