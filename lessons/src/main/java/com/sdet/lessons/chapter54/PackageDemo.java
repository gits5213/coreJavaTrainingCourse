package com.sdet.lessons.chapter54;

/**
 * Chapter 54 — Packages.
 * The first line of this file is {@code package com.sdet.lessons.chapter54;}.
 * The folder path {@code com/sdet/lessons/chapter54} must match that name.
 * Packages group related classes and keep names unique, for example
 * {@code com.company.project.pages.LoginPage} vs {@code com.company.project.models.User}.
 */
public class PackageDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 54: Packages ===");
        System.out.println("This class lives in package " + PackageDemo.class.getPackageName());
        System.out.println("Full name: " + PackageDemo.class.getName());
        System.out.println("Typical SDET tree:");
        System.out.println("  com.company.project.pages");
        System.out.println("  com.company.project.api");
        System.out.println("  com.company.project.models");
        System.out.println("  com.company.project.utils");
        System.out.println("  com.company.project.tests");
    }
}
