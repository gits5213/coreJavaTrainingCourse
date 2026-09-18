# Part 42 — TestNG

JUnit is the default for unit tests in this course. **TestNG** is widely used in Java UI/API automation: suites, groups, `@DataProvider`, parallel settings, and a lifecycle that matches setup/teardown of browsers.

You can know both. Interviews expect you to map annotations, not to fight a holy war.

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [TestNG for Automation](testng-for-automation.md) | `@Test`, `@BeforeMethod`, `@AfterMethod`, `@BeforeClass`, `@AfterClass`, `@DataProvider`, `@Parameters`, lifecycle |

## Prerequisite

Part 41. You understand what a test is. TestNG is another runner.

## Maven

```xml
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.10.2</version>
    <scope>test</scope>
</dependency>
```

Surefire can run TestNG tests when TestNG is on the test classpath.
