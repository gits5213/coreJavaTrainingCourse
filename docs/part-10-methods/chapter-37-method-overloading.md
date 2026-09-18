# Chapter 37 — Method Overloading

## 1. Today's Goal

By the end of this lesson, you will understand **method overloading**: the same method name with **different parameter lists**.

You will see why `login(String username, String password)` and a later `login(User user)` can both exist.

Today we will overload with types you already know, because `User` objects arrive in the OOP part. The idea is the same.

## 2. Why It Matters

Humans like one verb for one idea.

You `print` an int, a double, or a String. Java's `System.out.println` is overloaded. You should not need `printlnInt`, `printlnDouble`, and `printlnString` as names.

In SDET frameworks:

```text
click(String selector)
click(By locator)        // later with Selenium
login(username, password)
login(User user)
```

One name, several ways to start the same kind of work.

## 3. Real-Life Analogy

The verb "open."

```text
open(a door)
open(a jar)
open(a file)
```

Same name in English. Different objects. You still understand.

A coffee shop `order`:

```text
order("latte")
order("latte", "large")
order("latte", "large", "oat milk")
```

Same action, more details.

## 4. Illustrated Explanation

```text
login
  ├── login(String username, String password)
  └── login(User user)            // later, when User exists

Same name
+ different parameter list
= overloading
```

Java chooses which method to run by looking at the arguments you pass.

```text
login("john", "Test123")
        ↓
matches (String, String)

login(user)
        ↓
matches (User)   // later
```

Not overloading:

```text
login vs logIn vs doLogin   ← different names, not overloading
```

Not allowed as the only difference:

```text
int add(int a, int b)
double add(int a, int b)   ← same parameters, only return type differs
```

Java cannot tell those two apart from the call `add(1, 2)`.

## 5. Syntax / Concept

Overloading means:

```text
Same method name
+ different number of parameters
  OR different types
  OR different type order
```

Example with types you have now:

```java
public static void login(String username, String password) {
    System.out.println("Logging in: " + username);
}

public static void login(String username) {
    System.out.println("Logging in: " + username + " with default password policy");
}
```

```java
public static void report(int expected, int actual) {
    System.out.println("Expected: " + expected + " Actual: " + actual);
}

public static void report(String testName, int expected, int actual) {
    System.out.println(testName);
    report(expected, actual);
}
```

The second `report` can call the first. That is a good way to avoid duplication.

Preview of the OOP form, not required to compile today:

```java
login(String username, String password)

login(User user)
```

When `User` exists, `login(user)` might unpack fields and call `login(user.username, user.password)`.

Changing only the return type is **not** overloading.

## 6. Simple Example

```java
public class OverloadDemo {

    public static void main(String[] args) {
        greet();
        greet("Amina");
        greet("Amina", 3);
    }

    public static void greet() {
        System.out.println("Hello!");
    }

    public static void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }

    public static void greet(String name, int times) {
        for (int i = 1; i <= times; i++) {
            System.out.println("Hello, " + name + "! (" + i + ")");
        }
    }
}
```

Sample output:

```text
Hello!
Hello, Amina!
Hello, Amina! (1)
Hello, Amina! (2)
Hello, Amina! (3)
```

Java picked three different methods named `greet`.

## 7. Real-World Example

Payment:

```java
public static void pay(double amount) {
    System.out.println("Paying " + amount + " from default wallet");
}

public static void pay(double amount, String currency) {
    System.out.println("Paying " + amount + " " + currency);
}
```

Search orders:

```java
public static void searchOrders(String customerEmail) { }

public static void searchOrders(String customerEmail, String status) { }
```

Same idea: more filters, same verb.

## 8. SDET Example

Overloaded login:

```java
public class OverloadLogin {

    public static void main(String[] args) {
        login("john", "Test123");
        login("john");
    }

    public static void login(String username, String password) {
        System.out.println("Logging in: " + username);
    }

    public static void login(String username) {
        login(username, "DefaultTest123");
    }
}
```

The one-argument method reuses the two-argument method. Default password belongs in test config later, not in production code.

Overloaded assert:

```java
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
}

public static boolean statusMatches(int expected, int actual, double responseTime, double limit) {
    return expected == actual && responseTime <= limit;
}
```

Overloaded report:

```java
public static void fail(int expected, int actual) {
    System.out.println("TEST FAILED");
    System.out.println("Expected: " + expected);
    System.out.println("Actual: " + actual);
}

public static void fail(String expectedMessage, String actualMessage) {
    System.out.println("TEST FAILED");
    System.out.println("Expected: " + expectedMessage);
    System.out.println("Actual: " + actualMessage);
}
```

Calls:

```java
fail(200, 404);
fail("Login successful", "Invalid password");
```

## 9. Break the Code

```java
public static int combine(int a, int b) {
    return a + b;
}

public static String combine(int a, int b) {
    return a + "," + b;
}
```

This does not compile. The parameter lists are identical.

Ambiguous calls can also fail:

```java
public static void show(int value) { }
public static void show(double value) { }

show(10); // usually picks int, OK
```

If you later add more numeric overloads, some literals can become ambiguous. For this course, keep overload sets obvious: different counts or clearly different types such as `int` vs `String`.

## 10. Debug

If IntelliJ says "method is already defined," you tried to overload by return type only. Change the parameters.

If the wrong overload runs, print a unique first line in each method:

```java
System.out.println("login two-arg");
```

Debugger: Step Into the call. The method you land in is the overload Java chose.

If a one-arg `login` should use a default password, make it call the two-arg version so behavior stays in one place.

## 11. Student Exercise

Create three overloaded methods named `printTest`.

- no parameters: print `TEST`
- one `String`: print that name
- `String` plus `int`: print name and status code

Call all three from `main`.

## 12. Challenge

Write overloaded `assertEquals`:

- two `int` values
- two `String` values using `.equals`
- two `String` values plus a boolean `ignoreCase`

Return `boolean`. Demonstrate all three in `main`. For strings, do **not** use `==`.

## 13. Knowledge Check

1. What is method overloading?
2. What must be different for two methods to overload each other?
3. Is a different return type enough?
4. Why is `println` a friendly example?
5. How does Java decide which overload to call?
6. Why might `login(User user)` exist later in addition to username/password?
7. True or false: overloaded methods can call each other.
8. What is a good SDET use of overloading?
9. Why is `combine(int, int)` twice illegal if only the return type changes?
10. Should overload names describe totally different actions?

## 14. Interview Question

**Question:** What is method overloading in Java?

A strong answer:

> Overloading means methods share a name but have different parameter lists: different types, different count, or different order. The return type alone is not enough. Java chooses the method from the arguments at the call site. In automation I might overload login for username and password now, and later overload login to accept a User object. println is overloaded in the JDK in a similar spirit.

## 15. Homework

Write `HomeworkOverload` with overloaded `openBrowser`:

- `openBrowser()` opens Chrome by default (print only)
- `openBrowser(String name)` opens the given name

And overloaded `evaluate`:

- two ints
- two ints plus a test name `String`

Reuse methods where you can. Print enough to prove which overload ran.

---

## Answer Key

1. Same name, different parameter list.
2. The parameters (number, type, and/or order).
3. No.
4. The same name prints many kinds of values.
5. By matching the arguments to a parameter list.
6. So tests can pass a whole user object instead of two loose strings.
7. True.
8. `login`, `fail`, `assertEquals`, `openBrowser`.
9. Java cannot tell the calls apart.
10. No. If the action is different, use a different name.
