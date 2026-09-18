package com.sdet.lessons.chapter38;

/**
 * Chapter 38 — Arrays.
 * An array is a fixed-size list of values of one type, such as browsers Chrome, Firefox, Edge.
 */
public class ArraysDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 38: Arrays ===");

        String[] browsers = {"Chrome", "Firefox", "Edge"};
        System.out.println("length = " + browsers.length);
        System.out.println("browsers[0] = " + browsers[0]);
        System.out.println("browsers[1] = " + browsers[1]);
        System.out.println("browsers[2] = " + browsers[2]);

        browsers[1] = "Firefox ESR";
        System.out.println("After update, browsers[1] = " + browsers[1]);

        System.out.println("All browsers:");
        for (int i = 0; i < browsers.length; i++) {
            System.out.println("  " + i + ": " + browsers[i]);
        }
        System.out.println("Arrays do not grow. Use a List when the size may change.");
    }
}
