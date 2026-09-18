# Chapter 47 — `final`

## 1. Today's Goal

By the end of this lesson, you will use **`final`** in three separate jobs: a **final variable**, a **final method**, and a **final class**.

You will not treat `final` as one vague "cannot change" sticker. The thing you lock is different in each case.

## 2. Why It Matters

Some values must not drift: a user's id, a max retry count, a tax rate loaded at startup.

Some methods must not be replaced by a subclass: a security check, a logging format you need to trust.

Some classes must not be extended: `String` is final so nobody makes a "sneaky String" that breaks assumptions.

SDET code uses `final` for constants (`DEFAULT_TIMEOUT`), for constructor-set urls that should not be swapped mid-test, and sometimes for page classes you do not want turned into a surprise hierarchy.

## 3. Real-Life Analogy

Three different locks:

```text
final variable  = a label printed on a sealed envelope
                  you cannot rewrite the address

final method    = a company safety procedure
                  a local branch cannot replace "how we evacuate"

final class     = a sealed official stamp design
                  you cannot issue a "subclass stamp" that looks official
```

A tattoo vs a pencil note vs a locked building. All "cannot easily change," but they are not the same object.

A train's car number is final for that trip. How the conductor greets people might be overridable. The idea "this vehicle is a sealed certified car" might forbid extra unofficial cars attached as subclasses.

## 4. Illustrated Explanation

```text
final variable
  int x = 5;
  x = 6;           // no

final field set in constructor
  User id is 42 at birth
  cannot become 43 later

final method
  BasePage.log() is final
  LoginPage cannot @Override log()

final class
  final class ChromeOptionsWrapper { }
  class MyChrome extends ChromeOptionsWrapper { }  // no
```

Constants (the common pattern):

```text
public static final int MAX_RETRIES = 3;
     │      │     │
     │      │     └── never reassign
     │      └── one copy on the class
     └── anyone may read
```

Naming: constants use `ALL_CAPS_WITH_UNDERSCORES`.

Blank final: a `final` field with no value at declaration **must** be assigned in the constructor (every constructor).

```text
private final String username;

User(String username) {
    this.username = username;  // last chance
}
```

## 5. Syntax / Concept

### Final variable (local or field)

```java
final int maxRetries = 3;
// maxRetries = 4; // does not compile
```

For a **reference**, `final` locks the arrow, not the object's insides:

```java
final User john = new User("john");
john.setRole("admin");     // allowed if setter exists — object can change
// john = new User("other"); // not allowed — arrow cannot change
```

That surprise matters. `final User john` does not make the user immutable. It makes the variable `john` stay pointed at the same object.

### Final method

```java
public final void log(String message) {
    System.out.println("[LOG] " + message);
}
```

Subclasses inherit it but cannot override it. They can still call it.

### Final class

```java
public final class StatusCodes {
    private StatusCodes() {
    }

    public static boolean isSuccess(int code) {
        return code >= 200 && code < 300;
    }
}
```

Nobody writes `class MyCodes extends StatusCodes`. Utility classes are often `final` with a private constructor so nobody instantiates or extends them.

`final` is not the same as `const` in other languages. Java's `final` is the tool you have. Immutability of objects is a design (private fields, no setters), not automatic from `final` on a reference.

## 6. Simple Example

```java
class User {
    private final String username;
    private String role;

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

public class FinalVariableDemo {

    public static void main(String[] args) {
        final int maxRetries = 3;
        User john = new User("john", "standard");
        john.setRole("admin");
        System.out.println(john.getUsername() + " retries=" + maxRetries);
        // john.username cannot be set — no setter, field is final
    }
}
```

Expected output:

```text
john retries=3
```

Username is locked at construction. Role can still change. `maxRetries` cannot be reassigned.

## 7. Real-World Example

A bank account id is final; balance is not.

```java
class BankAccount {
    public static final String BANK_CODE = "JAVABANK";
    private final String accountId;
    private double balance;

    public BankAccount(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void deposit(double amount) {
        balance = balance + amount;
    }
}
```

A `final` tax calculator class you do not want people to "customize" by extending:

```java
final class ReceiptTax {
    public static double addTax(double amount) {
        return amount * 1.07;
    }
}
```

## 8. SDET Example

Constants and a final logging method on a base page:

