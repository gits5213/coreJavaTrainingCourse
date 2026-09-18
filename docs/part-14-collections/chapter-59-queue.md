# Chapter 59 — Queue

## 1. Today's Goal

By the end of this lesson, you will use a **`Queue`** as a line of work: **offer** items in, **poll** items out.

You will see why "processing order" is a different need than "I want index 2."

## 2. Why It Matters

Retries, email sends, messages from a broker, and "tests still to run" are lines.

A List can fake a queue (`add` at the end, `remove(0)`), but Queue names the intent: **this is a line, not a random-access list.**

SDET uses:

- failed tests waiting for one retry
- screenshots waiting to upload
- HTTP 503 jobs to try again
- events consumed in order

You do not need to become a messaging expert today. You need the picture: first in, first out (FIFO) for a typical queue.

## 3. Real-Life Analogy

A ticket line at a bakery.

```text
front                     back
Alice  →  Bob  →  Cara     (Cara just arrived)
```

You serve Alice next. New people join the back.

A printer queue: documents wait; the first submitted is printed first (ideally).

A help-desk ticket pile processed oldest first.

Not a dictionary (Map). Not a unique guest list (Set). Not a playlist you skip to song 7 (List `get`).

## 4. Illustrated Explanation

```text
offer("loginTest")
offer("checkoutTest")
offer("searchTest")

Queue:
  loginTest → checkoutTest → searchTest
     ^
     poll() takes loginTest
```

After one poll:

```text
checkoutTest → searchTest
```

```text
offer / add    → join the back
peek / element → look at the front without removing
poll / remove  → take the front
```

Some methods return `false`/`null` on failure (`offer`, `poll`, `peek`). Others throw (`add`, `remove`, `element`). Beginners: prefer `offer` and `poll` so an empty queue does not explode.

`LinkedList` implements `Queue`. A common teaching line:

```java
Queue<String> jobs = new LinkedList<>();
```

There are other queues (`ArrayDeque` is often recommended as a general-purpose queue). `LinkedList` is fine to see the interface. `ArrayDeque` is an excellent default:

```java
Queue<String> jobs = new ArrayDeque<>();
```

This lesson uses `ArrayDeque` as the implementation.

Priority queues exist (serve VIP first). Not today. FIFO first.

## 5. Syntax / Concept

```java
import java.util.ArrayDeque;
import java.util.Queue;

Queue<String> jobs = new ArrayDeque<>();
jobs.offer("loginTest");
jobs.offer("checkoutTest");

String next = jobs.peek();  // loginTest, still in queue
String run = jobs.poll();   // loginTest, removed
int n = jobs.size();
```

Empty poll:

```java
Queue<String> empty = new ArrayDeque<>();
String missing = empty.poll(); // null
```

Loop until empty:

```java
while (!jobs.isEmpty()) {
    String job = jobs.poll();
    System.out.println("Run " + job);
}
```

Do not use enhanced for if you are polling inside the same queue — you are mutating a line you are also walking. Prefer `while (!isEmpty()) poll()`.

Queue is a Collection, so `size`, `isEmpty`, `contains` exist. Still, do not `get(i)`. The point is the front.

## 6. Simple Example

```java
import java.util.ArrayDeque;
import java.util.Queue;

public class QueueDemo {

    public static void main(String[] args) {
        Queue<String> jobs = new ArrayDeque<>();
        jobs.offer("loginTest");
        jobs.offer("checkoutTest");
        jobs.offer("searchTest");

        System.out.println("Next: " + jobs.peek());

        while (!jobs.isEmpty()) {
            String job = jobs.poll();
            System.out.println("Running " + job);
        }

        System.out.println("Empty? " + jobs.isEmpty());
    }
}
```

Expected output:

```text
Next: loginTest
Running loginTest
Running checkoutTest
Running searchTest
Empty? true
```

## 7. Real-World Example

Support tickets:

```java
Queue<String> tickets = new ArrayDeque<>();
tickets.offer("Password reset — Alice");
tickets.offer("Overdraft — Bob");
String handling = tickets.poll();
System.out.println("Now serving: " + handling);
```

Printer:

```java
jobs.offer("invoice.pdf");
jobs.offer("report.pdf");
```

Kitchen: tickets of dishes to cook, FIFO unless you later learn priority.

## 8. SDET Example

