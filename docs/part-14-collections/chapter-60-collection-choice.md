# Chapter 60 — Choosing a Collection

## 1. Today's Goal

By the end of this lesson, you will pick **List, Set, Map, or Queue** on purpose using one card:

| Need | Structure |
| --- | --- |
| Ordering (sequence, index) | **List** |
| Uniqueness | **Set** |
| Key → value | **Map** |
| Processing order (a line of work) | **Queue** |

You will practice on SDET situations until the choice feels like a reflex.

## 2. Why It Matters

The framework will not scold you for using `ArrayList` as a unique-id store. Your bugs will.

Wrong structure costs:

- List for unique ids → silent duplicates in reports
- Set for test steps → lost order, lost duplicate clicks
- List of pairs instead of Map → clumsy header lookup
- List + `remove(0)` everywhere → you meant a Queue and never said so

Interviews ask this. Code reviews ask this. Your future API tests *are* this.

## 3. Real-Life Analogy

Choosing a container in a warehouse.

```text
Need to walk aisle in order     → numbered shelf (List)
Need no duplicate serials       → unique-bin policy (Set)
Need lookup by SKU              → catalog binder (Map)
Need packing jobs in line       → conveyor (Queue)
```

Bringing a catalog binder to a ticket line does not serve the next guest.

A chef: mise en place in order (List), unique spices on the rack (Set), recipes by name (Map), tickets to cook (Queue).

## 4. Illustrated Explanation

```text
START: what is the job of this group?

Does each item have a name I look up?
    YES → Map
    NO  ↓

Do I process take-next, usually FIFO?
    YES → Queue
    NO  ↓

Must values be unique?
    YES → Set
    NO  → List   (order / duplicates / index)
```

Hybrids exist:

```text
Unique AND insertion order → LinkedHashSet
Key/value AND insertion order → LinkedHashMap
```

Learn the four defaults first: `ArrayList`, `HashSet`, `HashMap`, `ArrayDeque`.

JSON mapping:

```text
{ ... }   object   → Map
[ ... ]   array    → List
```

That pair will carry you through Rest Assured later.

```text
WRONG: one HashMap for everything
  step0 → open
  step1 → click
  (this is a List of steps wearing a Map costume)
```

## 5. Syntax / Concept

Declare the interface, construct the default class:

```java
List<String> steps = new ArrayList<>();
Set<String> ids = new HashSet<>();
Map<String, String> headers = new HashMap<>();
Queue<String> retries = new ArrayDeque<>();
```

Questions to ask out loud:

1. Will I `get(index)` or care about first/second/third?
2. Is a duplicate a bug or a real second item?
3. Do I look up by a key (header name, username, env key)?
4. Am I draining work from the front?

If 3 is yes, Map wins even if you *could* fake it with two lists.

If 1 and 2 both yes (ordered unique), say so: "LinkedHashSet" or "List plus Set to filter." Do not pretend HashSet keeps your CSV order.

Collection vs Map: if you find yourself calling `add` on something that needs a key, you wanted `put`.

## 6. Simple Example

Four structures in one program, four jobs:

```java
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ChoiceDemo {

    public static void main(String[] args) {
        List<String> browsers = new ArrayList<>();
        browsers.add("Chrome");
        browsers.add("Firefox");

        Set<String> uniqueCodes = new HashSet<>();
        uniqueCodes.add("200");
        uniqueCodes.add("200");

        Map<String, String> config = new HashMap<>();
        config.put("env", "qa");

        Queue<String> jobs = new ArrayDeque<>();
        jobs.offer("retry-login");

        System.out.println("Browsers (order): " + browsers);
        System.out.println("Unique codes: " + uniqueCodes.size());
        System.out.println("Env: " + config.get("env"));
        System.out.println("Next job: " + jobs.poll());
    }
}
```

Expected output:

```text
Browsers (order): [Chrome, Firefox]
Unique codes: 1
Env: qa
Next job: retry-login
```

## 7. Real-World Example

Checkout:

| Piece | Choice | Why |
| --- | --- | --- |
| Line items in the cart | List | order, two mugs allowed |
| Coupon codes applied | Set | unique |
| SKU → quantity | Map | lookup |
| Fulfillment jobs | Queue | warehouse line |

Bank:

| Piece | Choice |
| --- | --- |
| Recent transactions | List |
| Flagged account ids | Set |
| Currency → rate | Map |
| Pending ACH transfers | Queue |

## 8. SDET Example

