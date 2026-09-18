# Chapter 43 — `this`

## 1. Today's Goal

By the end of this lesson, you will use **`this`** to mean "this object right here," especially when a parameter name matches a field name.

You will understand this constructor:

```java
User(String username, String role) {
    this.username = username;
    this.role = role;
}
```

## 2. Why It Matters

Professionals like the same name for the same idea. The field is `username`. The constructor argument is `username`. Without `this`, Java cannot tell them apart, and you will write a classic silent bug: the field stays `null` because you assigned the parameter to itself.

SDET page objects and data classes use `this` in almost every constructor. You will also use `this` to pass the current page into a helper, and to call another constructor in the same class.

## 3. Real-Life Analogy

You are in a meeting. Two people are named Jordan.

Someone says "Jordan should take notes." You ask: **which** Jordan?

`this` is you tapping your own badge:

```text
this Jordan  = me, the person speaking
```

A package labeled "address" arrives at a building that also has a field called address. The clerk writes: **this building's** address = the address on the form.

## 4. Illustrated Explanation

Shadowing: a parameter hides a field with the same name.

```text
class User {
    String username;          ← field (the object's data)

    User(String username) {   ← parameter (a temporary incoming value)
        username = username;  ← BOTH names refer to the parameter
                               the field is never touched
    }
}
```

```text
WRONG
parameter username ──► assigned to parameter username
field username     ──► still null

RIGHT
this.username = username;
     │              │
     │              └── the parameter (incoming)
     └── the field on this object
```

`this` is a reference to the current object — the same kind of arrow as `john`, but from the inside.

```text
john.introduce();
        │
        └── inside introduce(), this == john

admin.introduce();
        │
        └── inside introduce(), this == admin
```

Calling another constructor with `this(...)`:

```text
User(String username) {
    this(username, "standard");  // call the two-argument constructor
}
```

That must be the **first** statement in the constructor.

## 5. Syntax / Concept

Assign fields when names match:

```java
this.username = username;
```

Call a method on the current object (usually optional, because Java assumes `this`):

```java
this.introduce();
introduce(); // same thing in typical instance methods
```

Constructor chaining:

```java
class User {
    String username;
    String role;

    User(String username) {
        this(username, "standard");
    }

    User(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
```

`this(...)` cannot be used after other statements. It has to come first.

You cannot use `this` in a `static` method. Static code is not running "on" an object. Chapter 46 will make that rule feel obvious.

`this` is not a field you declare. Java provides it inside instance methods and constructors.

## 6. Simple Example

```java
class User {
    String username;
    String role;

    User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    void introduce() {
        System.out.println("I am " + this.username + " (" + this.role + ")");
    }
}

public class ThisDemo {

    public static void main(String[] args) {
        User john = new User("john", "standard");
        john.introduce();
    }
}
```

Expected output:

```text
I am john (standard)
```

Inside `introduce`, `this.username` and `username` are the same because nothing is shadowing the field. The `this.` is extra clarity, not required there.

## 7. Real-World Example

Bank account with matching names:

```java
class BankAccount {
    String owner;
    double balance;

    BankAccount(String owner, double balance) {
        this.owner = owner;
        this.balance = balance;
    }

    void deposit(double amount) {
        this.balance = this.balance + amount;
    }
}

public class ThisBankDemo {

    public static void main(String[] args) {
        BankAccount account = new BankAccount("Alice", 100.00);
        account.deposit(25.00);
        System.out.println(account.owner + " has " + account.balance);
    }
}
```

Chaining constructors for a shop order:

```java
class Order {
    String id;
    String status;

    Order(String id) {
        this(id, "NEW");
    }

    Order(String id, String status) {
        this.id = id;
        this.status = status;
    }
}
```

`new Order("A-100")` becomes NEW. `new Order("A-100", "PAID")` loads a known status.

## 8. SDET Example

