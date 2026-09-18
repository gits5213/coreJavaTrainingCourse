# Chapter 28 — `else if`

## 1. Today's Goal

By the end of this lesson, you will chain several conditions with `else if` so the program can choose among more than two outcomes.

You will classify HTTP status codes:

- 200 → Success
- 404 → Not Found
- 500 → Server Error
- anything else → Unknown

## 2. Why It Matters

The world is not only PASS or FAIL. Status codes have families. Browsers have names. Users have roles. Orders have several states.

If you only have `if / else`, you start nesting or writing many separate `if` statements that can all run. `else if` gives an ordered list of choices: check this, else check that, else check the next thing, else do the fallback.

## 3. Real-Life Analogy

A package tracking board:

```text
if delivered
    show delivered
else if out for delivery
    show truck icon
else if shipped
    show in transit
else
    show label created
```

A school grading scale is the same pattern:

```text
if score >= 90 → A
else if score >= 80 → B
else if score >= 70 → C
else → Needs improvement
```

Order matters. If you check `>= 70` first, a score of 95 would be called C.

## 4. Illustrated Explanation

```text
statusCode
    │
    ▼
== 200? --Yes--> Success
    │
   No
    ▼
== 404? --Yes--> Not Found
    │
   No
    ▼
== 500? --Yes--> Server Error
    │
   No
    ▼
Unknown
```

Only **one** of those four messages should print.

```text
if
else if
else if
else
```

Java checks from the top. The first true condition wins. The rest are skipped.

## 5. Syntax / Concept

```java
if (statusCode == 200) {
    System.out.println("Success");
} else if (statusCode == 404) {
    System.out.println("Not Found");
} else if (statusCode == 500) {
    System.out.println("Server Error");
} else {
    System.out.println("Unknown");
}
```

You can have as many `else if` pieces as you need. The final `else` is optional, but it is wise. It catches surprises.

Compare with separate `if` statements:

```java
if (statusCode == 200) {
    System.out.println("Success");
}
if (statusCode == 404) {
    System.out.println("Not Found");
}
```

Separate `if` statements are independent. Each one can run. That is correct when several facts can all be true at once. It is wrong when you want exactly one category.

Use `equals` for text:

```java
if (role.equals("admin")) {
    System.out.println("Full access");
} else if (role.equals("tester")) {
    System.out.println("QA access");
} else {
    System.out.println("Limited access");
}
```

## 6. Simple Example

```java
public class ElseIfDemo {

    public static void main(String[] args) {
        int statusCode = 404;

        if (statusCode == 200) {
            System.out.println("Success");
        } else if (statusCode == 404) {
            System.out.println("Not Found");
        } else if (statusCode == 500) {
            System.out.println("Server Error");
        } else {
            System.out.println("Unknown");
        }
    }
}
```

Expected output:

```text
Not Found
```

Try `200`, `500`, and `418`. You should see Success, Server Error, and Unknown.

## 7. Real-World Example

Order status:

```java
public class OrderStatusChain {

    public static void main(String[] args) {
        String orderStatus = "shipped";

        if (orderStatus.equals("placed")) {
            System.out.println("We received your order");
        } else if (orderStatus.equals("shipped")) {
            System.out.println("Your order is on the way");
        } else if (orderStatus.equals("delivered")) {
            System.out.println("Your order has arrived");
        } else if (orderStatus.equals("cancelled")) {
            System.out.println("This order was cancelled");
        } else {
            System.out.println("Unknown order status");
        }
    }
}
```

Banking account type:

```java
char accountType = 'C';

if (accountType == 'C') {
    System.out.println("Checking");
} else if (accountType == 'S') {
    System.out.println("Savings");
} else if (accountType == 'M') {
    System.out.println("Money market");
} else {
    System.out.println("Unknown account type");
}
```

`char` uses `==` because it is a primitive. `String` uses `equals`.

## 8. SDET Example

HTTP families are a daily SDET skill.

```java
public class StatusClassifier {

    public static void main(String[] args) {
        int statusCode = 401;

        if (statusCode == 200) {
            System.out.println("Success");
        } else if (statusCode == 201) {
            System.out.println("Created");
        } else if (statusCode == 400) {
            System.out.println("Bad Request");
        } else if (statusCode == 401) {
            System.out.println("Unauthorized");
        } else if (statusCode == 403) {
            System.out.println("Forbidden");
        } else if (statusCode == 404) {
            System.out.println("Not Found");
        } else if (statusCode == 500) {
            System.out.println("Server Error");
        } else {
            System.out.println("Unknown");
        }
    }
}
```

