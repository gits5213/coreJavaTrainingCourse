package com.sdet.lessons.chapter42;

/**
 * Chapter 42 — Constructors.
 * A constructor runs when you write {@code new}. It sets up the object's starting values.
 */
public class ConstructorDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 42: Constructor ===");

        Product laptop = new Product("Laptop", 899.00);
        Product mouse = new Product("Mouse", 25.50);

        laptop.printDetails();
        mouse.printDetails();
        System.out.println("new Product(...) called the constructor for each object.");
    }
}
