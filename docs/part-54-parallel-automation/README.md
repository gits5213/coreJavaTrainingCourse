# Part 54 — Parallel Automation

Parallel tests save clock time. They destroy suites that share a browser.

```text
Thread 1
 ↓
Driver 1

Thread 2
 ↓
Driver 2

Thread 3
 ↓
Driver 3
```

Each test needs **isolated state**.

Advanced but required for real frameworks:

```java
ThreadLocal<WebDriver>
```

Explain thoroughly before implementation.

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [Parallel and ThreadLocal](parallel-and-threadlocal.md) | Why isolation, race conditions, ThreadLocal in depth, TestNG parallel |

## Prerequisite

Concurrency basics (Part 31) help. Driver factory. Do not enable parallel on a singleton driver.
