# Chapter 34 — Why Methods?

## 1. Today's Goal

By the end of this lesson, you will explain why methods exist, write a simple method, and call it from `main`.

You will turn this:

```java
System.out.println("Welcome!");
```

into a named action:

```java
printWelcome();
```

## 2. Why It Matters

Duplication is a silent factory of bugs.

If login is copied 20 times, and the password field id changes, you must find all 20 copies. You will miss one. That missed copy becomes a flaky or failing test that "only happens sometimes."

A method gives you:

- a name for a task
- one place to fix
- a shorter `main` method
- a way to test a piece of work by itself later

## 3. Real-Life Analogy

A cooking recipe named "Boil Pasta" is a method.

You do not rewrite the boiling steps every time a cookbook mentions pasta. You point to the recipe.

A fire drill is a method named `evacuate()`. Everyone knows what that name means.

```text
Recipe name     → method name
Ingredients     → parameters (next chapter)
Finished dish   → return value (chapter after that)
```

Today the recipe has no ingredients and produces no dish. It only performs steps, like printing.

## 4. Illustrated Explanation

Without methods:

```text
main
 ├─ login steps
 ├─ login steps   (copy)
 ├─ login steps   (copy)
 └─ login steps   (copy)
```

With methods:

```text
main
 ├─ login()
 ├─ login()
 ├─ login()
 └─ login()

login() {
    steps live here once
}
```

Call flow:

```text
main starts
   │
   │  printWelcome();
   ▼
printWelcome runs
   prints Welcome!
   │
   returns to main
   │
   main continues
```

A method that returns nothing uses `void`.

```text
public static void printWelcome()
 │       │     │        │
 │       │     │        └── name
 │       │     └── returns nothing
 │       └── can be called without an object (for now)
 └── other classes could call it if they can see it
```

Do not memorize every keyword today. Remember:

```text
void   → this method does not give a value back
name() → run this recipe
```

## 5. Syntax / Concept

Define:

```java
public static void printWelcome() {
    System.out.println("Welcome!");
}
```

Call:

```java
printWelcome();
```

The parentheses are required even when empty. `printWelcome;` is not a call.

Place the method **inside the class**, usually below `main` or above it. Do not put a method inside another method.

```java
public class WhyMethods {
    public static void main(String[] args) {
        printWelcome();
    }

    public static void printWelcome() {
        System.out.println("Welcome!");
    }
}
```

You can call the same method more than once.

```java
printWelcome();
printWelcome();
```

That prints `Welcome!` twice.

## 6. Simple Example

```java
public class WhyMethods {

    public static void main(String[] args) {
        printWelcome();
        printWelcome();
    }

    public static void printWelcome() {
        System.out.println("Welcome!");
    }
}
```

Expected output:

```text
Welcome!
Welcome!
```

## 7. Real-World Example

An online store might have a method that prints a header on every receipt.

```java
public class ReceiptHeader {

    public static void main(String[] args) {
        printStoreHeader();
        System.out.println("Item: Keyboard");
        printStoreHeader();
        System.out.println("Item: Mouse");
    }

    public static void printStoreHeader() {
        System.out.println("==== Neighborhood Shop ====");
    }
}
```

A bank kiosk welcome:

```java
public static void printAtmWelcome() {
    System.out.println("Welcome to Training Bank");
    System.out.println("Insert card to begin");
}
```

If the legal text changes, you change one method.

## 8. SDET Example

Test logs should not copy the same banner 20 times.

```java
public class TestBanner {

    public static void main(String[] args) {
        printTestStart();
        System.out.println("Opening login page...");
        printTestStart();
        System.out.println("Opening dashboard...");
    }

    public static void printTestStart() {
        System.out.println("----- TEST START -----");
    }
}
```

A `login()` with no parameters yet (hard-coded demo):

```java
public class HardCodedLoginMethod {

    public static void main(String[] args) {
        login();
        login();
    }

    public static void login() {
        System.out.println("Typing username john");
        System.out.println("Typing password ********");
        System.out.println("Clicking Sign in");
    }
}
```

Hard-coded usernames are a starting point. Parameters in the next chapter make this useful.

BAD vs GOOD:

```text
BAD
Same login logic
repeated
20 times

GOOD
login();
```

## 9. Break the Code

```java
public class BrokenMethodPlacement {

    public static void main(String[] args) {
        public static void printWelcome() {
            System.out.println("Welcome!");
        }
        printWelcome();
    }
}
```

You cannot declare a method inside `main`.

Another bug: defining the method but never calling it, then wondering why nothing prints.

```java
public static void main(String[] args) {
    System.out.println("I forgot to call printWelcome");
}

public static void printWelcome() {
    System.out.println("Welcome!");
}
```

## 10. Debug

IntelliJ will highlight a method declared inside another method. Move `printWelcome` so it is a sibling of `main`, both inside the class.

If the method exists but does not run, search for the call. A definition is not a call.

```text
Definition  →  the recipe book entry
Call        →  actually cooking
```

Debugger: Step Into on `printWelcome();` to jump into the method. Step Out to return to `main`. This is excellent practice for later Selenium stacks, where one click() call goes through several methods.

## 11. Student Exercise

Create `MethodExercise` with:

- `printWelcome()`
- `printGoodbye()`

Call welcome, print `Running tests...` in `main`, then call goodbye.

## 12. Challenge

Write `printPass()` and `printFail()` that print `TEST PASSED` and `TEST FAILED`.

In `main`, use an `if / else` on `int actual = 404` vs `int expected = 200` to call the correct method. On failure, still print expected and actual in `main` or in `printFail`. Choose one place so you do not duplicate wildly.

## 13. Knowledge Check

1. What is a method in plain English?
2. Why is copying login steps 20 times dangerous?
3. What does `void` mean here?
4. How do you call `printWelcome`?
5. Can you put a method inside `main`?
6. Does defining a method run it automatically?
7. True or false: you can call the same method twice.
8. Where should methods sit: inside the class, or inside `main`?
9. What happens if you write `printWelcome` without `()`?
10. Why do SDET frameworks use so many methods?

## 14. Interview Question

**Question:** Why do we use methods in Java?

A strong answer:

> A method is a named block of code we can reuse. It reduces duplication, so we fix a change in one place. In testing, login steps should live in a login method, not in every test. Today I can write public static void printWelcome() and call printWelcome() from main. Later, methods will take parameters and return values, and then they will belong to objects such as LoginPage.

## 15. Homework

Find any program you already wrote (Test Result Evaluator is perfect). Move the printing of `TEST PASSED` into a method `printPassed()` and `TEST FAILED` into `printFailed()`. `main` should still decide which to call.

If you do not want to touch the project yet, write a new class that only demonstrates three methods: start, step, end.

---

## Answer Key

1. A named recipe / named block of steps.
2. Fixes must be repeated; some copies will be missed.
3. The method does not return a value.
4. `printWelcome();`
5. No.
6. No. You must call it.
7. True.
8. Inside the class, beside `main`.
9. It is not a call. You may get a compile error or a confusing unused-value situation.
10. So tests stay short and shared actions live in one place.
