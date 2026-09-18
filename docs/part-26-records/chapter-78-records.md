# Chapter 78 — Records

## 1. Today's Goal

By the end of this lesson, you will declare a **record** for immutable test data:

```java
public record LoginData(String username, String password) {}
```

You will create `new LoginData("john", "Test123")`, read `username()` and `password()`, and print it.

You will know records are **immutable**: no `setPassword`. That is a feature for test data.

## 2. Why It Matters

Testers pass around usernames, expected status codes, API payloads. A class with 40 lines of getters is noise. A record is the data.

Immutability prevents:

```text
LoginData data = sharedAdmin;
data.password = "changed"; // if it were a mutable field
// some other test now logs in with the wrong password
```

Parallel tests (later) make this worse. Immutable test data is a professional habit.

JSON mapping: Jackson can deserialize to records (with a matching constructor). You will meet that after Maven.

## 3. Real-Life Analogy

A laminated badge.

```text
record LoginData(username, password)
```

The badge is printed. You do not erase the name with a pencil. You print a **new** badge if the name changes.

A frozen meal label: ingredients listed, not a whiteboard.

A shipping label printed from a form: the record is the printed label. To change the address, issue a new label.

## 4. Illustrated Explanation

```text
record LoginData(String username, String password) {}

Java generates (idea, not exact code you write):

  constructor LoginData(String, String)
  username()
  password()
  equals / hashCode
  toString  →  LoginData[username=john, password=Test123]
```

```text
        LoginData
        ┌─────────────────┐
        │ username: john  │  final, no setters
        │ password: ***   │
        └─────────────────┘
                 ▲
                 │
          new LoginData("john", "Test123")
```

```text
class (mutable)          record (immutable data)
fields + setters         components in the header
you write equals         equals by components
easy to mess up          defaults that match the data
```

You can still write a class when you need mutability or inheritance (records cannot extend a class; they can implement interfaces).

## 5. Syntax / Concept

```java
public record LoginData(String username, String password) {}
```

Use:

```java
LoginData login = new LoginData("john", "Test123");
String user = login.username();   // not getUsername()
String pass = login.password();
```

Compact constructor for validation:

```java
public record LoginData(String username, String password) {
    public LoginData {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password required");
        }
    }
}
```

The compact constructor runs before the fields are assigned. Throw on bad test data. Do not empty-catch that throw in tests.

Accessors are `username()`, not `getUsername()`, unless you add extra methods.

Records can have extra methods, static factories, but **cannot** add extra instance fields outside the header.

`List.of` and records pair well: unmodifiable list of immutable data.

## 6. Simple Example

```java
public record LoginData(String username, String password) {}

public class RecordDemo {

    public static void main(String[] args) {
        LoginData login = new LoginData("john", "Test123");
        System.out.println(login.username());
        System.out.println(login);
        LoginData again = new LoginData("john", "Test123");
        System.out.println(login.equals(again));
    }
}
```

Expected:

```text
john
LoginData[username=john, password=Test123]
true
```

(Put record and class in files Java allows: public record in `LoginData.java`, demo in its own file, or package-private record in the same file as the demo.)

## 7. Real-World Example

Shop:

```java
public record OrderLine(String sku, int quantity) {}
```

Bank:

```java
public record Transfer(String from, String to, int cents) {}
```

Once created, quantity should not silently change during checkout. New line item → new record (or a class if your domain is mutable — product code differs from test fixtures).

## 8. SDET Example

```java
public record LoginData(String username, String password) {
    public LoginData {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password required");
        }
    }
}

public class RecordSdetDemo {

    public static void login(LoginData data) {
        System.out.println("Typing " + data.username());
        System.out.println("Typing password ********");
    }

    public static void main(String[] args) {
        LoginData standard = new LoginData("standardUser", "Test123");
        LoginData locked = new LoginData("lockedUser", "Test123");
        login(standard);
        login(locked);
        System.out.println("TEST PASSED — both logins had complete data");
    }
}
```

`new LoginData("", "x")` throws before the test clicks. Good.

Do not catch `IllegalArgumentException` empty to keep a parameterized test "running."

## 9. Break the Code

```java
login.username = "other"; // no field access like this; components are private final
```

```java
login.setPassword("x"); // no setter
```

```java
public record LoginData(String username, String password) {
    private String role; // extra instance field — does not compile
}
```

```java
login.getUsername(); // wrong name unless you wrote that method
```

Catching validation and substituting admin credentials: security bug in a test suite. Forbidden.

## 10. Debug

Cannot find `getUsername`: use `username()`.

Jackson not filling a record: later, check component names match JSON keys (`username`, `password`).

`IllegalArgumentException` from compact constructor: print the values you passed (not real production passwords in shared logs — test passwords are usually public in testdata).

Debugger: inspect the record; IntelliJ shows components.

```text
Immutable  →  if data is wrong, create a new record, do not mutate
```

## 11. Student Exercise

Create `LoginData` with username and password. Create two instances. Print both. Print `equals`.

Create `User` record with `username` and `role`. Print `new User("john", "tester")`.

## 12. Challenge

`record ExpectedStatus(String testName, int code) {}`

List of expected statuses. Loop or stream: simulated actual 200 for all. Print TEST FAILED with test name when code != actual.

Add compact constructor: code must be 100–599.

Try `new ExpectedStatus("login", 99)` in a try/catch that prints TEST FAILED on the exception message. Specific catch.

## 13. Knowledge Check

1. What is a record for?
2. Write `LoginData` with username and password.
3. How do you read the username?
4. Are records mutable?
5. What methods does Java generate for you?
6. Can a record have extra instance fields?
7. What is a compact constructor good for in SDET?
8. True or false: records replace the need to learn classes.
9. Why is immutability useful in tests?
10. Empty-catch validation from a record constructor: allowed?

## 14. Interview Question

**Question:** What is a Java record, and why would testers use one?

A strong answer:

> A record is a concise immutable data class. record LoginData(String username, String password) gives me a constructor, username() and password() accessors, equals, hashCode, and toString. Testers use records as test-data models so one test cannot change password for another. I can validate in a compact constructor and throw IllegalArgumentException. Records do not replace classes when I need mutability or inheritance. I do not hide constructor validation with empty catch.

## 15. Homework

Convert a `User` class from earlier (if you have one) into a record, or write `User(username, role)` fresh.

Write three login records in a `List<LoginData>` and `forEach` print usernames.

Notes: "Immutable test data. Accessors are username() not getUsername()."

---

## Answer Key

1. Immutable data carriers with generated constructor, accessors, equals, toString.
2. `record LoginData(String username, String password) {}`
3. `login.username()`
4. No (components are final).
5. Constructor, accessors, equals, hashCode, toString.
6. No extra instance fields beyond the header.
7. Reject blank username/password immediately.
8. False.
9. Shared data cannot be mutated by another test; failures are easier to reason about.
10. No.
