# TestNG for Automation Engineering

## Goal

By the end of this lesson, you will use TestNG annotations:

```text
@Test
@BeforeMethod
@AfterMethod
@BeforeClass
@AfterClass
@DataProvider
@Parameters
```

and trace this lifecycle:

```text
@BeforeMethod
     ↓
Setup
     ↓
@Test
     ↓
Validation
     ↓
@AfterMethod
     ↓
Cleanup
```

## Why It Matters

UI tests need a browser before the test and `quit` after, even when the test fails. That is `@BeforeMethod` / `@AfterMethod`. Data-driven login is `@DataProvider`. Environment URLs are `@Parameters` or, better later, config files.

If you put `new ChromeDriver()` inside every `@Test`, you will forget `quit` and leak processes.

## Real-Life Analogy

A theater.

```text
@BeforeClass     unlock the building once
@BeforeMethod    set the stage before every scene
@Test            perform the scene
@AfterMethod     clear the stage
@AfterClass      lock the building
```

If you only clean the stage on success, a crashed scene leaves a sofa in the way of the next play. `finally`-like teardown is `@AfterMethod`.

## Illustrated Explanation

```text
@BeforeClass        once per test class (expensive shared setup)
    @BeforeMethod   before each @Test
        @Test       the behavior + assertions
    @AfterMethod    after each @Test, including failures
@AfterClass         once per class
```

Curriculum lifecycle (the one to memorize for interviews):

```text
@BeforeMethod
     ↓
Setup
     ↓
@Test
     ↓
Validation
     ↓
@AfterMethod
     ↓
Cleanup
```

Validation lives *in* the `@Test` (asserts). The diagram means: you set up, you act, you assert, you clean.

```text
@Test           the test
@BeforeMethod   per-test setup (driver)
@AfterMethod    per-test cleanup (quit)
@BeforeClass    per-class setup (read config)
@AfterClass     per-class cleanup
@DataProvider   supplies rows of data to a test
@Parameters     injects values from testng.xml
```

## Concept and Syntax

```java
import org.testng.annotations.Test;
import org.testng.Assert;

public class StatusNgTest {

    @Test
    public void shouldMatchOk() {
        Assert.assertTrue(StatusCodes.statusMatches(200, 200));
    }
}
```

TestNG `@Test` methods are typically `public`. JUnit 5 methods can be package-private. Do not mix annotations from both libraries on the same method.

### Before / After

```java
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginLifecycleDemo {

    private String session;

    @BeforeMethod
    public void setUp() {
        session = "open-browser-and-go-to-login";
        System.out.println("Setup: " + session);
    }

    @Test
    public void shouldLoginWithValidUser() {
        System.out.println("Test + validation using " + session);
        Assert.assertNotNull(session);
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Cleanup: quit driver");
        session = null;
    }
}
```

Replace `session` with `WebDriver` when you reach Selenium. Same shape.

```java
@BeforeClass
public void startSuiteResources() {
    System.out.println("Read config once");
}

@AfterClass
public void stopSuiteResources() {
    System.out.println("Close report once");
}
```

Do not share a mutable `WebDriver` field across tests without care. Prefer per-method driver. Class-level driver plus parallel methods is a flake factory.

### @DataProvider

```java
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StatusDataProviderTest {

    @DataProvider(name = "codes")
    public Object[][] codes() {
        return new Object[][] {
                {200, 200, true},
                {200, 404, false},
                {201, 201, true}
        };
    }

    @Test(dataProvider = "codes")
    public void shouldMatchAsExpected(int expected, int actual, boolean matches) {
        Assert.assertEquals(StatusCodes.statusMatches(expected, actual), matches);
    }
}
```

Each row is a separate test invocation. That is data-driven testing (Part 52) in TestNG form.

### @Parameters

`testng.xml`:

```xml
<suite name="qa">
  <parameter name="baseUrl" value="https://qa.example.com"/>
  <test name="smoke">
    <classes>
      <class name="com.training.sdet.ParamDemoTest"/>
    </classes>
  </test>
</suite>
```

```java
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParamDemoTest {

    @Parameters("baseUrl")
    @Test
    public void shouldReadBaseUrl(String baseUrl) {
        Assert.assertTrue(baseUrl.startsWith("https://"));
    }
}
```

Later, configuration classes beat a pile of XML parameters. Learn this because many jobs still use it.

## Simple Example

Full class you can run:

```java
package com.training.sdet;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CalculatorNgTest {

    private Calculator calculator;

    @BeforeMethod
    public void setUp() {
        calculator = new Calculator();
    }

    @Test
    public void shouldAddTwoNumbers() {
        int actual = calculator.add(2, 3);
        Assert.assertEquals(actual, 5);
    }

    @AfterMethod
    public void tearDown() {
        calculator = null;
    }
}
```

