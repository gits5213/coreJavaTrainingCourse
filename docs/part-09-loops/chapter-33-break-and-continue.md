# Chapter 33 — `break` and `continue`

## 1. Today's Goal

By the end of this lesson, you will use `break` to stop a loop immediately and `continue` to skip the rest of the current round and move to the next one.

You will use them carefully. They are sharp tools. They make retry loops and filtered test lists easier, but they can also make code hard to follow if you scatter them everywhere.

## 2. Why It Matters

Sometimes the job is finished early:

```text
Retry up to 3 times
If the page loads on attempt 1, stop. Do not retry twice more.
```

That early stop is `break`.

Sometimes one item in a list should be skipped:

```text
For each test name
If the name starts with "WIP", skip it
Otherwise run it
```

That skip is `continue`.

Without these, you nest more `if` statements. With them, you must stay readable.

## 3. Real-Life Analogy

`break` is pulling the emergency brake on a train. The route stops now. You do not visit the remaining stations.

`continue` is skipping one house while delivering mail. You do not go home. You just jump to the next house.

```text
Houses: 1  2  3  4  5
Skip 3           continue
Stop at 4        break  (2 and 5 never get mail after that)
```

## 4. Illustrated Explanation

### `break`

```text
i = 1
print Attempt 1
page loaded? Yes → break → leave loop
Attempts 2 and 3 never happen
```

```text
LOOP
  round 1
  round 2  → break
  (round 3 skipped because we left)
AFTER LOOP
```

### `continue`

```text
i = 1 run
i = 2 continue (skip remaining lines of round 2)
i = 3 run
```

```text
round 1 → work
round 2 → continue → jump to update/next check
round 3 → work
```

`continue` in a `for` loop still does the `i++` update. It does not freeze the counter.

## 5. Syntax / Concept

```java
break;
```

Leaves the innermost loop (or `switch`, as you saw earlier).

```java
continue;
```

Skips the rest of the current iteration of the innermost loop.

Typical pattern with `if`:

```java
if (pageLoaded) {
    break;
}

if (testName.startsWith("WIP")) {
    continue;
}
```

Do not use `break` as a random goto. Prefer a clear condition in the `while` header when that is enough. Use `break` when an extra early-exit reads more naturally.

`break` and `continue` are **not** methods. No object sits in front of them. They are Java keywords.

## 6. Simple Example

```java
public class BreakDemo {

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            if (i == 3) {
                break;
            }
            System.out.println("i = " + i);
        }
        System.out.println("After loop");
    }
}
```

Expected output:

```text
i = 1
i = 2
After loop
```

`3`, `4`, and `5` never print.

```java
public class ContinueDemo {

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            if (i == 3) {
                continue;
            }
            System.out.println("i = " + i);
        }
    }
}
```

Expected output:

```text
i = 1
i = 2
i = 4
i = 5
```

`3` is skipped. The loop continues.

## 7. Real-World Example

Stop searching for a user when found:

```java
public class BreakSearch {

    public static void main(String[] args) {
        int foundId = -1;

        for (int id = 1; id <= 10; id++) {
            System.out.println("Looking at user " + id);
            if (id == 4) {
                foundId = id;
                break;
            }
        }

        System.out.println("Found id: " + foundId);
    }
}
```

Skip out-of-stock item numbers:

```java
for (int item = 1; item <= 5; item++) {
    if (item == 2) {
        System.out.println("Item 2 skipped (out of stock)");
        continue;
    }
    System.out.println("Adding item " + item + " to cart");
}
```

## 8. SDET Example

Retry until pass, then `break`:

```java
public class BreakOnPass {

    public static void main(String[] args) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.println("Attempt " + attempt);
            boolean pageLoaded = attempt == 2;
            if (pageLoaded) {
                System.out.println("Page loaded. Stopping retries.");
                break;
            }
            System.out.println("Still loading...");
        }
    }
}
```

Expected output:

```text
Attempt 1
Still loading...
Attempt 2
Page loaded. Stopping retries.
```

Skip ignored tests:

