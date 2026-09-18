# Page Object Model

## Goal

By the end of this lesson, you will write a `LoginPage` that owns locators and UI actions, keep assertions and data out of the page (or keep only page-level queries like `isErrorVisible`), and keep tests readable.

## Why It Matters

When the username id changes, you want **one** edit. Without POM, you grep 40 tests. You will miss one. That is the flake named "only login test 17 fails."

POM is DRY for UI. It is not a religion. A page that emails and queries SQL violates Single Responsibility.

## Real-Life Analogy

A TV remote is a page object. You press `volumeUp`. You do not rewire the television from the sofa every time.

The test is the viewer: "watch the news." The page is the remote: "press these buttons." Selenium is infrared. The browser is the TV.

## Illustrated Explanation

Without POM:

```text
Test
+
Locators
+
Actions
+
Assertions
+
Data
```

With POM:

```text
Test
 ↓
Page Object
 ↓
Selenium
 ↓
Browser
```

```text
LoginTest
   loginPage.enterUsername(...)
   loginPage.enterPassword(...)
   loginPage.submit()
   assert dashboard.isLoaded()
```

## Syntax / Concept

Curriculum example (type this; it is the heart of the part):

```java
public class LoginPage {

    private WebDriver driver;

    private By username = By.id("username");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String value) {
        driver.findElement(username).sendKeys(value);
    }
}
```

Grow it with password, submit, and waits:

```java
public void enterPassword(String value) {
    driver.findElement(password).sendKeys(value);
}

public void submit() {
    wait.until(ExpectedConditions.elementToBeClickable(submit)).click();
}

public void login(String user, String pass) {
    enterUsername(user);
    enterPassword(pass);
    submit();
}
```

Returning the next page (`return new DashboardPage(driver)`) is a nice fluent style. Do not force it if navigation is uncertain.

Pages do **not** own: JDBC, REST create-user, Excel dumps, SMTP, global logging frameworks (a debug line is fine).

## Simple Example

```java
public class LoginPage {
    private final WebDriver driver;
    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By submit = By.id("login-button");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String value) {
        driver.findElement(username).sendKeys(value);
    }

    public void enterPassword(String value) {
        driver.findElement(password).sendKeys(value);
    }

    public void submit() {
        driver.findElement(submit).click();
    }
}
```

```java
@Test
void shouldLogin() {
    WebDriver driver = new ChromeDriver();
    try {
        driver.get("https://qa.example.com/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword(passFromVault());
        loginPage.submit();
        // assert next — DashboardPage
    } finally {
        driver.quit();
    }
}
```

## Real-World Example

Banking: `TransferPage.enterAmount`, `selectFromAccount`. Tests read like the user journey.

E-commerce: `CartPage.checkout()`. If checkout clicks 4 buttons, the page method still has one name.

## SDET Example

BAD test:

```java
driver.findElement(By.id("username")).sendKeys("x");
driver.findElement(By.id("password")).sendKeys("y");
driver.findElement(By.id("login-button")).click();
```

GOOD test:

```java
new LoginPage(driver).login("standard_user", password);
```

GOOD split (SOLID reminder):

```text
LoginPage          UI
UserApiClient      API
UserFactory        data
ScreenshotService  evidence
```

## Break the Code

```java
public class LoginPage {
    public void login() {
        // locators, API create user, SQL, screenshot, email
    }
}
```

That is the God page again.

```java
public class LoginPage {
    public static WebDriver driver; // global
}
```

Parallel tests will collide. Pass driver in the constructor.

## Debug

If a page method fails, the stack trace should show `LoginPage.enterUsername`. That is a benefit: you know which screen. If everything is in the test, traces are a wall of `findElement`.

If tests still duplicate login, you did not actually use the page.

## Student Exercise

Type the curriculum `LoginPage` with `enterUsername`. Add `enterPassword` and `submit`. Write a test that calls them (site or local HTML). Quit in finally.

## Challenge

Add `login(user, pass)` that calls the three methods. Add `DashboardPage.isLoaded()` using a wait. Test asserts `isLoaded()`. Do not add API calls to `LoginPage`.

## Knowledge Check

1. What lives in a test without POM?
2. Draw Test → Page → Selenium → Browser.
3. Who owns `By.id("username")`?
4. Why constructor injection of `WebDriver`?
5. What should not go in `LoginPage`?
6. Why `login()` plus smaller methods?
7. Is POM mandatory on day 1 of one test?
8. How does POM help a locator change?
9. Static driver on the page — why bad?
10. POM vs God object?

## Interview Question

**Question:** What is the Page Object Model?

A strong answer:

> POM puts locators and UI actions in a page class so tests stay about behavior. Test talks to LoginPage, which talks to Selenium, which talks to the browser. Without POM the test mixes locators, actions, asserts, and data. LoginPage has By.id username and enterUsername(value) using sendKeys. I inject WebDriver. I do not put database, API, email, or screenshots in the page. When the id changes I edit one class. It is DRY for UI, not a dump of every framework service.

## Homework

Move any raw `findElement` in tests into a page class. Commit `Introduce LoginPage page object`.

---

## Answer Key

1. Locators, actions, asserts, data
2. Test → Page Object → Selenium → Browser
3. The page object
4. Isolated, testable, no global session
5. DB, API, email, test data factories, screenshot services
6. Readable tests and reusable parts
7. YAGNI says extract when duplication appears; even test two benefits from a page
8. One edit
9. Shared mutable browser
10. POM is a UI layer; God object mixes layers
