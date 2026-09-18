# Chapter 35 — Parameters

## 1. Today's Goal

By the end of this lesson, you will send values **into** a method using parameters.

You will write:

```java
login("john", "Test123");
```

and see the method use those values.

## 2. Why It Matters

A method with no parameters can only do the exact same thing every time. `login()` that always types `john` cannot test `admin`, `lockedUser`, or an empty username.

Parameters make one recipe work for many ingredients.

This is the difference between a stamp that always prints "Hello" and a mail merge that prints each customer's name.

## 3. Real-Life Analogy

A coffee shop method: `makeDrink`.

Parameters:

- drink name
- size
- milk type

```text
makeDrink("latte", "large", "oat")
makeDrink("espresso", "small", "none")
```

Same steps. Different inputs.

A package delivery method needs an address. Without an address parameter, every package goes to the same house.

## 4. Illustrated Explanation

```text
CALL
login("john", "Test123")
        │         │
        │         └── second argument
        └── first argument
                │
                ▼
METHOD
login(String username, String password)
             │                │
             "john"           "Test123"
```

Vocabulary:

```text
Parameter  → the variable in the method definition
Argument   → the actual value you pass at the call site
```

People mix these words in conversation. In class, try to keep them straight.

```text
Definition:  holes in the recipe card
Call:        the ingredients you pour into those holes
```

Order matters:

```text
login("john", "Test123")

username = "john"
password = "Test123"
```

If you swap the arguments, the password might be stored as the username.

## 5. Syntax / Concept

Definition:

```java
public static void login(String username, String password) {
    System.out.println("Logging in: " + username);
}
```

Call:

```java
login("john", "Test123");
```

Multiple parameters are separated by commas. Each needs a type and a name.

```java
public static void printStatus(int expected, int actual) {
    System.out.println("Expected: " + expected);
    System.out.println("Actual: " + actual);
}
```

Call:

```java
printStatus(200, 404);
```

The argument types must match. You cannot pass `"200"` (a `String`) where an `int` is required without extra conversion.

Parameters are local to the method. `username` inside `login` is not automatically visible in `main`. `main` has its own variables unless you pass them in.

```java
String user = "john";
login(user, "Test123");
```

Here `user` is an argument. Inside `login`, that value is received as `username`.

## 6. Simple Example

```java
public class ParameterDemo {

    public static void main(String[] args) {
        login("john", "Test123");
        login("admin", "AdminPass!");
    }

    public static void login(String username, String password) {
        System.out.println("Logging in: " + username);
        System.out.println("Password received (not printing the real secret in real reports).");
    }
}
```

Expected output:

```text
Logging in: john
Password received (not printing the real secret in real reports).
Logging in: admin
Password received (not printing the real secret in real reports).
```

## 7. Real-World Example

Transfer money:

```java
public class TransferMethod {

    public static void main(String[] args) {
        transfer("CHK-1001", "SAV-2002", 50.00);
    }

    public static void transfer(String fromAccount, String toAccount, double amount) {
        System.out.println("From: " + fromAccount);
        System.out.println("To: " + toAccount);
        System.out.println("Amount: " + amount);
    }
}
```

Print an order line:

```java
public static void printItem(String name, int quantity, double price) {
    System.out.println(quantity + " x " + name + " @ " + price);
}
```

## 8. SDET Example

The login example from the curriculum:

```java
public class LoginParameters {

    public static void main(String[] args) {
        login("john", "Test123");
        login("lockedUser", "Test123");
    }

    public static void login(String username, String password) {
        System.out.println("Logging in: " + username);
    }
}
```

Status reporter:

```java
public static void reportStatus(String testName, int expected, int actual) {
    System.out.println("Test: " + testName);
    if (actual == expected) {
        System.out.println("TEST PASSED");
    } else {
        System.out.println("TEST FAILED");
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);
    }
}
```

Call:

```java
reportStatus("GET user", 200, 404);
```

Browser launcher (print only):

```java
public static void openBrowser(String browserName) {
    System.out.println("Opening " + browserName);
}
```

Use `equalsIgnoreCase` inside if you branch on the name. Do not use `==` for the text.

## 9. Break the Code

```java
public class BrokenParameters {

    public static void main(String[] args) {
        login("john");
    }

    public static void login(String username, String password) {
        System.out.println("Logging in: " + username);
    }
}
```

The call has one argument. The method requires two.

Swapped arguments:

```java
login("Test123", "john");
```

This may compile because both are `String`. The bug is logical: username is now `Test123`.

Type mismatch:

```java
printStatus("200", 404);
```

if `printStatus` expects two `int`s.

## 10. Debug

Read the compiler message: "expected 2 arguments but got 1." Match the call to the definition.

If types are wrong, IntelliJ underlines the argument.

If the program runs but prints the password in the username slot, you swapped order. Print both labels:

```java
System.out.println("username=" + username);
```

Debugger: Step Into `login("john", "Test123")` and inspect `username` and `password` in the Variables view. Confirm they received the values you intended.

Never log real production passwords. In homework, prefer fake passwords and consider printing only `********`.

## 11. Student Exercise

Write `greet(String firstName)` that prints `Hello, ` plus the name.

Call it with three different names from `main`.

Then write `printAttempt(int attemptNumber)` and call it in a `for` loop from 1 to 3.

## 12. Challenge

Write `assertStatus(int expected, int actual)` that prints TEST PASSED or the three-line TEST FAILED report from Project 1.

Call it twice: once with 200/200, once with 200/404.

No duplicated print logic in `main`.

## 13. Knowledge Check

1. What is a parameter?
2. What is an argument?
3. Why does `login()` without parameters struggle in real tests?
4. Write a method header for `login` with username and password.
5. Does parameter order matter?
6. Can you pass a variable as an argument?
7. What happens if you pass one argument to a two-parameter method?
8. How should you compare a `String` parameter to `"chrome"`?
9. True or false: parameter names must match argument variable names in `main`.
10. Why is swapping two `String` arguments hard for the compiler to catch?

## 14. Interview Question

**Question:** What are method parameters in Java, and why are they useful in test automation?

A strong answer:

> Parameters are inputs declared in the method header. When I call the method I pass arguments that fill those inputs. login(String username, String password) can test many users with one method. Testers pass expected and actual values into assertion helpers the same way. The types and the order of arguments must match the parameter list.

## 15. Homework

Create `HomeworkParameters` with:

- `openUrl(String url)`
- `login(String username, String password)`
- `report(String testName, int expected, int actual)`

Call them in a tiny script: open a fake url, login, report 200 vs 404.

---

## Answer Key

1. A named input in the method definition.
2. The actual value passed in the call.
3. Because it cannot change username, environment, or data.
4. `public static void login(String username, String password)`
5. Yes.
6. Yes.
7. It does not compile.
8. `equals` or `equalsIgnoreCase`, not `==`.
9. False. Names can differ. Values are copied in.
10. Because both types still match.
