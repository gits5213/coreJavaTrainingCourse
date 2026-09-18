# Part 65 — Interview Training

Interviews are a skill. Knowing Java silently is not the same as answering in 90 seconds with a diagram.

This part lists **Beginner, Intermediate, Advanced Java, SDET, and Architect** questions from the curriculum, with **model answer sketches**. Sketches are not scripts to memorize word-for-word. They are the ideas you must hit. Practice out loud.

## Goal

You will answer each band of questions in complete sentences, with one example, and you will draw the architect diagram without notes.

## Why It Matters

Hiring is a conversation. The person across the table is asking: can this human debug, design, and not leak secrets? Your capstone does not speak. You do.

## Real-Life Analogy

A driving test oral: they do not want a poem about engines. They want "mirrors, signal, maneuver" plus a reason.

## How to Practice

```text
Read question
  10 seconds think
  60–90 seconds speak
  Check sketch
  Repeat until the sketch's bullets appear naturally
```

Use STAR only when they ask for a story (flake you fixed). For "What is JVM?" do not start in 2019 on a team.

---

# Beginner Questions

### What is Java?

**Sketch:** A programming language (and a platform: bytecode + JVM) created so we write once and run on many operating systems. We write `.java`, compile to bytecode, JVM runs it. Used in banks, Android (historically), and lots of SDET tooling.

### Why was Java created?

**Sketch:** Oak / James Gosling / Sun, mid-1990s. Problem: C/C++ and hardware-specific binaries. Goal: **Write Once, Run Anywhere** via bytecode + JVM. Also safer memory than manual C pointers for many app teams.

### What is JVM?

**Sketch:** Java Virtual Machine — the engine that loads bytecode and executes it on Windows, macOS, Linux. Not the JDK by itself. You can picture: `.class` → class loader → JVM → CPU.

### What is JDK?

**Sketch:** Java Development Kit: compiler `javac`, `java` launcher, libraries, tools. Developers and SDETs install a JDK (this course: 25 LTS). JVM sits inside the runtime you get with the JDK.

### What is bytecode?

**Sketch:** The portable instruction format in `.class` files. Not machine code for one chip. The JVM interprets/JITs it. That is why WORA works.

### What is a variable?

**Sketch:** A named box with a type. `int expected = 200;` stores a value we can read later. Tests are full of expected vs actual variables.

### Primitive vs reference type?

**Sketch:** Primitives (`int`, `boolean`) hold the value in the box. References (`String`, `WebDriver`) hold a pointer to an object on the heap. `==` on references is identity; use `equals` for String content.

### `==` vs `.equals()`?

**Sketch:** `==` compares primitives' values, or whether two references point to the same object. `.equals()` is content equality when the class defines it — always for `String`. Status codes as `int` use `==`. Usernames as `String` use `equals`.

### `if` vs `switch`?

**Sketch:** `if` is general conditions (`actual == 200`, `name.equals("admin")`). `switch` picks among discrete values of one variable (status families, browser enum). Modern switch expressions exist; I learned classic `break` first.

### `for` vs `while`?

**Sketch:** `for` when you know the count or you iterate an array with an index. `while` when you repeat until a state changes (retry until dashboard visible — though Selenium waits are better than a raw while+sleep).

---

# Intermediate

### What is OOP?

**Sketch:** Organizing code as objects that combine data and behavior. Class is the blueprint; object is an instance. Helps model `User`, `LoginPage`, `Order`. Four common pillars people name: encapsulation, inheritance, polymorphism, abstraction — I can define each.

### Class vs object?

**Sketch:** `class LoginPage` is the blueprint. `new LoginPage(driver)` is one page object for one browser session. Two objects do not automatically share field values (unless static).

### What is encapsulation?

**Sketch:** Hide fields (`private balance`), expose methods (`withdraw`). Callers cannot put the account in an illegal state as easily. Page objects encapsulate locators.

### Inheritance?

**Sketch:** `is-a` relationship: `ChromeDriver` is a `WebDriver` implementation. Easy to misuse for code reuse (`BaseTest` towers). Prefer composition when it is `has-a`.

### Polymorphism?

**Sketch:** Treat different types through a common API: `WebDriver driver = factory.create(type);` then `driver.get` works for Chrome or Firefox. Tests depend on the interface.

### Interface?

**Sketch:** A contract of methods. `WebDriver` is an interface. A class `implements` it. Good for DriverFactory return type, `UserDirectory`, TestNG is not this — different word.

### Abstract class?

