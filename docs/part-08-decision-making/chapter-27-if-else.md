# Chapter 27 — `if` / `else`

## 1. Today's Goal

By the end of this lesson, you will use `if` and `else` together so the program always chooses one of two paths.

You will write the classic tester decision:

```java
if (statusCode == 200) {
    System.out.println("PASS");
} else {
    System.out.println("FAIL");
}
```

## 2. Why It Matters

Yesterday's `if` could print PASS and otherwise stay quiet. Tests usually need an answer either way.

A login either succeeds or it does not. A payment is approved or declined. A status matches or it does not.

`else` means:

> If the condition was not true, do this instead.

Now the program cannot "forget" to report a failure.

## 3. Real-Life Analogy

A fork in a road:

```text
Is the bridge open?
   /          \
 Yes           No
  ↓             ↓
Take bridge   Take tunnel
```

You always take exactly one of those two roads. You do not take both. You do not take neither.

A coin has two sides. An `if / else` has two branches.

## 4. Illustrated Explanation

```text
Status 200?
  /    \
Yes    No
 ↓      ↓
PASS   FAIL
```

Flow:

```text
Evaluate condition
        │
        ├── true  → run if block
        │            skip else block
        │
        └── false → skip if block
                     run else block
        │
        ▼
continue after both blocks
```

Only one block runs.

```text
if (condition) {
    // path A
} else {
    // path B
}
```

## 5. Syntax / Concept

```java
if (statusCode == 200) {
    System.out.println("PASS");
} else {
    System.out.println("FAIL");
}
```

Rules:

- `else` attaches to the nearest `if`
- `else` does not have its own condition
- exactly one of the two blocks runs
- always use braces

You can put several statements in each block:

```java
if (statusCode == 200) {
    System.out.println("TEST PASSED");
    System.out.println("Status was 200");
} else {
    System.out.println("TEST FAILED");
    System.out.println("Status was " + statusCode);
}
```

String example:

```java
if (actualMessage.equals(expectedMessage)) {
    System.out.println("Message OK");
} else {
    System.out.println("Message mismatch");
}
```

## 6. Simple Example

```java
public class IfElseDemo {

    public static void main(String[] args) {
        int statusCode = 200;

        if (statusCode == 200) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
        }
    }
}
```

Expected output:

```text
PASS
```

Change `statusCode` to `500`:

```text
FAIL
```

## 7. Real-World Example

Banking PIN check:

```java
public class PinCheck {

    public static void main(String[] args) {
        int enteredPin = 4321;
        int savedPin = 4321;

        if (enteredPin == savedPin) {
            System.out.println("Access granted");
        } else {
            System.out.println("Access denied");
        }
    }
}
```

E-commerce in-stock check:

```java
public class StockIfElse {

    public static void main(String[] args) {
        int requested = 2;
        int inStock = 5;

        if (requested <= inStock) {
            System.out.println("Item added to cart");
        } else {
            System.out.println("Not enough stock");
        }
    }
}
```

User age gate:

```java
int age = 17;
if (age >= 18) {
    System.out.println("Account type: adult");
} else {
    System.out.println("Account type: minor");
}
```

## 8. SDET Example

The core of Project 1 lives here.

```java
public class PassOrFail {

    public static void main(String[] args) {
        int expected = 200;
        int actual = 404;

        if (actual == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + actual);
        }
    }
}
```

Expected output:

```text
TEST FAILED
Expected: 200
Actual: 404
```

Login:

```java
public class LoginIfElse {

    public static void main(String[] args) {
        boolean loginSuccessful = false;

        if (loginSuccessful) {
            System.out.println("Continue to home page");
        } else {
            System.out.println("Show error and stay on login");
        }
    }
}
```

Timeout:

```java
double responseTime = 3.2;
double limit = 2.0;

if (responseTime <= limit) {
    System.out.println("Performance PASS");
} else {
    System.out.println("Performance FAIL");
}
```

## 9. Break the Code

This program should print FAIL for a 404, but the braces are wrong.

```java
public class BrokenIfElse {

    public static void main(String[] args) {
        int statusCode = 404;

        if (statusCode == 200)
            System.out.println("PASS");
            System.out.println("All good");
        else
            System.out.println("FAIL");
    }
}
```

This often does not even compile, because `else` is left hanging after a line that is not part of the `if`.

Another logic bug:

```java
if (actual == expected) {
    System.out.println("TEST FAILED");
} else {
    System.out.println("TEST PASSED");
}
```

The messages are swapped.

## 10. Debug

Always wrap both branches in braces, even for one line.

```java
public class FixedIfElse {

    public static void main(String[] args) {
        int statusCode = 404;

        if (statusCode == 200) {
            System.out.println("PASS");
            System.out.println("All good");
        } else {
            System.out.println("FAIL");
        }
    }
}
```

For swapped messages, read the condition in English:

```text
if actual equals expected → that is success → print PASSED
else → print FAILED
```

Debugger practice:

1. Breakpoint on the `if`.
2. Confirm `actual` and `expected`.
3. Step into the branch you believe should run.
4. If you land in the other branch, either the condition is inverted or the values are not what you thought.

Print expected and actual on failure. Silent FAIL is hard to debug. Visible numbers are easy to debug.

## 11. Student Exercise

Write `IfElseExercise` for a created-user API.

- expected status `201`
- actual status `201`
- expected message `User created`
- actual message `User created`

If both the status matches (`==`) and the message matches (`.equals`), print `TEST PASSED`. Otherwise print `TEST FAILED`.

Hint: you can put `&&` in the `if` condition.

Then change the actual message to `User created ` with a trailing space and observe FAIL. Trim if you want to recover PASS.

## 12. Challenge

A checkout test passes only when:

- payment status is `200`
- **or** the order is marked `prepaid` (boolean)

If that combined condition is true, print `CHECKOUT PASS`. Else print `CHECKOUT FAIL` and print the payment status.

Show two runs in comments: one that passes because of prepaid, and the values you used.

## 13. Knowledge Check

1. How many branches of an `if / else` run for one decision?
2. Does `else` have a condition?
3. What prints when status is 404 in the first SDET example with expected 200?
4. Why print expected and actual on failure?
5. Rewrite in English: `if (loginSuccessful) { ... } else { ... }`
6. Can each branch contain more than one statement?
7. What is dangerous about skipping braces?
8. How do you compare two messages in the condition?
9. True or false: both `PASS` and `FAIL` can print from a correct `if / else` in one run.
10. Why is `if / else` more complete than a lone `if` for test reporting?

## 14. Interview Question

**Question:** What is the difference between `if` and `if / else`?

A strong answer:

> if runs a block only when a condition is true and otherwise skips it. if / else chooses between two blocks, so exactly one of them runs. In testing, if / else lets me print TEST PASSED or TEST FAILED every time, which is clearer than printing PASS only on success and staying silent on failure.

## 15. Homework

Build `HomeworkPassFail` with hard-coded expected `200` and actual `200` first. Print TEST PASSED. Then change actual to `500` and print TEST FAILED with both numbers.

Add a second `if / else` that checks whether `responseTime <= 2.0`.

---

## Answer Key

1. One.
2. No. It runs when the `if` condition is false.
3. TEST FAILED, then Expected: 200, then Actual: 404.
4. So you can see the mismatch without guessing.
5. If login worked, do the success path. Otherwise do the failure path.
6. Yes.
7. Extra lines may run unconditionally, or `else` may not bind where you think.
8. `actualMessage.equals(expectedMessage)`
9. False.
10. Because every run reports an outcome.
