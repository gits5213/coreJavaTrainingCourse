# Chapter 11 — What Happens When Java Runs?

## 1. Today's Goal

By the end of this lesson, you will walk a program from disk to output, using this pipeline:

```
  .java  →  compiler  →  .class  →  class loader  →  JVM
        →  verification  →  interpreter / JIT  →  machine work
        →  (GC cleans unused objects along the way)
```

This is a **preview**, not a JVM-internals degree. You should be able to tell the story in two minutes. You should not be able to write a garbage collector.

---

## 2. Why It Matters

When something fails, the question is **where** in the pipeline.

| Failure | Likely station |
|---------|----------------|
| Red squiggle, `javac` error | compiler |
| `ClassNotFoundException` | class loader / classpath |
| `VerifyError` (rare for you) | verification |
| `UnsupportedClassVersionError` | JVM too old for bytecode |
| `NullPointerException` | your running logic |
| Program gets slower, then faster | interpreter then JIT (normal) |
| `OutOfMemoryError` | memory / GC could not save you |

SDETs who can point at a station debug in minutes. Others reinstall Chrome four times.

---

## 3. Real-Life Analogy

A **theater production**.

```
  Script written          .java
  Script translated       javac → .class (common stage language)
  Stage manager finds     class loader
    the right pages
  Safety check            verification (no obviously illegal moves)
  First rehearsals        interpreter (do it carefully, a bit slower)
  Hit scenes get polish   JIT (compile hot bytecode to native)
  Props recycled          GC (garbage collector)
  Audience sees the play  output
```

If the translated script is missing, the stage manager panics (`ClassNotFound`). If an actor tries to walk through a wall, verification or the JVM's rules stop it. If you never throw away unused props, the theater fills up (`OutOfMemoryError`).

---

## 4. Illustrated Explanation

### The full cartoon

```
  [1] YOU TYPE
      Hello.java
           |
           v
  [2] COMPILER (javac)
      checks grammar and types
      writes Hello.class (bytecode)
           |
           v
  [3] LAUNCHER (java Hello)
      starts a JVM process
           |
           v
  [4] CLASS LOADER
      finds Hello.class
      also loads library classes (String, System, ...)
           |
           v
  [5] BYTECODE VERIFIER
      "Is this bytecode well-formed and type-safe enough to run?"
           |
           v
  [6] EXECUTION
      Interpreter: run bytecode step by step
           |
           |  if a method is HOT (called a lot)
           v
      JIT compiler: turn that bytecode into native machine code
                    then run the fast version
           |
           v
  [7] MACHINE + OS
      actually print characters, read files, send packets
           |
           v
  [8] GC (throughout, not only at the end)
      finds objects you cannot reach anymore
      recycles their memory
```

### Class loader (preview)

```
  java Hello
        |
        v
  "I need class Hello"
        |
        v
  look on the classpath (folders/jars the JVM was told to search)
        |
        +-- found --> load bytes --> link --> initialize
        +-- not found --> ClassNotFoundException / NoClassDefFoundError
```

IntelliJ sets the classpath for you. Later, Maven does. Wrong classpath is a top-5 SDET outage.

### Interpreter vs JIT

```
  First calls of a method:  interpreter  (start quickly)
  Method called many times: JIT may compile it to native (run faster)

  Why not JIT everything immediately?
  Because compiling takes time. Short tests might finish before
  "perfect native code" would pay off.
```

### Garbage Collection (GC)

```
  You write:  String name = new String("Amina");
  (actually literals are special, but the idea stands)

  Objects live in a memory area called the heap.

  If nothing can reach an object anymore, it is garbage.
  GC later reclaims that space. You do not call free() like in C.

  +--------+     becomes unreachable     +--------+
  | object |  ----------------------->   | garbage|
  +--------+                             +--------+
                                              |
                                              v
                                            recycled
```

GC is why Java feels safer than C for beginners. It is also why you can still run out of memory if you *keep* everything (lists that grow forever in a test suite).

---

## 5. Syntax / Concept

| Stage | One-sentence meaning |
|-------|----------------------|
| **Source** | What you maintain. |
| **Compile** | `.java` → `.class`. |
| **Class loader** | Brings class bytes into the JVM. |
| **Verification** | Checks bytecode safety/structure. |
| **Interpreter** | Executes bytecode promptly. |
| **JIT** | Just-In-Time compiler: hot code → native. |
| **GC** | Automatic recycling of unreachable objects. |
| **Classpath** | Where the loader looks for classes. |

`main` is the **entry point**. The launcher looks for `public static void main(String[] args)` on the class you named (classic form).

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("The pipeline made it here.");
    }
}
```

When `println` runs, you are already deep in loaded library classes, native OS calls, and a living JVM. Your one line is the tip of the iceberg — and that is fine.

---

## 6. Simple Example

A program that creates objects and prints. Imagine the pipeline under it.

```java
public class PipelineDemo {

