# Chapter 65 — throw

## 1. Today's Goal

By the end of this lesson, you will **throw** an exception on purpose with the `throw` keyword.

You will write something like:

```java
if (username == null || username.isBlank()) {
    throw new IllegalArgumentException("username must not be blank");
}
```

Java's library throws when `parseInt` fails. Your methods should throw when **your rules** are broken: empty password, negative quantity, status outside 100–599.

## 2. Why It Matters

`catch` is receiving a signal. `throw` is sending one.

If a `login` method gets a blank username, returning `"ok"` is a lie. Returning `null` is a delayed crash. Printing an error and continuing is easy to ignore. **Throwing** forces the caller to notice.

SDET helpers should fail loud when test data is nonsense. A helper that "kind of works" with empty passwords creates flaky tests and false greens.

```text
Silent bad data   →  later NPE, wrong click, mystery
throw now         →  the stack trace points at the real cause
```

## 3. Real-Life Analogy

A bouncer.

```text
if age < 21
    throw new UnderageException("not tonight")
```

The bouncer does not whisper "maybe enter." The bouncer stops the line.

A referee throwing a flag. A smoke detector that you **test** by pressing the button — you create the alarm on purpose.

A package handler who refuses a box with no address: they do not ship it to a random city. They reject it.

## 4. Illustrated Explanation

```text
your method
    │
    ├─ data OK  →  continue  →  return normally
    │
    └─ data illegal
            │
            ▼
     create exception object
     new IllegalArgumentException("reason")
            │
            ▼
     throw it
            │
            ▼
     this method STOPS
            │
            ▼
     caller must catch or also stop
```

`throw` vs `throws` (do not mix them — `throws` is the next chapter):

```text
throw    a statement: actually hurl THIS object
throws   a declaration on a method: this method might hurl THAT type
```

```text
        throw new IllegalArgumentException("qty");
        │      │
        │      └── create the object
        └── send it
```

You throw **objects**, not classes:

```text
throw IllegalArgumentException;           // wrong
throw new IllegalArgumentException("m");  // right
```

## 5. Syntax / Concept

```java
throw new IllegalArgumentException("quantity must be positive");
```

Common unchecked types you throw in application and test code:

| Type | When |
| --- | --- |
| `IllegalArgumentException` | A parameter is illegal (blank user, negative price) |
| `IllegalStateException` | The object is not ready (browser not started) |
| `NullPointerException` | Possible, but prefer a clearer type and message if you can |
| `AssertionError` | A test found expected ≠ actual (tests use this idea; JUnit wraps it) |

You can throw a checked exception too (`throw new IOException("missing")`). Then the method usually needs `throws` (next chapter).

Create, then throw, in two lines if you want to inspect in a debugger:

```java
IllegalArgumentException problem =
        new IllegalArgumentException("username blank");
throw problem;
```

After `throw`, further lines in that block are unreachable. The compiler will complain if you write them.

Do not throw `Exception` as a vague blob when a specific type exists. Specific types are searchable in logs.

## 6. Simple Example

```java
public class ThrowDemo {

    public static int parsePositiveQuantity(String text) {
        int quantity = Integer.parseInt(text);
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be >= 1, got " + quantity);
        }
        return quantity;
    }

    public static void main(String[] args) {
        System.out.println(parsePositiveQuantity("3"));
        System.out.println(parsePositiveQuantity("0"));
    }
}
```

First call prints `3`. Second throws `IllegalArgumentException` with a message you wrote. `parseInt` is not the villain this time — **your rule** is.

Wrap the second call in `try/catch` in a second version if you want to see the program continue. Today it is OK if `main` crashes; you caused it on purpose.

## 7. Real-World Example

Bank transfer amount:

```java
public class Transfer {

    public static void transfer(String from, String to, int cents) {
        if (from == null || from.isBlank()) {
            throw new IllegalArgumentException("from account is required");
        }
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("to account is required");
        }
        if (cents <= 0) {
            throw new IllegalArgumentException("cents must be positive");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("cannot transfer to the same account");
        }
        System.out.println("Transfer " + cents + " from " + from + " to " + to);
    }

    public static void main(String[] args) {
        transfer("111", "222", 500);
        transfer("111", "111", 500); // throws
    }
}
```

An online store `Order` should not accept `quantity 0`. Throw at the door of the method so the rest of checkout never runs on garbage.

## 8. SDET Example

Test data builders and page helpers should reject nonsense.

