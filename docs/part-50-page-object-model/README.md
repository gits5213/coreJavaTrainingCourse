# Part 50 — Page Object Model

Without POM, a test is a junk drawer:

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

`LoginPage` holds `By.id("username")` and `enterUsername`. Tests say `loginPage.login(user, pass)`.

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [POM](pom.md) | Why POM, LoginPage example, what does *not* belong in a page |

## Prerequisite

Locators and waits. SOLID: a page is not a database client.
