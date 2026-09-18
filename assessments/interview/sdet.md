# SDET Interview

Questions from Parts 32–53 (debugging, Maven, Git, clean code, SOLID, patterns, JUnit, TestNG, HTTP, HttpClient, REST Assured, JDBC, Selenium, locators, waits, POM, data, DriverFactory), with ThreadLocal/CI appearing because jobs will ask. Model answers a student can study, then speak.

---

### How do you know you learned a topic?

**Model answer:** I can quiz myself, write code, debug a broken sample, and explain it out loud. Our course assesses weekly that way: theory 20, coding 35, problem solving 20, debugging 15, explanation 10. Nodding through a chapter is not evidence.

---

### What is a bug, and how do you debug in IntelliJ?

**Model answer:** A bug is when the program behaves differently from what I intended. The computer did what the code said. Flow: run → breakpoint → pause → inspect → understand → fix. I read the stack trace from the top. I do not change five lines at once. A failing test is a product bug *or* a test bug; the debugger works on test code too — I inspect expected versus actual on the assertion.

---

### Why Maven? What is in `pom.xml`? What is the standard layout?

**Model answer:** A real SDET project needs JUnit, TestNG, Selenium, REST Assured, drivers. Maven downloads those from `pom.xml` (Project Object Model): `groupId`, `artifactId`, version, properties, dependencies, plugins. Layout is `src/main/java` for production/framework code and `src/test/java` for tests. `mvn clean compile test package verify` is the lifecycle I can name. CI runs `mvn test`; the green triangle in IntelliJ is not the team workflow. This course teaches Maven first because most Java automation jobs still speak it. Gradle exists; I am not at war with it.

---

### Git versus GitHub?

**Model answer:** Git is version control on my computer: snapshots, branches, `status`, `add`, `commit`. GitHub hosts the shared repository. Workflow: local repo → GitHub → pull request → code review → CI → merge. A test that is not in Git does not exist for the team. "I fixed it locally" is not a delivery.

---

### What is clean code for tests?

**Model answer:** Names a teammate can read at 8 a.m. `createTestUser(username)` not `doIt(x)`. Small methods, little duplication, one job per method. Test names are documentation. Clean code is not clever code.

---

### DRY, KISS, YAGNI?

**Model answer:** Don't Repeat Yourself — login steps live in one method or page. Keep It Simple — a readable `if` beats a puzzle. You Aren't Gonna Need It — I do not create a complex framework for problems I do not have. Grid, twelve-layer data platforms, and custom annotation processors wait until pain is real.

---

### SOLID, with a LoginPage example?

**Model answer:**

- **S**ingle responsibility: a `LoginPage` that also does database, API, screenshots, email, and logging is a design failure. Split clients and reporters.
- **O**pen/closed: add a new `BrowserType` in the factory without rewriting every test.
- **L**iskov: a subclass should be usable where the parent is expected; do not surprise callers.
- **I**nterface segregation: do not force pages to implement a giant `KitchenSink` interface.
- **D**ependency inversion: tests depend on `WebDriver` and factories, not on `new ChromeDriver()` everywhere.

---

### Design patterns you actually type?

**Model answer:** **Factory:** `DriverFactory.create(BrowserType.CHROME)`. **Builder:** `UserBuilder` when constructors grow too many parameters. **Strategy:** different waits or browsers behind one API. **Facade:** a workflow that hides a multi-step journey. **Adapter:** wrapping a third-party client. **Observer:** listeners for reporting. **Singleton:** often overused. A singleton `WebDriver` is a shared session and a race. I am suspicious of it.

---

### JUnit — what is a unit test?

**Model answer:** A unit test checks a small piece of production code without a browser or network if I can help it. Arrange, act, assert. `@Test`, `assertEquals(expected, actual)` in JUnit 5. If I cannot unit test `statusMatches` or a calculator, I cannot honestly unit test a `UserFactory`. UI tests are expensive; unit tests are cheap truth. `mvn test` runs them.

---

### TestNG — why do automation teams use it?

**Model answer:** Suites, groups, `@DataProvider`, `@Parameters`, parallel settings, and a lifecycle that matches browsers: `@BeforeMethod` setup, `@Test`, `@AfterMethod` quit. I can know JUnit and TestNG. Interviews want the mapping, not a holy war. Lifecycle: BeforeMethod → setup → test → validation → AfterMethod → cleanup.

---

### HTTP and status codes?

**Model answer:** Client sends a request; API/service/database produce a response. Methods: GET, POST, PUT, PATCH, DELETE. 200 OK, 201 created, 400 bad request, 401 not authenticated, 403 authenticated but not allowed, 404 missing, 500 server error. I teach HTTP before REST Assured so `given().when().then()` is not magic when I debug a 401.

---

### Java HttpClient versus REST Assured?

