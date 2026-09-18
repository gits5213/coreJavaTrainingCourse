# Architect Interview

**Headline question:** Design a framework for **5,000 tests** and **50 automation engineers**.

This is the same scenario as [architecture-exam.md](../exams/architecture-exam.md): 3 environments, Chrome / Firefox / Edge, UI / API / Database, parallel execution, CI/CD. You design and defend.

**How to practice:** 10 seconds to outline, then **8–12 minutes** of structured talk covering every heading below. Draw the layer diagram. Leave 5 minutes for attack questions. Sketches are ideas to hit, not a script.

Close with: architecture solves problems; we grew into this (Part 61); I can still write `statusMatches` on a whiteboard.

---

## The question

> We have 50 automation engineers and about 5,000 automated tests. Three environments. Chrome, Firefox, and Edge. UI, API, and database checks. We need parallel runs and CI/CD. Design the Java SDET platform. Walk me through repository structure, driver strategy, API, pages, data, configuration, parallelism, CI/CD, reporting, flaky tests, governance, code ownership, and security.

---

## Diagram (draw this)

```text
                    TESTS
                      │
        ┌─────────────┼─────────────┐
        │             │             │
       UI            API       Integration
        │             │
        ↓             ↓
   Page Objects    API Clients
        │             │
        └──────┬──────┘
               ↓
         Business Services
               ↓
          Application
```

Supporting packages: models, factories, configuration, fixtures, database, logging, reporting, utilities.

Tests travel **down**. Pages do not call random tests. Clients do not own CI.

---

## Model answer (study this; speak it in your own words)

### Repository structure

I would start from a **monorepo** (the course name is `java-sdet-engineering`) so pages, API clients, and tests share one version. Fifty engineers on fifty repos will drift on WebDriver versions and duplicate `LoginPage`. Maven standard layout:

```text
automation/   (or src/main/java)
  config/
  driver/
  pages/
  components/
  api/
  database/
  models/
  factories/
  services/   (workflows)
  utils/      (tiny, not a junk drawer)

src/test/java
  unit/
  ui/smoke/
  ui/regression/
  api/
  integration/
```

`pom.xml` at the root (modules only if a split is earned). `CODEOWNERS`, a PR template, and a short architecture doc. I would not put every class in `utils`.

If someone argues for many repos: I can split *later* when a domain team publishes a versioned library. Day-one multi-repo is usually coordination cost, not scale.

### Driver strategy

Tests never `new ChromeDriver()`. They ask `DriverFactory.create(browser)`.

- `BrowserType` enum: CHROME, FIREFOX, EDGE.
- Factory applies options (headless in CI, downloads folder, etc.).
- If `GRID_URL` is set, return `RemoteWebDriver`; otherwise local drivers.
- **ThreadLocal&lt;WebDriver&gt;** for parallel: `set` in `@BeforeMethod`, `get` in pages, `quit` **and** `remove` in `@AfterMethod`. Thread pools reuse threads; leftover ThreadLocal state leaks to the next test.
- **Never a singleton driver.** One heap object, many threads, races: who clicked last.
- Always `quit`, including on failure (`try/finally` or AfterMethod). GC does not close Chrome. Grid slots do not free themselves.

Grid is **YAGNI** until we need remote machines, extra OS coverage, or more browsers than laptops can hold. Adding Grid before isolation is proven just multiplies flakes.

### API architecture

HTTP is cheaper and more stable than UI for most business rules.

- One **client per service**: `UserApiClient`, `OrderApiClient`.
- Rest Assured or JDK `HttpClient` underneath — the team picks one and wraps it. Tests do not paste URLs 5,000 times.
- Models / records for JSON (Jackson). Assert status **and** body fields.
- given / when / then is arrange / act / assert.
- API tests live in `api/`; UI tests may call clients for **setup**, not for asserting every field twice without a reason.

### Page architecture

Page Object Model: Test → Page → Selenium → Browser.

- Locators and UI actions in pages; waits **inside** the page (state-based, not `Thread.sleep`).
- **Components** for header, nav, date picker — composition, not a 2,000-line `LoginPage`.
- **Workflows / business services** for journeys: "login as new user and open checkout" so tests stay short.
- Pages do **not** contain JDBC, mail, or raw HTTP. That violates Single Responsibility.
- Stable locators (`id`, `name`, short CSS). Absolute XPath is a last resort with a comment.

### Data architecture

Independent tests or 5,000 tests become a queue of people sharing one `admin` password.

- `record` / model types: `LoginData`, `OrderDraft`. Mask secrets in `toString`.
- **Factories** create unique users (timestamp or UUID) so parallel classes do not collide.
- JSON/CSV for tables of cases; `@DataProvider` or parameterized tests.
- Climb the ladder: hardcoded → parameters → DataProvider → model → JSON → factory. Do not start with a 12-layer data platform.
- Test accounts, not production customer PII.
- Database fixtures only through approved helpers, with cleanup.

### Configuration

Three environments: **dev**, **qa**, **staging** (names as the company uses).

- One file (or profile) per env: base URLs, API hosts, timeouts, `GRID_URL`, browser default.
- Selected by environment variable in CI (`ENV=qa`).
- Timeouts are config, not copied literals.
- **Never passwords, tokens, or connection strings in Git.** Vault or CI secrets. Local `.env` is gitignored.

