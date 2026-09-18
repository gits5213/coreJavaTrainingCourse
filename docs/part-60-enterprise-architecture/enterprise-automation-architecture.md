# Enterprise Automation Architecture

## Goal

By the end of this lesson, you will draw Tests → Business Workflows → Page Objects/API Clients → Application, name the supporting packages, and reproduce the `java-sdet-engineering/` repository tree from the curriculum. You will explain what each folder is for in one sentence.

## Why It Matters

50 engineers cannot share a single `Utils.java`. Architecture is how a team finds login, user creation, and config without Slack archaeology.

Architect interviews: "Design a framework." This diagram plus this tree is the answer — then you defend trade-offs (Part 66).

## Real-Life Analogy

A hospital.

```text
Tests                 doctor orders
Business workflows    "admit patient" (several steps)
Pages / API clients   wards and labs (how you actually act)
Application           the patient's body
Supporting            records, pharmacy, facilities, security
```

You do not put the pharmacy inside the stethoscope. `LoginPage` is not the pharmacy (data factory) or the records office (reporting).

## Illustrated Explanation

Target architecture:

```text
Tests
  ↓
Business Workflows
  ↓
Page Objects / API Clients
  ↓
Application
```

```text
Test: shouldTransferSalary
  Workflow: TransferJourney.asTeller(...)
    Pages: LoginPage, AccountsPage, TransferPage
    Clients: LedgerApiClient
      Application: bank web + bank API
```

Supporting:

```text
Models           LoginData, TransferData
Factories        UserFactory, DriverFactory
Configuration    env, timeouts, URLs
Fixtures         seed data, test users
Database         JDBC DAOs for verification
Logging          SLF4J
Reporting        Allure
Utilities        small, boring helpers (dates, waits wrapper)
```

Dependencies should point **down**. Tests may call workflows. Pages must not call tests. Utilities must not import TestNG tests. Clients must not depend on page objects (or you couple UI to API).

```text
tests → workflows → pages/clients → app
           ↑
     models, config, factories
```

## Enterprise Repository (curriculum tree)

This is the full tree. Type it. Hang it on a wall.

```text
java-sdet-engineering/
│
├── .github/
│   ├── workflows/
│   ├── CODEOWNERS
│   └── pull_request_template.md
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/company/automation/
│   │           ├── config/
│   │           ├── driver/
│   │           ├── pages/
│   │           ├── components/
│   │           ├── api/
│   │           ├── database/
│   │           ├── models/
│   │           ├── factories/
│   │           ├── services/
│   │           └── utils/
│   │
│   └── test/
│       ├── java/
│       │   └── com/company/tests/
│       │       ├── unit/
│       │       ├── ui/
│       │       ├── api/
│       │       ├── smoke/
│       │       └── regression/
│       │
│       └── resources/
│           ├── config/
│           ├── testdata/
│           └── suites/
│
├── docs/
│
├── pom.xml
├── .gitignore
└── README.md
```

### Folder one-liners

| Path | Job |
| --- | --- |
| `.github/workflows/` | CI: PR and nightly YAML |
| `CODEOWNERS` | who must review `/pages` vs `/api` |
| `pull_request_template.md` | review checklist |
| `config/` | URLs, timeouts, env |
| `driver/` | DriverFactory, DriverManager ThreadLocal |
| `pages/` | LoginPage, TransferPage |
| `components/` | shared widgets (header, date picker) |
| `api/` | UserApiClient, Rest Assured or HttpClient |
| `database/` | JDBC DAOs |
| `models/` | records/POJOs |
| `factories/` | UserFactory, data builders |
| `services/` | workflows / business services |
| `utils/` | leftover small helpers — keep this folder hungry and thin |
| `tests/unit` | Calculator, factories, matchers |
| `tests/ui` | Selenium |
| `tests/api` | REST |
| `tests/smoke` | tiny subset for PR |
| `tests/regression` | nightly |
| `resources/config` | `qa.properties` |
| `resources/testdata` | JSON/CSV |
| `resources/suites` | testng.xml |
| `docs/` | how to run, architecture |

