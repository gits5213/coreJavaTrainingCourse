# Chapter 38 — Arrays

## 1. Today's Goal

By the end of this lesson, you will create an array, read values with indexes that **start at 0**, and loop through a browsers array with both a traditional `for` loop and an **enhanced for** loop.

You will understand this program:

```java
String[] browsers = { "Chrome", "Firefox", "Edge" };
System.out.println(browsers[0]);
```

## 2. Why It Matters

One `String browser` can store only Chrome **or** Firefox **or** Edge. Cross-browser testing needs all of them.

Without arrays you might write:

```java
String browser1 = "Chrome";
String browser2 = "Firefox";
String browser3 = "Edge";
```

That explodes when there are 50 items. Arrays keep one name and a row of slots.

SDET uses arrays (and later lists) for:

- browsers
- test user names
- status codes to try
- environment names

`main` itself receives `String[] args` — an array of command-line text.

## 3. Real-Life Analogy

A train with numbered cars.

```text
Car 0  Car 1  Car 2
Chrome Firefox Edge
```

The first car is car 0. That feels odd if you count from 1 in daily life. In Java, the first slot is always 0.

A row of lockers in a school hallway is the same. Locker 0 is still a locker. Do not skip it.

A parking lot with numbered spaces: space 0, space 1, space 2.

## 4. Illustrated Explanation

```java
String[] browsers = { "Chrome", "Firefox", "Edge" };
```

```text
browsers
index:     0          1          2
        ┌──────────┬──────────┬──────────┐
value:  │ Chrome   │ Firefox  │ Edge     │
        └──────────┘└──────────┘└──────────┘

length: 3
last index: length - 1  →  2
```

Access:

```text
browsers[0]  →  Chrome
browsers[1]  →  Firefox
browsers[2]  →  Edge
browsers[3]  →  crash! ArrayIndexOutOfBoundsException
```

Enhanced for loop (also called for-each):

```text
for each browser in browsers
    print browser

Does not use an index in your code.
Java walks 0, then 1, then 2 for you.
```

```text
Chrome  → print
Firefox → print
Edge    → print
done
```

An array of numbers is the same idea:

```text
statusCodes
[0]=200  [1]=201  [2]=404  [3]=500
```

## 5. Syntax / Concept

Create and fill in one step:

```java
String[] browsers = {
    "Chrome",
    "Firefox",
    "Edge"
};
```

Create an empty row of a given size, then fill:

```java
String[] browsers = new String[3];
browsers[0] = "Chrome";
browsers[1] = "Firefox";
browsers[2] = "Edge";
```

Length:

```java
int howMany = browsers.length;
```

Notice: `length` on an array has **no parentheses**. On a `String`, `length()` has parentheses. That difference trips everyone once.

Index access:

```java
System.out.println(browsers[0]);
```

Traditional loop using indexes:

```java
for (int i = 0; i < browsers.length; i++) {
    System.out.println(browsers[i]);
}
```

Start at `0`. Stop when `i < length`, not `i <= length`. If `length` is 3, legal indexes are 0, 1, 2.

Enhanced for loop:

```java
for (String browser : browsers) {
    System.out.println(browser);
}
```

Read `:` as "in":

```text
for each String browser in browsers
```

Use enhanced for when you need every element and you do not need the index. Use index `for` when you need the number (`Test 1 of 3`) or you need to write into `array[i]`.

Arrays hold one type:

```java
int[] statusCodes = { 200, 201, 404 };
boolean[] results = { true, true, false };
```

An array is a reference type. The variable `browsers` points at the array object.

## 6. Simple Example

```java
public class ArrayDemo {

    public static void main(String[] args) {
        String[] browsers = {
            "Chrome",
            "Firefox",
            "Edge"
        };

        System.out.println(browsers[0]);
        System.out.println("Count: " + browsers.length);

        for (String browser : browsers) {
            System.out.println(browser);
        }
    }
}
```

Expected output:

```text
Chrome
Count: 3
Chrome
Firefox
Edge
```

## 7. Real-World Example

Product sizes:

```java
public class SizesArray {

    public static void main(String[] args) {
        String[] sizes = { "S", "M", "L", "XL" };

        for (int i = 0; i < sizes.length; i++) {
            System.out.println("Size option " + i + ": " + sizes[i]);
        }
    }
}
```

Bank last four transactions as amounts:

```java
double[] withdrawals = { 20.00, 5.50, 100.00 };

double total = 0;
for (double amount : withdrawals) {
    total = total + amount;
}
System.out.println("Total withdrawn: " + total);
```

User names:

```java
String[] usernames = { "john", "admin", "guest" };
```

