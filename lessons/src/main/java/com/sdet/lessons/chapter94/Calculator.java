package com.sdet.lessons.chapter94;

/**
 * Chapter 94 — Production code for unit testing.
 * {@code add} is the function students will test with JUnit in a later project.
 * This class is not a test. It is the code under test.
 */
public class Calculator {

    public int add(int left, int right) {
        return left + right;
    }

    public static void main(String[] args) {
        System.out.println("=== Chapter 94: Calculator (production code) ===");
        Calculator calculator = new Calculator();
        int sum = calculator.add(2, 3);
        System.out.println("calculator.add(2, 3) = " + sum);
        System.out.println("Write JUnit tests in a project module, not in this lessons module.");
    }
}
