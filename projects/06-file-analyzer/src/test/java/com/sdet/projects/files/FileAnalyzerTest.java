package com.sdet.projects.files;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileAnalyzerTest {

    @TempDir
    Path tempDir;

    @Test
    void countsLinesFailsAndUniqueTags() throws IOException {
        Path log = tempDir.resolve("run.txt");
        Files.write(log, List.of(
                "loginTest PASS",
                "checkoutTest FAIL",
                "searchTest PASS"
        ));

        AnalysisResult result = new FileAnalyzer().analyze(log);

        assertEquals(3, result.lineCount());
        assertEquals(1, result.failCount());
        assertEquals(Set.of("PASS", "FAIL"), result.statusTags());
        assertTrue(result.uniqueWords().contains("loginTest"));
        assertTrue(result.uniqueWords().contains("FAIL"));
    }

    @Test
    void missingFileThrowsNoSuchFileException() {
        Path missing = tempDir.resolve("does-not-exist.txt");

        assertThrows(NoSuchFileException.class, () -> new FileAnalyzer().analyze(missing));
    }
}
