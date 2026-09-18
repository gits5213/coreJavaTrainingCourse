# Waits and Synchronization

## Goal

By the end of this lesson, you will replace `Thread.sleep(5000)` with `WebDriverWait` that waits for a **state** (element visible, clickable, URL contains, text present). You will explain why sleeps make suites slow and still flake.

## Why It Matters

Modern web apps are asynchronous. The button is in the DOM after 200 ms or after 3 seconds. Sleep 5 seconds: you waste 4.8 seconds × thousands of tests. Sleep 1 second: you fail on a slow CI agent.

State-based waits: wait **up to** 10 seconds, continue **as soon as** the condition is true. Fast when the app is fast. Patient when the app is slow. Fail with a timeout if the app is broken.

## Real-Life Analogy

Waiting for a bus.

```text
Thread.sleep(300000)     sit 5 minutes even if the bus arrived at 10 seconds
WebDriverWait            look until the bus is here, or until 5 minutes then call it missing
```

You wait for a **state**: "bus at the stop," not "time passed."

## Illustrated Explanation

```text
BAD
click
sleep 5s
assert dashboard

GOOD
click
wait until dashboard heading visible
assert
```

```text
Implicit wait     driver.manage().timeouts().implicitlyWait(...)
                  findElement retries until timeout
                  easy to overuse; mixes poorly with explicit waits

Explicit wait     WebDriverWait + condition
                  this course's default tool

Fluent wait       explicit wait with polling tweaks
```

Prefer **explicit** waits on the states you care about. Avoid stacking large implicit + explicit (confusing timeouts).

Curriculum:

```text
Never default to Thread.sleep(5000)
Teach WebDriverWait
Teach state-based synchronization
```

## Syntax / Concept

```java
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
```

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));
```

Clickable:

```java
wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button"))).click();
```

URL:

```java
wait.until(ExpectedConditions.urlContains("/dashboard"));
```

Custom state (lambda):

```java
wait.until(d -> d.findElement(By.id("status")).getText().equals("READY"));
```

That is state-based: **READY**, not "enough milliseconds."

If you must sleep (file download, animation with no hook), isolate it, comment why, keep it short. Default is still: no.

## Simple Example

```java
WebDriver driver = new ChromeDriver();
try {
    driver.get("https://example.com");
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1")));
    System.out.println(driver.findElement(By.cssSelector("h1")).getText());
} finally {
    driver.quit();
}
```

Login shape:

```java
wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button"))).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dashboard")));
```

## Real-World Example

Banking transfer: wait until confirmation number is visible, not sleep 8 seconds hoping the ledger committed.

E-commerce search: wait until product grid has at least one card, or until "no results" is visible. Those are two valid states. Assert which one.

## SDET Example

```java
public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void login(String user, String pass) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys(user);
        driver.findElement(By.id("password")).sendKeys(pass);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button"))).click();
    }
}
```

Timeouts belong in config later (`explicitWaitSeconds=10`). Do not scatter 7, 10, 15, 30 randomly.

## Break the Code

```java
driver.findElement(By.id("login-button")).click();
Thread.sleep(5000);
assertTrue(driver.findElement(By.id("dashboard")).isDisplayed());
```

Works on a fast laptop. Fails on CI. Wastes time when the dashboard was ready in 200 ms.

```java
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
new WebDriverWait(driver, Duration.ofSeconds(30)).until(...);
```

You can wait far longer than you think. Tests look "hung."

## Debug

`TimeoutException`: the state never happened. Causes: bad locator, app bug, spinner covering the button, need `invisibilityOf` spinner first, iframe.

Do not increase timeout to 60 as the first fix. Prove the element exists in DevTools. Wait for spinner gone:

```java
wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".spinner")));
```

## Student Exercise

Rewrite any Selenium code you have: remove sleeps. Add `WebDriverWait` for presence of `h1` on example.com. Time the test mentally: it should finish almost immediately, not after 5 seconds.

## Challenge

Write a helper `waitVisible(By locator)` on a small `Waiter` class. Use it twice. Explain why this is DRY and still KISS (one class, not a framework).

## Knowledge Check

1. Why is `Thread.sleep(5000)` a bad default?
2. What is state-based synchronization?
3. What class is the explicit wait?
4. Name two `ExpectedConditions`.
5. Implicit vs explicit?
6. Why mix them carefully?
7. What exception means the state never arrived?
8. Should you jump to 60s timeouts first?
9. Where should timeout values live long-term?
10. Sleep ever allowed?

## Interview Question

**Question:** How do you handle synchronization in Selenium?

A strong answer:

> I never default to Thread.sleep(5000). Sleeps make suites slow and still flake when CI is slower than the sleep. I use WebDriverWait with ExpectedConditions: visible, clickable, URL contains, text present. That is state-based synchronization: wait until the app is in the right state, up to a timeout. I put waits in page objects. Timeouts come from config. If I timeout, I debug locator, spinner, iframe — I do not blindly sleep longer.

## Homework

Delete sleeps from your training UI code. If a test fails, fix it with a named condition. Commit `Replace sleeps with WebDriverWait`.

---

## Answer Key

1. Slow when app is fast; still races when app is slower than 5s.
2. Wait until a condition is true, not until a clock fires.
3. `WebDriverWait`
4. visibilityOfElementLocated, elementToBeClickable (others: urlContains, textToBePresent...)
5. Global findElement retry vs targeted condition.
6. Timeouts add up; behavior is hard to reason about.
7. `TimeoutException`
8. No
9. Configuration
10. Rare, isolated, commented; never the default.
