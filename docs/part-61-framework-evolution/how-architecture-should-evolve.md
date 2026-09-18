# How Architecture Should Evolve

## Goal

By the end of this lesson, you will recite the evolution ladder, name the **pain** that justifies each step, and refuse to start a two-test project with Grid, ThreadLocal, Allure, Kafka, and a custom annotation processor.

## Why It Matters

Copied "ultimate frameworks" from YouTube are unmaintainable by the copier. Seniors delete them. Juniors who grow architecture from pain can explain every folder in an interview. That is the architect skill.

## Real-Life Analogy

A house.

```text
Tent                 one class
Cabin                a few classes
Plumbing when you leak
Rooms when you have family
City permits         governance
```

You do not pour a 20-story foundation for a weekend camping trip. You also do not stay in a tent when 50 people move in.

## Illustrated Explanation

Start:

```text
One Java Class
```

then:

```text
Multiple Classes
```

then experience duplication.

Then introduce:

```text
Methods
```

Then:

```text
OOP
```

Then:

```text
Packages
```

Then:

```text
Page Objects
```

Then:

```text
Test Data Models
```

Then:

```text
Factories
```

Then:

```text
Configuration
```

Then:

```text
API Clients
```

Then:

```text
Parallel Execution
```

Then:

```text
CI/CD
```

Then:

```text
Governance
```

The student must understand:

> Architecture solves problems. Architecture should not be added just to look advanced.

## Each Rung: Pain → Move

| You have | Pain | Add |
| --- | --- | --- |
| One class | File is 400 lines of mixed jobs | Multiple classes |
| Multiple classes | Same login typed everywhere | Methods (and later objects) |
| Procedures | Cannot model User, Page | OOP |
| Default package | Name clashes, no navigation | Packages |
| Raw Selenium in tests | Locator changes break 40 tests | Page objects |
| `login("a","b")` soup | Swapped arguments, no names | Models |
| `new ChromeDriver()` / user strings copied | Options and users drift | Factories |
| URLs in source | Cannot switch QA/staging | Configuration |
| UI-only, slow for business rules | Need cheaper checks | API clients |
| Suite takes 4 hours | Idle machines | Parallel + ThreadLocal |
| "Works on my machine" | Rotten main | CI/CD |
| 50 engineers, chaos | Ownership, standards | Governance (CODEOWNERS, review, lint) |

Skipping rungs: possible if you **already have the pain**. Not because a blog used all the folders.

## Simple Example — Honest Day 1

```java
public class OpenSite {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("https://example.com");
        } finally {
            driver.quit();
        }
    }
}
```

This is allowed. Do not apologize. When you have three tests, extract. When you have two browsers, factory. When CI exists, config + headless.

## Real-World Example

A bank hires 2 SDETs. They have 30 tests. They need: Maven, Git, pages, waits, JUnit/TestNG, CI smoke. They do **not** need a custom DSL and Grid on week 1.

A year later: 5000 tests, 50 people. Now Part 60 + Grid + parallel + governance is the problem they have.

## SDET Example Anti-pattern

```text
Day 1
  AbstractBaseTest
  Grid
  Parallel
  Kafka listener
  Excel engine
  Custom @Step processor
  0 passing tests
```

That student cannot debug Hello World. The architecture was a costume.

## Break the Code

Adding `ThreadLocal` before a second thread exists: complexity without benefit (unless you are practicing in a kata — say so).

Adding API clients when the product has no API: theater.

Adding governance documents nobody reads: theater. Governance is CODEOWNERS + review that actually blocks + CI that actually fails.

## Debug

If the team spends more time on the framework than on product risk, you overshot. Delete a layer. If every test still copy-pastes login, you undershot. Extract.

Measure: time to add a new test, flake rate, PR duration. Architecture should improve those numbers.

## Student Exercise

Write your current project on the ladder. Circle the next *one* step. Write the pain sentence that justifies it. If you cannot write the pain, you are not ready for that step.

## Challenge

Take the Part 60 tree. Mark each folder NOW / NEXT / LATER / NEVER for your training project. NEVER might include Kafka. Defend NEVER.

## Knowledge Check

1. What must you not do on Day 1?
2. Recite the ladder from one class to governance.
3. Quote the architecture sentence from the curriculum.
4. Pain that justifies page objects?
5. Pain that justifies configuration?
6. Pain that justifies parallel?
7. Pain that justifies governance?
8. Is skipping to API clients OK if that is the product risk?
9. How do you know you over-architected?
10. How does this relate to YAGNI?

## Interview Question

**Question:** How should an automation framework evolve?

A strong answer:

> Do not create everything on day one. Start with one Java class, then multiple classes, then extract methods when duplication hurts, then OOP, packages, page objects, test data models, factories, configuration, API clients, parallel execution, CI/CD, then governance. Architecture solves problems; it should not be added just to look advanced. Page objects when locators duplicate. ThreadLocal when we actually run parallel. Grid when we need remote browsers. I can map each folder to a pain we had.

## Homework

Journal: "We will add X when Y happens." Pick X from the ladder. Share in class. Do not add Grid tonight unless Y is already true.

---

## Answer Key

1. Create the entire enterprise tree / all tools
2. One class → multiple classes → duplication → methods → OOP → packages → page objects → models → factories → config → API clients → parallel → CI/CD → governance
3. Architecture solves problems. Architecture should not be added just to look advanced.
4. Locators/actions duplicated in tests
5. Environment-specific URLs/secrets/timeouts
6. Wall-clock suite time with isolated tests ready
7. Many people, inconsistent PRs, unclear ownership
8. Yes if APIs are the right risk — still don't add unused Grid
9. New tests are hard; unused layers; no pain story
10. YAGNI is the same idea in three letters