Note: TestNG `Assert.assertEquals(actual, expected)` argument order is **actual first**, unlike JUnit. That trips everyone. Read the Javadoc when the message looks backwards.

```text
JUnit 5   assertEquals(expected, actual)
TestNG    Assert.assertEquals(actual, expected)
```

## Real-World Example

Banking smoke:

```text
@BeforeMethod   Chrome + QA URL
@Test           login as teller, assert dashboard
@AfterMethod    quit
```

Cross-environment: `@Parameters("env")` chooses QA vs staging URL. Better: env from a config file. Same lifecycle.

## SDET Example

```java
public class UiShapeTest {

    private WebDriver driver; // when Selenium is on the classpath

    @BeforeMethod
    public void openBrowser() {
        driver = DriverFactory.create(BrowserType.CHROME);
        driver.get("https://example.com/login");
    }

    @Test
    public void shouldRejectEmptyPassword() {
        new LoginPage(driver).enterUsername("john");
        new LoginPage(driver).submit();
        Assert.assertTrue(new LoginPage(driver).errorVisible());
    }

    @AfterMethod
    public void quitBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
```

Always `quit` in `@AfterMethod`. Check null so a failed setup does not hide the original error.

## Break the Code

```java
@BeforeMethod
public void setUp() {
    driver = new ChromeDriver();
}

@Test
public void one() { /* no quit */ }

@Test
public void two() { /* second Chrome, first still alive */ }
```

Missing `@AfterMethod` leaks browsers.

```java
@AfterMethod
public void tearDown() {
    driver.quit();
}
```

If `setUp` failed, `driver` is null → `NullPointerException` masks the real failure. Use the null check.

Mixing JUnit `@Test` and TestNG `@BeforeMethod` on one class: one runner will ignore half the annotations.

## Debug

| Symptom | Cause |
| --- | --- |
| BeforeMethod never runs | You launched with JUnit runner |
| Tests pass in IDEA, not Maven | Surefire not picking TestNG |
| Parameter `baseUrl` is null | Not running via testng.xml |
| DataProvider mismatch | Row column count ≠ method parameters |
| Assert message inverted | TestNG vs JUnit argument order |

IntelliJ: run the class with TestNG. Watch the output order: setup, test, cleanup, setup, test, cleanup.

## Student Exercise

Convert `CalculatorTest` ideas to TestNG: `@BeforeMethod` new calculator, two `@Test` methods (`add`, `subtract`), `@AfterMethod` null out. Run them. Then add a `@DataProvider` for three addition triples.

## Challenge

Write `testng.xml` with a parameter `env=qa`. A test prints it. Document how to run: right-click XML in IntelliJ, or `mvn test` with Surefire suiteXmlFiles (look up the plugin config; adding it is the challenge).

## Knowledge Check

1. List the seven annotations this part teaches.
2. Recite the lifecycle diagram.
3. `@BeforeMethod` vs `@BeforeClass`?
4. Why quit in `@AfterMethod` not only at the end of `@Test`?
5. What does `@DataProvider` return?
6. Where do `@Parameters` values often come from?
7. TestNG vs JUnit `assertEquals` order?
8. Should two parallel `@Test` methods share one driver field?
9. Why is TestNG common in automation shops?
10. What happens if setup fails and teardown does not null-check?

## Interview Question

**Question:** Explain the TestNG test lifecycle.

A strong answer:

> BeforeMethod runs setup, then the Test runs the action and validation, then AfterMethod cleans up. I use BeforeMethod to open a browser and AfterMethod to quit so cleanup happens even when the test fails. BeforeClass and AfterClass run once per class. DataProvider feeds rows of data into a test. Parameters inject values from testng.xml. I still keep tests independent. For unit tests I often use JUnit; for UI suites TestNG's groups and data providers are convenient.

## Homework

Add TestNG to the Maven POM. Write one lifecycle demo that prints the stages in order. Paste the console order into your notes. Next Selenium chapter will put a real driver in those hooks.

---

## Answer Key

1. `@Test` `@BeforeMethod` `@AfterMethod` `@BeforeClass` `@AfterClass` `@DataProvider` `@Parameters`
2. BeforeMethod → Setup → Test → Validation → AfterMethod → Cleanup
3. Per test vs once per class.
4. Tests fail; you still must quit.
5. Usually `Object[][]` rows of arguments.
6. `testng.xml` (or programmatically).
7. TestNG actual then expected; JUnit expected then actual.
8. No. Isolated state (later ThreadLocal).
9. Suites, groups, data providers, parallel config, listeners.
10. Teardown NPE hides the setup error.
