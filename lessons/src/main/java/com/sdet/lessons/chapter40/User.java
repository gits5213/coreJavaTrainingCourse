package com.sdet.lessons.chapter40;

/**
 * Chapter 40 — A class is a blueprint.
 * {@code User} stores data (fields) and behavior (methods) for one kind of object.
 */
public class User {

    String username;
    String role;

    public void introduce() {
        System.out.println("I am " + username + " with role " + role);
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
}