Retry queue:

```java
import java.util.ArrayDeque;
import java.util.Queue;

public class RetryQueueDemo {

    public static void main(String[] args) {
        Queue<String> retries = new ArrayDeque<>();
        retries.offer("login_standard");
        retries.offer("add_to_cart");

        while (!retries.isEmpty()) {
            String test = retries.poll();
            System.out.println("Retrying " + test);
            boolean passed = test.equals("add_to_cart");
            if (!passed) {
                System.out.println("  failed again — not re-queued in this demo");
            } else {
                System.out.println("  passed on retry");
            }
        }
    }
}
```

Upload jobs:

```java
Queue<String> screenshots = new ArrayDeque<>();
screenshots.offer("login-fail.png");
screenshots.offer("timeout.png");
```

Work the line after the suite, not in random list order if the pipeline must drain oldest first.

A message queue in real systems is a cousin of this idea (RabbitMQ, SQS). Those are servers. `java.util.Queue` is an in-memory line in your process. Same story, smaller stage.

## 9. Break the Code

Using `remove()` on an empty queue — throws `NoSuchElementException`. `poll()` would have returned `null`.

Treating Queue like a List:

```java
jobs.get(1); // not on Queue
```

Enhanced-for while polling the same queue.

Offering `null` to `ArrayDeque` — it does not allow `null`. You will get `NullPointerException`. That is good: null jobs are bugs.

Infinite retry: failed job `offer`ed back forever without a retry counter. Always cap retries (a `Map` of name → attempts, or a small object).

## 10. Debug

If you always process the last item first, you used a stack (`push`/`pop` on `ArrayDeque` as Deque) instead of queue `offer`/`poll`. Queue: offer back, poll front.

If `poll` is `null` earlier than you thought, you already drained the queue, or you never `offer`ed.

Print `size()` before the loop.

Debugger: watch the head of the deque move as you poll.

## 11. Student Exercise

Offer three test names into a `Queue<String>`.

`peek` and print the next job without removing.

Then `poll` all jobs in a `while` loop, printing `Running ...`.

Print `isEmpty()`.

## 12. Challenge

Build a retry system:

- start with a list of failed tests
- offer each into a queue
- each job may "pass" if its name contains `"cart"` (silly rule)
- if it fails, offer it back **only if** it has been tried fewer than 2 times

Hint: `Map<String, String>` for attempt counts as strings, or keep a parallel `Map` — after Part 15 you would use `Integer`. You may store attempts as a second structure: `Map<String, String>` with `"0"`, `"1"`.

Print a log of each try. This is a baby flaky-test retry queue. In real life, retries hide bugs — keep the cap small and the log loud.

## 13. Knowledge Check

1. What is a Queue for?
2. What does FIFO mean?
3. Which methods does this lesson prefer for insert and take?
4. What does `peek` do?
5. What does `poll` return on an empty queue?
6. Why not `remove()` as a beginner default?
7. Write a line that creates a `Queue<String>` with `ArrayDeque`.
8. True or false: Queue is a Collection.
9. Give one SDET use.
10. How is Queue different from List?

## 14. Interview Question

**Question:** What is a `Queue` in Java? When would you use one in testing?

A strong answer:

> A Queue is a collection for processing elements in order, usually FIFO: offer at the back, poll from the front. I prefer offer/poll/peek because they do not throw on an empty queue; poll returns null. I construct Queue<String> jobs = new ArrayDeque<>(); I use it for retry jobs, screenshot uploads, or any work that should drain in arrival order. A List is for index access and general sequences. A Set is for uniqueness. A Map is for keys. Queue names the line. I cap retries so failed jobs cannot loop forever.

## 15. Homework

Write `HomeworkQueue` that offers four jobs, peeks, then polls until empty.

Add a second queue of screenshot names and drain it.

Comment: `Queue = processing order. List = index. Set = unique. Map = key/value.`

---

## Answer Key

1. Holding elements to process in a line.
2. First in, first out.
3. `offer` and `poll`
4. Returns the front without removing it.
5. `null`
6. `remove()` throws when empty.
7. `Queue<String> jobs = new ArrayDeque<>();`
8. True.
9. Retry queue, upload queue, tests still to run.
10. List emphasizes index and random access; Queue emphasizes take-next processing. You typically do not `get(i)` on a queue.
