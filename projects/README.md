# Projects 1–17

Work these in order. Each folder is a standalone Maven module with a README, source, and (from Project 2 onward) tests you can run with `mvn test`.

Classroom JDK baseline is **25 LTS**. These modules compile with **Java 17** so older local JDKs still work.

```text
One Java class
      ↓
Methods and OOP
      ↓
Files, streams, HTTP, JDBC
      ↓
JUnit / TestNG
      ↓
Selenium + REST Assured architecture
      ↓
Data, browsers, parallel, CI
      ↓
Capstone
```

| # | Folder | Concepts | Run |
| --- | --- | --- | --- |
| 1 | [01-hello-java](01-hello-java/README.md) | class, `main`, console | `mvn -q compile exec:java` |
| 2 | [02-calculator](02-calculator/README.md) | variables, operators, methods | `mvn test` |
| 3 | [03-student-grade-calculator](03-student-grade-calculator/README.md) | conditions, loops | `mvn test` |
| 4 | [04-bank-account-simulator](04-bank-account-simulator/README.md) | encapsulation | `mvn test` |
| 5 | [05-employee-management](05-employee-management/README.md) | collections, interfaces | `mvn test` |
| 6 | [06-file-analyzer](06-file-analyzer/README.md) | files, exceptions | `mvn test` |
| 7 | [07-test-result-analyzer](07-test-result-analyzer/README.md) | streams, records | `mvn test` |
| 8 | [08-rest-api-client](08-rest-api-client/README.md) | Java HTTP, JSON | needs network |
| 9 | [09-database-validator](09-database-validator/README.md) | JDBC, H2, SQL | `mvn test` |
| 10 | [10-junit-tests](10-junit-tests/README.md) | JUnit, Arrange-Act-Assert | `mvn test` |
| 11 | [11-testng-project](11-testng-project/README.md) | TestNG lifecycle, DataProvider | `mvn test` |
| 12 | [12-selenium-ui](12-selenium-ui/README.md) | POM, DriverFactory (fake driver in tests) | `mvn test` |
| 13 | [13-rest-assured-api](13-rest-assured-api/README.md) | given/when/then on localhost | `mvn test` |
| 14 | [14-data-driven-framework](14-data-driven-framework/README.md) | JSON data factory | `mvn test` |
| 15 | [15-cross-browser-framework](15-cross-browser-framework/README.md) | browser from config | `mvn test` |
| 16 | [16-parallel-framework](16-parallel-framework/README.md) | `ThreadLocal` isolation | `mvn test` |
| 17 | [17-cicd-framework](17-cicd-framework/README.md) | smoke tests + GitHub Actions | `mvn test` |

After Project 17, build the [Enterprise Java SDET Platform](../capstone/java-sdet-engineering/README.md).

## Early mini-project (Part 8)

Before the numbered roadmap, Chapter 26–28 includes a **Test Result Evaluator** (expected vs actual status code). Runnable copy:

```bash
mvn -f lessons/pom.xml -q exec:java -Dexec.mainClass=com.sdet.lessons.chapter28.TestResultEvaluator
```

## Selenium live browser

Project 12 tests use a **FakeDriver** so `mvn test` does not need Chrome. Live WebDriver is opt-in. See that project's README.

## Do not start with the capstone

Architecture solves problems you have already felt. One class comes first. The evolution path is in [Part 61](../docs/part-61-framework-evolution/how-architecture-should-evolve.md).
