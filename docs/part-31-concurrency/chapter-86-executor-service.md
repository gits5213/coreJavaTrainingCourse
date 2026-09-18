# Chapter 86 — ExecutorService

## 1. Today's Goal

By the end of this lesson, you will prefer an **ExecutorService** (a thread pool) over creating hundreds of `new Thread` objects yourself.

```java
try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
    pool.submit(() -> System.out.println("task"));
}
```

On JDK 19+ (you are on 25), `ExecutorService` extends `AutoCloseable`. `try-with-resources` shuts it down. Older style: `shutdown()` and `awaitTermination`.

## 2. Why It Matters

`new Thread` per task:

- costs: each platform thread is expensive (memory for a stack)
- no limit: 5000 tests × 1 thread can hurt the machine
- hard to reuse workers
- easy to forget lifecycle

A pool: **N** workers, a **queue** of tasks. You submit work. The pool reuses threads.

SDET: CI agents have 2–4 cores. A pool of 4 for I/O-bound API calls might help. A pool of 200 Chrome instances will melt the agent. Pool size is a product decision, not a flex.

## 3. Real-Life Analogy

A bank with 4 tellers and a ticket queue.

```text
Customers (tasks) wait in line
Tellers (threads) reuse the same 4 people
You do not hire a new employee per customer
```

Hiring 400 tellers for 400 customers: `new Thread` per task. The branch (machine) has no floor space.

A kitchen with 4 burners: you queue tickets, you do not install 400 stoves.

## 4. Illustrated Explanation

```text
BAD
task → new Thread → start
task → new Thread → start
... × 400

GOOD
        ┌──────── ExecutorService (4 workers) ────────┐
tasks → │ queue: t1 t2 t3 ...                         │
        │ workers: W1 W2 W3 W4  (reuse)               │
        └─────────────────────────────────────────────┘
```

```text
submit(Runnable or Callable)
    returns Future for Callable / result
    you can get() and wait
```

```text
shutdown
    stop taking new tasks, finish existing
shutdownNow
    interrupt; more aggressive
try-with-resources (modern)
    close() shuts down the pool
```

```text
newFixedThreadPool(4)     4 platform threads
newSingleThreadExecutor() 1 worker, sequential queue
newVirtualThreadPerTaskExecutor()  Chapter 87
```

Concurrency vs parallelism: a pool of 4 on a 4-core machine **can** run 4 CPU tasks in parallel. A pool of 4 with 100 waiting HTTP calls is mostly **concurrent I/O**, not 100-way CPU parallelism.

## 5. Syntax / Concept

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
```

```java
try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
    Future<?> f = pool.submit(() -> System.out.println("hello"));
    f.get(); // wait, rethrows task failures wrapped
}
```

`get()` throws `ExecutionException` if the task threw. **Unwrap** and fail the test. Do not empty-catch.

Callable with result:

```java
Future<Integer> status = pool.submit(() -> 200);
int code = status.get();
```

Always shut down pools in tests, or the JVM may linger.

Do not `newFixedThreadPool(1000)` "to be safe." Measure. Start small.

`invokeAll` for a list of tasks — useful. Keep the list bounded.

## 6. Simple Example

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorDemo {

    public static void main(String[] args) throws Exception {
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
            Future<?> a = pool.submit(() -> System.out.println("task A " + Thread.currentThread().getName()));
            Future<?> b = pool.submit(() -> System.out.println("task B " + Thread.currentThread().getName()));
            a.get();
            b.get();
        }
        System.out.println("pool closed");
    }
}
```

You should see two tasks, thread names like `pool-1-thread-1`. Order may vary.

## 7. Real-World Example

Shop: resize 50 images with 4 workers.

Bank: process a **bounded** batch of overnight files with a small pool. Unbounded `new Thread` per file on a 100,000-file day: disaster.

The queue is a safety valve. An unbounded queue plus a tiny pool can still use huge memory if you **submit** 100,000 huge tasks at once. Bound your work.

