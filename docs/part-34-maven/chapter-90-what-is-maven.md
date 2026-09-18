# Chapter 90 — What Is Maven?

## 1. Today's Goal

By the end of this lesson, you will explain Maven as a build tool that reads `pom.xml`, uses a standard folder layout, and runs a **lifecycle** of steps (clean, compile, test, package, verify).

You will recognize this tree on sight:

```text
project/
├── src/main/java
├── src/test/java
└── pom.xml
```

## 2. Why It Matters

Teams share more than code. They share **how code is built**. Maven is a convention:

- production Java lives in `src/main/java`
- tests live in `src/test/java`
- the recipe lives in `pom.xml`

When those conventions hold, a stranger can clone the repo and know where to look. SDET frameworks die when every engineer invents a new folder named `stuff` or `newTestsFinal`.

## 3. Real-Life Analogy

Maven is a school with a fixed schedule.

```text
Homeroom     clean old worksheets
Period 1     compile (translate student work into a standard form)
Period 2     test (quiz)
Period 3     package (put the notebook in a folder)
Period 4     verify (extra checks)
```

You do not invent a new period order every Monday. The lifecycle is the schedule. Plugins are the teachers who actually run each period.

A warehouse analogy:

```text
pom.xml          shopping list + store address of the project
Maven Central    the warehouse
~/.m2            your pantry at home (cache)
src/main/java    the kitchen where the product is cooked
src/test/java    the tasting table
```

## 4. Illustrated Explanation

High-level flow:

```text
Java Project
   ↓
pom.xml
   ↓
Dependencies   (libraries Maven downloads)
   ↓
Plugins        (tools that compile, test, package)
   ↓
Build Lifecycle
```

Directory contract:

```text
project/
├── src/
│   ├── main/
│   │   └── java/                 com.example.app.Calculator
│   │
│   └── test/
│       └── java/                 com.example.app.CalculatorTest
│
└── pom.xml
```

Later you will also see `src/main/resources` (config files) and `src/test/resources` (test data). Same idea: production vs test.

Lifecycle (simplified, the ones you will actually say in interviews):

```text
clean      delete target/ (old compiled files)
compile    compile src/main/java into target/classes
test       compile tests and run them
package    build a JAR (or other artifact) in target/
verify     run extra checks (integration rules, some plugins)
install    put the artifact in your local ~/.m2 (later)
```

Each later phase includes the earlier ones (except `clean`, which you often run first on purpose).

```text
mvn test
   runs compile, then test

mvn package
   runs compile, test, then package
```

That is why `mvn package` can feel slow: it runs tests unless you ask it not to.

```text
Your source          Maven output
src/main/java   →    target/classes
src/test/java   →    target/test-classes
packaging       →    target/artifactId-version.jar
```

Never edit files inside `target/` by hand. Maven owns that folder. `clean` deletes it.

## 5. Syntax / Concept

Maven identity of *your* project also uses coordinates:

```text
groupId      organization, often a reversed domain: com.training.sdet
artifactId   project name: login-tests
version      1.0.0-SNAPSHOT   (SNAPSHOT means still changing)
```

Together they name the thing you build, just as they name dependencies you consume.

**Plugin:** a small program Maven runs during a phase. The compiler plugin compiles. The Surefire plugin runs unit tests. You will see plugins in Chapter 91.

**Scope** (preview): a dependency can be `test` scoped so it is on the test classpath but not shipped as if it were production code. JUnit is almost always `test` scope.

## 6. Simple Example

A tiny Maven project that only prints hello still uses the layout:

```text
hello-maven/
├── pom.xml
└── src/main/java/com/training/HelloMaven.java
```

```java
package com.training;

public class HelloMaven {

    public static void main(String[] args) {
        System.out.println("Maven compiled me");
    }
}
```

You do not put `HelloMaven.java` in the project root if you want Maven to compile it automatically. The path under `src/main/java` must match the package.

```text
package com.training
file: src/main/java/com/training/HelloMaven.java
```

Mismatch is a common beginner trap.

## 7. Real-World Example

A banking automation repo:

```text
bank-automation/
├── pom.xml
├── src/main/java/com/bank/automation/
│     pages/LoginPage.java
│     api/TransferClient.java
│     driver/DriverFactory.java
└── src/test/java/com/bank/tests/
      ui/LoginTest.java
      api/TransferApiTest.java
```

