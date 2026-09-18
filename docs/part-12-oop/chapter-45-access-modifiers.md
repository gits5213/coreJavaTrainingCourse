# Chapter 45 — Access Modifiers

## 1. Today's Goal

By the end of this lesson, you will choose among **`public`**, **`private`**, **`protected`**, and **package-private** (no modifier) with intention.

You will not make everything `public` "so it compiles." You will pick the smallest visibility that still lets the right code cooperate.

## 2. Why It Matters

Access modifiers are the locks on doors.

A field that is `public` is a door with no lock. A method that is `private` is a door only this room can use. A package of `pages` should not need to reach into another class's private locators.

SDET frameworks fail in messy ways when tests call helpers that were never meant to be public: `clickInternalRetryHack()` leaks into 200 tests, then nobody can rename it.

Learning the four levels now saves you from accidental APIs.

## 3. Real-Life Analogy

An office building.

```text
public          = street entrance (anyone)
package-private = office floor (same department / same package)
protected       = family house key (this class + subclasses, and same package)
private         = personal desk drawer (this class only)
```

You do not put payroll files in the street entrance. You do not lock the lobby so tightly that customers cannot enter.

A restaurant:

- public: menu items
- private: the exact spice mix
- protected: a sauce recipe child restaurants may refine
- package-private: helpers only kitchen stations on this floor share

## 4. Illustrated Explanation

```text
MOST OPEN                                                MOST CLOSED
public  →  package-private  →  protected*  →  private
```

\* `protected` is not simply "between package and private." It is visible in the same package **and** to subclasses even in other packages. That extra reach is why people use it for `BasePage` helpers.

Same package `com.shop.pages`:

```text
package com.shop.pages

BasePage
  public    openUrl()          → tests and anyone can call
  protected click()            → LoginPage (subclass) can call
  private   waitForSpinner()   → only BasePage itself

LoginPage extends BasePage
  can call click()
  cannot call waitForSpinner()
```

Different package `com.shop.tests`:

```text
can call public methods
cannot call private
cannot call package-private
can call protected only if it is a subclass (tests usually are not)
```

```text
┌────────────── package com.shop.pages ──────────────┐
│  BasePage          LoginPage                       │
│  packageMethod() ◄── LoginPage can call            │
└────────────────────────────────────────────────────┘
         ▲
         │  other package cannot call packageMethod()
         │
   com.shop.tests
```

## 5. Syntax / Concept

The four modifiers on members (fields, methods, constructors, nested types):

| Modifier | Same class | Same package | Subclass other package | Everyone |
| --- | --- | --- | --- | --- |
| `public` | yes | yes | yes | yes |
| `protected` | yes | yes | yes | no |
| (none) package-private | yes | yes | no | no |
| `private` | yes | no | no | no |

Write nothing for package-private:

```java
void clickInternal() { }      // package-private
private void hide() { }       // private
protected void click() { }    // protected
public void login() { }       // public
```

Top-level classes are only `public` or package-private. You cannot write `private class User` as a top-level class. (Nested classes can be private. That is a later refinement.)

A `public` class must live in a file with the same name: `User.java`.

Practical defaults for this course:

```text
fields                     → private
constructors               → public (or package-private in tests)
methods meant for tests    → public
helpers only this class    → private
helpers for subclasses     → protected
helpers for the package    → package-private
```

Packages get a full chapter soon. For this lesson, "same folder + `package` line" means same package.

## 6. Simple Example

One file, two classes in the default package (no `package` statement). Package-private is visible here because both classes share that default package.

```java
class User {
    private String password;
    String username; // package-private on purpose for this demo
    public String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean passwordMatches(String attempt) {
        return password.equals(attempt);
    }
}

public class AccessDemo {

    public static void main(String[] args) {
        User john = new User("john", "Test1234", "standard");
        System.out.println(john.username);
        System.out.println(john.role);
        System.out.println(john.passwordMatches("Test1234"));
        // System.out.println(john.password); // would not compile
    }
}
```

In a real `User`, `username` would also be private. This demo exists so you can *see* the difference in one file.

## 7. Real-World Example

Bank service methods:

```java
class BankAccount {
    private double balance;

    public BankAccount(double openingBalance) {
        this.balance = openingBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        validatePositive(amount);
        balance = balance + amount;
    }

    private void validatePositive(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }
}
```

`validatePositive` is private. Customers of `BankAccount` should not call it. `deposit` already does.

A `protected` method on a catalog base type:

```java
class Product {
    protected String sku;

    protected void setSku(String sku) {
        this.sku = sku;
    }
}

class Tea extends Product {
    public Tea(String sku) {
        setSku(sku);
    }
}
```

`Tea` is a `Product` (IS-A), so `protected` fits. A random `ReceiptPrinter` in another package should not call `setSku`.

## 8. SDET Example