    public static void main(String[] args) {
        String product = "headphones";
        int quantity = 2;
        String line = quantity + " x " + product;

        System.out.println(line);

        line = null; // we drop our handle; GC may later recycle what nothing else references
        System.out.println("Dropped the line variable's object handle.");
    }
}
```

You will not see GC print a receipt. You are only supposed to know **it exists and it is why you did not free memory by hand**.

---

## 7. Real-World Example

Banking: a nightly batch loads `InterestJob.class`.

1. CI compiled it yesterday (compiler).
2. Scheduler runs `java InterestJob`.
3. Class loader loads `InterestJob` and helpers (`Account`, `Money`).
4. If a jar is missing, the job dies **before** interest math — class loader station.
5. If math hits null accounts, you get a runtime exception — execution station.
6. Millions of `Account` objects: JIT may speed the loop; GC reclaims each batch's garbage.

```java
public class InterestJob {

    public static void main(String[] args) {
        double principal = 1000.00;
        double rate = 0.05;
        double interest = principal * rate;
        System.out.println("Interest: " + interest);
    }
}
```

E-commerce checkout is the same pipeline with more classes: `Cart`, `Tax`, `PaymentClient`. Missing `PaymentClient.class` on the server is not "the tax formula is wrong." It is the loader.

---

## 8. SDET Example

A UI test class `LoginTest` depends on `LoginPage`.

```
  java ... LoginTest
       |
       class loader wants LoginTest
       class loader wants LoginPage
       class loader wants org.openqa.selenium.WebDriver
       |
       if selenium jar not on classpath → boom at loader station
```

```java
public class LoginTestPipelineIdea {

    public static void main(String[] args) {
        String actual = "Welcome";
        String expected = "Welcome";

        if (!actual.equals(expected)) {
            throw new AssertionError("Login landing page text mismatch");
        }
        System.out.println("PASS");
    }
}
```

If this were a real Selenium test, a failure might be:

- compile station (typo)
- loader station (missing dependency)
- execution station (element not found)
- environment (browser driver)

**Name the station in your bug report.** That is architect energy at junior volume.

---

## 9. Break the Code

```java
public class BoomAtRuntime {

    public static void main(String[] args) {
        String email = null;
        System.out.println(email.length()); // compiles; explodes while running
    }
}
```

**Predict:** Which pipeline station fails? Compiler, loader, or execution?

Another break (conceptual): you run `java Hello` in the wrong folder so `Hello.class` is not found.

---

## 10. Debug

**Case A:** `NullPointerException` at `email.length()`

- Compiler: happy (`null` is a legal `String` value).
- Loader: happy.
- Execution: you asked a missing object to do work.

**Fix:**

```java
public class BoomFixed {

    public static void main(String[] args) {
        String email = null;

        if (email == null) {
            System.out.println("No email stored.");
        } else {
            System.out.println("Length: " + email.length());
        }
    }
}
```

**Case B:** `Error: Could not find or load main class Hello`

- Loader station. Wrong directory, wrong name, wrong classpath, or you typed `java Hello.class`.

**Fix:** `cd` to the folder, run `java Hello`, or fix IntelliJ run configuration.

**Case C:** slowness on first run of a huge suite, faster later in the same JVM

- Often interpreter → JIT warmup. Not always a "bug." Long-lived servers care more than tiny Hello World.

**Debug mantra:** *Which station printed this error?*

---

## 11. Student Exercise

Copy the 8-step cartoon into your notebook by hand. Under each step, write **one** failure you might see.

Then trace `PipelineDemo` with arrows: which lines are source-only, which become bytecode, which need `System` loaded from the library.

---

## 12. Challenge

A CI log contains three different failures on three days:

1. `cannot find symbol`
2. `Could not find or load main class`
3. `NullPointerException`

Write a table: failure → station → first question you ask.

Then add a fourth row for `UnsupportedClassVersionError`.

---

## 13. Knowledge Check

1. Order these: JIT, source, class loader, javac, verifier.
2. What does the class loader do?
3. What is verification in one sentence?
4. Interpreter vs JIT, in one sentence each.
5. What does GC recycle?
6. True or false: you must call GC by hand after every `println`.
7. Which station is a missing Selenium jar?
8. Which station is a missing semicolon?
9. Which station is `email` being `null` then `.length()`?
10. What is classpath, roughly?

---

## 14. Interview Question

**Question:** "Walk me through what happens when you run a Java program."

**Strong answer:**

> "I write .java source. javac compiles it to .class bytecode. The java command starts a JVM. The class loader loads my class and the library classes it needs from the classpath. The verifier checks the bytecode. The JVM executes it, first interpreting bytecode and JIT-compiling hot methods to native code. The garbage collector reclaims unreachable objects. Output happens when libraries call the operating system. If something fails, I locate whether it was compile, load, or runtime."

That answer is Level 1 complete.

---

## 15. Homework

1. Teach the theater analogy to a rubber duck.
2. Complete the Level 1 *knowledge* checklist items about JVM/JDK from `docs/00-student-levels.md`. Hands-on remains Part 4.
3. Open Part 4 and set up IntelliJ. The theory wants a workshop now.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. source → javac → class loader → verifier → JIT (interpreter is alongside JIT in execution; a fair order is interpreter then JIT).
2. Finds and loads class bytes into the JVM.
3. Checks that bytecode is structurally/type-safe enough to run.
4. Interpreter runs bytecode directly; JIT compiles hot bytecode to native machine code.
5. Memory from objects that are no longer reachable.
6. False.
7. Class loader / classpath.
8. Compiler.
9. Execution / runtime.
10. The list of places the JVM searches for classes.

</details>
