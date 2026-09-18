# Architecture Exam

**Level:** after Parts 54–62  
**Format:** whiteboard or shared diagram + oral defense  
**Timebox:** 45 minutes design + 15 minutes questions

This **is** the interview-style design prompt. Practice until you can defend `quit` + `remove`, PR vs nightly, and no secrets without notes.

Suggested weight in the [final exam](final-exam-guide.md): **10%** (architecture presentation). The same scenario appears in [interview/architect.md](../interview/architect.md).

---

## Scenario

```text
50 engineers
5,000 automated tests
3 environments
Chrome
Firefox
Edge
UI
API
Database
Parallel execution
CI/CD
```

**Design the system and defend every decision.**

There is no single correct vendor. There **is** incorrect: singleton driver, sleeps as the default wait, passwords in Git, 5,000 UI tests on every pull request, a God `LoginPage`.

---

## Mandatory decisions

You must pick and defend each row:

| Topic | You must pick and defend |
| --- | --- |
| Repo | Monorepo vs many repos; tree |
| Drivers | Factory, ThreadLocal, Grid or not |
| Layers | Tests, workflows, pages, clients |
| Data | Factories, uniqueness, secrets |
| Environments | 3 config files, how selected |
| Browsers | Matrix nightly vs PR Chrome only |
| Database | Who may query QA; cleanup |
| Parallel | Unit of parallel; slot counts |
| CI | PR vs nightly contents |
| Flakes | Policy |
| Governance | CODEOWNERS, reviews |
| Security | Logs, screenshots, vault |

Also cover reporting (Allure or equivalent: screenshot, exception, browser, env, timestamps, no PII).

---

## Diagram you should be able to draw

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

Supporting: models, factories, configuration, fixtures, database helpers, logging, reporting, utilities.

---

## Model defense (what "good" sounds like)

**Repository.** A monorepo such as `java-sdet-engineering` keeps pages, API clients, and tests on one version. Maven standard layout. Tests split `unit` / `ui` / `api` / `smoke` / `regression`. CODEOWNERS by domain. PR template. Docs. Fifty people cannot all own `Utils.java`.

**Driver.** `DriverFactory.create(BrowserType)` returns local Chrome/Firefox/Edge or `RemoteWebDriver` when `GRID_URL` is set. `ThreadLocal<WebDriver>`: `set` in BeforeMethod, `get` in tests/pages, `quit` + `remove` in AfterMethod. Never a singleton driver. Grid is YAGNI until remote or cross-OS scale is a real pain.

**API.** One client per service (`UserApiClient`). Rest Assured or HttpClient underneath. Models for JSON. Tests do not paste URLs 5,000 times.

**Pages.** POM + components (header, nav). Waits inside pages. No JDBC in pages. Workflows/services for multi-step journeys (login-then-checkout).

**Data.** Records, factories for unique users, JSON/CSV for tables. Secrets from vault/CI. Independent tests; no shared mutable "the" admin user that every class edits.

**Config.** `dev` / `qa` / `staging` files: base URLs, timeouts, `GRID_URL`. Selected by env var. Never passwords in Git.

**Parallel.** Prove isolation first. Then TestNG parallel classes or methods. Thread count ≤ Grid slots (and laptop reality). Unit tests parallel cheaply; UI is expensive.

**CI/CD.** PR (minutes): compile, quality, unit, smoke (often Chrome only). Nightly: regression, Chrome + Firefox + Edge, deeper API, selected DB checks. Do not run 5,000 UI tests on every commit.

**Reporting.** Allure (or similar) + screenshots + exception + browser + env + timestamps. No PII in screenshots if you can avoid it; mask logs.

**Flakes.** Treat as defects. Quarantine with an expiry date. Do not make retry the culture. Root causes: waits, locators, shared data, isolation.

**Governance.** Review checklist (Part 59), linters, required checks, branch protection on `main`.

**Ownership.** CODEOWNERS: payments pages vs identity API vs core driver. Architecture review for shared folders.

**Security.** Secrets in CI, masked logs, test accounts, no production customer data as UI evidence.

**Evolution.** You did not build this on day one (Part 61). Pain → the next layer. You can still write `statusMatches` on a whiteboard.

---

## Examiner attack questions

- Why not one Git repo per engineer?
- Why not a static `WebDriver` "to save launch time"?
- Why not `Thread.sleep(5000)` "because Grid is slow"?
- Who runs 5,000 tests and when?
- How do you stop 50 people from putting passwords in `config.properties`?
- A flake has been retried three times a night for a month. What do you do?
- When do you add Grid?
- Why might API tests be the default and UI the expensive subset?

---

## Rubric (10 points)

| Look for | Points |
| --- | --- |
| Layers + repo tree | 2 |
| Driver isolation (ThreadLocal, quit/remove) | 2 |
| PR vs nightly + browsers | 2 |
| Data, config, secrets | 2 |
| Flakes, ownership, security | 2 |

Fail if the design is a slide of folder names with a singleton driver and sleeps.
