# How to Use This Course

Welcome. If you have never written a computer program, never used IntelliJ, or never even thought about how a computer *thinks*, you are in the right place.

This course is written for a complete beginner who wants to become an **SDET** (Software Development Engineer in Test). An SDET is a person who writes programs that test other programs. That sounds advanced. It is. We will not start there. We will start with what a computer actually is.

Read this file before you open Chapter 1.

---

## Who This Course Is For

This course is for you if any of these are true:

- You have never programmed before.
- You have copied a few lines of code from the internet and did not really understand them.
- You work in manual testing and want to move into automation.
- You understand English well enough to follow a patient teacher, but technical words still feel like a foreign language.

You do **not** need:

- A computer science degree
- Math beyond everyday arithmetic
- Prior Java knowledge
- Prior testing-framework knowledge

You **do** need:

- Curiosity
- Patience with yourself
- A computer (Windows, macOS, or Linux)
- A willingness to type code with your own fingers, not only read it

---

## What You Will Become

By the end of the full journey, you will not just "know some Java." You will be able to:

1. Think in problems, not in random syntax.
2. Write Java that a teammate can read.
3. Debug failures without panic.
4. Design tests the way a professional SDET designs them.
5. Grow toward a **QA Automation Architect** — someone who designs how a whole team tests software.

That last title is Level 8. You are probably Level 0 today. That is expected. Every architect in this field started by not knowing what a variable was.

---

## The Most Important Course Rule

Memorize this path. We will use it in **every** chapter, every exercise, and every real job you do later.

```
problem
   → understand
      → algorithm
         → Java
            → code
               → run
                  → fail
                     → debug
                        → refactor
                           → test
                              → improve
                                 → architect
```

Here is what each step means in plain English.

| Step | Meaning |
|------|---------|
| **Problem** | What are we actually trying to do? State it in one sentence. |
| **Understand** | What is given? What is unknown? What would a correct result look like? |
| **Algorithm** | Write the steps in ordinary language, as if you were teaching a careful child. |
| **Java** | Choose the Java tools that match those steps (if, loop, method, class, and later: stream, test, page object). |
| **Code** | Type it. Do not paste blindly. Typing trains your hands and your eyes. |
| **Run** | Execute it. A program that is never run is a guess. |
| **Fail** | Expect failure. Beginners fail. Professionals fail too. Failure is data. |
| **Debug** | Read the error. Find the smallest cause. Fix one thing. Run again. |
| **Refactor** | After it works, make it clearer. Rename. Remove duplication. Do not "gold-plate." |
| **Test** | Prove it still works. Happy path, empty input, wrong input, boundary values. |
| **Improve** | Ask: is this reliable? Readable? Fast enough? Would a teammate understand it? |
| **Architect** | Ask the bigger question: how should this fit into a system of tests, not just one file? |

If you skip from "problem" straight to "code," you will write confused programs. If you stop at "it ran once," you will write fragile programs. The full path is the habit of a professional.

We will repeat a shorter version of this rule on the board of every class:

> **Do not start typing until you can explain the steps out loud.**

---

## How to Study Each Chapter

Every teaching chapter uses the same 15-section format. Treat it like a class period, not a blog post.

1. **Today's Goal** — Read this first. If you cannot say the goal in one sentence, you are not ready to code yet.
2. **Why It Matters** — This is the "why should I care?" section. SDET work is easier when you know why a concept exists.
3. **Real-Life Analogy** — We compare new ideas to restaurants, banks, shipping, and everyday objects. Analogies are bridges, not the destination.
4. **Illustrated Explanation** — ASCII diagrams. Trace them with your finger. Draw them on paper if that helps.
5. **Syntax / Concept** — The precise idea. Read slowly. You do not have to memorize it on sight.
6. **Simple Example** — The smallest working program that shows the idea.
7. **Real-World Example** — Banking, e-commerce, users, or orders. This is how the idea appears at work.
8. **SDET Example** — How a tester uses the same idea.
9. **Break the Code** — An intentional bug. Read it. Predict the failure *before* you look at the debug section.
10. **Debug** — How a professional would find and fix that bug.
11. **Student Exercise** — You must type this yourself.
12. **Challenge** — A stretch. Struggle is allowed. Looking up earlier chapters is allowed. Copying a finished answer from the internet is not.
13. **Knowledge Check** — Answer on paper or in a notes file. Then check the Answer Key at the end.
14. **Interview Question** — How this topic is asked in real interviews. Practice answering out loud.
15. **Homework** — Do this before the next chapter. Homework is not optional if you want the skill.

### A good study session

```
+------------------+     +------------------+     +------------------+
|  1. Read goal    | --> |  2. Read analogy | --> |  3. Trace diagram|
|     and "why"    |     |     slowly       |     |     on paper     |
+------------------+     +------------------+     +------------------+
         |                                                 |
         v                                                 v
+------------------+     +------------------+     +------------------+
|  4. Type the     | --> |  5. Break and    | --> |  6. Do exercise, |
|     simple code  |     |     debug        |     |     challenge,   |
|                  |     |                  |     |     homework     |
+------------------+     +------------------+     +------------------+
```

