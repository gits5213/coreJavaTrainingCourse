package com.sdet.lessons.chapter46;

/**
 * Chapter 46 — {@code static}.
 * Static members belong to the class, not to one object. All objects share them.
 */
public class StaticDemo {

    public static void main(String[] args) {
        System.out.println("=== Chapter 46: static ===");

        System.out.println("Class-level default timeout = " + TestRun.DEFAULT_TIMEOUT_SECONDS);

        TestRun first = new TestRun("login");
        TestRun second = new TestRun("checkout");

        first.print();
        second.print();
        System.out.println("TestRun.createdCount() = " + TestRun.createdCount());
        System.out.println("createdCount is static. Both objects share the same counter.");
    }
}
