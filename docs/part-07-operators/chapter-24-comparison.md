# Chapter 24 — Comparison Operators

## 1. Today's Goal

By the end of this lesson, you will compare numbers with `==`, `!=`, `>`, `<`, `>=`, and `<=`.

You will also learn the **safe way to compare String content**: `equals` and `equalsIgnoreCase`.

You will **not** use `==` to decide whether two names or messages have the same characters.

## 2. Why It Matters

A test is a comparison.

```text
Did we get what we expected?
```

That question is not poetry. It is an operator:

```java
int actual = 200;
int expected = 200;
boolean match = actual == expected;
```

Without comparison, you can print numbers but you cannot say PASS or FAIL.

Text comparison is just as important, and it has a trap. Because `String` is a reference type, `==` may not mean "same words." Until we study references more deeply later, use `equals` for text.

## 3. Real-Life Analogy

A teacher comparing scores:

```text
actual score == passing score     same score?
actual score != passing score     different?
actual score >  90                better than 90?
actual score >= 60                at least 60?
actual score <  50                worse than 50?
```

Comparing names on two badges is different from comparing two numbers. You look at the printed letters. You do not ask whether the plastic badges are the same physical object.

```text
Numbers:  are these quantities the same?
Text:     do these characters match?
```

## 4. Illustrated Explanation

```text
actual          expected
  200     ==      200        → true
  404     ==      200        → false
  404     !=      200        → true
  404      >      200        → true
  200      <      404        → true
  200     >=      200        → true
  199     <=      200        → true
```

Picture a checkpoint:

```text
Is actual equal to expected?
        │
        ├── ==   equal
        └── !=   not equal

Is actual bigger or smaller?
        │
        ├── >    greater than
        ├── <    less than
        ├── >=   greater or equal
        └── <=   less or equal
```

Every comparison produces a `boolean`: `true` or `false`.

### String content

```text
WRONG for text content
name1 == name2

RIGHT for text content
name1.equals(name2)
name1.equalsIgnoreCase(name2)
```

```text
"John".equals("John")           → true
"John".equals("john")           → false
"John".equalsIgnoreCase("john") → true
```

Why not `==` for names? `String` variables hold references. `==` can ask "are these the same object?" when testers meant "do they contain the same letters?" The detailed reference-versus-value discussion will come later. Today's rule is already enough to write correct tests.

## 5. Syntax / Concept

### Number comparison

```java
int actual = 200;
int expected = 200;

boolean equal = actual == expected;
boolean different = actual != expected;
boolean slowerThanLimit = 3.1 > 2.0;
boolean fastEnough = 1.5 <= 2.0;
```

Notice `==` for comparison and `=` for assignment. They are not the same.

```text
=    put this value into the variable
==   ask whether two values are equal
```

### String content comparison

```java
String actualMessage = "Login successful";
String expectedMessage = "Login successful";

boolean sameText = actualMessage.equals(expectedMessage);
boolean sameIgnoringCase = actualMessage.equalsIgnoreCase("LOGIN SUCCESSFUL");
```

Do **not** write this for ordinary value comparison of names or messages:

```java
boolean sameText = name1 == name2; // do not use this for String content
```

If the requirement says case does not matter, use `equalsIgnoreCase`. If the requirement says the text must match exactly, use `equals`.

Numbers still use `==`:

```java
boolean statusOk = actualStatusCode == 200;
```

## 6. Simple Example

```java
public class ComparisonDemo {

    public static void main(String[] args) {
        int actual = 200;
        int expected = 200;

        System.out.println(actual == expected);
        System.out.println(actual != expected);
        System.out.println(actual > 100);
        System.out.println(actual < 100);
        System.out.println(actual >= 200);
        System.out.println(actual <= 199);
    }
}
```

Expected output:

```text
true
false
true
false
true
false
```

String example:

```java
public class StringEqualsDemo {

    public static void main(String[] args) {
        String actualName = "John";
        String expectedName = "John";
        String otherCase = "john";

        System.out.println(actualName.equals(expectedName));
        System.out.println(actualName.equals(otherCase));
        System.out.println(actualName.equalsIgnoreCase(otherCase));
    }
}
```

Expected output:

```text
true
false
true
```

## 7. Real-World Example

Banking: is a withdrawal allowed?

```java
public class WithdrawalCheck {

    public static void main(String[] args) {
        double balance = 100.00;
        double request = 80.00;

        boolean enoughFunds = balance >= request;
        boolean requestIsPositive = request > 0;

        System.out.println("Enough funds: " + enoughFunds);
        System.out.println("Positive request: " + requestIsPositive);
    }
}
```

E-commerce: did the customer order more than the stock?

```java
public class StockCheck {

    public static void main(String[] args) {
        int quantityRequested = 4;
        int quantityInStock = 3;

        boolean outOfStock = quantityRequested > quantityInStock;
        System.out.println("Out of stock: " + outOfStock);
    }
}
```

User names:

```java
public class UsernameCheck {

    public static void main(String[] args) {
        String enteredUsername = "Sam";
        String savedUsername = "sam";

        boolean exactMatch = enteredUsername.equals(savedUsername);
        boolean loginNameMatch = enteredUsername.equalsIgnoreCase(savedUsername);

        System.out.println("Exact match: " + exactMatch);
        System.out.println("Ignore case: " + loginNameMatch);
    }
}
```

