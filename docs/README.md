# Course Documentation Index

Start here if you already read the [root README](../README.md). Then open **[How to Use This Course](00-how-to-use-this-course.md)**.

Every numbered chapter follows the same class format: goal, why, analogy, diagram, syntax, simple example, real-world example, SDET example, broken code, debug, exercise, challenge, knowledge check, interview question, homework.

## Orientation

| File | Purpose |
| --- | --- |
| [How to use this course](00-how-to-use-this-course.md) | Software, study habits, course rule |
| [Learning journey](00-learning-journey.md) | Computer basics → QA Automation Architect |
| [Student levels 0–8](00-student-levels.md) | How to know you are ready to advance |

## Parts 1–11 — From zero to Java programmer

| Part | Topic |
| --- | --- |
| [1. Before Java](part-01-before-java/README.md) | Computer, programming, why languages exist |
| [2. History of Java](part-02-history-of-java/README.md) | Oak, Gosling, ownership, evolution |
| [3. How Java works](part-03-how-java-works/README.md) | Source, `javac`, JVM, JDK/JRE, runtime |
| [4. IntelliJ setup](part-04-intellij-setup/README.md) | IDE, JDK 25, first project, comments |
| [5. Variables](part-05-variables/README.md) | Named storage, naming rules |
| [6. Data types](part-06-data-types/README.md) | Primitives, references, String, immutability |
| [7. Operators](part-07-operators/README.md) | Arithmetic, comparison, logical, `.equals()` |
| [8. Decision making](part-08-decision-making/README.md) | `if`, `else`, `switch`, test-result mini-project |
| [9. Loops](part-09-loops/README.md) | `for`, `while`, `do-while`, `break` / `continue` |
| [10. Methods](part-10-methods/README.md) | Parameters, return values, overloading |
| [11. Arrays](part-11-arrays/README.md) | Indexes from 0, enhanced `for` |

## Parts 12–32 — Software developer and advanced Java

| Part | Topic |
| --- | --- |
| [12. OOP](part-12-oop/README.md) | Class, object, encapsulation, inheritance, polymorphism, composition |
| [13. Packages](part-13-packages/README.md) | Organizing a growing codebase |
| [14. Collections](part-14-collections/README.md) | List, Set, Map, Queue, how to choose |
| [15. Wrappers](part-15-wrapper-classes/README.md) | Autoboxing, `List<Integer>` |
| [16. Enums](part-16-enums/README.md) | `BrowserType` instead of magic strings |
| [17. Exceptions](part-17-exceptions/README.md) | try/catch/finally, throw/throws — never hide test failures |
| [18. Files](part-18-file-handling/README.md) | `Path` and `Files` |
| [19. JSON](part-19-json/README.md) | JSON → Java objects (Jackson later) |
| [20. Generics](part-20-generics/README.md) | `ApiResponse<T>` |
| [21. Lambdas](part-21-lambdas/README.md) | Functional interfaces |
| [22. Streams](part-22-streams/README.md) | filter, map, reduce |
| [23. Optional](part-23-optional/README.md) | Absence of a value — not everywhere |
| [24. Date/time](part-24-date-time/README.md) | `LocalDate`, `Instant`, `Duration` |
| [25. Regex](part-25-regular-expressions/README.md) | IDs, logs, emails |
| [26. Records](part-26-records/README.md) | Immutable test-data models |
| [27. Sealed classes](part-27-sealed-classes/README.md) | Restricted hierarchies (advanced) |
| [28. Annotations](part-28-annotations/README.md) | `@Override`, `@Test`, custom metadata |
| [29. Reflection](part-29-reflection/README.md) | Runtime inspection — use with reason |
| [30. JVM memory](part-30-jvm-memory/README.md) | Stack, heap, GC, JIT |
| [31. Concurrency](part-31-concurrency/README.md) | Threads, races, executors, virtual threads |
| [32. Debugging](part-32-debugging/README.md) | Breakpoints and IntelliJ debugger |

## Parts 33–66 — SDET, frameworks, and architecture

| Part | Topic |
| --- | --- |
| [33. Build tools](part-33-build-tools/README.md) | Why Maven/Gradle exist |
| [34. Maven](part-34-maven/README.md) | `pom.xml`, lifecycle |
| [35. Git](part-35-git/README.md) | Version control |
| [36. GitHub](part-36-github/README.md) | PRs, review, CI |
| [37. Clean code](part-37-clean-code/README.md) | Names, small methods, responsibility |
| [38. DRY / KISS / YAGNI](part-38-dry-kiss-yagni/README.md) | Do not overbuild |
| [39. SOLID](part-39-solid/README.md) | Practical Java + SDET examples |
| [40. Design patterns](part-40-design-patterns/README.md) | Factory, Builder, Strategy, and more |
| [41. Unit testing](part-41-unit-testing/README.md) | JUnit, Arrange-Act-Assert |
| [42. TestNG](part-42-testng/README.md) | Lifecycle, DataProvider |
| [43. API fundamentals](part-43-api-fundamentals/README.md) | HTTP methods and status codes |
| [44. Java HTTP client](part-44-java-http-client/README.md) | What libraries abstract |
| [45. REST Assured](part-45-rest-assured/README.md) | given / when / then |
| [46. Database](part-46-database/README.md) | SQL and JDBC |
| [47. Selenium](part-47-selenium/README.md) | WebDriver after Java fundamentals |
| [48. Locators](part-48-locators/README.md) | Stable selectors |
| [49. Waits](part-49-waits/README.md) | Never default `Thread.sleep` |
| [50. Page Object Model](part-50-page-object-model/README.md) | Tests talk to pages, not locators |
| [51. Test data models](part-51-test-data-model/README.md) | Records and POJOs |
| [52. Data-driven testing](part-52-data-driven-testing/README.md) | Hardcoded → factory |
| [53. Driver factory](part-53-driver-factory/README.md) | Chrome / Firefox / Edge |
| [54. Parallel automation](part-54-parallel-automation/README.md) | Isolation and `ThreadLocal` |
| [55. Selenium Grid](part-55-selenium-grid/README.md) | Remote browsers |
| [56. Logging](part-56-logging/README.md) | Never log secrets |
| [57. Reporting](part-57-reporting/README.md) | Evidence and Allure |
| [58. CI/CD](part-58-cicd/README.md) | GitHub Actions |
| [59. Code review](part-59-code-review/README.md) | How to review Java |
| [60. Enterprise architecture](part-60-enterprise-architecture/README.md) | Target framework layout |
| [61. Framework evolution](part-61-framework-evolution/README.md) | Do not build everything on day 1 |
| [62. Project roadmap](part-62-project-roadmap/README.md) | Projects 1–17 and capstone |
| [63. Daily class format](part-63-daily-class-format/README.md) | The 15 teaching steps |
| [64. Weekly assessment](part-64-weekly-assessment/README.md) | Scoring model |
| [65. Interview training](part-65-interview-training/README.md) | Beginner → architect |
| [66. Final exam](part-66-final-exam/README.md) | Full exam battery |

## Code, projects, and assessments

- Runnable chapter programs: [`lessons/`](../lessons/README.md)
- Skill-building projects: [`projects/README.md`](../projects/README.md)
- Enterprise capstone: [`capstone/java-sdet-engineering/`](../capstone/java-sdet-engineering/README.md)
- Quizzes, exams, interviews: [`assessments/README.md`](../assessments/README.md)
