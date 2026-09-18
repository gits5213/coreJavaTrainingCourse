# Chapter 46 — `static`

## 1. Today's Goal

By the end of this lesson, you will explain that **`static` belongs to the class, not to an instance**, and you will use it sparingly.

You will know why `main` is static, why `Math.max` is static, and why making *everything* static is how beginners accidentally leave OOP.

## 2. Why It Matters

Part 10 used `public static` methods so you could call them from `main` without objects. That was a ramp. If you stay on the ramp forever, you will write a `User` class with `static String username` and then wonder why john and admin share one name.

SDET code needs both:

- **instance** data: this page's driver, this user's password
- **static** data: a constant timeout, a shared counter of how many tests started, `Math` utilities with no object to own them

The mistake is using static as a global variable closet.

## 3. Real-Life Analogy

A school classroom.

```text
Each student (object)
  own backpack
  own name
  own grade

The classroom (class)
  one clock on the wall     ← static
  one fire-escape poster    ← static
```

If you make "name" a classroom poster, every student is suddenly named the same.

A bank:

- each account has its own balance (instance)
- the bank's routing number is shared (static constant)

A cookie cutter does not taste like sugar. Sugar belongs to each cookie. The cutter's metal type is shared.

## 4. Illustrated Explanation

```text
class User {
    static int createdCount;     ← one box for the whole class
    String username;             ← one box PER object
}

User john  = new User("john");
User admin = new User("admin");
```

```text
CLASS User
  createdCount = 2
  method: getCreatedCount()

OBJECT john              OBJECT admin
  username = john          username = admin
```

Calling:

```text
john.getUsername()           instance — needs an object
User.getCreatedCount()       static — use the class name
```

You *can* write `john.getCreatedCount()` in some cases. Do not. It looks like the count belongs to john. Call static members on the class: `User.getCreatedCount()`.

`main` is static because the JVM needs a starting door **before** any object exists.

```text
JVM starts
  └── no User objects yet
  └── needs a method it can call on the class itself
  └── public static void main(String[] args)
```

## 5. Syntax / Concept

Static field:

```java
static int createdCount = 0;
```

Static method:

```java
public static int getCreatedCount() {
    return createdCount;
}
```

Static constant (common, honest use — Chapter 47 will add `final`):

```java
public static final int DEFAULT_TIMEOUT_SECONDS = 10;
```

Instance methods can read static data (they can see the classroom clock).

Static methods **cannot** use `this` or instance fields directly:

```java
public static void broken() {
    System.out.println(username); // no "the" username
}
```

There is no current object.

When static is honest:

- `Math.max(a, b)` — no object needed
- counters and caches you truly want shared (careful in parallel tests)
- `main`
- constants

When static is a trap:

- `static String username` on `User`
- `static WebDriver driver` shared by all tests (classic flake factory)
- a `Utils` class where you hid all your objects' brains

Do **not** "make it static so main can call it" if the method needs object data. Create an object, then call the instance method.

## 6. Simple Example

```java
class User {
    private static int createdCount = 0;
    private String username;

    public User(String username) {
        this.username = username;
        createdCount++;
    }

    public String getUsername() {
        return username;
    }

    public static int getCreatedCount() {
        return createdCount;
    }
}

public class StaticDemo {

    public static void main(String[] args) {
        User john = new User("john");
        User admin = new User("admin");

        System.out.println(john.getUsername());
        System.out.println(admin.getUsername());
        System.out.println("Created: " + User.getCreatedCount());
    }
}
```

Expected output:

```text
john
admin
Created: 2
```

Two usernames. One counter.

## 7. Real-World Example

Interest rate shared by all savings accounts this quarter, balances still per account:

```java
class SavingsAccount {
    private static double annualRate = 0.04;
    private String owner;
    private double balance;

    public SavingsAccount(String owner, double balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public static void setAnnualRate(double annualRate) {
        SavingsAccount.annualRate = annualRate;
    }

    public double projectedInterest() {
        return balance * annualRate;
    }

    public void print() {
        System.out.println(owner + " interest " + projectedInterest());
    }
}

public class StaticBankDemo {

    public static void main(String[] args) {
        SavingsAccount a = new SavingsAccount("Alice", 1000);
        SavingsAccount b = new SavingsAccount("Bob", 2000);
        a.print();
        b.print();
        SavingsAccount.setAnnualRate(0.05);
        a.print();
        b.print();
    }
}
```

Changing the rate once changes the rule for every account. That is a real shared policy — not a fake shared name.

## 8. SDET Example