Expected output:

```text
Unauthorized
```

Ranges can also use `else if`:

```java
if (statusCode >= 200 && statusCode <= 299) {
    System.out.println("2xx success family");
} else if (statusCode >= 400 && statusCode <= 499) {
    System.out.println("4xx client error family");
} else if (statusCode >= 500 && statusCode <= 599) {
    System.out.println("5xx server error family");
} else {
    System.out.println("Other family");
}
```

Order is still important. Put the most specific checks first if a value could match more than one condition.

## 9. Break the Code

This grading program gives everyone an A if they scored 70 or higher.

```java
public class BrokenGrade {

    public static void main(String[] args) {
        int score = 73;

        if (score >= 70) {
            System.out.println("C");
        } else if (score >= 80) {
            System.out.println("B");
        } else if (score >= 90) {
            System.out.println("A");
        } else {
            System.out.println("Needs improvement");
        }
    }
}
```

73 prints `C`, which might look right, but 95 would also print `C` because `>= 70` is checked first and wins.

## 10. Debug

When using ranges, check the highest (or most specific) bucket first.

```java
public class FixedGrade {

    public static void main(String[] args) {
        int score = 95;

        if (score >= 90) {
            System.out.println("A");
        } else if (score >= 80) {
            System.out.println("B");
        } else if (score >= 70) {
            System.out.println("C");
        } else {
            System.out.println("Needs improvement");
        }
    }
}
```

Debug questions:

1. Which condition is first?
2. Could a later condition also be true for this value?
3. If yes, the first true one still wins — so the order may be wrong.

Print the value at the top of `main` while you test each branch. Change only the input, not the structure, until every branch has been seen once.

## 11. Student Exercise

Write `ElseIfExercise` that classifies a `String browser`.

- `chrome` → print `Launch Chrome`
- `firefox` → print `Launch Firefox`
- `edge` → print `Launch Edge`
- anything else → print `Unsupported browser`

Use `equalsIgnoreCase` so `Chrome` still works.

Test all four paths by changing the variable.

## 12. Challenge

Classify a login result with more than HTTP codes.

Variables: `int statusCode`, `boolean accountLocked`.

Rules, in this order:

1. If account is locked, print `Blocked: account locked` (ignore status).
2. Else if status is 200, print `Login OK`.
3. Else if status is 401, print `Bad credentials`.
4. Else print `Unexpected login result` and the status.

Prove with comments that a locked account with status 200 still prints the blocked message. That is why locked is first.

## 13. Knowledge Check

1. What does `else if` add that a plain `if / else` does not?
2. How many branches run in one `if / else if / else` chain?
3. Why does condition order matter?
4. When should you prefer separate `if` statements instead of `else if`?
5. What is the final `else` for?
6. How do you compare `String` roles in an `else if` chain?
7. Why would `score >= 70` before `score >= 90` be a bug?
8. Write the three status checks 200, 404, 500 with a fallback.
9. True or false: all `else if` conditions are evaluated even after one is true.
10. Is `char` compared with `equals` or `==`?

## 14. Interview Question

**Question:** When do you use `else if` instead of multiple independent `if` statements?

A strong answer:

> I use else if when the outcomes are mutually exclusive and I want the first matching condition to win. For example, an HTTP status is 200 or 404 or 500, not all of them at once. Independent if statements can all run, which is useful when several warnings can be true together. Order matters in an else if chain, so I put more specific checks first.

## 15. Homework

Write `HomeworkStatusStory` that prints a complete tester sentence:

- 200 → `TEST PASSED: resource found`
- 201 → `TEST PASSED: resource created`
- 400 → `TEST FAILED: bad request`
- 404 → `TEST FAILED: missing resource`
- 500 → `TEST FAILED: server error`
- else → `TEST FAILED: unexpected status` plus the number

Run it with at least three different codes.

---

## Answer Key

1. More than two exclusive choices, checked in order.
2. One.
3. Because the first true condition wins.
4. When several conditions can all be true and each should run.
5. A fallback when none of the earlier conditions matched.
6. `role.equals("admin")` or `equalsIgnoreCase`.
7. High scores would match the 70 bucket first.
8. See the syntax section in this chapter.
9. False. Later ones are skipped.
10. `==`, because `char` is a primitive.
