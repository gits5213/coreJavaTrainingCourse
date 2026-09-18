# Live Coding Exam

**Level:** beginner bar that every later station still assumes  
**Format:** shared screen or projector, examiner watches  
**Timebox:** 15–20 minutes including tests  
**Tools:** JDK 25, IntelliJ or VS Code, JUnit 5 allowed

Suggested weight in the [final exam](final-exam-guide.md): **15%**. You cannot score below **50%** on this station and still pass the battery.

The curriculum question:

> Write a Java method that checks whether an API returned the expected status code. Then test it.

---

## Prompt (give this to the student)

Write a method that reports whether an HTTP status code matches what the test expected.

```text
statusMatches(expected, actual) → boolean
```

Then prove it with tests (JUnit 5 preferred). A small `main` is acceptable if JUnit is not wired; say so out loud and still cover both cases.

### Required behavior

| expected | actual | result |
| --- | --- | --- |
| 200 | 200 | `true` |
| 200 | 404 | `false` |
| 201 | 201 | `true` |
| 201 | 200 | `false` |

### Stretch if time remains (problem solving)

Print a failure message that includes **expected** and **actual** once each (Week 2 / Project 1 muscle). Do **not** start a framework, a Maven multi-module repo, or Selenium.

---

## What the examiner watches

- You do not panic. Silence for 20 seconds of thinking is allowed.
- Names are `expected` and `actual`, not `x` and `y`.
- The method is at class level, not nested inside `main`.
- You compare with `==` because these are `int`s.
- You write at least two tests: match and mismatch.
- You run the tests (or `main`) and read the output.
- You do not overbuild.

### Starter shape (student may type this)

```java
public final class StatusCodes {
    private StatusCodes() {}

    public static boolean statusMatches(int expected, int actual) {
        return expected == actual;
    }
}
```

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatusCodesTest {

    @Test
    void shouldMatchOk() {
        assertTrue(StatusCodes.statusMatches(200, 200));
        // or: assertEquals(true, StatusCodes.statusMatches(200, 200));
    }

    @Test
    void shouldRejectNotFound() {
        assertFalse(StatusCodes.statusMatches(200, 404));
    }
}
```

Either `assertTrue`/`assertFalse` or `assertEquals(true, ...)` is fine. Arrange–act–assert in each test.

### Optional stretch implementation

```java
public static void printResult(int expected, int actual) {
    if (statusMatches(expected, actual)) {
        System.out.println("TEST PASSED");
    } else {
        System.out.println("TEST FAILED");
        System.out.println("expected: " + expected);
        System.out.println("actual: " + actual);
    }
}
```

---

## Rubric (15 points, scale as needed)

| Look for | Points |
| --- | --- |
| Compiles | 3 |
| Correct `==` behavior | 5 |
| Tests (or `main`) cover true and false | 4 |
| Names / no nested method / no overbuild | 3 |

Deductions: method inside `main`; `equals` on `Integer` objects in a confused way that breaks; comparing strings `"200"` without a reason; 40-line framework.

---

## Examiner script

1. Read the prompt aloud. Start the timer.
2. If the student freezes at JUnit, say: "A `main` that prints PASS/FAIL for two cases is enough; then we can add `@Test` if time."
3. After they run: "What would you do if expected were 201 and actual were 200?" They should not need a new method — same `statusMatches`.
4. Stop at 20 minutes. Partial credit for a correct method without tests is not a pass of this station.

---

## Practice

Time yourself: `statusMatches` + two tests in 15 minutes. Note where you stalled. That is the homework, not the exam.
