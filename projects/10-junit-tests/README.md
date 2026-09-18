# Project 10 — JUnit Test Project

## Goal

Write JUnit 5 tests for a `Calculator` using **Arrange-Act-Assert**. This is how SDETs prove production code without clicking a browser.

## Concepts this practices

- JUnit 5 (`@Test`, `assertEquals`)
- Arrange-Act-Assert
- Maven Surefire (`mvn test`)
- Separating `src/main/java` from `src/test/java`

The calculator here reuses the Project 2 idea (`add` / `subtract`). Keep tests independent; JUnit may run methods in any order.

## How to run

From this project directory:

```bash
mvn test
```

## Expected output

Surefire should report something like:

```text
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## What success looks like

Every test follows Arrange (create `Calculator`, pick inputs), Act (one method call), Assert (`assertEquals(expected, actual)` — expected first). `mvn test` is green. You can explain why a failing `assertEquals` is a signal, not a nuisance.

## Stretch challenge

Add `multiply` and `divide` to `Calculator`, then test divide-by-zero with `assertThrows(IllegalArgumentException.class, () -> calculator.divide(1, 0))`.
