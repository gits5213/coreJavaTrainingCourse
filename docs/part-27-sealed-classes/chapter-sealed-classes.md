# Sealed Classes — A Closed Family of Test Results

## 1. Today's Goal

By the end of this lesson, you will explain **sealed** types: a type that lists **which** subtypes are allowed.

You will read:

```java
public sealed interface TestResult permits PassedResult, FailedResult {}

public record PassedResult(String testName) implements TestResult {}

public record FailedResult(String testName, String reason) implements TestResult {}
```

You will know **when to wait**: this is advanced modeling, not a beginner requirement. If inheritance is still foggy, park this chapter and return later.

## 2. Why It Matters

When a test can only **pass** or **fail** (for a given design), an open `interface TestResult` lets anyone add `SkippedResult`, `FlakyResult`, `MaybeResult` in another package. Sometimes you want that. Sometimes you want a **closed** world so `switch` can be exhaustive: the compiler nags if you forget `FailedResult`.

SDET frameworks have result types. Understanding sealed types helps you **read** modern Java (JDK 17+). Writing them on day 30 of learning Java is optional.

If you came here from Chapter 1: go back. This chapter assumes you chose to be here.

## 3. Real-Life Analogy

A club with a guest list.

```text
TestResult is sealed
permits: PassedResult, FailedResult

Nobody else may implement TestResult
```

A traffic light: only red, yellow, green. Not "blue" unless you change the permitted list.

A multiple-choice exam: answers A–D, not a fifth letter someone invented in another file.

**When the guest list is a burden:** a plugin system where teams add new result types. Then do **not** seal. Sealing is for closed domains.

## 4. Illustrated Explanation

```text
        TestResult  (sealed interface)
         /                    \
        /                      \
PassedResult                 FailedResult
(record)                     (record)

A third class SurpriseResult implements TestResult
        →  compile error unless added to permits
```

```text
Open interface
    any class anywhere can implement
    switch may need default
    new type can appear in a library you do not control

Sealed interface
    only permitted types
    switch can cover all cases
    adding a type is a deliberate change to this file
```

```text
when to wait
    │
    ├─ still learning class/interface  →  WAIT
    ├─ still learning records          →  WAIT
    ├─ building first Selenium tests   →  WAIT
    └─ designing a small closed model  →  this chapter
```

Non-sealed / final / sealed subtypes exist for deeper hierarchies. You may ignore them until you need a branch that can be extended.

## 5. Syntax / Concept

```java
public sealed interface TestResult permits PassedResult, FailedResult {}
```

Permitted types must be in the same file, same package, or (with modules) accessible as the language requires. For homework, **same package** is enough.

```java
public record PassedResult(String testName) implements TestResult {}

public record FailedResult(String testName, String reason) implements TestResult {}
```

A sealed **class** uses `extends` instead of `implements`. Same `permits` idea.

Switch (modern, exhaustive):

```java
String line = switch (result) {
    case PassedResult p -> "PASS " + p.testName();
    case FailedResult f -> "FAIL " + f.testName() + " " + f.reason();
};
```

If you add a permitted type later, this switch fails to compile until you handle it. That is the payoff.

Do not seal `User` or `LoginData` for fun. Seal when the set of subtypes is a **business rule**.

You cannot `permits` a class you do not control in another library arbitrarily without coordination.

## 6. Simple Example

Same package:

```java
public sealed interface TestResult permits PassedResult, FailedResult {}

public record PassedResult(String testName) implements TestResult {}

public record FailedResult(String testName, String reason) implements TestResult {}

public class SealedDemo {

    public static String describe(TestResult result) {
        if (result instanceof PassedResult p) {
            return "PASS " + p.testName();
        }
        if (result instanceof FailedResult f) {
            return "FAIL " + f.testName() + ": " + f.reason();
        }
        throw new IllegalStateException("unreachable if sealed is complete");
    }

    public static void main(String[] args) {
        TestResult a = new PassedResult("login");
        TestResult b = new FailedResult("checkout", "status 500");
        System.out.println(describe(a));
        System.out.println(describe(b));
    }
}
```

Expected:

```text
PASS login
FAIL checkout: status 500
```

If `instanceof` still feels new, that is a sign to wait.

