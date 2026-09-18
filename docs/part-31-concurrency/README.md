# Part 31 — Concurrency

A **process** is a running program (your JVM). **Threads** are workers inside that process.

```text
Process (JVM)
   ├── thread: main
   ├── thread: background work
   └── thread: network wait
```

This part teaches `new Thread(() -> ...)`, race conditions, `synchronized` (cautiously), `ExecutorService`, and **virtual threads**.

## Do Not Confuse Concurrency with Parallelism

```text
Concurrency     many tasks in progress (interleaved). One CPU can juggle them.
Parallelism     many tasks at the same instant on many cores.
```

A receptionist handling three waiting lines is concurrent. Three chefs cooking three meals at the same moment is parallel. Virtual threads help **concurrency** of lots of waiting (I/O). They are not a magic "use all cores for CPU math" button.

## Chapters in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 83](chapter-83-thread.md) | Process vs threads; UI / background / network |
| [Chapter 84](chapter-84-race-condition.md) | Two threads, one shared value |
| [Chapter 85](chapter-85-synchronized.md) | `synchronized` cautiously |
| [Chapter 86](chapter-86-executor-service.md) | Prefer pools over hundreds of `new Thread` |
| [Chapter 87](chapter-87-virtual-threads.md) | Many concurrent tasks, lightweight threads |

## SDET Connection

Parallel test execution, async APIs, timeouts. Flaky tests are often races. Your first SDET years should **prefer** framework-managed parallelism (surefire forks, JUnit parallel config) over homemade threads. Learn the ideas so you can recognize a race.

## Prerequisite

Methods, lambdas, heap sharing (Chapter 80). Heap is shared; stacks are per thread.
