# Final Exam Guide

This is not a pop quiz. It is a **multi-part professional exam**: you write, you code live, you debug, you design, you talk.

If you can only do one piece, you are not finished. An SDET who cannot debug is not an SDET. An architect who cannot write `statusMatches` is only a slide deck.

Curriculum companion: [Part 66 — Final Exam](../../docs/part-66-final-exam/README.md).

---

## Goal

Complete every station below, including live `statusMatches` coding, the `User` class exam, the streams/records exam, and the 50-engineer / 5,000-test architecture defense.

Companies hire with mixed loops: coding screen, take-home, system design, behavioral. This exam is that loop in one place, aimed at Java SDET.

---

## What the student completes

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

---

## Suggested scoring

Keep coding heavy. A school may adjust numbers; do not drop live coding or capstone.

```text
Written               10%
Live coding           15%
Debugging             10%
OOP design            10%
API coding            10%
Automation challenge  15%
Capstone              15%
Architecture talk     10%
Technical interview    5%
```

**Pass:** 70% overall, **and** you cannot score below 50% on live coding or capstone.

Weekly assessments (Theory 20 / Coding 35 / Problem Solving 20 / Debugging 15 / Explanation 10) still apply during the course. The table above is the **final battery** only.

---

## Station map

| # | Station | File | Timebox |
| --- | --- | --- | --- |
| 1 | Java written | [java-written-exam.md](java-written-exam.md) | 45–60 min |
| 2 | Live coding | [live-coding-exam.md](live-coding-exam.md) | 15–20 min |
| 3 | Debugging | [debugging-exam.md](debugging-exam.md) | 20–30 min |
| 4 | OOP design | [oop-design-exam.md](oop-design-exam.md) | 30–40 min |
| 5 | Advanced Java (results pipeline) | [advanced-java-exam.md](advanced-java-exam.md) | 30–40 min |
| 6 | API coding | [api-coding-exam.md](api-coding-exam.md) | 25 min |
| 7 | Automation challenge | [automation-challenge.md](automation-challenge.md) | 45–60 min |
| 8 | Capstone demo | Part 62 + below | 20–30 min demo |
| 9 | Architecture | [architecture-exam.md](architecture-exam.md) | 45 + 15 min |
| 10 | Technical interview | [../interview/](../interview/beginner.md) mix | 30–45 min |

Advanced Java may be scheduled as its own sitting on a second day. It is still required for a complete pass of the Java SDET track.

---

## 1. Java written exam

Closed book. Trace a loop, draw `.java` → bytecode → JVM, `==` vs `equals`, checked vs unchecked, Maven layout, 401/403/404, why sleep is a bad default.

---

## 2. Live coding — `statusMatches`

Write:

```java
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
}
```

Then test it: 200/200 true, 200/404 false. Stretch: print expected and actual on failure.

See [live-coding-exam.md](live-coding-exam.md).

---

## 3. Debugging exam

Reproduce, read the first error, fix the cause, re-run, explain. Classic mix: method inside `main`; inverted if; empty catch; Selenium sleep + XPath + no quit.

See [debugging-exam.md](debugging-exam.md).

---

## 4. OOP design

`User` class, constructor, private fields, getters/setters, validation, `List<User>`.

See [oop-design-exam.md](oop-design-exam.md).

---

## 5. Advanced Java

Generics, Streams, Records, exception handling, collections — process `List<TestResult>`.

See [advanced-java-exam.md](advanced-java-exam.md).

---

## 6. API coding

HttpClient or REST Assured: GET, assert 200, explain given/when/then if used, no secret logging.

See [api-coding-exam.md](api-coding-exam.md).

---

## 7. Automation challenge

DriverFactory, page object, WebDriverWait, assert visible result, quit. No default sleep.

See [automation-challenge.md](automation-challenge.md).

---

## 8. Framework capstone

The Part 62 **Enterprise Java SDET Platform** must exist in Git. The examiner clones and follows the README.

Minimum live demo:

```text
mvn test          unit green
one UI smoke
one API test
show config       two environments
show no secrets   grep / log sample
show CI           green PR or YAML
```

If Grid is absent, say why (YAGNI) and how you would add it.

This station is not a separate markdown exam file; it is the student's repository.

---

## 9. Architecture presentation

10 minutes + 5 questions in a short sitting, or the full [architecture-exam.md](architecture-exam.md) (45 + 15).

Must include:

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

Scenario: **50 engineers, 5,000 tests, 3 environments, Chrome/Firefox/Edge, UI/API/Database, parallel, CI/CD.** Defend every decision.

---

## 10. Technical interview

30–45 minutes. Mix [beginner](../interview/beginner.md), [intermediate](../interview/intermediate.md), [advanced Java](../interview/advanced-java.md), and [SDET](../interview/sdet.md). Include at least one prompt from [architect](../interview/architect.md).

Practice: 10 seconds think, 60–90 seconds speak. Sketches are ideas to hit, not scripts.

---

## How to schedule (example two-day battery)

**Day 1**

- Written (morning)
- Live coding + debugging (late morning)
- OOP + advanced Java (afternoon)

**Day 2**

- API + automation (morning)
- Capstone clone + demo (afternoon)
- Architecture defense + technical interview

---

## Student practice (not the real final)

1. Time `statusMatches` + two tests in 15 minutes.
2. `User` class in 30 minutes.
3. Whiteboard the 5,000-test design in 15 minutes; then fill gaps from the architect model answer.
4. Do not sit the real final the same week you first read this file.

---

## Knowledge check (for the student)

1. List the nine completion items from Part 66 (written through technical interview).
2. Write `statusMatches` from memory.
3. What does the intermediate OOP exam require besides User fields?
4. What five tools/ideas does the advanced exam use?
5. Capstone must be cloneable — true or false?
6. Architecture scenario: how many engineers and tests?
7. How many environments? Which browsers?
8. Which layers: UI, API, Database?
9. What two things must you still do besides design slides?
10. Can quiz-only strength pass this battery?

### Knowledge check answers

1. Written, live coding, debugging, OOP design, API coding, automation challenge, framework capstone, architecture presentation, technical interview
2. `return expected == actual;`
3. Constructor, private fields, getters/setters, validation, `List<User>`
4. Generics, Streams, Records, Exception Handling, Collections
5. True
6. 50 and 5,000
7. 3; Chrome, Firefox, Edge
8. All three
9. Parallel execution and CI/CD — and **defense** of decisions
10. No. Coding, debugging, and the capstone are required.
