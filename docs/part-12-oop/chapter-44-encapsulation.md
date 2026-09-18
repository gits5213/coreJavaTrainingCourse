# Chapter 44 — Encapsulation

## 1. Today's Goal

By the end of this lesson, you will hide fields with **`private`** and talk to objects through **getters** and **setters** (and better methods like `deposit`).

You will treat a `User` password as something the outside world does not poke directly.

## 2. Why It Matters

If every class can write `account.balance = 1_000_000`, your bank is a spreadsheet with no security.

Encapsulation means:

```text
data is private
behavior is the door
```

Getters and setters are the simplest door. Better doors are methods that keep rules: `withdraw` refuses a negative amount. `setPassword` refuses a two-character password.

SDET objects need this too. Tests should not reach into a page and smash internal locators from fifty classes. They should call `loginPage.login(user)`. When a locator changes, you fix one private field.

## 3. Real-Life Analogy

An ATM.

You do not open the vault. You press buttons: deposit, withdraw, balance.

```text
Vault          = private fields
Buttons        = public methods
Your wallet    = other classes
```

A capsule of medicine: the powder is inside. You swallow the capsule. You do not scoop the powder with your fingers. That is the word **encapsulate**.

A restaurant kitchen: guests order from the menu. They do not walk in and stir the sauce. The menu is the public API.

## 4. Illustrated Explanation

Without encapsulation:

```text
main
 └── account.balance = -50;   // nonsense, but allowed
 └── user.password = "x";     // too weak, but allowed
```

With encapsulation:

```text
Other class
    │
    │  getUsername()
    │  setPassword(...)
    │  deposit(20)
    ▼
┌──────────────────────────┐
│  private double balance  │
│  private String password │
│                          │
│  public methods enforce  │
│  the rules               │
└──────────────────────────┘
```

Getters and setters:

```text
getter  getUsername()     →  read
setter  setUsername(...)  →  write, optionally with checks
```

Encapsulation is not "I wrote get and set for every field." A private field with **no** setter is allowed. A password might be set only in the constructor. A balance might change only through `deposit` and `withdraw`.

```text
GOOD encapsulation
  private balance
  no setBalance
  deposit / withdraw only

WEAK encapsulation
  private balance
  public setBalance(double anything)  ← the vault door is still wide open
```

## 5. Syntax / Concept

Make fields private:

```java
private String username;
private String password;
```

Public getter:

```java
public String getUsername() {
    return username;
}
```

Public setter with a rule:

```java
public void setPassword(String password) {
    if (password == null || password.length() < 8) {
        throw new IllegalArgumentException("password too short");
    }
    this.password = password;
}
```

Naming convention (JavaBeans style, used everywhere):

```text
boolean field active  →  isActive() / setActive(boolean)
other fields          →  getName() / setName(String)
```

Constructor can still set private fields — the constructor is inside the class.

```java
public User(String username, String password) {
    this.username = username;
    setPassword(password); // reuse the rule
}
```

From another class:

```java
user.getUsername();
user.setPassword("Secret123");
// user.password = "x";  // does not compile
```

`private` means "only code inside this class." Chapter 45 will compare `public`, `protected`, and package-private.

## 6. Simple Example

```java
class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("password too short");
        }
        this.password = password;
    }

    public boolean passwordMatches(String attempt) {
        return password.equals(attempt);
    }
}

public class EncapsulationDemo {

    public static void main(String[] args) {
        User john = new User("john", "Test1234");
        System.out.println(john.getUsername());
        System.out.println(john.passwordMatches("Test1234"));
        john.setPassword("NewPass1");
        System.out.println(john.passwordMatches("NewPass1"));
    }
}
```

Expected output:

```text
john
true
true
```

There is no `getPassword()` on purpose. Tests can still check `passwordMatches`. The raw secret does not have to travel.

## 7. Real-World Example

Bank balance without `setBalance`:

```java
class BankAccount {
    private String owner;
    private double balance;

    public BankAccount(String owner, double openingBalance) {
        this.owner = owner;
        this.balance = openingBalance;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("deposit must be positive");
        }
        balance = balance + amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance = balance - amount;
        return true;
    }
}

public class EncapsulatedAccount {

    public static void main(String[] args) {
        BankAccount account = new BankAccount("Alice", 100.00);
        account.deposit(20.00);
        System.out.println(account.getOwner() + " " + account.getBalance());
        System.out.println("Withdraw 500? " + account.withdraw(500.00));
    }
}
```

The rule "you cannot overdraw" lives in one place.

