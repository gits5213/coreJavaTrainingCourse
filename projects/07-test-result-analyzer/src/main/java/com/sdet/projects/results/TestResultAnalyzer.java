package com.sdet.projects.results;

import java.util.List;
import java.util.Objects;

public class TestResultAnalyzer {

    public List<String> failedNames(List<TestResult> results) {
        Objects.requireNonNull(results, "results");
        return results.stream()
                .filter(result -> result.statusCode() != 200)
                .map(TestResult::name)
                .toList();
    }

    public long totalDurationMs(List<TestResult> results) {
        Objects.requireNonNull(results, "results");
        return results.stream()
                .map(TestResult::durationMs)
                .reduce(0L, Long::sum);
    }
}
