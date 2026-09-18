package com.sdet.lessons.chapter70;

/**
 * Chapter 70 — Generics.
 * {@code ApiResponse<UserProfile>} and {@code ApiResponse<Order>} share one envelope type with different data.
 */
public class GenericsDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 70: Generics ===");

        ApiResponse<UserProfile> userResponse = new ApiResponse<>(200, new UserProfile("qa.tester"));
        ApiResponse<Order> orderResponse = new ApiResponse<>(201, new Order("ORD-1001"));

        System.out.println("user status = " + userResponse.status + ", data = " + userResponse.data.name);
        System.out.println("order status = " + orderResponse.status + ", data = " + orderResponse.data.id);
        System.out.println("No cast needed. The compiler already knows the data types.");
    }

    static class UserProfile {
        final String name;

        UserProfile(String name) {
            this.name = name;
        }
    }

    static class Order {
        final String id;

        Order(String id) {
            this.id = id;
        }
    }
}
