# Chapter 75 — Optional

## 1. Today's Goal

By the end of this lesson, you will use `Optional<User>` to model **a value that might exist or not**.

You will write:

```java
Optional<User> maybe = findUser("john");
if (maybe.isPresent()) {
    User user = maybe.get();
    System.out.println(user.username);
} else {
    System.out.println("No user");
}
```

You will prefer `orElse`, `orElseThrow`, and `ifPresent` over reckless `get()`.

You will **not** wrap every field in Optional.

## 2. Why It Matters

`findUser` returning `null` is a landmine. Callers forget to check. `NullPointerException` happens far from the search.

Returning `Optional<User>` **forces the caller to see** "might be empty." That is the point.

SDET: a locator might not find a row. An API might omit a field. Optional is one honest tool. Empty catch of NPE is not a tool.

## 3. Real-Life Analogy

A mailbox.

```text
Optional.of(letter)  →  mail today
Optional.empty()     →  no mail
```

You do not pretend an empty mailbox is a blank letter that you try to open and then crash.

A restaurant reservation: table for "Ada" might exist tonight or not. The host returns a maybe-table, not a fake table with null chairs.

A school lost-and-found: `findUmbrella(color)` — sometimes the box is empty. That is not an exception (unless policy says missing umbrella is an error). Optional vs exception: **expected absence** vs **broken rules**.

## 4. Illustrated Explanation

```text
        Optional<User>
        ┌──────────────────┐
        │  PRESENT  User   │
        └──────────────────┘
                 or
        ┌──────────────────┐
        │  EMPTY           │
        └──────────────────┘
```

```text
findUser("john")
    found  →  Optional.of(user)
    miss   →  Optional.empty()
                    │
                    ▼
            caller must choose:
            orElse(guest)
            orElseThrow()
            ifPresent(...)
            isPresent() + get()   ← get is sharp
```

```text
null return
    │
    ▼
caller forgets
    │
    ▼
NPE in a different method
    │
    ▼
you debug the wrong place

Optional return
    │
    ▼
compiler and teammates see Maybe
```

Abuse:

```text
class User {
    Optional<String> username;  // usually too much
    Optional<String> role;
}
JSON mapping becomes painful. Fields can just be String and sometimes null,
or you validate at the boundary.
```

## 5. Syntax / Concept

```java
import java.util.Optional;
```

Create:

```java
Optional<User> present = Optional.of(user);           // user must not be null
Optional<User> maybe = Optional.ofNullable(user);     // null → empty
Optional<User> missing = Optional.empty();
```

`Optional.of(null)` throws NPE. Use `ofNullable` if it might be null.

Use:

```java
maybe.ifPresent(u -> System.out.println(u.username));

User u = maybe.orElse(new User("guest", "none"));

User required = maybe.orElseThrow(() -> new IllegalStateException("user required"));
```

`get()` throws `NoSuchElementException` if empty. Only `get()` after `isPresent()`, or skip `get` entirely.

Do **not**:

- Use Optional as a field everywhere
- Use Optional as a method **parameter** (overload methods or use overloading; parameters should be simple)
- `orElse(expensive())` when you needed `orElseGet(() -> expensive())` — `orElse` always creates the fallback object even if present

Empty Optional is not an exception. Do not empty-catch `NoSuchElementException` from `get()` to "fix" it. Use `orElseThrow` with a clear message.

## 6. Simple Example

```java
import java.util.Optional;

class User {
    String username;
    String role;

    User(String username, String role) {
        this.username = username;
        this.role = role;
    }
}

public class OptionalDemo {

    public static Optional<User> findUser(String username) {
        if ("john".equals(username)) {
            return Optional.of(new User("john", "tester"));
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        Optional<User> john = findUser("john");
        Optional<User> ghost = findUser("nobody");

        john.ifPresent(u -> System.out.println("Found " + u.username));
        System.out.println("Ghost present? " + ghost.isPresent());
        User fallback = ghost.orElse(new User("guest", "none"));
        System.out.println("Fallback " + fallback.username);
    }
}
```

Expected:

```text
Found john
Ghost present? false
Fallback guest
```

