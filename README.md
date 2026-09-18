# Java Programming & Software Engineering for SDET

**Complete beginner course:** zero technical knowledge → Java programmer → software engineer → automation engineer → SDET → senior SDET → QA automation architect.

If you can honestly say *“I don't know what Java is,”* start at Chapter 1. The destination is:

> I can design, code, test, debug, review, and maintain professional Java-based software and automation frameworks.

---
<video src="https://github.com/user-attachments/assets/0c583563-5c00-424c-aa66-53de10582a29" controls="controls" width="640" height="360">
</video>

---

## Book purpose

This course assumes you may initially know nothing about computers, programming, Java, software development, testing, automation, IDEs, Git, Maven, APIs, databases, object-oriented programming, or software architecture.

It does **not** teach Java as syntax → memorize → exam.

It teaches this path in every lesson:

```text
PROBLEM → UNDERSTAND → ALGORITHM → JAVA CONCEPT → CODE
    → RUN → FAIL → DEBUG → REFACTOR → TEST → IMPROVE → ARCHITECT
```

## Who this is for

- Complete beginners
- Manual testers moving into automation
- Anyone who wants a Java-first SDET career path, not a “record-and-playback” shortcut

You need a computer (Windows, macOS, or Linux), curiosity, and willingness to type code yourself.

## Complete learning journey

```text
Computer Basics
      ↓
What Programming Means
      ↓
History of Programming
      ↓
History of Java
      ↓
How Java Works
      ↓
JDK / JVM / JRE
      ↓
IntelliJ IDEA
      ↓
First Java Program
      ↓
Variables & Data Types
      ↓
Conditions
      ↓
Loops
      ↓
Methods
      ↓
Arrays
      ↓
Object-Oriented Programming
      ↓
Collections
      ↓
Exceptions
      ↓
Files
      ↓
Generics
      ↓
Lambdas
      ↓
Streams
      ↓
Date / Time
      ↓
Regular Expressions
      ↓
Annotations
      ↓
JVM & Memory
      ↓
Concurrency
      ↓
Clean Code
      ↓
Design Principles
      ↓
Design Patterns
      ↓
Maven / Gradle
      ↓
Git / GitHub
      ↓
Unit Testing
      ↓
TestNG
      ↓
API Programming
      ↓
Database Programming
      ↓
Selenium
      ↓
REST Assured
      ↓
Automation Framework Design
      ↓
CI/CD
      ↓
Parallel Execution
      ↓
Enterprise SDET Architecture
      ↓
QA Automation Architect
```

## How to start (today)

1. Read **[How to Use This Course](docs/00-how-to-use-this-course.md)**.
2. Skim the **[learning journey](docs/00-learning-journey.md)** and **[student levels 0–8](docs/00-student-levels.md)**.
3. Open **[Chapter 1 — What is a computer?](docs/part-01-before-java/chapter-01-what-is-a-computer.md)**.
4. When you reach Chapter 15, run your first program from [`lessons/`](lessons/README.md).

Full documentation index: **[docs/README.md](docs/README.md)**.

## Repository map

```text
coreJavaTrainingCourse/
├── docs/                          Teaching chapters (Parts 1–66)
├── lessons/                       Runnable Java examples per chapter
├── projects/                      Skill projects 01–17
├── capstone/java-sdet-engineering Enterprise SDET platform skeleton
├── assessments/                   Weekly quizzes, exams, interview guides
└── .github/workflows/ci.yml       Compile → unit → smoke
```

| Path | What you do there |
| --- | --- |
| [docs/](docs/README.md) | Read lessons. Each chapter has exercises, a knowledge check, and homework. |
| [lessons/](lessons/README.md) | Run the matching Java demo in IntelliJ or with Maven. |
| [projects/](projects/README.md) | Build 17 programs, from Hello World to CI. |
| [capstone/](capstone/java-sdet-engineering/README.md) | Assemble UI, API, data, logging, and parallel architecture. |
| [assessments/](assessments/README.md) | Weekly scoring, final exam battery, interview answers. |

## Software you will install

Recommended classroom baseline:

```text
JDK 25 LTS
IntelliJ IDEA
Git
Maven (or IntelliJ's bundled Maven)
```

Lesson and project `pom.xml` files use **Java 17** as the compiler release so a machine that is not yet on JDK 25 can still compile records and text blocks. Install **25** when you can; 17 is the compatibility floor.

Verify:

```bash
java -version
javac -version
```

- `java` runs bytecode
- `javac` compiles source

First IntelliJ project name in the early chapters: `java-learning` (IntelliJ build system first, Maven later).

## Run a lesson

```bash
mvn -f lessons/pom.xml -q exec:java -Dexec.mainClass=com.sdet.lessons.chapter15.HelloWorld
```

