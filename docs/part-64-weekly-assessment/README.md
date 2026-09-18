# Part 64 — Weekly Assessment

Every week is not only "more chapters." It is a **check** that the student can use the week's ideas without the book open.

## Goal

You will run (or sit) a weekly assessment with four parts — quiz, coding, debugging, explanation — and you will understand the scoring weights.

## Why It Matters

People can nod through 15 sections and still not write a method. Weekly pressure is how you find that out while there is time to restudy, not at the final exam.

SDET jobs are mixed: theory, code, debug, talk. The weights match the job more than a 100% multiple-choice quiz.

## Real-Life Analogy

A driving instructor does not only ask "what is a steering wheel?" They watch you drive, stall, recover, and explain a roundabout.

## What Every Week Contains

```text
Quiz
+
Coding Assignment
+
Debugging Problem
+
Explanation Exercise
```

## Recommended Score

```text
Theory          20%

Coding          35%

Problem Solving 20%

Debugging       15%

Explanation     10%
```

How the four activities map:

| Activity | Primarily scores |
| --- | --- |
| Quiz | Theory 20% |
| Coding assignment | Coding 35% |
| Coding + quiz stretch items | Problem solving 20% |
| Debugging problem | Debugging 15% |
| Explanation exercise (spoken or written) | Explanation 10% |

If you must combine, do not drop coding below 35%. An SDET who can only recite JVM facts is not done.

## Illustrated Explanation

```text
Monday–Thursday     chapters + homework
Friday              assessment (60–90 min)

Quiz 15 min         closed book, 8–10 questions
Coding 30 min       one focused task from the week
Debug 15 min        a broken class you provide
Explain 10 min      interview question out loud
```

Pass guideline: **70% weighted**. Below 70% on coding: do not "make it up" with quiz points. Restudy.

## Example Week (Methods)

**Quiz (theory):** What is `void`? Can you nest methods? What is a call vs definition?

**Coding:** Write `statusMatches(int expected, int actual)` and a `main` that prints PASS/FAIL.

**Problem solving:** Given expected 201 and actual 200, design the message (expected/actual printed once).

**Debugging:** Method declared inside `main`. Student must move it and run.

**Explanation:** "Why do SDET frameworks use methods?" Spoken, 60–90 seconds.

## Example Week (Selenium waits)

**Quiz:** Why is sleep bad? Name two ExpectedConditions.

**Coding:** Page method that waits until clickable then clicks.

**Problem solving:** Spinner covers the button — what state do you wait for?

**Debugging:** Test with `Thread.sleep(5000)` and a wrong locator mixed — which do you fix first? (Locator in DevTools, then wait.)

**Explanation:** State-based synchronization in one minute.

## SDET Example Rubric Snippet

```text
Coding 35
  compiles              10
  correct behavior      15
  names / structure     10

Debug 15
  finds cause           8
  minimal fix           7

Explain 10
  accurate              6
  clear to a non-dev    4
```

## Student Exercise

Build a 10-question quiz for the part you just finished. Sit it tomorrow without notes. Score honestly.

## Challenge

Record a 90-second explanation of ThreadLocal *or* Maven lifecycle. Listen back. If you say "um, the thing," rewrite a skeleton and retry.

## Knowledge Check

1. What four activities happen every week?
2. Theory weight?
3. Coding weight?
4. Problem solving weight?
5. Debugging weight?
6. Explanation weight?
7. Why is coding the largest?
8. Suggested pass bar?
9. Can quiz points replace missing code?
10. Why spoken explanation?

## Interview Question

**Question:** How do you know you learned a topic?

A strong answer: I can quiz myself, write code, debug a broken sample, and explain it out loud. That is how our course assesses weekly: theory 20, coding 35, problem solving 20, debugging 15, explanation 10.

## Homework

Schedule a weekly slot. Put it on a calendar. Assessments that never happen are not assessments.

---

## Answer Key

1. Quiz, coding assignment, debugging problem, explanation exercise
2. 20%
3. 35%
4. 20%
5. 15%
6. 10%
7. SDET work is writing and changing code
8. 70% weighted, coding cannot be skipped
9. No
10. Jobs interview by talking
