package com.sdet.lessons.chapter22;

/**
 * Chapter 22 — String immutability.
 * A String object never changes. Methods return a new String instead of editing the old one.
 */
public class StringImmutabilityDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 22: String Immutability ===");

        String username = "qa.tester";
        System.out.println("original username = " + username);

        username.toUpperCase();
        System.out.println("after toUpperCase() without saving = " + username);
        System.out.println("The original object did not change.");

        String upperUsername = username.toUpperCase();
        System.out.println("saved result upperUsername = " + upperUsername);
        System.out.println("original username is still = " + username);

        String fullName = username.concat("@example.com");
        System.out.println("concat created a new String: " + fullName);
        System.out.println("username is still: " + username);
    }
}
