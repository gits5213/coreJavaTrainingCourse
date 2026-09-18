# Chapter 49 — Method Overriding

## 1. Today's Goal

By the end of this lesson, you will **override** a parent method in a child class and mark it with **`@Override`**.

You will see the difference from **overloading** (Chapter 37): overriding replaces behavior in a subclass. Overloading is the same name with a different parameter list, often in the same class.

## 2. Why It Matters

`BasePage.open` might print. `LoginPage` might need to open **and** wait for the username box. That is still `open`. The child's version should replace the parent's when you have a `LoginPage`.

Without overriding, you invent `openLoginSpecial()` and tests never remember to call it.

`@Override` is not decoration. If you misspell the method, the annotation makes the compiler shout instead of quietly adding a new unrelated method.

## 3. Real-Life Analogy

A company handbook says: "Greet the customer."

```text
Base employee:    "Hello."
Store in Tokyo:   "Irasshaimase!"   ← same job, local version
```

The job title is still "greet." The child location overrides the script.

A fire alarm: the building rule is "evacuate." A lab overrides it with "shut gas valves, then evacuate." Same name, stricter steps.

Overloading would be "greet(person)" vs "greet(person, language)" — extra ingredients, not a child replacing the parent.

## 4. Illustrated Explanation

```text
BasePage.open(url)
    print Opening url

LoginPage.open(url)     @Override
    print Opening url
    print Wait for #username
```

Which one runs?

```text
LoginPage page = new LoginPage();
page.open("...");
        │
        └── LoginPage version  (the object's real type)
```

```text
Runtime chooses the method from the actual object,
not only from the variable's declared type.
(That sentence is the door to polymorphism in the next chapter.)
```

`@Override` sits on the child method:

```text
@Override
public void open(String url) { ... }
```

If the parent has `open(String)` and you write `open(String, String)` you are **overloading**, not overriding — unless the parent also has that pair.

Rules of thumb for a valid override:

```text
same name
same parameter types
compatible return type
not more private than the parent
cannot override static methods (that is hiding — do not)
cannot override final methods
```

`super.open(url)` calls the parent version so you can extend rather than copy.

## 5. Syntax / Concept

Parent:

```java
public void open(String url) {
    System.out.println("Opening " + url);
}
```

Child:

```java
@Override
public void open(String url) {
    super.open(url);
    System.out.println("Waiting for login form");
}
```

Always add `@Override` when you intend to override. It is an annotation: extra information for the compiler (and for humans).

Overload vs override:

| | Overload | Override |
| --- | --- | --- |
| Where | usually same class | subclass |
| Parameters | must differ | must match |
| Purpose | extra ways to call | replace/extend behavior |

```text
click(String selector)           // original
click(String selector, int n)    // overload

// in subclass
@Override
click(String selector)           // override
```

## 6. Simple Example

```java
class BasePage {
    public void open(String url) {
        System.out.println("Opening " + url);
    }
}

class LoginPage extends BasePage {
    @Override
    public void open(String url) {
        super.open(url);
        System.out.println("Wait for #username");
    }

    public void login(String username) {
        System.out.println("Login as " + username);
    }
}

public class OverrideDemo {

    public static void main(String[] args) {
        LoginPage page = new LoginPage();
        page.open("https://qa.shop.example/login");
        page.login("john");
    }
}
```

Expected output:

```text
Opening https://qa.shop.example/login
Wait for #username
Login as john
```

## 7. Real-World Example

Fees on accounts:

```java
class BankAccount {
    protected double balance;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public void applyMonthlyFee() {
        balance = balance - 5.00;
    }

    public double getBalance() {
        return balance;
    }
}

class PremiumAccount extends BankAccount {
    public PremiumAccount(double balance) {
        super(balance);
    }

    @Override
    public void applyMonthlyFee() {
        // premium: no fee
    }
}

public class FeeOverrideDemo {

    public static void main(String[] args) {
        BankAccount basic = new BankAccount(100);
        BankAccount premium = new PremiumAccount(100);
        basic.applyMonthlyFee();
        premium.applyMonthlyFee();
        System.out.println("Basic: " + basic.getBalance());
        System.out.println("Premium: " + premium.getBalance());
    }
}
```

Expected output:

```text
Basic: 95.0
Premium: 100.0
```

Same method name. Different policy. Notice the variables are typed `BankAccount` — that is a peek at polymorphism.

## 8. SDET Example

