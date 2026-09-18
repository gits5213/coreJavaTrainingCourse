# Student Levels (0–8)

This file is your promotion guide. A **level** is not a vanity badge. It is a description of what you can *do when nobody is watching*.

Read it in three ways:

1. **Before a part** — so you know the destination.
2. **During struggle** — so you remember that confusion is a level, not a personality.
3. **Before skipping ahead** — so you do not steal future-you's foundation.

---

## How Promotion Works

You are ready to advance when **all** of these are true:

- You can explain the level's ideas in complete sentences, out loud, to a friend who is not a programmer.
- You can complete the level's "ready to advance" checklist without looking up every answer.
- You have done the homework, not only read it.
- You can debug a small broken example from that level.

If you can *recognize* a term but cannot *use* it, you are still on the current level. Recognition is the lobby. Skill is the building.

```
  "I have heard of it"     "I can explain it"     "I can build it"
         |                        |                      |
         v                        v                      v
    not ready              almost ready              ready
```

---

## Level 0 — Computer Newcomer

**One-line identity:** "I use computers. I am starting to see them as machines that follow exact instructions."

### What you understand

- A computer takes **input**, **processes** it, **stores** some of it, and produces **output**.
- **Hardware** is the physical machine. **Software** is the set of instructions.
- Humans think in goals ("log me in"). Computers need steps ("read email, compare password, then show home page or error").
- An **algorithm** is those steps, written clearly, before any Java exists.
- Programming languages exist because people cannot comfortably write billions of on/off signals by hand.

### What you can do

- Point at a laptop and name input, process, store, and output for a simple action (typing a search, paying a bill).
- Write a login algorithm in numbered steps, including the failure path.
- Explain why "the computer should know what I meant" is not how computers work.

### How you know you are ready for Level 1

- [ ] You can draw the input → process → store → output diagram from memory.
- [ ] You can tell hardware from software with two examples of each.
- [ ] You can write an algorithm for "user logs into a bank app" with at least one error case.
- [ ] You can explain, without jargon, why we do not program modern apps in raw machine code.

**If you fail the checklist:** stay in Part 1. Re-read Chapters 1–3 and redo the homework on paper.

---

## Level 1 — Java Workshop Apprentice

**One-line identity:** "I know where Java came from, how a program actually runs, and I can use IntelliJ to run my own file."

### What you understand

- Java began as **Oak**, created by **James Gosling** at **Sun Microsystems**, announced around **1995**, with the promise **Write Once, Run Anywhere**.
- "Java" is several things at once: a **language**, a **specification**, a **trademark**, and implementations (**OpenJDK**, **Oracle JDK**) shipped as a **JDK**.
- You write `.java` **source**. `javac` compiles it to `.class` **bytecode**. The **JVM** runs bytecode on Windows, macOS, or Linux.
- **JDK** includes the compiler, the `java` launcher, libraries, debugger, and a runtime. **JRE** was the run-only kit. **JVM** is the engine inside.
- A first program is a class with a `main` method. You do not need to worship the keywords. You need to know what job each piece is doing.
- Comments are notes for humans. Good comments explain *why*, not what the next line already says.

### What you can do

- Install **JDK 25 LTS** and confirm with `java -version` and `javac -version`.
- Create the **java-learning** project in IntelliJ (IntelliJ build system first; Maven later).
- Write, run, and change a Hello World program.
- Draw: `.java` → compiler → `.class` → class loader → JVM → output.

### How you know you are ready for Level 2

- [ ] You can say what WORA means and why bytecode is the trick.
- [ ] You can distinguish Java the language from the JDK you installed.
- [ ] You can explain JDK vs JVM in under a minute.
- [ ] You have a `java-learning` project that runs a program you typed.
- [ ] You can add a useful comment and remove a useless one.

**If you fail the checklist:** repeat Part 3 diagrams on paper and reinstall/recreate the project from Part 4. Do not memorize version trivia as a substitute.

---

## Level 2 — Java Speaker

**One-line identity:** "I can make the computer store data, decide, and repeat, using Java I typed myself."

### What you understand

- A **variable** is a named box. A **type** is the kind of thing allowed in that box.
- Programs **branch** (`if`) and **repeat** (loops) because real life is full of conditions and lists.
- A **method** is a named recipe you can call more than once.
- Errors are messages. The first line is a clue, not an insult.

### What you can do

- Turn a word problem into variables and control flow.
- Trace a loop on paper (what is `i` on each round?).
- Write a method that returns a result, not only prints.
- Predict output before running.

### How you know you are ready for Level 3

- [ ] You can declare, assign, and print variables of several types without guessing randomly.
- [ ] You can write `if` / `else` for a pass/fail or valid/invalid decision.
- [ ] You can loop over a list of numbers or names and compute something.
- [ ] You can explain a stack trace's first relevant line.
- [ ] You completed exercises without pasting a whole solution from the internet.

**SDET hint at this level:** a test is a decision: "is the actual result equal to the expected result?"

---

## Level 3 — Object Builder

**One-line identity:** "I model real things as classes, and I protect their data."

### What you understand

- A **class** is a blueprint. An **object** is one thing built from that blueprint.
- **Fields** hold state. **Methods** hold behavior.
- **Encapsulation** means other code should not poke around inside an object's pockets.
- Inheritance and interfaces are tools for sharing behavior — easy to misuse, powerful when the relationship is real.

### What you can do

