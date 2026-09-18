package com.sdet.lessons.chapter17;

/**
 * Chapter 17 — Variables.
 * A variable is a named box that holds a value you can read and update.
 */
public class VariableDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 17: Variables ===");

        int statusCode = 200;
        String browser = "Chrome";
        double responseTimeSeconds = 1.4;

        System.out.println("statusCode = " + statusCode);
        System.out.println("browser = " + browser);
        System.out.println("responseTimeSeconds = " + responseTimeSeconds);

        statusCode = 404;
        System.out.println("After update, statusCode = " + statusCode);
        System.out.println("The name stayed the same. The value in the box changed.");
    }
}
