# Selenium Locators

## Goal

By the end of this lesson, you will use `By.id`, `By.name`, `By.cssSelector`, `By.xpath`, and `By.linkText`, and you will reject locators that break when a designer moves a `<div>`.

## Why It Matters

Flaky UI tests are often locator tests. The product works. Your XPath pointed at the third `div` of the fifth `table`. A banner ad added a `div`. You failed CI.

Stable locators are a design conversation with developers: **test ids** (`data-testid`) are a gift. Beg for them.

## Real-Life Analogy

Finding a person.

```text
id           passport number          best if unique and stable
name         name on a form           good for inputs
css          "blue coat, third floor" ok if the coat is a class meant to last
xpath        "left of the plant"      brittle if furniture moves
linkText     "click the word Login"   good for real visible links
```

Absolute XPath is "start at the city, second street, fourth house, third window." A new street lamp and you are lost.

## Illustrated Explanation

```text
HTML
<input id="username" name="user" />

Java
driver.findElement(By.id("username"))
```

```text
Prefer
  1. id (stable, unique)
  2. name / data-testid / aria
  3. cssSelector (classes that are semantic, not generated)
  4. xpath (relative, not absolute)
  5. linkText / partialLinkText for links
```

Avoid:

```text
absolute XPath
generated IDs
fragile CSS chains
visual position
```

Examples of avoid:

```text
/html/body/div[2]/div[3]/form/input[1]     absolute XPath
id="ember-283"                             generated
body > div > div > div > input             fragile CSS
(//button)[last()-2]                       visual/index games
```

Better:

```text
By.id("username")
By.cssSelector("[data-testid='login-submit']")
By.xpath("//button[@type='submit' and text()='Sign in']")
```

## Syntax / Concept

```java
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
```

```java
WebElement user = driver.findElement(By.id("username"));
WebElement named = driver.findElement(By.name("user"));
WebElement css = driver.findElement(By.cssSelector("#username"));
WebElement xp = driver.findElement(By.xpath("//input[@id='username']"));
WebElement link = driver.findElement(By.linkText("Forgot password?"));
```

`findElement` throws if missing. `findElements` returns a list (empty if missing) — useful for "is it here?" without try/catch.

CSS vs XPath: CSS cannot climb to a parent as easily; XPath can. Do not use that power to write novels. Prefer attributes.

## Simple Example

```java
WebDriver driver = new ChromeDriver();
try {
    driver.get("https://example.com");
    // example.com is mostly a link:
    WebElement more = driver.findElement(By.linkText("More information..."));
    System.out.println(more.getTagName());
} finally {
    driver.quit();
}
```

A training login page (conceptual):

```java
driver.findElement(By.id("username")).sendKeys("standard_user");
driver.findElement(By.id("password")).sendKeys("secret");
driver.findElement(By.id("login-button")).click();
```

Passwords: do not print. `sendKeys` is enough.

## Real-World Example

Banking login: ids `username`, `password`, `sign-in`. If the bank uses generated Angular ids, ask for `data-testid="login-username"`.

E-commerce product: `By.cssSelector("[data-product-id='sku-44']")` beats "third card in the grid."

## SDET Example

```java
public class LoginFields {
    static final By USERNAME = By.id("username");
    static final By PASSWORD = By.id("password");
    static final By SUBMIT = By.cssSelector("[data-testid='login-submit']");
}
```

These constants will move into `LoginPage` next part. The stability rules stay.

```text
BAD
By.xpath("/html/body/div[1]/div[2]/input")

GOOD
By.id("username")
```

## Break the Code

```java
By.xpath("/html/body/div[1]/form/input[2]");
```

A new cookie banner adds a div. Test dies.

```java
By.id("username_12345");
```

Copied from a session where the id was generated.

```java
By.cssSelector("div:nth-child(3) > input");
```

Visual position.

## Debug

`NoSuchElementException`: locator wrong, or element not in DOM yet (waits next), or inside iframe (switchTo frame), or shadow DOM (advanced).

Debug steps:

1. Copy locator into browser DevTools. `$('#username')` or XPath search.
2. If it works in DevTools and fails in Selenium, it is probably **timing** or **iframe**.
3. Do not immediately write a longer XPath.

## Student Exercise

On a practice site your instructor names (or a static HTML file you write), locate one input with id, one with name, one button with css, one link with linkText. Print tag names. Quit.

## Challenge

Write two locators for the same button: one bad (absolute XPath) and one good (`data-testid` or id). Explain in comments why the bad one fails a redesign.

## Knowledge Check

1. List the five `By` methods taught.
2. What should you prefer?
3. List four things to avoid.
4. `findElement` vs `findElements`?
5. Why are generated IDs bad?
6. Why is absolute XPath bad?
7. What is `data-testid` for?
8. When is `linkText` appropriate?
9. If DevTools finds it but Selenium does not, what next?
10. Where will locators live after POM?

## Interview Question

**Question:** How do you choose Selenium locators?

A strong answer:

> I prefer stable selectors: id, name, data-testid, then semantic CSS, then relative XPath. I use By.id, By.name, By.cssSelector, By.xpath, By.linkText. I avoid absolute XPath, generated IDs, fragile CSS chains, and visual position like nth-child. If the app has random ids I ask developers for test ids. NoSuchElement is often a wait or iframe, not a reason to write a longer XPath.

## Homework

Save a screenshot of DevTools showing a good locator (not a password value). Add constants for locators in a class. Do not commit credentials.

---

## Answer Key

1. id name cssSelector xpath linkText
2. Stable unique attributes
3. Absolute XPath, generated IDs, fragile CSS, visual position
4. One element or throw vs list (maybe empty)
5. They change every build/session
6. Any DOM shift breaks the path from html/body
7. Stable hooks for automation without using CSS cosmetics
8. Visible link text that is a real user-facing string (watch i18n)
9. Wait, iframe, timing
10. Page objects
