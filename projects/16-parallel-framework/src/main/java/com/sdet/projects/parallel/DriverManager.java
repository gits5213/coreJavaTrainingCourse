package com.sdet.projects.parallel;

/**
 * Thread-local driver holder. Each test thread must {@link #create()} in setup
 * and {@link #unload()} in teardown ({@code quit} + {@code remove}).
 */
public final class DriverManager {

    private static final ThreadLocal<FakeDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void create() {
        DRIVER.set(new FakeDriver());
    }

    public static FakeDriver get() {
        FakeDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No driver bound to this thread. Call DriverManager.create() first.");
        }
        return driver;
    }

    public static void unload() {
        FakeDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
        }
        DRIVER.remove();
    }
}
