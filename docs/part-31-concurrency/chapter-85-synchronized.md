# Chapter 85 — synchronized

## 1. Today's Goal

By the end of this lesson, you will use `synchronized` **cautiously** to make a critical section run for only one thread at a time.

```java
synchronized (lock) {
    count = count + 1;
}
```

You will know this can fix the lost-update race, and can also **deadlock** or slow everything if you synchronize too much.

## 2. Why It Matters

The increment race is a **critical section**: a few lines that must not interleave.

`synchronized` is the built-in lock on an object. Testers should recognize it in application code when hunting flakes. Testers should **not** sprinkle `synchronized` on every test method "just in case."

Prefer isolation. If you must share, lock the smallest region. Prefer higher-level tools (`ExecutorService`, concurrent collections) when they fit.

## 3. Real-Life Analogy

A talking stick in a meeting.

```text
Only the person holding the stick may update the tally sheet
Others wait
```

If two teams each wait for the other's stick forever: **deadlock**. Two people bow, each waiting for the other to go first.

A bathroom lock: useful. Locking the entire office building to use the bathroom: `synchronized` on too much.

## 4. Illustrated Explanation

```text
synchronized (lock) { count++; }

Thread A gets lock → inc → release
Thread B waits     → then inc

Final count correct
```

```text
CAUTION
  lock too wide     →  all tests run as if single-threaded (slow)
  many locks        →  deadlock risk
  lock the wrong object  →  two locks, race continues
  synchronized as personality  →  you needed isolation, not a lock
```

```text
Deadlock

Thread A: holds lock1, waits lock2
Thread B: holds lock2, waits lock1
        ×  forever
```

```text
Same lock object
  both threads must synchronize on THE SAME instance
  synchronized (new Object()) inside the method  →  useless, new lock every time
```

## 5. Syntax / Concept

Synchronized method (locks `this`):

```java
synchronized void inc() {
    value++;
}
```

Synchronized block (preferred when you have a dedicated lock):

```java
private final Object lock = new Object();

void inc() {
    synchronized (lock) {
        value++;
    }
}
```

Static synchronized locks the `Class` object. Easy to over-lock.

`synchronized` does not make a whole program safe. It only serializes blocks that use the **same** monitor.

It also provides visibility: writes before release become visible after acquire. That is why a locked increment is not only "atomic-ish" but also published.

Cautious rules:

1. Keep the locked block tiny.
2. Do not do network or long sleeps inside `synchronized`.
3. Document what the lock protects.
4. Avoid locking on `this` if outsiders could also lock on your object.
5. Prefer not sharing.

## 6. Simple Example

```java
public class SynchronizedDemo {

    static final Object lock = new Object();
    static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable work = () -> {
            for (int i = 0; i < 10_000; i++) {
                synchronized (lock) {
                    count = count + 1;
                }
            }
        };
        Thread a = new Thread(work);
        Thread b = new Thread(work);
        a.start();
        b.start();
        a.join();
        b.join();
        System.out.println("count = " + count);
    }
}
```

Expected: `20000` consistently (for this pattern).

Compare to Chapter 84.

## 7. Real-World Example

Shop inventory decrement in one JVM: lock per SKU, not the whole shop. (Distributed inventory is a database problem, not `synchronized` across machines. `synchronized` is **one JVM**.)

Bank: do not believe `synchronized` on a server is enough for two server processes. Interviews love that distinction.

## 8. SDET Example

A thread-safe **in-memory** pass counter for a homemade runner:

```java
class SafeTally {
    private int passed = 0;
    private final Object lock = new Object();

    void addPass() {
        synchronized (lock) {
            passed++;
        }
    }

    int getPassed() {
        synchronized (lock) {
            return passed;
        }
    }
}
```

Still better in JUnit: let the framework count.

Do not:

```java
public synchronized void testLogin() { /* entire selenium test */ }
```

You just made parallel tests wait on one method of one object — maybe accidentally the whole suite if they share the instance.

Empty catch inside synchronized still hides failures. Locks ≠ passing tests.

## 9. Break the Code

```java
synchronized (new Object()) {
    count++;
}
```

Each entry has a unique lock. No mutual exclusion.

```java
synchronized (lock) {
    Thread.sleep(10_000);
    count++;
}
```

Lock held during sleep. Throughput dies. Other threads pile up.

Two locks acquired in opposite order in two methods: deadlock (may need a crafted example; still teach the shape).

## 10. Debug

Still racing: not the same lock, or some paths increment without the lock.

Hang: possible deadlock. jstack / IntelliJ thread dump. Look for `BLOCKED` and wait cycles.

Slow suite: lock too coarse. Measure, then shrink.

```text
What object is the monitor?
Is every write and read of that data using it?
Is the block too big?
```

## 11. Student Exercise

Copy the synchronized demo. Confirm 20000.

Break it with `synchronized (new Object())` on purpose. See a wrong count (or increase iterations). Then restore.

## 12. Challenge

Write `SafeTally`. Two threads add 10000 passes each. Assert `getPassed() == 20000` with `AssertionError`.

Write a comment: "I will not lock a whole Selenium test. I isolate tests instead."

## 13. Knowledge Check

1. What does `synchronized (lock)` do?
2. Why must threads share the same lock object?
3. Why is `synchronized (new Object())` broken?
4. What is a deadlock (simple)?
5. Should you sleep inside `synchronized`?
6. Does `synchronized` work across two JVMs?
7. True or false: synchronize every test method to remove flakes.
8. Why still prefer isolation for SDET?
9. Tiny critical section vs whole method: which is cautious?
10. Empty catch inside a locked block: allowed?

## 14. Interview Question

**Question:** How does `synchronized` work, and what are the dangers?

A strong answer:

> synchronized uses an object's monitor so only one thread at a time runs that critical section. It can fix lost updates on count++ if all threads use the same lock. Dangers: synchronizing on a new Object each time does nothing; locking too much kills parallelism; opposite lock orders deadlock; it only works inside one JVM. Testers should use it cautiously and prefer isolated test data. I would not wrap an entire UI test in synchronized to hide races.

## 15. Homework

Re-run Chapter 84 vs 85. Write the two counts in notes.

Draw a deadlock square (A waits B, B waits A).

Read Chapter 86 before you spawn more threads.

---

## Answer Key

1. Only one thread at a time may enter that block for that lock.
2. Otherwise they are not mutually excluding each other.
3. Each visit uses a different lock.
4. Two (or more) threads wait for locks the other holds; nobody proceeds.
5. No (not as a design).
6. No.
7. False.
8. No shared mutable state, fewer flakes, simpler.
9. Tiny section.
10. No.
