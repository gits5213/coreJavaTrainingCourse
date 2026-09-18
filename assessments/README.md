# Assessments — Java SDET Course

Weekly assessments are how this course checks that you can *use* the week's ideas, not only reread them.

Every week is more than extra chapters. It is a checkpoint: quiz, code, debug, and explain. SDET work is mixed in the same way. The scoring weights match the job more closely than a 100% multiple-choice quiz.

Related curriculum:

- [Part 64 — Weekly Assessment](../docs/part-64-weekly-assessment/README.md) — why the weights exist
- [Part 65 — Interview Training](../docs/part-65-interview-training/README.md) — spoken practice
- [Part 66 — Final Exam](../docs/part-66-final-exam/README.md) — the full battery

---

## How weekly assessment works

Typical week:

```text
Monday–Thursday     chapters + homework
Friday              assessment (60–90 minutes)
```

Each weekly file contains four activities:

```text
Quiz
+
Coding Assignment
+
Debugging Problem
+
Explanation Exercise
```

How to sit the week:

1. Close the book for the quiz.
2. Write the coding assignment in IntelliJ (or on paper first if the week is still Level 1).
3. Diagnose the debugging problem. Fix the cause, then re-run.
4. Explain the week's idea out loud or in writing, as if a teammate asked.

**Pass guideline:** 70% weighted overall. Coding cannot be skipped. Quiz points do not replace missing code.

If you score below 70% on coding, restudy that week's parts before moving on.

---

## Scoring

| Component | Weight | What it measures |
| --- | --- | --- |
| Theory | 20% | Vocabulary and accurate mental models |
| Coding | 35% | You can write working Java from a spec |
| Problem Solving | 20% | You can design a small solution, not only copy a sample |
| Debugging | 15% | You can find the cause and apply a minimal fix |
| Explanation | 10% | You can teach the idea clearly |

How the four activities map onto those weights:

| Activity | Primarily scores |
| --- | --- |
| Quiz | Theory 20% |
| Coding assignment | Coding 35% |
| Coding stretch items and design choices | Problem solving 20% |
| Debugging problem | Debugging 15% |
| Explanation exercise (spoken or written) | Explanation 10% |

Suggested coding rubric (35 points):

```text
Compiles / runs                 10
Correct behavior                15
Names and structure             10
```

Suggested debugging rubric (15 points):

```text
Finds the actual cause           8
Applies a minimal, correct fix   7
```

Suggested explanation rubric (10 points):

```text
Accurate                         6
Clear to a non-developer         4
```

Do not drop coding below 35%. An SDET who can only recite JVM facts is not done.

---

## Suggested Friday timebox

```text
Quiz                15 minutes     closed book, 8–10 questions
Coding              30 minutes     one focused task from the week
Debugging           15 minutes     a broken snippet you diagnose and fix
Explanation         10 minutes     interview-style, spoken or written
```

Total: about 70 minutes. Later weeks (API, Selenium, architecture) may need 90 minutes. Each weekly file lists its own timebox.

---

## Weekly folders

| Week | File | Curriculum coverage |
| --- | --- | --- |
| 1 | [week-01-computer-and-java-story.md](weekly/week-01-computer-and-java-story.md) | Parts 1–4: computer, programming, Java history, JVM, first program |
| 2 | [week-02-variables-to-methods.md](weekly/week-02-variables-to-methods.md) | Parts 5–10: variables, types, operators, decisions, loops, methods |
| 3 | [week-03-oop-and-collections.md](weekly/week-03-oop-and-collections.md) | Parts 12–16: OOP, packages, collections, wrappers, enums |
| 4 | [week-04-exceptions-modern-java.md](weekly/week-04-exceptions-modern-java.md) | Parts 17–26: exceptions, files, JSON, generics, lambdas, streams, Optional, date-time, regex, records |
| 5 | [week-05-jvm-git-clean-code.md](weekly/week-05-jvm-git-clean-code.md) | Parts 30–40: JVM memory, concurrency, debugging, Maven, Git, clean code, SOLID, patterns |
| 6 | [week-06-testing-api-selenium.md](weekly/week-06-testing-api-selenium.md) | Parts 41–53: JUnit, TestNG, HTTP, REST Assured, JDBC, Selenium, POM, data, DriverFactory |
| 7 | [week-07-framework-and-architecture.md](weekly/week-07-framework-and-architecture.md) | Parts 54–62: parallel, Grid, logging, reporting, CI/CD, review, enterprise architecture |

Arrays (Part 11) sit between Weeks 2 and 3. Treat them as required background for collections.

---

## Exams

Use these after the matching weeks, and again as the final battery. See [final-exam-guide.md](exams/final-exam-guide.md) for the full sequence and scoring.

| Exam | File |
| --- | --- |
| Java written (beginner through intermediate) | [java-written-exam.md](exams/java-written-exam.md) |
| Live coding (`statusMatches` and tests) | [live-coding-exam.md](exams/live-coding-exam.md) |
| Debugging | [debugging-exam.md](exams/debugging-exam.md) |
| OOP design (`User`, validation, `List<User>`) | [oop-design-exam.md](exams/oop-design-exam.md) |
| Advanced Java (generics, streams, records) | [advanced-java-exam.md](exams/advanced-java-exam.md) |
| API coding | [api-coding-exam.md](exams/api-coding-exam.md) |
| Automation challenge | [automation-challenge.md](exams/automation-challenge.md) |
| Architecture defense | [architecture-exam.md](exams/architecture-exam.md) |
| Final exam guide | [final-exam-guide.md](exams/final-exam-guide.md) |

---

## Interview practice

Each file has questions from this curriculum and **strong model answers** you can study. Speak the answer first, then check the model. Do not memorize word for word.

| Band | File |
| --- | --- |
| Beginner | [interview/beginner.md](interview/beginner.md) |
| Intermediate | [interview/intermediate.md](interview/intermediate.md) |
| Advanced Java | [interview/advanced-java.md](interview/advanced-java.md) |
| SDET | [interview/sdet.md](interview/sdet.md) |
| Architect | [interview/architect.md](interview/architect.md) |

The architect interview is the design prompt: a framework for 5,000 tests and 50 automation engineers.

---

## Instructor notes

- Give the quiz closed book. Coding and debugging may use the JDK, IntelliJ, and Javadoc, not the course chapters.
- Print expected vs actual once in failure messages. That habit starts in Week 2 and never stops.
- Secrets, empty `catch (Exception e)`, `Thread.sleep` as a default wait, and a singleton `WebDriver` are automatic deductions once those topics have been taught.
- Below 70% on coding: restudy. Do not average it away with quiz points.
