# Chapter 71 — Lambda Expressions

## 1. Today's Goal

By the end of this lesson, you will read and write a lambda:

```java
item -> System.out.println(item)
```

You will use `forEach` on a list, and you will implement a `@FunctionalInterface Validator` with a lambda instead of a whole anonymous class.

You will remember: **learn methods and interfaces first; lambdas are compact implementations, not a new religion.**

## 2. Why It Matters

Without lambdas, passing behavior means extra classes:

```java
public class PrintItem implements Consumer<String> {
    public void accept(String item) {
        System.out.println(item);
    }
}
```

That is honest but noisy. A lambda keeps the *behavior* next to the *list*.

SDET code filters status codes, maps usernames, validates fields. Streams (next part) are unreadable if lambdas are unreadable.

If you skip interfaces, `item ->` looks like punctuation salad. If you know "this interface has one method `isValid(String)`," the lambda is just that method's body.

## 3. Real-Life Analogy

A sticky note you hand to a coworker:

```text
For each package:
    stamp the label
```

You do not hire a new employee class `StampLabelPerson` for a one-line job. You hand the instruction.

A vending machine button: the machine (list/`forEach`) already knows *when* to call you. You provide *what* to do with each item.

A restaurant ticket: "table 4 → extra sauce." The arrow is "given this, do that."

## 4. Illustrated Explanation

```text
for each item in the list
        │
        ▼
   run this tiny function
   item -> System.out.println(item)

Chrome  →  print Chrome
Firefox →  print Firefox
Edge    →  print Edge
```

From anonymous class to lambda:

```text
new Validator() {
    public boolean isValid(String value) {
        return value != null && !value.isBlank();
    }
}

becomes

value -> value != null && !value.isBlank()
```

```text
parameters  ->  body

(item)      ->  System.out.println(item)
item        ->  System.out.println(item)    // one param: parentheses optional
(a, b)      ->  a + b
()          ->  System.out.println("hi")    // no params
```

If the body is more than one statement, use `{ }` and `return` when needed.

Functional interface:

```text
@FunctionalInterface
interface Validator {
    boolean isValid(String value);   // exactly one abstract method
}
```

The annotation is optional but documents intent. If you add a second abstract method, the compiler complains.

## 5. Syntax / Concept

`forEach` on a `List`:

```java
List<String> browsers = List.of("Chrome", "Firefox", "Edge");
browsers.forEach(item -> System.out.println(item));
```

Method reference (cousin, optional):

```java
browsers.forEach(System.out::println);
```

Same idea: "use this existing method as the lambda." Learn `->` first.

Your functional interface:

```java
@FunctionalInterface
public interface Validator {
    boolean isValid(String value);
}
```

Use:

```java
Validator notBlank = value -> value != null && !value.isBlank();
boolean ok = notBlank.isValid("john");
```

A lambda must **match** the single abstract method: same number of parameters, compatible types, compatible return.

This chapter assumes Java 8+ lambdas (you are on JDK 25). You cannot put a lambda where a two-method interface is required.

Do not replace every method in your project with lambdas. Named methods still win for multi-step login. Lambdas shine for short, local behavior.

## 6. Simple Example

```java
import java.util.List;

public class LambdaForEachDemo {

    public static void main(String[] args) {
        List<String> browsers = List.of("Chrome", "Firefox", "Edge");
        browsers.forEach(item -> System.out.println(item));
    }
}
```

Expected:

```text
Chrome
Firefox
Edge
```

## 7. Real-World Example

Shop discounts: for each cart item name, print a packing line.

Bank: for each last-four transaction description, print it on a receipt.

```java
List<String> items = List.of("Keyboard", "Mouse");
items.forEach(item -> System.out.println("Packed: " + item));
```

A checkout validator:

```java
Validator quantityOk = text -> {
    try {
        return Integer.parseInt(text) > 0;
    } catch (NumberFormatException e) {
        return false;
    }
};
```

Here catching `NumberFormatException` is **specific** and returns false for "not a valid quantity." That is not an empty catch. Do not catch `Exception`.

