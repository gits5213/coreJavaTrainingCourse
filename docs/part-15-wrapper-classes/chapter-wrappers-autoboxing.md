# Wrappers and Autoboxing

## 1. Today's Goal

By the end of this lesson, you will connect primitives to their **wrapper classes**, explain **autoboxing** and **unboxing**, and write a **`List<Integer>`** of status codes.

You will understand why `List<int>` does not compile, and why `Integer` can be `null` while `int` cannot.

## 2. Why It Matters

`List`, `Set`, `Map`, and `Queue` store objects. Status codes are `int` in your head. Java needs `Integer` in a `List`.

API JSON numbers become objects in maps. Counts of failed tests live in `Map<String, Integer>`. Optional JSON fields might be missing — a wrapper can be `null`; a primitive cannot.

If you do not know wrappers, generics will look like a wall. If you do, `List<Integer> codes = new ArrayList<>(); codes.add(200);` will feel natural.

## 3. Real-Life Analogy

A coin (`int`) vs a labeled coin envelope (`Integer`).

The vending machine (collections) only accepts envelopes. Autoboxing is the clerk putting your coin in an envelope as you hand it over. Unboxing is you tearing the envelope to get the coin back to do arithmetic.

A checkbox: the primitive `boolean` is always yes or no. `Boolean` can be yes, no, or "not filled in" (`null`). JSON `true`/`false`/missing field matches that.

A price tag object (`Double`) vs a raw number (`double`). The tag can be missing from the shelf.

## 4. Illustrated Explanation

```text
PRIMITIVES              WRAPPERS (classes)
int                     Integer
long                    Long
double                  Double
float                   Float
boolean                 Boolean
char                    Character
byte                    Byte
short                   Short
```

Daily trio for this course:

```text
int     ↔  Integer
double  ↔  Double
boolean ↔  Boolean
```

Autoboxing:

```text
int 200  ──box──►  Integer object 200
list.add(200)  works for List<Integer>
```

Unboxing:

```text
Integer object 200  ──unbox──►  int 200
int code = list.get(0);
```

```text
List<int>        illegal
List<Integer>    legal
```

Null trap:

```text
int x = 200;           // always a number
Integer y = null;      // allowed
int z = y;             // unboxing null → NullPointerException
```

## 5. Syntax / Concept

Manual wrapping (you will rarely write this):

```java
Integer boxed = Integer.valueOf(200);
int back = boxed.intValue();
```

Autoboxing / unboxing (what you will write):

```java
Integer boxed = 200; // autobox
int back = boxed;    // unbox
```

List of codes:

```java
List<Integer> codes = new ArrayList<>();
codes.add(200);      // autobox
codes.add(404);
int first = codes.get(0); // unbox
```

Map of counts:

```java
Map<String, Integer> failedCounts = new HashMap<>();
failedCounts.put("login", 2);
int n = failedCounts.get("login");
```

Useful static helpers:

```java
int parsed = Integer.parseInt("200");
String text = Integer.toString(200);
boolean ok = Boolean.parseBoolean("true");
```

`parseInt` returns `int`. `Integer.valueOf("200")` returns `Integer`.

Equality:

```java
Integer a = 200;
Integer b = 200;
a.equals(b); // true — use equals for objects
```

Do not rely on `==` for `Integer` values (caching of small numbers makes `==` sometimes work and sometimes not). Use `equals` or unbox to `int` and then `==`.

`double` vs `Double`: same story. Prefer `BigDecimal` for money later; `double` is still what beginners use for non-money decimals.

## 6. Simple Example

```java
import java.util.ArrayList;
import java.util.List;

public class WrapperDemo {

    public static void main(String[] args) {
        int primitive = 200;
        Integer wrapped = primitive; // autobox

        System.out.println(wrapped);

        List<Integer> codes = new ArrayList<>();
        codes.add(200);
        codes.add(201);
        codes.add(404);

        int first = codes.get(0); // unbox
        System.out.println("First: " + first);
        System.out.println("Size: " + codes.size());
    }
}
```

Expected output:

```text
200
First: 200
Size: 3
```

## 7. Real-World Example

Prices as `Double` in a list (shop tags):

```java
List<Double> prices = new ArrayList<>();
prices.add(19.99);
prices.add(4.50);
double sum = 0;
for (Double price : prices) {
    sum = sum + price; // unbox each time
}
```

Flags:

```java
List<Boolean> checks = new ArrayList<>();
checks.add(true);
checks.add(false);
```

A bank might use `Boolean enrolledInOverdraft` where `null` means "customer never answered." That is different from `false` (answered no).

