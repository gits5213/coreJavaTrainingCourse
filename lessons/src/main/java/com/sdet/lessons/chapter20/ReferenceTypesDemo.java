package com.sdet.lessons.chapter20;

/**
 * Chapter 20 — Reference types.
 * A reference variable holds an address of an object, not the object itself.
 */
public class ReferenceTypesDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 20: Reference Types ===");

        String browser = "Chrome";
        String[] browsers = {"Chrome", "Firefox", "Edge"};

        System.out.println("String browser points at text: " + browser);
        System.out.println("String[] browsers points at an array object:");
        System.out.println("  index 0 = " + browsers[0]);
        System.out.println("  index 1 = " + browsers[1]);
        System.out.println("  index 2 = " + browsers[2]);

        String alias = browser;
        System.out.println("alias points at the same String as browser: " + alias);

        int statusCode = 200;
        System.out.println("Primitive int statusCode holds the number itself: " + statusCode);
        System.out.println("If a reference has no object yet, its value is null.");
    }
}