## 8. SDET Example

```java
import java.util.List;

@FunctionalInterface
interface Validator {
    boolean isValid(String value);
}

public class LambdaSdetDemo {

    public static void main(String[] args) {
        List<String> users = List.of("john", "admin", " ");
        Validator notBlank = value -> value != null && !value.isBlank();

        users.forEach(user -> {
            if (notBlank.isValid(user)) {
                System.out.println("Would login as: " + user);
            } else {
                System.out.println("TEST FAILED — blank user in data");
            }
        });
    }
}
```

Status printer:

```java
List<Integer> codes = List.of(200, 404, 500);
codes.forEach(code -> {
    if (code != 200) {
        System.out.println("Need investigation: " + code);
    }
});
```

Frameworks later: `button -> button.click()` style waits. Same arrow.

## 9. Break the Code

```java
browsers.forEach(item, System.out.println(item)); // not a lambda
```

Missing `->`.

```java
Validator v = (value) -> System.out.println(value);
```

`isValid` must return `boolean`. `println` returns `void`. Does not compile.

```java
interface Validator {
    boolean isValid(String value);
    boolean isStrong(String value); // two abstract methods
}
Validator v = value -> true; // does not compile — not functional
```

Using lambdas without understanding `forEach` still calls the method on each item. If you put `login()` inside `forEach` and catch `Exception` empty per item, you hide per-user failures. Still forbidden.

## 10. Debug

"Does not compile: target type":

1. Is the target a functional interface?
2. Do parameter count and return type match?
3. Did you use `{ }` without `return` on a non-void method?

Lambda breakpoints: IntelliJ can breakpoint inside `item -> { ... }`. For a one-liner, expand to a block temporarily.

If `forEach` prints nothing, the list is empty. Print `list.size()`.

```text
Does the interface have one abstract method?
Does the lambda match that method?
```

## 11. Student Exercise

Create a `List<String>` of four test names. `forEach` print `Running: ` plus the name.

Create `Validator` with `isValid(String)`. Implement `startsWithTest` as `name -> name.startsWith("test")` (choose your prefix). Check each name.

## 12. Challenge

`List<String> statuses = List.of("200", "404", "hello", "201");`

Validator `numeric` using parseInt in try/catch `NumberFormatException` → false.

`forEach`: if valid, parse and print PASS or FAIL against expected 200; if not numeric, TEST FAILED — not a number.

No `catch (Exception e)`.

## 13. Knowledge Check

1. What does `item ->` mean in plain English?
2. What is a functional interface?
3. What does `@FunctionalInterface` do?
4. Write `forEach` that prints each browser.
5. When do you need `{ }` in a lambda?
6. Why learn interfaces before lambdas?
7. True or false: every interface can be written as a lambda.
8. What is `System.out::println` relative to a lambda?
9. Can a lambda return `boolean` for `isValid`?
10. Empty catch inside a lambda: allowed for hiding test failures?

## 14. Interview Question

**Question:** What is a lambda expression in Java?

A strong answer:

> A lambda is a compact way to implement a functional interface — an interface with one abstract method. item -> System.out.println(item) means given item, print it. I use it with forEach and later with streams. I only teach it after methods and interfaces, because the lambda must match that one method. I still use named methods for bigger workflows like login. I do not empty-catch exceptions inside lambdas to hide failures.

## 15. Homework

Write `Validator` and three lambdas: not blank, length at least 8, does not equal `"password"`.

Check `"secret12"`, `""`, `"password"`. Print pass/fail per rule.

Draw: `interface method` on the left, `lambda` on the right, arrow matching them.

---

## Answer Key

1. Given this item, then do the body.
2. An interface with exactly one abstract method.
3. Asks the compiler to enforce that it stays functional.
4. `browsers.forEach(item -> System.out.println(item));`
5. When the body has multiple statements (and `return` if a value is required).
6. So you know what method the lambda is implementing.
7. False. Only functional interfaces.
8. A method reference; shorthand for a lambda that only calls that method.
9. Yes, if that is what `isValid` returns.
10. No.
