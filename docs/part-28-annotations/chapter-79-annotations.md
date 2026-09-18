# Chapter 79 — Annotations

## 1. Today's Goal

By the end of this lesson, you will explain an annotation as **metadata**, not as the method body.

You will use `@Override` correctly, read `@Test` as a JUnit (or similar) marker, and see an **advanced** custom annotation `@SmokeTest` with `RetentionPolicy.RUNTIME`.

You will not confuse "there is an annotation" with "the test passed."

## 2. Why It Matters

SDET code is covered in annotations:

```java
@Test
@DisplayName("login with valid user")
@Tag("smoke")
void loginWorks() { ... }
```

If you think `@Test` is a Java keyword that runs methods, you cannot debug "why didn't my test run?" (wrong package, missing engine, method not public, etc. — framework rules).

`@Override` is the compiler's ally: you think you overrode `toString`, but you misspelled it. Without `@Override`, you accidentally added a new method.

## 3. Real-Life Analogy

Sticky notes on a folder.

```text
Folder      →  the method
Sticky note →  @Test "please run this in the test runner"
              @Override "this must match a parent method"
              @SmokeTest "this is a smoke test"
```

The sticky note is not the work inside the folder. Someone (compiler, JUnit, your scanner) must **read** the note.

A shipping sticker "FRAGILE" does not pack the box. Warehouse software looks for the sticker.

## 4. Illustrated Explanation

```text
            @Override
public String toString() { ... }

Compiler checks: is there a toString to override?
    Yes  →  OK
    No   →  compile error (you misspelled)
```

```text
@Test
void login() { assertions }

Java language: this is just a method
JUnit engine:  find methods with @Test, run them
```

```text
Custom annotation (advanced)

@Retention(RetentionPolicy.RUNTIME)  →  still visible when the program runs
@Target(ElementType.METHOD)          →  only on methods
public @interface SmokeTest {}

Without RUNTIME, reflection cannot see it later.
SOURCE retention is for compile-time tools only.
CLASS retention is in the .class file but not always for reflection.
```

```text
Retention
  SOURCE   compiler / javac tools
  CLASS    in bytecode, not necessarily runtime-visible
  RUNTIME  Class.getAnnotation works  ← frameworks, @SmokeTest
```

## 5. Syntax / Concept

`@Override` (you should use this whenever you override):

```java
@Override
public String toString() {
    return "User";
}
```

`@Test` — **not in the JDK as JUnit**. You import it from the test library:

```java
import org.junit.jupiter.api.Test;

public class LoginTest {
    @Test
    void validUserCanLogin() {
        // arrange, act, assert later in the course
    }
}
```

Until JUnit is on the classpath, treat this as the shape you will type. `@Test` is metadata. The method still needs assertions. An empty `@Test` that does nothing **passes**. That is a useless test, not a language feature.

**Advanced — custom marker:**

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SmokeTest {
}
```

Use:

```java
@SmokeTest
public static void loginSmoke() {
    System.out.println("smoke login");
}
```

Without a scanner (reflection, next part), `@SmokeTest` does **nothing**. Annotations are not runtime behavior unless something reads them.

You can add elements later: `@SmokeTest(priority = 1)`. Skip for now.

Built-in: `@Deprecated`, `@SuppressWarnings("unchecked")` — use `@SuppressWarnings` sparingly; it hides generics honesty.

## 6. Simple Example

```java
public class OverrideDemo {

    @Override
    public String toString() {
        return "OverrideDemo ok";
    }

    public static void main(String[] args) {
        System.out.println(new OverrideDemo());
    }
}
```

If you write `tostring()` lowercase with `@Override`, it **fails to compile**. That is success: the compiler caught the typo.

## 7. Real-World Example

Shop: `@Deprecated` on `oldCheckout()` so the compiler warns callers.

Bank: custom `@Audited` read by a framework to log methods. Still metadata plus a reader.

The annotation is the sticker. The auditor program is the reader.

## 8. SDET Example

JUnit shape:

```java
import org.junit.jupiter.api.Test;

public class StatusTest {

