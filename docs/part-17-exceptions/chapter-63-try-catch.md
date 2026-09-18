# Chapter 63 — try / catch

## 1. Today's Goal

By the end of this lesson, you will wrap risky work in `try` and handle a **specific** exception in `catch`.

You will turn a crash into a controlled message:

```java
try {
    int code = Integer.parseInt("hello");
    System.out.println(code);
} catch (NumberFormatException e) {
    System.out.println("Not a number: " + e.getMessage());
}
```

You will also learn the **SDET rule**: never catch `Exception` with an empty body to hide a test failure.

## 2. Why It Matters

Unhandled exceptions stop the program. Sometimes you *want* to stop — a test should fail. Sometimes you want to **report** and continue to the next test, or show a clean error instead of a raw stack dump to a user.

`try` / `catch` is how Java gives you that choice.

The danger is the other extreme: catching everything and doing nothing. Then CI is green, production is on fire, and nobody knows why.

```text
No catch     →  crash (honest, sometimes too loud)
Specific catch →  you decide the next step
Empty catch    →  you lie
```

## 3. Real-Life Analogy

A laboratory test tube might shatter.

```text
try:
    pour the chemical

catch (ShatterException):
    put on gloves
    write in the log
    do NOT pretend the experiment succeeded
```

You do not catch "any possible disaster including the building collapsing" and then walk away whistling.

A spell-checker is another picture. `try` is reading a word. `catch (UnknownWord)` is "I cannot parse this." Empty catch is throwing the essay in the trash and stamping PASSED.

## 4. Illustrated Explanation

```text
enter try
   │
   ├─ all lines succeed ──────────────────────► skip catch ► continue after
   │
   └─ throw NumberFormatException
            │
            ▼
      is it NumberFormatException?
            │
           Yes  →  run catch body  →  continue after try/catch
            │
           No   →  not this catch  →  keep bubbling
```

```text
try {
    A
    B   ← throws here
    C   ← skipped
} catch (NumberFormatException e) {
    D   ← runs
}
E       ← runs after a handled exception
```

Multiple catch blocks (preview):

```text
try { ... }
catch (NumberFormatException e) { ... }
catch (NullPointerException e)  { ... }
```

Java picks the **first matching** type. Put more specific types before more general ones.

The forbidden pattern:

```text
try {
    assert status == 200;
} catch (Exception e) {
}
```

```text
Test failure thrown
        │
        ▼
empty catch eats it
        │
        ▼
CI says SUCCESS
        │
        ▼
users suffer
```

## 5. Syntax / Concept

```java
try {
    // risky work
} catch (NumberFormatException e) {
    // handle THIS type
}
```

- `try` must be followed by `catch` and/or `finally` (finally is next chapter).
- The variable `e` is the exception **object**. Use `e.getMessage()` and, when debugging, `e.printStackTrace()`.
- Catch the **narrow** type you expect.

You can name the variable anything legal (`ex`, `nfe`). Beginners often use `e`.

Multi-catch (same handling for two types):

```java
catch (NumberFormatException | NullPointerException e) {
    System.out.println("Bad input: " + e.getMessage());
}
```

**SDET RULE (memorize):**

> Never write `catch (Exception e) { }` to keep a test green.  
> If a test throws, the test failed. Log the exception. Re-throw it, or fail the assertion. Empty catch hides product bugs.

If you must catch a broad type in a test runner (frameworks do this at the edges), you still **record failure**. You never swallow.

```java
// BAD
} catch (Exception e) {
}

// STILL BAD
} catch (Exception e) {
    System.out.println("ignored");
}

// BETTER: specific type, visible handling
} catch (NumberFormatException e) {
    System.out.println("TEST FAILED — status was not a number: " + raw);
}

// FOR TESTS: you may catch to add context, then fail
} catch (NumberFormatException e) {
    throw new AssertionError("Status text was not an int: " + raw, e);
}
```

`AssertionError` here is "this test did not pass." Wrapping keeps the original exception as the **cause**.

## 6. Simple Example

```java
public class TryCatchParse {

    public static void main(String[] args) {
        String raw = "hello";
        try {
            int code = Integer.parseInt(raw);
            System.out.println("Parsed: " + code);
        } catch (NumberFormatException e) {
            System.out.println("Not a number: " + raw);
            System.out.println("Details: " + e.getMessage());
        }
        System.out.println("Program continues.");
    }
}
```

Expected output:

```text
Not a number: hello
Details: For input string: "hello"
Program continues.
```

Change `raw` to `"200"` and you get:

```text
Parsed: 200
Program continues.
```

The catch is skipped on success.

## 7. Real-World Example

A shipping form quantity:

```java
public class QuantityForm {

    public static int parseQuantity(String typed) {
        try {
            int quantity = Integer.parseInt(typed.trim());
            if (quantity < 1) {
                System.out.println("Quantity must be at least 1");
                return -1;
            }
            return quantity;
        } catch (NumberFormatException e) {
            System.out.println("Please type a whole number, not: " + typed);
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println(parseQuantity("2"));
        System.out.println(parseQuantity("two"));
        System.out.println(parseQuantity(" 4 "));
    }
}
```

The store shows a message. It does not dump a stack trace at a customer. It also does not pretend `"two"` means 2.

