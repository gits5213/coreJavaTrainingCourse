package com.sdet.lessons.chapter42;

/**
 * A simple product created through a constructor instead of setting fields later.
 */
public class Product {

    private final String name;
    private final double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public void printDetails() {
        System.out.println(name + " costs " + price);
    }
}
