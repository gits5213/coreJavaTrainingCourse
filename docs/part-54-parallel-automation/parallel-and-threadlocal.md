# Parallel Automation and ThreadLocal

## Goal

By the end of this lesson, you will explain why each parallel test needs its own WebDriver, why a singleton driver fails, and how `ThreadLocal<WebDriver>` stores **one driver per thread** without using a global that leaks across threads.

## Why It Matters

Teams turn on `parallel="methods"` in TestNG, CI time drops, and login tests type into each other's fields. Then they disable parallel and call Selenium flaky. The bug was shared mutable state.

Architect interviews: "How do you run 5000 tests in parallel?" If you cannot say ThreadLocal or equivalent isolation, you are not designing a framework.

## Real-Life Analogy

Three cooks, three knives.

```text
Shared knife     cooks bump hands, wrong ingredients
One knife each   independent meals
```

`ThreadLocal` is a labeled hook on each cook's belt: "my knife." You call `get()` and you get **your** knife, not the person next to you.

A singleton driver is one knife on a chain in the middle of the kitchen.

## Illustrated Explanation

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

What goes wrong with one static `WebDriver`:

```text
Thread 1  driver.get(login)
Thread 2  driver.get(cart)     // same driver, navigates away
Thread 1  types username       // types into cart page
```

That is a race. Same class of bug as Part 31, with a browser as the shared variable.

```text
static WebDriver driver;     one for the JVM — BAD in parallel

ThreadLocal<WebDriver>       one slot per thread — GOOD
```

`ThreadLocal` is not magic parallelism. TestNG/JUnit still must run methods on different threads. ThreadLocal only **isolates** what each thread sees.

## ThreadLocal Thoroughly

A `ThreadLocal<T>` holds a value **for the current thread**.

```java
ThreadLocal<String> box = new ThreadLocal<>();

// Thread A
box.set("A");
box.get(); // "A"

// Thread B
box.get(); // null unless B set its own
```

Threads do not see each other's `set` values.

For WebDriver:

```java
public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVERS = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void set(WebDriver driver) {
        DRIVERS.set(driver);
    }

    public static WebDriver get() {
        WebDriver driver = DRIVERS.get();
        if (driver == null) {
            throw new IllegalStateException("No WebDriver for this thread. Did BeforeMethod call set?");
        }
        return driver;
    }

    public static void unload() {
        DRIVERS.remove();
    }
}
```

**`remove()` is mandatory.** Thread pools reuse threads. If you `quit()` but leave the ThreadLocal pointing at a dead driver, the next test on that thread gets a corpse. If you forget `quit()` and forget `remove()`, you leak memory and browsers.

Lifecycle:

```text
@BeforeMethod  (on thread T)
  driver = DriverFactory.create(CHROME)
  DriverManager.set(driver)

@Test
  DriverManager.get().findElement(...)

@AfterMethod  (same thread T — TestNG guarantees this pairing)
  DriverManager.get().quit()
  DriverManager.unload()   // remove()
```

If AfterMethod runs on a different thread than BeforeMethod, ThreadLocal breaks. TestNG's parallel methods keep before/test/after on the same thread. Do not invent a custom thread hop in the middle of a test.

Why not `InheritableThreadLocal`? Child threads might inherit a driver they should not quit. Default `ThreadLocal` is the right default.

Why not `synchronized` on one driver? You would serialize all tests. You paid for parallel then threw it away, and you still have one browser's cookies mixed if you ever slip.

## Syntax — TestNG parallel

`testng.xml`:

```xml
<suite name="ui" parallel="methods" thread-count="3">
  <test name="smoke">
    <classes>
      <class name="com.training.sdet.LoginTest"/>
    </classes>
  </test>
</suite>
```

`parallel="tests"` vs `methods` vs `classes` changes the unit of parallelism. **Methods** is the strictest isolation demand: every method needs its own driver.

## Simple Example

