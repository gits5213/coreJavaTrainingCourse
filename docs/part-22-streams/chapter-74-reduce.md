# Chapter 74 — reduce

## 1. Today's Goal

By the end of this lesson, you will explain **reduce** as: take many values, combine them into **one**.

You will sum test **execution durations**:

```java
double total = durations.stream()
        .reduce(0.0, (a, b) -> a + b);
```

This chapter is **conceptually later** than `filter` and `map`. Teams live on filter/map/collect for months. Interviews still ask reduce. We study it fully now so the word is not a fog. If it feels heavy, type the sum example, finish homework, and come back after you have used streams at work for a week.

## 2. Why It Matters

Filter answers "which?" Map answers "what shape?" Reduce answers **"what single result?"**

- sum of durations
- max response time
- concatenate log lines (usually `String.join` is clearer)
- product of numbers (rare)

You *can* sum with a `for` loop. Reduce is the pipeline version of that loop. Performance reports for SDET: total time of a suite, or total of failed tests' durations.

## 3. Real-Life Analogy

Folding a stack of receipts into one total.

```text
$1.50
$2.00
$0.50
   fold + fold +  →  $4.00
```

A snowball: start with 0, roll each duration into the ball.

A team standup: many status updates reduce into one "we are red" or "we are green" (that one is often `anyMatch` / `allMatch`, cousins of reduce).

## 4. Illustrated Explanation

```text
durations:  [1.2, 0.4, 3.0]

reduce(0.0, (a, b) -> a + b)

start identity = 0.0
  0.0 + 1.2 = 1.2
  1.2 + 0.4 = 1.6
  1.6 + 3.0 = 4.6

one number: 4.6
```

```text
Collection → Stream → Filter → Transform → Collect
                                      reduce is another
                                      kind of finish line
                                      (terminal, one value)
```

```text
filter  many → many (or fewer)
map     many → many (same count)
reduce  many → one
toList  many → many (as a List)
```

Identity (the starting value) must be **safe**: `0` for sum, `1` for product. Wrong identity (`0` for product) corrupts the answer.

## 5. Syntax / Concept

```java
List<Double> durations = List.of(1.2, 0.4, 3.0);

double total = durations.stream()
        .reduce(0.0, (a, b) -> a + b);
```

Without identity, `reduce((a, b) -> a + b)` returns `Optional<Double>` because an empty stream has no sum. That is why we teach Optional soon. Prefer identity `0.0` for sums.

`mapToDouble` plus `sum()` is often clearer for numbers:

```java
double total = durations.stream()
        .mapToDouble(d -> d)
        .sum();
```

Learn reduce anyway: it is the general idea behind sum, max, and custom combining.

Max duration:

```java
double max = durations.stream()
        .reduce(Double.NEGATIVE_INFINITY, (a, b) -> a > b ? a : b);
```

Or `.max(Double::compare)` which returns `Optional`. Clearer. Reduce is the ancestor thought.

Empty list + identity 0.0 → total 0.0. That might be OK or a missed "no tests ran." SDET: if the list is empty, consider failing or warning.

## 6. Simple Example

```java
import java.util.List;

public class ReduceSumDemo {

    public static void main(String[] args) {
        List<Double> durations = List.of(1.2, 0.4, 3.0);
        double total = durations.stream()
                .reduce(0.0, (a, b) -> a + b);
        System.out.println("Total seconds: " + total);
    }
}
```

Expected:

```text
Total seconds: 4.6
```

(Floating point might print `4.6` or a close binary representation. For money later, do not use `double`. Durations in tests are usually OK as `double`.)

## 7. Real-World Example

Shop: sum line-item prices (still careful with money types in production).

Bank: sum a list of transaction amounts.

Warehouse: reduce box weights into total truck load.

The fold is the same: many measurements, one total.

## 8. SDET Example

