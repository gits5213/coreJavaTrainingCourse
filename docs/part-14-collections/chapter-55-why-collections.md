# Chapter 55 — Why Collections?

## 1. Today's Goal

By the end of this lesson, you will explain why the Collections framework exists, how **List / Set / Queue** sit under **Collection**, and why **Map** is a separate, equally important family.

You will not memorize every class in `java.util`. You will learn the menu.

## 2. Why It Matters

Arrays cannot grow. `String[] users = new String[3]` is wrong on the day you add a fourth user.

You also need different *kinds* of groups:

- keep insertion order of steps → List
- unique browser names even if a CSV repeats them → Set
- `"Content-Type"` → `"application/json"` → Map
- jobs waiting to be processed → Queue

SDET life is data: fixtures, responses, configs. Choosing the wrong box makes uniqueness bugs and JSON-handling pain.

## 3. Real-Life Analogy

Kitchen storage.

```text
Baking tray (array)     — fixed number of cookies
Expandable cooling rack (List) — add more cookies
Spice jars with unique labels (Set) — you do not want two "cumin"
Address book (Map)      — name → phone number
Ticket line (Queue)     — first in line is served next
```

You would not look up a phone number in a ticket line. You would not enforce unique spices with a baking tray.

A test report folder: ordered pages (List), unique bug ids (Set), environment file of keys (Map), failing tests waiting to rerun (Queue).

## 4. Illustrated Explanation

```text
java.util

Collection                      Map
    │                            │
    ├── List                     └── HashMap  (and others)
    │     ArrayList
    ├── Set
    │     HashSet
    └── Queue
          LinkedList can play Queue
```

Collection means "a group of elements."

Map means "a group of pairs."

```text
Collection:   [ a, b, c ]
Map:          a → 1
              b → 2
              c → 3
```

Interfaces vs classes:

```text
List is the interface (the contract)
ArrayList is a class that implements List

Program to the interface:

List<String> browsers = new ArrayList<>();
```

That is the same idea as `Browser browser = new ChromeBrowser()` and `TestDataProvider data = new JsonDataProvider()`.

Why a framework? Because growing, searching, unique-checking, and key lookup are solved problems. You should not write your own unique-list with nested loops unless you are learning.

## 5. Syntax / Concept

You will import from `java.util`:

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
```

Generics: `List<String>` means a list of String objects. The angle brackets are the type of the contents.

```java
List<String> browsers = new ArrayList<>();
```

The `<>` on the right copies the type from the left (the diamond operator).

Collection common verbs (List/Set/Queue share many):

```text
add
remove
size
isEmpty
contains
```

Map verbs are different:

```text
put(key, value)
get(key)
containsKey
keySet
```

Arrays vs collections:

| Array | Collection (typical List) |
| --- | --- |
| `length` fixed | grows with `add` |
| `arr[i]` | `list.get(i)` |
| one type, including primitives | objects (`String`, `User`, later `Integer`) |
| `for` / enhanced for | enhanced for works too |

You still need arrays (`main(String[] args)`, some APIs). Collections are the daily toolbox.

Do not learn all of `LinkedList`, `TreeSet`, `Hashtable` today. Learn **ArrayList**, **HashSet**, **HashMap**, and a **Queue**. Chapter 60 is the choice guide.

## 6. Simple Example

A first taste — growing a list (detail in Chapter 56):

```java
import java.util.ArrayList;
import java.util.List;

public class WhyCollectionsDemo {

    public static void main(String[] args) {
        List<String> browsers = new ArrayList<>();
        browsers.add("Chrome");
        browsers.add("Firefox");
        browsers.add("Edge");

        System.out.println("Count: " + browsers.size());
        for (String browser : browsers) {
            System.out.println(browser);
        }
    }
}
```

Expected output:

```text
Count: 3
Chrome
Firefox
Edge
```

You did not pick a length in advance.

## 7. Real-World Example

A shop's inventory is not one product. It is a list of products, a set of unique SKUs, a map of SKU → quantity, and a queue of restock jobs.

Bank: list of last transactions, set of flagged account ids, map of currency code → rate, queue of pending transfers.

If you store unique SKUs in a List and forget to check duplicates, you will double-count stock. That is a Set job.

## 8. SDET Example

```text
Need                         Structure
-----------------------------------------
Users in a data file         List<User>
Browsers from a matrix       List<String> or later enum list
Unique failed test names     Set<String>
Response JSON object         Map<String, Object> (later)
HTTP headers                 Map<String, String>
qa.properties config         Map<String, String>
Retry jobs                   Queue<String>
```

JSON object:

```text
{ "username": "john", "role": "admin" }

