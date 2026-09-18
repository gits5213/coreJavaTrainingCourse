# Chapter 30 — `for` Loop

## 1. Today's Goal

By the end of this lesson, you will write a `for` loop that repeats a block a chosen number of times.

You will print:

```text
Test 1
Test 2
Test 3
Test 4
Test 5
```

using one `println` inside a loop, not five copied lines.

## 2. Why It Matters

Repeating copy-paste is how bugs multiply. You change one line and forget the others.

A `for` loop is the standard Java way to say:

> Start at this number, keep going while this is true, and after each round update the number.

SDET work uses this for:

- generating numbered test names
- running a setup step N times
- walking through indexes (arrays come in Part 11)

## 3. Real-Life Analogy

A gym teacher says:

```text
Start at 1
While you have not passed 5
    Do a jumping jack
    Add 1 to the count
```

That is a `for` loop in English.

A stamp on five envelopes is the same idea. You do not hire five people to stamp once. You loop.

## 4. Illustrated Explanation

```java
for (int i = 1; i <= 5; i++) {
    System.out.println("Test " + i);
}
```

The header has three parts:

```text
for ( start ;  continue while true ;  update )
         │              │                │
     int i = 1      i <= 5              i++
```

```text
Round 1: i is 1  → print Test 1  → i becomes 2
Round 2: i is 2  → print Test 2  → i becomes 3
Round 3: i is 3  → print Test 3  → i becomes 4
Round 4: i is 4  → print Test 4  → i becomes 5
Round 5: i is 5  → print Test 5  → i becomes 6
Then i <= 5 is false → stop
```

Picture:

```text
i:  1 → 2 → 3 → 4 → 5 → 6
    |    |    |    |    |
    print print print print print
                            stop
```

`i++` means `i = i + 1`.

`i` is a common short name for a loop counter. For a tiny counter, `i` is acceptable. If the number has business meaning, a fuller name such as `testNumber` is even clearer.

## 5. Syntax / Concept

```java
for (int i = 1; i <= 5; i++) {
    System.out.println("Test " + i);
}
```

Read it as:

1. Create `i` starting at 1.
2. If `i <= 5` is false, skip the loop.
3. Otherwise run the block.
4. Then do `i++`.
5. Go back to step 2.

Counting from 0 is also common, especially with arrays later:

```java
for (int i = 0; i < 5; i++) {
    System.out.println(i);
}
```

That prints `0` through `4`. Five times still, but the numbers start at 0.

Counting downward:

```java
for (int i = 3; i >= 1; i--) {
    System.out.println("T-minus " + i);
}
```

`i--` means subtract 1.

The loop variable's scope is the loop. You usually cannot use `i` after the loop unless you declared it outside.

## 6. Simple Example

```java
public class ForDemo {

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            System.out.println("Test " + i);
        }
    }
}
```

Expected output:

```text
Test 1
Test 2
Test 3
Test 4
Test 5
```

## 7. Real-World Example

Print order line numbers:

```java
public class OrderLines {

    public static void main(String[] args) {
        int itemCount = 4;

        for (int line = 1; line <= itemCount; line++) {
            System.out.println("Item line " + line);
        }
    }
}
```

Bank PIN attempts remaining, counting down:

```java
public class PinCountdown {

    public static void main(String[] args) {
        for (int remaining = 3; remaining >= 1; remaining--) {
            System.out.println("Attempts remaining: " + remaining);
        }
        System.out.println("No attempts left");
    }
}
```

## 8. SDET Example

Numbered test execution labels:

```java
public class ForTestNames {

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            System.out.println("Test " + i);
        }
    }
}
```

Simulate checking five users:

```java
public class ForUserChecks {

    public static void main(String[] args) {
        int userCount = 5;

        for (int i = 1; i <= userCount; i++) {
            System.out.println("Checking user " + i);
            System.out.println("Expected status: 200");
        }
    }
}
```

A performance warmup:

```java
for (int i = 1; i <= 3; i++) {
    System.out.println("Warmup request " + i);
}
System.out.println("Real measurement starts now");
```

## 9. Break the Code

This loop never stops in a useful way, or it never starts.

```java
public class BrokenFor {

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i--) {
            System.out.println("Test " + i);
        }
    }
}
```

`i` starts at 1 and goes down: 1, 0, -1, -2... `i <= 5` stays true. This is an infinite loop.

Another bug:

```java
for (int i = 1; i >= 5; i++) {
    System.out.println("Test " + i);
}
```

`1 >= 5` is false immediately, so the loop body never runs.

## 10. Debug

Ask three questions about every `for` header:

```text
Where do I start?
When do I stop?
How do I move toward the stop?
```

In the infinite example, the update `i--` moves away from stopping at 5. Use `i++` when counting up.

If the program hangs, click the red stop square in IntelliJ. Then fix the header.

Debugger:

1. Breakpoint on the `println`.
2. Watch `i` change.
3. If `i` goes the wrong direction, inspect the update clause.

Off-by-one bugs are common:

- `i <= 5` includes 5
- `i < 5` stops before 5 if you started at 1, giving 1..4

Print `i` until the count matches what you intended.

## 11. Student Exercise

Write a `for` loop that prints `Attempt 1` through `Attempt 3`.

Then write a second loop that prints even numbers 2, 4, 6, 8, 10. Hint: `i = i + 2`.

## 12. Challenge

Print a mini report:

```text
Running test 1 of 4
Running test 2 of 4
Running test 3 of 4
Running test 4 of 4
All tests launched
```

Use one loop and a `total` variable. Do not hard-code 4 inside the print except through the variable.

## 13. Knowledge Check

1. What are the three parts of a `for` header?
2. What does `i++` mean?
3. How many times does `for (int i = 1; i <= 5; i++)` run?
4. What numbers print for `for (int i = 0; i < 3; i++)`?
5. What is an infinite loop?
6. Why might `i >= 5` fail to run if `i` starts at 1?
7. Is `i` a good name for a tiny counter?
8. True or false: you must copy `println` five times to print Test 1..5.
9. What does `i--` mean?
10. When should you use `<=` versus `<`?

## 14. Interview Question

**Question:** How does a Java `for` loop work?

A strong answer:

> A for loop has initialization, a condition, and an update. For example, int i = 1; i <= 5; i++ starts at 1, continues while i is at most 5, and adds 1 after each round. Testers use it to repeat numbered steps, such as printing Test 1 through Test 5 or sending a warmup request several times. If the update never moves toward the end condition, the loop can run forever.

## 15. Homework

Write `HomeworkFor`:

- Loop from 1 to 10 and print whether each number is a test slot.
- Nested idea is optional: if you feel ready, a loop of 2 browsers each running 3 tests, printing `Browser b, Test t`. If nested loops feel too soon, skip nesting and only do 1 to 10.

Stop any accidental infinite loop with IntelliJ's stop button and fix it.

---

## Answer Key

1. Start, condition, update.
2. Add 1 to `i`.
3. Five times.
4. `0`, `1`, `2`
5. A loop whose condition never becomes false.
6. Because the condition is already false.
7. Yes for a short-lived counter.
8. False.
9. Subtract 1 from `i`.
10. `<=` includes the end number. `<` stops before it. Choose based on whether you start at 0 or 1 and how many rounds you want.