Production framework code is not "the website." It is the automation support code. Tests live under `src/test/java`. Mixing them makes `mvn test` confusing and makes packaging include tests by accident.

Online store: `src/main/java` might hold `CartPage` and `OrderClient`. `src/test/java` holds `CheckoutTest`. Same rule.

## 8. SDET Example

When CI says "Maven failed at test," it means the Surefire plugin ran tests and at least one failed — or tests did not compile. The lifecycle tells you *when* it failed:

```text
Failed at compile     your Java has errors
Failed at test        a test assertion failed or an exception escaped
Failed at package     compile and tests passed; packaging plugin failed
```

SDET habit: read the **first failure in the Maven log**, not the last red IntelliJ squiggle.

BAD vs GOOD:

```text
BAD
C:\mytests\Login.java
C:\mytests\try2\LoginTest.java
C:\jars\selenium.jar

GOOD
src/main/java/.../LoginPage.java
src/test/java/.../LoginTest.java
pom.xml
```

## 9. Break the Code

```text
File: src/Hello.java
package com.training;
public class Hello { }
```

Maven looks in `src/main/java`. This file is invisible to the default compiler plugin. `mvn compile` succeeds and produces nothing useful. You think Java is broken. The folder is wrong.

Another break:

```text
package com.training;
file path: src/main/java/Hello.java
```

Package and folder disagree. Compiles may fail or behave oddly depending on settings.

## 10. Debug

If `mvn compile` says "nothing to compile":

1. Is the file under `src/main/java`?
2. Does the folder path match the `package` line?
3. Are you in the directory that contains `pom.xml` when you run `mvn`?

```text
cd hello-maven        ← the folder with pom.xml
mvn compile
```

Running Maven from a random parent folder is a classic "I typed the command and it said no POM" bug.

IntelliJ: **Open** the folder that contains `pom.xml` as a Maven project. Let IntelliJ import Maven. If you create a plain Java module and then drop in a `pom.xml`, reload the Maven project.

## 11. Student Exercise

On paper, draw a Maven project named `status-checker` with:

- a production class `StatusChecker` in package `com.training.qa`
- a test class `StatusCheckerTest` in package `com.training.qa`

Write the full paths from `src/...`.

Then write one sentence: what does `mvn test` do that `mvn compile` does not?

## 12. Challenge

Explain this sentence to a beginner:

> `mvn package` will run tests.

Why is that usually good? When might a team skip tests for a local experiment (`-DskipTests`), and why is skipping tests dangerous on CI?

Write a 6-line lifecycle diagram for `mvn clean package`.

## 13. Knowledge Check

1. What file is Maven's project descriptor?
2. What does POM stand for?
3. Where does production Java live?
4. Where do tests live?
5. What folder should you not edit by hand?
6. Name five lifecycle commands/phases from this chapter.
7. Does `mvn test` compile production code first?
8. What three coordinates identify *your* project?
9. What is `SNAPSHOT` a hint of?
10. Why do SDET repos put `LoginPage` in `main` and `LoginTest` in `test`?

## 14. Interview Question

**Question:** What is Maven, and what is the standard Maven project layout?

A strong answer:

> Maven is a Java build tool. It reads pom.xml for coordinates, dependencies, and plugins, then runs a lifecycle: clean, compile, test, package, verify. Production code lives in src/main/java. Tests live in src/test/java. Output goes to target/. That convention lets any engineer or CI job run mvn test the same way. SDET frameworks use main for page objects and clients, and test for the actual tests.

## 15. Homework

Create a Maven project in IntelliJ (New Project → Maven → JDK 25). Accept the default layout. Create `src/main/java/com/training/HelloMaven.java` with a `main` that prints `Maven compiled me`. Run `mvn compile` from the project root (or IntelliJ's Maven tool window). Confirm `target/classes` appears. Do not add dependencies yet — that is Chapter 91.

If Maven is not on your PATH, use IntelliJ's bundled Maven: View → Tool Windows → Maven → compile.

---

## Answer Key

1. `pom.xml`
2. Project Object Model
3. `src/main/java`
4. `src/test/java`
5. `target/`
6. clean, compile, test, package, verify (install is extra credit)
7. Yes
8. groupId, artifactId, version
9. The version is still in development / not a final release
10. Pages are framework (main); tests are tests (test). Maven and CI treat them differently.
