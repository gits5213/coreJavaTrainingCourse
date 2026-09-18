package com.sdet.lessons.chapter74;

import java.util.List;

/**
 * Chapter 74 — Stream {@code reduce}.
 * Combine many values into one, such as summing response times.
 */
public class StreamReduceDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 74: Stream reduce ===");

        List<Double> responseTimes = List.of(1.2, 0.8, 1.5);
        double total = responseTimes.stream()
                .reduce(0.0, Double::sum);
        System.out.println("responseTimes = " + responseTimes);
        System.out.println("reduce sum = " + total);

        List<String> parts = List.of("login", "search", "checkout");
        String joined = parts.stream()
                .reduce("", (left, right) -> left.isEmpty() ? right : left + " -> " + right);
        System.out.println("reduce join = " + joined);
    }
}