Honest static: a constant and a pure helper.

```java
class WaitTimes {
    public static final int SHORT_SECONDS = 5;
    public static final int LONG_SECONDS = 30;
}

class StatusCodes {
    public static boolean isSuccess(int code) {
        return code >= 200 && code < 300;
    }
}

public class StaticSdetDemo {

    public static void main(String[] args) {
        System.out.println("Short wait: " + WaitTimes.SHORT_SECONDS);
        System.out.println("201 success? " + StatusCodes.isSuccess(201));
        System.out.println("404 success? " + StatusCodes.isSuccess(404));
    }
}
```

Dangerous static (do not copy into a real suite):

```java
class DriverManagerWrong {
    public static String currentBrowser; // all tests share one slot
}
```

If two tests run at once, they overwrite each other's browser. Prefer an object per test, or a carefully designed thread-safe manager much later.

Counting tests with static is fine for a demo, risky if you assume the number is always 0 at process start in a reused JVM. Know that static lives as long as the class is loaded.

## 9. Break the Code

Shared identity:

```java
class User {
    static String username;
}

public class StaticTrap {

    public static void main(String[] args) {
        User a = new User();
        User b = new User();
        a.username = "john";
        b.username = "admin";
        System.out.println(a.username); // admin — one shared field
    }
}
```

Using instance data from static:

```java
class User {
    String username;

    static void print() {
        System.out.println(username); // does not compile
    }
}
```

Making `login` static on `LoginPage` so you never construct a page: then you cannot have two pages, two drivers, or two environments at once.

## 10. Debug

`non-static variable cannot be referenced from a static context` means you are in `main` (or another static method) and you used a field that belongs to an object. Fix: `User user = new User("john");` then `user.getUsername()`.

If all objects show the same data, search for `static` on fields that should be per object. Remove it.

If parallel tests contaminate each other, search for `static WebDriver` and static mutable test data. That is a usual root cause.

Debugger: static fields appear on the class, not inside each object expansion. If you expand `john` and do not see `createdCount`, look at the class-level statics.

## 11. Student Exercise

Write `TestCase` with:

- instance fields `name` and `passed`
- static field `executedCount`
- constructor that increments `executedCount`
- instance method `printResult()`
- static method `getExecutedCount()`

In `main`, create three test cases with different names and pass/fail. Print each result. Print the count using `TestCase.getExecutedCount()` (class name, not an object).

## 12. Challenge

1. Write `MathStyle.max(int a, int b)` as a static helper. Use it from `main`.
2. Write `User` with instance `username` and static `createdCount`.
3. Write a short comment: why `max` should be static and `getUsername` should not.
4. Break a copy of `User` on purpose with `static String username`, print the bug, then fix it.

Optional: explain why `static WebDriver driver` is a bad default for a class named `BrowserFactory` when two tests run together.

## 13. Knowledge Check

1. What does `static` mean for a field?
2. What does `static` mean for a method?
3. Why is `main` static?
4. Can a static method use `this`?
5. How should you call `User.getCreatedCount()` — on the class or on `john`?
6. Why is `static String username` on `User` a bug?
7. Give one honest use of a static method.
8. Give one honest use of a static field in tests.
9. True or false: if `main` cannot call a method, you should make that method static.
10. Why is a static `WebDriver` dangerous?

## 14. Interview Question

**Question:** What is `static` in Java? When would you use it?

A strong answer:

> static means the member belongs to the class, not to each object. There is one copy of a static field for the class. A static method can run without an instance and cannot use this or instance fields. I use static for constants, pure helpers like Math.max, and main. I do not make User.username static, because each user needs their own name. I am careful with static mutable state in tests — a static WebDriver or static current user will leak between tests, especially in parallel. Static is a tool, not the default for every method.

## 15. Homework

Write `HomeworkStatic` with `User` (instance username/role, static createdCount) and `StatusCodes.isSuccess(int)`.

Create two users. Print instance data and the count.

In comments, write five lines titled `I will not make everything static because...`

Leave `main` static. That is required, not a lifestyle.

---

## Answer Key

1. One shared variable for the whole class, not one per object.
2. A method you call on the class; no current object.
3. The JVM must start the program before objects exist.
4. No.
5. On the class: `User.getCreatedCount()`.
6. All User objects would share one username.
7. `Math.max`, `StatusCodes.isSuccess`, parsing helpers with no object state.
8. A constant timeout, or a counter — with care.
9. False. Create an object and call an instance method if the work needs object data.
10. Tests overwrite the same driver; parallel runs collide; leftover state flakes later tests.
