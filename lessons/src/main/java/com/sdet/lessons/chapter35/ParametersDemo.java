package com.sdet.lessons.chapter35;

/**
 * Chapter 35 — Parameters.
 * Parameters let a method receive input. {@code login} needs a username and a password.
 */
public class ParametersDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 35: Parameters ===");
        login("qa.tester", "Test123");
        login("admin.user", "Test123");
        System.out.println("Demo passwords are obvious fakes like Test123. Never log real secrets.");
    }

    private static void login(String username, String password) {
        System.out.println("Logging in as " + username + " with demo password " + password);
    }
}
