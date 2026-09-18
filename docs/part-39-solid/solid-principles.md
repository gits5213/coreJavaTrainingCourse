# SOLID Principles

## Goal

By the end of this lesson, you will explain each SOLID letter with a small Java example, and you will split a God-object `LoginPage` into `LoginPage`, `UserApiClient`, `UserFactory`, and `ScreenshotService`.

## Why It Matters

Automation frameworks rot when one class knows everything. Changes to email break login tests. Database downtime fails UI smoke. SOLID is how you keep change local.

Architect interviews: "How would you structure page objects?" They are asking S, I, and D whether they use the letters or not.

## Real-Life Analogy

A restaurant.

```text
S   the chef does not also do taxes and plumbing
O   you add a new dessert without rewriting the menu software
L   a substitute chef can still cook the same tickets
I   waiters are not forced to implement "repair the freezer"
D   the restaurant depends on "a payment terminal," not "exactly Brand X cable"
```

## Illustrated Explanation

```text
S  one class, one reason to change
O  add behavior by adding code, not by rewriting working classes
L  subtypes must honor the parent's contract
I  small interfaces, not fat "do everything" interfaces
D  depend on abstractions; high-level policy should not depend on JDBC details
```

God object (curriculum BAD):

```text
LoginPage
  locators
  database
  API requests
  test data
  screenshots
  email
  logging
```

Better (curriculum GOOD):

```text
LoginPage          UI behavior
UserApiClient      API operations
UserFactory        data
ScreenshotService  screenshots
```

```text
Test
  ├─ LoginPage.login(user)
  ├─ UserApiClient.create(user)
  ├─ UserFactory.admin()
  └─ ScreenshotService.capture(driver)
```

## S — Single Responsibility

A class should have one reason to change.

```java
// BAD — two reasons to change: calculation policy AND printing format
public class Paycheck {
    public double netPay(double hours, double rate) {
        return hours * rate;
    }

    public void printToConsole(double net) {
        System.out.println("NET=" + net);
    }
}

// GOOD
public class PayCalculator {
    public double netPay(double hours, double rate) {
        return hours * rate;
    }
}

public class PaycheckPrinter {
    public void print(double net) {
        System.out.println("NET=" + net);
    }
}
```

SDET S: `LoginPage` changes when the login *UI* changes. `UserApiClient` changes when the *API* changes. `UserFactory` changes when *test data rules* change.

## O — Open/Closed

Open for extension, closed for modification. You add a new notifier without editing a `switch` in a core class every time.

```java
public interface StatusNotifier {
    void notify(String message);
}

public class ConsoleNotifier implements StatusNotifier {
    @Override
    public void notify(String message) {
        System.out.println(message);
    }
}

public class SlackNotifier implements StatusNotifier {
    @Override
    public void notify(String message) {
        System.out.println("Slack: " + message); // stand-in
    }
}

public class TestReporter {
    private final StatusNotifier notifier;

    public TestReporter(StatusNotifier notifier) {
        this.notifier = notifier;
    }

    public void passed(String testName) {
        notifier.notify("PASSED " + testName);
    }
}
```

Adding email later is a new class, not surgery on `TestReporter`.

SDET O: `DriverFactory.create(BrowserType type)` uses a strategy/switch you can extend with `EDGE` without rewriting tests. Prefer a map or polymorphism over a 40-case switch as the family grows.

## L — Liskov Substitution

Subtypes must be usable wherever the parent is expected. If `Bird` has `fly()`, `Penguin` should not explode.

```java
public abstract class TestUser {
    public abstract String role();
}

public class AdminUser extends TestUser {
    @Override
    public String role() {
        return "ADMIN";
    }
}

public class GuestUser extends TestUser {
    @Override
    public String role() {
        return "GUEST";
    }
}
```

A method `void openAdminPanel(TestUser user)` that assumes every `TestUser` can open admin **violates L** for `GuestUser`. Fix the abstraction: `AdminUser` only, or a capability interface `AdminAccess`.

SDET L: `RemoteWebDriver` should be usable where `WebDriver` is expected. If your wrapper throws on `quit()` for one subtype, tests cannot share teardown.

## I — Interface Segregation

Do not force clients to depend on methods they do not use.

