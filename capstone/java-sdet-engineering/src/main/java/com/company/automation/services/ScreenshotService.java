package com.company.automation.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Failure evidence. This skeleton writes a text marker; a live run would capture a PNG.
 */
public final class ScreenshotService {

    private final Path outputDir;

    public ScreenshotService(Path outputDir) {
        this.outputDir = outputDir;
    }

    public Path capture(String testName) throws IOException {
        Files.createDirectories(outputDir);
        Path file = outputDir.resolve(testName + ".txt");
        Files.writeString(file, "fake-screenshot:" + testName);
        return file;
    }
}
