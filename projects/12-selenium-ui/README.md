# Project 12 — Selenium UI Automation

## Goal

Build the **shape** of a Selenium framework: `BrowserType`, `DriverFactory`, a `LoginPage` page object, and tests. `mvn test` uses an in-memory `FakeDriver` so CI and student machines do **not** need Chrome or Firefox installed.

## Concepts this practices

- Page Object Model (`LoginPage` owns locators and actions)
- Driver factory (tests ask; they do not `new ChromeDriver()`)
- Fake driver / fake element for unit tests
- Real Selenium imports in an **opt-in** live example

Architecture should evolve. This is not Grid, not waits-for-everything, not a 20-page object library on day one.

## How to run

Unit tests (no browser):

```bash
mvn test
```

Live browser (optional — you must have Chrome installed):

```bash
mvn -q compile exec:java
```

That runs `SeleniumLiveExample` against `https://the-internet.herokuapp.com/login`. Tests never start that main method.

## Expected output

```text
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## What success looks like

`LoginTest` talks only to `LoginPage`. Locators stay in the page object (`By.id("username")` conceptually). `LocalDriverFactory` shows how a real `ChromeDriver` would be created; `FakeDriverFactory` is what `mvn test` uses.

## Stretch challenge

Add an explicit wait wrapper around the live example (no `Thread.sleep`). Keep the fake-driver tests as the default green path.
