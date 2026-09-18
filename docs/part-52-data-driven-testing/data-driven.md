# Data-Driven Testing

## Goal

By the end of this lesson, you will climb the data ladder from hardcoded values to a factory, stop at the rung that matches your problem, and keep each row an independent test.

## Why It Matters

Copying a login test five times for five users is DRY failure. One test with a table of users is data-driven. Jumping straight to a database-backed "test data platform" for two rows is YAGNI.

Interviews love: "How do you data-drive tests?" Tell the progression.

## Real-Life Analogy

A teacher grading with an answer key table vs rewriting the whole exam for each student.

```text
Hardcoded        one student, answers written in the question
Parameters       method that grades any pair
DataProvider     a spreadsheet of students
Model            a Student record per row
JSON/CSV         the spreadsheet file
Factory          a machine that invents valid students
```

## Illustrated Explanation

```text
Hardcoded Data
      ↓
Method Parameters
      ↓
DataProvider
      ↓
Model Object
      ↓
JSON / CSV
      ↓
Data Factory
```

Each arrow is a response to pain:

```text
Pain                         Next rung
Need a second username       parameters
Need many rows in runner     DataProvider
Rows are confusing columns   model
Non-engineers edit data      JSON/CSV
Need unique users each run   factory
```

## Stage 1 — Hardcoded

```java
@Test
void shouldLogin() {
    loginPage.login("standard_user", "secret");
}
```

Fine for one smoke. Pain at three users.

## Stage 2 — Method Parameters

```java
void loginAs(String user, String pass) {
    loginPage.login(user, pass);
}

@Test
void shouldLoginStandard() {
    loginAs("standard_user", "secret");
}
```

You still duplicate `@Test` methods. Better than copy-paste of locators.

## Stage 3 — DataProvider (TestNG)

```java
@DataProvider(name = "logins")
public Object[][] logins() {
    return new Object[][] {
            {"standard_user", "secret"},
            {"problem_user", "secret"}
    };
}

@Test(dataProvider = "logins")
public void shouldReachDashboard(String user, String pass) {
    new LoginPage(driver).login(user, pass);
    Assert.assertTrue(new DashboardPage(driver).isLoaded());
}
```

JUnit 5: `@ParameterizedTest` + `@CsvSource` / `@MethodSource`. Same idea.

## Stage 4 — Model Object

```java
@DataProvider(name = "logins")
public Object[][] logins() {
    return new Object[][] {
            {new LoginData("standard_user", "secret")},
            {new LoginData("problem_user", "secret")}
    };
}

@Test(dataProvider = "logins")
public void shouldReachDashboard(LoginData data) {
    new LoginPage(driver).login(data);
    Assert.assertTrue(new DashboardPage(driver).isLoaded());
}
```

Columns have names. Password masking still required in reports.

## Stage 5 — JSON / CSV

`src/test/resources/testdata/logins.json`:

```json
[
  {"username": "standard_user", "password": "${STANDARD_USER_PASSWORD}"},
  {"username": "problem_user", "password": "${PROBLEM_USER_PASSWORD}"}
]
```

Do not put real passwords in JSON in Git. Placeholder + env substitution, or usernames only + secrets API.

CSV:

```text
username,role
standard_user,user
admin_user,admin
```

Passwords still not in CSV.

A reader class maps files → `List<LoginData>`. Keep it boring.

## Stage 6 — Data Factory

```java
public class UserFactory {
    public static LoginData uniqueUser() {
        return new LoginData("qa_" + System.currentTimeMillis(), Secrets.defaultPassword());
    }

    public static LoginData admin() {
        return new LoginData(Secrets.adminUser(), Secrets.adminPassword());
    }
}
```

Factories create *valid* data and uniqueness. They may call APIs to seed users (then you have a client + factory).

## SDET Example

Status codes:

```java
@DataProvider
public Object[][] codes() {
    return new Object[][] {
            {200, 200, true},
            {200, 404, false}
    };
}

@Test(dataProvider = "codes")
public void status(int expected, int actual, boolean match) {
    Assert.assertEquals(StatusCodes.statusMatches(expected, actual), match);
}
```

This is data-driven without a browser. Start here if UI is not ready.

## Break the Code

```java
@Test(dataProvider = "logins")
public void shouldLogin(String user, String pass) {
    // uses a user created by the previous row
}
```

Rows must be independent. Factories help.

JSON in Git with production passwords: rotate, purge history.

One DataProvider that does UI, API, and DB in a single method: too much. Drive *inputs*; keep the test one purpose.

## Debug

DataProvider column count ≠ parameters: TestNG error before the test runs. Read the message.

Parameterized tests fail: the report must show **which row**. If it only says `shouldLogin`, you cannot debug. TestNG usually shows parameters; keep them non-secret.

## Student Exercise

Write a JUnit or TestNG data-driven test for `statusMatches` with three rows. Then convert rows to a tiny record `StatusCase(int expected, int actual, boolean match)`.

## Challenge

Implement the ladder in notes for login: one paragraph per rung. Implement hardcoded + DataProvider + model in code. JSON is optional. Do not build a factory framework with plugins.

## Knowledge Check

1. Recite the six rungs.
2. When is hardcoded OK?
3. What does a DataProvider supply?
4. Why models inside the provider?
5. Why not passwords in CSV in Git?
6. What problem does a factory solve?
7. JUnit equivalent of DataProvider?
8. Must rows be independent?
9. When is JSON YAGNI?
10. Can API tests be data-driven too?

## Interview Question

**Question:** How do you approach data-driven testing?

A strong answer:

> I grow data with the problem: hardcoded, then method parameters, then DataProvider, then a model like LoginData, then JSON or CSV if non-engineers own the table, then a factory for unique users. I do not start with a complex data platform. Rows are independent. Secrets stay out of Git. TestNG DataProvider or JUnit ParameterizedTest runs one method many times. Reports must identify the row without printing passwords.

## Homework

Data-drive `statusMatches`. Commit `Add data-driven statusMatches tests`. Sketch where login would sit on the ladder today.

---

## Answer Key

1. Hardcoded → parameters → DataProvider → model → JSON/CSV → factory
2. One smoke, one user
3. Rows of arguments
4. Named fields, type safety
5. Leakage
6. Unique/valid data without copy-paste
7. `@ParameterizedTest`
8. Yes
9. When you have two rows and only engineers edit them
10. Yes — status codes, payloads
