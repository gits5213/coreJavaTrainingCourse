# Part 24 — Date and Time

Java's modern date-time API lives in `java.time`. You will use:

| Type | Meaning |
| --- | --- |
| `LocalDate` | A calendar date, no time of day |
| `LocalTime` | A time of day, no date |
| `LocalDateTime` | Date and time, **no time zone** |
| `Instant` | A moment on the timeline (UTC-based) |
| `Duration` | An amount of time between instants (or times) |

```java
LocalDate today = LocalDate.now();
LocalDate birthday = LocalDate.of(1990, 5, 10);
```

Do not start new code with `java.util.Date` and `Calendar`. You will still *read* them in old APIs. This part teaches the modern types.

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 76](chapter-76-date-and-time.md) | `LocalDate`, `LocalTime`, `LocalDateTime`, `Instant`, `Duration` |

## SDET Connection

Tokens expire. Reports need timestamps. "Wait 2 seconds" is a `Duration`. Flaky tests often misuse sleeps; later you will wait for conditions. Still, you must know what a duration is.

`LocalDate.now()` depends on the machine's clock and default zone. Tests that freeze "today" should inject a date, not call `now()` if the assertion is calendar-sensitive.
