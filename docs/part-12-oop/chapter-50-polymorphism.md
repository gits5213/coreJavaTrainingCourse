# Chapter 50 — Polymorphism

## 1. Today's Goal

By the end of this lesson, you will hold a **parent type** in a variable and plug in **different child objects**.

You will write:

```java
Browser browser = new ChromeBrowser();
browser.launch();

browser = new FirefoxBrowser();
browser.launch();
```

The rest of the code still says `browser.launch()`. That is polymorphism as a working idea: **one handle, many shapes**.

## 2. Why It Matters

Cross-browser testing is polymorphism. Your test should not be rewritten for Firefox. It should ask a `Browser` to launch, click, and quit.

API tests do the same with HTTP clients. Payment tests do the same with `PaymentService`. If every `if (browserName.equals("chrome"))` is copied into fifty tests, you will miss one.

Polymorphism lets the **caller** stay stable while the **actual object** changes.

## 3. Real-Life Analogy

A wall socket.

```text
You plug in:
  a lamp
  a phone charger
  a laptop brick

You still say: "get power from the socket."
```

The socket is the parent type. The devices are children. You do not rebuild the house to charge a new phone.

A remote that says "press Play" works on a TV, a speaker, or a projector if they all speak the Play language.

Driving: you can drive a `Vehicle`. Today a car, tomorrow a van. You still use steering and brakes.

## 4. Illustrated Explanation

```text
variable type     actual object
   Browser     =  ChromeBrowser
   Browser     =  FirefoxBrowser
```

```text
Browser browser
      │
      │  points at
      ▼
┌─────────────────┐
│ ChromeBrowser   │
│ launch() Chrome │
└─────────────────┘

later the same variable
      │
      ▼
┌─────────────────┐
│ FirefoxBrowser  │
│ launch() Firefox│
└─────────────────┘
```

The **compiler** looks at the variable type (`Browser`) to know which methods you may call.

The **JVM at run time** looks at the actual object to decide **which override** to run.

```text
Compile time:  is launch() on Browser?  yes → code compiles
Run time:      what object is this?     Chrome → Chrome.launch()
```

You cannot call a method that exists only on the child unless you use the child type (or a cast, which we will avoid in this lesson).

```text
Browser browser = new ChromeBrowser();
browser.launch();           // ok — on Browser
browser.openDevTools();     // no — only on ChromeBrowser
```

That restriction is a feature. Tests that only need `launch` stay browser-agnostic.

## 5. Syntax / Concept

Parent (or later: interface / abstract class):

```java
class Browser {
    public void launch() {
        System.out.println("Launch generic");
    }

    public void quit() {
        System.out.println("Quit");
    }
}
```

Children override:

```java
class ChromeBrowser extends Browser {
    @Override
    public void launch() {
        System.out.println("Launch Chrome");
    }
}

class FirefoxBrowser extends Browser {
    @Override
    public void launch() {
        System.out.println("Launch Firefox");
    }
}
```

Polymorphic variable:

```java
Browser browser = new ChromeBrowser();
browser.launch();
browser = new FirefoxBrowser();
browser.launch();
```

A method that accepts the parent type is the real prize:

```java
static void runSmoke(Browser browser) {
    browser.launch();
    browser.quit();
}

runSmoke(new ChromeBrowser());
runSmoke(new FirefoxBrowser());
```

`runSmoke` never mentions Chrome. New browsers do not change `runSmoke`.

This is not magic. It requires inheritance (or an interface) plus overriding. Without override, every child would run the parent's `launch`.

## 6. Simple Example

```java
class Browser {
    public void launch() {
        System.out.println("Launch generic");
    }
}

class ChromeBrowser extends Browser {
    @Override
    public void launch() {
        System.out.println("Launch Chrome");
    }
}

class FirefoxBrowser extends Browser {
    @Override
    public void launch() {
        System.out.println("Launch Firefox");
    }
}

public class PolymorphismDemo {

    public static void main(String[] args) {
        Browser browser = new ChromeBrowser();
        browser.launch();

        browser = new FirefoxBrowser();
        browser.launch();
    }
}
```

Expected output:

```text
Launch Chrome
Launch Firefox
```

Same variable name. Same method call. Different objects. Different output.

## 7. Real-World Example

Payments at checkout:

```java
class Payment {
    public void pay(double amount) {
        System.out.println("Pay " + amount + " somehow");
    }
}

class CardPayment extends Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Charge card " + amount);
    }
}

class WalletPayment extends Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Debit wallet " + amount);
    }
}

public class CheckoutPolymorphism {

    public static void payOrder(Payment payment, double amount) {
        payment.pay(amount);
    }

    public static void main(String[] args) {
        payOrder(new CardPayment(), 49.99);
        payOrder(new WalletPayment(), 49.99);
    }
}
```

The checkout method does not `if` on payment type. Tomorrow `BankTransferPayment` can be passed in if it extends `Payment` and overrides `pay`.

Chapter 51 will make `Payment` abstract so nobody can `new Payment()` by accident.

## 8. SDET Example