## 7. Real-World Example

Shop payment result: `Paid`, `Declined`, `Pending` — a sealed interface `PaymentStatus`. Checkout `switch` must handle all three.

Bank: `Approved`, `Rejected` — maybe too small; an enum might be enough. **Enums** are the older closed set. Sealed types shine when each variant **carries different data** (`FailedResult` has `reason`, `PassedResult` does not).

If all variants are just names, prefer `enum`. Do not use sealed as fashion.

## 8. SDET Example

```java
public static void printReport(TestResult result) {
    switch (result) {
        case PassedResult p -> System.out.println("TEST PASSED: " + p.testName());
        case FailedResult f -> {
            System.out.println("TEST FAILED: " + f.testName());
            System.out.println("Reason: " + f.reason());
        }
    }
}
```

No default. Exhaustive.

A runner returns `TestResult` instead of boolean. Booleans cannot carry a reason. Sealed records can.

Still never: catch Exception empty in the runner to turn `FailedResult` into pass.

If this is your first month, **model pass/fail with a boolean plus a message string**. Come back.

## 9. Break the Code

```java
public class SurpriseResult implements TestResult {}
```

Does not compile (not permitted).

```java
public sealed interface TestResult permits PassedResult {}
public record FailedResult(...) implements TestResult {}
```

`FailedResult` not in `permits`.

Using sealed on day one of Java to avoid learning `if`. That is the pedagogical bug. Sealed does not replace basics.

Empty default in switch that ignores failed results: you defeated exhaustiveness. Do not.

## 10. Debug

"Cannot implement sealed": add to `permits` or stop implementing.

Permitted type in the wrong package: move it or adjust accessibility/modules.

`switch` not exhaustive: you missed a permitted type. Handle it. That is the compiler helping.

If IntelliJ language level is too old, sealed types will not parse. This course uses JDK 25.

```text
Is the domain really closed?
If not, do not seal.
If yes, permits list is documentation the compiler enforces.
```

## 11. Student Exercise

**If you are early in the course:** write one paragraph in your notes: "I will learn sealed types after interfaces and records. TestResult may only be PassedResult or FailedResult." Stop. Do the knowledge check lightly.

**If you are ready:** type the three types and `describe`. Print pass and fail.

## 12. Challenge

Ready students only: add `SkippedResult(String testName, String why)` to the permits list. Update `describe` / `switch`. Confirm a missing case is a compile error (comment a case, see the error, then restore).

Early students: skip. Write `boolean passed` + `String message` instead. That is honest.

## 13. Knowledge Check

1. What does `sealed` mean?
2. What does `permits` list?
3. Write the `TestResult` interface line from this chapter.
4. Why might `FailedResult` be a record with a `reason`?
5. When should you **wait** to learn this?
6. When is `enum` enough instead?
7. True or false: every interface should be sealed.
8. What is the switch payoff?
9. Should beginners start SDET with sealed types?
10. Can you hide a failed sealed result with empty catch Exception?

## 14. Interview Question

**Question:** What are sealed classes/interfaces? Should every project use them?

A strong answer:

> A sealed type lists the only allowed subtypes in a permits clause. For example a sealed interface TestResult can permit PassedResult and FailedResult so no surprise implementations exist. That helps exhaustive switch. I would not teach or use this early. Beginners need classes, interfaces, and honest test failures first. Enums are enough when variants have no extra data. I would not seal types just to look modern. I still never empty-catch exceptions to hide failures.

## 15. Homework

If waiting: add a dated note in your notebook "Return to Part 27 after records + interfaces are easy." Draw the wait flowchart from section 4.

If ready: implement TestResult as in the lesson and write one method that returns `TestResult` from `actual == expected`.

Do not commit to using sealed types in every homework after this. Use them when the family is closed and each variant has shape.

---

## Answer Key

1. The type restricts which subtypes may exist.
2. The allowed implementations/subclasses.
3. `public sealed interface TestResult permits PassedResult, FailedResult {}`
4. Failure carries extra information; pass might only need a name.
5. When OOP, interfaces, and records are not solid; when you are still in early Java.
6. When variants are names only, no different fields.
7. False.
8. Compiler requires all cases; new types break the build until handled.
9. No.
10. No. Never.