## 7. Real-World Example

Shop: `findProduct(sku)` — empty means out of catalog, show "not found" page. That is Optional.

Bank: `findAccount(id)` — if missing during a transfer that **requires** the account, `orElseThrow`. Absence is a hard error in that flow.

Same type, different policy at the call site. That is why Optional is a return type: the **caller** decides.

## 8. SDET Example

```java
import java.util.List;
import java.util.Optional;

public class FindUserInTestData {

    public static Optional<User> findByRole(List<User> users, String role) {
        return users.stream()
                .filter(u -> role.equals(u.role))
                .findFirst();
    }

    public static void main(String[] args) {
        List<User> users = List.of(
                new User("john", "tester"),
                new User("ada", "admin")
        );

        User admin = findByRole(users, "admin")
                .orElseThrow(() -> new AssertionError("TEST FAILED — no admin user in data"));
        System.out.println("Will login as " + admin.username);
        System.out.println("TEST PASSED");
    }
}
```

`findFirst()` already returns `Optional<User>`. Stream API and Optional are friends.

If admin is required for the test, `orElseThrow` is the failure. Do not `get()` and empty-catch `NoSuchElementException`.

## 9. Break the Code

```java
User u = findUser("nobody").get();
```

Empty → `NoSuchElementException`.

```java
Optional.of(null);
```

NPE immediately.

```java
public void login(Optional<String> username) { }
```

Awkward API. Callers wrap constantly. Prefer `login(String username)` and validate.

```java
try {
    user = maybe.get();
} catch (Exception e) {
    user = new User("admin", "admin"); // hidden empty, maybe privilege escalation in tests
}
```

Forbidden pattern. Empty test user became admin.

## 10. Debug

NPE from `Optional.of`: you passed null. Use `ofNullable` or fix the source.

`NoSuchElementException`: `get()` on empty. Switch to `orElseThrow` with a message that includes the id you searched.

Debugger: IntelliJ shows Optional as empty or with a value. Expand it.

```text
Expected absence  →  Optional.empty, handle at caller
Unexpected absence in a test  →  orElseThrow AssertionError
Bug  →  not empty-catch
```

## 11. Student Exercise

Write `findUser` as in the simple example. In `main`, look up `"john"` and `"nobody"`. Print using `isPresent` and `orElse`.

Do not call `get()` on the empty one.

## 12. Challenge

`List<User>` of three users. `findByUsername`. Test:

- found user role equals expected
- missing user → TEST FAILED via `orElseThrow(AssertionError)`

Second method `findAdmin` using stream `filter` + `findFirst`.

No Optional fields on `User`.

## 13. Knowledge Check

1. What does `Optional<User>` mean?
2. How do you make an empty Optional?
3. What does `orElseThrow` do when empty?
4. Why is `get()` dangerous?
5. Should every class field be Optional?
6. Should method parameters be Optional as a style?
7. `Optional.of(null)` does what?
8. How does `findFirst()` relate?
9. True or false: empty Optional is the same as throwing an exception.
10. Empty-catch `NoSuchElementException` to invent an admin user: allowed?

## 14. Interview Question

**Question:** What is Optional, and when should you not use it?

A strong answer:

> Optional<User> is a container that may hold a User or be empty. It is a return type for methods like findUser when absence is a normal outcome. I use orElse, orElseThrow, or ifPresent, not get() without a check. I do not use Optional on every field or parameter; that adds noise and hurts JSON mapping. In tests, missing required data is orElseThrow with AssertionError. I never empty-catch to hide emptiness.

## 15. Homework

Refactor a method that currently returns `null` for "not found" (write one if needed) to return `Optional`. Update the caller.

Notes: "Might exist or not. Not a new null for everything."

---

## Answer Key

1. A box that may contain a User or be empty.
2. `Optional.empty()`
3. Throws the exception you supply (or `NoSuchElementException` by default).
4. It throws if empty.
5. No.
6. No, generally.
7. Throws `NullPointerException`.
8. It returns `Optional` of the first match, or empty.
9. False. Empty is a value meaning absence; throwing is a different policy.
10. No.
