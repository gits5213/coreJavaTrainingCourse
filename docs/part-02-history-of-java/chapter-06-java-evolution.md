# Chapter 6 — How Java Evolved (Without Memorizing Every Release)

## 1. Today's Goal

By the end of this lesson, you will have a **subway map** of Java, not a phone book of every station.

You will recognize these stops:

| Stop | Why it is on the map |
|------|----------------------|
| **1.0** | Java is born as a public language |
| **2 (1.2)** | "Java 2" era — Java becomes an enterprise platform |
| **5** | Everyday language upgrades: generics, annotations, enhanced for, enums, autoboxing |
| **8** | A new style: lambdas, streams, Optional, modern Date/Time |
| **9** | Modules, JShell; the start of a faster release train |
| **11, 17, 21, 25** | **LTS** (Long-Term Support) versions companies actually standardize on |
| **Modern Java** (scattered across 14–25) | records, pattern matching, switch expressions, text blocks, sealed classes, virtual threads, compact source files |

You will **not** be asked to recite 10, 12, 13, 18, 19, 20, 22, 23, 24 as a party trick. Professionals remember **waves**, then look up details.

---

## 2. Why It Matters

Job posts say "Java 8+" or "Java 17" or "Java 21." If you think Java froze in 1995, you will write 2005-style code in a 2026 suite.

If you try to memorize every release, you will burn out before `if` statements.

This chapter teaches **which waves changed how we think**:

- Java 5: the language grew *types and annotations*
- Java 8: the language grew *functions as values* and a sane date library
- Java 9+: the platform ships more often; **LTS** is how companies stay sane
- Modern Java: less boilerplate, better switches, virtual threads, simpler starter programs

SDETs live here: JUnit uses annotations (Java 5 wave). Streams show up in test data shaping (Java 8 wave). Records make test data objects short (modern wave).

---

## 3. Real-Life Analogy

Think of a **city subway**.

```
  1.0     first line opens (you can ride)
   |
   v
  Java 2  whole districts connected (enterprise city)
   |
   v
  5       better trains, clearer maps (generics, enums, annotations)
   |
   v
  8       express trains and a new clock at every station (lambdas, Date/Time)
   |
   v
  9       turnstiles redesigned (modules), a practice track (JShell)
   |
   v
  11 17 21 25    "LTS stations" — the ones tourists (companies) memorize
```

There are small stations between LTS stops. Locals use them. Tourists should not quiz themselves on every staircase.

**LTS** is the station where the city promises: "We will maintain this platform for years." JDK **25** is this course's recommended LTS baseline.

---

## 4. Illustrated Explanation

### The map (study this, do not tattoo it)

```
  1996   Java 1.0     "It exists."
  1998   Java 2       Platform era (name: Java 2 Standard Edition)
  2004   Java 5       Language jump (also called 1.5)
  2014   Java 8       Language jump (lambdas/streams) — stayed at work forever
  2017   Java 9       Modules + new release cadence begins
  2018   Java 11      LTS
  2021   Java 17      LTS
  2023   Java 21      LTS
  2025   Java 25      LTS  ← this course
```

Between 9 and today, Oracle moved to **a new feature release about every six months**, and an LTS on a longer rhythm. That is why there are so many numbers. The numbers are a calendar, not a personality test.

### Three language waves (the part that matters)

```
  WAVE A — Java 5
  +---------------------------+
  | generics   List<User>     |
  | annotations  @Test        |
  | enhanced for  for (x : xs)|
  | enums                     |
  | autoboxing  int <-> Integer|
  +---------------------------+

  WAVE B — Java 8
  +---------------------------+
  | lambdas   x -> x * 2      |
  | streams   list.stream()   |
  | Optional                  |
  | java.time  (Date/Time)    |
  +---------------------------+

  WAVE C — Modern (learn when we meet them)
  +---------------------------+
  | records                   |
  | pattern matching          |
  | switch expressions        |
  | text blocks               |
  | sealed classes            |
  | virtual threads           |
  | compact source files      |
  +---------------------------+
```

Java 9's **modules** (Project Jigsaw) matter more for giant JDK internals and some libraries than for your first 200 test classes. **JShell** is a scratchpad: type Java without a full class if you want to experiment later.

---

## 5. Syntax / Concept

We show **shapes**, not mastery. If this looks like a foreign city, good: you now have a map. We will walk the streets in later parts.

### Java 5 shapes

