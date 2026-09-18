# Chapter 83 — Threads

## 1. Today's Goal

By the end of this lesson, you will explain a **process** vs a **thread**, and start a thread with:

```java
new Thread(() -> System.out.println("background")).start();
```

You will picture a UI thread, a background thread, and a network thread. You will **not** confuse concurrency with parallelism.

## 2. Why It Matters

If everything runs on `main`, a network call freezes the whole program until it finishes. UIs freeze. Tests that wait on I/O can still be sequential — that is OK. When you run tests in parallel or call async APIs, threads are the mechanism.

SDET: the browser is another process. Your test JVM is a process. HTTP libraries may use worker threads. You need the vocabulary.

`main` is already a thread. You have been concurrent-capable without knowing.

## 3. Real-Life Analogy

A kitchen (the **process**). Cooks (**threads**). They share the fridge (the **heap**). Each cook has their own notepad (the **stack**).

```text
UI cook         talks to the customer, must stay responsive
background cook chops vegetables
network cook    waits at the delivery door
```

Waiting at the door is still "in progress" even if the cook is standing still. That is concurrency: several tasks underway. If only one burner exists, they still take turns (not always parallel).

A restaurant with one waiter serving many tables: concurrent. A restaurant with four waiters carrying four plates at the same instant: also parallel if they move together.

## 4. Illustrated Explanation

```text
Operating system
    │
    ├── Process: Chrome
    ├── Process: IntelliJ
    └── Process: your JVM (java YourClass)
            │
            ├── Thread: main
            ├── Thread: Thread-0  (you started)
            └── Thread: ...
```

```text
new Thread(() -> {
    System.out.println("background");
}).start();

main continues immediately  (unless you join)
background prints soon     (scheduler decides when)
```

```text
.start()   actually asks the JVM to schedule the thread
.run()     runs on THIS thread — a classic bug (no new worker)
```

```text
Concurrency:  timeline with overlapping tasks (may be one core)
Parallelism:  two cores executing two threads at the same moment
```

```text
UI thread ── paints, clicks
     │
     └── must not do 10s network  (UI freeze)

background thread ── file zip, report write
network thread   ── HTTP wait
```

In a console homework there is no real Swing UI. We still use the analogy. Selenium's browser UI is another process anyway.

## 5. Syntax / Concept

```java
Thread t = new Thread(() -> {
    System.out.println("hello from " + Thread.currentThread().getName());
});
t.start();
```

`Thread.sleep(100)` pauses **that** thread (checked `InterruptedException`). Handle or declare. Do not empty-catch interrupts — restoring interrupt status is the professional pattern later; for homework, catch and print / rethrow.

Wait for a thread to finish:

```java
t.join();
```

Without `join`, `main` may exit and you might barely see output (JVM waits for non-daemon threads; still, join is clearer).

Runnable:

```java
Runnable work = () -> System.out.println("work");
new Thread(work).start();
```

Do not create hundreds of threads by hand (Chapter 86). Today: one extra thread.

`Thread.currentThread().getName()` — useful in prints.

## 6. Simple Example

```java
public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("main start " + Thread.currentThread().getName());
        Thread background = new Thread(() -> {
            System.out.println("background " + Thread.currentThread().getName());
        });
        background.start();
        background.join();
        System.out.println("main end");
    }
}
```

Expected shape:

```text
main start main
background Thread-0
main end
```

(Name `Thread-0` can vary.)

If you `run()` instead of `start()`, both prints may say `main`.

## 7. Real-World Example

Shop app:

- UI thread: show cart
- background: resize images
- network: call payment API

Bank ATM: UI waits for PIN; a timeout thread cancels the session. Shared "session still valid" flag is a race if you are careless (next chapter).

## 8. SDET Example

```java
public class ThreadSdetDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread network = new Thread(() -> {
            System.out.println("GET /health (simulated)");
        });
        Thread report = new Thread(() -> {
            System.out.println("writing report (simulated)");
        });
        network.start();
        report.start();
        network.join();
        report.join();
        System.out.println("TEST PASSED — both helpers finished");
    }
}
```

Real tests: prefer the HTTP client and JUnit, not homemade threads, unless you are testing concurrent behavior on purpose.

A UI freeze analogy: do not `Thread.sleep(10000)` on a thread that must poll a condition without a timeout strategy. Sleep is not a wait-for-element API.

## 9. Break the Code

```java
new Thread(() -> System.out.println("hi")).run(); // no new thread
```

```java
new Thread(() -> System.out.println("hi")); // forgot start() — nothing runs
```

```java
main starts thread and immediately ends without join
// output order surprising; sometimes you think it "didn't work"
```

Empty catch of `InterruptedException` swallowing interrupt: the thread no longer knows it was asked to stop. Later cancellation breaks.

## 10. Debug

No output from the worker: forgot `start`, or `main` ended in a more complex program with daemon threads.

Order of prints changes between runs: **normal**. Scheduler. Do not assert exact print order of two unsynchronized threads.

IntelliJ: Debug → Threads view. Switch frames. You will use this in races.

```text
Did I start()?
Did I join() if I need completion?
Concurrency ≠ guaranteed parallel on two cores
```

## 11. Student Exercise

Print from `main`. Start a thread that prints `"background"`. `join`. Print `"done"`.

Print `Thread.currentThread().getName()` in both places.

## 12. Challenge

Simulate UI / background / network with three threads that print their role. Start all, join all.

Then deliberately `run()` one of them and comment the difference in thread names.

Do not add shared counters yet (that is the next chapter).

## 13. Knowledge Check

1. What is a process vs a thread?
2. How do you start a lambda on a new thread?
3. What is the bug of calling `run()` instead of `start()`?
4. Give UI / background / network examples.
5. Concurrency vs parallelism in one line each.
6. What does `join()` do?
7. True or false: `main` is a thread.
8. Do two `println`s from two threads have a guaranteed order?
9. Should SDET beginners parallelize all tests with raw `new Thread`?
10. Empty-catch `InterruptedException`: wise?

## 14. Interview Question

**Question:** What is a thread in Java? How is concurrency different from parallelism?

A strong answer:

> A process is a running program. Threads are workers inside it, sharing the heap but with their own stacks. I can start one with new Thread(() -> work).start(). main is already a thread. Concurrency means many tasks in progress, possibly interleaved on one core. Parallelism means executing at the same instant on multiple cores. A UI should not block on network. Testers should not confuse Selenium waits with JVM threads, and should not spawn hundreds of raw threads.

## 15. Homework

Draw process → threads (UI, background, network).

Run the simple example three times. Note if order of start messages ever surprises you (with more prints it will).

Notes: "start not run. join to wait. concurrent is not automatically parallel."

---

## Answer Key

1. Process = running program; thread = worker inside it.
2. `new Thread(() -> ...).start();`
3. Work runs on the current thread; no extra worker.
4. UI responsive, background report, HTTP wait.
5. Concurrency: many in progress. Parallelism: same instant, extra cores.
6. Waits until that thread finishes.
7. True.
8. No.
9. No.
10. No.
