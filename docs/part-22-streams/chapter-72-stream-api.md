# Chapter 72 — The Stream API

## 1. Today's Goal

By the end of this lesson, you will build a stream pipeline:

```text
Collection  →  Stream  →  Filter  →  Collect
```

You will take a list of HTTP status codes and collect those that are **not** 200:

```java
List<Integer> failures = statusCodes.stream()
        .filter(code -> code != 200)
        .toList();
```

You will know a stream does not store the data the way a `List` does. It processes it.

## 2. Why It Matters

Loops work. You already wrote them. Streams shine when the story is:

> Keep some items. Maybe change them (next chapter). Put them in a new list.

SDET reports: "which calls failed?" is a filter. Writing four nested loops hides that sentence. A pipeline **is** the sentence.

Streams also make later parallel processing possible. You do not need parallel today. You need `filter` and `toList`.

## 3. Real-Life Analogy

A factory conveyor.

```text
Bin of packages (List)
    →  put on conveyor (stream())
    →  workers toss aside 200-OK stickers (filter != 200)
    →  remaining packages into a new bin (toList)
```

The conveyor is not a warehouse. If you never put a bin at the end, you might have defined work that never runs (lazy pipelines). `toList()` is a **terminal** operation: it starts the real work.

A kitchen colander: stream of pasta water, filter keeps pasta, collect into a bowl.

## 4. Illustrated Explanation

```text
statusCodes List
[ 200, 201, 404, 200, 500 ]
        │
        │  .stream()
        ▼
   Stream pipeline (not executed yet)
        │
        │  .filter(code -> code != 200)
        ▼
   keep 201, 404, 500
        │
        │  .toList()
        ▼
   new List [ 201, 404, 500 ]
```

```text
Collection → Stream → Filter → Transform → Collect
     ^           ^        ^         ^          ^
    List      stream()  filter    map      toList
              (ch 72)   (ch 72)  (ch 73)   (ch 72)
```

Lazy:

```text
stream().filter(...)     // recipe
.toList()                // cook the recipe
```

If you forget a terminal operation, nothing useful happens. IntelliJ often warns.

The original list is **not** modified by this pipeline. You get a new list. (That is the usual beginner style. There are mutating collectors; skip them today.)

## 5. Syntax / Concept

```java
List<Integer> statusCodes = List.of(200, 201, 404, 200, 500);

List<Integer> failures = statusCodes.stream()
        .filter(code -> code != 200)
        .toList();
```

- `stream()` — view the collection as a pipeline
- `filter(predicate)` — keep items where the lambda returns true
- `toList()` — terminal; unmodifiable list in modern Java

Older style: `.collect(Collectors.toList())` — still seen everywhere. `toList()` is shorter on JDK 16+.

Count without collecting:

```java
long failed = statusCodes.stream()
        .filter(code -> code != 200)
        .count();
```

`count()` is also terminal.

`filter(code -> code != 200)` keeps failures **if** 200 is the only success. Real HTTP has 201, 204. For a "not 200" teaching example we keep the user's exact rule. In production you might `filter(code -> code >= 400)`.

Do not call `stream()` on null. NPE. Guard the list.

## 6. Simple Example

```java
import java.util.List;

public class StreamFilterDemo {

    public static void main(String[] args) {
        List<Integer> statusCodes = List.of(200, 201, 404, 200, 500);

        List<Integer> not200 = statusCodes.stream()
                .filter(code -> code != 200)
                .toList();

        System.out.println(not200);
    }
}
```

Expected:

```text
[201, 404, 500]
```

## 7. Real-World Example

Shop order statuses: filter not `PAID`.

```java
List<String> statuses = List.of("PAID", "PENDING", "PAID", "CANCELLED");
List<String> needsWork = statuses.stream()
        .filter(s -> !s.equals("PAID"))
        .toList();
```

Bank transactions: keep amounts below 0 (debits).

The conveyor idea is the same: source, filter, collect.

## 8. SDET Example

