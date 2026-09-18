package com.sdet.lessons.chapter75;

import java.util.Optional;

/**
 * Chapter 75 — {@code Optional}.
 * Optional is a box that may hold a value or may be empty. It makes "missing" visible.
 */
public class OptionalDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 75: Optional ===");

        Optional<String> foundUser = findUser("qa.tester");
        Optional<String> missingUser = findUser("ghost");

        System.out.println("foundUser.isPresent() = " + foundUser.isPresent());
        foundUser.ifPresent(name -> System.out.println("found = " + name));

        String fallback = missingUser.orElse("guest");
        System.out.println("missingUser.orElse(\"guest\") = " + fallback);
        System.out.println("Do not call get() unless you have already checked isPresent().");
    }

    private static Optional<String> findUser(String username) {
        if ("qa.tester".equals(username) || "qa.admin".equals(username)) {
            return Optional.of(username);
        }
        return Optional.empty();
    }
}
