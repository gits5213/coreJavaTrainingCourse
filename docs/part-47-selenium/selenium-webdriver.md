# Selenium WebDriver

## Goal

By the end of this lesson, you will explain the Java → WebDriver → Browser → Application path, open Chrome, load a URL, and quit. You will treat Selenium as a library, not as a career identity.

## Why It Matters

SDET job posts say Selenium. Used well, it checks what a user sees. Used as the only tool, it makes slow, flaky pipelines.

You needed Java first because every line is still Java: objects, exceptions, waits, design.

## Real-Life Analogy

A remote control.

```text
You (Java test) press buttons
Remote (WebDriver) speaks the browser's protocol
TV (browser) shows the app
Show (application) is what users watch
```

If you throw the remote at the TV (`Thread.sleep` and random clicks), the show still might play. You will not know why it failed on Tuesday.

## Illustrated Explanation

```text
Java Test
 ↓
Selenium WebDriver
 ↓
Browser
 ↓
Application
```

```text
@Test
  new ChromeDriver()     starts Chrome
  driver.get(url)        navigates
  findElement...         later chapter
  driver.quit()          closes browser + session
```

`close()` vs `quit()`: `close` closes a window; `quit` ends the session. In tests, **quit** in teardown.

```text
Without quit
  Chrome processes pile up
  CI machines die
```

Selenium Manager (Selenium 4.6+): downloads a matching driver. Still keep browsers reasonably up to date.

## Syntax / Concept

```java
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
```

```java
WebDriver driver = new ChromeDriver();
driver.get("https://example.com");
String title = driver.getTitle();
driver.quit();
```

`WebDriver` is the interface. `ChromeDriver` is a concrete browser. Later `DriverFactory` returns `WebDriver` so tests do not mention Chrome.

Always quit in `finally` or `@AfterMethod`:

```java
WebDriver driver = new ChromeDriver();
try {
    driver.get("https://example.com");
} finally {
    driver.quit();
}
```

## Simple Example

```java
package com.training.sdet;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class OpenExampleCom {

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("https://example.com");
            System.out.println(driver.getTitle());
        } finally {
            driver.quit();
        }
    }
}
```

TestNG shape:

```java
public class OpenExampleComTest {

    @Test
    public void shouldLoadExampleDotCom() {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("https://example.com");
            Assert.assertTrue(driver.getTitle().toLowerCase().contains("example"));
        } finally {
            driver.quit();
        }
    }
}
```

## Real-World Example

Banking: open QA login URL, later type credentials from a vault, never from Git. Today you only open the URL and assert title or URL contains `login`.

E-commerce: open home page, assert title. Tiny smoke. Valuable on CI.

## SDET Example

```text
Java Test
  → Selenium WebDriver
      → Chrome
          → https://example.com
```

This is a smoke that CI can run: "browser can start and network can reach the app." It is not a substitute for API tests of business rules.

```text
BAD
Selenium as first Java project
Copy 200 lines from a blog including Thread.sleep(5000)

GOOD
Java fundamentals first
get + quit
then locators, waits, POM
```

## Break the Code

```java
WebDriver driver = new ChromeDriver();
driver.get("https://example.com");
// forgot quit
```

```java
driver.get("example.com"); // missing scheme
```

```java
driver.close();
driver.getTitle(); // session may be dead
```

```java
@Test
void t() {
    new ChromeDriver();
}
```

Browser opens, test "passes," Chrome stays. Not a test.

## Debug

| Symptom | Check |
| --- | --- |
| `session not created` | Chrome vs driver mismatch; update browser / Selenium |
| blank page | VPN, wrong env URL, app down |
| `NoSuchSessionException` | quit too early; using driver after quit |
| CI cannot open Chrome | need headless options later; display in Linux CI |

Do not "fix" with random sleeps yet. Next waits chapter is the real fix for timing.

## Student Exercise

Add `selenium-java` to Maven. Write `OpenExampleCom` with try/finally quit. Print the title. Then convert to a TestNG or JUnit test with an assertion on title.

## Challenge

Add Chrome options for headless (`--headless=new`) behind a boolean `HEADLESS=true` environment variable. Default headed on your laptop. CI will thank you later. Do not add Grid.

## Knowledge Check

1. Draw Java Test → ... → Application.
2. Write the three-line first concept (new, get, quit).
3. Why `quit` in `finally` / AfterMethod?
4. `WebDriver` vs `ChromeDriver`?
5. Why Java before Selenium?
6. `close` vs `quit`?
7. Is opening example.com enough to prove login?
8. What does Selenium Manager help with?
9. Where should this code live: main vs test?
10. What will locators add next?

## Interview Question

**Question:** How does Selenium WebDriver work?

A strong answer:

> A Java test talks to Selenium WebDriver, which controls a browser, which renders the application. I create a ChromeDriver, call get with a URL, then quit in teardown so sessions do not leak. WebDriver is the interface; ChromeDriver is one implementation. I learned Java first. Selenium is not my only tool: APIs often prove business rules cheaper. Next I add locators, waits, and page objects. I never default to Thread.sleep.

## Homework

Green test: open example.com, assert title, quit. Commit `Add Selenium smoke that opens example.com`. Watch Task Manager/Activity Monitor: Chrome should disappear after the test.

---

## Answer Key

1. Java Test → Selenium WebDriver → Browser → Application
2. `new ChromeDriver(); driver.get("https://example.com"); driver.quit();`
3. Exceptions skip later lines; browsers leak.
4. Interface vs Chrome implementation.
5. Otherwise you automate without being able to design or debug.
6. Window vs entire session.
7. No. Only that navigation works.
8. Matching browser drivers.
9. Tests in `src/test/java`; later pages in main.
10. Finding elements to type and click.