- Design `User`, `BankAccount`, or `Order` with private fields and clear methods.
- Create two objects of the same class and show they do not share all data.
- Explain why `LoginPage` in a test framework is a class.

### How you know you are ready for Level 4

- [ ] You can draw class vs object with an example that is not `Car` copied from a meme.
- [ ] You can write a constructor and use `this` with understanding.
- [ ] You can name one good reason for `private` fields.
- [ ] You can read a small class you did not write and say what it is for.

---

## Level 4 — Toolkit Tester-Coder

**One-line identity:** "I can hold many values, survive failures, and read files — the daily Java of automation."

### What you understand

- Real programs work with **collections** of things, not one variable at a time.
- Things go wrong. **Exceptions** are how Java shouts "this path failed."
- Tests and apps both need **configuration** and sometimes files.
- Modern Java (lambdas, streams, Optional) is useful when it makes intent clearer, not when it shows off.

### What you can do

- Store users or orders in a `List` or `Map` and find one of them.
- Catch a specific exception and fail with a clear message.
- Write a reusable helper method in a sensible package.

### How you know you are ready for Level 5

- [ ] You can choose `List` vs `Map` for a simple job and say why.
- [ ] You can follow a `try` / `catch` / `finally` (or try-with-resources) story.
- [ ] You do not ignore exceptions empty-handed (`catch (Exception e) {}`).
- [ ] You can explain Optional as "a value that might be missing," not as a magic spice.

---

## Level 5 — Test Designer

**One-line identity:** "I write automated checks that prove a behavior, with names a teammate can trust."

### What you understand

- A test has **arrange**, **act**, **assert**.
- A green test is evidence. A red test is a signal. A flaky test is a liar.
- Assertions must check the thing you actually care about.
- Test code is production-adjacent: it must be readable.

### What you can do

- Write independent JUnit tests (or the course runner) for a Java method.
- Name tests so they read like specifications.
- Choose a few strong cases (happy, empty, invalid, boundary) instead of 40 copies of the happy path.

### How you know you are ready for Level 6

- [ ] You can explain arrange-act-assert with a banking or login example.
- [ ] Your tests fail for the right reason when you break the code on purpose.
- [ ] You can describe a bad test (too broad, no assert, depends on previous test).
- [ ] You treat test failures as information, not as "the tool is broken" by default.

---

## Level 6 — Automation Engineer

**One-line identity:** "I can drive a browser and an API, and I know which tool belongs to which risk."

### What you understand

- UI tests see what a user sees, and they are slower and more fragile.
- API tests see the business rules more directly, and they are usually cheaper.
- Locators and waits are design decisions. Hard-coded sleeps are debt.
- Page objects and API clients exist to protect tests from UI/HTTP churn.

### What you can do

- Automate a login (or equivalent) without a sleep-based "fix."
- Send an HTTP request, read status and body, assert something meaningful.
- Explain a flake you caused and how you removed it.

### How you know you are ready for Level 7

- [ ] You have at least one stable UI flow and one stable API flow in your own project.
- [ ] You can defend why a check belongs at API vs UI.
- [ ] You can debug a failed locator or a 400/401/500 without random re-runs as your only strategy.

---

## Level 7 — Framework and Pipeline Owner

**One-line identity:** "Other people can run my tests tomorrow morning without calling me."

### What you understand

- A framework is a **consistent way** to write tests, not a folder of copy-paste.
- Environments need configuration, not edited source files.
- CI is how tests become a team habit.
- Reporting is how failures become conversations.

### What you can do

- Structure tests / application drivers / data / utilities in layers.
- Run the suite in CI on every push or on a schedule.
- Document the one command that runs the smoke tests.

### How you know you are ready for Level 8

- [ ] A new teammate can run smoke tests from README instructions.
- [ ] You can explain your layers on a whiteboard.
- [ ] You can name trade-offs (speed vs coverage, UI vs API, retry vs fix).
- [ ] You review tests as carefully as application code.

---

## Level 8 — QA Automation Architect

**One-line identity:** "I design the quality system: people, risk, tools, and feedback loops."

### What you understand

- Architecture is **judgment under constraints** (time, risk, team skill, product shape).
- Not everything should be automated. Not everything should be a UI test.
- Your job includes mentoring, standards, and saying no to fashionable waste.
- You still personally debug. Titles do not exempt you from stack traces.

### What you can do

- Write a test strategy for a banking or e-commerce product.
- Choose a stack and explain why it fits *this* team.
- Coach a Level 2 student through the course rule instead of grabbing the keyboard.
- Quantify cost: pipeline minutes, flake rate, escaped defects.

### How you know you are operating at Level 8 (there is no "finished")

- [ ] You can walk executives and juniors through the same strategy in two different vocabularies.
- [ ] You improve the system when tests rot, instead of adding more rot.
- [ ] You still start with **problem → understand → algorithm** before tools.

Level 8 is a practice, not a certificate. Return to earlier levels whenever a foundation wobbles. That is professionalism.

---

## Quick Map: Level to Current Docs

| Level | Focus | Start here |
|-------|--------|------------|
| 0 | Computer + programming thinking | `docs/part-01-before-java/` |
| 1 | Java history, JVM, IntelliJ | `docs/part-02-history-of-java/` then Parts 3–4 |
| 2–8 | Later parts of this course | See `docs/00-learning-journey.md` |

If you are new, you are Level 0. Open Part 1 and begin Chapter 1.
