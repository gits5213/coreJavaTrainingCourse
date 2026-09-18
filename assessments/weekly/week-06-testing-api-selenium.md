# Week 6 — Testing, API, and Selenium

**Coverage:** Parts 41–53 (JUnit, TestNG, HTTP, Java HttpClient, REST Assured, JDBC, Selenium, locators, waits, POM, test data models, data-driven testing, DriverFactory)

**Suggested timebox:** 90 minutes

| Activity | Time |
| --- | --- |
| Quiz (closed book) | 15 minutes |
| Coding assignment | 40 minutes |
| Debugging problem | 20 minutes |
| Explanation exercise | 10 minutes |
| Buffer | 5 minutes |

Score: Theory 20%, Coding 35%, Problem Solving 20%, Debugging 15%, Explanation 10%. Pass bar: 70% weighted.

You may use Javadoc and the JDK. Do not paste a full framework from the internet. Prefer a training URL (example.com or the API your instructor names).

---

## Quiz (10 questions)

1. What is a unit test? Name the three letters of arrange–act–assert.
2. JUnit vs TestNG: when does this course use each? Name `@BeforeMethod`, `@AfterMethod`, and `@DataProvider`.
3. Draw Client → HTTP request → API → service → database → HTTP response. What is the difference between 401, 403, and 404?
4. What do `HttpClient`, `HttpRequest`, and `HttpResponse` represent? Why learn them before REST Assured?
5. REST Assured: what are `given`, `when`, and `then`? Why is status `200` not enough for a real API test?
6. Java → JDBC → driver → database. Name one safety rule for training SQL. Why try-with-resources?
7. Selenium architecture in four boxes (Java test → … → application). Why must you `quit()`?
8. Locator priority: why prefer stable `By.id` over absolute XPath?
9. Why is `Thread.sleep(5000)` a bad default? What should you wait for instead?
10. What belongs in a page object, and what does **not**? Why `DriverFactory.create(BrowserType.CHROME)` instead of `new ChromeDriver()` in every test? Sketch the data-driven progression from hardcoded values to a data factory.

---

## Coding assignment

**Title:** API check + a tiny page object (two parts)

Instructors may assign only Part A if no browser is available, but both parts are the week's bar.

### Part A — API

Using Java `HttpClient` **or** REST Assured:

1. Send GET to a URL the instructor provides (or `https://example.com/`).
2. Assert the status code with `statusMatches(expected, actual)` **or** REST Assured `.statusCode(200)`.
3. If the body is JSON (training API), map one field using Jackson or REST Assured's body path — not `split`.
4. Do not log tokens, passwords, or cookies.

### Part B — UI smoke

1. `DriverFactory.create(BrowserType.CHROME)` (or equivalent). Tests must not call `new ChromeDriver()` directly.
2. A `HomePage` (or `ExamplePage`) with one stable locator and one action (for example `open(url)` + `titleContains` / wait until visible).
3. `WebDriverWait` for a visible element or title. **No** `Thread.sleep` as the wait strategy.
4. TestNG or JUnit: create driver in setup, `quit` in teardown / `finally`.
5. Optional: `record LoginData(String username, String password)` even if the site has no login — show the model. Override `toString` if you would otherwise print a password.

### Acceptance criteria

- [ ] API assertion uses a real status code check.
- [ ] No secrets in logs.
- [ ] Factory (not raw `new ChromeDriver()` in the test).
- [ ] Locator lives in the page class, not in the test.
- [ ] Explicit wait, not sleep.
- [ ] Browser always quits, including on failure.

### Problem-solving stretch

One `@DataProvider` (or JUnit parameterized test) with two rows (for example two expected titles or two query params). Independent rows. Or: GET that sets up data, then UI assert (keep it tiny).

---

## Debugging problem

```java
public class FragileLoginTest {
    WebDriver driver;

    @Test
    public void login() throws Exception {
        driver = new ChromeDriver();
        driver.get("https://training.example/login");
        Thread.sleep(5000);
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/form/div[3]/input")).sendKeys("admin");
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/form/div[4]/input")).sendKeys("secret");
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/form/div[5]/button")).click();
        Thread.sleep(5000);
        assert driver.getTitle().contains("Dashboard");
        System.out.println("token=" + driver.manage().getCookieNamed("SESSION").getValue());
    }
}
```

The test is flaky. Sometimes it cannot find the element. Sometimes it fails on CI but passes locally. The browser stays open when it fails. Security review is unhappy.

### Your job

1. List the defects in priority order. The course asks: **locator first, then wait** — which do you fix first, and why?
2. Rewrite a sketch of `LoginPage` + wait-until-clickable + factory + quit.
3. Say what you would do with the session cookie log line.

### Expected diagnosis

| Defect | Diagnosis |
| --- | --- |
| Absolute XPath | Fragile; any div change breaks the test. Confirm the real id/name/css in DevTools **first**. Sleep will not fix a wrong locator. |
| `Thread.sleep(5000)` | Slow when the page is fast; still racy when a spinner lasts 6 seconds. Wait for **clickable / visible / URL** (state-based). |
| No `quit` / no `finally` | Failed assert skips cleanup; leftover Chrome; Grid slots leak later. |
| `new ChromeDriver()` in the test | Cannot switch Firefox/Grid/options in one place. |
| Logging session cookie | Secret in logs/CI artifacts. Never. |
| Password in source | Use config/vault later; do not print it. |
| Assertion as raw `assert` | Easy to skip if assertions are disabled; use JUnit/TestNG asserts. |

**Order:** identify a stable locator in DevTools, then replace sleep with a wait for that element's state, then factory + teardown, then remove secret logging.

---

## Explanation exercise

**Prompt (90 seconds):**

> Explain state-based synchronization. Then explain Page Object Model in four arrows (Test → Page → Selenium → Browser). Why is JDBC not a method on `LoginPage`?

**Strong answer hits:**

- Wait until a condition is true (visible, clickable, title contains), not until a wall clock says 5 seconds passed.
- POM: tests speak domain (`loginPage.login(data)`); locators and waits live in the page.
- A page is not a database client (SRP). API/DB belong in clients. `DriverFactory` owns browser construction.

---

## Answer key

1. A small, fast check of production code without a browser when possible. Arrange, Act, Assert.
2. JUnit: unit tests in this course. TestNG: common for UI/API automation (lifecycle, DataProvider, parallel, suites). BeforeMethod = setup, AfterMethod = cleanup (quit), DataProvider = rows of data into one `@Test`.
3. 401 unauthenticated, 403 authenticated but not allowed, 404 missing resource. Do not treat them as the same failure.
4. Client, request, response. Libraries hide that pipeline; you still debug 401 at HTTP level.
5. Setup, action, validation. Assert body fields (`"role": "admin"`), not only status.
6. Training DB only; never production; never commit passwords. Close connections even on failure.
7. Java test → WebDriver → browser → app. `quit` frees the process/session. GC will not do it.
8. Ids (when stable) survive layout changes. Absolute XPath is coupled to the full tree.
9. Sleep is slow and still racy. Wait for a state (`ExpectedConditions`, lambda on title, clickable).
10. Locators and UI actions belong in the page. Not DB, not email, not assertions-only soup. Factory: one place for options/Grid. Progression: hardcoded → parameters → DataProvider → model → JSON/CSV → factory.

**Debugging key:** locator before sleep; always quit; never log session tokens.
