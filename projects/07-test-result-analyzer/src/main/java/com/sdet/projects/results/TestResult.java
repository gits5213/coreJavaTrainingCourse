package com.sdet.projects.results;

public record TestResult(String name, int statusCode, long durationMs) {

    public TestResult {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (durationMs < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
    }
}
