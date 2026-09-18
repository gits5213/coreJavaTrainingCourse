# Part 66 — Final Exam

This is not a pop quiz. It is a **multi-part professional exam**: you write, you code live, you debug, you design, you talk.

If you can only do one piece, you are not finished. An SDET who cannot debug is not an SDET. An architect who cannot write `statusMatches` is a slide deck (the course already told you that).

## Goal

You will complete every station below, including the live `statusMatches` coding, the User class exam, the streams/records exam, and the 50-engineer / 5000-test architecture defense.

## Why It Matters

Companies hire with mixed loops: coding screen, take-home, system design, behavioral. This exam is that loop in one place, aimed at Java SDET.

## Real-Life Analogy

A medical board: written science, live procedure, diagnosis of a broken case, ethics oral. Passing only the written part does not make a surgeon.

## What the Student Completes

```text
Java Written Exam
       +
Live Coding
       +
Debugging Exam
       +
OOP Design Exercise
       +
API Coding
       +
Automation Challenge
       +
Framework Capstone
       +
Architecture Presentation
       +
Technical Interview
```

Suggested scoring (adjust as a school, keep coding heavy):

```text
Written              10%
Live coding          15%
Debugging            10%
OOP design           10%
API coding           10%
Automation challenge 15%
Capstone             15%
Architecture talk    10%
Technical interview  5%
```

Pass: **70%** overall, and you cannot score below 50% on live coding or capstone.

---

# 1. Java Written Exam

Closed book, 45–60 minutes. Sample prompts:

- Trace a `for` loop on paper.
- Draw `.java` → bytecode → JVM.
- `==` vs `equals` for `"qa"` interned vs `new String`.
- Checked vs unchecked: name one of each.
- Maven: where tests live; what `mvn test` does.
- HTTP: 401 vs 403 vs 404.
- Why `Thread.sleep(5000)` is a bad default.

Grade for correctness of ideas, not handwriting.

---

# 2. Live Coding — `statusMatches`

**Question (curriculum):**

> Write a Java method that checks whether an API returned the expected status code.

```java
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
}
```

**Then test it.**

Examiner watches:

- You do not panic.
- You name expected vs actual correctly.
- You write JUnit or a small `main` that proves 200/200 true, 200/404 false.
- You do not overbuild a framework.

Stretch if time: print a failure message with both numbers (Project 1 muscle).

```java
@Test
void shouldMatchOk() {
    assertEquals(true, StatusCodes.statusMatches(200, 200));
}

@Test
void shouldRejectNotFound() {
    assertEquals(false, StatusCodes.statusMatches(200, 404));
}
```

Timebox: 15–20 minutes including tests.

---

# 3. Debugging Exam

Provide a broken project (or this classic mix):

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

And/or: Selenium with sleep + absolute XPath + forgotten `quit`.

Student must:

1. Reproduce.
2. Read the first error.
3. Fix the cause (not a random rewrite).
4. Re-run.
5. Explain what they saw.

Timebox: 20–30 minutes.

---

# 4. OOP Design Exercise / Intermediate Exam

**Curriculum intermediate exam:**

Create:

```text
User class
Constructor
private fields
getters/setters
validation
List<User>
```

**Bar:**

