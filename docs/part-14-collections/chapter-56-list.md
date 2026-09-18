# Chapter 56 — List

## 1. Today's Goal

By the end of this lesson, you will use **`List`** and **`ArrayList`** to store an ordered group that can grow.

You will `add`, `get`, `size`, loop, and understand that **indexes still start at 0**.

## 2. Why It Matters

A test suite has an ordered list of users to try. A page has an ordered list of items in a cart. A report has steps 1, 2, 3.

List keeps **sequence**. The first browser is still index `0`, like arrays, but you can `add` a fourth without rebuilding the structure.

Duplicates are allowed. Two `"Chrome"` entries stay two entries. If you needed uniqueness, that is Set (next chapter).

## 3. Real-Life Analogy

A shopping list.

```text
1. milk
2. eggs
3. milk     ← allowed; you forgot you already wrote milk
```

Order matters if you walk the store aisle by aisle. Duplicates matter if you actually need two milks.

A playlist: song order is the point.

A queue ticket with numbers is closer to Queue. A numbered grocery list you can also jump to item 3 is a List (`get(2)`).

## 4. Illustrated Explanation

```java
List<String> browsers = new ArrayList<>();
browsers.add("Chrome");
browsers.add("Firefox");
browsers.add("Edge");
```

```text
index:     0          1          2
        ┌──────────┬──────────┬──────────┐
        │ Chrome   │ Firefox  │ Edge     │
        └──────────┴──────────┴──────────┘

size() = 3
get(0) = Chrome
get(3) → IndexOutOfBoundsException
```

After `browsers.add("Safari");`:

```text
0 Chrome  1 Firefox  2 Edge  3 Safari
size() = 4
```

`ArrayList` is a resizable array behind the scenes. You do not manage the resizing.

```text
List (interface)          ArrayList (class)
  add / get / size          the actual growing array
```

Variable type `List`, construction `new ArrayList<>()`.

## 5. Syntax / Concept

```java
import java.util.ArrayList;
import java.util.List;

List<String> browsers = new ArrayList<>();
browsers.add("Chrome");
browsers.add("Firefox");
```

Read:

```java
String first = browsers.get(0);
int n = browsers.size();
boolean hasEdge = browsers.contains("Edge");
```

Remove by index or by value:

```java
browsers.remove(1);          // removes Firefox in the example
browsers.remove("Chrome");   // removes the first matching Chrome
```

Removing by index shifts later elements left. Be careful inside a loop.

Enhanced for:

```java
for (String browser : browsers) {
    System.out.println(browser);
}
```

Index for:

```java
for (int i = 0; i < browsers.size(); i++) {
    System.out.println(i + ": " + browsers.get(i));
}
```

Use `size()`, not `length`. `length` is for arrays and `String` (with `()`).

You can put objects in a List:

```java
List<User> users = new ArrayList<>();
users.add(new User("john", "Test1234"));
```

`List.of("Chrome", "Firefox")` creates an **unmodifiable** list (Java 9+). You cannot `add` to it. Useful for fixed fixtures. For a growing list, use `new ArrayList<>()`.

## 6. Simple Example

```java
import java.util.ArrayList;
import java.util.List;

public class ListDemo {

    public static void main(String[] args) {
        List<String> browsers = new ArrayList<>();
        browsers.add("Chrome");
        browsers.add("Firefox");
        browsers.add("Edge");

        System.out.println("First: " + browsers.get(0));
        System.out.println("Size: " + browsers.size());

        for (String browser : browsers) {
            System.out.println("Would launch: " + browser);
        }
    }
}
```

Expected output:

```text
First: Chrome
Size: 3
Would launch: Chrome
Would launch: Firefox
Would launch: Edge
```

## 7. Real-World Example

Cart line items:

```java
import java.util.ArrayList;
import java.util.List;

public class CartList {

    public static void main(String[] args) {
        List<String> items = new ArrayList<>();
        items.add("Mug");
        items.add("Tea");
        items.add("Mug"); // duplicate allowed

        System.out.println("Items: " + items.size());
        System.out.println(items);
    }
}
```

Two mugs is valid. A Set would have collapsed them.

Bank last transactions as a List of amounts (using `Double` — wrappers officially in Part 15; you may print strings for now):