A bank CSV line `"balance=n/a"`: catch `NumberFormatException`, mark the row invalid, keep processing the next row. Still log the bad row. Do not skip logging.

## 8. SDET Example

```java
public class StatusParseTest {

    public static void assertStatusIsNumber(String actualText, int expected) {
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
            System.out.println("Expected a numeric status, actual text: [" + actualText + "]");
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        assertStatusIsNumber("200", 200);
        assertStatusIsNumber("hello", 200);
    }
}
```

The second case is **TEST FAILED**, not a quiet success.

**Illegal empty catch in a "test":**

```java
public static void dishonestTest() {
    try {
        int actual = Integer.parseInt("hello");
        if (actual != 200) {
            throw new AssertionError("wrong status");
        }
    } catch (Exception e) {
        // SDET RULE VIOLATION. This test never fails.
    }
    System.out.println("TEST PASSED"); // a lie
}
```

If you see this in a code review, reject it. Kindly. Firmly.

## 9. Break the Code

```java
public class BrokenCatch {

    public static void main(String[] args) {
        try {
            int code = Integer.parseInt("hello");
            System.out.println(code);
        } catch (NullPointerException e) {
            System.out.println("It was null");
        }
        System.out.println("Done");
    }
}
```

Wrong type. `parseInt("hello")` throws `NumberFormatException`, not `NullPointerException`. The catch does not match. The program still crashes. `Done` does not print.

Second break — the empty swallow:

```java
try {
    Integer.parseInt("hello");
    System.out.println("TEST PASSED");
} catch (Exception e) {
}
```

Nothing prints. The test "ran." CI might still be green if this is inside a larger runner that does not check anything else. That is the worst kind of bug: **absence of signal**.

## 10. Debug

If you thought you caught it but still crash:

1. Print the **real** exception name from the stack trace.
2. Check that `catch` uses that type or a parent of it.
3. `NumberFormatException` is a child of `IllegalArgumentException` and `RuntimeException`. Catching `RuntimeException` would work but is usually too wide in application code.

If a test always passes:

1. Search for `catch (Exception`.
2. Search for empty `{ }` after catch.
3. Add `e.printStackTrace()` temporarily, or log, or **delete the catch** and let the test framework report the throw.

Debugger:

- Breakpoint inside `try` and inside `catch`.
- Step Over the `parseInt`. If it throws, IntelliJ jumps to the matching `catch`. Inspect `e`.

```text
Did the catch type match?
    No  →  crash continues
    Yes →  did you fail the test / log the truth?
```

## 11. Student Exercise

Create `TryCatchExercise`.

Parse these strings in a loop: `"200"`, `"404"`, `"hello"`, `" 201 "`.

For each:

- `try` parseInt (trim first)
- on success print `Parsed: ...`
- on `NumberFormatException` print `TEST FAILED — not a number: ...`

After the loop, print `All rows attempted.`

## 12. Challenge

Write `safeParseInt(String raw)` that returns `Integer` — the object type — so you can return `null` when parsing fails. (We will later prefer `Optional`. Today `null` is a teaching tool; do not start returning `null` from every method in real frameworks.)

- Success: return the number.
- Failure: print a clear message and return `null`.
- `main` must check `null` before unboxing.

Add a method `runCheck(String raw, int expected)` that uses `safeParseInt`. If parse fails, print TEST FAILED. If numbers differ, print expected vs actual. Never empty-catch.

## 13. Knowledge Check

1. What does a `try` block contain?
2. When does the matching `catch` run?
3. Do lines after a handled `try/catch` still run?
4. If `catch` has the wrong exception type, what happens?
5. How do you get the message from exception object `e`?
6. Why catch `NumberFormatException` instead of `Exception` when parsing ints?
7. What is the SDET rule about empty `catch (Exception e)`?
8. True or false: catching an exception always means the test passed.
9. What is multi-catch syntax for two exception types?
10. Is it OK to catch, add context, and rethrow or wrap in `AssertionError`?

## 14. Interview Question

**Question:** How does try/catch work, and what should testers never do with it?

A strong answer:

> try runs the risky code. If that code throws, Java looks for a catch whose type matches. A matching catch runs, then the program continues after the try/catch. If the type does not match, the exception keeps bubbling. Testers should catch specific exceptions when they can add a clear failure message. They must never catch Exception with an empty body to hide failures. A swallowed exception makes CI green while the product is broken.

## 15. Homework

Take yesterday's `parseStatus` idea. Add `try/catch (NumberFormatException)`.

Inputs: `"200"`, `"hello"`, `""`, `" 500 "`.

Print TEST PASSED only when the parsed number equals an expected value you choose. Every bad parse must print TEST FAILED with the raw text.

In your notes, write the SDET rule in your own words. Do not copy it with empty eyes. Mean it.

---

## Answer Key

1. The statements that might throw.
2. When a thrown exception matches (or is a subtype of) the catch type.
3. Yes, if the exception was caught.
4. The exception is not handled there; the program can still crash.
5. `e.getMessage()`
6. You only intend to handle bad number text. Other bugs should still be visible.
7. Never use it to hide test failures. Empty catch destroys evidence.
8. False. Catching is not passing.
9. `catch (A | B e)`
10. Yes. That preserves failure and adds context.
