# Part 6 — Data Types

A variable is a named box. A **data type** is the rule that says what is allowed inside that box.

```text
Variable name  →  the label on the box
Data type      →  the kind of thing the box can hold
Value          →  the thing currently inside
```

If you skip data types, Java will feel random. One line will store `25`, another will store `"25"`, and they will not behave the same. The first is a number you can add. The second is text.

## The Big Split

Java values fall into two families:

```text
DATA TYPES
    │
    ├── Primitive types
    │     byte, short, int, long
    │     float, double
    │     char
    │     boolean
    │
    └── Reference types
          String
          arrays
          objects you create later, such as User
```

Primitive types store a simple value directly.  
Reference types store a pointer to an object.

You will feel this difference most clearly with `String`.

## What You Will Learn

- the eight primitive types
- when beginners should use `int`, `long`, `double`, `boolean`, and `char`
- what a reference type is
- how `String` stores text
- common `String` methods used in testing
- why `String` is immutable, meaning methods do not change the original text unless you assign the result to a variable

## Chapters in This Part

| Chapter | Topic | Why it matters |
| --- | --- | --- |
| [Chapter 19](chapter-19-primitive-types.md) | Primitive types | Numbers, characters, and true/false values |
| [Chapter 20](chapter-20-reference-types.md) | Reference types | Objects, including `String` and future classes |
| [Chapter 21](chapter-21-string.md) | `String` methods | Cleaning and checking text from UIs and APIs |
| [Chapter 22](chapter-22-string-immutability.md) | Immutability | Why `toUpperCase()` does not change the original |

## How This Connects to SDET Work

Test data is full of types:

```text
int     expectedStatusCode = 200;
double  responseTimeSeconds = 1.52;
boolean testPassed = true;
char    environmentCode = 'Q';
String  actualMessage = " Login successful ";
```

If you store a status code as text by accident, comparisons get harder. If you forget that `String` methods return a new value, your trim or uppercase "fix" will appear to do nothing.

## Prerequisite

Complete Part 5. You should already be able to declare a variable and print it.

## Study Tip

Do not memorize every number range on the first day. Remember the map:

```text
Whole numbers     → int, sometimes long
Decimal numbers   → double
Yes / no          → boolean
One character     → char
Text              → String
```

The other primitive number types exist, and you will meet them, but daily beginner code uses `int`, `long`, `double`, `boolean`, `char`, and `String` most often.
