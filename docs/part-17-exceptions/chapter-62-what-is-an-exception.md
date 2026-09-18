# Chapter 62 — What Is an Exception?

## 1. Today's Goal

By the end of this lesson, you will explain an **exception** as a Java object that represents a problem the program cannot ignore.

You will cause this on purpose:

```java
int code = Integer.parseInt("hello");
```

That line does not return a number. It **throws** `NumberFormatException`. You will read the stack trace, name checked vs unchecked exceptions, and stop treating a crash as mysterious punishment.

## 2. Why It Matters

Beginners think:

> The computer hates me.

Professionals think:

> Java created an exception object, filled it with a message and a stack trace, and threw it. If nobody catches it, the thread stops.

SDET work is full of this moment. A test expected `"200"` and the UI showed `"OK"`. `parseInt` explodes. That explosion is **information**. If you do not understand exceptions, you cannot debug automation, APIs, or even a tiny homework program.

```text
A silent wrong answer  →  dangerous
A loud exception       →  usable evidence
```

## 3. Real-Life Analogy

You order soup. The waiter brings a sealed envelope that says **PROBLEM: kitchen on fire**.

That envelope is the exception.

- The **type** of envelope is the exception class (`NumberFormatException`, `IOException`).
- The **note inside** is the message (`For input string: "hello"`).
- The **list of rooms the waiter ran through** is the stack trace.

If nobody opens the envelope, the restaurant closes for the night. That is an **unhandled** exception: the program stops.

A fire alarm is also a good picture. The alarm is not the fire. The alarm is the signal. Exceptions are signals.

```text
Fire               →  the real problem (bad data, missing file, null)
Alarm              →  the exception object
Fire department    →  catch block (next chapter)
Ignoring the alarm →  empty catch (never do this in a test)
```

## 4. Illustrated Explanation

### Normal flow

```text
Integer.parseInt("200")
        │
        ▼
   looks like a number
        │
        ▼
     int 200
        │
        ▼
   program continues
```

### Exception flow

```text
Integer.parseInt("hello")
        │
        ▼
   not a number
        │
        ▼
   create NumberFormatException
        │
        ▼
   THROW it  ─────────────────────────────────┐
                                              │
   remaining lines in this method are skipped │
                                              ▼
                         is there a catch nearby?
                           /              \
                         Yes               No
                          │                │
                          ▼                ▼
                    handle it         bubble up
                                      to the caller
                                           │
                                           ▼
                                      still nobody?
                                           │
                                           ▼
                                      crash + stack trace
```

### Stack trace as a ladder

```text
Exception in thread "main" java.lang.NumberFormatException:
For input string: "hello"
    at java.base/...Integer.parseInt(...)
    at ParseDemo.main(ParseDemo.java:6)   ← YOUR line. start here.
```

Read **top to bottom**, then find the first frame that is **your file**. That is where *your* code asked Java to do the impossible.

### Checked vs unchecked

```text
                    Throwable
                        │
          ┌─────────────┴─────────────┐
          │                           │
        Error                     Exception
     (JVM disaster)                   │
                            ┌─────────┴─────────┐
                            │                   │
                         checked            unchecked
                      (compiler nags)    (RuntimeException)
                            │                   │
                      IOException      NumberFormatException
                      SQLException     NullPointerException
                                       ArrayIndexOutOfBoundsException
                                       IllegalArgumentException
```

**Unchecked** means: the compiler does not force `try/catch`. `parseInt` can throw `NumberFormatException` and your code still compiles without a catch.

**Checked** means: the compiler says "you must handle this or declare `throws`." File reading in the next part is the classic example (`IOException`).

You did not cause a moral failure. You caused a typed event.

## 5. Syntax / Concept

`Integer.parseInt(String)` tries to turn text into an `int`.

```java
int status = Integer.parseInt("200");  // OK, status is 200
int broken = Integer.parseInt("hello"); // throws NumberFormatException
```

