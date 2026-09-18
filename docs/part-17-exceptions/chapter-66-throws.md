# Chapter 66 — throws

## 1. Today's Goal

By the end of this lesson, you will read and write a method that **declares** a checked exception with `throws`.

```java
public static String readTitle() throws IOException {
    return Files.readString(Path.of("title.txt"));
}
```

You will connect this to Part 18 (files). You will not confuse `throws` (declaration) with `throw` (action). You will know why `parseInt` does **not** need `throws NumberFormatException` on the signature: that exception is unchecked.

## 2. Why It Matters

Checked exceptions are Java's compiler saying:

> This can go wrong in a way the caller must acknowledge.

File missing, network hiccup, database closed — classic checked cases. If you call a method that `throws IOException`, you must **catch** it or **declare** `throws IOException` yourself. The compiler will not compile "hope."

SDET code reads config files and JSON. Those APIs use checked `IOException`. You will see `throws IOException` on `main` in early exercises. Later you will catch at a sensible boundary (the test, the report writer) and fail clearly.

```text
Compiler: handle or declare
You:      do not empty-catch just to satisfy the compiler
```

## 3. Real-Life Analogy

A waiver on a field trip.

```text
This hike may include: rain, mud, closed trail (IOException)

Sign the waiver (throws on your method)
    or
Bring a rain plan (catch)
```

You cannot leave the bus unsigned. The compiler is the teacher with the clipboard.

`throw` is actually stepping in a puddle. `throws` is the waiver that says puddles are possible.

A restaurant menu that says "may contain nuts" is a declaration. Serving nuts is the event.

## 4. Illustrated Explanation

```text
Files.readString(path)   throws IOException   (checked)
        │
        ▼
your method readTitle()
        │
        ├─ catch (IOException e) { fail the test / message }
        │
        └─ declare  readTitle() throws IOException
                    │
                    ▼
              caller of readTitle must also
              catch or declare
                    │
                    ▼
              often main(String[] args) throws IOException
              in early student programs
```

```text
Unchecked (RuntimeException)
    Integer.parseInt
    no throws required on YOUR method
    still can crash at runtime

Checked (Exception that is not RuntimeException)
    Files.readString
    compiler requires catch or throws
```

Hot potato:

```text
  method A throws IOException
       │
       ▼
  method B throws IOException
       │
       ▼
  main throws IOException
       │
       ▼
  if it actually happens: JVM prints stack trace, program stops
```

Declaring is not catching. Declaring **passes the potato**.

## 5. Syntax / Concept

```java
public static String readTitle() throws IOException {
    return Files.readString(Path.of("title.txt"));
}
```

Multiple types:

```java
public static void load() throws IOException, InterruptedException {
    // ...
}
```

`main` can declare too:

```java
public static void main(String[] args) throws IOException {
    String text = Files.readString(Path.of("data.txt"));
    System.out.println(text);
}
```

That is honest for a tiny demo. In a test suite, prefer catching at the test boundary and marking the test failed with a clear message.

Remember:

```text
throw   →  statement inside the body
throws  →  clause on the method signature
```

You do **not** write:

```java
public static int parse(String s) throws NumberFormatException { // legal but pointless noise
    return Integer.parseInt(s);
}
```

Unchecked exceptions may be listed; teams usually omit them. List checked ones you do not catch.

If you catch a checked exception inside the method, you do **not** have to declare it — unless you rethrow it.

```java
public static String readOrEmpty() {
    try {
        return Files.readString(Path.of("data.txt"));
    } catch (IOException e) {
        System.out.println("Missing file: " + e.getMessage());
        return "";
    }
}
```

For SDET: returning `""` can hide a missing config. Often better to fail. Empty file result vs missing file are different bugs.

## 6. Simple Example

This example uses the modern file API you will live in next part. Create a file `hello.txt` next to your project working directory, or use an absolute path while learning — Chapter 69 will teach `Path` properly.

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ThrowsDemo {

    public static String readHello() throws IOException {
        return Files.readString(Path.of("hello.txt"));
    }

    public static void main(String[] args) throws IOException {
        System.out.println(readHello());
    }
}
```

If `hello.txt` exists, you see its text. If not, `NoSuchFileException` (a kind of `IOException`) crashes `main`. The compiler was satisfied because you declared `throws`.

Alternative: catch in `main` and print TEST FAILED without empty body.

## 7. Real-World Example

A shop loads a banner from a file. A bank loads a holiday calendar.

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StoreBanner {

    public static String loadBanner() throws IOException {
        return Files.readString(Path.of("banner.txt")).trim();
    }

    public static void main(String[] args) {
        try {
            System.out.println(loadBanner());
        } catch (IOException e) {
            System.out.println("Cannot open banner.txt");
            System.out.println(e.getMessage());
        }
    }
}
```

`loadBanner` declares. `main` catches. The store homepage can show a fallback message. It must not pretend the special banner loaded if the file is missing.

## 8. SDET Example

