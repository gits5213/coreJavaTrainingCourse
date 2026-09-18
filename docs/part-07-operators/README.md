# Part 7 — Operators

Variables store values. **Operators** let you work with those values: add them, compare them, and combine yes/no questions.

```text
Variables hold data
        ↓
Operators work with data
        ↓
Programs can calculate and decide
```

This part has three families of operators:

```text
Arithmetic     +  -  *  /  %     work with numbers
Comparison     == != > < >= <=   ask questions about values
Logical        && || !           combine or reverse true/false answers
```

## Critical Rule for Text

When two numbers should be equal, `==` is the normal tool:

```java
actualStatusCode == expectedStatusCode
```

When two pieces of **text** should have the same characters, do **not** use `==`.

Use:

```java
name1.equals(name2)
name1.equalsIgnoreCase(name2)
```

`==` on `String` is about references, not everyday "do these words match?" checking. You already learned that `String` is a reference type. The detailed memory explanation of reference comparison versus value comparison will come later. For now, build the safe habit:

```text
Numbers          → ==
String content   → equals or equalsIgnoreCase
```

## Chapters in This Part

| Chapter | Topic | Typical SDET use |
| --- | --- | --- |
| [Chapter 23](chapter-23-arithmetic.md) | Arithmetic | passed tests = total - failed |
| [Chapter 24](chapter-24-comparison.md) | Comparison | actual == expected for status codes; equals for messages |
| [Chapter 25](chapter-25-logical-operators.md) | Logical | status is 200 **and** response is fast |

## Prerequisite

Parts 5 and 6. You should be comfortable with `int`, `double`, `boolean`, and `String`.

## After This Part

Part 8 uses these operators inside `if`, `else`, and `switch` so the program can choose a path: PASS or FAIL, retry or stop, Chrome or Firefox.
