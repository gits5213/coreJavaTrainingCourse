# Chapter 31 — `while` Loop

## 1. Today's Goal

By the end of this lesson, you will write a `while` loop that keeps repeating **as long as a condition stays true**.

You will model retries:

```text
Attempt 1
Attempt 2
Attempt 3
```

## 2. Why It Matters

A `for` loop shines when you know the count in advance. A `while` loop shines when you keep going based on a situation:

- while the page is not loaded, retry
- while attempts remain, try login
- while there is more input, read the next line (later)

SDET retry logic is naturally a `while` story.

## 3. Real-Life Analogy

Knocking on a door:

```text
while nobody has opened the door
    AND you have not knocked 3 times
        knock again
```

You do not always know that you will knock exactly three times. Maybe the door opens on knock 1. The condition decides.

A video game: while lives remain, keep playing.

## 4. Illustrated Explanation

```java
int retry = 1;

while (retry <= 3) {
    System.out.println("Attempt " + retry);
    retry++;
}
```

```text
retry is 1
Is 1 <= 3? Yes → print Attempt 1 → retry becomes 2
Is 2 <= 3? Yes → print Attempt 2 → retry becomes 3
Is 3 <= 3? Yes → print Attempt 3 → retry becomes 4
Is 4 <= 3? No  → stop
```

Picture:

```text
Check condition
     │
     ├── false → skip loop entirely
     │
     └── true  → run body
                    │
                    update something
                    │
                    go back to check
```

If you forget `retry++`, `retry` stays 1, `1 <= 3` stays true, and the loop never ends.

## 5. Syntax / Concept

```java
while (condition) {
    // body
}
```

The condition is checked **before** each round. If it starts false, the body never runs.

You almost always need:

1. a variable used in the condition, set before the loop
2. a change to that variable inside the loop

```java
int retry = 1;           // start
while (retry <= 3) {     // continue?
    // work
    retry++;             // move forward
}
```

`while` can also wait on a boolean:

```java
boolean pageLoaded = false;
int attempt = 1;
int max = 3;

while (!pageLoaded && attempt <= max) {
    System.out.println("Trying to load page, attempt " + attempt);
    // in a real test you would try to load, then set pageLoaded
    attempt++;
}
```

If `pageLoaded` never becomes true, `attempt` still must increase or you must `break` later so the loop can end.

## 6. Simple Example

```java
public class WhileDemo {

    public static void main(String[] args) {
        int retry = 1;

        while (retry <= 3) {
            System.out.println("Attempt " + retry);
            retry++;
        }
    }
}
```

Expected output:

```text
Attempt 1
Attempt 2
Attempt 3
```

## 7. Real-World Example

ATM: while the PIN is wrong and attempts remain, ask again. We simulate with a counter only:

```java
public class WhilePinAttempts {

    public static void main(String[] args) {
        int attempt = 1;
        int maxAttempts = 3;
        boolean pinCorrect = false;

        while (!pinCorrect && attempt <= maxAttempts) {
            System.out.println("PIN attempt " + attempt);
            attempt++;
        }

        if (!pinCorrect) {
            System.out.println("Card retained in the training simulation");
        }
    }
}
```

This demo never sets `pinCorrect` to true, so it uses all three attempts. That is intentional for showing the loop.

Shopping: while items remain to scan:

```java
int itemsLeft = 3;
while (itemsLeft > 0) {
    System.out.println("Scanning an item. Left after this: " + (itemsLeft - 1));
    itemsLeft--;
}
```

## 8. SDET Example

Retry a flaky check:

```java
public class WhileRetry {

    public static void main(String[] args) {
        int retry = 1;

        while (retry <= 3) {
            System.out.println("Attempt " + retry);
            retry++;
        }
    }
}
```

A more honest simulation, where success happens on attempt 2:

