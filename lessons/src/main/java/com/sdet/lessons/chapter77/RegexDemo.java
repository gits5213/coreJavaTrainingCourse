package com.sdet.lessons.chapter77;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Chapter 77 — Regular expressions.
 * A regex describes a text pattern, such as an email or a three-digit status code.
 */
public class RegexDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 77: Regular Expressions ===");

        Pattern emailPattern = Pattern.compile("^\\S+@\\S+\\.\\S+$");
        System.out.println("qa@example.com matches email? "
                + emailPattern.matcher("qa@example.com").matches());
        System.out.println("not-an-email matches email? "
                + emailPattern.matcher("not-an-email").matches());

        Pattern statusPattern = Pattern.compile("\\b(\\d{3})\\b");
        Matcher matcher = statusPattern.matcher("API returned 200 then 404");
        System.out.print("status codes found:");
        while (matcher.find()) {
            System.out.print(" " + matcher.group(1));
        }
        System.out.println();
        System.out.println("matches() asks if the whole string fits. find() hunts inside the text.");
    }
}
