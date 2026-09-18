# Week 7 — Framework and Architecture

**Coverage:** Parts 54–62 (parallel automation / ThreadLocal, Selenium Grid, logging, reporting, CI/CD, code review, enterprise architecture, framework evolution, project roadmap)

**Suggested timebox:** 90 minutes (presentation-style explanation may use the last 15)

| Activity | Time |
| --- | --- |
| Quiz (closed book) | 15 minutes |
| Coding / design assignment | 40 minutes |
| Debugging problem | 15 minutes |
| Explanation exercise | 15 minutes |
| Buffer | 5 minutes |

Score: Theory 20%, Coding 35%, Problem Solving 20%, Debugging 15%, Explanation 10%. Pass bar: 70% weighted.

This week the "coding" piece is mostly **design you can defend**, plus a small ThreadLocal-correct snippet. The architecture exam reuses the same scenario at larger scale.

---

## Quiz (10 questions)

1. Why must parallel UI tests not share one `WebDriver`? What does `ThreadLocal<WebDriver>` store, and why is `remove()` required after `quit()`?
2. When do you add Selenium Grid, and when is it YAGNI? Draw Tests → RemoteWebDriver → Grid → nodes.
3. Why is `System.out.println` not a logging strategy? Which levels exist, and what must you never log?
4. What evidence belongs in a test report (Allure or equivalent)?
5. Draw Developer → git push → GitHub → CI → compile → unit → automation → report. What belongs on a **PR** pipeline vs a **nightly** pipeline?
6. List at least six code-review questions from the course checklist (purpose, names, duplication, exceptions, resources, shared state, isolation, secrets, inheritance vs composition, testability).
7. Draw the enterprise test travel path: Tests → workflows → pages/API clients → application. Name supporting packages (models, factories, config, …).
8. Architecture should evolve from pain. Give three growth stages from Part 61 in order, and one thing you must **not** build on day one.
9. What is the capstone of this course supposed to be (Part 62), as opposed to a pile of unrelated scripts?
10. Fifty engineers cannot all own `Utils.java`. What is CODEOWNERS for?

---

## Coding assignment

**Title:** Design a slice of an enterprise platform, then implement one isolation-safe driver holder

### Requirements

**A. Written design (counts as coding + problem solving)**

You are preparing a team of **50 engineers**, **5,000 tests**, **3 environments**, browsers **Chrome / Firefox / Edge**, layers **UI / API / Database**, **parallel** runs, and **CI/CD**.

Write 1–2 pages (or a clear diagram plus bullets) covering:

1. Repository layout (packages, not a novel).
2. Driver strategy: factory + ThreadLocal; local vs Grid via config.
3. API clients vs page objects vs workflows.
4. Config for three environments; secrets not in Git.
5. PR vs nightly: what runs where.
6. Flake policy (quarantine with expiry, not retry-as-culture).
7. Logging/reporting: evidence, no PII.

**B. Code slice**

Implement a small `DriverManager` (no need for a real Grid):

```java
public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static void set(WebDriver driver) { ... }
    public static WebDriver get() { ... }
    public static void quit() { ... }  // quit + remove
}
```

- `get()` should fail clearly if no driver was set (do not return `null` silently).
- `quit()` must `remove()` even if `quit` throws.
- A short comment: thread pools reuse threads; leftover drivers would leak to the next test.

### Acceptance criteria

- [ ] Design mentions isolation, PR vs nightly, and secrets.
- [ ] No singleton `WebDriver`.
- [ ] `quit` + `remove` together.
- [ ] You did not schedule 5,000 UI tests on every commit.
- [ ] God `LoginPage` (DB + email + API + UI) is explicitly rejected.

### Problem-solving stretch

Pick thread count vs Grid slot count. What happens if you start 50 Chrome sessions on a laptop with 4 cores and 8 Grid slots?

---

## Debugging problem