```java
class BasePage {
    public static final int DEFAULT_TIMEOUT_SECONDS = 10;

    public final void log(String message) {
        System.out.println("[PAGE] " + message);
    }

    protected void click(String selector) {
        log("click " + selector);
    }
}

class LoginPage extends BasePage {
    private final String url;

    public LoginPage(String url) {
        this.url = url;
    }

    public void open() {
        log("open " + url);
    }

    // public void log(String message) { } // cannot override — log is final
}

public class FinalSdetDemo {

    public static void main(String[] args) {
        LoginPage page = new LoginPage("https://qa.shop.example/login");
        page.open();
        page.click("#login");
        System.out.println("Timeout: " + BasePage.DEFAULT_TIMEOUT_SECONDS);
    }
}
```

A `final class JsonPayloads` of canned bodies is a reasonable way to say "do not inherit this dump of strings; just use the constants."

## 9. Break the Code

Reassigning a final local:

```java
final int timeout = 10;
timeout = 20; // does not compile
```

Forgetting to assign a blank final in one constructor:

```java
class User {
    private final String username;

    User(String username) {
        this.username = username;
    }

    User() {
        // missing assignment — does not compile
    }
}
```

Thinking `final User user` freezes the user's fields:

```java
final User user = new User("john", "standard");
user.setRole("admin"); // still works
```

Overriding a final method:

```java
class LoginPage extends BasePage {
    public void log(String message) { } // does not compile if log is final
}
```

Extending a final class:

```java
final class BrowserTypeHolder { }
class More extends BrowserTypeHolder { } // does not compile
```

## 10. Debug

`cannot assign a value to final variable` — you tried to reassign. Either remove `final` (if it really must change) or change the one allowed assignment.

`blank final field may not have been initialized` — every constructor must set it, or you set it at declaration.

If a subclass "override" does not compile, the parent method may be `final` (or `private`, which is not overridable either).

If you wanted an immutable object and only marked the reference `final`, add `private` fields and remove setters. `final` on the variable was the wrong lock.

## 11. Student Exercise

Write `User` with:

- `public static final int MIN_PASSWORD_LENGTH = 8;`
- `private final String username;`
- `private String password;`

Constructor sets both. Setter for password checks `MIN_PASSWORD_LENGTH`. No setter for username.

In `main`, create a user, change password, print username and the constant via `User.MIN_PASSWORD_LENGTH`.

## 12. Challenge

1. Write `BasePage` with `public final void log(String message)`.
2. Write `LoginPage extends BasePage` that calls `log` from `login`.
3. Attempt an override of `log` in a comment; record the error.
4. Write `final class WaitTimes` with two public static final ints and a private constructor.
5. Write one paragraph: difference between final variable, final method, and final class, using SDET examples.

## 13. Knowledge Check

1. What does `final` on a variable prevent?
2. If `final User john = ...`, can you change `john`'s role through a setter?
3. What does `final` on a method prevent?
4. What does `final` on a class prevent?
5. Write a typical constant declaration for max retries = 3.
6. What is a blank final field?
7. Why is `String` final (idea, not JVM trivia)?
8. True or false: `final` on a reference makes the object immutable.
9. Why make `log` final on `BasePage`?
10. Why might a utility class be `final` with a private constructor?

## 14. Interview Question

**Question:** What is `final` in Java? Explain it for variables, methods, and classes.

A strong answer:

> final means different locks. A final variable cannot be reassigned. A final field is often set in the constructor, like a user id. If the variable is a reference, the reference cannot change, but the object's fields still can unless I designed the object to be immutable. A final method cannot be overridden, which I use for a logging or security method I do not want subclasses to replace. A final class cannot be extended; String is an example, and I may make a utility class final. I name constants public static final with ALL_CAPS.

## 15. Homework

Write `HomeworkFinal` that includes all three:

- constants `SHORT_WAIT` and `LONG_WAIT`
- `User` with final `username`
- `BasePage` with a final `log` method
- `final class StatusCodes` with `isSuccess`

In a comment block, write:

```text
final variable → ...
final method   → ...
final class    → ...
```

Fill the dots with your own words, not a copy of this chapter's first sentence.

---

## Answer Key

1. Reassignment of that variable.
2. Yes, if a setter exists. `final` locked the reference, not the fields.
3. Overriding in a subclass.
4. Extending the class.
5. `public static final int MAX_RETRIES = 3;`
6. A final field not set at declaration; constructors must assign it.
7. So nobody subclasses String and breaks assumptions about how text behaves.
8. False.
9. So every page logs the same way; a child cannot silently change the format.
10. It is a bag of static helpers, not a type to inherit or instantiate.
