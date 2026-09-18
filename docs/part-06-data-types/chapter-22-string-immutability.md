# Chapter 22 — String Immutability

## 1. Today's Goal

By the end of this lesson, you will understand this rule:

> String objects are immutable. Calling a method such as `toUpperCase()` does **not** change the original String. You must use the returned value, usually by assigning it to a variable.

You will stop writing "mystery" code that looks like it changes text but does not.

## 2. Why It Matters

This is one of the most common beginner bugs in Java, and it shows up constantly in test code.

A tester writes:

```java
actualMessage.toUpperCase();
System.out.println(actualMessage);
```

They expect `LOGIN SUCCESSFUL`. They still see `Login successful`. They think Java is broken. Java did exactly what the language promises.

If you do not learn immutability now, you will "fix" messages, trim spaces, and normalize case, then wonder why assertions still fail.

## 3. Real-Life Analogy

Imagine a printed bank statement. You cannot erase the ink. If you want a highlighted copy, the copier gives you a **new** page. The original page stays as it was.

```text
Original statement
     ↓
Make an uppercase copy
     ↓
You now have TWO pages
     original still original
     copy is uppercase
```

If you throw away the copy and keep staring at the original, nothing looks changed.

`trim()` is the same idea: you do not scrape the spaces off the original paper. You receive a new paper without those spaces.

## 4. Illustrated Explanation

Start here:

```java
String name = "John";
```

```text
name ──►  ["John"]
```

Now call a method and ignore the result:

```java
name.toUpperCase();
```

```text
name ──►  ["John"]          ← original object is unchanged

          ["JOHN"]          ← a new object was created and then ignored
```

The uppercase object existed for a moment and then became unused. `name` still points at `"John"`.

To keep the new value, reassign:

```java
name = name.toUpperCase();
```

```text
BEFORE
name ──►  ["John"]

AFTER
name ──►  ["JOHN"]

["John"] may still exist for a moment, but name no longer points at it.
```

The same picture for trim:

```java
String actual = " Login ";
actual.trim();              // original still has spaces
actual = actual.trim();     // now actual refers to "Login"
```

Immutability means:

```text
Once a String object is created,
its characters do not change.

Methods return new String objects.
```

## 5. Syntax / Concept

Common pattern:

```java
String original = "John";
String updated = original.toUpperCase();
```

Now you have both values.

Or replace the variable's reference:

```java
String name = "John";
name = name.toUpperCase();
```

After the second line, `name` refers to `"JOHN"`.

This applies to all of the methods from Chapter 21 that return a `String`:

```java
name = name.toLowerCase();
name = name.trim();
```

Methods that return `boolean` or `int` do not try to change the `String`. They only answer a question:

```java
int size = name.length();
boolean ok = name.contains("JO");
```

There is nothing to reassign there unless you want to store the answer.

Remember:

```text
toUpperCase()  → returns String   → must keep the result if you want it
contains()     → returns boolean  → store the true/false answer
length()       → returns int      → store the number if you need it
```

## 6. Simple Example

```java
public class ImmutabilityDemo {

    public static void main(String[] args) {
        String name = "John";

        name.toUpperCase();
        System.out.println("After calling toUpperCase without assigning: " + name);

        name = name.toUpperCase();
        System.out.println("After assigning the result: " + name);
    }
}
```

Expected output:

```text
After calling toUpperCase without assigning: John
After assigning the result: JOHN
```

A trim version:

```java
public class TrimImmutabilityDemo {

    public static void main(String[] args) {
        String message = " Login successful ";
        System.out.println("Original: [" + message + "]");

        message.trim();
        System.out.println("After trim without assigning: [" + message + "]");

        message = message.trim();
        System.out.println("After assigning trim: [" + message + "]");
    }
}
```

## 7. Real-World Example

A store wants to store coupon codes in uppercase so they can be compared consistently.

```java
public class CouponCode {

    public static void main(String[] args) {
        String couponCode = "  save10  ";

        couponCode.trim();
        couponCode.toUpperCase();
        System.out.println("Still messy: [" + couponCode + "]");

        couponCode = couponCode.trim();
        couponCode = couponCode.toUpperCase();
        System.out.println("Normalized: [" + couponCode + "]");
    }
}
```

Expected output:

```text
Still messy: [  save10  ]
Normalized: [SAVE10]
```

A bank username entered by a customer may need a trimmed copy for lookup, while the original typed value is kept for an audit log:

```java
public class UsernameCleanup {

    public static void main(String[] args) {
        String typedUsername = "  jordan  ";
        String lookupUsername = typedUsername.trim().toLowerCase();

        System.out.println("Typed: [" + typedUsername + "]");
        System.out.println("Lookup: [" + lookupUsername + "]");
    }
}
```

The original `typedUsername` still has spaces. That is not a bug. Immutability let you keep both versions.

## 8. SDET Example

Test cleanup often fails for this exact reason.

