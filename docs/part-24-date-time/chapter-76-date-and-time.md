# Chapter 76 — Date and Time

## 1. Today's Goal

By the end of this lesson, you will use `java.time` types:

```java
LocalDate today = LocalDate.now();
LocalDate birthday = LocalDate.of(1990, 5, 10);
LocalTime noon = LocalTime.of(12, 0);
LocalDateTime meeting = LocalDateTime.of(today, noon);
Instant start = Instant.now();
Duration elapsed = Duration.between(start, Instant.now());
```

You will know `LocalDate` is a date without a clock time, and `Instant` is a moment on the timeline.

## 2. Why It Matters

"Date" in English mixes several ideas. Java splits them so you do not store a birthday as a millisecond-since-1970 and then get it wrong in another time zone.

SDET work:

- assert `expiry` is after `now`
- stamp a report file name with a date
- measure how long a call took (`Duration`)
- compare `LocalDate` of an order to today

Old `Date.toString()` changes with locale and confuses CI logs. Prefer `toString()` on `LocalDate` (`1990-05-10`) which is ISO and sortable.

## 3. Real-Life Analogy

```text
LocalDate      →  a mark on a wall calendar (May 10, 1990)
LocalTime      →  a kitchen clock (12:00) with no idea what day it is
LocalDateTime  →  "May 10, 1990 at 12:00" on a sticky note, no city
Instant        →  the same cosmic moment everywhere (a stopwatch beep in UTC)
Duration       →  how long the exam took (2 hours), not a clock time
```

A birthday is `LocalDate`. A flight landing in a city needs a time zone (`ZonedDateTime` — mention only: later). A performance measurement is `Duration` between two `Instant`s.

Do not use a calendar date to measure elapsed milliseconds. Do not use `Duration` as someone's birthday.

## 4. Illustrated Explanation

```text
LocalDate.of(1990, 5, 10)

    1990-05-10
    │    │  └── day
    │    └───── month (MAY is 5, not 0. Unlike old Calendar!)
    └────────── year
```

Old `Calendar` months were 0-based. `LocalDate` months are **1–12**. May is 5.

```text
Instant.now()     ────────────────►  Instant.now()
         │                                │
         └──── Duration.between ──────────┘
                    elapsed
```

```text
Machine default zone
        │
        ▼
LocalDate.now()   "today" where the JVM thinks you are
Instant.now()     same instant worldwide, different local clocks
```

```text
LocalDateTime  has no zone
    2026-09-18T09:00  in New York vs London is a different Instant
```

For tests of "the API returned this timestamp," prefer Instant or an explicit zone. For "user birthday," LocalDate.

## 5. Syntax / Concept

```java
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
```

```java
LocalDate today = LocalDate.now();
LocalDate birthday = LocalDate.of(1990, 5, 10);
LocalTime nowTime = LocalTime.now();
LocalTime noon = LocalTime.of(12, 0, 0);
LocalDateTime dt = LocalDateTime.of(1990, 5, 10, 12, 0);
Instant t0 = Instant.now();
Duration d = Duration.between(t0, Instant.now());
long ms = d.toMillis();
```

Compare dates:

```java
boolean past = birthday.isBefore(today);
boolean adultish = birthday.plusYears(18).isBefore(today) || birthday.plusYears(18).isEqual(today);
```

Parse ISO:

```java
LocalDate parsed = LocalDate.parse("1990-05-10");
```

Wrong format throws `DateTimeParseException` (unchecked). Catch specifically or fail the test. Not empty `Exception`.

`LocalDate.now()` in a test is **non-deterministic**. If you assert `today.toString().equals("2026-09-18")`, the test dies tomorrow. Pass a `LocalDate` into the method, or assert format/pattern, not a fixed day, unless you inject a clock (advanced).

## 6. Simple Example

```java
import java.time.LocalDate;

public class LocalDateDemo {

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(1990, 5, 10);
        System.out.println("Today: " + today);
        System.out.println("Birthday: " + birthday);
        System.out.println("Birthday before today? " + birthday.isBefore(today));
    }
}
```

Today's printed date depends on when you run it. Birthday always `1990-05-10`.

## 7. Real-World Example

Shop sale window: `LocalDate` start and end. Inclusive dates are a classic off-by-one. Write tests.

Bank interest day count: dates, not instants.

Order placed: `Instant` of the click, stored in UTC, displayed as local time in the UI (UI layer). Tests of the API often assert the ISO timestamp string.