```java
import java.util.List;

class TestRun {
    String name;
    double seconds;
    boolean passed;

    TestRun(String name, double seconds, boolean passed) {
        this.name = name;
        this.seconds = seconds;
        this.passed = passed;
    }
}

public class ReduceDurations {

    public static void main(String[] args) {
        List<TestRun> runs = List.of(
                new TestRun("login", 1.2, true),
                new TestRun("checkout", 3.5, false),
                new TestRun("search", 0.8, true)
        );

        double total = runs.stream()
                .map(run -> run.seconds)
                .reduce(0.0, (a, b) -> a + b);

        double failedTotal = runs.stream()
                .filter(run -> !run.passed)
                .map(run -> run.seconds)
                .reduce(0.0, (a, b) -> a + b);

        System.out.println("All tests seconds: " + total);
        System.out.println("Failed tests seconds: " + failedTotal);

        if (total <= 0.0) {
            throw new AssertionError("TEST FAILED — no durations recorded");
        }
        System.out.println("SUITE TIME RECORDED");
    }
}
```

Pipeline: Collection → Stream → Filter → Transform (`map`) → reduce (collect-into-one).

## 9. Break the Code

```java
.reduce(1.0, (a, b) -> a + b);  // identity 1, sum is 1 too high
```

```java
.reduce(0, (a, b) -> a + b); // types: Integer identity vs Double stream — may not compile
```

Empty catch around reduce: if `map` throws NPE, you hide a null duration. Forbidden.

```java
durations.stream().reduce((a, b) -> a + b);
```

Forgot to use the `Optional`. Empty list → empty Optional → `get()` without check → exception. Next part teaches Optional.

Using reduce to build a List with `add` inside: that is the wrong tool and can be buggy in parallel streams. Use `toList()`.

## 10. Debug

Sum looks wrong:

1. Print the list.
2. Check identity (0 for sum).
3. Did you filter too much first?
4. Floating point: `0.1 + 0.2` is a famous almost-0.3. For homework, simple decimals are OK.

Debugger: you cannot easily step "inside" reduce the same way as a for-loop at first. Convert to a loop temporarily if stuck:

```java
double total = 0;
for (double d : durations) {
    total = total + d;
}
```

If that matches, your data is the story. If reduce disagrees, identity or types are the story.

```text
Many values → one value. What is the combining rule? What is the start?
```

## 11. Student Exercise

`List<Double> durations = List.of(0.5, 1.0, 1.5);`

Sum with `reduce(0.0, (a, b) -> a + b)`.

Also compute sum with a `for` loop. Print both. They should match.

## 12. Challenge

From a list of `TestRun`, compute:

- total duration
- count of failures (you may use `filter` + `count` — that is not reduce, and that is OK)
- max duration using `reduce` **or** `.max` — if you use `.max`, comment that it returns Optional and use `orElse(-1)`

Print a one-page suite summary. If any duration is negative, throw `IllegalArgumentException` before reducing (validate with a loop or `anyMatch`).

## 13. Knowledge Check

1. What does reduce do in one sentence?
2. Why is this chapter "later conceptually" than filter/map?
3. What identity do you use to sum numbers?
4. Write reduce that sums `durations`.
5. How does reduce differ from `toList()`?
6. Why can reduce without identity return `Optional`?
7. True or false: `sum()` on a double stream is often clearer than reduce for totals.
8. Name an SDET use of reduce.
9. What goes wrong if product uses identity 0?
10. Empty catch if a duration is null inside map: allowed?

## 14. Interview Question

**Question:** Explain `reduce` on a stream.

A strong answer:

> reduce combines many elements into one using a function. To sum execution durations I use durations.stream().reduce(0.0, (a, b) -> a + b). The 0.0 is the identity, the start value. filter and map still return streams of many items; reduce is a terminal many-to-one. For numbers I might prefer mapToDouble and sum, but reduce is the general idea. Empty streams without an identity need Optional. Testers sum times for suite reports. I would not use reduce to rebuild lists; that is toList.

## 15. Homework

Time three imaginary tests (hard-coded doubles). Sum with reduce. Write the total to `target/reports/duration.txt` using Part 18 skills.

On paper: fold `1.2, 0.4, 3.0` from 0 by hand. Do not skip the arithmetic.

You may now use streams in later homework. Prefer clear filter/map. Use reduce when you truly need one combined value.

---

## Answer Key

1. Combines many values into one using a rule.
2. Daily work is filter/map/collect; reduce is a more general fold; still interview-relevant.
3. `0` or `0.0` for a sum.
4. `.reduce(0.0, (a, b) -> a + b)`
5. `toList` collects many items; reduce produces one combined value.
6. There is no element to start from on an empty stream.
7. True.
8. Summing test durations, totaling retries, combining counts.
9. Everything becomes 0.
10. No.