Config for base URL:

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BaseUrlConfig {

    public static String readBaseUrl() throws IOException {
        String raw = Files.readString(Path.of("config/base-url.txt")).trim();
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("base-url.txt is empty");
        }
        return raw;
    }

    public static void main(String[] args) {
        try {
            String url = readBaseUrl();
            System.out.println("Tests will hit: " + url);
        } catch (IOException e) {
            System.out.println("TEST FAILED — cannot read base-url.txt");
            e.printStackTrace();
        }
    }
}
```

Notice two different problems:

- File missing → checked `IOException` → declared, then caught in `main` as TEST FAILED.
- File empty → unchecked `IllegalArgumentException` → not declared, still a failure if it escapes.

**Illegal:**

```java
public static String readBaseUrl() {
    try {
        return Files.readString(Path.of("config/base-url.txt"));
    } catch (Exception e) {
        return "http://localhost"; // hidden failure, wrong environment, false greens
    }
}
```

Never "fix" a missing config by inventing a URL unless that is an **explicit**, documented default — and even then, log loudly.

## 9. Break the Code

```java
import java.nio.file.Files;
import java.nio.file.Path;

public class BrokenThrows {

    public static String read() {
        return Files.readString(Path.of("data.txt"));
    }

    public static void main(String[] args) {
        System.out.println(read());
    }
}
```

This does **not compile**. `Files.readString` throws checked `IOException`. `read()` neither catches nor declares `throws IOException`.

"Fix" that is still a product bug:

```java
public static String read() {
    try {
        return Files.readString(Path.of("data.txt"));
    } catch (Exception e) {
        return null;
    }
}
```

Compiles. Lies. Next line gets `NullPointerException` far from the file problem.

Wrong keyword:

```java
public static String read() throw IOException { // should be throws
```

## 10. Debug

Compiler error `unreported exception IOException` means:

```text
You called a method with a checked exception.
This method must catch it or declare throws.
```

Do not add `throws Exception` on every method as a lifestyle. Declare the specific type, or catch at a boundary.

If it compiles with `throws` but CI still dies:

1. The file path is wrong (working directory ≠ where you think).
2. Print `Path.of("data.txt").toAbsolutePath()`.
3. Catch in the test, print TEST FAILED, include the absolute path.

```text
Compiles  ≠  file exists
throws    ≠  the exception already happened
```

## 11. Student Exercise

Create `hello.txt` with one line of text.

Write `ThrowsExercise.readHello()` that returns `Files.readString(Path.of("hello.txt"))` and declares `throws IOException`.

`main` should declare `throws IOException` and print the file.

Then make a second class (or second `main` path) that **catches** `IOException` and prints a friendly TEST FAILED if you rename the file.

## 12. Challenge

Write `readFirstLine(Path path) throws IOException`.

Use `Files.readString`, then take the first line (split on `\n` or use a `BufferedReader` if you already peeked at Part 18).

If the file is empty after trim, `throw new IllegalArgumentException("empty file: " + path)`.

`main` tests:

1. A good file.
2. A missing file — catch `IOException`, TEST FAILED.
3. An empty file — catch `IllegalArgumentException`, TEST FAILED.

Do not catch `Exception` empty. You may use two catch blocks.

## 13. Knowledge Check

1. What does `throws IOException` on a method mean?
2. How is `throws` different from `throw`?
3. Why does `Integer.parseInt` not force you to write `throws NumberFormatException`?
4. If `A()` throws `IOException` and `B()` calls `A()`, what must `B` do?
5. Does declaring `throws` catch the exception?
6. What compiler message appears if you ignore a checked exception?
7. Is `main(...) throws IOException` legal?
8. Why is catching `Exception` and returning a fake URL dangerous in SDET config?
9. True or false: listing unchecked exceptions on `throws` is required.
10. Where should a test suite usually convert `IOException` into a failed test?

## 14. Interview Question

**Question:** Explain `throw` vs `throws`, and checked vs unchecked.

A strong answer:

> throw is a statement that actually throws an exception object. throws is a method clause that declares which checked exceptions the method may pass to its caller. Checked exceptions must be caught or declared; IOException from file APIs is the usual example. Unchecked exceptions extend RuntimeException; NumberFormatException from parseInt is unchecked, so the compiler does not force throws. In tests I declare or catch checked exceptions at a boundary and I never empty-catch Exception to keep CI green.

## 15. Homework

Write two methods:

- `readRequired(Path path) throws IOException` — no catch inside.
- `readRequiredOrFail(Path path)` — catch `IOException`, print TEST FAILED with absolute path, then `throw new IllegalStateException("missing test data", e)` so the cause is chained.

Call both from `main` on a file you create and on a file you do not create (comment one call so the program can finish, or catch in `main`).

Notes: one paragraph on why chained exceptions (`new IllegalStateException("...", e)`) help debugging.

You have finished Part 17 when you can explain exception, try/catch, finally, throw, throws, checked vs unchecked, and the empty-catch rule without looking.

---

## Answer Key

1. This method might pass that checked exception to its caller; callers must handle or declare.
2. `throw` acts; `throws` declares.
3. `NumberFormatException` is unchecked.
4. Catch `IOException` or declare `throws IOException`.
5. No. It only passes the requirement upward.
6. Unreported exception ... must be caught or declared to be thrown.
7. Yes.
8. Tests hit the wrong system and can pass for the wrong reasons.
9. False. Not required.
10. At the test boundary: log, fail the test, keep the cause. Never empty catch.
