# Chapter 91 — pom.xml

## 1. Today's Goal

By the end of this lesson, you will read a `pom.xml` and point to **groupId**, **artifactId**, **version**, **properties**, **dependencies**, and **plugins**. You will run:

```bash
mvn clean
mvn compile
mvn test
mvn package
mvn verify
```

and explain what each does.

## 2. Why It Matters

`pom.xml` is the contract of a Java team. CI reads it. New hires read it. Dependency scanners read it. If the POM is wrong, the whole pipeline is wrong.

SDET interviews often open with: "Walk me through this POM." They want to hear coordinates, Java version properties, test scope, and Surefire — not a shrug.

## 3. Real-Life Analogy

`pom.xml` is a shipping manifest.

```text
Who we are           groupId + artifactId + version
House rules          properties (Java 25, encoding UTF-8)
What we import       dependencies
Which machines       plugins (compiler, Surefire)
```

If the manifest says "Java 17" but the team uses JDK 25 features, the compiler plugin will complain. The manifest must match reality.

## 4. Illustrated Explanation

```text
pom.xml
├── project identity     groupId, artifactId, version
├── properties           reusable values, Java version
├── dependencies         libraries
└── build
    └── plugins          compiler, surefire, failsafe...
```

A dependency line:

```text
<dependency>
  <groupId>org.seleniumhq.selenium</groupId>
  <artifactId>selenium-java</artifactId>
  <version>4.21.0</version>
</dependency>
```

```text
groupId      who published it
artifactId   the product name
version      which release
scope        compile (default) or test
```

Commands map to lifecycle:

```text
mvn clean      wipe target/
mvn compile    compile main
mvn test       compile + run unit tests
mvn package    compile + test + build JAR
mvn verify     package + extra verification
```

You can chain:

```bash
mvn clean test
```

Meaning: delete old output, then compile and test from scratch. Useful when you suspect stale `target/` files.

## 5. Syntax / Concept

Root element is always `project` with the Maven POM schema. `modelVersion` is `4.0.0` for modern Maven. You copy it; you do not invent it.

**properties:** named values you reuse.

```xml
<properties>
    <maven.compiler.release>25</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <junit.version>5.11.4</junit.version>
</properties>
```

Then a dependency can use `${junit.version}` so you bump one number, not ten.

**plugins:** live under `<build><plugins>`. The compiler plugin must know Java 25. Surefire runs JUnit 5 or TestNG.

**packaging:** default `jar`. Stay with `jar` for this course.

## 6. Simple Example

A teaching POM for a calculator with JUnit 5. Type this. Do not worship it. Trace each tag.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.training.sdet</groupId>
    <artifactId>calculator-demo</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>Calculator Demo</name>

    <properties>
        <maven.compiler.release>25</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <junit.version>5.11.4</junit.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.5.2</version>
            </plugin>
        </plugins>
    </build>
</project>
```

Matching production class:

```java
package com.training.sdet;

public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }
}
```

Place it at `src/main/java/com/training/sdet/Calculator.java`.

A test belongs in `src/test/java/com/training/sdet/CalculatorTest.java` (you will write real `@Test` methods in Part 41). For this chapter, `mvn compile` is already a victory.

## 7. Real-World Example

A bank POM will grow more dependencies, but the skeleton stays:

```xml
<properties>
    <maven.compiler.release>25</maven.compiler.release>
    <selenium.version>4.21.0</selenium.version>
    <testng.version>7.10.2</testng.version>
    <restassured.version>5.5.0</restassured.version>
</properties>
```

E-commerce: same pattern. Pin versions in properties so "upgrade Selenium" is one edit, reviewed in Git.

Never mix "latest" as a version. Latest is not reproducible. Monday's latest is not Friday's.

## 8. SDET Example

Later you will add (do not add all of these today if you have not reached those parts):

```xml
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>${selenium.version}</version>
</dependency>
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>${testng.version}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>${restassured.version}</version>
    <scope>test</scope>