```java
// BAD fat interface
public interface Worker {
    void type(String locator, String value);
    void click(String locator);
    void query(String sql);
    void sendEmail(String to, String body);
}

// GOOD
public interface UiActor {
    void type(String locator, String value);
    void click(String locator);
}

public interface DatabaseChecker {
    void query(String sql);
}
```

`LoginPage` should not implement `sendEmail`.

## D — Dependency Inversion

High-level code depends on abstractions, not on concrete JDBC or Gmail.

```java
public interface UserDirectory {
    boolean exists(String username);
}

public class JdbcUserDirectory implements UserDirectory {
    @Override
    public boolean exists(String username) {
        return false; // stand-in for a real query
    }
}

public class LoginVerifier {
    private final UserDirectory users;

    public LoginVerifier(UserDirectory users) {
        this.users = users;
    }

    public boolean canLogin(String username) {
        return users.exists(username);
    }
}
```

Tests inject a fake `UserDirectory`. That is why D pairs with unit testing.

SDET D: tests depend on `LoginPage` and `UserApiClient` interfaces or narrow classes, not on `ChromeDriver` constructed in every test. Factories invert the dependency on browser details.

## Practical Java: Split the God Page

```java
public class LoginPage {
    private final WebDriver driver;
    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By submit = By.id("login");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String user, String pass) {
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        driver.findElement(submit).click();
    }
}
```

```java
public class UserApiClient {
    public void createUser(String username, String role) {
        System.out.println("POST /users " + username + " " + role);
    }
}
```

```java
public class UserFactory {
    public static String uniqueUsername(String prefix) {
        return prefix + System.currentTimeMillis();
    }
}
```

```java
public class ScreenshotService {
    public void capture(WebDriver driver, String label) {
        System.out.println("Screenshot: " + label);
        // later: TakesScreenshot
    }
}
```

Logging and email are their own types too. They are not fields stuffed into `LoginPage`.

## Student Exercise

List every responsibility in this fake class, then draw the split:

```java
public class LoginPage {
    public void login() {}
    public void insertUserInDb() {}
    public void createUserViaApi() {}
    public void loadCsv() {}
    public void screenshot() {}
    public void emailReport() {}
    public void log() {}
}
```

Write empty Java classes with one-sentence JavaDoc each.

## Challenge

Take `PayCalculator` / `PaycheckPrinter` and add a `CsvPaycheckWriter` without modifying `PayCalculator`. Which SOLID letters did you use?

## Knowledge Check

1. Spell SOLID.
2. What is wrong with the God `LoginPage`?
3. Name the four better types from the curriculum.
4. Open/Closed in one sentence.
5. Give a Liskov violation in testing.
6. What is a fat interface?
7. Why inject `UserDirectory` instead of calling JDBC in `LoginVerifier`?
8. Does SOLID mean more files are always better?
9. Which letter is the God object mainly violating?
10. How does D help unit tests?

## Interview Question

**Question:** Explain SOLID with an SDET example.

A strong answer:

> SOLID is five design rules. Single Responsibility: LoginPage only drives login UI. A page that also does database, API, test data, screenshots, email, and logging has too many reasons to change. Better: LoginPage, UserApiClient, UserFactory, ScreenshotService. Open/Closed: add a browser type without rewriting tests. Liskov: any WebDriver subtype must still quit cleanly. Interface Segregation: do not force pages to implement email. Dependency Inversion: tests depend on abstractions and factories, not new ChromeDriver() in every class. I apply these after I understand OOP, not as wallpaper.

## Homework

Refactor any training class that both prints and calculates, or both clicks and queries. Open a PR that names the SOLID letter in the description.

---

## Answer Key

1. Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion.
2. Too many responsibilities; unrelated failures; painful change.
3. LoginPage, UserApiClient, UserFactory, ScreenshotService.
4. Extend by adding code; do not keep editing a closed working module for every new variant.
5. GuestUser passed where admin-only behavior is assumed; wrapper that cannot `quit`.
6. An interface with unrelated methods clients must stub.
7. High-level logic should not depend on a concrete database; tests can fake the directory.
8. No. Empty ceremony classes violate KISS/YAGNI.
9. S (also I and D in practice).
10. You replace concrete IO with fakes through the abstraction.
