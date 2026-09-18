package com.sdet.projects.results;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestResultAnalyzerTest {

    private final TestResultAnalyzer analyzer = new TestResultAnalyzer();

    private final List<TestResult> results = List.of(
            new TestResult("getUser", 200, 120),
            new TestResult("createUser", 201, 80),
            new TestResult("deleteUser", 500, 45),
            new TestResult("login", 401, 30),
            new TestResult("health", 200, 10)
    );

    @Test
    void failedNamesAreNon200() {
        List<String> actual = analyzer.failedNames(results);

        assertEquals(List.of("createUser", "deleteUser", "login"), actual);
    }

    @Test
    void totalDurationSumsAllResults() {
        long actual = analyzer.totalDurationMs(results);

        assertEquals(285L, actual);
    }
}
