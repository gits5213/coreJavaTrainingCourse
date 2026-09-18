# Part 63 — Daily Class Format

Every teaching chapter in this course follows the same 15-step class. Teachers use it live. Students use it when studying alone. Do not skip Break the Code because it feels negative. Professionals recover.

## Goal

You will run a 45–90 minute session using all 15 steps, and you will know what the student is supposed to *do* in each step.

## Why It Matters

Random tutorials have no debug step and no interview sentence. This format is how a beginner becomes someone who can explain, break, and fix.

## Real-Life Analogy

A cooking class: taste the goal, why this dish, analogy to something you know, diagram of the stove, the knife technique, a tiny practice, a restaurant version, a QA version (plating), a burned pan on purpose, how we saved it, you cook, a harder dish, quiz, "tell the examiner," leftovers as homework.

## The 15 Steps

### 1. Today's Goal

One sentence. Example from the curriculum:

> Today we will understand Java variables.

If the student cannot repeat the goal, stop. Do not open IntelliJ yet.

### 2. Why It Matters

The real problem. Variables matter because tests store expected vs actual. Waits matter because CI is slower than your laptop.

### 3. Real-Life Analogy

Bridge, not destination.

Example:

```text
Variable
=
Labeled storage box
```

Say it, then return to Java. Do not spend the whole hour on boxes.

### 4. Illustrated Explanation

ASCII diagrams. Trace with a finger. Draw on a whiteboard.

### 5. Syntax

Minimum syntax. Not the entire language spec.

### 6. Simple Example

Build together. Student types. Teacher does not paste a wall.

### 7. Real-World Example

Use:

* banking
* e-commerce
* QA
* APIs
* users
* orders

Pick one domain and stay there for the hour if possible.

### 8. SDET Example

How a QA / SDET uses the same idea. Even in Chapter 1: "if this were a bank app, what could go wrong?"

### 9. Break the Code

Intentional bug. Students **predict** the failure before the debug section.

### 10. Debug

Find it with IntelliJ: first error line, debugger step-into. Change one thing.

### 11. Student Exercise

Independent. Struggle is allowed. Copying a finished internet solution is not.

### 12. Challenge

Harder variation. Optional in time-boxed class; not optional for ambitious students.

### 13. Knowledge Check

5–10 questions. Paper or notes. Then answer key.

### 14. Interview Question

Student explains **verbally**. Out loud. Mumblers fail interviews that readers pass.

### 15. Homework

Small practical assignment. Same day if possible. Homework is reps, not punishment.

## Suggested Timing (60 minutes)

```text
Goal + Why + Analogy     8 min
Diagram + Syntax         10 min
Simple + Real + SDET     15 min
Break + Debug            10 min
Exercise (start)         10 min
Knowledge + Interview    7 min
Homework assigned        0 min (at the end)
```

Challenge spills into homework. That is fine.

## Diagram of a session

```text
Goal → Why → Analogy → Picture → Syntax
         → Type a tiny program
         → See it at work and in tests
         → Break it → Fix it
         → You try → Stretch
         → Quiz → Say it aloud → Practice at home
```

## SDET Example of using the format

Teaching waits: goal is "replace sleep with WebDriverWait." Why: flakes and slow CI. Analogy: bus. Diagram: sleep vs until visible. Syntax: `new WebDriverWait`. Simple: wait for h1. Real: bank confirmation. SDET: page object wait. Break: sleep 5. Debug: timeout vs locator. Exercise: delete sleeps. Challenge: Waiter helper. Quiz. Interview. Homework: commit.

## Student Exercise

Take Chapter 17 (variables) or any numbered chapter. Teach a rubber duck all 15 headings in 5 minutes. Skip coding; hit the *purpose* of each section.

## Challenge

Write a 15-section outline for a topic not yet a chapter (e.g. cookies). Do not implement a full chapter unless assigned.

## Knowledge Check

1. How many steps?
2. What is step 9?
3. Why interview out loud?
4. Name four real-world domains the curriculum lists.
5. Should students predict Break the Code?
6. Is homework optional for skill?
7. Where is the answer key?
8. What is the analogy rule?
9. Typical chapter duration?
10. What is the course rule before typing?

## Interview Question

**Question:** How do you learn a new tool?

A strong answer should sound like this format: goal, why, analogy, picture, smallest syntax, example, how testers use it, a failure, a fix, practice, explain aloud.

## Homework

For your next chapter, keep notes with three headings: words I did not know, bugs I hit, one interview sentence. That is Part 63 in miniature.

---

## Answer Key

1. 15
2. Break the Code
3. Interviews are spoken
4. banking, e-commerce, QA, APIs, users, orders
5. Yes
6. No if you want the skill
7. End of numbered chapters
8. Bridge, not destination
9. 45–90 minutes plus homework
10. Do not start typing until you can explain the steps out loud