```java
class BasePage {
    private String lastLog = "";

    public void open(String url) {
        log("open " + url);
        System.out.println("Opening " + url);
    }

    protected void click(String selector) {
        log("click " + selector);
        System.out.println("Click " + selector);
    }

    protected void type(String selector, String value) {
        log("type into " + selector);
        System.out.println("Type into " + selector + ": " + value);
    }

    private void log(String message) {
        lastLog = message;
    }

    public String getLastLog() {
        return lastLog;
    }
}

class LoginPage extends BasePage {
    public void login(String username, String password) {
        type("#username", username);
        type("#password", password);
        click("#login");
    }
}

public class AccessSdetDemo {

    public static void main(String[] args) {
        LoginPage page = new LoginPage();
        page.open("https://qa.shop.example/login");
        page.login("john", "Test1234");
        System.out.println("Last log: " + page.getLastLog());
        // page.click("#login"); // if click were the test API, tests couple to internals
    }
}
```

Tests call `open` and `login` (public). Subclasses call `click` and `type` (protected). `log` stays private.

We are previewing inheritance. Chapter 48 will slow down on `extends`. You only need: child may use parent's `protected` members.

## 9. Break the Code

Everything public:

```java
public String usernameBox = "#username";
public void waitExactlyThreeSecondsBecauseFlaky() { }
```

Tests will call both. You can never change them quietly.

Making a helper `private` and then trying to reuse it in `LoginPage`:

```java
// in BasePage
private void click(String selector) { }

// in LoginPage
click("#login"); // does not compile
```

That helper wanted `protected` (or a public page action that wraps it).

Leaving off `public` on a class that tests in another package must construct:

```java
class LoginPage { } // package-private
// other package: new LoginPage() does not compile
```

## 10. Debug

Compiler messages:

- `username has private access` — use a method, or you leaked a field you meant to hide.
- `click() has protected access` — you called a subclass tool from a stranger class. Wrap it in a public method.
- `is not public; cannot be accessed from outside package` — add `public` or move the caller into the package, on purpose.

Do not "just add public" until you can say who should call it.

If a test imports a class from `utils` and cannot see a method, check whether the method has no modifier (package-private).

## 11. Student Exercise

Write `BasePage` with:

- `public void open(String url)`
- `protected void click(String selector)`
- `private void record(String action)` called from `open` and `click`
- `public String getLastAction()`

Write `HomePage extends BasePage` with `public void clickLogo()` that calls `click("#logo")`.

In `main`, create `HomePage`, `open` a url, `clickLogo`, print last action.

Uncomment (in a comment block) a line `page.click("#logo")` from `main` if `main` is not a subclass — and write whether it compiles. In the **default package**, `protected` is visible to other classes in that same package. Note that in a comment: same package vs other package is the trap. Write one sentence: "If tests live in another package, they cannot call protected click."

## 12. Challenge

Design a tiny three-class picture (all in one file is fine):

- `User` — private password, public `passwordMatches`
- `UserFactory` — package-private method `defaultPassword()` used when building users
- public demo class that can construct users but cannot call `defaultPassword` **if** you put `UserFactory` in a different package

If you are still in the default package, you cannot fully feel package-private. Write the intended package names in comments:

```text
com.shop.models.User
com.shop.models.UserFactory
com.shop.tests.AccessChallenge  // cannot call defaultPassword()
```

Explain in five lines who can see what.

(Chapter 54 will make the folders real. The thinking belongs today.)

## 13. Knowledge Check

1. List the four access levels.
2. What is package-private in source code?
3. Who can see `private` members?
4. Who can see `public` members?
5. Who can see `protected` members?
6. Why are fields usually `private`?
7. Why might `click` on `BasePage` be `protected` rather than `public`?
8. Can a top-level class be `private`?
9. True or false: no modifier means `public` in Java.
10. What goes wrong if tests call every helper because everything is `public`?

## 14. Interview Question

**Question:** Explain Java access modifiers.

A strong answer:

> Java has four: public, protected, package-private (no modifier), and private. public is visible everywhere. private is visible only inside the same class. package-private is visible to other classes in the same package. protected is visible in the same package and also to subclasses in other packages. I default fields to private and expose a small public API. In a page object hierarchy I often make click and type protected so LoginPage can use them, but tests in another package should call login(), not click() directly. Making everything public creates an accidental API that is hard to change.

## 15. Homework

Copy the `BasePage` / `LoginPage` example. Change `click` from `protected` to `private` and write down the compiler error.

Change it to `public` and write two sentences: what tests could now do, and why you might regret it.

Leave it `protected` as the designed choice.

Add a private field `timeoutSeconds` with a public getter. No public setter.

---

## Answer Key

1. `public`, `protected`, package-private (none), `private`.
2. You write no keyword. Visibility is the package.
3. Only the same class.
4. Any class that can see the type.
5. Same class, same package, and subclasses (including other packages).
6. Encapsulation: control change and hide representation.
7. Subpages need it; tests should not couple to raw clicks.
8. No, not as a top-level class.
9. False. No modifier is package-private, not public.
10. You cannot refactor internals; tests depend on accidents.