An **exception** is:

1. A class in the Java library (or your own class later).
2. An **object** created when something goes wrong.
3. **Thrown** with the `throw` keyword (Java's library does this for `parseInt`; you will do it yourself in Chapter 65).

If nothing **catches** it, the current method stops. The caller stops. Eventually `main` stops. The JVM prints a stack trace.

Common unchecked exceptions you already met or will meet:

| Exception | Typical cause |
| --- | --- |
| `NumberFormatException` | `parseInt("hello")`, `parseInt("")` |
| `NullPointerException` | calling a method on `null` |
| `ArrayIndexOutOfBoundsException` | `array[99]` when length is 3 |
| `IllegalArgumentException` | a method received a value it refuses |

Checked vs unchecked in one sentence:

```text
Checked   → compiler forces a decision (handle or declare)
Unchecked → compiler stays quiet; still can crash at runtime
```

`Error` (for example `OutOfMemoryError`) is not for beginner `catch` blocks. If the JVM is dying, your empty catch will not save the test suite.

## 6. Simple Example

```java
public class ParseHello {

    public static void main(String[] args) {
        System.out.println("About to parse...");
        int code = Integer.parseInt("hello");
        System.out.println("This line never runs. Code = " + code);
    }
}
```

Expected result: the program prints `About to parse...`, then crashes. You will **not** see `This line never runs`.

Typical output shape:

```text
About to parse...
Exception in thread "main" java.lang.NumberFormatException: For input string: "hello"
    at java.base/java.lang.Integer.parseInt(Integer.java:...)
    at ParseHello.main(ParseHello.java:6)
```

Now parse a real status:

```java
int code = Integer.parseInt("200");
System.out.println(code);
```

That prints `200` and finishes. Same method, different input, different story.

## 7. Real-World Example

A checkout page shows quantity as text. A bank shows a balance as text. An order API might send `"N/A"` when a field is missing.

```java
public class OrderQuantity {

    public static void main(String[] args) {
        String quantityText = "3";
        int quantity = Integer.parseInt(quantityText);
        double price = 19.99;
        double total = price * quantity;
        System.out.println("Total: " + total);
    }
}
```

If `quantityText` is `"two"` or `"—"`, `parseInt` throws. The store must not invent a total. Crashing is ruder than showing an error, but inventing `0` silently can ship an empty box for free. The exception is the honest stop.

A login age field:

```java
String ageText = "hello";
int age = Integer.parseInt(ageText); // NumberFormatException
```

The form should reject the input. Java already rejected it. Your product code must turn that into a user-visible message. Your test must notice if the product swallowed it.

## 8. SDET Example

You scrape a status code from a page or a CSV of expected results.

```java
public class StatusFromText {

    public static void parseAndCheck(String actualText, int expected) {
        int actual = Integer.parseInt(actualText);
        if (actual == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + actual);
        }
    }

    public static void main(String[] args) {
        parseAndCheck("200", 200);
        parseAndCheck("hello", 200); // exception before the if
    }
}
```

The second call never prints TEST FAILED. It throws. That is still a failure of the run. Do **not** hide it. The UI did not give a number. That is a defect or a bad locator — both deserve a red result.

Another SDET moment: config says `timeoutSeconds=abc`. Parsing throws. Better a loud exception in CI than tests that "pass" with a 0-second timeout.

```text
SDET mindset
  exception during a test  →  the test did not prove success
  empty catch              →  you destroyed the evidence
```

## 9. Break the Code

```java
public class BrokenParse {

    public static void main(String[] args) {
        String statusFromUi = "OK";
        int status = Integer.parseInt(statusFromUi);
        if (status == 200) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }
}
```

The programmer thought `"OK"` was as good as `200`. `parseInt` does not translate English. It throws `NumberFormatException`. The `if` never runs.

Another silent-looking bug: empty string.

```java
Integer.parseInt("");  // NumberFormatException
Integer.parseInt(" 200 "); // also throws: spaces are not allowed unless you trim first
```

And a cousin you will meet forever:

```java
String text = null;
Integer.parseInt(text); // NumberFormatException in current JDK (null message),
                        // or NPE in some older stories — still a crash
```

## 10. Debug

1. Read the **exception class name** first: `NumberFormatException`.
2. Read the **message**: `For input string: "OK"`.
3. Open **your** line: `BrokenParse.java:6`.
4. Print the raw text before parsing:

```java
System.out.println("RAW: [" + statusFromUi + "]");
```

Square brackets show spaces.

5. In IntelliJ, set a **breakpoint** on the `parseInt` line. Run **Debug**. Inspect `statusFromUi`. If it is `"OK"` or `"200 OK"`, you cannot parse it as a pure int. Trim, split, or fail the test with a clear message.

```text
Run  →  exception  →  read top of stack  →  open your line
     →  print the input  →  understand  →  fix the input or the test
```

Do not start wrapping everything in `try/catch` yet. First understand the throw. Catch is a tool, not a blanket.

## 11. Student Exercise

Create `ExceptionExercise`.

1. Parse `"404"` and print the int.
2. Parse `"hello"` in a **second** statement (you will crash; that is the point for this chapter).
3. Before each parse, print the string you are about to parse.
4. In a comment, write the exception class name you saw.

Then change `"hello"` to `"500"` and confirm both lines print numbers.

## 12. Challenge

Write `parseStatus(String text)` that:

- trims the string
- if the trimmed text is empty, print `Cannot parse empty status` and return `-1` without calling `parseInt`
- otherwise call `parseInt` and return the number

In `main`, try these inputs: `"200"`, `" 201 "`, `""`, `"hello"`.

You still will crash on `"hello"`. That is OK today. Next chapter you will catch it. Write a comment: `hello still throws — catch comes next.`

## 13. Knowledge Check

1. In plain English, what is an exception?
2. What does `Integer.parseInt("hello")` throw?
3. Does the line after a thrown exception in the same method run?
4. What is a stack trace for?
5. Which line of a stack trace should you open first?
6. What is the difference between checked and unchecked exceptions?
7. Is `NumberFormatException` checked or unchecked?
8. True or false: an exception is a Java object.
9. Why is a loud exception often better than a silent wrong number in a test?
10. Should you catch `Error` as a beginner to "keep tests green"?

## 14. Interview Question

**Question:** What is an exception in Java? Give an example.

A strong answer:

> An exception is an object that represents a problem at runtime. Java throws it to stop the current path unless some code catches it. A classic example is Integer.parseInt("hello"), which throws NumberFormatException because hello is not a number. Unchecked exceptions like that extend RuntimeException, so the compiler does not force try/catch. Checked exceptions, such as IOException, do force a handle-or-declare decision. In testing I treat an unexpected exception as a failed test, not as something to swallow.

## 15. Homework

Run three tiny programs (or one class with three methods):

1. `Integer.parseInt("200")` — success.
2. `Integer.parseInt("hello")` — `NumberFormatException`.
3. Access `args[0]` when you run with no arguments — `ArrayIndexOutOfBoundsException`.

For each crash, copy the **exception name**, the **message**, and **your filename:line** into your notes. Write one sentence: "An exception is a thrown object, not a mysterious mood."

Tomorrow we catch on purpose — without hiding failures.

---

## Answer Key

1. A thrown object that signals a problem the current path cannot continue honestly.
2. `NumberFormatException`
3. No.
4. It shows the call chain: which methods were running when the throw happened.
5. The first frame that is **your** class/file.
6. Checked: compiler requires handle or `throws`. Unchecked: compiler does not, but runtime can still crash.
7. Unchecked (`RuntimeException`).
8. True.
9. The test must not pretend success. The exception is evidence.
10. No. `Error` means the JVM is in serious trouble. Green tests that hide that are worthless.
