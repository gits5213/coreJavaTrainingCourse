# Chapter 25 — Logical Operators

## 1. Today's Goal

By the end of this lesson, you will combine boolean values with `&&` (and), `||` (or), and `!` (not).

You will write conditions such as:

> The status code is 200 **and** the response time is under 2 seconds.

That is how real tests decide PASS.

## 2. Why It Matters

One comparison is rarely enough.

A login test might require:

```text
status is 200
AND
message is Login successful
AND
response time is fast
```

An order might be ready to ship if:

```text
payment succeeded
OR
the order is marked prepaid
```

A retry might happen when the page is **not** loaded.

Logical operators glue small true/false answers into one decision.

## 3. Real-Life Analogy

A nightclub door policy:

```text
You may enter if you have a ticket AND you are on the guest list.
You may enter if you have a VIP pass OR you are the host.
You may not enter if you are NOT on the approved list.
```

```text
AND  &&   both must be true
OR   ||   at least one must be true
NOT  !    reverse the answer
```

A test report is the same kind of policy:

```text
PASS only if expected equals actual AND no error banner is shown.
```

## 4. Illustrated Explanation

Start with two booleans:

```text
statusOk = true
fast     = true
```

AND:

```text
statusOk && fast

 true  &&  true   → true
 true  &&  false  → false
false  &&  true   → false
false  &&  false  → false
```

```text
Both doors must open.

[status 200] ----\
                  AND → PASS
[time < 2s ] ----/
```

OR:

```text
chrome || firefox

 true  ||  false  → true
false  ||  true   → true
 true  ||  true   → true
false  ||  false  → false
```

```text
At least one door must open.

[retry allowed] --\
                   OR → try again
[first attempt ] --/
```

NOT:

```text
!testPassed

!true  → false
!false → true
```

```text
testPassed = false
!testPassed → true   meaning "the test did not pass"
```

## 5. Syntax / Concept

```java
boolean bothOk = statusCode == 200 && responseTime < 2;
boolean eitherOk = browser.equals("chrome") || browser.equals("firefox");
boolean failed = !testPassed;
```

Use `equals` for the browser name, not `==`.

### Combining with comparisons

Comparisons produce booleans. Logical operators combine those booleans.

```java
int statusCode = 200;
double responseTime = 1.2;

if (statusCode == 200 && responseTime < 2) {
    System.out.println("PASS");
}
```

You will study `if` fully in the next part. The condition inside the parentheses is today's topic.

### Read them in English

```text
&&   and
||   or
!    not
```

```java
statusCode == 200 && responseTime < 2
// status is 200 AND time is less than 2

statusCode == 404 || statusCode == 500
// status is 404 OR status is 500

!loginSuccessful
// login is NOT successful
```

### Short-circuit, briefly

Java is efficient:

- with `&&`, if the left side is already `false`, the right side is not necessary
- with `||`, if the left side is already `true`, the right side is not necessary

You do not need to design around this yet. Just know that `&&` and `||` evaluate left to right and may skip the second part.

### Parentheses

When a line has several operators, parentheses keep your meaning obvious:

```java
boolean pass = (statusCode == 200) && (responseTime < 2) && message.equals("OK");
```

## 6. Simple Example

```java
public class LogicalDemo {

    public static void main(String[] args) {
        boolean statusOk = true;
        boolean fast = false;
        boolean testPassed = false;

        System.out.println(statusOk && fast);
        System.out.println(statusOk || fast);
        System.out.println(!testPassed);
    }
}
```

Expected output:

```text
false
true
true
```

With real comparisons:

```java
public class LogicalComparisons {

    public static void main(String[] args) {
        int statusCode = 200;
        double responseTime = 1.1;

        boolean pass = statusCode == 200 && responseTime < 2;
        System.out.println("PASS condition: " + pass);
    }
}
```

## 7. Real-World Example

A bank transfer might be allowed only if the account is active **and** the amount is positive **and** the balance is large enough.

```java
public class TransferRules {

    public static void main(String[] args) {
        boolean accountActive = true;
        double amount = 50.00;
        double balance = 120.00;

        boolean allowed = accountActive && amount > 0 && balance >= amount;
        System.out.println("Transfer allowed: " + allowed);
    }
}
```

E-commerce free shipping:

```java
public class FreeShipping {

    public static void main(String[] args) {
        double orderTotal = 48.00;
        boolean hasCoupon = true;

        boolean freeShipping = orderTotal >= 50 || hasCoupon;
        System.out.println("Free shipping: " + freeShipping);
    }
}
```

The order total is under $50, but a coupon exists, so `freeShipping` is `true`.

## 8. SDET Example

A complete HTTP check:

