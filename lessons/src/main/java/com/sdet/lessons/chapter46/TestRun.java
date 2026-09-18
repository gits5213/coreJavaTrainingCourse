package com.sdet.lessons.chapter46;

/**
 * Shows a static constant and a static counter shared by every TestRun.
 */
public class TestRun {

    public static final int DEFAULT_TIMEOUT_SECONDS = 10;

    private static int created = 0;

    private final String name;

    public TestRun(String name) {
        this.name = name;
        created++;
    }

    public static int createdCount() {
        return created;
    }

    public void print() {
        System.out.println("TestRun \"" + name + "\" (total created = " + created + ")");
    }
}