```java
List<String> withdrawals = new ArrayList<>();
withdrawals.add("20.00");
withdrawals.add("5.50");
```

Or wait for `List<Double>` after autoboxing.

## 8. SDET Example

Users to run:

```java
import java.util.ArrayList;
import java.util.List;

class User {
    String username;

    User(String username) {
        this.username = username;
    }
}

public class SdetList {

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add(new User("standard_user"));
        users.add(new User("locked_out_user"));
        users.add(new User("problem_user"));

        for (int i = 0; i < users.size(); i++) {
            System.out.println("Test " + (i + 1) + ": " + users.get(i).username);
        }
    }
}
```

Steps:

```java
List<String> steps = new ArrayList<>();
steps.add("open login");
steps.add("type user");
steps.add("type password");
steps.add("click login");
```

Order is the test. `get(0)` is the first step.

Failed test names can be a List if you want every failure including repeats, or a Set if you want unique names. Decide on purpose.

## 9. Break the Code

```java
List<String> browsers = new ArrayList<>();
browsers.add("Chrome");
System.out.println(browsers.get(1));
```

`IndexOutOfBoundsException` — size is 1, legal index is 0.

Using `length`:

```java
browsers.length; // does not compile
```

Importing `java.awt.List` — wrong List. Methods will look alien.

```java
List<String> fixed = List.of("Chrome");
fixed.add("Firefox"); // UnsupportedOperationException
```

Looping with `remove` on the same list while enhanced-for iterating — `ConcurrentModificationException`. Remove after the loop, or iterate with an iterator (later), or collect indexes to delete.

## 10. Debug

If `get` throws, print `size()` and remember last index is `size() - 1`.

If `add` throws on `List.of`, you needed `new ArrayList<>(List.of(...))` to get a mutable copy, or start with `new ArrayList<>()`.

If order looks wrong, you `add`ed in the wrong sequence, or you `remove`d an index and shifted the rest.

Debugger: expand the list; IntelliJ shows `[0]`, `[1]`, like arrays.

## 11. Student Exercise

Create `List<String> testNames` with four names using `add`.

Print:

- first via `get(0)`
- last via `get(testNames.size() - 1)`
- all with enhanced for
- all with index for labeled `Index 0:`

Then `add` a fifth name and print `size()` again.

## 12. Challenge

`List<String> actualCodes` as `200`, `200`, `404`, `200`, `500` (store as strings or wait — you may use `List<String>`).

Count passed (`"200"`) and failed. Print passed, failed, total.

Print each non-200 with its index.

Then convert your thinking: this is the array homework from Chapter 38, grown into a List. Write one comment: what `add` gives you that the array did not.

## 13. Knowledge Check

1. What is a List?
2. What implementation do we use in this chapter?
3. Write a line that creates an empty `List<String>`.
4. How do you append `"Edge"`?
5. How do you read the first element?
6. What method returns the count?
7. Do Lists allow duplicate values?
8. What exception is `get` past the end?
9. Why might `List.of` reject `add`?
10. True or false: List indexes start at 1.

## 14. Interview Question

**Question:** How does `ArrayList` work, and when do you use a List?

A strong answer:

> List is an ordered collection that allows duplicates and index access. ArrayList is the usual implementation: a resizable array. I declare List<String> browsers = new ArrayList<>(); then add, get, size, and for-each. Indexes start at 0, and get past the end throws IndexOutOfBoundsException. I use List for browsers, users, steps, and any sequence. If I need uniqueness I use a Set. I program to List so I am not glued to ArrayList. List.of makes an unmodifiable list for fixed data.

## 15. Homework

Write `HomeworkList` that stores three browsers and four test users (as strings or `User` objects).

Print both lists. Remove the middle browser. Print again.

Add a comment: `Indexes start at 0. size() not length.`

Optional: `List<User>` with constructors from Part 12.

---

## Answer Key

1. An ordered collection of elements, typically allowing duplicates and index access.
2. `ArrayList`
3. `List<String> names = new ArrayList<>();`
4. `names.add("Edge");`
5. `names.get(0)`
6. `size()`
7. Yes.
8. `IndexOutOfBoundsException`
9. It is unmodifiable.
10. False. They start at 0.
