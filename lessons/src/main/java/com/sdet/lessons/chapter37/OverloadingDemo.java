package com.sdet.lessons.chapter37;

/**
 * Chapter 37 — Method overloading.
 * Same method name, different parameter lists. Java picks the matching version.
 */
public class OverloadingDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 37: Overloading ===");
        click("Login");
        click("Submit", 2);
        waitFor(1);
        waitFor(1.5);
    }

    private static void click(String buttonName) {
        System.out.println("click(\"" + buttonName + "\") → clicking once");
    }

    private static void click(String buttonName, int times) {
        System.out.println("click(\"" + buttonName + "\", " + times + ") → clicking " + times + " times");
    }

    private static void waitFor(int seconds) {
        System.out.println("waitFor(" + seconds + ") → whole seconds");
    }

    private static void waitFor(double seconds) {
        System.out.println("waitFor(" + seconds + ") → fractional seconds");
    }
}
