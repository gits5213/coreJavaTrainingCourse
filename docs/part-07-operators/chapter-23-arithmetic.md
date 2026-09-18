# Chapter 23 — Arithmetic Operators

## 1. Today's Goal

By the end of this lesson, you will use Java's arithmetic operators to add, subtract, multiply, divide, and find a remainder.

You will understand why `7 / 2` can be `3` in Java, and you will calculate simple test totals.

## 2. Why It Matters

Programs calculate constantly:

- order total = price × quantity
- remaining attempts = max retries − used retries
- passed tests = total tests − failed tests
- average response time = total time / number of calls

If you cannot do arithmetic in Java, you can only print values. You cannot produce new values from old ones.

## 3. Real-Life Analogy

A calculator has buttons:

```text
+   add
-   subtract
*   multiply     (the calculator × button)
/   divide
%   remainder    (what is left after division)
```

`%` is the "leftover" button. If 10 cookies are shared by 3 people, each person gets 3, and 1 cookie remains:

```text
10 / 3  →  3
10 % 3  →  1
```

## 4. Illustrated Explanation

```text
int totalTests = 100;
int failed = 10;
int passed = totalTests - failed;

┌─────────────┐     ┌─────────────┐
│ totalTests  │     │ failed      │
│ 100         │     │ 10          │
└─────────────┘     └─────────────┘
        \                 /
         \               /
          −  subtraction
                 │
                 ▼
          ┌─────────────┐
          │ passed      │
          │ 90          │
          └─────────────┘
```

The five operators:

```text
10 + 3   →  13     addition
10 - 3   →  7      subtraction
10 * 3   →  30     multiplication
10 / 3   →  3      integer division (both are int)
10 % 3   →  1      remainder
```

Integer division is the surprise. When both numbers are `int`, Java throws away the fraction instead of rounding in the way many people expect.

```text
7 / 2

You might expect  3.5
int division gives 3

The .5 is dropped, not rounded up.
```

If you need a decimal result, at least one value should be a `double`:

```text
7.0 / 2    →  3.5
7 / 2.0    →  3.5
```

## 5. Syntax / Concept

```java
int sum = 10 + 3;
int difference = 10 - 3;
int product = 10 * 3;
int quotient = 10 / 3;
int remainder = 10 % 3;
```

You can use variables on either side:

```java
int totalTests = 100;
int failed = 10;
int passed = totalTests - failed;
```

### Combined with assignment

These shortcuts are common. You do not need to memorize them on day one, but you will see them:

```java
int retry = 1;
retry = retry + 1;   // retry is now 2
retry++;             // same idea: add 1
```

We will use `++` more in loops. For now, `retry = retry + 1` is perfectly clear.

### Remainder uses

`%` is useful when you care about leftover, even vs odd, or cycling:

```java
int number = 10;
int leftover = number % 2;  // 0 means even
```

### Division by zero

Dividing an `int` by `0` crashes the program. Do not do it. Later, exceptions will explain the crash more formally.

## 6. Simple Example

```java
public class ArithmeticDemo {

    public static void main(String[] args) {
        int a = 10;
        int b = 3;

        System.out.println(a + b);
        System.out.println(a - b);
        System.out.println(a * b);
        System.out.println(a / b);
        System.out.println(a % b);
    }
}
```

Expected output:

```text
13
7
30
3
1
```

Decimal division:

```java
public class DecimalDivision {

    public static void main(String[] args) {
        System.out.println(7 / 2);
        System.out.println(7.0 / 2);
    }
}
```

Expected output:

```text
3
3.5
```

## 7. Real-World Example

Checkout math:

```java
public class OrderTotal {

    public static void main(String[] args) {
        double unitPrice = 19.99;
        int quantity = 3;
        double subtotal = unitPrice * quantity;
        double tax = 5.00;
        double total = subtotal + tax;

        System.out.println("Subtotal: " + subtotal);
        System.out.println("Tax: " + tax);
        System.out.println("Total: " + total);
    }
}
```

Bank withdrawal:

```java
public class Withdrawal {

    public static void main(String[] args) {
        double balance = 100.00;
        double withdrawAmount = 20.00;
        double updatedBalance = balance - withdrawAmount;

        System.out.println("Before: " + balance);
        System.out.println("After: " + updatedBalance);
    }
}
```

## 8. SDET Example

Test reporting math is a classic use.

