# Test Data Models

## Goal

By the end of this lesson, you will model login (and similar) data as a `record LoginData` or a POJO, pass it into tests and page methods, and keep passwords out of logs.

## Why It Matters

`login(String, String, String, String, boolean)` is how bugs hide. Was the third string email or account number? A model names the fields.

SDET data is a product: unique users, roles, expired cards. Models plus factories (next parts) beat Excel-only thinking — though CSV/JSON will arrive in Part 52.

## Real-Life Analogy

A paper form with boxes labeled Username and Password vs a sticky note "a / b."

The form is a type. The sticky note is two strings.

## Illustrated Explanation

```text
Hardcoded strings
    ↓
LoginData model
    ↓
UserFactory / JSON  (later)
```

```java
public record LoginData(
        String username,
        String password
) {
}
```

```text
Test
  LoginData data = new LoginData("standard_user", password);
  loginPage.login(data);
```

POJO equivalent (Java without records):

```java
public class LoginData {
    private final String username;
    private final String password;

    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
```

Records are shorter and immutable by default. POJOs are fine. Do not mix 15 styles.

## Syntax / Concept

Use the model at the edges of tests:

```java
public void login(LoginData data) {
    enterUsername(data.username());
    enterPassword(data.password());
    submit();
}
```

Validation can live on the model:

```java
public record LoginData(String username, String password) {
    public LoginData {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username required");
        }
    }

    @Override
    public String toString() {
        return "LoginData[username=" + username + ", password=****]";
    }
}
```

Override `toString` so reports do not print passwords. This is not optional.

## Simple Example

```java
public class LoginDataDemo {
    public static void main(String[] args) {
        LoginData data = new LoginData("standard_user", "do-not-print-me");
        System.out.println(data); // must not show the real password
    }
}
```

## Real-World Example

Banking:

```java
public record TransferData(
        String fromAccount,
        String toAccount,
        String amount
) {
}
```

E-commerce:

```java
public record AddressData(
        String line1,
        String city,
        String postalCode
) {
}
```

Nested models beat 12 string parameters.

## SDET Example

```java
@Test
void shouldLoginWithStandardUser() {
    LoginData data = new LoginData("standard_user", Secrets.password("STANDARD_USER"));
    new LoginPage(driver).login(data);
    assertTrue(new DashboardPage(driver).isLoaded());
}
```

`UserFactory.admin()` can return `LoginData` or a richer `User`. Start small.

## Break the Code

```java
System.out.println(data.password());
```

```java
public record LoginData(String username, String password) {}
// default toString prints password
```

```java
login("user", "pass", "http://qa", "chrome", "en-US");
```

Unlabelled strings. Make models.

## Debug

If JSON mapping fails later, field names on the record must match (or use annotations). Today: constructor argument order for POJOs is a common swap (username/password reversed). Tests that still pass might be logging in as the password. Assert the username on the dashboard.

## Student Exercise

Create `LoginData` as a record *or* POJO. Override `toString` to mask password. Add `LoginPage.login(LoginData)`. Unit-test `toString` does not contain the raw password.

## Challenge

Add `User` with role, plus `LoginData` nested or converted via `toLoginData()`. Keep YAGNI: only fields you use.

## Knowledge Check

1. Write the `LoginData` record from the curriculum.
2. When use a POJO instead?
3. Why override `toString`?
4. Why not 6 string parameters?
5. Where do passwords come from?
6. Should models live in tests only?
7. Immutable data benefit?
8. How does this prepare for JSON?
9. What belongs in a factory vs the record?
10. Can a page accept `LoginData`?

## Interview Question

**Question:** How do you model test data in Java?

A strong answer:

> I use a record LoginData(String username, String password) or a POJO at beginner level. Named fields beat a pile of strings. I mask passwords in toString so reports stay safe. Pages accept LoginData. Later JSON and factories produce those models. I do not hardcode secrets in Git. Models make DataProviders and API bodies less error-prone.

## Homework

Add `LoginData` to `src/main/java` (shared models). Unit test masking. Commit `Add LoginData model with masked toString`.

---

## Answer Key

1. `public record LoginData(String username, String password) {}`
2. Older Java, or need mutability/setters for a library
3. Prevent secret leakage
4. Call sites become unreadable and swappable
5. Vault, env, CI secrets — not source
6. Shared models in main are OK; tests consume them
7. Safer sharing; no accidental mutation across tests
8. JSON maps to the same fields
9. Record = shape; factory = how to invent values
10. Yes, that is a clean API