## 8. SDET Example

Status codes are numbers, so `==` is correct.

```java
public class StatusComparison {

    public static void main(String[] args) {
        int actual = 200;
        int expected = 200;
        boolean match = actual == expected;
        boolean isNotFound = actual == 404;
        boolean isServerError = actual >= 500;

        System.out.println("Match: " + match);
        System.out.println("Not found: " + isNotFound);
        System.out.println("Server error range: " + isServerError);
    }
}
```

Messages are text, so `equals` is correct.

```java
public class MessageComparison {

    public static void main(String[] args) {
        String actualMessage = "Login successful";
        String expectedMessage = "Login successful";

        boolean messageMatches = actualMessage.equals(expectedMessage);
        boolean messageMatchesAnyCase =
                actualMessage.equalsIgnoreCase("login successful");

        System.out.println("Exact: " + messageMatches);
        System.out.println("Ignore case: " + messageMatchesAnyCase);
    }
}
```

Response time limit:

```java
double responseTime = 1.52;
double maxSeconds = 2.0;
boolean fastEnough = responseTime <= maxSeconds;
```

## 9. Break the Code

This program is supposed to confirm that two usernames match ignoring case. It is written in a dangerous way.

```java
public class BrokenNameComparison {

    public static void main(String[] args) {
        String name1 = "John";
        String name2 = new String("John");

        boolean match = name1 == name2;
        System.out.println("Match: " + match);
    }
}
```

It may print `false` even though both usernames are `John`. That is the trap. Testers who learn `==` for names get flaky, confusing results.

A second bug, with numbers:

```java
int actual = 404;
int expected = 200;
boolean match = actual = expected;
```

That line uses assignment by mistake.

## 10. Debug

For names and messages, replace `==` with `equals` or `equalsIgnoreCase`.

```java
public class FixedNameComparison {

    public static void main(String[] args) {
        String name1 = "John";
        String name2 = new String("John");

        boolean match = name1.equals(name2);
        System.out.println("Match: " + match);
    }
}
```

This prints `true`.

We will explain later why `name1 == name2` can be `false` even when the text looks identical: it can compare references instead of characters. You do not need that full story to write correct code today. You need the habit.

For the number bug, `actual = expected` assigns `200` into `actual`. Comparison must be `==`:

```java
boolean match = actual == expected;
```

IntelliJ often warns when you assign inside a place that expected a boolean. Read that warning. It is protecting you.

## 11. Student Exercise

Given:

```java
int expectedStatus = 201;
int actualStatus = 201;
String expectedMessage = "Created";
String actualMessage = "created";
double responseTime = 2.4;
```

Print booleans for:

- status codes equal
- status codes not equal
- message exact match with `equals`
- message match with `equalsIgnoreCase`
- response time greater than 2.0
- response time less than or equal to 2.0

## 12. Challenge

Write a tiny assertion printer.

If the HTTP status is `200` **and** you are only allowed to use today's operators, print:

```text
statusMatch: true/false
messageMatch: true/false
withinTime: true/false
```

Use:

- expected status `200`, actual `200`
- expected message `OK`, actual `ok`
- max time `2.0`, actual time `1.1`

Message matching should ignore case. Do not use `==` on the messages.

## 13. Knowledge Check

1. What does `==` mean for `int` values?
2. What does `!=` mean?
3. What is the difference between `>` and `>=`?
4. What type do comparison expressions produce?
5. Should you write `name1 == name2` to check username text?
6. Which method compares `String` content exactly?
7. Which method compares `String` content without caring about uppercase vs lowercase?
8. Why can `==` on two `String` values confuse beginners?
9. Write a check that `actualStatus` equals `404`.
10. True or false: `=` and `==` are interchangeable.

## 14. Interview Question

**Question:** How do you compare two values in Java, and how is String comparison different?

A strong answer:

> For numbers I use ==, !=, >, <, >=, and <=. Those expressions give a boolean. For String content I use equals for exact text and equalsIgnoreCase when case should not matter. I do not use == to compare the characters in two names, because String is a reference type and == can compare references. The deeper reference-versus-value explanation comes with object identity, but testers should already use equals for text.

## 15. Homework

Create `HomeworkComparisons` that models a failed login.

- expected status `200`
- actual status `401`
- expected message `Welcome`
- actual message `Invalid credentials`
- response time `0.8` against a limit of `2.0`

Print a report of every comparison from this chapter that is useful here. Include `equals` and `equalsIgnoreCase` on the messages so you can see both are `false`. In a comment, write: Do not use == to compare String content.

---

## Answer Key

1. It asks whether the two numbers are equal.
2. It asks whether they are not equal.
3. `>` requires strictly greater. `>=` also accepts equality.
4. `boolean`
5. No. Use `equals` or `equalsIgnoreCase`.
6. `equals`
7. `equalsIgnoreCase`
8. Because it may compare object references instead of the characters testers care about.
9. `actualStatus == 404`
10. False. `=` assigns. `==` compares.
