# Chapter 57 — Set

## 1. Today's Goal

By the end of this lesson, you will use **`Set`** and **`HashSet`** to store **unique** values.

You will see that adding `"Chrome"` twice does not create two entries, and that **order is not the promise** of `HashSet`.

## 2. Why It Matters

Order ids, email addresses, test tags, and browser names in a "unique browsers we saw today" report should not double-count.

If you use a List for unique ids, you will write `if (!list.contains(id)) list.add(id)` forever, and `contains` on a big List is slow. Set is the structure whose job **is** uniqueness.

SDET examples:

- unique failed test names
- unique HTTP status codes observed
- unique user ids in a response array
- tags: `@smoke` should appear once

## 3. Real-Life Analogy

A guest list that refuses a second "Jordan Lee."

A stamp collection: one of each design.

A bag of raffle tickets where duplicate ticket numbers are a fraud, not a feature.

Contrast with a shopping list (List) where two "milk" lines can be honest.

A classroom attendance sheet: a name should appear once. The teacher's seating order is a List. Attendance is a Set.

## 4. Illustrated Explanation

```java
Set<String> browsers = new HashSet<>();
browsers.add("Chrome");
browsers.add("Firefox");
browsers.add("Chrome");
```

```text
Set contents (unique)
  Chrome
  Firefox

size() = 2
The second Chrome was ignored (add returns false).
```

`HashSet` does **not** promise you will print Chrome then Firefox. It might print Firefox first. If you need unique **and** insertion order, there is `LinkedHashSet` — mention it, do not live there yet. If you needed order as the main feature, you wanted a List.

```text
List:  Chrome, Firefox, Chrome     size 3
Set:   Chrome, Firefox             size 2
```

No `get(0)` on `Set`. There is no contract for "first." Loop with enhanced for, or convert to a List if you must index.

```text
for (String browser : browsers) {
    ...
}
```

## 5. Syntax / Concept

```java
import java.util.HashSet;
import java.util.Set;

Set<String> ids = new HashSet<>();
ids.add("ORD-1");
ids.add("ORD-2");
ids.add("ORD-1");
int n = ids.size(); // 2
boolean seen = ids.contains("ORD-1");
ids.remove("ORD-2");
```

`add` returns `boolean`: `true` if the set changed.

Program to `Set`:

```java
Set<String> tags = new HashSet<>();
```

Unmodifiable set:

```java
Set<String> smoke = Set.of("login", "checkout");
```

`Set.of` rejects duplicate arguments at creation (`IllegalArgumentException`) and rejects `null`.

Equality: `HashSet` uses `equals` (and `hashCode`). For `String`, same text means same element. For your own `User` class, until you learn `equals`/`hashCode`, two `User` objects with the same username still count as two. Use `Set<String>` of usernames when that is the uniqueness you mean.

## 6. Simple Example

```java
import java.util.HashSet;
import java.util.Set;

public class SetDemo {

    public static void main(String[] args) {
        Set<String> browsers = new HashSet<>();
        browsers.add("Chrome");
        browsers.add("Firefox");
        boolean addedAgain = browsers.add("Chrome");

        System.out.println("Size: " + browsers.size());
        System.out.println("Second Chrome added? " + addedAgain);
        System.out.println(browsers.contains("Edge"));

        for (String browser : browsers) {
            System.out.println(browser);
        }
    }
}
```

Expected output (order of the two names may vary):

```text
Size: 2
Second Chrome added? false
false
Chrome
Firefox
```

If Firefox prints first, that is still correct.

## 7. Real-World Example

Unique coupon codes used today:

```java
Set<String> coupons = new HashSet<>();
coupons.add("SAVE10");
coupons.add("FREESHIP");
coupons.add("SAVE10");
System.out.println(coupons.size()); // 2
```

Bank: unique account numbers flagged for review. A List would let the same account appear 50 times and scare the compliance team.

## 8. SDET Example

Unique statuses from a run:

```java
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueStatuses {

    public static void main(String[] args) {
        List<String> all = List.of("200", "200", "404", "200", "500", "404");
        Set<String> unique = new HashSet<>();
        for (String code : all) {
            unique.add(code);
        }
        System.out.println("Unique codes: " + unique);
        System.out.println("Count unique: " + unique.size());
    }
}
```

Failed tests, unique names:

```java
Set<String> failed = new HashSet<>();
failed.add("login_standard");
failed.add("login_locked");
failed.add("login_standard");
System.out.println(failed.size()); // 2
```

Tags on a test:

```java
Set<String> tags = new HashSet<>();
tags.add("smoke");
tags.add("login");
tags.add("smoke");
```

## 9. Break the Code

```java
Set<String> browsers = new HashSet<>();
browsers.add("Chrome");
browsers.get(0); // does not compile — no get(int)
```

Asserting print order in a unit test of a HashSet — the test will flake.

Using HashSet of `User` without `equals` and expecting username uniqueness — you will store duplicates.

```java
Set.of("a", "a"); // throws IllegalArgumentException
```

Assuming `add` always grows `size`.

## 10. Debug

If `size` is bigger than you expected, you are not adding the same object according to `equals`. Print the set. For strings, check stray spaces: `"Chrome "` vs `"Chrome"`.

If you need the first inserted unique browser, `HashSet` is the wrong promise. Use `List` plus a Set to filter, or `LinkedHashSet`.

If enhanced for throws, you modified the set while iterating. Collect to-be-removed items, then remove.

Debugger: a HashSet does not show 0,1,2 indexes the way a List does. You will see a hash table picture. That is normal.

## 11. Student Exercise

Add these tags to a `HashSet`: `smoke`, `api`, `smoke`, `ui`, `api`.

Print `size()` (expect 3). Print `contains("api")`. Loop and print each tag.

Then try `add("smoke")` again and print the boolean result.

## 12. Challenge

You are given a `List<String>` of test names that includes duplicates (copy-paste in a CSV).

Build a Set of unique names. Print how many rows were in the list vs how many unique tests.

Print names that were duplicated: extra stretch — loop the list and use a Set of seen names; when `add` returns false, record the name in a `duplicates` set.

## 13. Knowledge Check

1. What is a Set's main job?
2. What implementation do we use here?
3. Does `HashSet` promise insertion order?
4. What happens if you `add` a duplicate String?
5. Why is there no `get(0)`?
6. Write a line that creates a `Set<String>`.
7. What does `contains` tell you?
8. True or false: Sets allow duplicate `String` values.
9. When would you still use a List instead of a Set for test names?
10. Why is Set useful for unique failed tests?

## 14. Interview Question

**Question:** What is a `Set`? When would you choose `HashSet` over `ArrayList`?

A strong answer:

> A Set is a collection that does not allow duplicate elements. HashSet is the common implementation. add ignores a duplicate String and size stays the same. HashSet does not promise order, and it has no index get. I use it for unique ids, tags, and distinct status codes. I use ArrayList when order and duplicates matter, like steps or two cart items with the same name. For my own objects, uniqueness follows equals and hashCode, so I often store Set of ids as strings until I implement equality.

## 15. Homework

Write `HomeworkSet` that reads (hard-codes) a list of browsers including duplicates and prints unique browsers.

Print whether `"Edge"` was present.

In a comment: `HashSet: unique, no index, order not promised.`

---

## Answer Key

1. Store unique elements.
2. `HashSet`
3. No.
4. The set stays the same; `add` returns `false`.
5. A set has no positional contract.
6. `Set<String> tags = new HashSet<>();`
7. Whether that value is already in the set.
8. False.
9. When order of execution or repeated runs of the same name both matter.
10. You want each failing name once in a report, even if it failed in three retries.