```java
public class Drivers {
    public static WebDriver driver; // shared

    public static WebDriver chrome() {
        if (driver == null) {
            driver = new ChromeDriver();
        }
        return driver;
    }
}

// TestNG: parallel="methods"
public class CheckoutTests {
    @BeforeMethod
    public void open() {
        Drivers.chrome().get("https://shop.example");
    }

    @Test
    public void addItem() { /* clicks */ }

    @Test
    public void pay() { /* clicks */ }

    @AfterMethod
    public void close() {
        // "save time" — do not quit
    }
}
```

Logs also contain:

```text
INFO login password=Passw0rd! token=eyJhbGciOi...
```

CI on every pull request runs the entire 5,000-test UI regression on Chrome, Firefox, and Edge.

### Your job

1. Name the parallelism bug.
2. Name the lifecycle bug.
3. Name the security bug.
4. Name the CI design bug.
5. Write the corrected driver lifecycle in 10–15 lines (ThreadLocal, set in BeforeMethod, quit+remove in AfterMethod).

### Expected diagnosis

| Bug | Diagnosis |
| --- | --- |
| Static singleton `driver` | Parallel methods share one session; races; "who clicked last." Opposite of ThreadLocal. |
| No `quit` | Browser processes accumulate; Grid slots never free; later tests inherit a dirty session if someone starts quitting "sometimes." |
| Password and JWT in logs | Reporting/CI artifacts leak secrets. Mask; vault; never INFO them. |
| 5,000 × 3 browsers on every PR | PR should be compile, quality, unit, smoke (minutes). Cross-browser regression is nightly. |

**Fix:** `ThreadLocal`, factory, AfterMethod `quit`+`remove`, secret hygiene, split PR/nightly.

---

## Explanation exercise

**Prompt (3–4 minutes spoken, or a one-page write-up):**

> Design a framework for 5,000 tests and 50 automation engineers. Walk repository structure, driver strategy, API, pages, data, config, parallelism, CI/CD, reporting, flaky tests, governance, code ownership, and security.

This is a rehearsal for [architecture-exam.md](../exams/architecture-exam.md) and [architect.md](../interview/architect.md). Use the diagram:

```text
                    TESTS
                      │
        ┌─────────────┼─────────────┐
        │             │             │
       UI            API       Integration
        │             │
        ↓             ↓
   Page Objects    API Clients
        │             │
        └──────┬──────┘
               ↓
         Business Services
               ↓
          Application
```

**Strong answer hits:** architecture solves problems and grows (Part 61); you can still write `statusMatches` on a whiteboard; singleton driver, default sleeps, passwords in Git, and 5,000 UI tests per PR are incorrect.

---

## Answer key

1. Shared driver is a race. ThreadLocal holds one driver per thread. Thread pools reuse threads; `remove()` prevents the next test from seeing the old driver.
2. Add Grid when you need remote/cross-OS scale. YAGNI until then. Tests → RemoteWebDriver → Grid → browser nodes.
3. Use SLF4J (or equivalent) with levels TRACE DEBUG INFO WARN ERROR. Never log passwords, tokens, secrets, or sensitive customer data.
4. Results plus evidence: screenshot, exception, browser, environment, timestamps. No PII.
5. PR: compile, quality, unit, smoke. Nightly: regression, cross-browser, deeper API. Fast gate vs deep suite.
6. One purpose? Meaningful names? Duplication justified? Exceptions correct? Resources closed? Shared state safe? Tests independent? Secrets exposed? Inheritance necessary? Composition simpler? Testable?
7. Tests call workflows; workflows use pages and API clients; those talk to the app. Support: models, factories, config, fixtures, database, logging, reporting, utils.
8. Example path: one class → methods → OOP → packages → POM → models → factories → config → API clients → parallel → CI → governance. Do not build Grid + 12-layer data platform on day one.
9. An Enterprise Java SDET Platform in Git, cloneable, with unit + UI smoke + API + two envs + no secrets + CI — not disconnected scripts.
10. Domain ownership (payments pages vs identity API). Review and branch protection on `main`.

**Debugging key:** singleton + no quit + secrets in logs + giant PR pipeline.