## 8. SDET Example

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorSdetDemo {

    public static void main(String[] args) throws Exception {
        List<Integer> codes = List.of(200, 200, 404);
        try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
            List<Future<Integer>> futures = new ArrayList<>();
            for (int code : codes) {
                futures.add(pool.submit(() -> {
                    // simulate HTTP
                    return code;
                }));
            }
            int failures = 0;
            for (Future<Integer> future : futures) {
                int actual = future.get();
                if (actual != 200) {
                    failures++;
                    System.out.println("TEST FAILED status " + actual);
                }
            }
            if (failures == 0) {
                System.out.println("TEST PASSED");
            }
        }
    }
}
```

If a task throws, `get()` surfaces it. Catch `ExecutionException`, print `getCause()`, fail. Never empty-catch.

Hundreds of `new Thread` for hundreds of tests: prefer the **build tool** / JUnit parallel runner, which is a pool under the hood. You just configured it.

## 9. Break the Code

```java
ExecutorService pool = Executors.newFixedThreadPool(4);
pool.submit(() -> System.out.println("hi"));
// forgot shutdown — depending on type, JVM may not exit
```

```java
pool.submit(() -> { throw new AssertionError("fail"); });
// never get() — main thinks success
```

You must `get()` (or otherwise observe) or the failure stays on the worker.

```java
} catch (Exception e) { }
```

Hides `ExecutionException`. Forbidden.

Creating `newFixedThreadPool(400)` for 400 Chrome tests on a laptop: resource race, flakes, OOM. Wrong tool size.

## 10. Debug

Task "didn't run": pool already shut down; or you never `get` and main ended in a confusing way.

Failure "disappeared": no `get()`, or empty catch.

Hung `get()`: task deadlocked or infinite loop. Dump threads. Time-limited `get(5, SECONDS)` in tests.

```text
Did I shut down?
Did I observe Future.get()?
Is pool size sane for browsers vs HTTP?
```

## 11. Student Exercise

`newFixedThreadPool(2)`, submit two print tasks, `get` both, try-with-resources.

Print thread names.

## 12. Challenge

Submit 5 Callables returning status codes (mix 200 and not). After all `get`, count failures. Print TEST FAILED with each non-200. If a callable throws `IllegalArgumentException` for code 0, let `get` fail the program with a clear print of the cause — no empty catch.

## 13. Knowledge Check

1. Why prefer ExecutorService over hundreds of `new Thread`?
2. What is a thread pool?
3. How do you wait for a submitted task?
4. What if you never `get()` a failing task?
5. How do you close a pool on JDK 25 in the lesson?
6. True or false: a pool of 400 is always faster.
7. Does a pool automatically mean CPU parallelism of all tasks?
8. What exception wraps a task's throw on `get()`?
9. SDET: who should usually manage parallel tests?
10. Empty catch around `get()`: allowed?

## 14. Interview Question

**Question:** Why use ExecutorService instead of creating threads manually?

A strong answer:

> ExecutorService is a thread pool: a bounded number of workers and a queue of tasks. I submit work instead of new Thread for every task. That reuses threads, limits load, and gives me Future.get() to see failures. Hundreds of manual threads waste memory and are hard to shut down. In tests I still size pools carefully, especially with browsers. I shut the pool down, I observe futures, and I never empty-catch ExecutionException. Concurrency of many tasks is not the same as unlimited parallelism.

## 15. Homework

Rewrite a "two threads + join" homework using a pool of 2.

Notes: "Submit tasks. Reuse workers. get() to see failures. Shutdown."

Chapter 87: virtual threads — many concurrent I/O tasks without 400 fat stacks.

---

## Answer Key

1. Reuse, bound the number of threads, easier lifecycle, Future results.
2. Workers + a queue of tasks.
3. `Future.get()` (or invokeAll, etc.).
4. The failure can be missed; main may look green.
5. try-with-resources on ExecutorService / `close()`.
6. False. Resources and queues matter.
7. No. It depends on cores and whether tasks wait on I/O.
8. `ExecutionException`
9. The test framework / CI config, with sane limits.
10. No.