```java
public class ContinueIgnoredTests {

    public static void main(String[] args) {
        for (int testNumber = 1; testNumber <= 5; testNumber++) {
            if (testNumber == 3) {
                System.out.println("Skipping test " + testNumber + " (ignored)");
                continue;
            }
            System.out.println("Running test " + testNumber);
        }
    }
}
```

Stop on first failure:

```java
boolean failed = false;
for (int i = 1; i <= 5; i++) {
    int actual = (i == 4) ? 500 : 200; // you may write a normal if instead
    if (actual != 200) {
        System.out.println("TEST FAILED on test " + i);
        System.out.println("Expected: 200");
        System.out.println("Actual: " + actual);
        failed = true;
        break;
    }
    System.out.println("Test " + i + " passed");
}
```

If the ternary `?:` is new, use `if (i == 4) { actual = 500; } else { actual = 200; }`.

## 9. Break the Code

This program was supposed to skip test 3 but still run 4 and 5. It used `break` by mistake.

```java
public class BrokenBreakInsteadOfContinue {

    public static void main(String[] args) {
        for (int testNumber = 1; testNumber <= 5; testNumber++) {
            if (testNumber == 3) {
                System.out.println("Skipping test 3");
                break;
            }
            System.out.println("Running test " + testNumber);
        }
    }
}
```

Output:

```text
Running test 1
Running test 2
Skipping test 3
```

Tests 4 and 5 never run. That is `break` leaving the loop.

## 10. Debug

Ask: do I want to leave the **whole loop**, or skip **this round**?

```text
Leave the loop     → break
Skip this round    → continue
```

If later numbers never appear, you probably used `break`. If one number is missing but later ones appear, you used `continue` (or an `if` that skipped printing).

Debugger: Step Over on `break` and watch execution jump to the first line after the loop. On `continue` in a `for`, watch `i` increase and the body start again.

Avoid this unreadable style:

```java
while (true) {
    if (done) {
        break;
    }
}
```

until you have a clear reason. A `while (!done)` is often cleaner.

## 11. Student Exercise

Write two loops from 1 to 6.

Loop A: `break` when the number is 4. Print the numbers that ran.

Loop B: `continue` when the number is 4. Print the numbers that ran.

Write a comment comparing the two outputs.

## 12. Challenge

Simulate 5 API calls. Status codes: 200, 200, 503, 200, 200.

- If status is 503, print `Retryable, skipping record` and `continue`.
- If status is 500, print TEST FAILED and `break`.
- Count how many calls "processed successfully" (200).

Print the success count after the loop. It should be 4, not 5.

Then change the third code to 500 and show that processing stops.

## 13. Knowledge Check

1. What does `break` do in a loop?
2. What does `continue` do in a loop?
3. After `break`, do later iterations run?
4. After `continue` in a `for` loop, does `i++` still happen?
5. Which one fits "stop retrying because we already passed"?
6. Which one fits "this test is ignored, run the next"?
7. True or false: `break` is a method of `String`.
8. Why can overusing `break` hurt readability?
9. In a `switch`, what does `break` prevent?
10. If you `break` on the first failure, what happens to remaining tests in that loop?

## 14. Interview Question

**Question:** What is the difference between `break` and `continue`?

A strong answer:

> break leaves the loop immediately, so no more iterations run. continue skips the rest of the current iteration and goes to the next one. In a retry loop I break when the page has loaded. When processing a list of tests I continue past names that are marked ignored. In a traditional switch, break also prevents fall-through between cases.

## 15. Homework

Write `HomeworkBreakContinue` with a `for` loop of attempts 1 to 5.

- On attempt 2, `continue` after printing `flaky skip`.
- On attempt 4, simulate PASS and `break`.
- Print a line after the loop: `Finished retry logic`.

Paste the output in a comment. You should not see attempt 5.

---

## Answer Key

1. Stops the loop immediately.
2. Skips the rest of this iteration and goes to the next.
3. No.
4. Yes.
5. `break`
6. `continue`
7. False. It is a keyword.
8. Because readers must hunt for hidden exits.
9. Fall-through into the next case.
10. They do not run.