```java
public class BrokenThenFixedMessageCleanup {

    public static void main(String[] args) {
        String actualMessage = " Login successful ";

        actualMessage.trim();
        actualMessage.toLowerCase();
        System.out.println("Still original: [" + actualMessage + "]");
        System.out.println("Contains login? " + actualMessage.contains("login"));

        actualMessage = actualMessage.trim();
        actualMessage = actualMessage.toLowerCase();
        System.out.println("Cleaned: [" + actualMessage + "]");
        System.out.println("Contains login? " + actualMessage.contains("login"));
    }
}
```

Expected output:

```text
Still original: [ Login successful ]
Contains login? false
Cleaned: [login successful]
Contains login? true
```

The first `contains("login")` is `false` because the original text starts with a space and uses a capital `L`. The cleanup methods were called, but their results were thrown away.

Professional habit:

```text
Need a cleaned value?
  cleaned = actual.trim().toLowerCase();
Keep actual if you want to print the raw value in a failure report.
```

## 9. Break the Code

This test is supposed to print `PASS` when the banner is `welcome` in any mix of case and spaces. It always prints `FAIL`.

```java
public class BrokenImmutableCheck {

    public static void main(String[] args) {
        String banner = "  Welcome  ";
        banner.trim();
        banner.toLowerCase();

        if (banner.equals("welcome")) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
            System.out.println("Banner was: [" + banner + "]");
        }
    }
}
```

You have not fully studied `if` yet. You can still see the printed `FAIL` and the original banner.

## 10. Debug

Print the value immediately after the supposed cleanup. If it still has spaces and capital letters, the assignment is missing.

Fix:

```java
public class FixedImmutableCheck {

    public static void main(String[] args) {
        String banner = "  Welcome  ";
        banner = banner.trim();
        banner = banner.toLowerCase();

        if (banner.equals("welcome")) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
            System.out.println("Banner was: [" + banner + "]");
        }
    }
}
```

IntelliJ sometimes grays out a method call whose result is ignored. Treat that gray code as a warning: you asked for a new `String` and then dropped it on the floor.

```text
WRONG
banner.toLowerCase();

RIGHT
banner = banner.toLowerCase();
```

You will learn more about `equals` in the operators chapters. For now, notice that we did **not** write `banner == "welcome"` to compare text content.

## 11. Student Exercise

Start with:

```java
String title = "  java for testers  ";
```

Print `title` after each of these steps:

1. call `trim()` without assigning
2. assign `title = title.trim();`
3. call `toUpperCase()` without assigning
4. assign `title = title.toUpperCase();`

Your printed results should prove immutability with your own eyes.

## 12. Challenge

Write `NormalizeStatus`.

Input:

```java
String actualStatus = "  Passed  ";
```

Create a new variable `normalizedStatus` that is trimmed and lowercase, **without losing** the original `actualStatus`. Print both. Then check whether `normalizedStatus` contains `pass`.

If you overwrite `actualStatus` too early, you fail the challenge. The original extra spaces must still be printable.

## 13. Knowledge Check

1. What does immutable mean for `String`?
2. After `name.toUpperCase();` with no assignment, what does `name` still hold if it started as `"John"`?
3. How do you keep the uppercase result in `name`?
4. Does `trim()` change the original object?
5. Why can ignoring a `String` method result look like a test bug?
6. Which methods return a new `String` in this lesson: `toUpperCase`, `contains`, `trim`?
7. Why might a tester keep both the raw message and a cleaned copy?
8. True or false: `length()` changes the `String`.
9. What IntelliJ hint often appears when you ignore a returned `String`?
10. Fill in: `actual = actual._____();` to remove outer spaces.

## 14. Interview Question

**Question:** Why does `name.toUpperCase()` not change `name` in Java?

A strong answer:

> String is immutable, so its characters cannot be changed after the object is created. toUpperCase creates a new String. If I do not assign that result back to a variable, the original reference still points at the old text. Testers must assign cleaned values, for example actual = actual.trim(), or store them in a new variable.

## 15. Homework

Write two methods of demonstration in one class named `HomeworkImmutability` (you may put everything in `main` if methods still feel new).

Case A: show a failing cleanup that calls `trim()` and `toUpperCase()` without assigning.

Case B: show the corrected version with assignment.

Use a realistic API message:

```text
  user created  
```

Print labeled before/after values. In a comment, write the sentence: `String objects are immutable.`

---

## Answer Key

1. The object's characters cannot be changed after creation.
2. `"John"`.
3. `name = name.toUpperCase();`
4. No. It returns a new `String`.
5. Because the raw text is still used in the check.
6. `toUpperCase` and `trim` return `String`. `contains` returns `boolean`.
7. The raw value is useful in failure logs; the cleaned value is useful for comparisons.
8. False. It only reports the size.
9. The unused method result may appear grayed out or as a warning.
10. `trim`
