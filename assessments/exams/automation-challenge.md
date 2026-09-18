# Automation Challenge

**Level:** after Parts 47–53 (Selenium, locators, waits, POM, data, DriverFactory)  
**Format:** IDE, browser, training site named by the instructor  
**Timebox:** 45–60 minutes

Suggested weight in the [final exam](final-exam-guide.md): **15%**.

---

## Prompt

Automate **one** small UI flow on the training site (example: open home page, click a visible link, assert a heading or title). This is not a 50-page regression pack.

### You must include

1. **DriverFactory** — tests do not `new ChromeDriver()`.
2. **Page object** — one page class with a **stable** locator (`By.id`, `By.name`, or a short CSS selector). No absolute XPath unless you can defend that the app has no better hook.
3. **WebDriverWait** — wait until clickable or visible. **No** default `Thread.sleep`.
4. **Assertion** of a visible result (title, heading text, URL).
5. **`quit`** in `finally` or `@AfterMethod` / JUnit `@AfterEach`. Failure must still close the browser.

### Optional stretch

- One API setup (create data) + UI assert (integration).
- `record LoginData(...)` or a factory for unique data if the flow is login.
- TestNG `@DataProvider` with two independent rows.

---

## Suggested structure

```text
src/test/java/.../tests/HomeSmokeTest.java
src/main/java/.../pages/HomePage.java
src/main/java/.../driver/DriverFactory.java
src/main/java/.../driver/BrowserType.java
```

### Factory sketch

```java
public final class DriverFactory {
    public static WebDriver create(BrowserType type) {
        return switch (type) {
            case CHROME -> new ChromeDriver();
            case FIREFOX -> new FirefoxDriver();
            case EDGE -> new EdgeDriver();
        };
    }
}
```

(A classic `if` is fine if the student has not used switch expressions.)

### Page sketch

```java
public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By heading = By.tagName("h1"); // replace with a stable locator for the real site

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public HomePage open(String url) {
        driver.get(url);
        return this;
    }

    public String headingText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(heading)).getText();
    }
}
```

### Test sketch

```java
public class HomeSmokeTest {
    private WebDriver driver;

    @BeforeMethod
    public void start() {
        driver = DriverFactory.create(BrowserType.CHROME);
    }

    @AfterMethod
    public void stop() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void headingIsVisible() {
        HomePage home = new HomePage(driver);
        home.open("https://example.com/");
        String text = home.headingText();
        Assert.assertTrue(text != null && !text.isBlank());
    }
}
```

Use the real assertion your instructor specifies (exact title string).

---

## Anti-patterns (automatic deductions)

- `Thread.sleep(5000)` as the wait
- Absolute `/html/body/div[1]/...` without a documented reason
- Locators in the test class
- No `quit`
- `LoginPage` that also opens JDBC or sends email
- Passwords printed to console
- Singleton static `WebDriver`

---

## Acceptance criteria

- [ ] Factory used
- [ ] POM used
- [ ] Explicit wait used
- [ ] Assertion on a user-visible result
- [ ] Browser quits on pass and fail
- [ ] Student can explain why sleep is the wrong default (60 seconds)

---

## Rubric (15 points)

| Look for | Points |
| --- | --- |
| Factory + quit | 4 |
| Page object + stable locator | 4 |
| Wait (not sleep) | 3 |
| Correct assertion | 3 |
| Clean structure / no secrets | 1 |

Optional stretch: +0–2 instructor bonus, not required to pass.