```java
class BasePage {
    public void open(String url) {
        System.out.println("Navigate: " + url);
    }

    public String title() {
        return "Generic Title";
    }
}

class LoginPage extends BasePage {
    @Override
    public void open(String url) {
        super.open(url);
        System.out.println("Assert login button visible");
    }

    @Override
    public String title() {
        return "Login";
    }
}

class HomePage extends BasePage {
    @Override
    public String title() {
        return "Home";
    }
}

public class SdetOverride {

    public static void main(String[] args) {
        LoginPage loginPage = new LoginPage();
        loginPage.open("https://qa.shop.example/login");
        System.out.println(loginPage.title());

        HomePage homePage = new HomePage();
        homePage.open("https://qa.shop.example/home");
        System.out.println(homePage.title());
    }
}
```

`HomePage` did not override `open`, so it uses the parent. `LoginPage` did.

## 9. Break the Code

Misspelling without `@Override`:

```java
class LoginPage extends BasePage {
    public void openn(String url) { // new method, parent open still used
        System.out.println("never runs when someone calls open");
    }
}
```

This compiles. Tests call `open`. Your "override" never runs. **`@Override` would have failed compilation.**

Narrowing access:

```java
class BasePage {
    public void open(String url) { }
}

class LoginPage extends BasePage {
    @Override
    void open(String url) { } // weaker than public — does not compile
}
```

Trying to override `final` or `static` parent methods.

Forgetting `super` when you still needed the parent work, then losing navigation and only waiting.

## 10. Debug

If the parent method still runs and your extra prints never appear:

1. Did you actually override (same name, same parameters)?
2. Did you put `@Override` on it?
3. Are you calling through a different object type than you think?

If `@Override` errors with "method does not override or implement a method from a supertype," you mismatched the signature. Fix the child to match the parent, or you are overloading by accident.

Debugger: step into `open`. Confirm you land in `LoginPage.open`, then `super.open` if you called it.

## 11. Student Exercise

`BasePage` has `click(String selector)` printing `Click selector`.

`LoginPage` overrides `click` to print `LoginPage click selector` after `super.click(selector)`.

In `main`, construct `LoginPage` and call `click("#login")`.

Then deliberately misspell the child method, add `@Override`, and paste the compiler error into a comment. Fix it.

## 12. Challenge

Write `Browser` with `String name()` returning `"Browser"` and `void launch()` printing `Launching generic`.

Write `ChromeBrowser` that overrides both: name `"Chrome"`, launch prints `Launching Chrome`.

Write `FirefoxBrowser` similarly.

In `main`, create both children and call `launch()` and `name()`.

Do not use polymorphism variables yet if you want a warm-up — or type them as `Browser` if you want a head start on Chapter 50.

## 13. Knowledge Check

1. What is overriding?
2. How is it different from overloading?
3. What does `@Override` do for you?
4. Why should you almost always use `@Override` when overriding?
5. How do you call the parent version from the child?
6. Can you override a `final` method?
7. Can you make an override `private` if the parent method is `public`?
8. True or false: changing the parameter list still counts as overriding.
9. What silent bug happens if you misspell the method without `@Override`?
10. Why override `open` on `LoginPage` instead of inventing `openLogin()`?

## 14. Interview Question

**Question:** What is method overriding? Why use `@Override`?

A strong answer:

> Overriding is when a subclass provides its own version of an instance method that already exists on the parent, with the same name and parameters. The JVM calls the object's actual type's method. I use super.method() when I want to extend the parent rather than replace it completely. @Override tells the compiler I intend to override. If I misspell the name, the code fails to compile instead of adding a new method that never runs. This is different from overloading, which is the same name with different parameters. In page objects, LoginPage overrides open() to wait for the login form.

## 15. Homework

Type the SDET example. Add `CheckoutPage` that overrides `title()` to `"Checkout"` and overrides `open` to wait for a `#pay` button.

Write 4 flash cards in comments:

```text
overload vs override
@Override
super.method()
misspelling without annotation
```

---

## Answer Key

1. A subclass replacing a parent instance method with the same signature.
2. Overloading: different parameter lists. Overriding: subclass, same list.
3. It asks the compiler to verify you really overrode something.
4. So typos become compile errors, not silent extra methods.
5. `super.methodName(...)`
6. No.
7. No. You cannot narrow `public` to `private`.
8. False. That is overloading (or a new method).
9. The parent method still runs; your method is ignored.
10. Callers already know `open`; they should get the right behavior automatically.
