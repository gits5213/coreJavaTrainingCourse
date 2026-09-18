# Project 15 — Cross-Browser Framework

## Goal

Select a browser from configuration (`-Dbrowser=chrome|firefox|edge`, default `chrome`) and reuse **one** test method across `BrowserType` values. Drivers are fakes so `mvn test` does not need Chrome, Firefox, or Edge installed.

## Concepts this practices

- `BrowserType` enum
- `DriverFactory` as the only place that constructs a driver
- System property config (`browser`)
- Parameterized tests instead of copy-paste `chromeTest` / `firefoxTest` / `edgeTest`

On a real machine you would skip a browser only if the OS cannot run it, and document that skip. Here every type is exercised through `FakeDriver`.

## How to run

Default (chrome):

```bash
mvn test
```

Override the configured browser (still a fake driver):

```bash
mvn test -Dbrowser=firefox
```

## Expected output

```text
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## What success looks like

Changing `-Dbrowser` does not require a new test class. The parameterized method proves the same login page object works for Chrome, Firefox, and Edge.

## Stretch challenge

Read browser from a `.properties` file when the system property is absent (the capstone does this).