```java
import java.util.ArrayList;
import java.util.List;

public class Java5Shapes {

    enum OrderStatus {
        NEW, PAID, SHIPPED
    }

    public static void main(String[] args) {
        List<String> users = new ArrayList<String>(); // Java 5: List of String, not "whatever"
        users.add("Amina");
        users.add("Ben");

        for (String user : users) { // enhanced for
            System.out.println(user);
        }

        OrderStatus status = OrderStatus.PAID; // enum
        int count = 3;
        Integer boxed = count; // autoboxing: primitive int to Integer object
        System.out.println(status + " count=" + boxed);
    }
}
```

Annotations look like `@Override` or, later, `@Test`. They are **labels the compiler and tools can read**.

### Java 8 shapes

```java
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Java8Shapes {

    public static void main(String[] args) {
        List<Integer> totals = List.of(10, 50, 80);

        int sum = totals.stream()
                .mapToInt(n -> n)   // lambda: n goes to n
                .sum();

        Optional<Integer> maybe = totals.stream()
                .filter(n -> n > 100)
                .findFirst();

        LocalDate today = LocalDate.now(); // modern Date/Time, not the old Date mess

        System.out.println("sum=" + sum + " empty?" + maybe.isEmpty() + " date=" + today);
    }
}
```

### Modern shapes (recognition only)

```java
public class ModernShapes {

    record User(String email, boolean active) {
    }

    public static void main(String[] args) {
        User user = new User("a@shop.com", true);

        String jsonLike = """
                {
                  "email": "a@shop.com"
                }
                """; // text block

        String label = switch (user.active()) {
            case true -> "can login";
            case false -> "blocked";
        }; // switch expression

        System.out.println(user.email() + " " + label);
        System.out.println(jsonLike);
    }
}
```

**Virtual threads** (Java 21): many lightweight threads, useful when lots of tests or servers wait on the network. You will not configure them in Chapter 6.

**Compact source files / simple `main`** (modern JDK, including 25): shorter starter programs. This course still teaches the classic `public class` + `public static void main` so you can read 95% of work code. We will mention the short form when we write Hello World.

**Do not memorize this section.** Skim it like a museum brochure. "Oh, records exist. Oh, Java 8 added streams." That is the win.

---

## 6. Simple Example

A tiny "which Java wave am I standing on?" program.

```java
public class JavaWaveCheck {

    public static void main(String[] args) {
        String spec = System.getProperty("java.specification.version");
        System.out.println("This JVM implements Java specification version: " + spec);
        System.out.println("This course expects 25 (an LTS).");
        System.out.println("If you see 8, 11, 17, or 21, you can still learn — but install 25 if you can.");
    }
}
```

If this prints `25`, you are on the subway stop this course built the station around.

---

## 7. Real-World Example

A banking codebase is 15 years old. That is normal.

```
  2009  Java 5-style enums for account type
  2016  Java 8 streams to filter transactions
  2022  move to LTS 17
  2026  move to LTS 21 or 25
```

They do **not** rewrite the whole bank every six months. They hop **LTS to LTS**.

E-commerce order status is a perfect **enum** (Java 5 idea) that still looks modern:

```java
public class OrderStatusDemo {

    enum Status {
        PLACED,
        PAID,
        PACKED,
        SHIPPED,
        CANCELLED
    }

    public static void main(String[] args) {
        Status status = Status.PAID;
        System.out.println("Order is " + status);

        if (status == Status.CANCELLED) {
            System.out.println("Do not ship.");
        } else {
            System.out.println("Fulfillment may continue.");
        }
    }
}
```

If you wrote status as the numbers `1,2,3,4`, every new teammate would guess wrong. Enums exist because Java 5 decided **named states are better than magic numbers**.

---

## 8. SDET Example

JUnit tests are annotated methods. That is the **Java 5 annotation wave** holding up **2026 automation**.

```java
public class FakeTestRunner {

    // In real JUnit you would write @Test on a method.
    // Here we simulate the idea: a label that means "this is a test."

    public static void main(String[] args) {
        String actualPageTitle = "Your cart";
        String expectedPageTitle = "Your cart";

        runTest("cart title", actualPageTitle.equals(expectedPageTitle));
    }

    static void runTest(String name, boolean pass) {
        if (pass) {
            System.out.println("PASS: " + name);
        } else {
            System.out.println("FAIL: " + name);
        }
    }
}
```

Java 8 shows up when test data is a list of users to try:

```java
import java.util.List;

public class StreamyTestData {

    public static void main(String[] args) {
        List<String> emails = List.of("good@shop.com", "bademail", "also@shop.com");

        long validLooking = emails.stream()
                .filter(e -> e.contains("@"))
                .count();

        System.out.println("Emails that look valid: " + validLooking);
    }
}
```

