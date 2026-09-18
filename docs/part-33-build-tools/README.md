# Part 33 — Build Tools

Until now, IntelliJ compiled your small programs. That is enough for learning syntax. It is not enough for a real SDET project.

A real project needs libraries: JUnit, TestNG, Selenium, REST Assured, JDBC drivers, logging, reporting. You cannot download JAR files by hand every week and hope they still match.

```text
WITHOUT a build tool

Download selenium.jar
Download junit.jar
Download json.jar
Put them in a lib folder
Tell IntelliJ where they are
Compile by hand
Run tests by hand
Pray the versions still match
```

A **build tool** automates that work:

```text
Declare what you need
   ↓
The tool downloads it
   ↓
Compiles your code
   ↓
Runs tests
   ↓
Packages a result
```

## The Two Main Java Build Tools

```text
Maven     XML file named pom.xml     most SDET jobs expect this first
Gradle    Groovy or Kotlin scripts   common in Android and some newer teams
```

This course teaches **Maven first**. Not because Gradle is bad. Because SDET interviews, Selenium tutorials, TestNG suites, and most Java automation repos still speak Maven.

## Chapter in This Part

| Chapter | Topic | You will be able to... |
| --- | --- | --- |
| [Chapter 89](chapter-89-why-build-tools.md) | Why build tools? | Explain the problem Maven solves, and why we start with Maven |

## Prerequisite

You can write Java classes, run them in IntelliJ, and explain compile vs run. You do not need Selenium yet. This part is about *how a project is built*, not about browsers.

## After This Part

Open Part 34. Maven is the first professional build tool you will actually use.