**Sketch:** Partial implementation; cannot `new` it. Use when subclasses share code *and* identity. Interfaces preferred for pure contracts in modern Java. Do not make AbstractEverything.

### Composition?

**Sketch:** `has-a`: `LoginPage` has a `WebDriver`. `TestClass` has a `LoginPage`. Usually clearer than inheriting 8 base classes.

### List vs Set?

**Sketch:** `List` ordered, duplicates allowed — test steps, users in a file. `Set` unique — browser names you already ran. `List<User>` for a roster; `Set<String>` for unique ids.

### HashMap?

**Sketch:** Key → value. `Map<String, String> headers`. `get("Authorization")`. Unordered. Average fast lookup. Keys need `equals`/`hashCode`. Do not use Map as a fake object when a `LoginData` record exists.

### Exception?

**Sketch:** An error object thrown when a path fails. `NoSuchElementException`, `IOException`. Tests fail when unexpected exceptions escape. Catch only what you can handle.

### Checked vs unchecked?

**Sketch:** Checked (`IOException`) must be declared or caught — compiler enforces. Unchecked (`RuntimeException`, `NullPointerException`, most Selenium exceptions) not declared. SDET rule: do not swallow either.

---

# Advanced Java

### Generics?

**Sketch:** `List<User>` not raw `List`. Compiler catches putting a `String` in a user list. API clients return `List<Order>`. Wildcards exist; I use them when I must.

### Streams?

**Sketch:** `list.stream().filter(r -> r.failed()).count()`. Declarative processing of collections. I use them when they read clearer than a loop, not to show off on `add(int,int)`.

### Lambda?

**Sketch:** `(r) -> r.failed()` as a concise implementation of a functional interface. Waits: `wait.until(d -> d.getTitle().contains("Dash"))`.

### Optional?

**Sketch:** A box that may be empty. `findUser(id)` returns `Optional<User>` instead of null. I do not use Optional as a field everywhere; I use it as a return when missing is normal.

### Records?

**Sketch:** Immutable data carriers: `record LoginData(String username, String password) {}`. Perfect for test data. Override `toString` to mask secrets.

### JVM memory?

**Sketch:** Stack: method frames, primitives, references. Heap: objects. Each thread has a stack. `WebDriver` objects live on the heap. Stack overflow vs OutOfMemoryError are different.

### Garbage collection?

**Sketch:** JVM reclaims heap objects with no live references. I do not call `System.gc()` in tests as a strategy. Leaking WebDriver sessions is a *native* leak too — `quit()` matters.

### Concurrency?

**Sketch:** Multiple threads running. TestNG parallel methods = concurrent tests. Shared mutable state is the danger.

### Race condition?

**Sketch:** Result depends on timing. Two tests one driver: who clicked last wins. Fix: isolation, not sleeps hoping to win the race.

### Thread safety?

**Sketch:** Correct under concurrent access. Immutable objects are easier. `ThreadLocal<WebDriver>` is per-thread isolation, not "the driver is thread-safe." Do not share a Chrome session.

### Immutability?

**Sketch:** Object state does not change after construction. Records, `final` fields. Safe to share config. Mutable driver session is the opposite — do not share it.

---

# SDET

### Maven?

**Sketch:** Build tool. `pom.xml` coordinates, dependencies, plugins. Layout `src/main/java` `src/test/java`. `mvn clean compile test package verify`. Reproducible CI.

### JUnit?

**Sketch:** Unit test runner. `@Test`, arrange-act-assert, `assertEquals(expected, actual)` in JUnit 5. Fast tests for Calculator, factories, matchers.

### TestNG?

**Sketch:** Common in automation: `@BeforeMethod` setup, `@Test`, `@AfterMethod` quit, `@DataProvider`, `@Parameters`, suites, parallel. Lifecycle: BeforeMethod → Setup → Test → Validation → AfterMethod → Cleanup.

### Selenium?

**Sketch:** Java test → WebDriver → browser → app. `new ChromeDriver(); get(url); quit();` Locators, waits, POM. Not the only tool; APIs often cheaper.

### REST Assured?

**Sketch:** Fluent HTTP tests. given=setup when=action then=validation. I still know HttpClient and status codes. Wrap in API clients. Never log tokens.

### POM?

**Sketch:** Page Object Model. Test → Page → Selenium → Browser. Locators and actions in `LoginPage.enterUsername`. Not DB/email. One place to fix ids.

### DriverFactory?

**Sketch:** `DriverFactory.create(BrowserType.CHROME)` returns Chrome/Firefox/Edge (or RemoteWebDriver). Tests do not `new ChromeDriver()`. Options and Grid live here.

