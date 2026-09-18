# Your Learning Journey

This is the student map of the whole course: from **Computer Basics** to **QA Automation Architect**.

You do not need to understand every box today. You only need to know that there is a path, that the path is ordered, and that skipping boxes is how people become "tutorial-confident" and job-terrified.

---

## The Big Picture

```
 Level 0          Level 1           Level 2            Level 3
 Computer         How Java          Java               Object-Oriented
 Basics           Works + Tools     Language           Java
     |                |                 |                  |
     v                v                 v                  v
 +--------+      +----------+      +---------+       +-----------+
 | Input  |      | History  |      | Types   |       | Class     |
 | Process| ---> | Source   | ---> | Control | ----> | Object    |
 | Store  |      | Compile  |      | Methods |       | Inherit   |
 | Output |      | JVM/JDK  |      | Arrays  |       | Interface |
 |        |      | IntelliJ |      | Strings |       |           |
 +--------+      +----------+      +---------+       +-----------+
                                                       |
                       +-------------------------------+
                       |
                       v
 Level 4          Level 5           Level 6            Level 7         Level 8
 Intermediate     Test              UI + API           Frameworks      Architect
 Java for QA      Foundations       Automation         + CI/CD
     |                |                 |                  |               |
     v                v                 v                  v               v
 +----------+    +----------+      +----------+      +-----------+   +-----------+
 | Collect. |    | What is  |      | Selenium |      | Design    |   | Strategy  |
 | Except.  |    | a test?  |      | locators |      | patterns  |   | Mentoring |
 | Files    | -> | JUnit    | ---> | API      | ---> | reporting | ->| Scale     |
 | Dates    |    | Asserts  |      | Rest     |      | CI        |   | Quality   |
 | Streams  |    | Data     |      | waits    |      | Docker    |   | culture   |
 +----------+    +----------+      +----------+      +-----------+   +-----------+
```

Read it left to right, top then bottom. Each level **unlocks** the next. An architect who cannot debug a `NullPointerException` is not an architect. They are a slide deck.

---

## How to Use This Map

1. Find your current level in `docs/00-student-levels.md`.
2. Complete the parts listed for that level.
3. Use the "ready to advance" checklist. If you fail two items, stay.
4. Only then move forward.

If you are a complete beginner, you are **Level 0**. Start at Part 1. That is not an insult. That is honesty, and honesty is faster.

---

## Level 0 — Computer Basics

**Who you are:** a person who uses computers but has not yet looked *inside* the idea of a computer.

**What you learn:**

- A computer is a machine that follows **input → process → store → output**.
- Hardware is the body. Software is the instructions.
- Programming is writing those instructions with painful precision.
- Programming languages exist because humans cannot comfortably speak in raw electrical on/off signals.

**Course parts:** Part 1 (Before Java)

**You leave this level when:** you can explain a login in algorithm steps, and you can say why Java is a language instead of "magic."

---

## Level 1 — How Java Works and How You Work

**Who you are:** someone who understands that programs are instructions, and now needs a real language and a real workshop.

**What you learn:**

- Where Java came from (Oak, James Gosling, Sun, 1995, Write Once Run Anywhere).
- What "Java" actually means: language, specification, trademark, JDK, OpenJDK, Oracle JDK.
- How source code becomes bytecode and how the JVM runs it.
- The difference between JDK, JRE, and JVM.
- How to install JDK 25 LTS and create the `java-learning` project in IntelliJ.
- How to write, run, comment, and read a first program *without pretending you memorized it*.

**Course parts:** Part 2, Part 3, Part 4

**You leave this level when:** you can create a class, run it in IntelliJ, explain `.java` vs `.class`, and describe JDK vs JRE vs JVM in one minute.

---

## Level 2 — The Java Language (the sentences)

**Who you are:** a student who can run Hello World and is ready to make the computer *decide* and *repeat*.

**What you learn (later parts of this course):**

- Variables and types (`int`, `boolean`, `String`, and why types exist)
- Operators and expressions
- `if` / `else`, `switch`
- Loops (`for`, `while`)
- Methods (reusable recipes)
- Arrays and `String` as daily tools
- Reading errors as a skill, not as shame

**SDET thread:** every test is "given this input, expect this output." That is already programming.

**You leave this level when:** you can write a small program from a word problem without copying, and you can trace a loop on paper.

---

## Level 3 — Object-Oriented Java (the blueprints)

**Who you are:** a student who can write procedures, and now must model *things* (users, accounts, orders, pages).

