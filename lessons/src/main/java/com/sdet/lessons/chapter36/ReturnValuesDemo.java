package com.sdet.lessons.chapter36;

/**
 * Chapter 36 — Return values.
 * A method can hand a result back to the caller. {@code statusMatches} returns true or false.
 */
public class ReturnValuesDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 36: Return Values ===");

        boolean ok = statusMatches(200, 200);
        boolean notOk = statusMatches(404, 200);

        System.out.println("statusMatches(200, 200) = " + ok);
        System.out.println("statusMatches(404, 200) = " + notOk);
        System.out.println("The caller decides what to print or assert with the returned boolean.");
    }

    private static boolean statusMatches(int actual, int expected) {
        return actual == expected;
    }
}