Part 8 mini-project (expected vs actual status code):

```bash
mvn -f lessons/pom.xml -q exec:java -Dexec.mainClass=com.sdet.lessons.chapter28.TestResultEvaluator
```

## Run projects

From any project folder:

```bash
mvn test
```

Hello Java (Project 1):

```bash
mvn -f projects/01-hello-java/pom.xml -q compile exec:java
```

Project 8 (Java HTTP client) needs **network access**. Selenium **live** browser runs are opt-in in Project 12; `mvn test` uses a fake driver so you do not need Chrome installed.

Capstone:

```bash
mvn -f capstone/java-sdet-engineering/pom.xml test
```

## Student levels

| Level | You can honestly say |
| --- | --- |
| 0 Computer beginner | I know hardware vs software and what an algorithm is |
| 1 Java explorer | I can run a class with variables, `if`, and loops |
| 2 Java programmer | I can write methods, arrays, classes, and objects |
| 3 Java software developer | I use OOP, collections, exceptions, files, generics |
| 4 Advanced Java developer | I use streams, lambdas, records, concurrency, design principles |
| 5 QA automation engineer | I use JUnit, TestNG, Selenium, REST Assured, Maven |
| 6 SDET | I cover UI, API, database, Git, CI/CD, and parallelism |
| 7 Senior SDET | I review code, fight flaky tests, and design frameworks |
| 8 QA automation architect | I can design and defend an enterprise quality system |

## Daily class format

Every chapter uses the same 15 steps (see [Part 63](docs/part-63-daily-class-format/README.md)):

1. Today's goal  
2. Why it matters  
3. Real-life analogy  
4. Illustrated explanation  
5. Syntax  
6. Simple example  
7. Real-world example  
8. SDET example  
9. Break the code  
10. Debug  
11. Student exercise  
12. Challenge  
13. Knowledge check  
14. Interview question  
15. Homework  

## Weekly assessment weights

| Area | Weight |
| --- | --- |
| Theory | 20% |
| Coding | 35% |
| Problem solving | 20% |
| Debugging | 15% |
| Explanation | 10% |

Materials live under [`assessments/weekly/`](assessments/README.md).

## Projects at a glance

1. Hello Java  
2. Calculator  
3. Student grade calculator  
4. Bank account simulator  
5. Employee management system  
6. File analyzer  
7. Test result analyzer  
8. REST API client (Java `HttpClient`)  
9. Database validator (JDBC / H2)  
10. JUnit  
11. TestNG  
12. Selenium UI (page objects; tests use a fake driver)  
13. REST Assured (localhost server)  
14. Data-driven framework  
15. Cross-browser framework  
16. Parallel framework (`ThreadLocal`)  
17. CI/CD framework  

Then the **Enterprise Java SDET Platform** capstone: Java, Maven, JUnit, TestNG ideas, Selenium architecture, REST Assured, JDBC, JSON, page objects, API clients, data models and factories, configuration, logging, reporting, Git, GitHub, CI/CD, and parallel execution.

Architecture rule ([Part 61](docs/part-61-framework-evolution/how-architecture-should-evolve.md)):

> Architecture solves problems. Architecture should not be added just to look advanced.

## Most important SDET rules baked into the course

- Compare String **values** with `.equals()` / `.equalsIgnoreCase()`, not `==`
- Do not swallow test failures with empty `catch (Exception e) {}`
- Prefer `Path` / `Files` over hand-built OS path strings
- Do not default to `Thread.sleep(5000)`
- Never log passwords, tokens, secrets, or sensitive customer data
- Prefer composition when it is simpler than inheritance
- Do not make everything `static`
- Singleton is often overused
- Isolated tests: one driver (or fake driver) per thread

## Interview and exam path

- [Beginner](assessments/interview/beginner.md) · [Intermediate](assessments/interview/intermediate.md) · [Advanced Java](assessments/interview/advanced-java.md) · [SDET](assessments/interview/sdet.md) · [Architect](assessments/interview/architect.md)
- [Final exam guide](assessments/exams/final-exam-guide.md)

Architect prompt you should eventually be able to answer:

> Design a framework for 5,000 tests and 50 automation engineers.

## Final student mindset

You start with:

```java
System.out.println("Hello, Java!");
```

You finish able to walk this chain:

```text
Requirement
   ↓
Design
   ↓
Java implementation
   ↓
Automated tests
   ↓
API / UI / database validation
   ↓
CI/CD
   ↓
Quality architecture
```

And say:

> I understand where Java came from, how source becomes bytecode, how the JVM executes it, how to write professional Java, how to test and debug it, how to build automation frameworks, and how to design scalable quality-engineering systems.
