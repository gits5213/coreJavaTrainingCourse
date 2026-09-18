# Advanced Java Exam

**Level:** after Parts 17–26 (and comfort with collections)  
**Format:** IDE + tests  
**Timebox:** 30 minutes + tests (about 40 minutes total)

Suggested weight: include in the final battery as part of live/take-home skill, or as a standalone gate before API week. The [final exam guide](final-exam-guide.md) lists this as a required station alongside OOP.

Use:

```text
Generics
Streams
Records
Exception Handling
Collections
```

to process test results.

---

## Prompt

You are given test results from a CI run. Process them in memory (no Selenium).

### 1. Model

```java
public record TestResult(String name, String status, int durationMs) {
}
```

Add validation in a compact constructor:

- `name` not null/blank
- `status` not null/blank (store upper-case, or compare with `equalsIgnoreCase` later — pick one and stay consistent)
- `durationMs >= 0`

Throw `IllegalArgumentException` with a clear message.

### 2. API

Class `ResultStats`:

```java
public static List<String> failedNames(List<TestResult> results)
public static double averagePassDuration(List<TestResult> results)
public static Map<String, Long> countByStatus(List<TestResult> results)
```

Rules:

- `failedNames`: status `FAIL` (ignore case), names in encounter order, `List<String>` not raw `List`.
- `averagePassDuration`: average `durationMs` of `PASS`. If there are **no** passing tests, throw `IllegalArgumentException` (`orElseThrow` is the intended stream style). Do not return `0` silently.
- `countByStatus`: map status → count. Generics: `Map<String, Long>`.
- Null `results` list: throw `NullPointerException` or `IllegalArgumentException` (document it). Empty list: failed names empty; average throws; counts empty map.

Prefer streams when they stay readable. A defended loop is acceptable; a 40-line mutation with raw types is not.

### 3. Tests (required)

JUnit 5 (or TestNG) with arrange–act–assert:

- two PASS, one FAIL → failed names size 1, average is the mean of the two durations
- all FAIL → average throws
- invalid record construction throws
- `List<TestResult>` — compiler should prevent adding a `String`

### Reference solution (examiner)

```java
public record TestResult(String name, String status, int durationMs) {
    public TestResult {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("status required");
        }
        if (durationMs < 0) {
            throw new IllegalArgumentException("durationMs must be >= 0");
        }
        status = status.toUpperCase();
    }
}

public class ResultStats {

    public static List<String> failedNames(List<TestResult> results) {
        return results.stream()
                .filter(r -> "FAIL".equalsIgnoreCase(r.status()))
                .map(TestResult::name)
                .toList();
    }

    public static double averagePassDuration(List<TestResult> results) {
        return results.stream()
                .filter(r -> "PASS".equalsIgnoreCase(r.status()))
                .mapToInt(TestResult::durationMs)
                .average()
                .orElseThrow(() -> new IllegalArgumentException("no passing tests"));
    }

    public static Map<String, Long> countByStatus(List<TestResult> results) {
        return results.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        TestResult::status,
                        java.util.stream.Collectors.counting()));
    }
}
```

`Objects.requireNonNull(results)` at the start of each method is a plus.

---

## Rubric

| Look for | Points |
| --- | --- |
| Record + validation | 4 |
| Generics (`List<TestResult>`) | 3 |
| Streams (or clear equivalent) | 4 |
| Exception when no passes | 3 |
| Tests | 4 |
| **Total** | **18** (scale to 100 or to battery weight) |

Automatic deductions: empty `catch`; raw `List`; `==` for status strings; silent `0` average.

---

## Oral (2 minutes)

"Why a record instead of a mutable class?" and "Why not Optional as a field on `TestResult`?"