**What you learn:**

- Class vs object
- Fields and methods
- Encapsulation (protecting data)
- Inheritance and interfaces (sharing behavior carefully)
- Constructors
- Why SDET frameworks are full of classes named `LoginPage` and `UserClient`

**You leave this level when:** you can design a small `BankAccount` or `Order` class and explain *why* the data is private.

---

## Level 4 — Intermediate Java for Testers

**Who you are:** a student who can model objects, and now needs the toolbox used in real automation code.

**What you learn:**

- Collections (`List`, `Set`, `Map`)
- Exceptions (`try` / `catch` / `throw`) — tests fail; systems throw
- Files and configuration
- Dates and time
- Optional, streams, lambdas (practical use, not fashion)
- Packages, access, and clean project structure

**SDET thread:** test data is lists and maps. Failed locators throw exceptions. Reports are files.

**You leave this level when:** you can parse a list of users, handle a missing value without crashing, and write a method another class can reuse.

---

## Level 5 — Test Foundations (thinking like SDET)

**Who you are:** a Java student who must now become a *quality* engineer, not only a coder.

**What you learn:**

- What a test really is (arrange, act, assert)
- Test cases vs test scripts
- Assertions
- JUnit (or the course's chosen runner)
- Test data and parameterization
- Bug reports that developers can actually use
- The difference between a test that *clicks* and a test that *proves*

**You leave this level when:** you can write independent tests with clear names, and you can explain why a flaky test is a liability.

---

## Level 6 — UI and API Automation

**Who you are:** someone who can write tests in Java, and now drives real software: browsers and HTTP APIs.

**What you learn:**

- How a web page is a tree of elements
- Locators, waits, and why "just sleep 5 seconds" is a future outage
- Page objects as a design, not a religion
- HTTP: request, response, status, body
- API automation (Rest Assured or equivalent)
- UI vs API: when each is the right tool

**You leave this level when:** you can automate a login (UI) and a create-user call (API), and you know which one should be the nightly safety net.

---

## Level 7 — Frameworks, CI/CD, and Professional Delivery

**Who you are:** an SDET who can write tests, and now must make *other people* able to run them every day.

**What you learn:**

- Layered framework design (tests / pages or clients / data / reporting)
- Configuration per environment (dev, qa, staging)
- Reporting and logs
- Running tests in CI (GitHub Actions or similar)
- Parallelism, retries (used carefully), tagging
- Containers at a practical level
- Code review habits

**You leave this level when:** a teammate can clone the repo, run tests with one documented command, and trust the report.

---

## Level 8 — QA Automation Architect

**Who you are:** a senior engineer who designs the *system of quality*, not only files of tests.

**What you learn / practice:**

- Test strategy: what to automate, what not to automate, and why
- Risk-based coverage
- Mentoring juniors in the course rule (problem → … → architect)
- Tooling choices with reasons, not hype
- Observability: tests as production-adjacent signals
- Cost of flaky tests, cost of slow pipelines, cost of missing bugs
- Talking to developers, product, and leadership without melting into jargon

**You leave this level when:** you can defend a testing architecture on a whiteboard, and you still write a failing test when that is the honest next step.

---

## Journey Timeline (honest, not marketing)

```
Month 0-1     Level 0-1    Computers, Java story, JVM, IntelliJ, first programs
Month 1-3     Level 2-3    Language + OOP  (this is the long climb)
Month 3-5     Level 4-5    Collections, exceptions, real tests
Month 5-8     Level 6      UI + API automation that survives change
Month 8-12    Level 7      Framework + CI that a team can own
Year 2+       Level 8      Architecture: judgment, not just syntax
```

These numbers assume consistent study. A busy full-time job stretches them. That is normal.

---

## The SDET Thread That Runs Through Every Level

Even in Chapter 1, we will ask:

> If this were a bank app, what could go wrong, and how would we notice?

```
  Feature in production
           |
           v
  +------------------+
  | Developer code   |
  +------------------+
           |
           v
  +------------------+     +------------------+
  | SDET tests       | --> | Fail = signal    |
  | (your future job)|     | Pass = evidence  |
  +------------------+     +------------------+
```

You are not learning Java "for fun trivia." You are learning Java so that **evidence** can replace **hope**.

---

## What to Open Next

- If you want the belt definitions and promotion checklists: `docs/00-student-levels.md`
- If you are Level 0: `docs/part-01-before-java/README.md`
