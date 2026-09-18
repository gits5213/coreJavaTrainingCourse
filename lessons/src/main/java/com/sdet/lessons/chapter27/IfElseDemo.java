package com.sdet.lessons.chapter27;

/**
 * Chapter 27 — {@code if} / {@code else}.
 * One path runs when the check is true. The other path runs when it is false.
 */
public class IfElseDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 27: if / else ===");

        boolean loginSucceeded = true;
        if (loginSucceeded) {
            System.out.println("Login succeeded. Open the dashboard.");
        } else {
            System.out.println("Login failed. Stay on the login page.");
        }

        loginSucceeded = false;
        if (loginSucceeded) {
            System.out.println("Login succeeded. Open the dashboard.");
        } else {
            System.out.println("Login failed. Stay on the login page.");
        }
        System.out.println("Exactly one of the two branches runs.");
    }
}
