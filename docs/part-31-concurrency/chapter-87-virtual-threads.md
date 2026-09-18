# Chapter 87 — Virtual Threads

## 1. Today's Goal

By the end of this lesson, you will explain **virtual threads**: lightweight threads designed so you can have **many concurrent** tasks, especially ones that **wait** (network, file, sleep).

```java
try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
    pool.submit(() -> System.out.println("light task"));
}
```

You will **not** treat virtual threads as automatic parallelism for heavy CPU math, and you will not confuse them with "Java got rid of race conditions."

This is a JDK 21+ story (you are on 25). Older companies on 11/17 may not have them yet.

## 2. Why It Matters

A **platform thread** maps closely to an OS thread. Each has a large stack. You cannot cheaply start 100,000 of them.

A **virtual thread** is scheduled by the JVM onto a smaller set of platform **carrier** threads. When a virtual thread hits a blocking wait the JVM can unmount it and run another. That makes "one thread per request" affordable again.

SDET: lots of concurrent HTTP calls waiting on I/O is the happy case. 100,000 virtual threads each launching Chrome is still 100,000 browsers. Virtual threads do not shrink Chrome.

## 3. Real-Life Analogy

Platform threads: 4 full-time chefs (expensive, real humans).

Virtual threads: 10,000 tickets on a spindle. The 4 chefs pick up a ticket, cook until they must wait for the oven timer, put that ticket aside, pick another.

```text
Many tickets (virtual threads)
Few chefs    (platform carriers)
Waiting oven → chef does other tickets   (concurrency)
Four ovens at once → limited parallelism
```

A library with 4 librarians and 10,000 patrons who mostly wait for books to be fetched from the basement. Cheap to have many patron tickets. Not the same as 10,000 librarians running.

## 4. Illustrated Explanation

```text
Virtual thread ──► (when running) carrier platform thread ──► CPU
     │
     └── blocking I/O  →  unmount  →  another virtual thread mounts
```

```text
new Thread(...)                 typically a platform thread
Thread.ofVirtual().start(...)   virtual
Executors.newVirtualThreadPerTaskExecutor()
                                each submitted task gets a virtual thread
```

```text
Good fit
  many HTTP calls
  many socket waits
  "thread per task" style

Bad confusion
  "virtual = parallel speedup of a tight math loop"
  "virtual = no need for synchronized"
  "virtual = unlimited Selenium"
```

```text
Concurrency:  10,000 waiting calls in progress
Parallelism:  still ~number of cores for CPU work
```

Pinned virtual threads (if they sit in `synchronized` a long time in some cases) is an advanced footnote. Prefer not holding locks during long I/O. Do not memorize pinning for interviews unless asked; say you would read current JDK notes.

## 5. Syntax / Concept

```java
Thread.startVirtualThread(() -> System.out.println("hi"));
```

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> {
        // blocking call is OK style
        return 200;
    });
}
```

`Thread.ofVirtual().name("api-", 0).start(runnable);`

Count concurrent tasks with a pool of virtual threads the same way you `get()` futures. Failures still exist. Races still exist if you share mutable count.

Virtual threads are still **threads**. `Thread.currentThread()` works. `sleep` blocks that virtual thread (cheaply).

Do not mix: creating a virtual thread per task **and** a huge shared unsynchronized counter. You just made a bigger race.

## 6. Simple Example

```java
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualThreadDemo {

    public static void main(String[] args) throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<?> f = executor.submit(() -> {
                System.out.println("virtual? " + Thread.currentThread().isVirtual());
                System.out.println("name " + Thread.currentThread());
            });
            f.get();
        }
    }
}
```

Expected: `virtual? true` (on JDK 21+).

## 7. Real-World Example

Shop API gateway: 10,000 customers waiting on inventory HTTP. Virtual threads: one task per customer wait, few carriers.

Bank batch of 50,000 file-parse-and-wait-for-downstream. Good fit if mostly waiting.

CPU-bound encryption of 50,000 files: you still want a **bounded** platform pool sized to cores. Virtual per task may still run, but will not create extra CPUs.

## 8. SDET Example

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualHttpStyleDemo {

    public static void main(String[] args) throws Exception {
        List<Integer> codes = List.of(200, 201, 200, 500);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Integer>> futures = new ArrayList<>();
            for (int code : codes) {
                futures.add(executor.submit(() -> {
                    Thread.sleep(10); // simulate wait, not a real product wait API
                    return code;
                }));
            }
            List<Integer> not200 = new ArrayList<>();
            for (Future<Integer> future : futures) {
                int code = future.get();
                if (code != 200) {
                    not200.add(code);
                }
            }
            if (not200.isEmpty()) {
                System.out.println("TEST PASSED");
            } else {
                System.out.println("TEST FAILED " + not200);
            }
        }
    }
}
```