| Situation | Choice | Why |
| --- | --- | --- |
| Browsers in a cross-browser loop | List | order of launch; duplicates rare but index useful |
| Distinct browsers mentioned in a messy CSV | Set | uniqueness |
| `Accept`, `Content-Type` headers | Map | key/value |
| JSON login body fields | Map | object |
| JSON array of roles | List | array |
| Unique failed test names for a Slack message | Set | uniqueness |
| Ordered test steps in a report | List | sequence |
| Tests waiting for retry | Queue | process next |
| env → baseUrl | Map | config |
| Tags on a method (`smoke`, `api`) | Set | unique tags |

A full tiny suite:

```java
List<String> users = List.of("standard_user", "locked_out_user");
Map<String, String> expectedUrl = new HashMap<>();
expectedUrl.put("standard_user", "/inventory");
expectedUrl.put("locked_out_user", "/login");
Set<String> failed = new HashSet<>();
Queue<String> retries = new ArrayDeque<>();
```

That is a professional shape, even without TestNG.

## 9. Break the Code

Using Set for cart items named `"Mug"` twice — the second mug vanishes.

Using Map for steps `1`, `2`, `3` as keys when you never look up by anything but order — a List was clearer.

Using List + linear search for `"Authorization"` header on every request — Map.

Queue when you needed to peek at job #3 in the middle — if random access is the real need, List; if you only thought you needed it, maybe you did not.

`HashMap` of `Integer` indexes pretending to be a List — stop.

## 10. Debug

When stuck, write the need in one word: **order, unique, lookup, line**. If you wrote two words, you might need two structures (List of events plus Set of seen ids).

If duplicates appear in a "unique" report, you used List.

If order of steps shuffled, you used HashSet.

If `get("Content-Type")` is what you wanted and you are looping a List of strings, refactor to Map.

Code review phrase: "What is the job of this collection?" If the author cannot answer, the type is probably wrong.

## 11. Student Exercise

For each, write List/Set/Map/Queue and one reason:

1. HTTP headers
2. Cart line items
3. Unique order ids in a response
4. Screenshot files waiting to upload
5. `qa.properties` keys
6. Test method tags
7. JSON array of products
8. JSON object for a product
9. Browsers to launch in a fixed sequence
10. Failed tests to retry once

## 12. Challenge

Design (on paper or in empty class comments) a mini runner:

- ordered tests to execute
- expected results by test name
- unique defects found
- retries in a line

Write four field declarations with the correct types (`List`, `Map`, `Set`, `Queue`). Fill them with two fake tests in `main` and print a summary.

Do not write a framework. Write a clear model.

## 13. Knowledge Check

1. When do you choose List?
2. When do you choose Set?
3. When do you choose Map?
4. When do you choose Queue?
5. JSON object maps to which?
6. JSON array maps to which?
7. Default implementations named in this part?
8. True or false: Map is a Collection.
9. Why are two mugs a List problem, not a Set problem?
10. Why are headers a Map problem, not a List problem?

## 14. Interview Question

**Question:** How do you decide between List, Set, Map, and Queue?

A strong answer:

> I start from the job. If I need sequence, index, or duplicates, I use a List — usually ArrayList. If I need uniqueness, I use a Set — usually HashSet — and I remember HashSet does not promise order. If I look up by a key, I use a Map — usually HashMap — which is not a Collection. Testers use maps constantly for JSON, headers, config, and expected-result tables. If I am draining work FIFO, I use a Queue — ArrayDeque. I program to the interfaces. If I need two jobs, I use two structures rather than one confused HashMap.

## 15. Homework

From memory, write the choice table.

Implement `ChoiceDemo` yourself.

Pick a real website you know and list 8 groups of data a test suite would store. Label each List/Set/Map/Queue.

You have finished Part 14 when you can look at JSON and say "object is Map, array is List" without hesitation.

Next: wrapper classes, so `List<Integer>` makes sense.

---

## Answer Key

1. Order, index, duplicates allowed — sequences of browsers, steps, users, JSON arrays.
2. Uniqueness — ids, tags, distinct codes.
3. Key/value lookup — JSON objects, headers, config, expected tables.
4. Processing line — retries, uploads, jobs.
5. Map
6. List
7. `ArrayList`, `HashSet`, `HashMap`, `ArrayDeque`
8. False
9. Two identical product names are two line items; Set would collapse them.
10. You look up a header by name, not by walking a numbered list of "name: value" strings (though HTTP is text on the wire — in Java you still want Map).

**Student exercise suggested answers:** 1 Map 2 List 3 Set 4 Queue 5 Map 6 Set 7 List 8 Map 9 List 10 Queue