```java
import java.util.List;

public class FailedStatusStream {

    public static void main(String[] args) {
        List<Integer> statusCodes = List.of(200, 200, 404, 500, 200);

        List<Integer> failures = statusCodes.stream()
                .filter(code -> code != 200)
                .toList();

        if (failures.isEmpty()) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
            System.out.println("Non-200 codes: " + failures);
        }
    }
}
```

This is better than a boolean that loses which codes failed.

Empty catch around the pipeline: still forbidden. Streams rarely throw unless your lambda throws. If you parse inside a filter, be careful:

```java
.filter(text -> {
    try {
        return Integer.parseInt(text) != 200;
    } catch (NumberFormatException e) {
        return true; // treat garbage as failure-to-keep, or fail the test instead
    }
})
```

Often **fail the test** if text is not a number, rather than quietly filtering.

## 9. Break the Code

```java
statusCodes.stream().filter(code -> code != 200);
```

No terminal operation. The filter does not produce a list you can print. (Might be optimized away; beginners think "it didn't work.")

```java
filter(code -> code = 200)  // assignment, not comparison — may not compile for Integer
```

```java
List<Integer> failures = statusCodes
        .filter(code -> code != 200) // List has no filter; need .stream()
        .toList();
```

Using `filter` to **change** values: wrong tool. That is `map` (next chapter). Filter only keeps or drops.

## 10. Debug

Empty result when you expected values:

1. Print the source list.
2. Print the predicate by testing one value in Evaluate Expression: `code != 200` for `code=201`.
3. Did you invert the condition (`==` instead of `!=`)?
4. Did you forget `stream()`?

Debugger: IntelliJ can trace stream operations in recent versions. You can also temporarily `peek`:

```java
.filter(code -> code != 200)
.peek(code -> System.out.println("kept " + code))
.toList();
```

`peek` is for debugging, not for production business logic.

```text
Source empty?  Predicate inverted?  Missing toList?
```

## 11. Student Exercise

`List<Integer> statusCodes = List.of(200, 201, 204, 400, 404, 500);`

- collect `!= 200`
- print the list
- print `count()` of `!= 200`

## 12. Challenge

Given `List<String> raw = List.of("200", "404", "hello", "500", "201");`

Parse to integers where possible. Invalid strings should make the **whole check** TEST FAILED (do not hide with empty catch). If all parse, filter `!= 200` and print remaining. If none remaining, TEST PASSED for "all 200" — but `201` is not 200, so this data should fail the "all 200" idea. Print the non-200 ints.

## 13. Knowledge Check

1. What is a stream in one sentence?
2. Recite the pipeline: Collection → ? → Filter → Transform → Collect.
3. What does `filter(code -> code != 200)` keep?
4. Why do you need `toList()` (or another terminal op)?
5. Does this pipeline change the original list?
6. What is the difference between a `List` and a `Stream`?
7. How do you start a stream from a list?
8. True or false: `filter` is for changing `"john"` into `"JOHN"`.
9. Name one SDET use of filter.
10. Should you empty-catch inside a stream lambda to hide parse errors?

## 14. Interview Question

**Question:** What is the Java Stream API, and how would you get non-200 status codes from a list?

A strong answer:

> A stream is a pipeline over a collection: Collection to Stream to Filter to Transform to Collect. It does not replace the list; it processes it. I would write statusCodes.stream().filter(code -> code != 200).toList(). filter keeps items where the lambda is true. toList is a terminal operation that runs the pipeline. The original list stays the same. Testers use this to report which calls failed. I keep pipelines short enough to debug.

## 15. Homework

Take any array of numbers from earlier homework, put them in a `List`, filter a condition you care about (even vs odd, or > 0), collect, print.

Draw the conveyor diagram with your actual numbers.

Read Chapter 73 next for `map`. Skim Chapter 74 so `reduce` is not a stranger, then study 74 fully after 73.

---

## Answer Key

1. A pipeline for processing elements from a source like a list.
2. Stream (then Filter, Transform, Collect).
3. Codes that are not 200, including 201, 404, 500 in the example.
4. It is terminal; it actually runs the pipeline and produces a list.
5. No (this style).
6. List stores; stream processes (and is typically consumed once).
7. `.stream()`
8. False. That is `map`.
9. Collecting failed HTTP codes, failed tests, non-PAID orders.
10. No.
