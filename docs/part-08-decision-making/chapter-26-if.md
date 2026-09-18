# Chapter 26 — `if`

## 1. Today's Goal

By the end of this lesson, you will use an `if` statement so some lines of code run **only when a condition is true**.

You will write:

```java
if (statusCode == 200) {
    System.out.println("PASS");
}
```

and understand what happens when the status is not 200.

## 2. Why It Matters

Not every line should always run.

You should not print `PASS` for every response. You should not lock an account on every login. You should not charge a card if the cart is empty.

`if` is how a program asks a yes/no question and then maybe does extra work.

Without `if`, a test class can only print values. With `if`, it can react.

## 3. Real-Life Analogy

A thermostat:

```text
if room is colder than 68
    turn on heat
```

If the room is already warm, the heat stays off. The rest of your day continues either way.

A security guard:

```text
if ID is valid
    open the door
```

If the ID is not valid, the guard does not open that door. The guard does not have to do the opposite action yet. That opposite action is tomorrow's `else`.

Today is only: maybe do this extra thing.

## 4. Illustrated Explanation

```text
Program starts
      ↓
Reach the if
      ↓
Is the condition true?
     / \
   Yes  No
    ↓    ↓
 Run    Skip the block
 the    (do nothing extra)
 block
    \    /
     \  /
      ↓
Continue with the next lines after the if
```

For a status code:

```text
statusCode == 200 ?
        /    \
      Yes     No
       ↓       ↓
     PASS    (no PASS printed)
```

The curly braces `{ }` are the fence around the optional work.

```text
if (condition) {
    // this is the block
    // one or more statements
}
```

If you forget the braces and write only one line, Java will attach only the next single statement to the `if`. Beginners should always use braces.

## 5. Syntax / Concept

```java
if (condition) {
    // statements that run only when condition is true
}
```

The condition must be a `boolean` expression: something that is `true` or `false`.

Examples of conditions:

```java
statusCode == 200
responseTime < 2
loginSuccessful
message.equals("Login successful")
statusCode == 200 && responseTime < 2
```

The parentheses after `if` are required.

```java
if statusCode == 200 {   // missing parentheses — invalid
```

Inside the block, indent the statements so the eye sees they belong to the `if`.

You can have code before and after:

```java
System.out.println("Checking status...");

if (statusCode == 200) {
    System.out.println("PASS");
}

System.out.println("Check complete.");
```

`Check complete.` runs whether or not the status was 200. Only `PASS` is optional.

## 6. Simple Example

```java
public class IfDemo {

    public static void main(String[] args) {
        int statusCode = 200;

        System.out.println("Checking...");

        if (statusCode == 200) {
            System.out.println("PASS");
        }

        System.out.println("Done.");
    }
}
```

Expected output:

```text
Checking...
PASS
Done.
```

Change `statusCode` to `404` and run again:

```text
Checking...
Done.
```

`PASS` disappeared. That is `if` doing its job. The program did not fail. It simply skipped the optional block.

## 7. Real-World Example

Banking: maybe print a warning.

```java
public class LowBalanceWarning {

    public static void main(String[] args) {
        double balance = 15.00;

        System.out.println("Balance: " + balance);

        if (balance < 25) {
            System.out.println("Warning: low balance");
        }

        System.out.println("Thank you for banking with us.");
    }
}
```

If the balance is `$80`, the warning is skipped. The thank-you line still prints.

E-commerce: maybe apply free shipping.

```java
public class MaybeFreeShipping {

    public static void main(String[] args) {
        double orderTotal = 72.00;
        double shipping = 7.99;

        if (orderTotal >= 50) {
            shipping = 0;
            System.out.println("Free shipping applied");
        }

        System.out.println("Shipping: " + shipping);
    }
}
```

## 8. SDET Example

Print PASS only for a successful HTTP code.

```java
public class IfStatusPass {

    public static void main(String[] args) {
        int statusCode = 200;

        if (statusCode == 200) {
            System.out.println("PASS");
        }
    }
}
```

Retry message only when needed:

