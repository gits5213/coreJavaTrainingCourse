# Chapter 32 — `do-while` Loop

## 1. Today's Goal

By the end of this lesson, you will write a `do-while` loop that runs the body **at least once**, then repeats while a condition is true.

You will see why "try at least once" matches some test actions: send a request, then decide whether to retry.

## 2. Why It Matters

`while` checks first. If the check is already false, nothing happens.

Sometimes you must perform the action before you have a result to check:

```text
Do: send the request
Then: look at the status
While the status says "try again" and attempts remain
```

Menus are the classic beginner example: show the menu at least once, then maybe show it again.

In SDET work, polling and "attempt then inspect" often feel like `do-while`.

## 3. Real-Life Analogy

Taste the soup at least once, then keep seasoning **while** it is bland.

```text
do
    taste soup
while (soup is bland)
```

You cannot decide "soup is bland" before the first taste.

A store clerk: always greet the customer once. Repeat the greeting script only while more customers are in line.

## 4. Illustrated Explanation

```java
int attempt = 1;

do {
    System.out.println(attempt);
    attempt++;
} while (attempt <= 3);
```

```text
Run body first          print 1, attempt becomes 2
Check attempt <= 3      2 <= 3 true
Run body                print 2, attempt becomes 3
Check                   3 <= 3 true
Run body                print 3, attempt becomes 4
Check                   4 <= 3 false → stop
```

Compare with `while`:

```text
while                         do-while
check → maybe run             always run once → then check

If condition starts false:
while: 0 runs                 do-while: 1 run
```

Picture:

```text
   ┌──────────────┐
   │   do work    │
   └──────────────┘
          │
          ▼
   condition true?
       /     \
     Yes      No
      │        │
      └──►     stop
     back to work
```

## 5. Syntax / Concept

```java
do {
    // body
} while (condition);
```

The semicolon after `while (condition)` is required.

Beginners forget that semicolon, or they put a semicolon after `while` in a regular `while` loop. Those are different mistakes.

Structure:

```text
do {
    work
    update
} while (shouldContinue);
```

Use `do-while` when the first attempt must happen even if you are unsure about repeating.

If zero times should be possible, use `while` or `for`.

## 6. Simple Example

```java
public class DoWhileDemo {

    public static void main(String[] args) {
        int attempt = 1;

        do {
            System.out.println(attempt);
            attempt++;
        } while (attempt <= 3);
    }
}
```

Expected output:

```text
1
2
3
```

Proof that it runs once even when the condition is already false:

```java
public class DoWhileRunsOnce {

    public static void main(String[] args) {
        int attempt = 10;

        do {
            System.out.println("This still prints: " + attempt);
            attempt++;
        } while (attempt <= 3);
    }
}
```

Output:

```text
This still prints: 10
```

A `while (attempt <= 3)` with `attempt` already 10 would print nothing.

## 7. Real-World Example

Simple bank menu simulation (no real input yet):

```java
public class DoWhileMenu {

    public static void main(String[] args) {
        int shown = 0;
        int timesToShow = 2;

        do {
            shown++;
            System.out.println("1) Balance  2) Deposit  3) Exit");
            System.out.println("Menu shown " + shown + " time(s)");
        } while (shown < timesToShow);
    }
}
```

Checkout: always calculate total once; repeat discount application while a flag says another coupon exists. We simulate two coupons:

```java
int couponsLeft = 2;
do {
    System.out.println("Applying a coupon. Remaining after this: " + (couponsLeft - 1));
    couponsLeft--;
} while (couponsLeft > 0);
```

## 8. SDET Example

Try a request at least once, then retry while status is 503 (service unavailable) and attempts remain.

```java
public class DoWhilePoll {

    public static void main(String[] args) {
        int attempt = 1;
        int maxAttempts = 3;
        int statusCode = 503;

        do {
            System.out.println("Request attempt " + attempt + " status " + statusCode);
            if (attempt == 3) {
                statusCode = 200;
            }
            attempt++;
        } while (statusCode != 200 && attempt <= maxAttempts);

        if (statusCode == 200) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
            System.out.println("Expected: 200");
            System.out.println("Actual: " + statusCode);
        }
    }
}
```

The first request happens even though we already "know" in the story that the first status is 503. That matches reality: you learn the status by sending the request.

## 9. Break the Code

```java
public class BrokenDoWhile {

    public static void main(String[] args) {
        int attempt = 1;

        do {
            System.out.println(attempt);
            attempt++;
        } while (attempt <= 3)
    }
}
```

Missing semicolon after the `while` condition. The file should not compile.

Infinite version:

```java
int attempt = 1;
do {
    System.out.println(attempt);
} while (attempt <= 3);
```

No increment.

## 10. Debug

Compiler error near `while` in a `do-while` often means the semicolon is missing.

If it compiles but hangs, the condition never becomes false. Print `attempt` each time.

If it runs once more than you wanted, remember: the body ran before the last failed check. Count on paper:

```text
start attempt = 1
print, increment
check
```

Off-by-one is common when mixing `attempt++` and `attempt <= max`.

Debugger: breakpoint on `do` and on the `while` line. You will enter the body before the first condition check.

## 11. Student Exercise

Write a `do-while` that prints `Ping 1`, `Ping 2`, `Ping 3`.

Then copy the program to a second class, set the starting ping number to `99`, keep the condition `ping <= 3`, and show that one line still prints.

## 12. Challenge

Write `DoWhileLogin`.

- Always attempt login at least once.
- Success happens only when `attempt == 1` in one run, and never in a second run you simulate by changing a boolean `serverDown`.
- When `serverDown` is true, keep retrying until 3 attempts, then print TEST FAILED with expected login success vs actual failure.

You may use `if` inside the loop.

## 13. Knowledge Check

1. How many times does a `do-while` body run at minimum?
2. When is the condition checked?
3. Is the semicolon after `while (condition)` required?
4. When would `while` be a better choice than `do-while`?
5. What is the output of the simple 1..3 demo?
6. Why send an HTTP request in a `do-while` retry?
7. True or false: if the condition is false, `do-while` still runs once.
8. What bug makes `do-while` infinite?
9. How does this loop differ from `for`?
10. Name one non-SDET example of "at least once."

## 14. Interview Question

**Question:** What is the difference between `while` and `do-while`?

A strong answer:

> while checks the condition first, so the body may run zero times. do-while runs the body first and checks afterward, so the body always runs at least once. That fits cases where I must try an action before I can know whether to repeat, such as sending a request and then retrying while the status is 503. I still need an update or limit so the loop can finish.

## 15. Homework

Write `HomeworkDoWhile` that prints attempt numbers at least once. Compare it in comments with a `while` version using the same start value of `attempt = 5` and condition `attempt <= 3`. Record both outputs in comments.

---

## Answer Key

1. Once.
2. After the body.
3. Yes.
4. When zero iterations should be allowed.
5. `1` then `2` then `3`
6. Because the status is unknown until the first call happens.
7. True.
8. Forgetting to change the condition variable.
9. `for` packages counting in the header; `do-while` is condition-driven and always runs once.
10. Tasting soup, showing a menu, greeting a customer.
