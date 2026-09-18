# Chapter 48 — Inheritance

## 1. Today's Goal

By the end of this lesson, you will model an **IS-A** relationship with **`extends`**.

You will write `LoginPage extends BasePage` and feel why that sentence is legal English, not only legal Java.

## 2. Why It Matters

Automation code repeats: open a url, click, type, wait. If every page copies those methods, a wait bug is copied forty times.

Inheritance lets a general type hold shared tools, and a specific type add its own:

```text
BasePage          → open, click, type
LoginPage IS-A BasePage → login(username, password)
HomePage  IS-A BasePage → search(term)
```

The danger is using inheritance for convenience when the English IS-A test fails. `LoginPage extends User` compiles only if you force the types — and it is still wrong. Prefer composition (Chapter 53) when the honest sentence is HAS-A.

## 3. Real-Life Analogy

A smartphone IS-A phone.

```text
Phone
  call()
  hangUp()

Smartphone extends Phone
  call()       ← already has it
  hangUp()
  installApp() ← extra
```

A smartphone is a phone. A phone case is not a phone. A phone HAS-A case. Do not extend for the case.

A company:

- `Employee` has a name and an id
- `Tester extends Employee` IS-A employee, plus writes tests
- `Laptop` is not an employee. The tester HAS-A laptop.

## 4. Illustrated Explanation

```text
            BasePage
           /        \
          /          \
   LoginPage        HomePage
   IS-A BasePage    IS-A BasePage
```

```text
LoginPage object in memory
  ┌─────────────────────────────┐
  │  fields from BasePage       │
  │  methods from BasePage      │
  │  fields from LoginPage      │
  │  methods from LoginPage     │
  └─────────────────────────────┘
```

The child **inherits** the parent's accessible members. `private` parent fields are not directly visible in the child (Chapter 45). `protected` click is.

Java allows **one** parent class only:

```text
class LoginPage extends BasePage { }     // yes
class LoginPage extends BasePage, User { } // no — no multiple class inheritance
```

Multiple *interfaces* come in Chapter 52.

`super` means the parent:

```text
super()           → parent constructor
super.click(...)  → parent's click
```

If you do not write `super(...)`, Java inserts `super()` as the first constructor line — the parent no-argument constructor must exist.

The IS-A test (say it out loud):

```text
A LoginPage is a BasePage.     YES
A CheckoutPage is a Payment.   NO — it HAS a payment section
A Square is a Rectangle.       argue carefully; do not force it
```

## 5. Syntax / Concept

```java
class BasePage {
    public void open(String url) {
        System.out.println("Open " + url);
    }
}

class LoginPage extends BasePage {
    public void login(String username, String password) {
        System.out.println("Login as " + username);
    }
}
```

Use:

```java
LoginPage page = new LoginPage();
page.open("https://qa.shop.example/login"); // inherited
page.login("john", "Test1234");             // own
```

Parent constructor:

```java
class BasePage {
    private final String name;

    public BasePage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class LoginPage extends BasePage {
    public LoginPage() {
        super("Login");
    }
}
```

`super("Login")` must be first.

Do not inherit just to reuse one method. If `LoginPage` is not a `BasePage` in your mind, extract a helper object instead (composition).

Keep hierarchies shallow. `BasePage` → `LoginPage` is enough. `Entity → Thing → Page → WebPage → ShopPage → LoginPage` is a maze.

## 6. Simple Example

```java
class BasePage {
    public void open(String url) {
        System.out.println("Opening " + url);
    }

    public void click(String selector) {
        System.out.println("Click " + selector);
    }
}

class LoginPage extends BasePage {
    public void login(String username, String password) {
        click("#username");
        System.out.println("Type " + username);
        click("#password");
        System.out.println("Type ********");
        click("#login");
    }
}

public class InheritanceDemo {

    public static void main(String[] args) {
        LoginPage page = new LoginPage();
        page.open("https://qa.shop.example/login");
        page.login("john", "Test1234");
    }
}
```

Expected output:

```text
Opening https://qa.shop.example/login
Click #username
Type john
Click #password
Type ********
Click #login
```

`LoginPage` did not rewrite `open` or `click`. It IS-A `BasePage`.

## 7. Real-World Example

Bank accounts. A savings account IS-A bank account. The child constructor must call `super(owner, opening)` first:

```java
class BankAccount {
    private final String owner;
    protected double balance;

    public BankAccount(String owner, double opening) {
        this.owner = owner;
        this.balance = opening;
    }

    public void deposit(double amount) {
        balance = balance + amount;
    }

    public double getBalance() {
        return balance;
    }

    public String getOwner() {
        return owner;
    }
}

class SavingsAccount extends BankAccount {
    private final double interestRate;

    public SavingsAccount(String owner, double opening, double interestRate) {
        super(owner, opening);
        this.interestRate = interestRate;
    }

    public void applyInterest() {
        deposit(getBalance() * interestRate);
    }
}

public class BankInheritanceDemo {

    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount("Alice", 100.00, 0.10);
        savings.applyInterest();
        System.out.println(savings.getOwner() + " " + savings.getBalance());
    }
}
```