```java
public class User {
    private final String username;
    private String email;

    public User(String username, String email) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username required");
        }
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

```java
List<User> users = new ArrayList<>();
users.add(new User("aisha", "a@x.com"));
```

Find by username. Reject blank username. Examiner asks: why private? why not public fields? Optional: prefer records if they justify immutability (`record User(String username, String email)` with compact constructor) — that can exceed the intermediate brief; both OK if they defend it.

Timebox: 30–40 minutes.

---

# 5. API Coding

Using Java HttpClient **or** REST Assured:

- GET a URL (example.com or training API).
- Assert status 200 with `statusMatches` or REST Assured `statusCode(200)`.
- Explain given/when/then if they used REST Assured.
- Do not log secrets (even if the API has none — show the habit).

Timebox: 25 minutes.

---

# 6. Automation Challenge

A small UI flow on a training site:

- DriverFactory (not raw Chrome in the test).
- Page object with stable locator.
- WebDriverWait, **no** default sleep.
- Assert a visible result.
- `quit` in finally / AfterMethod.

Optional stretch: one API setup + UI assert (integration).

Timebox: 45–60 minutes.

---

# 7. Framework Capstone

The Part 62 **Enterprise Java SDET Platform** must exist in Git. Examiner clones and follows README.

Minimum live demo:

```text
mvn test          unit green
one UI smoke
one API test
show config       two envs
show no secrets   grep / log sample
show CI           green PR or YAML
```

If Grid is absent, student must say why (YAGNI) and how they would add it.

---

# 8. Architecture Presentation

10 minutes + 5 questions.

Must include the capstone diagram:

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

---

# 9. Technical Interview

30–45 minutes. Mix Part 65 questions. Include at least one architect prompt.

---

# Advanced Exam (streams / records)

**Curriculum advanced exam:**

Use:

```text
Generics
Streams
Records
Exception Handling
Collections
```

to process test results.

**Task:** Given a `List<TestResult>`, compute failed names and average duration of passed tests.

```java
public record TestResult(String name, String status, int durationMs) {
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
}
```

Examiner looks for: record, stream not a messy 40-line mutation unless they can defend a loop, exception when no passes if that was the spec, `List<TestResult>` not raw List.

Timebox: 30 minutes + tests.

---

# FINAL ARCHITECTURE EXAM

**Scenario:**

```text
50 engineers
5,000 automated tests
3 environments
Chrome
Firefox
Edge
UI
API
Database
Parallel execution
CI/CD
```

**Student must design the system and defend every decision.**

Use Part 65 architect sketch. Mandatory decisions:

| Topic | You must pick and defend |
| --- | --- |
| Repo | monorepo vs many repos; tree |
| Drivers | factory, ThreadLocal, Grid or not |
| Layers | tests, workflows, pages, clients |
| Data | factories, uniqueness, secrets |
| Envs | 3 config files, how selected |
| Browsers | matrix nightly vs PR Chrome only |
| DB | who may query QA; cleanup |
| Parallel | unit of parallel; slot counts |
| CI | PR vs nightly contents |
| Flakes | policy |
| Governance | CODEOWNERS, reviews |
| Security | logs, screenshots, vault |

There is no single correct vendor. There is incorrect: singleton driver, sleeps as default, passwords in Git, 5000 UI tests on every PR, God LoginPage.

Timebox: 45 minutes design (whiteboard) + 15 minutes defense.

---

## Student Exercise (practice exam, not the final)

Time yourself on `statusMatches` + two tests in 15 minutes. Then User class in 30. Note where you stalled.

## Challenge

Give the architecture exam to a peer; they attack your design. Update the design. That is the job.

## Knowledge Check

1. List the nine completion items.
2. Write `statusMatches`.
3. What does intermediate exam require besides User fields?
4. What five tools/ideas does the advanced exam use?
5. Capstone must be cloneable — true?
6. Architecture scenario: how many engineers and tests?
7. How many environments?
8. Which browsers?
9. Which layers: UI API DB?
10. What two things must you still do besides design slides?

## Interview Question

The architecture exam **is** the interview. Practice it until you can defend quit+remove, PR vs nightly, and no secrets without notes.

## Homework

Create an exam checklist. Book a mock with a classmate. Do not take the real final the same week you first read this file.

---

## Answer Key

1. Written, live coding, debugging, OOP design, API coding, automation challenge, framework capstone, architecture presentation, technical interview
2. `return expected == actual;`
3. Constructor, private fields, getters/setters, validation, `List<User>`
4. Generics, Streams, Records, Exception Handling, Collections
5. True
6. 50 and 5,000
7. 3
8. Chrome, Firefox, Edge
9. Yes all three
10. Parallel + CI/CD (and defense of decisions)