```java
public class IfRetry {

    public static void main(String[] args) {
        boolean pageLoaded = false;
        int attempt = 1;

        if (!pageLoaded) {
            System.out.println("Page not loaded. Retry attempt " + attempt);
        }
    }
}
```

Login message check:

```java
public class IfMessageContains {

    public static void main(String[] args) {
        String actualMessage = "Login successful";

        if (actualMessage.contains("successful")) {
            System.out.println("Success text found");
        }
    }
}
```

Use `contains` and `equals` for text, not `==`.

## 9. Break the Code

This program is supposed to print `PASS` only for status 200. It always prints `PASS`.

```java
public class BrokenIf {

    public static void main(String[] args) {
        int statusCode = 404;

        if (statusCode == 200); {
            System.out.println("PASS");
        }
    }
}
```

Look carefully at the punctuation after the condition.

A second common bug:

```java
if (statusCode = 200) {
    System.out.println("PASS");
}
```

That uses assignment instead of comparison.

## 10. Debug

The semicolon after `if (statusCode == 200)` ends the `if` immediately. The block in braces is no longer controlled by the `if`. It always runs.

```text
if (statusCode == 200);  ← this if does nothing useful
{
    System.out.println("PASS");  ← always runs
}
```

Fix: remove the extra semicolon.

```java
public class FixedIf {

    public static void main(String[] args) {
        int statusCode = 404;

        if (statusCode == 200) {
            System.out.println("PASS");
        }
    }
}
```

Now nothing extra prints, which is correct for 404 when we only have `if` and no `else`.

Second bug: `statusCode = 200` assigns. Use `==`.

IntelliJ debug habit:

1. Set a breakpoint on the `if` line.
2. Run Debug.
3. Inspect `statusCode`.
4. Step Over and watch whether you enter the block.

If you enter the block when you should not, the condition or the semicolon is wrong.

## 11. Student Exercise

Write `IfExercise`.

Store `int actualStatus = 201;` which often means "Created."

- Print `Created` only if the status is 201.
- Print `Fast enough` only if a `double responseTime = 0.8` is less than `1.0`.
- Print `Has error` only if `String body = "User created"` contains `"error"`.

Run once, then change the body to `"Unexpected error"` and run again.

## 12. Challenge

A login attempt should print `Lock warning` only when **all** of these are true:

- login is not successful
- failed attempt count is 2 or more
- account is not already locked

Choose variables so the warning prints. Then change one variable so it does not print.

## 13. Knowledge Check

1. When does the body of an `if` run?
2. What type of expression goes in the parentheses?
3. Do lines after the `if` block still run when the condition is false?
4. Why should beginners always write curly braces?
5. Why is `if (statusCode == 200);` dangerous?
6. Write an `if` that prints `FAIL` when `testPassed` is false.
7. Can an `if` condition use `&&`?
8. Should you use `==` to compare two status **messages**?
9. True or false: `if` must always come with `else`.
10. What happens if `statusCode` is 500 in the first simple example that only prints PASS for 200?

## 14. Interview Question

**Question:** What does an `if` statement do in Java?

A strong answer:

> An if statement runs a block of code only when a boolean condition is true. If the condition is false, Java skips that block and continues. Testers use if to print PASS only for an expected status code or to retry only when a page did not load. The condition often uses == for numbers and equals for String content.

## 15. Homework

Write `HomeworkIf` that reads like a smoke check (hard-coded values are fine).

- If status is 200, print `API reachable`.
- If response time is greater than 2, print `SLOW`.
- If the message contains `Exception`, print `Investigate logs`.

Use three separate `if` statements, not `else`. Run with a passing set of values and a failing set of values.

---

## Answer Key

1. Only when the condition is true.
2. A boolean expression.
3. Yes.
4. So every intended line is clearly inside the optional block.
5. The semicolon ends the `if`, so the following block always runs.
6. `if (!testPassed) { System.out.println("FAIL"); }`
7. Yes.
8. No. Use `equals` or `contains`.
9. False. `if` can stand alone.
10. `PASS` is skipped. The program continues.
