package com.sdet.projects.results;

import java.util.List;

public class TestResultAnalyzerApp {

    public static void main(String[] args) {
        List<TestResult> results = List.of(
                new TestResult("getUser", 200, 120),
                new TestResult("createUser", 201, 80),
                new TestResult("deleteUser", 500, 45),
                new TestResult("login", 401, 30),
                new TestResult("health", 200, 10)
        );

        TestResultAnalyzer analyzer = new TestResultAnalyzer();
        List<String> failedNames = analyzer.failedNames(results);
        long totalDuration = analyzer.totalDurationMs(results);

        System.out.println("Failed names: " + failedNames);
        System.out.println("Total duration ms: " + totalDuration);
        System.out.println("Failure count: " + failedNames.size());
    }
}