    @Test
    void status200IsPass() {
        int actual = 200;
        if (actual != 200) {
            throw new AssertionError("TEST FAILED");
        }
    }
}
```

Custom (advanced) — documentation even before a scanner exists:

```java
@SmokeTest
@Test
void loginPageLoads() { }
```

A future engine lists all `@SmokeTest` methods for a fast pipeline.

Empty `@Test` with empty catch inside: two ways to lie. The method "ran." Product still broken.

```java
@Test
void hidden() {
    try {
        throw new AssertionError("fail");
    } catch (Exception e) {
    }
}
```

`AssertionError` is an `Error`, not an `Exception` — this catch might not even swallow it. Catch `Throwable` empty would. Both are crimes. JUnit marks a thrown `AssertionError` as failure. Let it fly.

## 9. Break the Code

```java
@Override
public String tostring() { return "x"; } // compile error with @Override
```

Without `@Override`, it compiles and you did not override `toString`. Subtle bug.

```java
@Test
public static void test() {}  // JUnit 5 typically wants non-static instance methods
```

Framework rules ≠ Java rules. The annotation is present; the engine skips or errors.

Writing `@Test` without a test library: cannot resolve symbol.

Custom annotation without `@Retention(RUNTIME)` then wondering why reflection finds nothing. Advanced bug.

## 10. Debug

Test not running:

1. Is the class in the test source set?
2. Is the method annotated with the **library's** `@Test` (right import)?
3. Naming and modifiers per JUnit version.
4. Annotation does not run code by itself.

`@Override` error: you are not actually overriding. Check parent spelling and parameter types.

Debugger: breakpoints inside the method still work. The annotation does not change step-over.

```text
Who reads this annotation? Compiler, library, or nobody?
If nobody, it is a comment with extra @.
```

## 11. Student Exercise

Write a class that `@Override`s `toString`. Print it.

Intentionally misspell with `@Override` and read the compiler error. Then fix.

In notes, write `@Test` example (import line included) even if you cannot run JUnit yet.

## 12. Challenge

**Advanced:** declare `@SmokeTest` with `@Retention(RetentionPolicy.RUNTIME)` and `@Target(ElementType.METHOD)`.

Put it on two methods, one without. In `main`, print a message: "A scanner would look for @SmokeTest at runtime. We build that idea in the reflection chapter."

Do not fake a scanner with empty catch.

**Not ready for custom annotations:** skip code; write the retention diagram in notes.

## 13. Knowledge Check

1. What is an annotation?
2. What does `@Override` help catch?
3. Does `@Test` come from the JDK core as JUnit?
4. Does `@Test` by itself assert anything?
5. What is `@SmokeTest` in this chapter?
6. Why `RetentionPolicy.RUNTIME` for a custom test marker?
7. True or false: annotations always change runtime behavior.
8. Who reads `@Test`?
9. Should you `@SuppressWarnings` to ignore all problems?
10. Empty catch inside `@Test`: allowed?

## 14. Interview Question

**Question:** What are annotations in Java? How do `@Override` and `@Test` differ?

A strong answer:

> Annotations are metadata on code. @Override is a compiler check that I actually overrode a parent method. @Test is a library annotation, usually JUnit, that marks a method for the test engine to run. The Java language does not run tests because it saw @Test; the framework scans for it. Custom annotations like @SmokeTest need RetentionPolicy.RUNTIME if we look them up with reflection. An annotation is not a pass. I still assert, and I never empty-catch failures.

## 15. Homework

Add `@Override` to any `toString` you already wrote.

Read a JUnit `@Test` snippet online or in a later course part and label: annotation vs method body vs assertion.

If advanced: type the `@SmokeTest` annotation type into your project as a file. You will use it in the reflection chapter if you continue.

---

## Answer Key

1. Metadata for the compiler or tools, not the method's work itself.
2. Mistaken overrides (wrong name/signature).
3. No. It comes from a test library.
4. No.
5. A custom marker for smoke tests (advanced).
6. So it is visible to reflection at runtime.
7. False. Something must read them (or the compiler).
8. The test framework/engine.
9. No.
10. No.