```java
class User {
    String username;
    String password;

    User(String username) {
        this(username, "Password1!");
    }

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    void printAttempt() {
        System.out.println("Login as " + this.username);
    }
}

public class SdetThis {

    public static void main(String[] args) {
        User defaultPassword = new User("john");
        User custom = new User("admin", "Admin!23");
        defaultPassword.printAttempt();
        custom.printAttempt();
    }
}
```

A page later:

```java
class LoginPage {
    String url;

    LoginPage(String url) {
        this.url = url;
    }
}
```

That one line is the most common `this` you will type in automation.

## 9. Break the Code

The silent self-assignment:

```java
class User {
    String username;

    User(String username) {
        username = username;
    }
}

public class ThisBug {

    public static void main(String[] args) {
        User john = new User("john");
        System.out.println(john.username); // null
    }
}
```

This compiles. It runs. It prints `null`. That is why it is dangerous.

Using `this(...)` too late:

```java
User(String username) {
    System.out.println("hi");
    this(username, "standard"); // does not compile
}
```

Using `this` in `main` (static):

```java
public static void main(String[] args) {
    System.out.println(this); // does not compile
}
```

## 10. Debug

If a field is `null` after a constructor that "looks right," look for `username = username`. Fix with `this.username = username`.

IntelliJ often grays out or warns: "The assignment to variable username has no effect." Believe that warning.

If `this(...)` does not compile, move it to the first line, and make sure you are calling another constructor of **this** class, not a method.

Debugger:

1. Break inside the constructor.
2. Inspect `this`. Expand it. Those are the fields.
3. Inspect the parameter `username`. Confirm it has the incoming value.
4. Step over `this.username = username`. Watch `this.username` change.

## 11. Student Exercise

Write `Product` with `String name` and `double price`.

Constructor parameters must be named `name` and `price`. Use `this`.

Add `void printTag()` using `this.name` and `this.price`.

In `main`, construct two products and print them.

Then add a one-argument constructor `Product(String name)` that calls `this(name, 0.0)`.

## 12. Challenge

Write `User` with `username`, `email`, `role`.

- Three-argument constructor assigns with `this`.
- Two-argument constructor `(username, email)` calls `this(username, email, "standard")`.
- One-argument constructor `(username)` calls `this(username, username + "@example.com")`.

Create three users, one with each constructor. Print all fields.

If you reverse the chaining order and get a compiler error, write that error in a comment. Learning the first-statement rule is part of the challenge.

## 13. Knowledge Check

1. What does `this` mean?
2. Why do we write `this.username = username`?
3. What is shadowing in this chapter's sense?
4. What is the silent bug if you write `username = username` in a constructor?
5. Can you use `this` in a `static` method?
6. What does `this(username, "standard")` do in a constructor?
7. Where must `this(...)` appear in a constructor?
8. True or false: `this.introduce()` and `introduce()` are usually the same in an instance method.
9. Does `this` need to be declared as a field?
10. Why do page objects use `this.url = url` so often?

## 14. Interview Question

**Question:** What is `this` in Java?

A strong answer:

> this is a reference to the current object. I use it most in constructors when parameters have the same names as fields: this.username = username copies the argument into the object's field. Without this, the parameter shadows the field and I might assign the parameter to itself, leaving the field null. this() can also call another constructor in the same class, and it must be the first statement. this is not available in static methods because static code does not run on an instance.

## 15. Homework

Rewrite any `User` constructor from Chapter 42 so parameter names match field names, using `this`.

Add constructor chaining: `User(String username)` → default role `"standard"` and default password `"ChangeMe1!"`.

Print three users. In a comment, paste the wrong line `username = username` and write why you will never ship it.

---

## Answer Key

1. A reference to the current object — the object whose method or constructor is running.
2. To assign the parameter into the field when both have the same name.
3. A parameter (or local variable) hiding a field with the same name.
4. The field stays `null` (or unchanged). The parameter is assigned to itself.
5. No.
6. It calls another constructor of the same class.
7. As the first statement.
8. True, when nothing else is going on.
9. No. Java provides it.
10. Constructors take `url` (or `driver`) and must store it on this page object.
