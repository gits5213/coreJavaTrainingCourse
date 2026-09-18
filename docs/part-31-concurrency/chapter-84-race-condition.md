# Chapter 84 — Race Conditions

## 1. Today's Goal

By the end of this lesson, you will explain a **race condition**: two threads using a **shared** value without coordination, so the result depends on lucky timing.

You will see two threads increment the same `int` (or a holder object) and sometimes **lose updates**.

## 2. Why It Matters

Flaky tests are often races: the assertion ran before the UI thread updated the label; two tests shared one static user id; a counter of "open browsers" is wrong.

If a bug happens "only on CI, only sometimes," think **race** before thinking "CI is haunted."

SDET: parallel tests + shared mutable static data = flakes. Prefer isolation. This chapter shows why.

## 3. Real-Life Analogy

Two cashiers, one paper tally of "customers today."

```text
Both read 10
Both add 1
Both write 11
Two customers were served. Tally is 11. One count lost.
```

That is a race. The shared value is the paper.

Two cooks grabbing the last egg with no talking. Sometimes you get an omelet. Sometimes a fight. Timing.

## 4. Illustrated Explanation

```text
Shared: count = 0

Thread A                         Thread B
read count (0)
                                 read count (0)
add 1 → 1
                                 add 1 → 1
write 1
                                 write 1

Final count = 1   but we wanted 2
```

```text
Happens-before?  neither thread coordinated
Result depends on the scheduler  →  race
```

```text
Not a race: each thread has its own local count, then you add with a lock
Or use a thread-safe adder (later)
Or do not share
```

The heap object is shared (Chapter 80). Two stacks, one `count` field.

```text
Flaky test
  run 1: pass   (lucky order)
  run 2: fail   (assertion too early / lost update)
```

## 5. Syntax / Concept

Unsafe:

```java
class Counter {
    int value = 0;
    void inc() {
        value = value + 1; // not atomic
    }
}
```

`value = value + 1` is read, add, write. Two threads interleave.

A demo may still print 20000 if the computer is fast and the loop is "lucky." Increase iterations. Run several times. The **possibility** is the bug, not a guaranteed fail every run.

Do not "fix" a race by adding `Thread.sleep` until CI is green. Sleep hides races and slows everyone. Next chapter: `synchronized`. Later: `AtomicInteger`, queues, immutability, not sharing.

`boolean ready` without safe publication can also race (visibility). Beginners: if thread A sets a flag and B reads it in a loop, you may loop forever without proper synchronization. We mention visibility so you are not shocked; we do not write a memory-model essay.

## 6. Simple Example

```java
public class RaceDemo {

    static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable work = () -> {
            for (int i = 0; i < 10_000; i++) {
                count = count + 1;
            }
        };
        Thread a = new Thread(work);
        Thread b = new Thread(work);
        a.start();
        b.start();
        a.join();
        b.join();
        System.out.println("count = " + count + " (wanted 20000)");
    }
}
```

Run it several times. You may see 20000 sometimes and less other times. If you always see 20000, increase `10_000` to `100_000` or repeat the whole `main` idea. The lesson is the **lost update** picture, not a guaranteed number on every laptop.

## 7. Real-World Example

Shop: two clicks on "Pay" at once, two threads decrement inventory from 1 to 0 twice... or sell two items from inventory 1. Business race.

Bank: two transfers read balance 100, both subtract 80, both write 20. Lost 80. Banks use transactions and locking. You should not invent bank locking in homework. You should **fear shared mutable money**.

## 8. SDET Example

```java
static int testsPassed = 0; // shared among parallel tests — danger
```

Two tests do `testsPassed++`. Lost updates → report says 7 passed, 8 actually passed.

Better: each test returns a result; the runner aggregates with a thread-safe structure or on one thread at the end.

Shared `static WebDriver driver` for parallel tests: classic flake. Races on the browser session.

Empty catch of assertion in one thread: the other thread thinks all is well. Failures must surface on the thread JUnit is watching, or you `join` and check a result object.

## 9. Break the Code

The simple example **is** the broken code. Shipping it as a production counter is the bug.

"Fix":

```java
Thread.sleep(1); // inside inc
```

Still a race, now slower. Forbidden as a real fix.

```java
if (count == 20000) TEST PASSED else ignore and pass anyway
```

Lying. Run the demo as evidence, do not hide.

## 10. Debug

Reproduce: loop many times, print `count`. If it varies, you have a race (or you have a different bug — but variation is a clue).

IntelliJ: breakpoint with thread suspend policy. Advanced. First: diagram the read-add-write.

Thread dump: if a test hangs, maybe a visibility/wait race.

```text
Shared mutable state + no coordination = race
Fix by not sharing, or coordinating (next chapter), or using concurrent types
```

## 11. Student Exercise

Type `RaceDemo`. Run at least five times. Record the printed counts in notes.

Draw the lost-update diagram with your own numbers.

## 12. Challenge

Create a `Holder` object with `int value` on the heap (not a static int) and pass the same holder to both threads. Same race.

Then give **each** thread its own `Holder` and add the two values on `main` after join. That sum should be 20000. Isolation beat sharing.

## 13. Knowledge Check

1. What is a race condition?
2. Why is `count = count + 1` unsafe for two threads?
3. Why might the demo sometimes show 20000 anyway?
4. Is `Thread.sleep` a correct fix?
5. Give an SDET flake from shared static state.
6. True or false: if a test passed once, there is no race.
7. Two cashiers / one tally: what was lost?
8. Heap sharing vs stacks: which holds the shared `count` field?
9. Should parallel tests share one `WebDriver` static?
10. Hide a wrong count with empty catch: allowed?

## 14. Interview Question

**Question:** What is a race condition? Give an example.

A strong answer:

> A race is when the result depends on uncontrolled timing of threads using shared mutable state. Two threads can both read count 0, both add 1, both write 1, and lose an increment. Testers see this as flakes: CI fails sometimes, shared static drivers, counters of passed tests. Sleep is not a fix. I isolate data or use proper coordination. Concurrency is not automatically parallelism, but races can happen whenever two threads share a value.

## 15. Homework

Write the cashier story in your own words.

After Chapter 85, re-run a synchronized version and compare notes.

Do not use this unsafe counter in any real report homework.

---

## Answer Key

1. Uncoordinated shared access so timing changes the result.
2. It is not one atomic step; two threads can lost-update.
3. Luck / CPU; the bug is still there.
4. No.
5. Shared static driver, shared incrementing ids, passed-test counters.
6. False.
7. One customer count.
8. Heap (the object's field or static in the class object).
9. No.
10. No.