**Time guide:** a chapter is usually 45–90 minutes the first time, plus homework. If a chapter takes three hours because you are typing, running, and thinking, that is not failure. That is learning.

---

## The Software You Need

You need two main tools. We will install them properly in Part 4. You can still read Parts 1–3 before the install if you want the ideas first.

### 1. JDK 25 LTS (Java Development Kit)

- **JDK** means the toolkit that lets you *write* and *run* Java.
- **25** is the version number.
- **LTS** means Long-Term Support. Companies prefer LTS versions because they stay supported for years.

This course's recommended baseline is **JDK 25 LTS**.

If a workplace still uses 17 or 21, the core ideas still apply. We will mark the few modern features that are newer.

### 2. IntelliJ IDEA (the Community edition is enough)

An **IDE** is an Integrated Development Environment: a smart editor that understands Java. IntelliJ will:

- Color your code
- Warn you about many mistakes
- Run your program with a button
- Later: run tests, debug line by line, and navigate a large project

You do **not** need paid IntelliJ Ultimate for this course.

### What you do not need on day one

- Maven or Gradle (we start with IntelliJ's built-in Java project, then add Maven later)
- Selenium, Rest Assured, or TestNG (those come after Java foundations)
- A second monitor, a special keyboard, or a powerful gaming PC
- Linux command-line mastery

A normal laptop with internet is enough.

---

## How to Run Code (preview)

You will learn this with screenshots-in-words in Part 4. Here is the map so the words do not surprise you later.

### Inside IntelliJ (what we use daily)

1. Open the `java-learning` project.
2. Open a `.java` file.
3. Click the green triangle next to `main`, or right-click the file → **Run**.
4. Read the output in the **Run** tool window at the bottom.

### On the command line (what you should see at least once)

```text
javac Hello.java     →  creates Hello.class (bytecode)
java  Hello          →  the JVM runs that bytecode
```

```
Hello.java  --javac-->  Hello.class  --java-->  output on screen
 (you type)            (computer's              (you read)
                        language)
```

If `java -version` and `javac -version` both print 25, your machine is ready.

---

## Class Format (how to treat this like a real class)

Think of each part as a **module** and each chapter as a **lesson**.

| Piece | What it is | Your job |
|-------|------------|----------|
| Part README | The map of a module | Read it so you know where you are going |
| Chapter | One lesson | Complete all 15 sections |
| Knowledge Check | Mini quiz | Write answers first, then open the Answer Key |
| Homework | Practice reps | Do it the same day if possible |
| Student Levels | Your belt system | Do not skip levels because a title sounds impressive |

### Suggested weekly rhythm

- **3 chapters per week** if you have a full-time job.
- **5 chapters per week** if you can study 2 hours a day.
- **Never binge 8 chapters in one night.** Your brain needs sleep to keep syntax.

### Notes you should keep

Create a simple notebook (paper or a `notes/` folder) with three headings per chapter:

1. **Words I did not know**
2. **Bugs I hit**
3. **One sentence I could say in an interview**

That notebook will become your personal interview guide.

---

## Rules That Protect Beginners

1. **Type the examples.** Copy-paste teaches your clipboard, not your brain.
2. **Read errors from the top.** The first red line is usually the real clue. The rest is often a chain reaction.
3. **Change one thing at a time** when debugging.
4. **Do not memorize every Java release.** We teach the story of Java so you have context, not so you can recite version trivia.
5. **Do not skip "Break the Code."** Professionals are not people who never break things. They are people who can recover.
6. **Ask "what would a test do?"** even in early chapters. That is the SDET muscle.
7. **Honesty over speed.** If you do not understand a diagram, redraw it. Do not nod and scroll.

---

## How This Repository Is Organized

```
coreJavaTrainingCourse/
|
+-- docs/
|   +-- 00-how-to-use-this-course.md    ← you are here
|   +-- 00-learning-journey.md          ← the full map
|   +-- 00-student-levels.md            ← how to know you are ready
|   +-- part-01-before-java/
|   +-- part-02-history-of-java/
|   +-- part-03-how-java-works/
|   +-- part-04-intellij-setup/
|   +-- (later parts: syntax, OOP, testing, automation...)
|
+-- java-learning/                      ← your IntelliJ project (you create this)
```

Early chapters talk about Java before you install it. That is on purpose. A cook who understands "heat, time, and ingredients" before touching a stove makes fewer disasters.

---

## What "Done" Looks Like for This File

You are ready for the learning journey document when you can say:

> "I will not jump to code. I will go problem → understand → algorithm → Java → code → run → fail → debug → refactor → test → improve → architect. I will use JDK 25 LTS and IntelliJ. I will type examples myself."

If that sentence feels true, open `docs/00-learning-journey.md` next.
