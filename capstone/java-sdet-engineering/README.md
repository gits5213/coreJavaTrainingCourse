# Enterprise Java SDET Platform (capstone skeleton)

This is the **destination** architecture from the course (Part 60), not a day-one homework dump.

```text
One Java Class
  → Page Objects
  → Factories
  → Configuration
  → API Clients
  → Parallel Execution
  → CI/CD
```

Add a layer when duplication or pain appears (Part 61). Empty-looking packages are placeholders with a small real class so the tree is visible in Git.

Default tests use `FakeDriver` and `FakeUserApiClient`. `mvn test` does **not** need Chrome, Firefox, or the public internet.

## Logging — never log secrets

Do not log passwords, tokens, secrets, `Authorization` headers, or sensitive customer data. `SafeLogger` redacts those keys. `LoginData.toString()` prints `password=***`.

## How to run

From the repository root:

```bash
mvn -f capstone/java-sdet-engineering/pom.xml test
```

From this module:

```bash
mvn test
```

That is the smoke command for this skeleton: unit + API-against-fake + smoke, no browser.

## Environments

| File | Role |
| --- | --- |
| `src/test/resources/config/qa.properties` | default classroom URLs |
| `src/test/resources/config/staging.properties` | second environment |

Override browser with `-Dbrowser=firefox`. URLs in properties are example hosts, not live systems.

Secrets (if you later add a real API) belong in env vars or a local untracked file, never in Git.

## Package map

| Path | Put new code here when… |
| --- | --- |
| `config/` | URLs, timeouts, env |
| `driver/` | factory + ThreadLocal manager |
| `pages/` | a new UI screen |
| `components/` | a shared widget (header, date picker) |
| `api/` | a new REST resource client |
| `database/` | JDBC verification |
| `models/` | records such as `LoginData` |
| `factories/` | `UserFactory`, builders |
| `services/` | multi-step workflows, screenshots |
| `utils/` | tiny leftovers — keep this folder hungry |
| `tests/unit` | factories, logger, config |
| `tests/ui` | page objects against FakeDriver |
| `tests/api` | clients (fake or localhost) |
| `tests/smoke` | PR subset |
| `tests/regression` | broader nightly set |

## What this skeleton already has

- `Config` reading `browser` / `baseUrl` from properties
- `BrowserType`, `DriverFactory`, `DriverManager` (`ThreadLocal`) + `FakeDriver`
- `LoginPage` page object
- `LoginData` / `UserFactory`
- `UserApiClient` + `FakeUserApiClient`
- `ScreenshotService` and `SafeLogger`
- One smoke test, one unit test, one API test against the fake client

## What you should grow later

Live Selenium, REST Assured against a real QA host, JDBC against a training DB, Allure, Grid. Not on day one.

## Course CI

The repository-root workflow (`.github/workflows/ci.yml`) is the PR pipeline. This module also has `.github/workflows/README.md` explaining how a product repo would add its own workflow.