is mentally a Map
  username → john
  role     → admin
```

JSON array:

```text
[ "Chrome", "Firefox" ]

is mentally a List
```

That mapping is why collections (especially List + Map) are not optional for API testers.

## 9. Break the Code

Using only arrays and copying to a bigger array every time you add a user — you are rebuilding `ArrayList` badly.

Using a `List` when you needed unique ids, then asserting `size() == 3` while the file had a duplicate — the test lies.

Treating Map as a List of keys only, then looping to find a value — use `get(key)`.

Saying "I'll use HashMap for everything" including a simple ordered list of steps — you will lose order of steps as a first-class idea (and fight keys that should have been indexes).

## 10. Debug

If you feel lost in class names (`LinkedHashMap`, `CopyOnWriteArrayList`), return to the choice card: order, uniqueness, key/value, processing line.

If IntelliJ cannot find `List`, you imported `java.awt.List` by accident. Import `java.util.List`.

If you want a primitive `int` in a List, you need wrappers (Part 15). The compiler will talk about `Integer`.

## 11. Student Exercise

On paper, classify these as List, Set, Map, or Queue:

1. HTTP headers
2. Browsers to launch in order
3. Unique order confirmation numbers
4. Emails waiting to be sent
5. JSON object of user fields
6. Steps of a test, in order, duplicates possible (`click` twice)

Write one sentence: Map is not a Collection because...

## 12. Challenge

Describe a login API response that has:

- a JSON object (user)
- a JSON array (roles)
- headers
- a retry queue if status is 503

Name the Java type you would use for each piece. You do not need working JSON parsing yet.

## 13. Knowledge Check

1. Why are arrays often not enough?
2. What three interfaces sit under Collection in this course's map?
3. Is Map a Collection?
4. What is `List<String>` saying?
5. What class do we use as the usual List implementation?
6. What class do we use as the usual Map implementation?
7. Give one SDET use of Map.
8. True or false: you should memorize every class in `java.util` this week.
9. Why program to `List` instead of `ArrayList` on the variable?
10. JSON object ≈ which structure?

## 14. Interview Question

**Question:** What is the Java Collections Framework? How do List, Set, Map, and Queue differ?

A strong answer:

> The Collections Framework is a set of interfaces and classes for groups of objects. Collection splits into List (ordered, duplicates allowed), Set (unique elements), and Queue (processing order). Map is separate: keys to values. I usually use ArrayList, HashSet, HashMap, and a Queue implementation. In testing, lists hold users and browsers, sets hold unique ids, maps hold JSON, headers, and config — maps are especially important for API work — and queues hold jobs. I choose by need: order, uniqueness, key/value, or processing line. I program to the interface: List<String> names = new ArrayList<>();

## 15. Homework

Write the choice card in your notes from memory.

Run `WhyCollectionsDemo`.

Add a fourth browser with `add`. Print `size()` again. In a comment, write why this would have been painful with a fixed array.

Read Chapter 56 next. We slow down on List.

---

## Answer Key

1. They have a fixed length and a weaker set of operations; real data grows and needs uniqueness or keys.
2. List, Set, Queue.
3. No. It is a separate family of key/value pairs.
4. A list whose elements are String objects.
5. `ArrayList`
6. `HashMap`
7. Headers, JSON objects, config, environment values, test data tables.
8. False. Learn the menu and the common implementations.
9. Same polymorphism idea: depend on the contract; the class can change.
10. `Map` (nested with `List` for arrays).
