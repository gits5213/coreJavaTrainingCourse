# Chapter 19 — Primitive Types

## 1. Today's Goal

By the end of this lesson, you will know Java's eight primitive types and you will be able to choose a sensible type for everyday values: counts, money-like decimals, true/false flags, and single characters.

You will be able to read this line and explain every word:

```java
int totalTests = 100;
```

## 2. Why It Matters

Java is a **typed** language. The type is not optional decoration. It tells Java:

- how much memory to reserve
- which operations are allowed
- which values are legal

If you try to store text in an `int`, Java refuses. That refusal is a gift. It catches mistakes early.

In testing, the wrong type creates silent confusion:

```text
"200"   is text
200     is a number
```

Those two are not the same. You can add `1` to `200`. You cannot add `1` to `"200"` in the same way without extra work.

## 3. Real-Life Analogy

Think of measuring tools.

```text
A tally counter     → whole numbers     → int
A stopwatch         → decimals          → double
A light switch      → on or off         → boolean
A single letter grade → one character  → char
A very large ID     → huge whole number → long
```

You would not use a light switch to measure a 5K race time. You would not use a stopwatch to count how many tests failed. The tool should match the job.

A mailbox analogy also helps:

```text
boolean mailbox   → only true or false
int mailbox       → only whole numbers in a certain range
double mailbox    → numbers with a decimal point
char mailbox      → exactly one character
```

## 4. Illustrated Explanation

Java has exactly eight primitive types. They are built into the language. You do not import them.

```text
PRIMITIVE TYPES
┌──────────────────────────────────────────────┐
│ Integers (whole numbers)                     │
│   byte    very small whole number            │
│   short   small whole number                 │
│   int     normal whole number   ← most used  │
│   long    large whole number                 │
├──────────────────────────────────────────────┤
│ Decimals (floating point)                    │
│   float   smaller decimal                    │
│   double  normal decimal        ← most used  │
├──────────────────────────────────────────────┤
│ Character                                    │
│   char    one character in single quotes     │
├──────────────────────────────────────────────┤
│ Logical                                      │
│   boolean true or false                      │
└──────────────────────────────────────────────┘
```

A primitive stores the value **in the variable itself**:

```text
int expectedStatusCode = 200;

┌────────────────────────┐
│ expectedStatusCode     │
│ 200                    │  ← the number lives here
└────────────────────────┘
```

Approximate sizes, for orientation, not for memorizing every exam fact today:

```text
byte    8 bits     about -128 to 127
short  16 bits     about -32,768 to 32,767
int    32 bits     about -2 billion to 2 billion
long   64 bits     much larger whole numbers
float  32 bits     decimal with less precision
double 64 bits     decimal with more precision
char   16 bits     one Unicode character
boolean            true or false
```

Beginner rule of thumb:

```text
Need a whole number?           Use int.
Need a huge whole number?      Use long.
Need a decimal?                Use double.
Need yes/no?                   Use boolean.
Need one character?            Use char.
Need text?                     That is String, next chapters.
```

## 5. Syntax / Concept

### `int`

The default whole-number type.

```java
int totalTests = 100;
int failedTests = 10;
int expectedStatusCode = 200;
```

### `long`

Use when a number may not fit in `int`, such as a large transaction id. Write `L` at the end so Java treats the number as a `long`.

```java
long transactionId = 9_000_000_000L;
```

The underscores are optional visual separators. They do not change the value. `9000000000L` is the same number.

### `double`

The default decimal type.

```java
double responseTime = 1.52;
double accountBalance = 250.75;
```

### `float`

A smaller decimal type. You must write `f` or `F` on the number. Beginners should prefer `double` unless a library specifically requires `float`.

```java
float sampleRate = 44.1f;
```

### `boolean`

Only two values: `true` and `false`. No quotes.

```java
boolean testPassed = true;
boolean loginSuccessful = false;
```

### `char`

