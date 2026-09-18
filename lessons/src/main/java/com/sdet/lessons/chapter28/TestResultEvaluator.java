package com.sdet.lessons.chapter28;

/**
 * Part 8 mini-project — compare expected vs actual HTTP status and print evidence.
 * This is the first SDET-shaped program: expected, actual, decide, report.
 */
public class TestResultEvaluator {

    public static void main(String[] args) {
        int expected = 200;
        int actual = 404;

        System.out.println("=== Part 8 Project: Test Result Evaluator ===");
        System.out.println(evaluate(expected, actual));
        System.out.println();
        System.out.println("Matching example:");
        System.out.println(evaluate(200, 200));
    }

    public static String evaluate(int expected, int actual) {
        if (expected == actual) {
            return "TEST PASSED\nExpected: " + expected + "\nActual: " + actual;
        }
        return "TEST FAILED\nExpected: " + expected + "\nActual: " + actual;
    }
}