`Thread.sleep` in production tests for UI is still a smell. Here it **simulates I/O wait** so virtual threads have something to wait on.

Launching virtual threads that each start a browser: you still have N browsers. Limit **browser** concurrency separately.

## 9. Break the Code

```java
// thinking races gone
static int count;
executor.submit(() -> { count++; }); // still a race with two tasks
```

```java
newVirtualThreadPerTaskExecutor()
// 1000 tasks × new ChromeDriver()
```

Machine dies. Wrong bottleneck.

Catch `Exception` empty on `get()`. Same crime.

Calling this "parallel streams replacement" without thinking: different tools. Parallel streams = CPU-ish split of a collection. Virtual threads = many waits.

## 10. Debug

`isVirtual()` is false: you used `new Thread` or a fixed platform pool.

Task failures: still `Future.get()` / `ExecutionException`.

Hung: deadlocks and waits still exist. Thread dumps include virtual threads (JDK improved this; still learn to read names).

```text
Many concurrent waiters  →  virtual threads shine
Many CPUs needed        →  bound to cores
Shared mutable           →  still synchronize or isolate
```

## 11. Student Exercise

Run `VirtualThreadDemo`. Confirm `isVirtual()` true.

Start one platform `new Thread` and print `isVirtual()` false for comparison.

## 12. Challenge

Submit 20 virtual tasks that `sleep(50)` and return their index. `get` all. Print elapsed wall time with `Instant`/`Duration`. It should be closer to ~50ms than to 20×50ms if they overlap. (First run may include startup.) Comment: this shows **concurrency of waiting**, not that you have 20 cores.

If the number is disappointing, say so in notes (warmup, overhead). Honesty is an SDET skill.

## 13. Knowledge Check

1. What is a virtual thread for?
2. How does it differ from a platform thread (simple)?
3. Recite: many concurrent tasks + lightweight.
4. Do virtual threads remove race conditions?
5. Do they make 1000 Selenium browsers cheap?
6. Show `newVirtualThreadPerTaskExecutor`.
7. Concurrency vs parallelism — which do virtual threads primarily help for I/O?
8. True or false: companies on JDK 11 have virtual threads.
9. Should CPU-heavy work ignore core count because threads are virtual?
10. Empty catch on virtual task `get()`: allowed?

## 14. Interview Question

**Question:** What are Java virtual threads? When would you use them?

A strong answer:

> Virtual threads are lightweight threads scheduled by the JVM onto platform carrier threads. They shine when you have many concurrent tasks that wait on I/O, so you can write thread-per-task style without 100,000 fat OS stacks. I would use Executors.newVirtualThreadPerTaskExecutor() for lots of HTTP calls. They do not remove races, they do not create extra CPU cores, and they do not make browsers free. Concurrency is not the same as parallelism. I still observe Future.get() and I never empty-catch failures.

## 15. Homework

Notes diagram: tickets vs chefs.

One paragraph: where virtual threads help SDET API suites vs where they do not (UI browsers).

You have finished Part 31 when you can start a thread, explain a race, use synchronized cautiously, prefer a pool, describe virtual threads, and refuse to mix up concurrency and parallelism.

---

## Answer Key

1. Cheap many-waiting-tasks concurrency.
2. Platform ≈ OS thread (expensive stack); virtual is JVM-scheduled and lightweight.
3. Many concurrent tasks, lightweight threads.
4. No.
5. No.
6. `Executors.newVirtualThreadPerTaskExecutor()`
7. Concurrency of waiting; not magic extra cores.
8. False. (Preview later; standardized 21+.)
9. No. Bound CPU work to cores.
10. No.
