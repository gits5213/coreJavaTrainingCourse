package com.company.automation.utils;

import java.util.regex.Pattern;

/**
 * Classroom logger. Never log passwords, tokens, or secrets.
 */
public final class SafeLogger {

    private static final Pattern SECRET = Pattern.compile(
            "(?i)(password|token|secret|authorization)\\s*[=:]\\s*\\S+");

    public static String redact(String message) {
        if (message == null || message.isBlank()) {
            return "";
        }
        return SECRET.matcher(message).replaceAll("$1=***");
    }

    public void info(String message) {
        System.out.println("[INFO] " + redact(message));
    }
}
