package com.sdet.lessons.chapter34;

/**
 * Chapter 34 — Methods.
 * A method is a named block of steps you can call. {@code printWelcome} is one example.
 */
public class MethodsDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 34: Methods ===");
        printWelcome();
        printWelcome();
        System.out.println("The same method ran twice. We did not copy the print lines.");
    }

    private static void printWelcome() {
        System.out.println("Welcome to the SDET Java course.");
    }
}
