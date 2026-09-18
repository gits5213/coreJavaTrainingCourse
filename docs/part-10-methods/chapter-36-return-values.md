# Chapter 36 — Return Values

## 1. Today's Goal

By the end of this lesson, you will write methods that **give a value back** to the caller using `return`.

You will build:

```java
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
}
```

and use that boolean in `main` to print PASS or FAIL.

## 2. Why It Matters

`void` methods can only do something, like print. They do not hand an answer to the rest of the program.

Real tests need answers:

```text
Did the status match?  true/false
What is the pass count? a number
What is the cleaned message? a String
```

If `login` only prints, `main` cannot decide the next step. If `login` returns `boolean`, `main` can say: if login failed, do not open the dashboard.

## 3. Real-Life Analogy

You ask a cashier, "What is my total?" The cashier **returns** a number. You then decide whether to pay by card.

If the cashier only shouts "calculating!" and never tells you the total, you cannot pay correctly. That shout is a `void` method. The number is a return value.

A teacher returns a grade. You use the grade to decide pass or fail. The teacher should not only print "done grading" with no number.

## 4. Illustrated Explanation

```text
main
  │
  │  boolean ok = statusMatches(200, 200);
  ▼
statusMatches
  computes expected == actual
  return true
  │
  ▼
ok is true back in main
  │
  ▼
if (ok) print TEST PASSED
```

```text
METHOD BOX
inputs: expected, actual
work: compare
output arrow: true or false
```

If you forget `return` in a non-void method, Java refuses to compile.

If you `return` and then write more lines in the same branch, those lines never run.

```text
return true;
System.out.println("never");  // unreachable
```

## 5. Syntax / Concept

Replace `void` with the type you will return:

```java
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
}
```

```java
public static int passedCount(int total, int failed) {
    return total - failed;
}
```

```java
public static String clean(String raw) {
    return raw.trim();
}
```

Call and store:

```java
boolean ok = statusMatches(200, 404);
int passed = passedCount(100, 10);
String message = clean("  Login successful  ");
```

You can also use the result immediately:

```java
if (statusMatches(200, actual)) {
    System.out.println("TEST PASSED");
}
```

`return` exits the method immediately and sends the value back.

A `boolean` method should return `true` or `false`, not print PASS unless you also want printing. Cleaner design: return the answer, let the caller print. You will appreciate this when tests and logs need different messages.

## 6. Simple Example

```java
public class ReturnDemo {

    public static void main(String[] args) {
        boolean ok = statusMatches(200, 200);
        System.out.println(ok);

        boolean notOk = statusMatches(200, 404);
        System.out.println(notOk);
    }

    public static boolean statusMatches(int expected, int actual) {
        return expected == actual;
    }
}
```

Expected output:

```text
true
false
```

## 7. Real-World Example

Enough funds?

```java
public class FundsCheck {

    public static void main(String[] args) {
        boolean allowed = canWithdraw(100.00, 40.00);
        System.out.println("Allowed: " + allowed);
    }

    public static boolean canWithdraw(double balance, double amount) {
        return amount > 0 && balance >= amount;
    }
}
```

Order total:

```java
public static double lineTotal(int quantity, double unitPrice) {
    return quantity * unitPrice;
}
```

Clean a username:

```java
public static String normalizeUsername(String raw) {
    return raw.trim().toLowerCase();
}
```

Because `String` is immutable, you return the new value. The original argument in the caller does not magically change unless the caller assigns the result.

## 8. SDET Example

The curriculum method:

```java
public class StatusMatchesDemo {

    public static void main(String[] args) {
        int expected = 200;
        int actual = 404;

        if (statusMatches(expected, actual)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + actual);
        }
    }

    public static boolean statusMatches(int expected, int actual) {
        return expected == actual;
    }
}
```

Message check:

```java
public static boolean messageMatches(String expected, String actual) {
    return actual.equals(expected);
}
```

Case-insensitive:

```java
public static boolean messageMatchesIgnoreCase(String expected, String actual) {
    return actual.equalsIgnoreCase(expected);
}
```

Retry remaining:

```java
public static int retriesLeft(int max, int used) {
    return max - used;
}
```

Fast enough:

```java
public static boolean isFastEnough(double responseTime, double limit) {
    return responseTime <= limit;
}
```

You can combine in `main`:

```java
boolean pass = statusMatches(200, actual)
        && messageMatches("OK", body)
        && isFastEnough(1.1, 2.0);
```

## 9. Break the Code

```java
public static boolean statusMatches(int expected, int actual) {
    expected == actual;
}
```

This does not return the comparison. It may not compile.

```java
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
    return false;
}
```

The second `return` is unreachable.

```java
public static void statusMatches(int expected, int actual) {
    return expected == actual;
}
```

`void` cannot return a `boolean`.

Using the method incorrectly:

```java
statusMatches(200, 200);
```

If you ignore the returned value, you computed a result and threw it away. That is like `toUpperCase()` without assigning.

## 10. Debug

If IntelliJ says "missing return statement," every path must return.

```java
public static boolean statusMatches(int expected, int actual) {
    if (expected == actual) {
        return true;
    }
    return false;
}
```

The one-line `return expected == actual;` is simpler and avoids missing-else bugs.

If PASS never prints, print the returned value:

```java
System.out.println(statusMatches(expected, actual));
```

Debugger: Step Into `statusMatches`. Watch `expected`, `actual`, and the return value as you Step Out.

## 11. Student Exercise

Write:

- `statusMatches(int expected, int actual)`
- `passedCount(int total, int failed)`
- `padTrim(String text)` that returns `text.trim()`

Call all three from `main` and print the results.

## 12. Challenge

Write `evaluate(int expected, int actual)` that returns a `String`: `"TEST PASSED"` or `"TEST FAILED"`.

Then in `main`, print that string. If it is failed, also print expected and actual. Use `.equals` to check the returned string, not `==`.

## 13. Knowledge Check

1. What does `return` do?
2. What is the return type in `public static boolean statusMatches(...)`?
3. How do you store a returned boolean in `main`?
4. Why might a tester prefer returning `boolean` instead of printing inside the helper?
5. Can a `void` method return `true`?
6. What is wrong with ignoring the result of `statusMatches`?
7. Write `return` for "expected equals actual".
8. Does `return` end the method?
9. How should you compare two returned status **messages**?
10. True or false: `int` methods must return an `int` on every path.

## 14. Interview Question

**Question:** What is a return value, and how would you write a method that checks HTTP status codes?

A strong answer:

> A return value is the result a method gives back to the caller. I would write public static boolean statusMatches(int expected, int actual) { return expected == actual; }. The caller can then print TEST PASSED or TEST FAILED. Returning a boolean keeps the comparison reusable. For String messages I would return actual.equals(expected), not use ==.

## 15. Homework

Upgrade Project 1 logic into methods in a class named `HomeworkReturns`.

Required:

- `statusMatches`
- a `void printFailure(int expected, int actual)` for the three-line report
- `main` that uses both

Run 200/200 and 200/404.

---

## Answer Key

1. It sends a value back and exits the method.
2. `boolean`
3. `boolean ok = statusMatches(200, 200);`
4. So different callers can log, assert, or combine conditions.
5. No.
6. You lose the answer. The method work is wasted for decision-making.
7. `return expected == actual;`
8. Yes.
9. `equals` or `equalsIgnoreCase`.
10. True.