## 8. SDET Example

```java
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

public class DateTimeSdetDemo {

    public static void main(String[] args) {
        LocalDate expiry = LocalDate.of(1990, 5, 10);
        LocalDate today = LocalDate.of(2026, 9, 18); // injected, not now()
        if (!expiry.isBefore(today)) {
            throw new AssertionError("TEST FAILED — token should be expired");
        }
        System.out.println("TEST PASSED — expiry is in the past");

        Instant start = Instant.now();
        // simulate work
        Instant end = start.plusMillis(25);
        Duration took = Duration.between(start, end);
        System.out.println("Call took " + took.toMillis() + " ms");
        if (took.toMillis() > 5000) {
            throw new AssertionError("TEST FAILED — too slow: " + took);
        }
    }
}
```

File name:

```java
Path.of("target", "reports", "run-" + LocalDate.now() + ".txt");
```

Be aware `now()` in the name is environment clock.

## 9. Break the Code

```java
LocalDate.of(1990, 0, 10); // month 0 invalid — DateTimeException
```

```java
LocalDate.of(1990, 5, 32); // day invalid
```

```java
LocalDate.parse("05/10/1990"); // not ISO; throws DateTimeParseException
```

```java
if (LocalDate.now().equals(LocalDate.of(2026, 9, 18))) {
    System.out.println("TEST PASSED");
}
```

Works one day. Flaky forever after.

Empty catch of `DateTimeParseException` then using `LocalDate.now()` as expiry: you just made tokens never expire in the test. Dangerous.

## 10. Debug

`DateTimeParseException`: print the raw string. ISO is `yyyy-MM-dd`.

`DateTimeException` on `of`: illegal month/day. Month is 1–12.

Wrong "today": JVM time zone. Print `ZoneId.systemDefault()`. CI might be UTC, your laptop not.

Debugger: inspect `LocalDate` fields year/month/day. Inspect `Duration` seconds/nanos.

```text
Is this a calendar date, a clock time, a moment, or a length?
If you mix them, you will fight the compiler — that is help.
```

## 11. Student Exercise

Print `LocalDate.now()`, `LocalDate.of(1990, 5, 10)`, `LocalTime.of(9, 30)`, a `LocalDateTime` of that birthday at 09:30.

Print `Duration.between` two instants 100ms apart (`start.plusMillis(100)`).

## 12. Challenge

Write `assertExpired(LocalDate expiry, LocalDate today)` — TEST FAILED if expiry is after or equal today (or define inclusive clearly in a comment).

Write `assertFasterThan(Duration actual, Duration max)`.

Use `LocalDate.of` for deterministic dates in `main`. Do not use `now()` inside the assertion methods; pass dates in.

## 13. Knowledge Check

1. What does `LocalDate` store?
2. How do you write May 10, 1990?
3. How do you get today's date?
4. What is `Instant`?
5. What is `Duration`?
6. Are `LocalDate` months 0-based or 1-based?
7. Why can `LocalDate.now()` make tests flaky?
8. What is `LocalDateTime` missing?
9. True or false: new code should prefer `java.time` over `java.util.Date`.
10. `DateTimeParseException` empty catch: allowed?

## 14. Interview Question

**Question:** How do you work with dates and times in modern Java?

A strong answer:

> I use java.time. LocalDate is a date, LocalTime is a time of day, LocalDateTime is both without a zone, Instant is a moment on the timeline, Duration is elapsed time. LocalDate.now() is today in the JVM's default zone; LocalDate.of(1990, 5, 10) is a fixed date. Months are 1 through 12. In tests I inject dates instead of calling now() when the day matters. I measure waits and call times with Instant and Duration. I do not start new code with java.util.Date.

## 15. Homework

Write a tiny "token" with `LocalDate expiry = LocalDate.of(1990, 5, 10)` and a `today` you pass in. Print EXPIRED or VALID.

Stamp `target/reports/time-homework.txt` with `Instant.now()` as a string plus a `Duration` of 15 minutes (`Duration.ofMinutes(15)`).

Notes: draw the five types as five labeled boxes.

---

## Answer Key

1. A date (year-month-day), no time of day.
2. `LocalDate.of(1990, 5, 10)`
3. `LocalDate.now()`
4. A moment on the timeline (UTC-based).
5. An amount of time between two points.
6. 1-based (May is 5).
7. The value changes with the clock and zone; assertions on a fixed day break.
8. A time zone.
9. True.
10. No.
