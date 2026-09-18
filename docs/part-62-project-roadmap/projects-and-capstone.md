# Projects and Capstone

## Goal

By the end of this part, you will know the 17 skill-building projects and the **Enterprise Java SDET Platform** capstone: required technologies, architecture diagram, and what "done" means.

## Why It Matters

Reading Selenium is not a portfolio. A GitHub repo with a README, Maven, tests, and CI is. The capstone is how you prove Level 7.

## Real-Life Analogy

Music: scales (projects 1–9), ensemble pieces (10–17), a recital (capstone). Skipping scales and performing a symphony is noise.

## Illustrated Explanation — the sequence

```text
1 Hello Java
2 Calculator
3 Student Grade Calculator
4 Bank Account Simulator
5 Employee Management System
6 File Analyzer
7 Test Result Analyzer
8 REST API Client
9 Database Validator
10 JUnit Test Project
11 TestNG Project
12 Selenium UI Automation
13 REST Assured API Framework
14 Data-Driven Framework
15 Cross-Browser Framework
16 Parallel Framework
17 CI/CD Framework
        ↓
FINAL CAPSTONE
Enterprise Java SDET Platform
```

## PROJECT 1 — Hello Java

**Goal:** class, `main`, run in IntelliJ.

**Done:** prints a welcome line you wrote. You can explain `public static void main`.

## PROJECT 2 — Calculator

**Concepts:**

```text
Variables
Operators
Methods
```

**Done:** `add`, `subtract`, `multiply`, `divide` (define divide-by-zero behavior). Later, Project 10 tests this code.

## PROJECT 3 — Student Grade Calculator

**Concepts:**

```text
Conditions
Loops
Methods
```

**Done:** list of scores → average → letter grade. Print a small report.

## PROJECT 4 — Bank Account Simulator

**Concepts:**

```text
Classes
Objects
Encapsulation
```

**Done:** `BankAccount` with private balance, `deposit`, `withdraw`, rejected overdraft. No UI required.

## PROJECT 5 — Employee Management System

**Concepts:**

```text
Collections
OOP
Interfaces
```

**Done:** `List<Employee>`, find by id, an interface `Payable` or similar. No database yet.

## PROJECT 6 — File Analyzer

**Concepts:**

```text
Files
Exceptions
Collections
```

**Done:** read a text file of "test names + PASS/FAIL", count fails, handle missing file with a clear message.

## PROJECT 7 — Test Result Analyzer

**Concepts:**

```text
Streams
Collections
Records
```

**Done:** `record TestResult(String name, String status, int durationMs)` processed with streams: count failed, slowest test. This is your advanced-exam cousin.

## PROJECT 8 — REST API Client

**Concepts:**

```text
HTTP
JSON
Models
```

**Done:** Java HttpClient GET + parse a simple JSON field into a record. Assert status with `statusMatches`.

## PROJECT 9 — Database Validator

**Concepts:**

```text
JDBC
SQL
Models
```

**Done:** `SELECT` a row you inserted (local or training DB). try-with-resources. No production DB.

## PROJECT 10 — JUnit Test Project

**Done:** JUnit 5 tests for Calculator and `statusMatches`. Arrange-act-assert. `mvn test` green.

## PROJECT 11 — TestNG Project

**Done:** lifecycle demo + `@DataProvider` for status rows. `@BeforeMethod` / `@AfterMethod` exist even if they only reset a calculator.

## PROJECT 12 — Selenium UI Automation

**Done:** open a site, locators, wait (no default sleep), `quit`. At least one assertion. Page object started.

## PROJECT 13 — REST Assured API Framework

**Done:** `given/when/then` smoke + a small `UserApiClient` (or public API client). No token logging.

## PROJECT 14 — Data-Driven Framework

**Done:** climb at least to DataProvider + model. JSON optional. Independent rows.

## PROJECT 15 — Cross-Browser Framework

**Done:** `DriverFactory.create(CHROME|FIREFOX|EDGE)` (skip a browser only if the OS cannot run it, documented).

## PROJECT 16 — Parallel Framework

**Done:** ThreadLocal driver, two methods parallel, different driver hashes, quit+remove.

## PROJECT 17 — CI/CD Framework

**Done:** GitHub Actions on PR runs compile + unit + (optional) smoke. README how to run locally.

---

# FINAL CAPSTONE — Enterprise Java SDET Platform

Build a **single** Maven project that a teammate can clone and run.

## Required technologies / concepts

```text
Java
+
Maven
+
JUnit
+
TestNG
+
Selenium
+
REST Assured
+
JDBC
+
JSON
+
Page Objects
+
API Clients
+
Data Models
+
Data Factories
+
Configuration
+
Logging
+
Reporting
+
Git
+
GitHub
+
CI/CD
+
Parallel Execution
```

You do not need every tool on every test. You need them **in the platform**, used where they earn their keep.

## Capstone architecture

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

Supporting:

```text
Models
Factories
Configuration
Database
Logging
Reporting
Utilities
Test Data
```

This matches Part 60. Folders may follow `java-sdet-engineering/`.

## Capstone definition of done

- README: JDK 25, one smoke command, how to set env secrets
- `mvn test` runs unit tests without a browser
- UI smoke uses page objects, waits, factory, quit
- API tests use REST Assured or HttpClient with models
- At least one DB or integration check **or** a documented fake if no DB exists
- Config for at least two environments (qa/staging URLs)
- Logs without secrets
- Failure evidence (screenshot or Allure or `target/evidence`)
- GitHub PR workflow: compile, unit, smoke
- Parallel-safe driver if you enable parallel (ThreadLocal)
- A 10-minute architecture talk (use Part 66 questions)

## Student Exercise

Create a table in your notes: Project number, date started, date done, Git tag. Tag `project-02-calculator` etc.

## Challenge

Write a capstone README outline before you code the capstone. If you cannot describe how to run smoke, you are not designed yet.

## Knowledge Check

1. How many numbered projects before the capstone?
2. Which project is the Bank Account Simulator?
3. Which concepts does Project 7 use?
4. When do JUnit and TestNG appear?
5. Name the capstone title.
6. Recite four required capstone technologies.
7. Draw the capstone TESTS split.
8. What supporting list sits under the architecture?
9. Must every test use JDBC?
10. What is the first project?

## Interview Question

**Question:** What have you built?

A strong answer (after you have done the work):

> I followed a ladder from Hello Java and a calculator through OOP, files, HTTP, JDBC, JUnit, TestNG, Selenium with page objects, REST Assured, data-driven tests, cross-browser factory, ThreadLocal parallel, and GitHub Actions. The capstone is an Enterprise Java SDET Platform: tests split UI, API, integration, over page objects and API clients, with models, factories, config, logging, reporting, and CI. I can clone and run smoke with one documented command.

## Homework

If you are early in the course, do the next undone project, not the capstone. If you are finishing, open the capstone as a GitHub repo named honestly, not `final-final-2`.

---

## Answer Key

1. 17
2. Project 4
3. Streams, Collections, Records
4. Projects 10 and 11
5. Enterprise Java SDET Platform
6. Any four from the required list
7. UI | API | Integration under TESTS
8. Models Factories Configuration Database Logging Reporting Utilities Test Data
9. No — required in the platform, used where persistence matters
10. Hello Java