## 8. SDET Example

Cross-browser names:

```java
public class BrowserArray {

    public static void main(String[] args) {
        String[] browsers = {
            "Chrome",
            "Firefox",
            "Edge"
        };

        System.out.println("First browser: " + browsers[0]);

        for (String browser : browsers) {
            System.out.println("Would launch: " + browser);
        }
    }
}
```

Status codes to assert against:

```java
int[] successCodes = { 200, 201, 204 };

int actual = 201;
boolean ok = false;
for (int code : successCodes) {
    if (actual == code) {
        ok = true;
        break;
    }
}
System.out.println("Success family: " + ok);
```

Login users:

```java
String[] users = { "standardUser", "lockedUser", "admin" };

for (int i = 0; i < users.length; i++) {
    System.out.println("Test " + (i + 1) + ": login as " + users[i]);
}
```

`i + 1` is for human-friendly numbering. The index is still `i` starting at 0.

## 9. Break the Code

```java
public class BrokenArray {

    public static void main(String[] args) {
        String[] browsers = {
            "Chrome",
            "Firefox",
            "Edge"
        };

        System.out.println(browsers[1]);
        System.out.println(browsers[3]);
    }
}
```

The programmer thought "3 browsers means index 3 is the last one," or thought the first browser was at 1 and printed Firefox by accident.

`browsers[3]` throws `ArrayIndexOutOfBoundsException`.

Another bug:

```java
for (int i = 0; i <= browsers.length; i++) {
    System.out.println(browsers[i]);
}
```

`<=` goes one step too far.

`length()` on an array:

```java
browsers.length(); // does not compile
```

## 10. Debug

If you see `ArrayIndexOutOfBoundsException`, print:

```java
System.out.println(browsers.length);
```

Legal indexes: `0` through `length - 1`.

If the "first" browser looks wrong, you used `[1]` instead of `[0]`.

Debugger:

1. Set a breakpoint on the array creation.
2. Inspect `browsers` in Variables. IntelliJ shows `[0]`, `[1]`, `[2]`.
3. Step through the enhanced for and watch `browser` change.

If a loop crashes on the last round, you used `<= length`. Change to `< length`.

## 11. Student Exercise

Create `String[] testNames` with four test names.

Print:

- the first name using `[0]`
- the last name using `testNames[testNames.length - 1]`
- every name with an enhanced for loop
- every name with an index `for` loop, labeled `Index 0:` and so on

## 12. Challenge

`int[] actualCodes = { 200, 200, 404, 200, 500 };`

Loop through the array. Count passed (code == 200) and failed (anything else). Print:

```text
Passed: ...
Failed: ...
Total: ...
```

Then print TEST FAILED details for each non-200, including the index (so engineers know which call failed).

## 13. Knowledge Check

1. What is an array?
2. What is the index of the first element?
3. If `length` is 3, what is the last legal index?
4. How do you write an array of three browser names?
5. What does `browsers[0]` return in the lesson example?
6. What exception happens if you use an index that is too large?
7. How do you write an enhanced for loop over `browsers`?
8. What is the difference between `String.length()` and `array.length`?
9. When do you prefer an index `for` over enhanced for?
10. True or false: arrays can mix `int` and `String` values in one array.

## 14. Interview Question

**Question:** How do arrays work in Java, and why do indexes start at 0?

A strong answer:

> An array stores multiple values of the same type in one variable. Indexes start at 0, so the first element is array[0] and the last is array[length - 1]. Going past that throws ArrayIndexOutOfBoundsException. I can loop with for (int i = 0; i < array.length; i++) or with an enhanced for, for example for (String browser : browsers). Testers use arrays for browsers, users, and status codes. Arrays are reference types. Later we will often prefer List for growing collections.

## 15. Homework

Write `HomeworkArrays` that stores:

- three browsers
- four HTTP status codes
- two boolean test results

Print each array with an enhanced for loop. Print the first and last browser using indexes. In a comment, write: Indexes start at 0.

Optional: write a method `printAll(String[] values)` that uses enhanced for. Call it with your browsers array.

You have finished Part 11 when you can explain why `browsers[1]` is Firefox in a three-item list that starts with Chrome.

---

## Answer Key

1. A named row of values of the same type.
2. 0.
3. 2.
4. `String[] browsers = { "Chrome", "Firefox", "Edge" };`
5. `"Chrome"`
6. `ArrayIndexOutOfBoundsException`
7. `for (String browser : browsers) { ... }`
8. `String` uses a method `length()`. Arrays use a field `length` with no `()`.
9. When you need the index or you need to assign into a slot.
10. False. One array, one type.
