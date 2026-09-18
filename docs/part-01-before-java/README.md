# Part 1 — Before Java

This part is for students who may know **nothing** about computers as machines.

We will not install Java yet. We will not open IntelliJ yet. If you skip this part because you "just want code," you will treat Java like a magic spell book. Spells you do not understand will fail in production, and you will not know why.

---

## What This Part Is For

By the end of Part 1 you should be able to say:

> A computer follows exact instructions. Programming is writing those instructions. Languages exist so humans can write them. An algorithm is the plan we write before Java.

That paragraph is Level 0.

---

## Chapters

| Chapter | File | Question it answers |
|---------|------|---------------------|
| 1 | [chapter-01-what-is-a-computer.md](chapter-01-what-is-a-computer.md) | What does a computer actually *do*? |
| 2 | [chapter-02-what-is-programming.md](chapter-02-what-is-programming.md) | What is programming, really? |
| 3 | [chapter-03-why-programming-languages-exist.md](chapter-03-why-programming-languages-exist.md) | Why don't we talk to chips in English? |

---

## How to Study Part 1

- Read with a pen. Draw every diagram yourself.
- When a chapter shows Java, **look at it**. You are not expected to memorize syntax yet. You are expected to notice: "this file is a list of instructions the computer will follow."
- Do the exercises on paper first. Paper is allowed. Paper is encouraged.
- Keep using the course rule, even without a compiler:

```
problem → understand → algorithm → (Java later) → ...
```

For Part 1, stopping at **algorithm** is often the whole point.

---

## SDET Thread

Testers live in the gap between **what a human meant** and **what the machine did**.

```
  Human says: "Pay my bill"
                    |
                    v
  Software does: a long list of exact steps
                    |
                    v
  SDET asks: which step was wrong, missing, or surprising?
```

If you cannot see software as steps, you cannot test it well.

---

## When You Are Done

Complete the Level 0 checklist in `docs/00-student-levels.md`, then go to [Part 2 — History of Java](../part-02-history-of-java/README.md).