**Model answer:** `java.net.http.HttpClient` (JDK 11+, we use 25) can GET/POST and read status and body. I should see what libraries abstract. REST Assured is fluent: `given` setup, `when` action, `then` validation — arrange/act/assert in BDD clothing. I still wrap calls in an API client. I never log tokens. Status 200 is not enough; I assert JSON fields with Jackson or REST Assured paths, not `split`.

---

### Database / JDBC?

**Model answer:** Many bugs are not in the UI; the API wrote the wrong row. Java → JDBC → driver → database. I use try-with-resources so connections close. Training databases only. Never destructive SQL on production. Never commit DB passwords. Pages do not run JDBC; a validator or repository class does.

---

### Selenium architecture?

**Model answer:** Java test → WebDriver → browser → application. First honest program: `new ChromeDriver()`, `get(url)`, `quit()`. Selenium 4 can manage drivers (Selenium Manager); I still `quit`. Selenium is not a substitute for `if`, methods, OOP, Maven, or Git. People who skip Java write unmaintainable click scripts.

---

### Locators — how do you choose?

**Model answer:** `By.id`, `By.name`, `By.cssSelector`, `By.xpath`, `By.linkText`. I prefer stable selectors. I avoid absolute XPath, generated ids, long CSS chains, and visual position. If a test is flaky, I confirm the locator in DevTools **before** I add a sleep. A wrong locator plus `Thread.sleep(5000)` is still a wrong locator.

---

### Waits — why not sleep?

**Model answer:** `Thread.sleep(5000)` is slow when the page is fast and still racy when a spinner lasts six seconds. I use `WebDriverWait` and state-based synchronization: wait until visible, clickable, or URL changed. Implicit waits mixed with explicit waits confuse timeout math; I can explain the course preference for explicit, condition-based waits.

---

### Page Object Model?

**Model answer:** Tests should not be a junk drawer of locators, clicks, asserts, and data. Test → page object → Selenium → browser. `LoginPage` holds `By.id("username")` and `enterUsername`. Tests say `loginPage.login(data)`. JDBC, email, and HTTP clients do not belong on the page (single responsibility). When an id changes, I fix one class.

---

### Test data models and data-driven testing?

**Model answer:** `login("a", "b")` is unlabeled strings. Better: `record LoginData(String username, String password)` (mask secrets in `toString`). Progression: hardcoded → method parameters → DataProvider → model object → JSON/CSV → data factory. I climb that ladder when duplication hurts. Rows must be independent. Unique users from a factory prevent tests from editing the same account.

---

### DriverFactory?

**Model answer:** Tests should not construct browsers. `WebDriver driver = DriverFactory.create(BrowserType.CHROME)` returns Chrome, Firefox, or Edge (and later RemoteWebDriver). Options and Grid live in the factory. I always `quit`. I do not add Grid on day one (YAGNI).

---

### ThreadLocal and parallel tests?

**Model answer:** Parallel tests save clock time and destroy suites that share a browser. `ThreadLocal<WebDriver>` stores one driver per thread. I `set` in BeforeMethod, `get` in the test and pages, `quit` and `remove` in AfterMethod. Thread pools reuse threads, so `remove` is mandatory or the next test inherits a dead or dirty driver. This is the opposite of a singleton driver. Isolation first; then turn parallel on.

---

### Selenium Grid?

**Model answer:** Tests → `RemoteWebDriver` → Grid → browser nodes. The factory switches when `GRID_URL` is set. `quit` frees slots. I add Grid when I need remote or cross-OS scale, not to decorate a resume.

---

### Logging and reporting?

**Model answer:** SLF4J plus Logback (typical), levels TRACE through ERROR. `System.out.println` is not a strategy. I never log passwords, tokens, secrets, or sensitive customer data. A report needs results plus evidence: screenshot, exception, browser, environment, timestamps. Allure is a common tool. No PII in artifacts.

---

### CI/CD for automation?

**Model answer:** Push → GitHub → CI compile → unit tests → automation → report. **PR:** compile, quality, unit, smoke — minutes. **Nightly:** regression, cross-browser, deeper API. Fast gate, deep night. Tests that only run on a laptop are a hobby. I do not run 5,000 UI tests on every commit.

---

### Test isolation?

**Model answer:** Tests do not depend on order, a shared driver, or the same mutable user. Unique data, ThreadLocal, independent asserts. Flakes are often isolation bugs, not "the internet."

---

### Code review questions you ask on a test PR?

**Model answer:** Does this method have one purpose? Are names meaningful? Is duplication justified? Is exception handling correct? Are resources closed? Is shared state safe? Are tests independent? Are secrets exposed? Is inheritance necessary? Could composition be simpler? Is the code testable? A PR that passes CI can still be a design failure.

---

## Extra practice

- Map given/when/then to arrange/act/assert.
- Explain 401 vs 403 vs 404 with an API example.
- Fix-order: locator first, then wait, then quit, then factory.
