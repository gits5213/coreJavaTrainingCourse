# Driver Factory

## Goal

By the end of this lesson, you will implement `DriverFactory.create(BrowserType)` for Chrome, Firefox, and Edge, and tests will no longer call `new ChromeDriver()` directly.

## Why It Matters

When CI needs headless Chrome, you want one change. When a job says "run Firefox," you want a parameter, not 200 edits. That is Open/Closed plus Factory.

## Real-Life Analogy

A rental counter: you say "compact car" or "van." You do not assemble the engine in the parking lot.

```text
CHROME     compact
FIREFOX    sedan
EDGE       van
```

The factory knows keys, insurance, fuel (driver binaries, options).

## Illustrated Explanation

```text
Test
 ↓
DriverFactory
 ↓
ChromeDriver
or
FirefoxDriver
or
EdgeDriver
```

```text
create(CHROME)   → ChromeDriver (+ options)
create(FIREFOX)  → FirefoxDriver
create(EDGE)     → EdgeDriver
unknown          → illegal argument, fail fast
```

Config later:

```text
browser=chrome
headless=true
```

Factory reads config. Tests still call `create`.

## Syntax / Concept

```java
public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE
}

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver create(BrowserType type) {
        return switch (type) {
            case CHROME -> chrome();
            case FIREFOX -> new FirefoxDriver();
            case EDGE -> new EdgeDriver();
        };
    }

    private static WebDriver chrome() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "false"))) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }
}
```

Fail fast:

```java
public static BrowserType from(String name) {
    return BrowserType.valueOf(name.trim().toUpperCase());
}
```

Invalid `browser=safari` throws. Good. Silent default-to-Chrome hides misconfiguration.

## Simple Example

```java
WebDriver driver = DriverFactory.create(BrowserType.CHROME);
try {
    driver.get("https://example.com");
} finally {
    driver.quit();
}
```

TestNG:

```java
@BeforeMethod
@Parameters("browser")
public void setUp(String browser) {
    driver = DriverFactory.create(DriverFactory.from(browser));
}
```

If you have no XML yet:

```java
driver = DriverFactory.create(BrowserType.CHROME);
```

## Real-World Example

Banking CI matrix: Chrome on PR, Firefox+Edge nightly. Same tests, factory parameter.

E-commerce: mobile emulation is still options inside `chrome()`, not a new test class copy.

## SDET Example

```java
public abstract class UiTest {
    protected WebDriver driver;

    @BeforeMethod
    public void open() {
        driver = DriverFactory.create(BrowserType.CHROME);
    }

    @AfterMethod
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }
}
```

A base test class is OK if thin. Do not put login helpers that hide assertions. Parallel will later need ThreadLocal instead of a plain field if methods run concurrently.

## Break the Code

```java
public static WebDriver create(BrowserType type) {
    return new ChromeDriver(); // ignores type
}
```

```java
new ChromeDriver(); // in a test after factory exists
```

Two construction paths. Drift.

```java
options.addArguments("--no-sandbox"); // copied from a random blog for all laptops
```

Understand flags. Do not paste CI folklore into local Chrome without need.

## Debug

Wrong browser on CI: print the resolved `BrowserType` at INFO (not passwords). If factory swallows exceptions and returns null, teardown NPE. Let creation throw.

## Student Exercise

Implement `BrowserType` + `DriverFactory.create`. Open example.com with Chrome via factory. Quit.

## Challenge

Read `browser` from `-Dbrowser=firefox` (`System.getProperty`). Map to enum. Document the Maven command. Add Edge if you have it; otherwise throw a clear message.

## Knowledge Check

1. Draw Test → Factory → browsers.
2. Write `DriverFactory.create(BrowserType.CHROME)`.
3. Why enum not free strings everywhere?
4. Where do headless options live?
5. Why fail fast on unknown browser?
6. Who calls `quit`?
7. Why not `new ChromeDriver()` in tests afterward?
8. How does this help Open/Closed?
9. Grid next — what changes?
10. Base test class risk in parallel?

## Interview Question

**Question:** What is a DriverFactory?

A strong answer:

> Tests ask DriverFactory.create(BrowserType.CHROME) instead of constructing ChromeDriver. The factory maps CHROME, FIREFOX, EDGE to implementations and options like headless. One place changes when CI or Grid arrives. I fail fast on unknown browsers. Tests still quit the driver in AfterMethod. I do not start with Grid. Factory is enough when I have more than one browser or one set of options.

## Homework

Replace all `new ChromeDriver()` in training tests with the factory. Commit `Introduce DriverFactory`.

---

## Answer Key

1. Test → DriverFactory → Chrome/Firefox/Edge
2. As in curriculum
3. Typos become compile/valueOf errors
4. Inside factory / options helpers
5. Misconfig should not silently use Chrome
6. Test teardown (or a small owner), not the factory usually
7. Duplicate policy
8. Add a browser without rewriting tests
9. Factory returns RemoteWebDriver with capabilities
10. Shared field is unsafe; use ThreadLocal
