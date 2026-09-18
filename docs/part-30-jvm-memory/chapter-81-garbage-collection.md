# Chapter 81 — Garbage Collection

## 1. Today's Goal

By the end of this lesson, you will describe an object's life:

```text
created  →  used  →  unreachable  →  eligible for GC
```

You will know **GC does not mean you can ignore resource management**. Browsers, files, and sockets are not "just objects." Closing them is your job. GC may free the Java wrapper later; the real Chrome process can still be alive.

## 2. Why It Matters

Java does not require `free()` for every `new`. The **garbage collector** reclaims heap memory for objects that nothing can reach.

Beginners hear "GC handles memory" and leak 200 WebDriver processes. Interviewers ask: when is an object eligible? You will say: when there is no chain of references from GC roots (stacks, statics, etc.) to that object.

SDET suites that die overnight: often unreclaimed *native* resources plus huge heaps of saved page sources.

## 3. Real-Life Analogy

Coat check again.

```text
created     coat goes on the rack (new)
used        you wear it / hold the ticket
unreachable you burned every ticket; nobody can claim the coat
eligible    the shop may donate the coat (GC)
collected   rack space frees
```

The collector runs on **its** schedule. You do not `delete` in normal Java. You drop references.

A restaurant table: when the guests leave and nobody is coming back, bus staff **may** clear it. You still take your coat (close the browser). Bus staff will not call you a taxi (close Chrome) just because your Java ticket was lost.

## 4. Illustrated Explanation

```text
User user = new User("John");   // created + reachable from stack

user.doWork();                  // used

user = null;                    // if that was the only reference
                                // object is unreachable
                                // eligible for GC

later: GC may reclaim the bytes
```

```text
created ──► used ──► unreachable ──► eligible ──► (eventually) reclaimed
                         ▲
                         └── still in memory until GC actually runs
```

Sharing:

```text
a ●──► User
b ●──┘

a = null;     still reachable through b
b = null;     now eligible (if no other refs)
```

```text
GC ROOTS (simplified)
  local variables in running methods
  static fields
  JNI / special JVM refs

If the collector cannot walk from a root to your object, it is garbage.
```

```text
File / Browser

Java FileWriter object  ──GC can free the Java object
OS file handle          ──NOT automatically your "I forgot to close"
Chrome process          ──driver.quit() is not GC
```

`System.gc()` is a **hint**, not a command. Do not write tests that `System.gc()` then assert memory. Flaky and rude.

## 5. Syntax / Concept

Eligibility is about **reachability**, not about `delete`.

```java
User user = new User("John"); // reachable
user = null;                  // maybe eligible
```

or leaving the method so the stack frame dies:

```java
public static void temp() {
    User user = new User("John");
} // after return, local user is gone; object eligible if nothing else pointed at it
```

Collections hold references:

```java
list.add(user);
user = null; // object still reachable from the list
```

Leaks in Java are usually **unintended reachable** objects: a static `List<byte[]>` that grows forever, a cache without bounds, a listener never removed.

Finalizers (`finalize`) are legacy. Do not learn them as your cleanup plan. Use try-with-resources, `quit()`, `close()`.

GC algorithms (G1, ZGC, ...) are JVM flags for later. Concept: collector finds garbage, reclaims heap.

## 6. Simple Example

```java
public class GcEligibilityDemo {

    public static void main(String[] args) {
        User user = new User("John");
        System.out.println("created and used: " + user.name);
        user = null;
        System.out.println("reference cleared; object eligible if unreachable");
        // no System.gc() required for the lesson
    }
}
```

You will **not** see a print from the collector. That is OK. The lesson is the reachability story, not a live dashboard.

## 7. Real-World Example

Shop: a `new Order()` per request. After the HTTP response, locals vanish; orders become eligible unless stored in a static map "for debugging" that never clears — that static map is a leak.

Bank: session objects in a map keyed by id. If you never remove logged-out sessions, heap grows. GC cannot collect them: they are **reachable** from the map. Reachable is not garbage.

## 8. SDET Example

```java
FakeDriver driver = new FakeDriver();
runTest(driver);
driver = null; // Java object eligible eventually
// Chrome may still be running if you never quit
```

Correct:

```java
FakeDriver driver = new FakeDriver();
try {
    runTest(driver);
} finally {
    driver.quit(); // resource management
}
driver = null;
```

Saving every response body in a static `List<String>` for a 10-hour suite: heap fills, `OutOfMemoryError`. GC is working; your list is a root. Fix the list, do not "tune GC" first.

Empty catch of `OutOfMemoryError`: you cannot reliably continue. Do not.

## 9. Break the Code

```java
static List<byte[]> cache = new ArrayList<>();
// in a loop: cache.add(new byte[1_000_000]);
```

All arrays reachable from static cache. Not eligible. Heap dies.

```java
driver = null;
// forgot quit()
```

Java object vs OS process mixed up.

```java
System.gc();
assert heapIsSmall();
```

Not a specification. Flaky.

Thinking `user = null` **immediately** wipes memory. Eligibility ≠ instant reclaim.

## 10. Debug

`OutOfMemoryError`: heap dump if you must (advanced). First ask: what still has arrows? Statics, caches, lists of pages, screenshots as byte[] in memory.

Browser leak: look at OS process list, not only Java heap. `quit()` in `finally`.

IntelliJ Memory view is optional. Conceptual diagram first.

```text
Is it reachable?  then GC will not take it
Is it a native resource?  then you close it
```

## 11. Student Exercise

Write the four-stage pipeline in comments next to a tiny program that creates a `User`, prints it, sets `null`.

Write a second snippet where a `List` still holds the user after the local variable is nulled. Comment: "not eligible."

## 12. Challenge

Simulate a leak: static list of 5 dummy "page sources." Then fix it by clearing the list in `finally` after a fake suite.

Simulate a driver: `quit()` in `finally` even if the test throws `AssertionError`. Prove quit still runs (print). Do not catch the assertion empty — let it fail after quit, or catch AssertionError **after** finally's natural run (finally already runs). Structure: try/catch/finally with rethrow or no catch.

## 13. Knowledge Check

1. Recite created → used → ? → eligible.
2. When is an object eligible for GC?
3. Does `user = null` immediately free memory?
4. If a static list holds the object, is it eligible?
5. Does GC close Chrome?
6. What should you use instead of hoping GC closes files?
7. Is `System.gc()` a guarantee?
8. True or false: Java memory leaks are impossible because of GC.
9. `OutOfMemoryError` empty catch: allowed?
10. Why `finally { driver.quit(); }`?

## 14. Interview Question

**Question:** How does garbage collection work, and what can it not do?

A strong answer:

> An object is created with new, used while reachable, and becomes eligible when nothing reachable from GC roots can find it. The collector may then reclaim heap memory. Eligibility is not instant deletion. GC does not replace resource management: I still close files and quit browsers. A static collection that grows forever is reachable, so it is not garbage — that is a leak. I would not call System.gc() in tests and hope. I would not empty-catch OutOfMemoryError.

## 15. Homework

Redraw the four stages.

Add to notes: "Reachable from a list = not garbage. Close browsers yourself."

If you have any homework that opens a fake resource, add `finally` close.

---

## Answer Key

1. unreachable (then eligible).
2. When it is unreachable from GC roots.
3. No. It may become eligible; reclaim is later.
4. No.
5. No.
6. try-with-resources / close / quit in finally.
7. No. A hint.
8. False. Unbounded reachable structures leak.
9. No.
10. So quit runs on pass and fail; GC will not quit for you.
