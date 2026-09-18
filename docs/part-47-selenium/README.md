# Part 47 — Selenium

**Only after Java fundamentals.** Selenium is not a substitute for `if`, methods, OOP, Maven, or Git. People who skip Java write unmaintainable click scripts.

```text
Java Test
 ↓
Selenium WebDriver
 ↓
Browser
 ↓
Application
```

First concept:

```java
WebDriver driver = new ChromeDriver();
driver.get("https://example.com");
driver.quit();
```

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [Selenium WebDriver](selenium-webdriver.md) | Architecture, ChromeDriver, get, quit, Maven, SDET rules |

## Prerequisite

Java through OOP, Maven, TestNG or JUnit, Git. Locators come next. Do not start with XPath olympics today.

## Maven

```xml
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.21.0</version>
</dependency>
```

Selenium 4+ can manage browser drivers for you (Selenium Manager). You should still `quit`.
