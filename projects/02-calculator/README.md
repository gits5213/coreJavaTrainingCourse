# Project 2 — Calculator

## Goal

Build a reusable `Calculator` with `add`, `subtract`, `multiply`, and `divide`. Guard divide-by-zero instead of letting Java throw an opaque `ArithmeticException` later.

## Concepts this practices

- Variables
- Operators (`+`, `-`, `*`, `/`)
- Methods and return values
- Input validation

## How to run

From this project directory:

```bash
mvn -q compile exec:java
```

Optional unit tests (same Arrange-Act-Assert style as Project 10):

```bash
mvn test
```

## Expected output

```text
10 + 4 = 14
10 - 4 = 6
10 * 4 = 40
10 / 4 = 2.5
10 / 0 -> Cannot divide by zero
```

## What success looks like

Four operations work for normal numbers. Divide-by-zero is rejected with a clear message. You can explain why `divide` returns `double` while the other methods return `int`.

## Stretch challenge

Add `modulo` and a `power` method. Write a test that `divide(1, 3)` is close to `0.333` using `assertEquals(expected, actual, delta)`.