Exactly one character, in **single quotes**.

```java
char grade = 'A';
char statusFlag = 'Y';
```

Double quotes create a `String`, not a `char`:

```java
char grade = 'A';       // correct
String gradeText = "A"; // also valid, but a different type
```

### `byte` and `short`

These exist for smaller integer storage. You will rarely choose them in everyday application or test code. Know they are whole-number primitives. Prefer `int` unless you have a special reason.

```java
byte tinyCount = 10;
short year = 2026;
```

### Assignment must match the type

```java
int count = 10;          // good
int count = 10.5;        // not allowed
boolean passed = true;   // good
boolean passed = "true"; // not allowed
char letter = 'B';       // good
char letter = "B";       // not allowed
```

## 6. Simple Example

```java
public class PrimitiveTypesDemo {

    public static void main(String[] args) {
        byte smallNumber = 100;
        short year = 2026;
        int totalTests = 100;
        long transactionId = 9_000_000_000L;
        float rating = 4.5f;
        double responseTime = 1.52;
        char grade = 'A';
        boolean testPassed = true;

        System.out.println("byte: " + smallNumber);
        System.out.println("short: " + year);
        System.out.println("int: " + totalTests);
        System.out.println("long: " + transactionId);
        System.out.println("float: " + rating);
        System.out.println("double: " + responseTime);
        System.out.println("char: " + grade);
        System.out.println("boolean: " + testPassed);
    }
}
```

Expected output:

```text
byte: 100
short: 2026
int: 100
long: 9000000000
float: 4.5
double: 1.52
char: A
boolean: true
```

## 7. Real-World Example

A bank account summary uses several primitive types together.

```java
public class BankAccountPrimitives {

    public static void main(String[] args) {
        long accountNumber = 4_882_193_004L;
        double balance = 1250.40;
        int failedLoginCount = 0;
        char accountType = 'S';
        boolean accountLocked = false;

        System.out.println("Account: " + accountNumber);
        System.out.println("Balance: " + balance);
        System.out.println("Failed logins: " + failedLoginCount);
        System.out.println("Type: " + accountType);
        System.out.println("Locked: " + accountLocked);
    }
}
```

An e-commerce item:

```java
public class ProductPrimitives {

    public static void main(String[] args) {
        int productId = 1044;
        int quantityInStock = 37;
        double price = 59.99;
        char sizeCode = 'M';
        boolean inStock = true;

        System.out.println("Product id: " + productId);
        System.out.println("Stock: " + quantityInStock);
        System.out.println("Price: " + price);
        System.out.println("Size: " + sizeCode);
        System.out.println("Available: " + inStock);
    }
}
```

Note: real money systems later use specialized decimal types because `double` is not perfect for currency. For this course's beginner math, `double` is the type we will use for prices and balances.

## 8. SDET Example

A test result record is almost a tour of primitive types.

```java
public class TestRunPrimitives {

    public static void main(String[] args) {
        int expectedStatusCode = 200;
        int actualStatusCode = 200;
        long responseSizeBytes = 2_048L;
        double responseTimeSeconds = 1.52;
        char environment = 'Q';
        boolean testPassed = true;
        int retryCount = 0;

        System.out.println("Expected: " + expectedStatusCode);
        System.out.println("Actual: " + actualStatusCode);
        System.out.println("Bytes: " + responseSizeBytes);
        System.out.println("Seconds: " + responseTimeSeconds);
        System.out.println("Environment: " + environment);
        System.out.println("Passed: " + testPassed);
        System.out.println("Retries: " + retryCount);
    }
}
```

Why each type was chosen:

```text
status codes          → int      whole numbers like 200, 404, 500
file/body size        → long     can grow large
response time         → double   1.52 seconds is not a whole number
environment code      → char     one letter, such as D, Q, P
test passed           → boolean  only true or false
retry count           → int      0, 1, 2, 3
```

## 9. Break the Code

