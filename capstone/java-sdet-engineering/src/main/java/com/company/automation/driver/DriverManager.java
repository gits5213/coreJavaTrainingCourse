package com.company.automation.driver;

/**
 * Thread-local driver holder for parallel-safe tests.
 */
public final class DriverManager {

    private static final ThreadLocal<Driver> DRIVER = new ThreadLocal<>();
    private static final DriverFactory DEFAULT_FACTORY = new FakeDriverFactory();

    private DriverManager() {
    }

    public static Driver create(BrowserType type) {
        Driver driver = DEFAULT_FACTORY.create(type);
        DRIVER.set(driver);
        return driver;
    }

    public static Driver get() {
        Driver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No driver bound to this thread");
        }
        return driver;
    }

    public static void unload() {
        Driver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
        }
        DRIVER.remove();
    }
}
