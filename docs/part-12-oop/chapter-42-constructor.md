# Chapter 42 — Constructor

## 1. Today's Goal

By the end of this lesson, you will write a **constructor** so a new object starts life with real data.

You will replace this two-step dance:

```java
User john = new User();
john.username = "john";
```

with a single honest birth:

```java
User john = new User("john", "standard");
```

## 2. Why It Matters

An object with empty fields is a landmine. Someone will call `login()` before setting `username`. You will get `null` in a request, a blank text box, or `NullPointerException`.

A constructor is the doorway. If the only way to build a `User` is `new User(username, role)`, nobody can "forget" the username.

SDET objects should be born valid:

- a `User` always has a username
- a `TestCase` always has a name
- a `LoginPage` always has a driver (later)

## 3. Real-Life Analogy

A new employee cannot start on Monday with a blank badge.

```text
HR constructor
  takes: name, role
  produces: a person who already has a badge
```

You do not hire someone and hope they write their own name on the badge later.

A restaurant table is "constructed" with a number and a capacity before guests sit. An unnumbered table is a waiter's nightmare.

A form that requires Name and Email before Submit is a constructor. Optional nickname can wait. Required identity cannot.

## 4. Illustrated Explanation

Without a constructor you write:

```text
new User()
   └── object exists
          username = null
          role = null
   └── later, maybe, fields get values
   └── later, maybe, someone uses it too soon
```

With a constructor:

```text
new User("john", "standard")
   └── constructor runs
          this object's username = "john"
          this object's role = "standard"
   └── object is ready
```

```text
User john = new User("john", "standard");
                 │
                 └── this call RUNS the constructor
```

Java's gift if you write **no** constructor at all:

```text
default constructor  →  public User() { }
```

The moment you write **any** constructor, that gift goes away. `new User()` will no longer compile unless you also write a no-argument constructor yourself.

You can have more than one constructor (overloading, same idea as method overloading):

```text
User()
User(String username)
User(String username, String role)
```

Java picks by the arguments you pass.

## 5. Syntax / Concept

A constructor:

- has the **same name as the class**
- has **no return type** (not even `void`)
- runs when `new` is used

```java
class User {
    String username;
    String role;

    User(String usernameValue, String roleValue) {
        username = usernameValue;
        role = roleValue;
    }
}
```

Call:

```java
User john = new User("john", "standard");
```

If the parameter names match the fields, you need `this` (next chapter). Today we used different parameter names so the assignment is obvious.

You can still set remaining fields later if they are not required at birth. Prefer putting required data in the constructor.

A constructor can contain checks:

```java
User(String usernameValue, String roleValue) {
    if (usernameValue == null || usernameValue.isBlank()) {
        throw new IllegalArgumentException("username required");
    }
    username = usernameValue;
    role = roleValue;
}
```

`throw` is a preview of exceptions. The idea: refuse to build a broken user.

Constructors are not methods. You do not write `void User(...)`. You do not call them like `john.User(...)`. `new` is the call.

## 6. Simple Example

```java
class User {
    String username;
    String role;

    User(String usernameValue, String roleValue) {
        username = usernameValue;
        role = roleValue;
    }

    void introduce() {
        System.out.println("I am " + username + " (" + role + ")");
    }
}

public class ConstructorDemo {

    public static void main(String[] args) {
        User john = new User("john", "standard");
        User admin = new User("admin", "admin");

        john.introduce();
        admin.introduce();
    }
}
```

Expected output:

```text
I am john (standard)
I am admin (admin)
```

No field assignments in `main`. Birth included identity.

## 7. Real-World Example

Bank account that cannot be born without an owner and an opening balance:

```java
class BankAccount {
    String owner;
    double balance;

    BankAccount(String ownerValue, double openingBalance) {
        owner = ownerValue;
        balance = openingBalance;
    }

    void print() {
        System.out.println(owner + " has " + balance);
    }
}

public class AccountConstructorDemo {

    public static void main(String[] args) {
        BankAccount checking = new BankAccount("Alice", 100.00);
        checking.print();
    }
}
```

Overloaded constructors for a product: with and without a discount code.

```java
class Product {
    String name;
    double price;
    String coupon;

    Product(String nameValue, double priceValue) {
        name = nameValue;
        price = priceValue;
        coupon = "NONE";
    }

    Product(String nameValue, double priceValue, String couponValue) {
        name = nameValue;
        price = priceValue;
        coupon = couponValue;
    }
}
```

## 8. SDET Example

