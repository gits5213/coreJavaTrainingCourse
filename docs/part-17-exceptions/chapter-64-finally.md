# Chapter 64 — finally

## 1. Today's Goal

By the end of this lesson, you will use a `finally` block for work that must run **whether the try succeeded or threw**.

You will understand this shape:

```java
try {
    // risky work
} catch (NumberFormatException e) {
    // handle
} finally {
    // always runs (almost always — see the rare exits below)
}
```

You will connect `finally` to cleanup: closing files, releasing browsers later, printing a test footer. You will also learn that modern Java often prefers **try-with-resources** for files, and `finally` is still an idea you must read in real code.

## 2. Why It Matters

A test can fail in the middle. If you opened a report file, a network connection, or (later) a WebDriver browser, someone must **put the toy back in the box**.

If cleanup lives only at the bottom of `try`, a throw **skips** it.

```text
try {
    open browser
    click login      ← throws
    close browser    ← skipped! browsers pile up. CI agents die.
}
```

`finally` is the hallway you always walk through on the way out — success door or alarm door.

## 3. Real-Life Analogy

A kitchen.

```text
try:
    cook
catch:
    deal with the smoke alarm
finally:
    turn off the stove
```

You turn off the stove if the meal is perfect **and** if the pan caught fire.

A library book: you return it whether you finished the chapter or the lights went out.

A gym locker: you take your bag when the workout ends **or** when you sprain an ankle.

## 4. Illustrated Explanation

```text
              try
               │
        ┌──────┴──────┐
        │             │
     success        throw
        │             │
        │          catch?
        │         /      \
        │       yes       no
        │        │        │
        ▼        ▼        ▼
      finally  finally  finally   ← then the exception continues if not caught
        │        │        │
        ▼        ▼        ▼
     continue  continue  crash (if uncaught)
```

```text
try { A; B; }
catch (...) { C; }
finally { D; }
E;

Happy:     A, B, D, E
Caught:    A, (throw), C, D, E
Uncaught:  A, (throw), D, then crash (E skipped)
```

Rare cases `finally` might not run: JVM crash, `System.exit`, power loss. Do not design daily code around those. Design as if `finally` is the cleanup guarantee of normal Java.

Order of printing (trace this with a finger):

```text
1. entering try
2. risky line
3. catch if thrown
4. finally
5. after
```

## 5. Syntax / Concept

Three legal shapes:

```java
try { } catch (ExceptionType e) { } finally { }

try { } finally { }   // legal: cleanup even if you do not catch

try { } catch (ExceptionType e) { }  // no finally — previous chapter
```

`finally` always needs a `try`. You cannot write `finally` alone.

Typical uses:

- print a footer
- set a `closed` flag
- close a resource if you are not using try-with-resources yet

**Try-with-resources** (preview for Part 18) is the modern way to close files:

```java
try (var reader = Files.newBufferedReader(path)) {
    // read
} // auto-close, even on throw
```

Learn `finally` anyway. You will read it in interviews, old code, and non-file cleanup (counters, locks later).

Do not use `finally` to hide failures. Cleanup is not the same as swallowing.

```java
try {
    Integer.parseInt("hello");
} finally {
    System.out.println("cleanup");
}
// exception still continues after finally if not caught
```

## 6. Simple Example

```java
public class FinallyDemo {

    public static void main(String[] args) {
        try {
            System.out.println("try: parsing");
            int code = Integer.parseInt("hello");
            System.out.println("try: parsed " + code);
        } catch (NumberFormatException e) {
            System.out.println("catch: not a number");
        } finally {
            System.out.println("finally: always");
        }
        System.out.println("after");
    }
}
```

Expected output:

```text
try: parsing
catch: not a number
finally: always
after
```

Success version — change `"hello"` to `"200"`:

```text
try: parsing
try: parsed 200
finally: always
after
```

Notice: `finally` in both stories. Catch only in the failure story.

## 7. Real-World Example

A kiosk prints a receipt. Whether payment succeeds or the card is declined, the kiosk must **eject the card**.

```java
public class CardKiosk {

    public static void charge(String amountText) {
        boolean cardInserted = true;
        try {
            int cents = Integer.parseInt(amountText);
            System.out.println("Charging " + cents + " cents");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Not charging.");
        } finally {
            if (cardInserted) {
                System.out.println("Ejecting card");
            }
        }
    }

    public static void main(String[] args) {
        charge("500");
        System.out.println("---");
        charge("abc");
    }
}
```

A warehouse scanner: log "scan session closed" in `finally` so the audit trail is complete even when a barcode is garbage.

## 8. SDET Example

Test footer and "browser close" as prints today (real WebDriver comes much later):