</dependency>
```

Notice TestNG and REST Assured as `test` scope if they are only used from tests. Selenium often lives in `main` because page objects in `src/main/java` need `WebDriver`.

BAD vs GOOD:

```text
BAD
<version>RELEASE</version>
<version>LATEST</version>
no properties, versions copied 12 times

GOOD
one property per library family
explicit numbers
scope=test for test-only libraries
```

## 9. Break the Code

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.11.4</version>
    <!-- forgot scope test; not fatal, but pollutes production classpath -->
</dependency>
```

Worse:

```xml
<artifactId>junit-jupiter</artifactId>
<!-- missing version AND no dependencyManagement -->
```

Maven cannot guess. Build fails with "version is missing."

Worse still: Java code uses `record` and `maven.compiler.release` is `8`. Compiler errors look like "language not supported." Students blame JDK install. The POM told the compiler to pretend it is Java 8.

## 10. Debug

| Symptom | Check |
| --- | --- |
| `No compiler is provided` | JDK is not configured; Maven is using a JRE |
| `package org.junit does not exist` | dependency missing, or test class is under `src/main/java` |
| Tests not running | Surefire plugin / test names / JUnit 5 need `junit-jupiter` + Surefire 3.x |
| Wrong Java language level | `maven.compiler.release` vs IntelliJ project SDK |
| `Non-resolvable parent POM` | you copied a company parent POM you do not have |

Always run Maven from the directory that contains `pom.xml`.

```bash
mvn -v
```

Confirm Maven uses JDK 25, not an old Java 8 leftover.

```text
mvn -v
Java version: 25.x   ← must match the course baseline
```

If IntelliJ shows a different SDK than `mvn -v`, fix that mismatch before debugging tests.

## 11. Student Exercise

Create `pom.xml` for:

- groupId `com.training.sdet`
- artifactId `status-checker`
- version `1.0.0-SNAPSHOT`
- Java 25 via properties
- JUnit Jupiter test dependency
- compiler + surefire plugins

Put `StatusChecker.java` in the correct main path with:

```java
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
}
```

Run `mvn clean compile`. It must succeed.

## 12. Challenge

Add a property `junit.version` and reference it with `${junit.version}`. Run:

```bash
mvn clean test
```

Even if you have no tests yet, read the log. Note the phases Maven prints (`clean`, `resources`, `compile`, `test-compile`, `test`). Write those phase names in your notebook.

Then run `mvn package` and find the JAR under `target/`. Open it only with curiosity; do not ship it.

## 13. Knowledge Check

1. What do groupId, artifactId, and version identify?
2. Why use `<properties>`?
3. What does `<scope>test</scope>` mean?
4. Which plugin compiles Java?
5. Which plugin runs unit tests?
6. What does `mvn clean` delete?
7. What extra work does `mvn package` do compared with `mvn test`?
8. What does `mvn verify` add after package (conceptually)?
9. Why is version `LATEST` a bad idea?
10. Should Selenium dependencies for page objects usually be `test` scope if pages live in `src/main/java`?

## 14. Interview Question

**Question:** Walk me through a Maven `pom.xml`.

A strong answer:

> The POM is the Project Object Model. groupId, artifactId, and version identify our artifact. properties hold Java version and library versions. dependencies list libraries from Maven Central; test scope keeps JUnit off the production classpath. plugins under build run the compiler and Surefire. I run mvn clean compile test package verify. Default layout is src/main/java and src/test/java. I pin versions. I do not use LATEST.

## 15. Homework

Keep the `status-checker` Maven project. You will add JUnit tests in Part 41. For now, make sure `mvn -v` shows JDK 25 and `mvn clean compile` is green.

Write in notes: one paragraph on why CI should call Maven instead of "click run in IntelliJ."

---

## Answer Key

1. The project's (or a dependency's) Maven coordinates.
2. One place to change Java version or library versions.
3. The library is for tests, not for the production artifact classpath.
4. maven-compiler-plugin.
5. maven-surefire-plugin.
6. `target/`
7. It builds the JAR (after tests).
8. Additional checks bound to verify (integration plugins, quality gates).
9. Builds are not reproducible; Monday differs from Friday.
10. No — main code needs Selenium at compile scope (default).
