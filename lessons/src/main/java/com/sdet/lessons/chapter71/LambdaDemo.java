package com.sdet.lessons.chapter71;

import java.util.List;

/**
 * Chapter 71 — Lambdas.
 * A lambda is a compact implementation of a functional interface such as {@link Validator}.
 */
public class LambdaDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 71: Lambda ===");

        List<String> browsers = List.of("Chrome", "Firefox", "Edge");
        System.out.println("forEach with a lambda:");
        browsers.forEach(item -> System.out.println("  " + item));

        Validator notBlank = value -> value != null && !value.isBlank();
        Validator looksLikeEmail = value -> value != null && value.contains("@");

        System.out.println("notBlank.isValid(\"qa.tester\") = " + notBlank.isValid("qa.tester"));
        System.out.println("notBlank.isValid(\"  \") = " + notBlank.isValid("  "));
        System.out.println("looksLikeEmail.isValid(\"qa@example.com\") = "
                + looksLikeEmail.isValid("qa@example.com"));
        System.out.println("The arrow is: given this value, run this body.");
    }
}
