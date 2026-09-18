package com.sdet.lessons.chapter67;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Chapter 67 — Reading a text file.
 * Sample data lives in {@code src/main/resources/data.txt} and is loaded from the classpath.
 */
public class ReadFileDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 67: Read File ===");

        try {
            String text = readClasspathFile("data.txt");
            System.out.println("File contents:");
            System.out.println(text);
            System.out.println("If this file were missing, that would be a failed lesson, not empty data.");
        } catch (IOException exception) {
            System.out.println("Could not read data.txt: " + exception.getMessage());
        }
    }

    private static String readClasspathFile(String name) throws IOException {
        try (InputStream input = ReadFileDemo.class.getClassLoader().getResourceAsStream(name)) {
            if (input == null) {
                throw new IOException("Missing classpath resource: " + name);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