Test users that must have credentials:

```java
class User {
    String username;
    String password;

    User(String usernameValue, String passwordValue) {
        username = usernameValue;
        password = passwordValue;
    }

    void printAttempt() {
        System.out.println("Login: " + username);
    }
}

public class SdetConstructor {

    public static void main(String[] args) {
        User standard = new User("standard_user", "secret_sauce");
        User locked = new User("locked_out_user", "secret_sauce");

        standard.printAttempt();
        locked.printAttempt();
    }
}
```

A page object later will look like:

```java
class LoginPage {
    String baseUrl;

    LoginPage(String baseUrlValue) {
        baseUrl = baseUrlValue;
    }
}

// LoginPage page = new LoginPage("https://qa.shop.example");
```

You do not want a `LoginPage` that does not know which environment it is talking to.

## 9. Break the Code

Writing a return type by accident makes it a method, not a constructor:

```java
class User {
    void User(String usernameValue) { // this is a method named User
    }
}
```

Then `new User("john")` fails if there is no real constructor with a String, because the default constructor takes no arguments.

Calling `new User()` after you added `User(String, String)`:

```java
User john = new User(); // does not compile
```

The default constructor disappeared.

Forgetting to assign inside the constructor:

```java
User(String usernameValue, String roleValue) {
    // empty — fields stay null
}
```

The object is born, but still empty. `main` looks clean. Data is still missing.

## 10. Debug

If `new User("john")` does not compile, read the constructor parameter list. Count and types must match.

If fields are still `null` after `new`, the constructor never assigned them. Put a `System.out.println` as the first and last lines of the constructor to prove it ran.

If you expected `new User()` to work "like before," remember: writing any constructor removes the free default. Either add `User() { }` or always pass arguments.

Debugger:

1. Breakpoint on the first line **inside** the constructor.
2. Step. Watch fields change from `null` to real values.
3. Step out. The object in `main` should already be filled.

## 11. Student Exercise

Write `User` with fields `username` and `email`.

Write a constructor that takes both and assigns them.

In `main`, create two users only with `new User(...)`. Print both.

Then add a second constructor that takes only `username` and sets email to `"unknown@example.com"`. Create a third user with that constructor.

## 12. Challenge

Write `TestCase` with:

- `String name`
- `String status`  (`"NOT_RUN"`, `"PASSED"`, `"FAILED"`)
- `long durationMs`

Rules:

1. The main constructor takes `name` only. It sets status to `"NOT_RUN"` and duration to `0`.
2. A second constructor takes `name`, `status`, and `durationMs` for loading historical results.
3. Refuse a blank name with `IllegalArgumentException`.

In `main`, construct one new test and one historical failed test. Print them. Try (in a comment, or with a try/catch if you already peeked at exceptions) what happens if name is `""`.

## 13. Knowledge Check

1. What is a constructor?
2. How must a constructor be named?
3. Does a constructor have a return type?
4. When does a constructor run?
5. What is the default constructor?
6. What happens to the default constructor when you write your own?
7. Why put required fields in the constructor instead of setting them later?
8. Write a constructor header for `User` that takes a username.
9. True or false: you can overload constructors.
10. Why would an SDET put `baseUrl` in a `LoginPage` constructor?

## 14. Interview Question

**Question:** What is a constructor in Java, and why do we use one?

A strong answer:

> A constructor is a special block with the same name as the class and no return type. It runs when you use new. I use it to put the object into a valid starting state — for example new User("john", "standard") so username is never forgotten. If I write no constructor, Java provides a no-argument default. If I write any constructor, that default goes away unless I add it myself. Constructors can be overloaded. In tests, I construct User and page objects with the data they must have, so a test cannot run against a half-built object.

## 15. Homework

Write `HomeworkConstructor` with:

- `User(String username, String password, String role)`
- `void printSafe()` — username, role, password masked

Create standard, admin, and guest using only constructors.

Add `Order(String id, double total)` that sets status `"NEW"`. Print two orders.

In a comment: `If it must exist, pass it to the constructor.`

---

## Answer Key

1. Code that runs at object creation to set up the new object.
2. Exactly the same as the class name.
3. No. Not even `void`.
4. When you use `new ClassName(...)`.
5. A no-argument constructor Java gives you if you write none.
6. It is no longer provided. `new ClassName()` fails unless you write it.
7. So the object cannot exist in a half-ready, null-filled state.
8. `User(String username) { ... }`
9. True.
10. So every page object knows which environment it is driving; a page without a URL is not ready.