```java
public class ParallelSmokeTest {

    @BeforeMethod
    public void setUp() {
        DriverManager.set(DriverFactory.create(BrowserType.CHROME));
    }

    @Test
    public void one() {
        DriverManager.get().get("https://example.com");
    }

    @Test
    public void two() {
        DriverManager.get().get("https://example.com");
    }

    @AfterMethod
    public void tearDown() {
        WebDriver driver = DriverManager.get();
        driver.quit();
        DriverManager.unload();
    }
}
```

Run with thread-count 2. Two Chromes should appear (or two headless processes).

## Real-World Example

Banking nightly: 8 threads × Chrome. Each thread has its own cookies. UserFactory must create unique users or tests will lock the same account.

E-commerce: inventory tests cannot share one cart in one browser. Isolation is functional correctness, not only speed.

## SDET Example

Page objects should take `WebDriver` in the constructor from `DriverManager.get()`, or receive it as a parameter. They should not look up a static `BaseTest.driver` that is not ThreadLocal.

```java
new LoginPage(DriverManager.get()).login(data);
```

Static locators (`By.id`) are fine — they are immutable. Static **driver** is not.

## Break the Code

```java
public static WebDriver driver; // class field, two methods parallel
```

```java
@AfterMethod
public void tearDown() {
    driver.quit();
    // forgot ThreadLocal.remove()
}
```

```java
DriverManager.get() in a @DataProvider
```

DataProvider may run on another thread. Create data without the driver.

## Debug

Symptoms of missing isolation: random `StaleElementReferenceException`, typed text that belongs to another test, `NoSuchSessionException`.

Debug: log `Thread.currentThread().getName()` and `System.identityHashCode(driver)` at start of each test. If two tests print the same hash, they share a driver.

Leaked Chrome: AfterMethod not running, or quit exception before remove. Use try/finally:

```java
@AfterMethod
public void tearDown() {
    try {
        WebDriver driver = DriverManager.get();
        driver.quit();
    } finally {
        DriverManager.unload();
    }
}
```

If `get()` throws because set never ran, still `remove` if needed — here get failed so unload anyway in a version that `remove` without get.

## Student Exercise

Write `DriverManager` with set/get/unload. Two `@Test` methods that print thread name and driver hash. Run parallel=methods thread-count=2. Confirm hashes differ.

## Challenge

Explain in a half page: ThreadLocal vs Singleton vs synchronized driver. Draw three diagrams. State which one you use for WebDriver and why.

## Knowledge Check

1. Draw three threads and three drivers.
2. What is isolated state?
3. Why singleton WebDriver fails in parallel?
4. What does `ThreadLocal.set` do?
5. What does `get` return on a thread that never set?
6. Why `remove`?
7. Why thread pools make `remove` extra important?
8. Are immutable `By` locators OK as static?
9. Should DataProvider use the driver?
10. How do you prove two tests do not share a driver?

## Interview Question

**Question:** How do you run Selenium tests in parallel?

A strong answer:

> Each test must have isolated state: Thread 1 Driver 1, Thread 2 Driver 2. I do not use a singleton WebDriver. I store drivers in ThreadLocal so get() returns this thread's browser. BeforeMethod creates and set(), AfterMethod quit() and remove(). remove() matters because thread pools reuse threads. Page objects receive the driver from DriverManager.get(). Unique test data avoids colliding users. I explain ThreadLocal before turning on TestNG parallel=methods. Parallel without isolation is not faster; it is flaky.

## Homework

Implement DriverManager. Run two parallel smokes. Commit `Add ThreadLocal DriverManager`. Do not enable parallel on the whole suite until isolation is proven.

---

## Answer Key

1. As in the curriculum diagram
2. No shared mutable browser, cookies, or driver
3. Clicks and navigation interleave
4. Stores a value for the current thread
5. null (unless you used withInitial)
6. Prevent leaks and stale references on reused threads
7. The next test inherits the old value
8. Yes
9. No — different thread / wrong time
10. identity hash / logs of thread name