```java
public class LoginDataGuard {

    public static void login(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username is required for login test");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password is required for login test");
        }
        System.out.println("Typing username: " + username);
        System.out.println("Typing password: ********");
        System.out.println("Clicking Sign in");
    }

    public static void assertStatus(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError(
                    "TEST FAILED — expected " + expected + " but was " + actual);
        }
        System.out.println("TEST PASSED");
    }

    public static void main(String[] args) {
        login("john", "Test123");
        assertStatus(200, 200);
        // login("  ", "Test123"); // would throw IllegalArgumentException
        // assertStatus(404, 200); // would throw AssertionError
    }
}
```

`AssertionError` is not for hiding. It **is** the failure. JUnit's `assertEquals` does this kind of thing for you later. You should understand the idea: tests throw when truth is broken.

Never do this:

```java
try {
    login("", "x");
} catch (Exception e) {
    // SDET RULE: do not hide the guard you just wrote
}
```

## 9. Break the Code

```java
public class BrokenThrow {

    public static void requireUser(String username) {
        if (username == null || username.isBlank()) {
            new IllegalArgumentException("username required");
        }
        System.out.println("User is " + username);
    }

    public static void main(String[] args) {
        requireUser("   ");
    }
}
```

The programmer **created** an exception object and never `throw` it. The method continues and prints `User is    `. Creating is not throwing.

Another bug:

```java
throw IllegalArgumentException("username required"); // missing new — does not compile
```

Another:

```java
if (username.isBlank()) {
    throw new IllegalArgumentException("blank");
    System.out.println("unreachable"); // compile error
}
```

## 10. Debug

If a guard "does not work":

1. Did you write `throw` or only `new`?
2. Put a breakpoint on the `if`. Inspect the value. Blank vs `"null"` the string vs `null` the reference are three different things.
3. Use `isBlank()` (spaces only) not only `isEmpty()` if spaces should be illegal.
4. If the stack trace starts in your `throw new ...` line, congratulations: the guard worked. Now decide if the **caller** should catch or fix its data.

```text
Created exception?  →  object exists in memory for a moment
Thrown?             →  control jumps out
Caught?             →  someone handled it
```

All three are different events.

## 11. Student Exercise

Create `ThrowExercise` with `requireStatusCode(int code)`.

Valid HTTP codes for this exercise: `100` through `599`.

If invalid, `throw new IllegalArgumentException` with the bad code in the message.

In `main`, call with `200`, then `99`. Let the second call crash. Read your message in the stack trace.

## 12. Challenge

Write `assertEquals(int expected, int actual)` that throws `AssertionError` with both numbers in the message when they differ, and prints TEST PASSED when they match.

Write `assertNotBlank(String name, String value)` that throws `IllegalArgumentException` including the field `name`.

In `main`:

- happy path: username `"admin"`, status expected 201 actual 201
- then demonstrate one assertion failure (comment it if you want the class to finish, or leave it throwing)

Do not catch empty.

## 13. Knowledge Check

1. What does the `throw` statement do?
2. What is wrong with `new IllegalArgumentException("x");` without `throw`?
3. Do you throw a class or an object?
4. What is a good exception type for a bad method argument?
5. What happens to lines after `throw` in the same block?
6. How is `throw` different from `throws`?
7. Why should a login helper throw on blank username instead of continuing?
8. True or false: tests may throw `AssertionError` when expected ≠ actual.
9. Should you catch the exception you just threw in the same method with an empty catch "so main is clean"?
10. Name one real-world rule worth throwing for (bank or shop).

## 14. Interview Question

**Question:** When would you throw an exception in your own method?

A strong answer:

> When the method cannot keep its promise. If login requires a username and the caller passes blank, I throw IllegalArgumentException with a clear message. That fails fast instead of causing a later null click. throw sends the exception object. throws on the method is a declaration, which is a different keyword. In tests I let assertion failures propagate. I do not catch Exception empty to hide them.

## 15. Homework

Write `PasswordGuard`. Rules: password must not be blank, must be length >= 8, must not equal `"password"`.

Throw `IllegalArgumentException` with a specific message for each rule (check in that order).

Call from `main` with one good password in a `try` and three bad ones each in their own `try/catch` that prints TEST FAILED plus the message. A good password prints TEST PASSED.

You are still not allowed to catch `Exception` empty.

---

## Answer Key

1. It actually hurls an exception object; the method stops that path.
2. The object is created and thrown away. Nothing stops.
3. An object (`throw new ...`).
4. `IllegalArgumentException`
5. They do not run (unreachable).
6. `throw` is the action. `throws` is a method declaration (next chapter).
7. So bad test data fails immediately with a clear cause.
8. True.
9. No. That hides the guard.
10. Example: transfer cents must be positive; from and to accounts required and different.
