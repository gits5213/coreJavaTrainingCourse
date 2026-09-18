package com.sdet.lessons.chapter28;

/**
 * Chapter 28 — {@code else if}.
 * Chain several checks when you have more than two outcomes, such as HTTP 200 / 404 / 500.
 */
public class ElseIfDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 28: else if ===");

        printMeaning(200);
        printMeaning(404);
        printMeaning(500);
        printMeaning(302);
    }

    private static void printMeaning(int statusCode) {
        System.out.print("status " + statusCode + " → ");
        if (statusCode == 200) {
            System.out.println("OK. The request succeeded.");
        } else if (statusCode == 404) {
            System.out.println("Not Found. The URL or resource is missing.");
        } else if (statusCode == 500) {
            System.out.println("Server Error. The application failed.");
        } else {
            System.out.println("Some other status. Investigate.");
        }
    }
}