```java
public class TestWithFinally {

    public static void runStatusTest(String actualText, int expected) {
        System.out.println("----- TEST START -----");
        try {
            int actual = Integer.parseInt(actualText.trim());
            if (actual == expected) {
                System.out.println("TEST PASSED");
            } else {
                System.out.println("TEST FAILED");
                System.out.println("Expected: " + expected);
                System.out.println("Actual: " + actual);
            }
        } catch (NumberFormatException e) {
            System.out.println("TEST FAILED");
            System.out.println("Actual text was not a number: " + actualText);
        } finally {
            System.out.println("Closing fake browser");
            System.out.println("----- TEST END -----");
        }
    }

    public static void main(String[] args) {
        runStatusTest("200", 200);
        runStatusTest("hello", 200);
    }
}
```

Both tests print START, a result, close, END. The failed parse still fails. `finally` did not hide it.

If you only closed the browser at the end of `try`, the `"hello"` case would leak a browser.

## 9. Break the Code

```java
public class BrokenFinally {

    public static void main(String[] args) {
        try {
            System.out.println("open resource");
            int n = Integer.parseInt("hello");
            System.out.println("close resource");
            System.out.println(n);
        } catch (NumberFormatException e) {
            System.out.println("bad number");
        }
    }
}
```

`close resource` is inside `try` after the throw point. It never runs. The catch runs. The resource stays "open."

Fix: move close to `finally` (or use try-with-resources later).

Another bug: empty catch plus finally that prints PASSED.

```java
try {
    Integer.parseInt("hello");
} catch (Exception e) {
} finally {
    System.out.println("TEST PASSED");
}
```

Two crimes: swallowed exception, and `finally` used as a fake success banner. `finally` is not "the happy ending." It is "the exit door."

## 10. Debug

If a file stays locked or a browser stays open:

1. Look at `try`. Is close **after** a line that can throw?
2. Move cleanup to `finally`, or use try-with-resources.
3. Put a breakpoint in `finally`. Run the failing path. Confirm you still get there.

If `finally` seems not to run, you might have:

- looked at a different method
- called `System.exit` (homework: do not)
- crashed the JVM (OutOfMemory) — rare in these exercises

Trace on paper with two columns: **success input** and **throwing input**. Write every `println` in order. Compare to the Run window.

```text
Cleanup after throw?  If no, you leaked something.
```

## 11. Student Exercise

Create `FinallyExercise`.

- `try`: print `Opening report`, parse an int from a variable you can change.
- `catch (NumberFormatException)`: print `TEST FAILED — bad int`.
- `finally`: print `Closing report`.
- After the whole structure: print `Main still running`.

Run twice: valid number, `"hello"`. Confirm `Closing report` in both.

## 12. Challenge

Simulate three tests in a loop: `"200"`, `"404"`, `"n/a"` against expected `200`.

Each iteration must print:

```text
TEST START
... result ...
TEST END
```

Use `try/catch/finally`. Count how many passed and how many failed (failed includes parse errors **and** wrong numbers). Print a summary **after** the loop. Do not empty-catch. Do not print TEST PASSED inside `finally`.

## 13. Knowledge Check

1. When does `finally` run relative to `try` and `catch`?
2. Does `finally` run if `try` succeeds?
3. Does `finally` run if `try` throws and `catch` handles it?
4. Does `finally` run if `try` throws and there is **no** catch?
5. Can you write `try` + `finally` with no `catch`?
6. Why is cleanup inside `try` after a risky line a bug?
7. Is `finally` a good place to print TEST PASSED? Why or why not?
8. What modern feature often replaces `finally` for closing files?
9. Name one rare case `finally` might not run.
10. True or false: `finally` swallowing exceptions is the SDET standard.

## 14. Interview Question

**Question:** What is `finally` used for?

A strong answer:

> finally runs after try, and after catch if there was one. It is for cleanup that must happen on both success and failure: close a file, stop a driver, print a test footer. If the exception was not caught, finally still runs, then the exception continues. I would not put TEST PASSED in finally, because finally is not success. For files I prefer try-with-resources, but I still need to understand finally when I read code. Testers must not use finally or catch to hide failures.

## 15. Homework

Write `HomeworkFinally` that opens a fake "connection" (`boolean open = true`), tries to parse a user id from text, catches `NumberFormatException`, and in `finally` sets `open = false` and prints `Connection closed. Open? false`.

Run with `"42"` and `"abc"`. Both must close.

In notes: draw the success vs throw diagrams from section 4 without looking. Then look. Fix your drawing.

---

## Answer Key

1. After the try, and after catch if catch ran.
2. Yes.
3. Yes.
4. Yes, then the exception keeps going.
5. Yes.
6. A throw skips the rest of `try`, so close never happens.
7. No. `finally` runs on failure too. It would lie.
8. Try-with-resources.
9. `System.exit`, JVM crash, power loss (any honest rare example).
10. False. Never hide failures.
