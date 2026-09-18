# Week 5 — JVM, Git, and Clean Code

**Coverage:** Parts 30–40 (JVM memory, concurrency, debugging, build tools, Maven, Git, GitHub, clean code, DRY/KISS/YAGNI, SOLID, design patterns)

**Suggested timebox:** 90 minutes

| Activity | Time |
| --- | --- |
| Quiz (closed book) | 15 minutes |
| Coding assignment | 35 minutes |
| Debugging problem | 20 minutes |
| Explanation exercise | 10 minutes |
| Buffer | 10 minutes |

Score: Theory 20%, Coding 35%, Problem Solving 20%, Debugging 15%, Explanation 10%. Pass bar: 70% weighted.

---

## Quiz (10 questions)

1. Where do method frames and local primitives live, and where do objects created with `new` live? What is different about stacks when there are many threads?
2. Garbage collection reclaims unreachable heap objects. Does GC close Chrome or file handles for you? What is `OutOfMemoryError` often telling an SDET who never calls `quit()`?
3. What does the JIT compiler do with "hot" bytecode?
4. Concurrency vs parallelism, in one line each. What is a race condition?
5. Why is a shared static `WebDriver` dangerous? How does that relate to thread safety?
6. Write the debugging flow from the course (run → … → fix). What should you read first on a stack trace?
7. Why do build tools exist? What file is Maven's project description, and where do production vs test Java files live?
8. Git vs GitHub: which is the local tool, and which is the team's hosted copy? Name `status`, `add`, `commit`, `push`, `pull` in one clause each.
9. Define DRY, KISS, and YAGNI. Complete the sentence: do not create complex frameworks for…
10. Name all five SOLID letters. Give the SDET example of a `LoginPage` that violates Single Responsibility. Why is Singleton often overused for a driver?

---

## Coding assignment

**Title:** Tiny Maven library + tests, named cleanly

### Requirements

1. Use a Maven layout:

   ```text
   src/main/java/.../StatusCodes.java
   src/test/java/.../StatusCodesTest.java
   pom.xml
   ```

2. `StatusCodes` production code:
   - `public static boolean statusMatches(int expected, int actual)`
   - `public static String family(int statusCode)` returning `success`, `client`, `server`, or `other` (same bands as Week 2 stretch)
   - Meaningful names. No `doIt(int x)`.

3. JUnit 5 tests (arrange–act–assert):
   - `statusMatches` true and false cases
   - `family` for 200, 404, 500, and 99

4. A `DriverFactory`-shaped **design on paper or as a small interface** (you do not need Selenium this week):
   - `interface BrowserFactory { Browser create(BrowserType type); }`
   - One sentence why this is Factory, not Singleton.

5. Run `mvn test` (not only the IntelliJ green triangle) and paste or note the result.

### Acceptance criteria

- [ ] `mvn test` is green.
- [ ] Tests live under `src/test/java`.
- [ ] Method names describe behavior (`shouldMatchOk`, `shouldClassifyNotFound`).
- [ ] No duplication of the 2xx/4xx/5xx logic in the test class (DRY: production method is the single place).
- [ ] You did **not** add a "framework" folder tree you do not need (YAGNI).

### Problem-solving stretch

Add a `UserBuilder` (Builder pattern) with `username` and `email`, validation on `build()`. Explain in a comment why Builder helps when constructors grow too many parameters — and why you would **not** add Observer, Facade, and Adapter in the same class "for the portfolio."

---

## Debugging problem

A teammate cannot get tests to run on CI. Local IntelliJ Run works. The repo looks like this:

```text
project/
├── StatusCodes.java          (in the repo root)
├── StatusCodesTest.java      (in the repo root)
└── pom.xml                   (exists, but surefire finds 0 tests)
```

`StatusCodesTest` contains:

```java
public class StatusCodesTest {
    void shouldMatch() {
        if (StatusCodes.statusMatches(200, 200) == false) {
            throw new RuntimeException("fail");
        }
    }
}
```

Separately, this concurrent counter is flaky:

```java
public class Counter {
    public static int total = 0;

    public static void increment() {
        int t = total;
        total = t + 1;
    }
}
```

Two threads call `increment()` many times. The final `total` is often less than expected.

### Your job

1. Diagnose the Maven layout / test-discovery problem.
2. Diagnose the missing JUnit annotation / assertion style.
3. Diagnose the race on `Counter`.
4. Propose a **minimal** fix for each. Do not rewrite Maven into Gradle for fun.

### Expected diagnosis

| Area | Cause |
| --- | --- |
| Maven | Standard layout is `src/main/java` and `src/test/java`. Root `.java` files are not compiled as tests. CI runs `mvn test`, not IntelliJ's classpath guess. |
| JUnit | No `@Test`. A void method named `shouldMatch` is not a test. Use `Assertions.assertEquals` (or AssertJ), not a hidden `RuntimeException`. |
| Race | Two threads read the same `total`, both add 1, both write the same number. Shared mutable static state. `synchronized` or `AtomicInteger` are possible; for WebDriver the lesson is **isolation**, not a global counter. |

**Fix:** move files; add JUnit dependency and `@Test`; do not share mutable static state across tests/threads.

---

## Explanation exercise

**Prompt (90 seconds): choose one**

**A.** Maven lifecycle in plain language: what `mvn clean test` does, and why CI uses it.

**B.** ThreadLocal vs singleton driver — even if you have not coded ThreadLocal yet, use heap-vs-stack and race-condition language from this week.

**Strong answer hits (A):** `pom.xml` declares coordinates and dependencies; Maven downloads, compiles main then test, runs tests, reports. CI clones Git and runs the same command so laptops are not the source of truth.

**Strong answer hits (B):** A singleton driver is one session on the heap, shared by many threads — a race (who clicked last). Isolation means one driver per test/thread. `quit()` matters; GC will not close the browser for you.

---

## Answer key

1. Stack: frames, locals, references. Heap: objects. Each thread has its own stack; the heap is shared.
2. No. GC does not `quit()` browsers. Leaking drivers is a native/resource leak and can also exhaust heap. `quit()` is required.
3. It observes hot bytecode and compiles it to optimized machine code. Cold JVMs feel slower.
4. Concurrency: many tasks in progress (interleaved). Parallelism: many at the same instant on many cores. A race: result depends on timing of shared mutable state.
5. One Chrome session, many tests; clicks interleave. Thread safety means correct under concurrent access. Immutable config is easier; a live driver session is not something to share.
6. Run → Breakpoint → Pause → Inspect → Understand → Fix. Read the stack trace from the **top**; the first line in *your* class is usually the clue.
7. Reproducible dependencies, compile, test, package. `pom.xml`. `src/main/java` vs `src/test/java`.
8. Git is local version control. GitHub hosts the shared repo, PRs, CI. `status` inspects; `add` stages; `commit` snapshots; `push` sends; `pull` fetches and integrates.
9. Don't Repeat Yourself; Keep It Simple; You Aren't Gonna Need It. …problems you do not have.
10. S, O, L, I, D. A `LoginPage` that also does DB, API, email, screenshots, and logging violates SRP. Singleton driver is a shared session; Factory + per-test lifecycle is the usual SDET move.

**Debugging key:** Maven folders, `@Test`, race on static `total`.
