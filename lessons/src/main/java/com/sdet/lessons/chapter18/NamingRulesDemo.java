package com.sdet.lessons.chapter18;

/**
 * Chapter 18 — Naming rules.
 * Names should tell a teammate what the value is. Use camelCase for variables.
 */
public class NamingRulesDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 18: Naming Rules ===");

        String username = "qa.tester";
        int expectedStatusCode = 200;
        boolean loginSucceeded = true;
        double maxResponseTimeSeconds = 2.0;

        System.out.println("Good names (camelCase, describe the value):");
        System.out.println("  username = " + username);
        System.out.println("  expectedStatusCode = " + expectedStatusCode);
        System.out.println("  loginSucceeded = " + loginSucceeded);
        System.out.println("  maxResponseTimeSeconds = " + maxResponseTimeSeconds);

        System.out.println();
        System.out.println("Avoid names like x, data1, or temp unless the scope is tiny.");
        System.out.println("Class names use PascalCase: NamingRulesDemo.");
        System.out.println("Constants use UPPER_SNAKE: MAX_RETRY_COUNT.");
    }
}
