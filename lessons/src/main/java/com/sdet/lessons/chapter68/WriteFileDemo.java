package com.sdet.lessons.chapter68;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Chapter 68 — Writing a text file.
 * Output goes to {@code lessons/output/}, which is gitignored. Do not commit generated files.
 */
public class WriteFileDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 68: Write File ===");

        Path outputDir = resolveOutputDirectory();
        Path outputFile = outputDir.resolve("write-demo.txt");
        String report = """
                SDET lesson write demo
                status=200
                passed=true
                """;

        try {
            Files.createDirectories(outputDir);
            Files.writeString(outputFile, report, StandardCharsets.UTF_8);
            System.out.println("Wrote " + outputFile.toAbsolutePath());
            System.out.println("Read back:");
            System.out.println(Files.readString(outputFile, StandardCharsets.UTF_8));
        } catch (IOException exception) {
            System.out.println("Could not write file: " + exception.getMessage());
        }
    }

    /**
     * IntelliJ usually starts in {@code lessons/}. Maven {@code exec:java} from the repo root
     * keeps that working directory, so we write under {@code lessons/output} in both cases.
     */
    private static Path resolveOutputDirectory() {
        Path current = Path.of("").toAbsolutePath();
        if (Files.isRegularFile(current.resolve("lessons").resolve("pom.xml"))) {
            return current.resolve("lessons").resolve("output");
        }
        return current.resolve("output");
    }
}