## 8. SDET Example

Status codes:

```java
import java.util.ArrayList;
import java.util.List;

public class StatusCodeList {

    public static void main(String[] args) {
        List<Integer> actual = new ArrayList<>();
        actual.add(200);
        actual.add(200);
        actual.add(500);

        int passed = 0;
        for (Integer code : actual) {
            if (code == 200) { // unbox, then compare ints
                passed++;
            }
        }
        System.out.println("Passed: " + passed);
    }
}
```

Config numbers:

```java
Map<String, Integer> timeouts = new HashMap<>();
timeouts.put("short", 5);
timeouts.put("long", 30);
int wait = timeouts.get("short");
```

JSON `{"status": 201}` will often appear as an `Integer` in a map of objects.

Parsing header or query text:

```java
String header = "200";
int status = Integer.parseInt(header);
```

`Boolean` for test flags:

```java
Boolean remote = Boolean.parseBoolean("true");
```

## 9. Break the Code

```java
List<int> codes = new ArrayList<>(); // does not compile
```

Unboxing null:

```java
Integer missing = null;
int code = missing; // NullPointerException
```

This happens with `map.get("missingKey")` if the value type is `Integer` and you assign straight to `int`.

`==` surprise:

```java
Integer a = 200;
Integer b = 200;
System.out.println(a == b); // may be false; 200 is outside the cached range
```

Use `a.equals(b)` or `a.intValue() == b.intValue()`.

`Integer.parseInt("200 OK")` throws `NumberFormatException`. Trim and isolate the number.

## 10. Debug

`Type argument cannot be of primitive type` — you wrote `List<int>`. Change to `Integer`.

NPE on a line that only "adds numbers" — a wrapper was null. Print the value. Use `containsKey` on maps. Check JSON for missing fields.

`NumberFormatException` — the string is not a clean number. Print the string with brackets: `[" 200"]` shows spaces. `trim()` first.

Debugger: an `Integer` is an object; `int` is a primitive in the variables view. Watch autoboxing as you `add(200)` to a list.

## 11. Student Exercise

Create `List<Integer>` with 200, 201, 404, 500.

Print each with enhanced for.

Count how many are `< 400` (success family for this exercise).

Parse `String text = "201"` with `Integer.parseInt` and add it to the list.

## 12. Challenge

Build `Map<String, Integer>` from test name → duration ms.

Put three tests. Print the map.

Compute the total duration by looping `values()` (unbox each `Integer` into an `int` sum).

Then put a key with `null` value (if you use `HashMap`, `put("broken", null)`). Try to unbox it into `int` inside a try/catch or by checking `== null`. Write a comment: missing JSON numbers are this NPE.

## 13. Knowledge Check

1. What wrapper matches `int`?
2. What wrapper matches `double`?
3. What wrapper matches `boolean`?
4. Why is `List<int>` illegal?
5. What is autoboxing?
6. What is unboxing?
7. What happens if you unbox a `null` `Integer`?
8. How do you parse `"404"` into an `int`?
9. True or false: `Integer` can be `null`.
10. Why do SDET lists of status codes use `List<Integer>`?

## 14. Interview Question

**Question:** What are wrapper classes? What is autoboxing?

A strong answer:

> Wrapper classes are object versions of primitives: int and Integer, double and Double, boolean and Boolean. Collections need objects, so I use List<Integer> not List<int>. Autoboxing converts a primitive to a wrapper automatically, so codes.add(200) works. Unboxing goes the other way. Wrappers can be null; unboxing null throws NullPointerException, which happens when a map key is missing or JSON omitted a field. I parse strings with Integer.parseInt. I compare Integer values with equals, not ==. This matters in tests for status codes, timeouts, and counts.

## 15. Homework

Write `HomeworkWrappers` with:

- `List<Integer>` of five status codes
- `List<Boolean>` of pass/fail flags
- `Map<String, Double>` of item → price (two items)

Loop each. Sum the prices.

In a comment, write the six-word map: `int Integer, double Double, boolean Boolean.`

Optional: demonstrate NPE from unboxing null in a tiny try/catch and print a friendly message.

---

## Answer Key

1. `Integer`
2. `Double`
3. `Boolean`
4. Type parameters must be reference types; primitives are not objects.
5. Automatic primitive → wrapper conversion.
6. Automatic wrapper → primitive conversion.
7. `NullPointerException`
8. `Integer.parseInt("404")`
9. True
10. `List` cannot store `int`; autoboxing stores `Integer`, which is what JSON/API counts look like in collections.