This program is supposed to store test metrics. It will not compile.

```java
public class BrokenPrimitives {

    public static void main(String[] args) {
        int expectedStatusCode = 200;
        int actualStatusCode = "404";
        long transactionId = 9000000000;
        boolean testPassed = "true";
        char grade = "A";
        double responseTime = 1.52;

        System.out.println(expectedStatusCode);
        System.out.println(actualStatusCode);
        System.out.println(transactionId);
        System.out.println(testPassed);
        System.out.println(grade);
        System.out.println(responseTime);
    }
}
```

There are several type mistakes. Find all of them before you look at the debug section.

## 10. Debug

| Problem | Why it fails | Fix |
| --- | --- | --- |
| `int actualStatusCode = "404";` | `"404"` is a `String` | `int actualStatusCode = 404;` |
| `long transactionId = 9000000000;` | The number is too big for `int`, and the literal needs `L` | `long transactionId = 9_000_000_000L;` |
| `boolean testPassed = "true";` | `"true"` is text | `boolean testPassed = true;` |
| `char grade = "A";` | Double quotes make a `String` | `char grade = 'A';` |

Fixed program:

```java
public class FixedPrimitives {

    public static void main(String[] args) {
        int expectedStatusCode = 200;
        int actualStatusCode = 404;
        long transactionId = 9_000_000_000L;
        boolean testPassed = false;
        char grade = 'A';
        double responseTime = 1.52;

        System.out.println(expectedStatusCode);
        System.out.println(actualStatusCode);
        System.out.println(transactionId);
        System.out.println(testPassed);
        System.out.println(grade);
        System.out.println(responseTime);
    }
}
```

IntelliJ hints:

- A red underline on the value usually means "this value does not match the type."
- If a large whole number is underlined, ask: should this be `long`, and did I forget `L`?

## 11. Student Exercise

Create `PrimitivePractice` and declare one variable for each of the eight primitive types. Print all eight.

Then add comments above `int`, `long`, `double`, `boolean`, and `char` explaining when you would choose that type.

## 12. Challenge

Model one failed API test using only primitive types (no `String` except inside `println` labels). Include:

- expected status code
- actual status code
- response time
- retry count
- whether the test passed
- a one-character environment code
- a large request id as `long`

Print a mini report. Choose types on purpose. If you use `int` for the request id, be ready to explain why that might be risky.

## 13. Knowledge Check

1. How many primitive types does Java have?
2. Name all eight.
3. Which primitive should a beginner usually choose for whole numbers?
4. Which primitive should a beginner usually choose for decimals?
5. What values can a `boolean` hold?
6. What is the difference between `'A'` and `"A"`?
7. Why does `9_000_000_000L` end with `L`?
8. Is `"200"` an `int`?
9. Which type fits `testPassed`?
10. True or false: you must import primitive types before using them.

## 14. Interview Question

**Question:** What are Java primitive types, and which ones do you use most in day-to-day code?

A strong beginner answer:

> Java has eight primitive types: byte, short, int, long, float, double, char, and boolean. They store simple values directly. In everyday code I use int for whole numbers, long when a number may be huge, double for decimals, boolean for true or false, and char for a single character. Text is not a primitive; text uses String.

## 15. Homework

Write `HomeworkPrimitives` for a checkout scenario. Use primitives for:

- item quantity
- unit price
- whether the coupon applied
- a one-letter warehouse code
- a large order id
- HTTP status from the payment API
- whether the payment test passed

Print every value with a label. In comments, write one sentence for each variable explaining why you chose that type.

---

## Answer Key

1. Eight.
2. `byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`.
3. `int`.
4. `double`.
5. `true` and `false`.
6. `'A'` is a `char`. `"A"` is a `String`.
7. So Java treats the number as a `long` instead of an `int`.
8. No. Quotes make it text.
9. `boolean`.
10. False. Primitive types are built into the language.
