package com.sdet.lessons.chapter21;

/**
 * Chapter 21 — String methods.
 * Strings are objects with useful methods for length, case, search, and comparison.
 */
public class StringMethodsDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 21: String Methods ===");

        String pageTitle = "  Login Page  ";
        System.out.println("original = [" + pageTitle + "]");
        System.out.println("length() = " + pageTitle.length());
        System.out.println("trim() = [" + pageTitle.trim() + "]");
        System.out.println("toUpperCase() = " + pageTitle.trim().toUpperCase());
        System.out.println("toLowerCase() = " + pageTitle.trim().toLowerCase());
        System.out.println("contains(\"Login\") = " + pageTitle.contains("Login"));
        System.out.println("startsWith(\"  Login\") = " + pageTitle.startsWith("  Login"));
        System.out.println("substring(2, 7) = " + pageTitle.substring(2, 7));
        System.out.println("isBlank() = " + pageTitle.isBlank());
        System.out.println("equals(\"Login Page\") = " + pageTitle.trim().equals("Login Page"));
        System.out.println("equalsIgnoreCase(\"login page\") = "
                + pageTitle.trim().equalsIgnoreCase("login page"));
        System.out.println("Use equals() for text. == compares references, not letters.");
    }
}