`utils/` is where good architectures go to die. If a class has a real name (`ScreenshotService`), it is a service, not a util.

## Simple Example — A test walking the layers

```java
@Test
void shouldCreateUserAndSeeWelcome() {
    LoginData admin = UserFactory.admin();
    User created = userWorkflow.createActiveUser(admin); // service
    loginPage.login(admin);                              // page
    userListPage.search(created.username());
    assertTrue(userListPage.contains(created.username()));
    assertTrue(userDao.exists(created.username()));       // database
}
```

```java
public class UserWorkflow {
    private final UserApiClient api;

    public User createdActiveUser(LoginData admin) { /* uses api, not Selenium */ }
}
```

Names illustrative. The **direction** of calls is the lesson.

## Real-World Example

Banking org: 50 SDETs. CODEOWNERS: payments pages reviewed by payments squad. Config has `dev`, `qa`, `staging`. Smoke on PR is login + one transfer API. Regression nightly is the rest.

## SDET Example

Do not import `com.company.tests` from `pages`. If you need a wait, it lives in pages/components or a small `Waiter` in utils.

Business workflows (`services/`) exist when a journey spans multiple pages *and* API setup. If a test only clicks login, a workflow class is YAGNI.

## Break the Code

```text
pages/LoginPage.java also contains JDBC, REST, Excel, mail
tests/ui/LoginTest.java new ChromeDriver()
utils/EverythingUtil.java 4000 lines
```

```text
Circular: api client imports LoginPage to "reuse locators"
```

## Debug

If newcomers cannot find where to put a class, the tree is wrong or undocumented. README maps: "new UI screen → pages, new REST resource → api, new JSON → testdata."

If every PR touches `utils`, rename the real concepts out of it.

## Student Exercise

Draw the four-layer diagram from memory. Recreate the tree in a notes file (or empty directories) without copying blindly — type it. Write one sentence per top-level package under `automation/`.

## Challenge

Pick three classes you already wrote (LoginPage, DriverFactory, CalculatorTest) and assign them a path in this tree. Justify. Identify one class that should *not* exist yet (YAGNI).

## Knowledge Check

1. Recite Tests → ... → Application.
2. Name eight supporting concerns.
3. Where do page objects live?
4. Where do TestNG XML suites live?
5. What is CODEOWNERS for?
6. Why split smoke vs regression packages?
7. Why keep utils thin?
8. May pages depend on tests?
9. What goes in `services/`?
10. Reproduce `driver/`, `api/`, `models/`, `factories/` purpose.

## Interview Question

**Question:** How would you structure an enterprise Java automation repository?

A strong answer:

> Tests call business workflows, which use page objects and API clients to talk to the application. Supporting packages: models, factories, configuration, fixtures, database, logging, reporting, utilities. Repository java-sdet-engineering with GitHub workflows, CODEOWNERS, src/main/java com.company.automation config driver pages components api database models factories services utils, and tests split unit ui api smoke regression, plus resources for config testdata suites. Dependencies point down. I would not create every folder on day one, but this is the target when 50 engineers share 5000 tests.

## Homework

Paste the tree into `docs/architecture.md` of your training repo (or this course notes). Map your current files onto it. Do not move everything blindly if you only have five classes — Part 61 is evolution.

---

## Answer Key

1. Tests → Business Workflows → Page Objects / API Clients → Application
2. Models Factories Configuration Fixtures Database Logging Reporting Utilities
3. `src/main/java/.../pages/`
4. `src/test/resources/suites/`
5. Required reviewers per path
6. PR speed vs nightly depth
7. Avoid a junk drawer
8. No
9. Multi-step journeys / domain services
10. browsers; HTTP; records; data/driver creation
