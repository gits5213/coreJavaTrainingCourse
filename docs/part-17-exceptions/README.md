# Part 17 — Exceptions

Until now, a wrong value often meant a wrong answer printed on the screen. Sometimes the program **stopped**. That stop is not random. Java threw an **exception**.

An exception is Java's way of saying:

> Something went wrong. I cannot continue this path honestly.

```text
Happy path
  parse "200"  →  int 200  →  compare  →  TEST PASSED

Exception path
  parse "hello"  →  NumberFormatException  →  program stops
                  unless you handle it
```

Testers live on exception paths. A missing file, a 500 from an API, a locator that cannot find a button, a timeout, a `null` object — all of these become exceptions in Java.

## The SDET Rule of This Part

**Never catch `Exception` with an empty body to hide a test failure.**

```text
BAD
try {
    runTest();
} catch (Exception e) {
    // silence. the test "passes." the product is broken.
}

GOOD
Let the test fail.
Or catch a specific type, log it, and still fail the test.
```

A test that swallows exceptions is a liar. Liars do not protect users.

## Checked vs Unchecked (the map)

```text
Throwable
    │
    ├── Error          (JVM in serious trouble; you almost never catch this)
    │
    └── Exception
            │
            ├── checked exceptions
            │     compiler forces you to handle or declare them
            │     example: IOException when a file is missing
            │
            └── unchecked exceptions  (RuntimeException and children)
                  compiler does not force a try/catch
                  example: NumberFormatException, NullPointerException
```

You will learn the names slowly. Do not memorize the whole family tree today.

## Chapters in This Part

| Chapter | Topic | You will be able to... |
| --- | --- | --- |
| [Chapter 62](chapter-62-what-is-an-exception.md) | What is an exception? | Explain a crash as a thrown object, starting with `Integer.parseInt("hello")` |
| [Chapter 63](chapter-63-try-catch.md) | `try` / `catch` | Handle a specific exception without hiding failures |
| [Chapter 64](chapter-64-finally.md) | `finally` | Run cleanup whether the try succeeded or failed |
| [Chapter 65](chapter-65-throw.md) | `throw` | Create and throw an exception on purpose |
| [Chapter 66](chapter-66-throws.md) | `throws` | Declare checked exceptions on a method signature |

## Prerequisite

You can write methods, `if`, loops, and arrays. You have seen at least one crash (`ArrayIndexOutOfBoundsException` in Part 11 is perfect). OOP classes help (`User`, `Order`) but you can follow with `String` and `int`.

## How This Connects to SDET Work

```text
UI click fails          → NoSuchElementException (later, Selenium)
API body is not JSON    → parse exception
Config file missing     → IOException
Status text is "N/A"    → NumberFormatException
Locator is null         → NullPointerException
```

Your job is not to catch everything. Your job is to **notice**, **report**, and **fail the test** when the product misbehaves.

## Study Tip

Type `Integer.parseInt("hello")` on purpose. Read the red stack trace from the **top**. The first line that mentions *your* class is usually the clue.

```text
problem  →  understand  →  algorithm  →  Java  →  code
   →  run  →  fail  →  debug  →  refactor  →  test  →  improve  →  architect
```

Do not start typing `try` until you can say out loud what went wrong and what you want to happen instead.
