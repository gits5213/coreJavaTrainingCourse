package com.sdet.lessons.chapter40;

/**
 * Chapter 40 / 41 — Objects.
 * {@code new User()} builds one object from the class blueprint and stores it in memory.
 */
public class ObjectDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 40: Class and Object ===");

        User tester = new User();
        tester.username = "qa.tester";
        tester.role = "tester";

        User admin = new User();
        admin.username = "qa.admin";
        admin.role = "admin";

        tester.introduce();
        admin.introduce();

        System.out.println("tester.isAdmin() = " + tester.isAdmin());
        System.out.println("admin.isAdmin() = " + admin.isAdmin());
        System.out.println("Two objects. Same class. Different field values.");
    }
}
