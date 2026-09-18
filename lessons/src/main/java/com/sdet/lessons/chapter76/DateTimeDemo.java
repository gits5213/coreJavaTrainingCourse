package com.sdet.lessons.chapter76;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Chapter 76 — Date and time.
 * Prefer {@code java.time} types: {@code LocalDate}, {@code LocalDateTime}, {@code Duration}.
 */
public class DateTimeDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 76: Date and Time ===");

        LocalDate today = LocalDate.of(2026, 9, 18);
        LocalDateTime started = LocalDateTime.of(2026, 9, 18, 9, 0);
        LocalDateTime finished = started.plusMinutes(45);
        Duration elapsed = Duration.between(started, finished);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        System.out.println("today = " + today);
        System.out.println("started = " + started.format(formatter));
        System.out.println("finished = " + finished.format(formatter));
        System.out.println("elapsed minutes = " + elapsed.toMinutes());
        System.out.println("Use java.time in new code. Avoid legacy Date unless an old API requires it.");
    }
}
