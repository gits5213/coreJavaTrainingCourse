package com.sdet.projects.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FileAnalyzer {

    public AnalysisResult analyze(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        int failCount = 0;
        Set<String> statusTags = new LinkedHashSet<>();
        Set<String> uniqueWords = new LinkedHashSet<>();

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] tokens = line.split("\\s+");
            for (String token : tokens) {
                uniqueWords.add(token);
            }

            String status = tokens[tokens.length - 1].toUpperCase(Locale.ROOT);
            if ("PASS".equals(status) || "FAIL".equals(status)) {
                statusTags.add(status);
            }
            if ("FAIL".equals(status)) {
                failCount++;
            }
        }

        return new AnalysisResult(lines.size(), failCount, statusTags, uniqueWords);
    }
}
