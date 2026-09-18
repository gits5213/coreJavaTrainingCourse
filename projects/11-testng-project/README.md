# Project 11 — TestNG Project

## Goal

Learn the TestNG lifecycle that UI and API suites use in Java automation: `@BeforeMethod`, `@Test`, `@AfterMethod`, and `@DataProvider`. Compare expected vs actual HTTP status codes without opening a browser.

## Concepts this practices

- TestNG annotations and method lifecycle
- `@DataProvider` for 200 / 404 / 500 rows
- `testng.xml` as the suite entry point
- Maven Surefire configured for TestNG

JUnit (Project 10) is still the default for isolated unit tests. TestNG shows up in many Selenium/API frameworks because suites, groups, and parallel settings are first-class.

## How to run

From this project directory:

```bash
mvn test
```

## Expected output

Surefire should report something like:

```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## What success looks like

`@BeforeMethod` creates a fresh `StatusCodeValidator` for every `@Test` row. `@AfterMethod` clears it. The DataProvider feeds expected/actual pairs; you can explain a FAIL row (`200` vs `404`) without rewriting the test method.

## Stretch challenge

Add TestNG groups (`smoke` / `regression`) and a second `<test>` in `testng.xml` that runs only `smoke`.
