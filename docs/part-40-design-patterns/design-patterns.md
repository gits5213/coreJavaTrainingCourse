# Design Patterns for Java SDETs

## Goal

By the end of this lesson, you will describe Factory, Builder, Strategy, Facade, Adapter, Observer, and Singleton, implement Factory and Builder in Java, and explain why Singleton is often the wrong answer — especially with parallel tests.

## Why It Matters

SDET frameworks are full of these names. If you memorize UML without a problem, you will wrap `click` in six patterns and still flake.

If you know the problem, you will say: "We needed one place to construct drivers" and reach for Factory. That is engineering.

## Real-Life Analogy

```text
Factory     a car plant: you say CHROME, you get a car of that model
Builder     a sandwich counter: chain "with cheese" "with tomato" then build()
Strategy    choosing a route: bike vs train, same "go to work" goal
Facade     a hotel concierge: one call hides taxi + restaurant + tickets
Adapter     a travel plug: UK plug to US socket
Observer    a group chat: when status changes, subscribers are notified
Singleton   a town with one mayor — useful until two towns try to share one mayor
```

## Illustrated Explanation

```text
Problem                              Pattern
Many places new ChromeDriver()       Factory
User with many optional fields       Builder
Browser choice, same test body       Strategy (often inside Factory)
Start report + log + screenshot      Facade
Third-party API that does not fit    Adapter
Test listeners, Allure steps         Observer
"One global WebDriver"               Singleton ← usually a trap
```

```text
Test
  → DriverFactory.create(CHROME)
      → ChromeDriver

Test
  → new UserBuilder().withFirstName("John").withRole("Admin").build()
      → User
```

## Factory

**Problem:** tests should not know how to construct a browser (driver path, options, headless).

```java
public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE
}

public class DriverFactory {

    public static WebDriver create(BrowserType type) {
        return switch (type) {
            case CHROME -> new ChromeDriver();
            case FIREFOX -> new FirefoxDriver();
            case EDGE -> new EdgeDriver();
        };
    }
}
```

Curriculum example:

```java
WebDriver driver = DriverFactory.create(BrowserType.CHROME);
```

Tests stay:

```java
WebDriver driver = DriverFactory.create(BrowserType.CHROME);
```

When Grid arrives, you change the factory, not 200 tests. That is Open/Closed plus Factory.

Do not build a Factory for a single class you instantiate once.

## Builder

**Problem:** `new User(String, String, String, String, String, boolean, boolean)` is unreadable.

Curriculum example:

```java
User user = new UserBuilder()
        .withFirstName("John")
        .withRole("Admin")
        .build();
```

Complete teaching version:

```java
public class User {
    private final String firstName;
    private final String role;

    User(String firstName, String role) {
        this.firstName = firstName;
        this.role = role;
    }

    public String firstName() {
        return firstName;
    }

    public String role() {
        return role;
    }
}

public class UserBuilder {
    private String firstName = "Jane";
    private String role = "User";

    public UserBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    public User build() {
        return new User(firstName, role);
    }
}
```

Java records can use a compact constructor plus a builder if optional fields pile up. Do not force a builder on a two-field record.

## Strategy

**Problem:** the *algorithm* varies (how we wait, how we authenticate) but the caller should not `if` everywhere.

```java
public interface AuthStrategy {
    void login(LoginPage page, String user, String password);
}

public class FormAuthStrategy implements AuthStrategy {
    @Override
    public void login(LoginPage page, String user, String password) {
        page.login(user, password);
    }
}

public class TokenAuthStrategy implements AuthStrategy {
    @Override
    public void login(LoginPage page, String user, String password) {
        page.loginWithSsoToken(user); // different algorithm
    }
}
```

Factory often *selects* a strategy. That is fine. Do not create Strategy interfaces with one implementation "for the architecture diagram."

## Facade

**Problem:** starting a test needs driver + wait + reporter + config. Tests should not wire all of that.

```java
public class UiTestFacade {
    public WebDriver startChromeOn(String url) {
        WebDriver driver = DriverFactory.create(BrowserType.CHROME);
        driver.get(url);
        return driver;
    }
}
```

A page object is a kind of facade over WebDriver locators. Do not add a facade in front of a facade in front of a facade.

## Adapter

**Problem:** you have a library method `send(Json json)` but your tests speak `User`.

