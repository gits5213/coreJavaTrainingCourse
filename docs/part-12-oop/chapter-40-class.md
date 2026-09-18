# Chapter 40 — Class

## 1. Today's Goal

By the end of this lesson, you will write a **class** as a blueprint: fields for data, methods for behavior.

You will build a `User` class that knows a username and can introduce itself. That `User` class is the same idea as `LoginPage` later. Only the fields change.

## 2. Why It Matters

A class is the unit of design in Java.

When a teammate says "add a field to User," they mean: change the blueprint so every user object can remember one more fact. When they say "LoginPage needs a clickLogin method," they mean: add behavior to that blueprint.

If you only know `main`, you can still write tests, but they will look like scripts. Scripts rot. Classes can be reused, tested, and named.

SDET work lives in classes:

- `User` — who is logging in
- `Order` — what was purchased
- `LoginPage` — how the UI is driven
- `ApiClient` — how HTTP is called

## 3. Real-Life Analogy

A blank employment form.

```text
EMPLOYEE FORM (the class)
  Name: ________
  Role: ________
  Badge id: ________
```

The form is not a person. It is the shape of a person in this company.

A cookie recipe is a class. It lists ingredients (fields) and steps (methods). The recipe is not edible.

A page template in a design tool is a class. "Login page has username, password, and a button" is the blueprint. Each environment (QA, staging) still fills it with different data.

## 4. Illustrated Explanation

```text
┌─────────────────────────────────────┐
│  class User                         │  ← blueprint
│                                     │
│  Fields (data)                      │
│    String username                  │
│    String role                      │
│                                     │
│  Methods (behavior)                 │
│    void introduce()                 │
│    boolean isAdmin()                │
└─────────────────────────────────────┘
              │
              │  new User()  (next chapter)
              ▼
     objects appear in memory
```

A class is a type. After you write `class User`, Java knows `User` the way it already knows `String`.

```text
String name;   // type String, already built into Java
User   accountHolder;  // type User, you invented it
```

Inside one `.java` file used for learning:

```text
class User { ... }           // the blueprint

public class UserClassDemo { // the class with main, to run the lesson
    public static void main(String[] args) { ... }
}
```

Java allows only one **public** class per file, and the file name must match that public class. Helper classes in the same file stay without `public`. In real projects, each important class usually gets its own file. We will do that more strictly in the packages part.

## 5. Syntax / Concept

Minimal class:

```java
class User {
}
```

That compiles. It is an empty blueprint. Empty blueprints are not useful for long.

Add fields:

```java
class User {
    String username;
    String role;
}
```

Fields are variables that belong to the object (once an object exists). They are declared inside the class, **outside** methods.

Add a method:

```java
class User {
    String username;
    String role;

    void introduce() {
        System.out.println("I am " + username + " (" + role + ")");
    }
}
```

Notice: `introduce` is **not** `static`. It is an instance method. It uses `username` and `role` from **this** user. Chapter 41 will call it on an object. Chapter 46 will explain `static` honestly.

A method can use fields and parameters together:

```java
boolean hasUsername(String expected) {
    return username.equals(expected);
}
```

Class names:

- start with a capital letter: `User`, `LoginPage`, `BankAccount`
- use CamelCase
- are nouns (a thing), not verbs

Field names:

- start with lowercase: `username`, `role`, `balance`

You already used `public class Something` in every program. That `Something` was a class whose job was mostly to hold `main`. Now classes also hold the business.

## 6. Simple Example

```java
class User {
    String username;
    String role;

    void introduce() {
        System.out.println("I am " + username + " (" + role + ")");
    }

    boolean isAdmin() {
        return role.equals("admin");
    }
}

public class UserClassDemo {

    public static void main(String[] args) {
        User user = new User();
        user.username = "john";
        user.role = "standard";
        user.introduce();
        System.out.println("Admin? " + user.isAdmin());
    }
}
```

Expected output:

```text
I am john (standard)
Admin? false
```

Read this slowly:

1. `class User` is the blueprint.
2. `new User()` builds one object (full detail in the next chapter).
3. `user.username = "john"` fills a field on **that** object.
4. `user.introduce()` runs the method using that object's data.

We are still assigning fields directly. Encapsulation (Chapter 44) will close that door. Learn the rooms of the house before you install locks.

## 7. Real-World Example

A product in a shop:

```java
class Product {
    String name;
    double price;
    int stock;

    void printTag() {
        System.out.println(name + " costs " + price + " (" + stock + " in stock)");
    }

    boolean isInStock() {
        return stock > 0;
    }
}

public class ProductClassDemo {

    public static void main(String[] args) {
        Product tea = new Product();
        tea.name = "Green Tea";
        tea.price = 4.50;
        tea.stock = 12;
        tea.printTag();
        System.out.println("Can sell? " + tea.isInStock());
    }
}
```

A bank account sketch:

```java
class BankAccount {
    String owner;
    double balance;

    void printBalance() {
        System.out.println(owner + " has " + balance);
    }
}
```

The business words become class names. That is not decoration. That is how teams talk.

## 8. SDET Example

A test user blueprint:

```java
class User {
    String username;
    String password;
    String role;

    void describeForReport() {
        System.out.println("Test user: " + username + " / role=" + role);
    }
}

public class SdetUserClass {

    public static void main(String[] args) {
        User standard = new User();
        standard.username = "standard_user";
        standard.password = "secret_sauce";
        standard.role = "shopper";
        standard.describeForReport();
    }
}
```

A page is the same shape, different fields:

```java
class LoginPage {
    String url;
    String usernameSelector;
    String passwordSelector;

    void describe() {
        System.out.println("Login page at " + url);
    }
}
```

You do not need Selenium to understand `LoginPage`. A page object is a class that remembers locators (data) and offers actions (methods).

## 9. Break the Code

Putting fields inside `main` is not a class of `User`. It is leftover procedural code:

```java
public class BrokenClass {

    public static void main(String[] args) {
        String username;
        String role;
        // these are local variables, not User fields
    }
}
```

Declaring a class but putting methods *outside* it does not compile:

```java
class User {
    String username;
}

void introduce() { // illegal: method must live inside a class
}
```

Naming the class `user` (lowercase) compiles, but it fights Java convention and looks like a variable. Interviewers notice.

Making every method `static` inside `User` "so main can call it" is the trap from Part 10. Then you no longer have per-user data. Chapter 46 will unpack this. For now: instance methods on `User` should not be `static`.

## 10. Debug

If IntelliJ says `cannot find symbol: class User`, the class is misspelled or in another file you did not compile.

If it says `non-static variable username cannot be referenced from a static context`, you tried to use a field from `main` without an object:

```java
public static void main(String[] args) {
    System.out.println(username); // there is no "the" username yet
}
```

Fix: create an object, then use `user.username`.

If a method cannot see a field, check braces. The field must be inside the same class, not inside another method.

Draw the class on paper with two boxes: DATA and ACTIONS. If an action needs data that is not in the DATA box, you are missing a field.

## 11. Student Exercise

Write a `User` class with:

- `String username`
- `String email`
- `void printProfile()` that prints both

Write a `public class UserExercise` with `main` that creates one user, sets both fields, and calls `printProfile()`.

Then write a second class in the same file (not public): `TestCase` with `String name` and `boolean passed`, plus `void printResult()`. Create one `TestCase` in `main` as well.

## 12. Challenge

Design `Order` as a class:

- `String orderId`
- `String customerName`
- `double total`
- `String status` (`"NEW"`, `"PAID"`, `"SHIPPED"`)

Methods:

- `void printSummary()`
- `boolean isPaid()` — true when status is `"PAID"` or `"SHIPPED"`

In `main`, create one order that is NEW and one that is PAID. Print both summaries and both `isPaid()` results.

Do not use inheritance. One class is enough.

## 13. Knowledge Check

1. What is a class?
2. What is a field?
3. Where do you declare fields — inside methods or inside the class body?
4. Why are class names capitalized?
5. Write a tiny `User` class with one `String` field.
6. Is `introduce()` in this chapter `static`? Why or why not?
7. How many public classes may one `.java` file have?
8. True or false: `class User { }` is a complete (if empty) class.
9. What is wrong with storing twenty users as `user1Name`, `user2Name`, ... in `main`?
10. How is `LoginPage` the same *shape* as `User`?

## 14. Interview Question

**Question:** What is a class in Java?

A strong answer:

> A class is a blueprint. It defines the data (fields) and the behavior (methods) that objects of that type will have. For example, a User class might hold username and role, and offer introduce() or isAdmin(). The class is the type. Objects are the living values created from it with new. In automation, LoginPage is a class: locators are fields, clickLogin is a method. I keep class names as nouns, capitalized, one main idea per class.

## 15. Homework

Write `HomeworkUserClass` with a `User` class that has `username`, `password`, and `role`.

Add methods:

- `void maskAndPrint()` — prints username, role, and `********` instead of the real password
- `boolean canAccessAdminPanel()` — true only when role is `"admin"`

Create three users in `main`: standard, admin, locked. Call both methods on each.

In a comment at the top of the file, write: `Class = blueprint. Object comes next.`

---

## Answer Key

1. A blueprint that defines fields and methods for a type of thing.
2. A variable that belongs to the class/object — the data the thing remembers.
3. Inside the class body, outside of methods.
4. Java convention: types start with a capital letter so they look different from variables.
5. `class User { String username; }`
6. No. It uses each object's own `username` and `role`.
7. One. The file name must match that public class.
8. True.
9. You copied the same idea without a blueprint. A `User` class scales; parallel variables do not.
10. Both group data plus methods: `User` has credentials and identity methods; `LoginPage` has locators and UI actions.