You do not need to master streams today. You need to know **why Java 8 is still in job descriptions**: a huge wave of library and test code was written in that style.

---

## 9. Break the Code

A student copies an old date example from a 2006 blog.

```java
import java.util.Date;

public class BrokenOldDateThinking {

    public static void main(String[] args) {
        Date now = new Date();
        // BUG of history: java.util.Date is awkward, mutable, and easy to misuse.
        // Beginners treat it as "the" date type because the internet is a museum.
        now.setTime(0);
        System.out.println("Accidentally mutated to: " + now);
    }
}
```

**Predict:** Why is mutating a date object a nasty surprise in tests?

Another break: assuming "Java 8 syntax" on a machine that is actually 7 (rare today, still a mental model): lambdas would not compile.

---

## 10. Debug

**Symptom:** Dates in tests flake, or two tests interfere, or the printed date is 1970.

**Historical cause:** old `Date` objects are **mutable**. One test changes a shared date, the next test sees the wreckage.

**Modern direction:** `java.time.LocalDate` / `LocalDateTime` (Java 8 wave) — values you treat as values.

```java
import java.time.LocalDate;

public class FixedModernDate {

    public static void main(String[] args) {
        LocalDate orderDate = LocalDate.of(2026, 9, 18);
        LocalDate shippedDate = orderDate.plusDays(2);

        System.out.println("Ordered: " + orderDate);
        System.out.println("Shipped: " + shippedDate);
        System.out.println("Original order date still: " + orderDate);
    }
}
```

**Version debugging:** if a feature "does not exist," ask "What JDK is this?" before asking "Is Java fake?"

```
  feature missing  →  check java -version
  CI cannot run    →  bytecode version vs JVM
  old tutorial     →  check the year of the blog
```

---

## 11. Student Exercise

In your notebook, draw three boxes: **Java 5**, **Java 8**, **Modern**. Put each item in a box:

- lambdas
- generics
- records
- annotations
- streams
- enums
- text blocks
- Optional
- enhanced for-loop
- virtual threads (just the name)

Then write one sentence: "Companies hop from LTS to LTS; this course uses 25."

---

## 12. Challenge

You join a team on **Java 11** with a plan to move to **25**.

Write a one-page "wave plan":

1. What you will **not** do (rewrite every file for fashion).
2. What you will learn first (the syntax you need for tests this month).
3. Which modern features you might adopt after the JDK upgrade (pick two and say why an SDET would care).
4. How you will confirm CI uses the same LTS.

No need for real migration commands. This is judgment practice.

---

## 13. Knowledge Check

1. Why does this course refuse to make you memorize every Java version?
2. Name the four LTS versions listed after Java 9 in this chapter.
3. Which version is the big "lambdas and streams" wave?
4. Which version is the big "generics, enums, annotations" wave?
5. What does LTS mean, in plain words?
6. What is JShell for?
7. True or false: you must master virtual threads before Hello World.
8. Why is `java.time` usually better than leftover `Date` code?
9. Why do job posts still say Java 8?
10. What is this course's baseline JDK?

---

## 14. Interview Question

**Question:** "Which Java version do you use, and what's the difference between Java 8 and newer Java?"

**Strong answer:**

> "I train on JDK 25 LTS. Java 8 was a major language wave — lambdas, streams, Optional, and java.time — and a lot of production code still looks like that. Newer LTS versions add things like records, better switch, text blocks, and virtual threads. Companies usually jump LTS to LTS rather than every six-month release. I learn features when the project needs them instead of reciting every version number."

That answer sounds like an adult.

---

## 15. Homework

1. Recite the subway stops from 1.0 to 25 using the table in Today's Goal — once out loud. Then stop. Do not drill nightly.
2. In your glossary, add: LTS, generics, lambda, stream, record (one line each, your words).
3. You have finished Part 2. Open Part 3 to see *how a program actually runs*.
4. Optional: when IntelliJ works, run `JavaWaveCheck`.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. Releases come often; skill is waves + looking things up, not trivia.
2. 11, 17, 21, 25.
3. Java 8.
4. Java 5.
5. Long-Term Support: a version maintained for years, favored in companies.
6. A REPL / scratchpad to try Java snippets.
7. False.
8. `java.time` types are clearer and not casually mutated like old `Date`.
9. It was a huge wave; many codebases and interviews still assume that style.
10. JDK 25 LTS.

</details>
