package com.sdet.projects.jdbc;

public record ValidationReport(int actualCount, int expectedCount, boolean usernameFound, String expectedUsername) {

    public boolean passed() {
        return actualCount == expectedCount && usernameFound;
    }

    public String format() {
        String countLine = "users row count: " + actualCount
                + " (expected " + expectedCount + ") "
                + (actualCount == expectedCount ? "PASS" : "FAIL");
        String userLine = "username '" + expectedUsername + "' exists: " + usernameFound + " "
                + (usernameFound ? "PASS" : "FAIL");
        String summary = "Validation: " + (passed() ? "PASS" : "FAIL");
        return countLine
                + System.lineSeparator()
                + userLine
                + System.lineSeparator()
                + summary;
    }
}