```java
class Browser {
    public void launch() {
        System.out.println("Launch generic");
    }

    public void open(String url) {
        System.out.println("Open " + url);
    }

    public void quit() {
        System.out.println("Quit");
    }
}

class ChromeBrowser extends Browser {
    @Override
    public void launch() {
        System.out.println("Launch Chrome");
    }
}

class FirefoxBrowser extends Browser {
    @Override
    public void launch() {
        System.out.println("Launch Firefox");
    }
}

class EdgeBrowser extends Browser {
    @Override
    public void launch() {
        System.out.println("Launch Edge");
    }
}

public class CrossBrowserSmoke {

    public static void smokeLogin(Browser browser) {
        browser.launch();
        browser.open("https://qa.shop.example/login");
        System.out.println("Would type credentials");
        browser.quit();
        System.out.println("---");
    }

    public static void main(String[] args) {
        Browser browser = new ChromeBrowser();
        smokeLogin(browser);

        browser = new FirefoxBrowser();
        smokeLogin(browser);

        smokeLogin(new EdgeBrowser());
    }
}
```

This is the shape of a parameterized test later: one smoke flow, three browsers.

Arrays of parent type:

```java
Browser[] browsers = {
    new ChromeBrowser(),
    new FirefoxBrowser(),
    new EdgeBrowser()
};
for (Browser browser : browsers) {
    smokeLogin(browser);
}
```

## 9. Break the Code

Using the child type everywhere:

```java
ChromeBrowser browser = new ChromeBrowser();
// later you must rewrite the test for FirefoxBrowser
```

That works for one browser. It is not polymorphism helping you.

Calling a Chrome-only method on a `Browser` variable:

```java
Browser browser = new ChromeBrowser();
browser.enableFlashForLegacySite(); // does not compile
```

If you truly need that, your test is Chrome-specific. Keep the variable as `ChromeBrowser` in that one test, or put the behavior on the parent if all browsers must support it.

`if` soup instead of overrides:

```java
if (name.equals("chrome")) {
    System.out.println("Launch Chrome");
} else if (name.equals("firefox")) {
    System.out.println("Launch Firefox");
}
```

Every new browser edits this `if`. Polymorphism moves that decision into the object.

Forgetting to override: `new ChromeBrowser().launch()` prints `Launch generic`. You thought you had polymorphism; you had unused subclasses.

## 10. Debug

If you always see the parent message, the child did not override (signature mismatch, missing `@Override`).

If the code will not compile `browser.childOnlyMethod()`, that is the compiler using the variable type. Either add the method to the parent, or use the child type on purpose.

If you feel you need `instanceof` and casts everywhere, your parent type is too thin or too fat. Redesign the parent API so tests can stay on `Browser`.

Debugger: watch the variable `browser`. The **type** in the variables view will show `ChromeBrowser` even if the declared type is `Browser`. Step into `launch` and land in the child class.

## 11. Student Exercise

Copy the simple `Browser` / `ChromeBrowser` / `FirefoxBrowser` example.

Write `static void launchAndQuit(Browser browser)` that calls `launch` then prints `Quit`.

Call it with Chrome and Firefox.

Then add `EdgeBrowser`. Call `launchAndQuit` again. You should not change `launchAndQuit`.

## 12. Challenge

Write a tiny test runner:

- `class TestCase { void run(); String name(); }`
- `class LoginTest extends TestCase` overrides both
- `class CheckoutTest extends TestCase` overrides both

In `main`, put them in a `TestCase[]` and loop: print name, call `run`.

That loop is a baby test framework. It does not mention Login or Checkout by name inside the loop.

## 13. Knowledge Check

1. What is polymorphism in this lesson's working language?
2. Write `Browser browser = new ChromeBrowser();` — which is the variable type, which is the actual object?
3. At compile time, which type decides which methods you may call?
4. At run time, which type decides which override runs?
5. Why is `smokeLogin(Browser browser)` powerful?
6. Can you call a method that exists only on `ChromeBrowser` through a `Browser` variable?
7. True or false: polymorphism requires different method names per child.
8. How do arrays help cross-browser tests here?
9. What goes wrong if subclasses do not override `launch`?
10. How is this different from a chain of `if (browser.equals("chrome"))`?

## 14. Interview Question

**Question:** What is polymorphism? Give an automation example.

A strong answer:

> Polymorphism means I can treat different objects through a common type, and the actual object's override runs. In Java I write Browser browser = new ChromeBrowser(); then later browser = new FirefoxBrowser(); I still call browser.launch(). Compile time checks the Browser API; run time dispatches to Chrome or Firefox. My smoke test method takes Browser, so adding EdgeBrowser does not rewrite the test steps. That is how cross-browser testing should work. I avoid instanceof chains when an override would do.

## 15. Homework

Implement `CrossBrowserSmoke` by typing it.

Add a comment at `smokeLogin`:

```text
This method does not know Chrome from Firefox. That is the point.
```

Write three browsers into an array and loop.

In notes, draw the socket analogy and map socket → `Browser`, devices → Chrome/Firefox/Edge.

---

## Answer Key

1. One parent-typed handle; many actual child objects; the right override runs.
2. Variable type `Browser`; actual object `ChromeBrowser`.
3. The variable type (`Browser`).
4. The actual object (`ChromeBrowser` or `FirefoxBrowser`).
5. You can pass any browser without changing the smoke steps.
6. No, not without a child-typed variable or a cast.
7. False. Same name, different overrides.
8. `Browser[]` can hold mixed children; one loop launches each.
9. You get the generic parent behavior for every browser.
10. New browsers require new `if` branches in every copy; polymorphism localizes behavior in the new class.
