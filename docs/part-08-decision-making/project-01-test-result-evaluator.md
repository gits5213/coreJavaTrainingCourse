# Project 1 — Test Result Evaluator

## 1. Today's Goal

You will build a small complete program that compares an expected HTTP status code with an actual HTTP status code and prints a clear test result.

When expected is `200` and actual is `404`, the program must print:

```text
TEST FAILED
Expected: 200
Actual: 404
```

When they match, it must print `TEST PASSED`.

This is your first SDET-flavored project. It is not a framework. It is a solid beginner program you can explain line by line.

## 2. Why It Matters

Every automated assertion is this idea:

```text
expected
actual
compare
report
```

If you can do this with two integers, you can later do it with messages, lists, JSON fields, and UI text. The habit starts here:

- store expected
- store actual
- decide
- print evidence on failure

A failure that only says `FAIL` is weak. A failure that shows expected and actual is professional.

## 3. Real-Life Analogy

A teacher grades a quiz with an answer key.

```text
Answer key says: 200
Student wrote:   404

Grade: incorrect
Show the key and what was written
```

You would not mark the quiz wrong without circling the two numbers. Your program should not either.

A warehouse scanner is similar: expected barcode versus scanned barcode. Mismatch? Show both.

## 4. Illustrated Explanation

```text
Input
┌────────────────┐
│ expected = 200 │
│ actual   = 404 │
└────────────────┘
        │
        ▼
   actual == expected ?
        /        \
      Yes         No
       ↓           ↓
 TEST PASSED   TEST FAILED
               Expected: 200
               Actual: 404
```

Happy path:

```text
expected = 200
actual   = 200
        ↓
TEST PASSED
```

Failure path (the required demo):

```text
expected = 200
actual   = 404
        ↓
TEST FAILED
Expected: 200
Actual: 404
```

Optional extra quality, if you finish early:

```text
Also print a human reason:
404 means Not Found
```

That extra line is nice. It is not required for the minimum project.

## 5. Syntax / Concept

You will combine skills from Parts 5 through 8:

```java
int expected = 200;
int actual = 404;

if (actual == expected) {
    System.out.println("TEST PASSED");
} else {
    System.out.println("TEST FAILED");
    System.out.println("Expected: " + expected);
    System.out.println("Actual: " + actual);
}
```

Numbers use `==`. If you later add a message check, use `equals`:

```java
actualMessage.equals(expectedMessage)
```

Do not compare messages with `==`.

Suggested class name:

```text
TestResultEvaluator
```

Keep everything in `main` for this project. Methods come in Part 10. You may still extract a method if you have already peeked ahead, but it is not required.

## 6. Simple Example

Minimum working program:

```java
public class TestResultEvaluator {

    public static void main(String[] args) {
        int expected = 200;
        int actual = 404;

        if (actual == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + actual);
        }
    }
}
```

Required output for these values:

```text
TEST FAILED
Expected: 200
Actual: 404
```

Then change `actual` to `200` and confirm:

```text
TEST PASSED
```

Both paths are part of the project. A program that only fails is incomplete. A program that only passes is incomplete.

## 7. Real-World Example

This evaluator is the same shape as checking an order API.

Imagine creating an order:

```text
Expected status: 201 Created
Actual status:   400 Bad Request
```

You would still print expected and actual. You might also print the order id if you had one.

A banking transfer API:

```text
Expected: 200
Actual:   403
Meaning:  forbidden, maybe the account cannot transfer
```

Your Java structure does not change. Only the numbers and the business story change.

You may add labeled context:

```java
String testName = "GET /users/1";
System.out.println("Test: " + testName);
```

Context helps, but the three required lines on failure stay required.

## 8. SDET Example

A slightly richer evaluator that still stays beginner-friendly:

```java
public class TestResultEvaluator {

    public static void main(String[] args) {
        String testName = "GET user by id";
        int expected = 200;
        int actual = 404;

        System.out.println("Running: " + testName);

        if (actual == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + actual);

            if (actual == 404) {
                System.out.println("Reason: resource was not found");
            } else if (actual == 401) {
                System.out.println("Reason: unauthorized");
            } else if (actual == 500) {
                System.out.println("Reason: server error");
            } else {
                System.out.println("Reason: unexpected status");
            }
        }
    }
}
```

Possible output:

```text
Running: GET user by id
TEST FAILED
Expected: 200
Actual: 404
Reason: resource was not found
```

The inner `else if` is extra credit. The outer `if / else` is the heart of the project.

Login variant you can try after the status evaluator works:

```java
String expectedMessage = "Login successful";
String actualMessage = "Invalid password";

if (actualMessage.equals(expectedMessage)) {
    System.out.println("TEST PASSED");
} else {
    System.out.println("TEST FAILED");
    System.out.println("Expected: " + expectedMessage);
    System.out.println("Actual: " + actualMessage);
}
```

## 9. Break the Code

Here is a version that looks similar but fails the assignment.

```java
public class BrokenEvaluator {

    public static void main(String[] args) {
        int expected = 200;
        int actual = 404;

        if (actual == expected) {
            System.out.println("TEST FAILED");
        } else {
            System.out.println("TEST PASSED");
        }
    }
}
```

Problems:

- PASS and FAIL are reversed
- expected and actual are never printed

Another broken version:

```java
if (actual = expected) {
    System.out.println("TEST PASSED");
}
```

That assigns instead of comparing.

A third:

```java
System.out.println("Expected 200 Actual 404");
```

Hard-coding the sentence without using variables means changing `actual` will not update the report.

## 10. Debug

Checklist:

1. Are `expected` and `actual` stored in variables?
2. Is the comparison `==` for integers?
3. Is the success branch the one where they match?
4. Does the failure branch print `TEST FAILED` on its own line?
5. Does the failure branch print `Expected: ` plus the variable, not a frozen number in the words only?
6. Does the failure branch print `Actual: ` plus the variable?

Use the debugger:

1. Set a breakpoint on the `if`.
2. Confirm `expected` is 200 and `actual` is 404.
3. Step Into the `else` block.
4. Watch the concatenated strings in the Variables view if you like.

If output is on one line, you used one `println` too few, or you used `print` instead of `println`.

If output shows the wrong numbers, you concatenated literals instead of variables:

```java
System.out.println("Expected: 200"); // weak if expected might change
System.out.println("Expected: " + expected); // correct
```

## 11. Student Exercise

Implement `TestResultEvaluator` with the required 200 vs 404 failure output.

Then create a second pair of variables in comments or a second run:

- expected `201`, actual `201` → `TEST PASSED`

Do not delete the failure demo. The instructor should be able to set `actual` to `404` and see the exact three-line failure report.

## 12. Challenge

Extend the evaluator without breaking the original output format.

Add:

- `double maxResponseTime = 2.0;`
- `double actualResponseTime = 2.7;`

Rules:

- If status mismatches, print the required TEST FAILED status report. Do not hide it.
- If status matches but time is too slow, print `TEST FAILED` and show both times.
- If status matches and time is within limit, print `TEST PASSED`.

Think carefully about `else if`. Status failure and slowness are different stories.

## 13. Knowledge Check

1. What are the two main inputs of this project?
2. Which operator compares two `int` status codes?
3. What must print when 200 is expected and 404 is actual?
4. Why is printing expected and actual better than printing only `FAIL`?
5. Why should the printed numbers come from variables?
6. What should print when expected and actual are both 200?
7. If you also compare messages, which method should you use?
8. Why is `if (actual = expected)` wrong?
9. True or false: Project 1 requires Selenium.
10. Where does this project sit in the bigger SDET journey?

## 14. Interview Question

**Question:** How would you write a simple Java program to compare expected and actual HTTP status codes?

Practice answering and then writing it on a whiteboard.

A strong answer:

> I would store expected and actual as int variables. If they are equal with ==, I print TEST PASSED. Otherwise I print TEST FAILED and I print both values so the failure is obvious. For the example expected 200 and actual 404, the program reports a failure with those two numbers. Later I would move this into a method and a real test framework, but the comparison idea stays the same.

## 15. Homework

1. Finish `TestResultEvaluator` so both PASS and FAIL paths work.
2. Run it with `200/404` and paste the output into a comment at the bottom of the file.
3. Run it with `200/200` and add that output in another comment.
4. Optional: add a `String testName` printed first.
5. Bring one question to class about something you still find unclear: `if`, `==`, or output formatting.

You have completed the first project when another student can change only `actual` and the printed report stays truthful.

---

## Answer Key

1. Expected status code and actual status code.
2. `==`
3. `TEST FAILED` then `Expected: 200` then `Actual: 404`
4. It shows the mismatch evidence.
5. So the report stays correct when values change.
6. `TEST PASSED`
7. `equals` or `equalsIgnoreCase`, never `==` for content.
8. It assigns, and it is not a proper boolean comparison.
9. False. It is plain Java.
10. It is the first automated-style assertion, before JUnit, TestNG, and REST Assured.

---

## Sample Solution

You should try first. This is a complete solution for the required behavior.

```java
public class TestResultEvaluator {

    public static void main(String[] args) {
        int expected = 200;
        int actual = 404;

        if (actual == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + actual);
        }
    }
}
```