```java
public class TestTotals {

    public static void main(String[] args) {
        int totalTests = 100;
        int failed = 10;
        int passed = totalTests - failed;
        int remainingRetries = 3 - 1;

        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + totalTests);
        System.out.println("Retries left: " + remainingRetries);
    }
}
```

Expected output:

```text
Passed: 90
Failed: 10
Total: 100
Retries left: 2
```

Response time average:

```java
public class AverageResponseTime {

    public static void main(String[] args) {
        double firstCall = 1.2;
        double secondCall = 1.8;
        double thirdCall = 1.5;
        int numberOfCalls = 3;
        double average = (firstCall + secondCall + thirdCall) / numberOfCalls;

        System.out.println("Average seconds: " + average);
    }
}
```

Parentheses make the addition happen before division. That is the same grouping rule you used in school.

Pass rate with integers would be misleading:

```java
int passed = 1;
int total = 2;
System.out.println(passed / total);        // 0  because int division
System.out.println(passed / (double) total); // 0.5
```

You do not need to master casting today. Just remember: whole-number division drops the fraction.

## 9. Break the Code

This program is supposed to print how many tests passed. The output is wrong.

```java
public class BrokenPassedCount {

    public static void main(String[] args) {
        int totalTests = 100;
        int failed = 10;
        int passed = totalTests + failed;

        System.out.println("Passed: " + passed);
    }
}
```

It prints `110`. That cannot be right if 10 failed out of 100.

A second broken example:

```java
public class BrokenAverage {

    public static void main(String[] args) {
        int totalDuration = 5;
        int tests = 2;
        int average = totalDuration / tests;
        System.out.println("Average: " + average);
    }
}
```

People may expect `2.5`. The program prints `2`.

## 10. Debug

First program: the operator is wrong. Passed tests are what remains after failures, so use `-`, not `+`.

```java
int passed = totalTests - failed;
```

Second program: both values are `int`, so `5 / 2` is `2`. If you need the decimal:

```java
public class FixedAverage {

    public static void main(String[] args) {
        double totalDuration = 5;
        double tests = 2;
        double average = totalDuration / tests;
        System.out.println("Average: " + average);
    }
}
```

Debug habit: print the inputs and the formula in words.

```text
totalTests = 100
failed     = 10
passed should be total minus failed
```

If the printed number is larger than the total, you probably added when you should have subtracted.

## 11. Student Exercise

A suite ran 25 tests. 3 failed. 1 was skipped.

Compute and print:

- how many passed, if skipped tests are not counted as passed or failed
- how many tests actually executed (total minus skipped)

Then print `10 % 4` and explain the result in a comment.

## 12. Challenge

A page load is retried. Maximum attempts are 3. Each failed attempt costs 2 seconds of wait. Two attempts have already failed.

Calculate and print:

- remaining attempts
- seconds already spent waiting
- total wait if the last remaining attempt also fails

Use only arithmetic and variables. No loops yet.

## 13. Knowledge Check

1. What operator multiplies?
2. What does `%` do?
3. What is `10 - 3`?
4. What is `10 / 3` when both values are `int`?
5. What is `10 % 3`?
6. Why can `7 / 2` surprise beginners?
7. Write a line that computes passed tests from `totalTests` and `failed`.
8. What happens if you divide an `int` by `0`?
9. Why use parentheses in `(a + b + c) / 3`?
10. True or false: `*` is the same as the letter `x` in Java code.

## 14. Interview Question

**Question:** What is integer division in Java, and why does it matter in test reporting?

A strong answer:

> When both operands are integers, Java division drops the remainder instead of keeping a decimal. 7 / 2 is 3. That matters if you compute pass rates or averages with int values; you can get 0% or a whole number that hides the real fraction. Use double when the result should keep a decimal.

## 15. Homework

Create `HomeworkArithmetic` for a mini test dashboard.

Variables:

- total tests = 40
- failed = 6
- skipped = 2

Print passed, executed, failed, and leftover if you split 40 tests across 6 groups (`40 % 6`). Then compute a `double` average of three response times: 0.9, 1.4, and 2.1.

---

## Answer Key

1. `*`
2. It gives the remainder after division.
3. `7`
4. `3`
5. `1`
6. Because the fraction is dropped.
7. `int passed = totalTests - failed;`
8. The program crashes.
9. So addition happens first, then division.
10. False. You must write `*`.
