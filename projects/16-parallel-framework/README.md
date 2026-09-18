# Project 16 — Parallel Framework

## Goal

Give each parallel test its **own** fake browser via `ThreadLocal`. Shared static `WebDriver` fields are how parallel suites leak logins between tests.

## Concepts this practices

- `ThreadLocal<FakeDriver>` (the same idea as `ThreadLocal<WebDriver>`)
- `DriverManager.create()` / `get()` / `unload()` (`quit` + `remove`)
- Surefire `parallel=methods` with three worker threads
- `System.identityHashCode` to prove instances are not shared

## How to run

From this project directory:

```bash
mvn test
```

## Expected output

```text
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## What success looks like

Three test methods can overlap in time. After a short sleep, `DriverManager.get()` still returns the same object the test stored. A concurrent set of identity hash codes has three distinct values — one driver per test.

Always `quit` and `ThreadLocal.remove()` in teardown so the next test on that thread cannot inherit a stale driver.

## Stretch challenge

Switch the holder to store a small session object (driver + test name) and fail fast if `get()` is called before `create()`.
