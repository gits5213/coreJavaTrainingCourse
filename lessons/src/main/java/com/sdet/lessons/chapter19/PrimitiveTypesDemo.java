package com.sdet.lessons.chapter19;

/**
 * Chapter 19 — Primitive types.
 * Primitives are built-in value types: numbers, characters, and booleans. They are not objects.
 */
public class PrimitiveTypesDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 19: Primitive Types ===");

        byte smallCount = 3;
        short port = 8080;
        int statusCode = 200;
        long userId = 1_000_000_001L;
        float loadTimeSeconds = 1.25F;
        double responseTimeSeconds = 1.847;
        char grade = 'A';
        boolean passed = true;

        System.out.println("byte smallCount = " + smallCount);
        System.out.println("short port = " + port);
        System.out.println("int statusCode = " + statusCode);
        System.out.println("long userId = " + userId);
        System.out.println("float loadTimeSeconds = " + loadTimeSeconds);
        System.out.println("double responseTimeSeconds = " + responseTimeSeconds);
        System.out.println("char grade = " + grade);
        System.out.println("boolean passed = " + passed);
        System.out.println("Daily SDET trio: int, double, boolean.");
    }
}