### DataProvider?

**Sketch:** TestNG rows of data into one `@Test`. Progression: hardcoded → parameters → DataProvider → model → JSON → factory. Independent rows. Mask secrets in reports.

### ThreadLocal?

**Sketch:** `ThreadLocal<WebDriver>` stores one driver per thread so parallel tests do not share sessions. `set` in BeforeMethod, `get` in test, `quit` + `remove` in AfterMethod. Thread pools reuse threads — `remove` is mandatory. Opposite of singleton driver.

### Selenium Grid?

**Sketch:** Tests → RemoteWebDriver → Grid → browser nodes. Factory switches on GRID_URL. Quit frees slots. YAGNI until we need remote/cross-OS scale.

### CI/CD?

**Sketch:** Push → GitHub → CI compile → unit → automation → report. PR: compile, quality, unit, smoke, merge. Nightly: regression + cross browser + API. Fast PR, deep night.

### Test isolation?

**Sketch:** Tests do not depend on order, shared driver, or the same mutable user. Unique data, ThreadLocal, independent asserts. Flakes are often isolation bugs.

---

# Architect

**Question:** Design a framework for 5,000 tests and 50 automation engineers.

Student should discuss:

```text
Repository Structure
Driver Strategy
API Architecture
Page Architecture
Data Architecture
Configuration
Parallelism
CI/CD
Reporting
Flaky Tests
Governance
Code Ownership
Security
```

### Model answer sketch (talk this in 8–12 minutes)

**Repository:** Monorepo `java-sdet-engineering` layout: `automation` main (config, driver, pages, components, api, database, models, factories, services, utils) and tests split unit/ui/api/smoke/regression. Maven. CODEOWNERS. PR template. Docs.

**Driver:** Factory + ThreadLocal. Local vs Grid via config. Chrome/Firefox/Edge. Never singleton driver. Always quit+remove.

**API:** Clients per service (`UserApiClient`), Rest Assured or HttpClient underneath, models for JSON. Tests do not paste URLs 5000 times.

**Pages:** POM, components for header/nav, waits inside pages, no JDBC in pages. Workflows/services for multi-step journeys.

**Data:** Records, factories for unique users, JSON/CSV for tables, secrets from vault. Independent tests.

**Config:** `qa`/`staging`/`dev` files, timeouts, GRID_URL, never passwords in Git.

**Parallel:** TestNG parallel classes or methods once isolation is proven; thread count sized to Grid slots.

**CI/CD:** PR = compile, quality, unit, smoke (minutes). Nightly = regression + cross-browser + API. Do not run 5000 UI tests on every commit.

**Reporting:** Allure + screenshots + exception + browser + env + timestamps. No PII.

**Flakes:** Treat as defects. Quarantine with expiry, do not retry as a culture. Root cause: waits, locators, shared data, isolation.

**Governance:** Review checklist (Part 59), linters, required checks, branch protection on `main`.

**Ownership:** CODEOWNERS by domain (payments pages vs identity API). 50 people cannot all own `Utils.java`.

**Security:** secrets in CI, masked logs, test accounts, no production customer data in UI evidence.

Close with: architecture solves problems; we grew into this (Part 61); I can still write `statusMatches` on a whiteboard.

## Student Exercise

Record answers to all Beginner questions in one sitting (notes, not a video required). Next day, Intermediate. Speak, do not only type.

## Challenge

Whiteboard the 5000-test design in 15 minutes. Timebox. Then fill gaps from the sketch.

## Knowledge Check

1. Why sketches not scripts?
2. JVM vs JDK one line each?
3. `==` on String risk?
4. ThreadLocal vs singleton driver?
5. PR vs nightly?
6. Name 5 architect discussion topics.
7. What is given/when/then?
8. Encapsulation in one line?
9. Why records for test data?
10. What must architect answers include besides folders?

## Interview Question

Practice the architect question until it is boring. That *is* the interview question.

## Homework

A friend asks you 10 random questions from this file. You answer without looking. Failed ones become flashcards.

---

## Answer Key

1. Interviews need ideas, not recitation robots
2. JVM runs bytecode; JDK is the dev kit including compiler
3. `==` may be identity not content
4. Per-thread isolation vs one shared session
5. Fast gate vs deep suite
6. Any five from the architect list
7. Setup, action, validation
8. Hide data, expose safe operations
9. Named immutable fields, compact
10. Trade-offs, flakes, security, CI shape, evolution