A savings account **is a** bank account. It can deposit (inherited) and apply interest (own).

## 8. SDET Example

Shared test page tools plus a specific login:

```java
class BasePage {
    private final String baseUrl;

    public BasePage(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void open(String path) {
        System.out.println("GET " + baseUrl + path);
    }

    protected void type(String selector, String value) {
        System.out.println("Type " + value + " into " + selector);
    }

    protected void click(String selector) {
        System.out.println("Click " + selector);
    }
}

class LoginPage extends BasePage {
    public LoginPage(String baseUrl) {
        super(baseUrl);
    }

    public void login(String username, String password) {
        open("/login");
        type("#username", username);
        type("#password", password);
        click("#login");
    }
}

class HomePage extends BasePage {
    public HomePage(String baseUrl) {
        super(baseUrl);
    }

    public void search(String term) {
        open("/home");
        type("#search", term);
        click("#search-btn");
    }
}

public class SdetInheritance {

    public static void main(String[] args) {
        String env = "https://qa.shop.example";
        LoginPage loginPage = new LoginPage(env);
        loginPage.login("standard_user", "secret_sauce");
        HomePage homePage = new HomePage(env);
        homePage.search("mug");
    }
}
```

Two pages. One place to change how `open` prints (later: how it really navigates).

## 9. Break the Code

Wrong IS-A:

```java
class User {
    String username;
}

class LoginPage extends User { // LoginPage is not a User
}
```

Forgetting `super(...)` when the parent has no no-arg constructor:

```java
class BasePage {
    public BasePage(String name) { }
}

class LoginPage extends BasePage {
    public LoginPage() {
        // implicit super() — does not compile
    }
}
```

Deep accidental trees:

```java
class TestBase { }
class UiBase extends TestBase { }
class ShopBase extends UiBase { }
class PageBase extends ShopBase { }
class LoginPage extends PageBase { }
```

You will not remember which layer owns the wait.

Making fields public in the parent "so the child can use them" instead of `protected` methods.

## 10. Debug

`there is no default constructor available in BasePage` — call `super(...)` with the arguments the parent needs, as the first line.

If the child cannot see a parent field, it is probably `private`. Use `protected` or a public/protected method.

If you are extending because you wanted to *call* a helper, stop. Pass a helper object (preview of composition).

Say the IS-A sentence. If you hesitate, do not extend.

Debugger: inspect a `LoginPage` object. You will see parent fields and child fields together as one object.

## 11. Student Exercise

Write `BasePage` with `open(String url)` and `click(String selector)`.

Write `LoginPage extends BasePage` with `login(String username, String password)` that uses `click` (and prints typing).

Write `HomePage extends BasePage` with `logout()` that clicks `#logout`.

In `main`, use both pages.

## 12. Challenge

Write `Employee` with `name` and `id` (constructor + getters).

Write `Sdet extends Employee` with `boolean canWriteAutomation` and `void describe()`.

`describe` should print id, name, and whether they write automation.

Then write a **wrong** design in comments: `class Laptop extends Employee`. Explain why HAS-A is the truth.

Create two SDETs. Call `describe` on both.

## 13. Knowledge Check

1. What keyword creates an inheritance relationship?
2. What English test should pass before you extend?
3. Can a Java class extend two classes?
4. What does a child inherit?
5. What is `super(...)` used for?
6. Why does `LoginPage extends BasePage` make sense?
7. Why does `LoginPage extends User` not make sense?
8. True or false: private parent fields are directly usable in the child.
9. What happens if the parent has only `BasePage(String)` and the child constructor does not call `super`?
10. When should you prefer composition over inheritance? (Short answer; details in Chapter 53.)

## 14. Interview Question

**Question:** What is inheritance? How do you use it in a test framework?

A strong answer:

> Inheritance models IS-A. A subclass extends one parent and reuses its accessible behavior. In UI tests, LoginPage extends BasePage because a login page is a page: it reuses open, click, and type, then adds login(). I call super(...) when the parent needs constructor data. Java has single class inheritance. I do not extend just to share a utility method, and I do not make LoginPage extend User. If the honest sentence is HAS-A, I use composition instead. I keep the tree shallow so people can find the code.

## 15. Homework

Implement the SDET example (`BasePage`, `LoginPage`, `HomePage`) by typing it yourself.

Add `InventoryPage extends BasePage` with `addFirstItemToCart()`.

Write six lines in a comment:

```text
IS-A examples:
HAS-A examples:
I will not extend when:
```

---

## Answer Key

1. `extends`
2. "A Child is a Parent."
3. No. Only one class parent.
4. Accessible fields and methods (and constructors via `super`).
5. Calling the parent constructor (or a parent method).
6. A login page is a page; it should reuse page tools.
7. A page is not a person.
8. False.
9. Compile error: no default parent constructor.
10. When the relationship is HAS-A or you only wanted to reuse a helper, not specialize a type.