### Parallelism

Parallelism is a reward for isolation, not a substitute for it.

- Unit tests: high parallel, cheap.
- API tests: moderate parallel, watch rate limits.
- UI: parallel **classes** or **methods** only after ThreadLocal + unique data are proven.
- Thread count **≤ Grid slots** (and ≤ what the environment can take). 50 Chrome sessions on a 4-core laptop with 8 slots will queue, timeout, and flake.
- Shared static mutable state is forbidden in reviews.

### CI/CD

```text
Developer → git push → GitHub → CI
  PR (minutes):  compile, quality/linters, unit, smoke (often Chrome only)
  Nightly:       full regression, Chrome + Firefox + Edge, deeper API, selected DB checks
  Report:        published artifact (Allure)
```

I will **not** run 5,000 UI tests × 3 browsers on every commit. That trains people to ignore CI. Branch protection: required checks on `main`. CD in this course means quality gates more than deploying the product, unless the team asks.

### Reporting

A run nobody can read is theater.

- Allure (or equivalent) from TestNG/JUnit listeners.
- Evidence: screenshot on failure, exception, browser, environment, timestamps.
- Attach API request/response **with secrets redacted**.
- Trends: duration, fail rate, flake rate — so 50 engineers see the same truth.

### Flaky tests

Flakes are **defects**. Retry-as-culture hides them.

- Policy: quarantine with an **expiry date** and an owner. After expiry, the test is fixed or deleted.
- Root-cause order: locator → wait (state) → test data / isolation → environment → product.
- Sleeps and singleton drivers are not flake strategies.
- Track flake rate in the report; a "green" nightly full of retries is red in disguise.

### Governance

- Code review checklist from the course: one purpose, names, duplication, exceptions, resources closed, shared state, test independence, secrets, inheritance vs composition, testability.
- Automated checks: compiler, unit tests, formatter/linter, secret scanning if available.
- Required reviewers on shared folders (`driver/`, `ci/`).
- Architecture evolves from **pain** (Part 61): one class → methods → OOP → packages → POM → models → factories → config → API clients → parallel → CI → governance. We do not build the end state on day one.

### Code ownership

Fifty people cannot all own `Utils.java`.

- `CODEOWNERS`: identity/API vs payments/pages vs core driver vs CI YAML.
- Domain teams own their page objects and clients.
- Core framework changes (DriverFactory, ThreadLocal, Grid) get a smaller set of maintainers and a design note.
- "Utils" is split or forbidden as a magnet.

### Security

- Secrets in CI/vault; never in Git, never in Allure as plaintext tokens.
- Logs: no passwords, cookies, JWTs, PAN, or customer PII.
- Screenshots: avoid production data; use test accounts; consider masking.
- Least privilege DB users for QA read/validation.
- No production customer databases as a "convenient" data source.

---

## One-minute variants (if they interrupt)

**"Just folders?"** Folders without ThreadLocal, PR vs nightly, flake policy, and secrets are a slide deck. I can still write `statusMatches`.

**"Why not one giant UI suite?"** UI is slow and brittle. API and unit tests carry most of the 5,000. UI covers journeys users see. That is how we keep PR times in minutes.

**"How do you start if you only have 50 tests today?"** YAGNI: factory + POM + Maven + CI smoke. Add ThreadLocal when we turn parallel on. Add Grid when laptops are the bottleneck. Add CODEOWNERS when a second team joins. Pain first.

---

## Attack questions — model answers

**Why not a static WebDriver to save launch time?**  
Launch time is real; correctness is more real. A static driver is a race under parallel, and a dirty session under sequential. Isolation first; then optimize with Grid and reusable environments, not a singleton.

**Why not sleep 5 seconds because Grid is slow?**  
Grid latency is a reason to wait for **clickable/visible**, with a sensible timeout in config. Sleep is slow when Grid is fast and insufficient when Grid is slower than 5 seconds.

**Who runs 5,000 tests and when?**  
PR: smoke + unit. Nightly: the 5,000 (split by layer). Cross-browser matrix on nightly, not on every push. Hotfix: smoke only.

**A flake has been retried for a month.**  
Stop retrying as the plan. Assign an owner, quarantine with expiry, fix locator/wait/data or delete the test. A month of retries is undocumented technical debt.

**When do you add Grid?**  
When we need more browsers/OS than local machines, or CI machines should not install browsers. Not before ThreadLocal and quit/remove are boring.

---

## Rubric (for mock interviews)

| Topic covered | Weight |
| --- | --- |
| Repo + layers | required |
| Driver factory + ThreadLocal + quit/remove | required |
| API clients vs pages vs workflows | required |
| Data uniqueness + secrets | required |
| PR vs nightly + browsers | required |
| Parallel + slots | required |
| Flakes + governance + CODEOWNERS | required |
| Security (logs, vault, PII) | required |

Missing singleton-driver rejection or "5,000 UI tests on every PR" is a fail of the architect band even if the folder tree is pretty.

---

## Practice homework

1. Whiteboard the 5,000-test design in 15 minutes with a timer.
2. Speak the twelve headings without notes.
3. Have a classmate attack singleton driver, sleeps, and secrets.
4. Re-read Part 60 (destination) and Part 61 (path) the same day.