```java
public class UserApiAdapter {
    private final LegacyHttp legacy;

    public UserApiAdapter(LegacyHttp legacy) {
        this.legacy = legacy;
    }

    public void create(User user) {
        legacy.send("{\"name\":\"" + user.firstName() + "\"}");
    }
}
```

REST Assured is already a nicer API over HTTP. You adapt *your domain* to it in an `UserApiClient`, not in every test.

## Observer

**Problem:** many things should happen when a test fails (screenshot, log, Allure attach) without the test listing them.

```java
public interface TestObserver {
    void onFailure(String testName, Throwable error);
}

public class ScreenshotOnFailure implements TestObserver {
    @Override
    public void onFailure(String testName, Throwable error) {
        System.out.println("Capture screenshot for " + testName);
    }
}
```

TestNG/JUnit listeners are observers. Use the runner's listener API before you write a custom event bus.

## Singleton

**Problem it claims to solve:** one shared instance (one config loader, one logger factory).

```java
public final class Config {
    private static final Config INSTANCE = new Config();

    private Config() {
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public String baseUrl() {
        return "https://qa.example.com";
    }
}
```

**Why it is overused:** people make `WebDriver` a singleton.

```text
Thread 1 and Thread 2 share one driver
  → clicks interleave
  → "flaky tests"
  → the pattern is the bug
```

Parallel automation needs **one driver per thread**, not one driver per JVM. That is `ThreadLocal` (Part 54), almost the opposite of a global singleton driver.

Acceptable singletons: immutable configuration read at startup, a true process-wide meter. Suspicious singletons: driver, wait, page objects, REST client with cookies, anything with mutable session state.

## SDET Example Map

```java
WebDriver driver = DriverFactory.create(BrowserType.CHROME);

User user = new UserBuilder()
        .withFirstName("John")
        .withRole("Admin")
        .build();
```

```text
Factory     DriverFactory
Builder     UserBuilder / test data
Strategy    browser options, wait policy
Facade      suite start, reporting start
Adapter     legacy API wrapper
Observer    listeners
Singleton   config maybe; driver almost never
```

## Student Exercise

Implement `BrowserType` + `DriverFactory.create` using **stub classes** if you do not have Selenium on the classpath yet:

```java
public interface WebDriver { }

public class ChromeDriver implements WebDriver { }
public class FirefoxDriver implements WebDriver { }
public class EdgeDriver implements WebDriver { }
```

Write a `main` that prints which type was created. Then implement `UserBuilder` as above.

## Challenge

Explain in writing: why a Singleton `WebDriver` breaks when two tests run at once. Draw Thread 1 / Driver 1 vs one shared driver. Do not implement ThreadLocal yet; just show you see the problem.

## Knowledge Check

1. When should you introduce a pattern?
2. What problem does Factory solve for browsers?
3. Write the `DriverFactory.create` call from the curriculum.
4. What problem does Builder solve?
5. Write the `UserBuilder` chain from the curriculum.
6. Facade vs Adapter in one sentence each.
7. Where do Observers appear in test runners?
8. Why is Singleton overused?
9. Why is a singleton WebDriver dangerous?
10. Name a legitimate singleton-like thing in a test repo.

## Interview Question

**Question:** Which design patterns have you used in automation, and why?

A strong answer:

> I use patterns when I have felt the problem. Factory: WebDriver driver = DriverFactory.create(BrowserType.CHROME) so tests do not construct browsers. Builder: User user = new UserBuilder().withFirstName("John").withRole("Admin").build() so test data stays readable. Strategy for auth or waits. Facade to start a test context. Adapter around a legacy HTTP library. Observer via TestNG listeners for screenshots. I avoid Singleton for WebDriver because parallel tests need isolated state. Singleton is often overused.

## Homework

Add `UserBuilder` to your training project. Do **not** add Observer, Adapter, and Facade all at once (YAGNI). In notes, list the problem that would justify each remaining pattern.

---

## Answer Key

1. After you understand the underlying problem; when it repeats.
2. Centralize driver construction; tests ask for a type.
3. `DriverFactory.create(BrowserType.CHROME)`
4. Readable construction of objects with many optional parts.
5. `new UserBuilder().withFirstName("John").withRole("Admin").build()`
6. Facade: simple front for a subsystem. Adapter: make an existing interface look like the one you need.
7. Listeners, reporting hooks, Allure.
8. It looks easy to grab a global; people hide messy state there.
9. Shared mutable browser session across tests/threads.
10. Immutable configuration, or a logging backend — not the driver.