```java
public class WhileUntilSuccess {

    public static void main(String[] args) {
        int attempt = 1;
        int maxAttempts = 3;
        boolean passed = false;

        while (!passed && attempt <= maxAttempts) {
            System.out.println("Running check, attempt " + attempt);
            if (attempt == 2) {
                passed = true;
                System.out.println("PASS on attempt " + attempt);
            } else {
                System.out.println("Not yet");
            }
            attempt++;
        }

        if (!passed) {
            System.out.println("TEST FAILED after retries");
        }
    }
}
```

Expected output:

```text
Running check, attempt 1
Not yet
Running check, attempt 2
PASS on attempt 2
```

The loop stops because `passed` becomes true, even though attempt 3 never happens.

## 9. Break the Code

```java
public class BrokenWhile {

    public static void main(String[] args) {
        int retry = 1;

        while (retry <= 3) {
            System.out.println("Attempt " + retry);
        }
    }
}
```

`retry` never changes. Infinite loop.

Another bug: condition never true:

```java
int retry = 5;
while (retry <= 3) {
    System.out.println("Attempt " + retry);
    retry++;
}
```

Nothing prints. That can be valid, but it surprises people who thought retries would run.

## 10. Debug

If the program hangs:

1. Stop it.
2. Look at the condition variable.
3. Confirm the body updates that variable.

Add a debug print:

```java
System.out.println("retry is " + retry);
```

If that line prints the same number forever, you found the bug.

If the loop never starts, print the condition pieces before the loop:

```java
System.out.println(retry);
System.out.println(retry <= 3);
```

Debugger: breakpoint on `while` and on `retry++`. Watch the boolean `retry <= 3` in Evaluate Expression or by inspecting `retry`.

## 11. Student Exercise

Write a `while` loop that prints `Retry 1`, `Retry 2`, `Retry 3`, `Retry 4`.

Then write a second program where a boolean `loginSuccessful` starts false and becomes true when `attempt == 3`. Print each attempt. Stop when login succeeds or when 5 attempts are used.

## 12. Challenge

Simulate polling an API.

- `int statusCode` starts as `202` (accepted, not done)
- each loop "poll" adds 1 to `pollCount`
- on poll 4, set `statusCode` to `200`
- stop when status is 200 or pollCount reaches 6

Print `Polling ... status =` each time. After the loop, print PASS or FAIL depending on whether you got 200.

## 13. Knowledge Check

1. When is a `while` condition checked?
2. If the condition is false at the start, how many times does the body run?
3. Why must something in the body (or the condition variables) change?
4. Write a `while` that runs while `retry <= 3`.
5. How is `while` different in spirit from `for`?
6. What happens if you forget `retry++`?
7. Translate: `while (!pageLoaded && attempt <= 3)`
8. True or false: `while` can use `&&` in its condition.
9. Why are retries a good `while` example?
10. How do you stop an accidental infinite loop in IntelliJ?

## 14. Interview Question

**Question:** When would you use a `while` loop instead of a `for` loop?

A strong answer:

> I use for when I know the number of iterations or I am counting through a range. I use while when I should continue based on a condition, such as retrying while the page is not loaded and attempts remain. while checks the condition before each iteration, so if it starts false the body never runs. I must update the condition variables inside the loop so it can end.

## 15. Homework

Write `HomeworkWhileRetry` that retries a fake login up to 3 times. Hard-code success on the last attempt. Print each attempt and a final `Login OK` or `Login failed`.

Then accidentally comment out the increment once, run it, stop it, and uncomment the increment. That scare is part of the lesson.

---

## Answer Key

1. Before each iteration.
2. Zero.
3. So the condition can eventually become false.
4. `int retry = 1; while (retry <= 3) { ... retry++; }`
5. `while` is condition-driven; `for` packages counting in the header.
6. Infinite loop if the condition stays true.
7. Keep looping while the page is not loaded and we still have attempts.
8. True.
9. Because you continue until success or until a limit.
10. Use the stop button / terminate the run.
