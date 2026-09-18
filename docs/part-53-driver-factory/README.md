# Part 53 — Driver Factory

Tests should not construct browsers. They should ask.

```text
Test
 ↓
DriverFactory
 ↓
ChromeDriver
or
FirefoxDriver
or
EdgeDriver
```

```java
WebDriver driver = DriverFactory.create(BrowserType.CHROME);
```

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [Driver Factory](driver-factory.md) | BrowserType, factory, options, config, quit |

## Prerequisite

Selenium, Factory pattern conceptually (Part 40), YAGNI (do not add Grid here).
