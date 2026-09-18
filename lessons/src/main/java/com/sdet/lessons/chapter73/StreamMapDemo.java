package com.sdet.lessons.chapter73;

import java.util.List;

/**
 * Chapter 73 — Stream {@code map}.
 * Transform each item into a new value, such as turning a status code into a label.
 */
public class StreamMapDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 73: Stream map ===");

        List<String> browsers = List.of("chrome", "firefox", "edge");
        List<String> titles = browsers.stream()
                .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1))
                .toList();
        System.out.println("browsers = " + browsers);
        System.out.println("mapped titles = " + titles);

        List<Integer> statusCodes = List.of(200, 404, 500);
        List<String> labels = statusCodes.stream()
                .map(code -> code + " -> " + labelFor(code))
                .toList();
        System.out.println("mapped labels = " + labels);
    }

    private static String labelFor(int statusCode) {
        if (statusCode == 200) {
            return "OK";
        }
        if (statusCode == 404) {
            return "NOT_FOUND";
        }
        if (statusCode == 500) {
            return "SERVER_ERROR";
        }
        return "OTHER";
    }
}
