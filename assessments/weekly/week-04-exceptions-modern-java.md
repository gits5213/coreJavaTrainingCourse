# Week 4 — Exceptions and Modern Java

**Coverage:** Parts 17–26 (exceptions, files, JSON, generics, lambdas, streams, Optional, date-time, regex, records)

**Suggested timebox:** 90 minutes

| Activity | Time |
| --- | --- |
| Quiz (closed book) | 15 minutes |
| Coding assignment | 40 minutes |
| Debugging problem | 15 minutes |
| Explanation exercise | 10 minutes |
| Buffer | 10 minutes |

Score: Theory 20%, Coding 35%, Problem Solving 20%, Debugging 15%, Explanation 10%. Pass bar: 70% weighted.

---

## Quiz (10 questions)

1. What is an exception? Why must a test **not** catch `Exception` with an empty body?
2. Checked vs unchecked: give one example of each. What does the compiler force you to do with a checked exception?
3. What is `finally` for? Name an SDET resource that must be cleaned up whether the test passed or failed.
4. How should you read a text file in modern Java? Why prefer `Path.of("tests", "data.txt")` over a hardcoded `"C:\\..."` string?
5. Java's standard library does not give you a JS-like JSON object. What do teams typically use, and why is `split` / `indexOf` a poor career strategy for JSON?
6. Why generics? What is wrong with a raw `List` for users?
7. What is a lambda, in one sentence? What kind of interface can a lambda implement?
8. Draw a stream pipeline (collection → stream → filter → map → collect). Give one SDET example.
9. When is `Optional<User>` a good return type, and when should you **not** use Optional?
10. What is a record? Why are records a good fit for test data such as `LoginData`?

---

## Coding assignment

**Title:** Process a list of test results

This is practice for the advanced Java exam.

### Requirements

1. Record:

   ```java
   public record TestResult(String name, String status, int durationMs) {}
   ```

   Compact constructor (or explicit checks): reject `null`/blank `name`, and reject negative `durationMs` with `IllegalArgumentException`.

2. Class `ResultStats` with generic-aware collections (`List<TestResult>`, not raw lists):

   - `List<String> failedNames(List<TestResult> results)` — stream, filter status `FAIL` (ignore case), map to name, collect to list.
   - `double averagePassDuration(List<TestResult> results)` — average `durationMs` of `PASS` results. If there are no passing tests, throw `IllegalArgumentException` with a clear message.
   - `List<TestResult> slowerThan(List<TestResult> results, int thresholdMs)` — filter durations strictly greater than the threshold.

3. `main` or JUnit tests that cover:
   - two PASS and one FAIL
   - failed names contains the failing test
   - average of the two passes is correct
   - empty pass list throws

4. Read a small results file **or** a hard-coded list. If you read a file, use `Files.readString(Path.of(...))` and handle `IOException` honestly (declare `throws` or catch and fail — no empty catch).

### Acceptance criteria

- [ ] `TestResult` is a record (immutable data carrier).
- [ ] Invalid name or negative duration is rejected.
- [ ] Failed names use streams, not a 40-line mutation, unless you can defend a loop as clearer.
- [ ] No-pass average throws; it does not return `0` silently.
- [ ] Types are `List<TestResult>` / `List<String>`, not raw `List`.
- [ ] No swallowed exceptions.

### Problem-solving stretch

- Use `Optional` for `findByName(List<TestResult> results, String name)` as a return type.
- Use a regex to validate that a test name looks like `test[A-Z]\\w*` (or a simpler `\\w+`). Do not claim your regex validates every possible name forever.

---

## Debugging problem

```java
import java.util.List;

public class ResultStats {
    public static List<String> failedNames(List results) {
        try {
            List<String> names = new java.util.ArrayList<String>();
            for (Object o : results) {
                TestResult r = (TestResult) o;
                if (r.status() == "FAIL") {
                    names.add(r.name());
                }
            }
            return names;
        } catch (Exception e) {
        }
        return List.of();
    }
}

record TestResult(String name, String status, int durationMs) {}
```

A test with status `"FAIL"` sometimes does not appear in `failedNames`. Sometimes the method returns an empty list even when the run clearly failed.

### Your job

1. List every defect (there are several).
2. Circle the one that **hides failures**.
3. Write a corrected `failedNames` using `List<TestResult>` and streams (or a clear loop) and `equals` for status.

### Expected diagnosis

| Defect | Why it hurts |
| --- | --- |
| Raw `List` + cast | `ClassCastException` at runtime instead of a compile error |
| `==` on `String` status | Identity, not content. Interning may make it "work" in demos and fail with strings from a file |
| Empty `catch (Exception e)` | Swallows the cast failure or NPE; method returns empty list; the test **lies** |
| Returning `List.of()` after swallow | Failed tests disappear from the report |

**Fix:** `List<TestResult>`, `"FAIL".equalsIgnoreCase(r.status())`, no empty catch, let failures surface.

---

## Explanation exercise

**Prompt (90 seconds):**

> A teammate wrote `catch (Exception e) {}` around an API call "so the suite stays green." Explain why that is a problem, then say how streams plus a `TestResult` record would make a failure report honest.

**Strong answer hits:**

- An exception means this path cannot continue honestly. Swallowing it hides product bugs.
- Catch only what you can handle; still fail the test.
- A record is an immutable result row. A stream can filter `FAIL` and list names. Empty catch would erase that evidence.

---

## Answer key

1. A thrown object that says this path failed. Empty catch makes a broken product look passing. Tests must not lie.
2. Checked: `IOException` — handle or declare `throws`. Unchecked: `NullPointerException`, `NumberFormatException`, most Selenium exceptions. `Error` is for JVM trouble; almost never catch it.
3. `finally` (or try-with-resources) runs cleanup either way. Browsers (`quit`), streams, DB connections.
4. `Files.readString(Path.of("data.txt"))`. `Path.of` is portable; drive-letter strings are not.
5. Jackson `ObjectMapper` (typical). Ad-hoc `split` breaks when whitespace or fields change.
6. Generics push type mistakes to compile time. Raw `List` needs casts and fails at runtime.
7. A compact implementation of a functional interface (one abstract method). Example: `r -> r.failed()`.
8. `results.stream().filter(...).map(...).toList()`. Example: names of failed tests.
9. Good as a **return** when missing is normal (`findUser`). Not a field on every class; not a replacement for every `null`.
10. Concise immutable data carrier; constructor, accessors, `equals`/`hashCode`/`toString` generated. Test data should not mutate mid-run. Mask secrets in `toString` if needed.

**Debugging key:** empty catch + `==` on String + raw List.