```java
public class ApiPassRule {

    public static void main(String[] args) {
        int statusCode = 200;
        double responseTime = 1.4;
        String actualMessage = "OK";

        boolean pass = statusCode == 200
                && responseTime < 2
                && actualMessage.equals("OK");

        System.out.println("PASS: " + pass);
    }
}
```

Retry logic in words:

```java
int attempt = 1;
int maxAttempts = 3;
boolean pageLoaded = false;

boolean shouldRetry = !pageLoaded && attempt < maxAttempts;
System.out.println("Retry: " + shouldRetry);
```

Browser gate:

```java
String browser = "firefox";
boolean supported = browser.equalsIgnoreCase("chrome")
        || browser.equalsIgnoreCase("firefox")
        || browser.equalsIgnoreCase("edge");
System.out.println("Supported browser: " + supported);
```

Notice `equalsIgnoreCase`, not `==`.

## 9. Break the Code

This program is supposed to pass only when the status is 200 **and** the message is `OK`. It prints `PASS` too often.

```java
public class BrokenAndOr {

    public static void main(String[] args) {
        int statusCode = 404;
        String message = "OK";

        boolean pass = statusCode == 200 || message.equals("OK");

        if (pass) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
        }
    }
}
```

A 404 with message `OK` should not be a passing API test, if both must be correct.

A second bug:

```java
boolean loginSuccessful = false;
if (!loginSuccessful == true) {
    System.out.println("This line is confusing and easy to get wrong.");
}
```

## 10. Debug

The first program used `||` (or) when the requirement was **and**. Replace it:

```java
public class FixedAndOr {

    public static void main(String[] args) {
        int statusCode = 404;
        String message = "OK";

        boolean pass = statusCode == 200 && message.equals("OK");

        if (pass) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
        }
    }
}
```

Now it prints `FAIL`.

English check:

```text
If the sentence uses AND, you probably want &&
If the sentence uses OR,  you probably want ||
If the sentence uses NOT, you probably want !
```

For the second bug, `!loginSuccessful` is already a boolean. You rarely need `== true`. Write:

```java
if (!loginSuccessful) {
    System.out.println("Login failed");
}
```

## 11. Student Exercise

Variables:

```java
int statusCode = 200;
double responseTime = 2.5;
boolean loginSuccessful = true;
```

Print:

- status is 200 AND login succeeded
- status is 200 AND response time is under 2
- status is 401 OR login failed
- NOT loginSuccessful

Then change `responseTime` to `1.0` and run again. Which boolean flips?

## 12. Challenge

Write a "flaky test detector" boolean.

A run is considered flaky-looking (for this exercise) if:

- the test failed, AND
- (the status was 200 OR the response time was greater than 3 seconds)

Use:

```java
boolean testPassed = false;
int statusCode = 200;
double responseTime = 1.0;
```

Print `looksFlaky`. Then change `statusCode` to `500` and `responseTime` to `1.0` and print again.

## 13. Knowledge Check

1. What does `&&` mean?
2. What does `||` mean?
3. What does `!true` produce?
4. When is `true && false` true?
5. When is `false || true` true?
6. Write a condition: status is 200 and time is less than 2.
7. Write a condition: status is 404 or status is 500.
8. Why is `browser.equals("chrome")` used instead of `browser == "chrome"`?
9. Translate to English: `!pageLoaded && attempt < 3`
10. True or false: `||` requires both sides to be true.

## 14. Interview Question

**Question:** How do logical operators work in Java, and how would you use them in a test?

A strong answer:

> && is and, || is or, and ! is not. They combine boolean values. A test might pass only if the status code is 200 and the response time is under a limit and the message equals the expected text. I compare numbers with == and String content with equals. If either the status or the time rule fails, && makes the whole condition false.

## 15. Homework

Write `HomeworkLogicalLogin` with:

- expected username `john` compared using `equalsIgnoreCase`
- actual username `John`
- expected status `200`
- actual status `200`
- response time `0.9`
- max time `2.0`

Create one boolean `testPassed` that is true only when all of these hold:

- username matches ignoring case
- status matches
- time is within the limit

Print `testPassed`. Then break one condition at a time by changing a value, rerun, and watch `testPassed` become `false`.

---

## Answer Key

1. Logical and. Both sides must be true.
2. Logical or. At least one side must be true.
3. `false`
4. Never. Both must be true.
5. Always, because one side is true.
6. `statusCode == 200 && responseTime < 2`
7. `statusCode == 404 || statusCode == 500`
8. Because we compare String content with `equals`, not `==`.
9. The page is not loaded and the attempt number is less than 3.
10. False. `||` is true if either side is true.
