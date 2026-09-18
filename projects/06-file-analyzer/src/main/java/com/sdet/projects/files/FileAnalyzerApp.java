package com.sdet.projects.files;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class FileAnalyzerApp {

    public static void main(String[] args) {
        Path path = args.length > 0
                ? Path.of(args[0])
                : Path.of("src", "main", "resources", "sample-log.txt");

        System.out.println("File: " + path);

        try {
            AnalysisResult result = new FileAnalyzer().analyze(path);
            System.out.println(result.format());
        } catch (NoSuchFileException exception) {
            System.err.println("File not found: " + path.toAbsolutePath());
            System.err.println("Run this project from the module directory, or pass a file path as an argument.");
        } catch (IOException exception) {
            System.err.println("Could not read file: " + exception.getMessage());
        }
    }
}
