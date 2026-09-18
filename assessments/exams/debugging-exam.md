# Debugging Exam

**Level:** beginner through SDET  
**Format:** broken code provided; student reproduces, diagnoses, fixes, re-runs, explains  
**Timebox:** 20–30 minutes  
**Tools:** IntelliJ debugger allowed. Do not guess-and-change five lines at once.

Suggested weight in the [final exam](final-exam-guide.md): **10%**.

Course flow to memorize:

```text
Run  →  Breakpoint  →  Pause  →  Inspect  →  Understand  →  Fix
```

Read stack traces from the **top**. The first line that mentions *your* class is usually the clue.

---

## Station rules

The student must:

1. Reproduce the failure (compile or run).
2. Read the first error.
3. Fix the **cause**, not a random rewrite.
4. Re-run.
5. Explain what they saw, in two minutes.

Examiner: if they start rewriting the class into a new design, stop them: "Minimal fix first."

---

## Problem 1 — Nested method (compile)

Give this first. Many students fail the live-coding exam for the same reason.

```java
public class Broken {
    public static void main(String[] args) {
        public static boolean statusMatches(int expected, int actual) {
            return expected == actual;
        }
        System.out.println(statusMatches(200, 200));
    }
}
```

### Expected diagnosis

Java does not allow a method declared inside another method. Move `statusMatches` to class level. Re-run; it should print `true`.

---

## Problem 2 — Logic bug (debugger)

This **compiles**. The test (or `main`) prints `TEST PASSED` when the API returned 404.

```java
public class ResultPrinter {
    public static void main(String[] args) {
        int expected = 200;
        int actual = 404;
        if (expected != actual) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
            System.out.println("expected: " + expected);
            System.out.println("actual: " + actual);
        }
    }
}
```

### Expected diagnosis

The condition is inverted: `!=` takes the PASS branch. The computer did what the code said; the code did not say what the author meant. That is the course definition of a bug.

**Fix:** `if (expected == actual)` for PASS, else FAIL with both numbers.

Examiner may ask them to set a breakpoint on the `if` and inspect `expected` and `actual` before editing.

---

## Problem 3 — Exception swallowed

```java
public class Parser {
    public static int parseStatus(String body) {
        try {
            return Integer.parseInt(body.trim());
        } catch (Exception e) {
        }
        return 200;
    }

    public static void main(String[] args) {
        System.out.println(parseStatus("hello"));
    }
}
```

### Expected diagnosis

`parseInt("hello")` throws `NumberFormatException`. Empty catch hides it. Returning `200` makes a garbage body look like HTTP OK. A test that uses this parser would lie.

**Fix:** do not catch, or catch `NumberFormatException`, log, and rethrow / fail. Never default to 200.

---

## Problem 4 — Selenium mix (after Week 6)

Use if the student has reached Selenium.

```java
public class BrokenUiTest {
    @Test
    public void dashboard() throws Exception {
        WebDriver driver = new ChromeDriver();
        driver.get("https://training.example/app");
        Thread.sleep(5000);
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/button")).click();
        assert driver.getTitle().contains("Dashboard");
    }
}
```

Symptoms: flaky locally; fails on CI; Chrome left running after failure.

### Expected diagnosis (order)

1. **Locator:** absolute XPath is fragile. Confirm a stable id/css in DevTools **before** changing waits. Sleep will not fix a wrong locator.
2. **Wait:** replace `Thread.sleep` with `WebDriverWait` until clickable or title contains.
3. **Cleanup:** `quit` in `finally` or `@AfterMethod`. The failed `assert` currently skips quit.
4. **Factory:** `new ChromeDriver()` in the test blocks Grid/options later.

Do not "fix" this by raising sleep to 15 seconds.

---

## Problem 5 — Parallel singleton (after Week 7)

```java
public class Drivers {
    public static WebDriver driver;

    public static WebDriver get() {
        if (driver == null) {
            driver = new ChromeDriver();
        }
        return driver;
    }
}
```

TestNG `parallel="methods"`. Tests click the wrong pages.

### Expected diagnosis

One heap object, many threads: race. Singleton driver is the opposite of `ThreadLocal<WebDriver>`. Fix: factory + ThreadLocal, `quit` + `remove` in AfterMethod.

---

## Rubric (15 points)

| Look for | Points |
| --- | --- |
| Reproduces and reads the first error | 4 |
| Names the real cause | 4 |
| Minimal correct fix | 4 |
| Re-run + clear explanation | 3 |

Choose 2–3 problems for a 30-minute sitting. Problem 1 + 2 for early students. Add 3–5 later.

---

## Answer summary

| # | Cause | Minimal fix |
| --- | --- | --- |
| 1 | Method nested in `main` | Move to class scope |
| 2 | Inverted `if` | PASS when `==` |
| 3 | Empty catch + fake 200 | Fail honestly |
| 4 | XPath + sleep + no quit | Locator, wait, finally quit |
| 5 | Shared static driver | ThreadLocal + quit/remove |
