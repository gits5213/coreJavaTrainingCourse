package com.sdet.lessons.chapter72;

import java.util.List;

/**
 * Chapter 72 — Stream {@code filter}.
 * Keep only the items that match a rule, such as successful HTTP status codes.
 */
public class StreamFilterDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 72: Stream filter ===");

        List<Integer> statusCodes = List.of(200, 200, 404, 500);
        System.out.println("all status codes = " + statusCodes);

        List<Integer> successes = statusCodes.stream()
                .filter(code -> code == 200)
                .toList();
        List<Integer> failures = statusCodes.stream()
                .filter(code -> code >= 400)
                .toList();

        System.out.println("filter code == 200 → " + successes);
        System.out.println("filter code >= 400 → " + failures);
    }
}