## 8. SDET Example

A login page that hides selectors:

```java
class LoginPage {
    private String url;
    private String usernameBox = "#username";
    private String passwordBox = "#password";
    private String submitButton = "#login";

    public LoginPage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void login(String username, String password) {
        System.out.println("Open " + url);
        System.out.println("Type " + username + " into " + usernameBox);
        System.out.println("Type ******** into " + passwordBox);
        System.out.println("Click " + submitButton);
    }
}

public class EncapsulatedLoginPage {

    public static void main(String[] args) {
        LoginPage page = new LoginPage("https://qa.shop.example/login");
        page.login("standard_user", "secret_sauce");
    }
}
```

When `#username` becomes `#user-name`, you change one private field. Tests still call `login`. That is Page Object Model in miniature.

## 9. Break the Code

Public fields:

```java
class User {
    public String password;
}
```

Anyone can write `user.password = "a"`.

Empty setters:

```java
public void setBalance(double balance) {
    this.balance = balance;
}
```

Private did nothing useful. Any class can still smash the balance.

A getter that hands out a live internal object you then mutate (preview): if you later store a `List` and `return items;` the caller can `clear()` your list. For now, know the idea: getters should not secretly give away the keys to the vault.

Trying to read `user.password` from `main` after it is private: compiler error. That is a **good** break. Listen to it.

## 10. Debug

If `main` cannot see a field, that is encapsulation working. Use a method.

If invalid data still gets in, your setter or constructor does not validate. Put the rule in the door, not in a comment.

If tests break every time a locator changes, you leaked private details. Search for `#username` outside `LoginPage`. Move it back inside.

Debugger: you can still *see* private fields in the Variables view. The debugger is a mechanic in the garage, not a guest in the restaurant. Encapsulation restricts **code**, not your debugger.

## 11. Student Exercise

Turn `User` into encapsulated form:

- private `username`, `email`, `role`
- constructor sets all three
- getters for all three
- setter only for `email` (must contain `@`)
- no setter for `username` (immutable identity)
- `boolean isAdmin()`

In `main`, construct a user, print getters, change email, try to imagine changing username (you should not be able to).

## 12. Challenge

Write `BankAccount` as in the real-world example.

Add `transferTo(BankAccount other, double amount)` that withdraws from this account and deposits into the other, or does nothing if withdraw fails.

Write `main` that proves:

- Alice can transfer 30 to Bob
- Alice cannot transfer 10_000 to Bob
- nobody can write `alice.balance = ...` from `main`

Print both balances after each attempt.

## 13. Knowledge Check

1. What is encapsulation in working language?
2. What keyword hides a field from other classes?
3. What is a getter?
4. What is a setter?
5. Why might a field have a getter but no setter?
6. Why is `setBalance` often a bad idea for an account?
7. Write a private field and its getter for `username`.
8. True or false: encapsulation means "write get/set for every field."
9. How does encapsulation help when a CSS selector changes?
10. Why might `User` have `passwordMatches` instead of `getPassword`?

## 14. Interview Question

**Question:** What is encapsulation, and how do you do it in Java?

A strong answer:

> Encapsulation keeps an object's data private and exposes a controlled API. In Java I mark fields private and provide methods — getters, setters with validation, or better operations like deposit and login. The goal is not to generate empty getters and setters. The goal is to protect invariants: balance cannot go negative, password cannot be two characters, locators are not copied into every test. If a field should not change, I omit the setter. Page objects are encapsulation: tests call login(), they do not poke private selectors.

## 15. Homework

Build `HomeworkEncapsulation` with encapsulated `User` and `BankAccount`.

Rules:

- `User`: private fields, getter for username, `setPassword` with length >= 8, no `getPassword`
- `BankAccount`: no `setBalance`, only `deposit` / `withdraw` / `getBalance`

Show illegal field access in a comment (the line that would not compile).

Write three sentences: what is private, what is the door, what rule the door enforces.

---

## Answer Key

1. Keep data private; let other code work through methods that can enforce rules.
2. `private`
3. A method that returns a field (or a safe view of it), usually `getX()`.
4. A method that updates a field, usually `setX(...)`, often with checks.
5. The value should be read-only after construction (id, username).
6. It lets callers break rules (negative money, skipping withdraw checks).
7. `private String username;` and `public String getUsername() { return username; }`
8. False. That can be a hollow version of the idea.
9. The selector lives in one private place; tests keep calling the public action.
10. So callers can verify a guess without receiving the raw secret.
