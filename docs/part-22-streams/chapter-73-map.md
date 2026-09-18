# Chapter 73 — map

## 1. Today's Goal

By the end of this lesson, you will use `map` to **transform** each element in a stream.

You will write:

```java
List<String> upper = names.stream()
        .map(String::toUpperCase)
        .toList();
```

and the equivalent lambda `name -> name.toUpperCase()`.

`filter` keeps or drops. `map` changes shape or value. The list size stays the same (one output per input), unless you use other operations later (`flatMap` — not today).

## 2. Why It Matters

API tests often have a list of `User` objects and need a list of usernames. UI tests have locators and need texts. Reports need durations as seconds.

Without `map`, you write a loop, create a new list, `add`. That is fine. `map` is the pipeline word for **transform**. Interviews expect the word.

## 3. Real-Life Analogy

A translator on the conveyor.

```text
Each package has a label in English
map  →  reprint the label in uppercase
still the same number of packages
```

A coat check: you hand in jackets (`User`), you get tickets (`String` ids). Same count, different type.

A currency exchange window: each amount in dollars becomes the same count of amounts in cents (`* 100`). Transform, not filter.

## 4. Illustrated Explanation

```text
names:  [john, ada, lin]
              │
              │ map(String::toUpperCase)
              ▼
upper:  [JOHN, ADA, LIN]
```

```text
Collection → Stream → Filter → Transform → Collect
                              map lives here
```

Filter then map:

```text
[200, 404, 500]
   filter != 200
[404, 500]
   map(code -> "failed:" + code)
["failed:404", "failed:500"]
```

```text
map:   1 in  →  1 out   (possibly different type)
filter: 1 in  →  0 or 1 out  (same type)
```

`String::toUpperCase` means "call `toUpperCase` on each String." It is a method reference for `s -> s.toUpperCase()`.

## 5. Syntax / Concept

```java
List<String> names = List.of("john", "ada", "lin");

List<String> upper = names.stream()
        .map(String::toUpperCase)
        .toList();
```

Lambda form:

```java
.map(name -> name.toUpperCase())
```

Change type:

```java
List<Integer> lengths = names.stream()
        .map(name -> name.length())
        .toList();
```

From objects:

```java
List<String> usernames = users.stream()
        .map(user -> user.username)
        .toList();
```

`map` does not replace `filter`. "Only admins, then their names" is filter then map.

Nulls: `map(String::toUpperCase)` on a null element throws NPE. Clean data first or filter nulls.

## 6. Simple Example

```java
import java.util.List;

public class MapUpperDemo {

    public static void main(String[] args) {
        List<String> names = List.of("john", "ada", "lin");
        List<String> upper = names.stream()
                .map(String::toUpperCase)
                .toList();
        System.out.println(upper);
    }
}
```

Expected:

```text
[JOHN, ADA, LIN]
```

## 7. Real-World Example

Shop SKUs to display labels:

```java
List<String> skus = List.of("kb-1", "ms-2");
List<String> labels = skus.stream()
        .map(sku -> sku.toUpperCase())
        .toList();
```

Bank: account numbers to last-four only:

```java
.map(acct -> acct.substring(acct.length() - 4))
```

Guard length first in real code. This is a sketch.

## 8. SDET Example

```java
import java.util.List;

class User {
    String username;
    String role;

    User(String username, String role) {
        this.username = username;
        this.role = role;
    }
}

public class MapUsernames {

    public static void main(String[] args) {
        List<User> users = List.of(
                new User("john", "tester"),
                new User("ada", "admin")
        );

        List<String> names = users.stream()
                .map(user -> user.username)
                .map(String::toUpperCase)
                .toList();

        System.out.println(names);

        List<String> admins = users.stream()
                .filter(user -> "admin".equals(user.role))
                .map(user -> user.username)
                .toList();
        System.out.println("Admins: " + admins);
    }
}
```

You can chain `map` after `map`. Keep it readable. Two maps vs one lambda that does both: choose clarity.

Compare expected usernames:

```java
if (!names.equals(List.of("JOHN", "ADA"))) {
    throw new AssertionError("TEST FAILED " + names);
}
```

## 9. Break the Code

```java
.map(String::toUpperCase())  // extra ()  — wrong method reference
```

Correct: `String::toUpperCase` without calling it yourself.

```java
.filter(String::toUpperCase)  // toUpperCase is not a boolean predicate
```

Wrong operation.

```java
.map(name -> System.out.println(name))
```

`println` returns `void`. `map` needs a result. Use `forEach` for print-only, or `peek` for debug.

Using `map` when you meant `filter`: you still have the same count, including 200s transformed to something else. Then your "failures list" is the wrong idea.

## 10. Debug

Wrong types in the result list:

1. Look at the last `map` lambda return type.
2. IntelliJ infers `List<Something>`. Hover.

NPE inside `map`: an element was null, or a field was null.

Order bugs: `map` then `filter` vs `filter` then `map` can both be valid but mean different work. Draw the boxes.

Evaluate Expression: ` "john".toUpperCase() ` should be `JOHN`.

```text
filter  = which items
map     = what shape
```

## 11. Student Exercise

`List<String> tests = List.of("login", "checkout", "search");`

- map to upper case with `String::toUpperCase`
- map to `"TEST-" + name`
- print both lists

## 12. Challenge

Given status codes `[200, 404, 500]`, map to strings `"OK"` if 200 else `"FAIL"`.

Then filter only `"FAIL"` (or map after filter — two versions). Print. Assert the fail list is `FAIL, FAIL` (two items). Use AssertionError on mismatch.

## 13. Knowledge Check

1. What does `map` do?
2. How does it differ from `filter`?
3. What does `String::toUpperCase` mean?
4. Does `map` change the original list?
5. Can `map` change `User` into `String`?
6. Why is `map(s -> System.out.println(s))` wrong?
7. Show filter then map in words for admins' usernames.
8. True or false: `map` always changes the list size.
9. What exception if you `toUpperCase` a null element?
10. Recite where Transform sits in the pipeline.

## 14. Interview Question

**Question:** What is `map` on a Java stream?

A strong answer:

> map transforms each element and returns a stream of the results, one per input. Size stays the same. filter keeps or drops elements. I often write names.stream().map(String::toUpperCase).toList() or map a User to user.username. Testers map payloads to fields they assert on. Method references are shorthand for a lambda that only calls that method. I keep map and filter as separate readable steps.

## 15. Homework

Take `List<String>` of three emails. Map to lower case. Map to the part before `@` (use `indexOf` carefully; if missing `@`, fail that item loudly in a loop or filter first).

Draw three boxes: original, after map 1, after map 2.

---

## Answer Key

1. Transforms each element to a new value/type.
2. Filter keeps/drops; map converts.
3. For each string, call `toUpperCase`.
4. No.
5. Yes.
6. `map` must produce a value; `println` is `void` — use `forEach`.
7. Keep users whose role is admin, then take username.
8. False. One in, one out.
9. `NullPointerException`
10. Collection → Stream → Filter → **Transform** → Collect.
