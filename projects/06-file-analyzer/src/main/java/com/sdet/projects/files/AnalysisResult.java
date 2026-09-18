package com.sdet.projects.files;

import java.util.Set;
import java.util.TreeSet;

public final class AnalysisResult {

    private final int lineCount;
    private final int failCount;
    private final Set<String> statusTags;
    private final Set<String> uniqueWords;

    public AnalysisResult(int lineCount, int failCount, Set<String> statusTags, Set<String> uniqueWords) {
        this.lineCount = lineCount;
        this.failCount = failCount;
        this.statusTags = Set.copyOf(statusTags);
        this.uniqueWords = Set.copyOf(uniqueWords);
    }

    public int lineCount() {
        return lineCount;
    }

    public int failCount() {
        return failCount;
    }

    public Set<String> statusTags() {
        return statusTags;
    }

    public Set<String> uniqueWords() {
        return uniqueWords;
    }

    public String format() {
        return "Lines: " + lineCount
                + System.lineSeparator()
                + "FAIL count: " + failCount
                + System.lineSeparator()
                + "Status tags: " + new TreeSet<>(statusTags)
                + System.lineSeparator()
                + "Unique words: " + new TreeSet<>(uniqueWords);
    }
}
