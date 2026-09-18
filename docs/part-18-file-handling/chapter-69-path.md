# Chapter 69 — Path

## 1. Today's Goal

By the end of this lesson, you will build file locations with `java.nio.file.Path` instead of gluing operating-system slashes.

You will prefer:

```java
Path.of("testdata", "users", "admin.json")
```

over:

```java
String p = "C:\\testdata\\users\\admin.json";
```

You will inspect `toAbsolutePath()`, `getParent()`, `getFileName()`, and `resolve`.

## 2. Why It Matters

SDET code runs on:

- your Windows laptop
- a teammate's Mac
- Linux CI

Backslash paths copied from your PC break Linux. Forward slash often works in Java on Windows, but **string guessing** is still a smell. `Path.of` names each folder as a separate argument. Java inserts the correct separator.

Wrong paths are one of the top "works on my machine" failures in automation.

## 3. Real-Life Analogy

An address as fields, not as one messy sentence.

```text
Country, city, street, number
    vs
"something I copied from a shipping label in another country"
```

`Path.of("testdata", "users", "admin.json")` is the fields.

A subway map: you name stops in order. You do not tattoo one city's street syntax onto every city.

## 4. Illustrated Explanation

```text
Path.of("testdata", "users", "admin.json")

testdata  /  users  /  admin.json     macOS, Linux
testdata  \  users  \  admin.json     Windows (Java will render this)

Same Path idea. Different separator when printed.
```

Relative vs absolute:

```text
Path.of("data.txt")
    relative to working directory

Path.of("/tmp/data.txt")          Unix-style absolute
Path.of("C:", "temp", "data.txt")  still better as pieces, or Path.of("C:\\temp\\data.txt") if you must
```

Prefer relative paths from the project, plus `toAbsolutePath()` for logs.

```text
working directory
        │
        ├── testdata/
        │      └── users.txt
        └── target/
               └── reports/
                      └── summary.txt

Path.of("testdata", "users.txt")
Path.of("target", "reports", "summary.txt")
```

`resolve` joins:

```text
Path folder = Path.of("testdata");
Path file   = folder.resolve("users.txt");
// testdata/users.txt
```

## 5. Syntax / Concept

```java
import java.nio.file.Path;
```

```java
Path relative = Path.of("testdata", "users.txt");
Path abs = relative.toAbsolutePath();
Path parent = relative.getParent();       // testdata
Path name = relative.getFileName();       // users.txt
Path child = Path.of("testdata").resolve("users.txt");
```

`Path.of` with one string still parses separators in that string. Prefer multiple names when you know the structure.

```java
Path.of("testdata/users.txt");      // works often, less explicit
Path.of("testdata", "users.txt");   // clearer
```

Never do this as your architecture:

```java
String path = os.equals("windows") ? "C:\\data\\a.txt" : "/Users/me/data/a.txt";
```

That hard-codes **your** machines. CI is not your house.

`Path` is not the file contents. It is the location. `Files` uses the location.

Compare paths with `equals` after `normalize()` if you have `..` segments:

```java
Path.of("testdata", ".", "users.txt").normalize();
```

## 6. Simple Example

```java
import java.nio.file.Path;

public class PathDemo {

    public static void main(String[] args) {
        Path path = Path.of("testdata", "users.txt");
        System.out.println("Path: " + path);
        System.out.println("Absolute: " + path.toAbsolutePath());
        System.out.println("Parent: " + path.getParent());
        System.out.println("Name: " + path.getFileName());
    }
}
```

Sample output (macOS-like):

```text
Path: testdata/users.txt
Absolute: /Users/you/project/testdata/users.txt
Parent: testdata
Name: users.txt
```

On Windows the separators look different. The program is the same.

## 7. Real-World Example

Store receipts by day:

```java
Path receipt = Path.of("receipts", "2026", "09", "18.txt");
```

Bank export:

```java
Path exportDir = Path.of("exports", "qa");
Path csv = exportDir.resolve("balances.csv");
```

The shop does not embed `C:\Users\Owner\Documents`. The server has a configured base directory, then `resolve`.

## 8. SDET Example

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestDataPath {

    public static Path userFile(String username) {
        return Path.of("testdata", "users", username + ".json");
    }

    public static void main(String[] args) throws IOException {
        Path path = userFile("admin");
        System.out.println("Looking for " + path.toAbsolutePath());
        if (Files.notExists(path)) {
            System.out.println("TEST FAILED — missing " + path);
            return;
        }
        System.out.println(Files.readString(path));
    }
}
```

Screenshot path later in Selenium:

```java
Path.of("target", "screenshots", testName + ".png")
```

Same idea. Folders as names. No OS hobby paths.

## 9. Break the Code

```java
String path = "C:\\testdata\\users.txt";
Files.readString(Path.of(path));
```

Works on one Windows PC. Fails on Linux CI (`NoSuchFileException` or invalid path).

Another bug: mixing separators and extra spaces.

```java
Path.of("testdata ", "users.txt"); // folder named "testdata " with a space
```

Another: resolving an **absolute** path onto another, which ignores the base (Java `resolve` rules). If the second path is absolute, it wins. Beginners concatenate strings to "fix" it and make a nonsense location.

```java
Path.of("testdata").resolve("/users.txt");
// on Unix this may be absolute /users.txt, not testdata/users.txt
```

Use `Path.of("testdata", "users.txt")` or `resolve("users.txt")` without a leading slash.

## 10. Debug

Wrong file:

1. Print the `Path`.
2. Print `toAbsolutePath().normalize()`.
3. List the real folders on disk.
4. Check for leading slashes in `resolve`.
5. Check working directory in the Run configuration.

IntelliJ: the Path in Variables is inspectable. Expand it.

```text
Relative path  →  depends on where the JVM was started
Absolute path  →  complete location
```

If CI and laptop disagree, they have different working directories or the files were never committed. `testdata/` belongs in git (without secrets).

## 11. Student Exercise

Print four paths:

1. `Path.of("data.txt")`
2. `Path.of("testdata", "data.txt")`
3. that path's `toAbsolutePath()`
4. `Path.of("target", "reports").resolve("summary.txt")`

Label each print. No need to create files unless you want to.

## 12. Challenge

Write `artifactPath(String testName, String extension)` returning `Path.of("target", "artifacts", testName + "." + extension)`.

Write `requireFile(Path path)` that throws `IllegalStateException` with absolute path if missing.

Create directories, write `"ok"` to `artifactPath("login", "txt")` using `Files.writeString`, then `requireFile` it, then read and print.

Use only `Path.of` / `resolve`, no `C:` or `/Users` literals.

## 13. Knowledge Check

1. What is a `Path`?
2. Why prefer `Path.of("a", "b")` over `"a\\b"`?
3. What does `toAbsolutePath()` show?
4. What does `resolve("users.txt")` do on a folder path?
5. Does a `Path` object automatically create a file?
6. Why do Windows-only path strings fail Linux CI?
7. What is `getFileName()` for `testdata/users.txt`?
8. True or false: relative `Path.of("data.txt")` is relative to the `.java` source file.
9. Why might `resolve("/x")` surprise you on Unix?
10. Should test data files be committed (without secrets)?

## 14. Interview Question

**Question:** How do you handle file paths in Java so tests run on every OS?

A strong answer:

> I use java.nio.file.Path and Path.of with one argument per folder or file name, then Files to read or write. I avoid hard-coded C:\\ paths. Relative paths are against the working directory, so I log toAbsolutePath when debugging. resolve joins a child name. Testers keep testdata in the repo and write artifacts under target. OS-specific strings are a common source of CI-only failures.

## 15. Homework

Refactor any earlier file exercise to use `Path.of("testdata", ...)` and `Path.of("target", "reports", ...)`.

Create `testdata/readme.txt` with one sentence. Read it. Write `target/reports/path-homework.txt` containing the absolute path of the readme and the text.

Notes: write "Path is a location. Files does the work."

You have finished Part 18 when you can read, write, and join paths without an OS-specific string as your default design.

---

## Answer Key

1. An object representing a file system location.
2. Java supplies the correct separator; the code stays portable.
3. The complete location from the filesystem root, based on working directory for relative paths.
4. Joins that file name under the folder.
5. No.
6. Those folders and separators do not exist on Linux.
7. `users.txt`
8. False. Working directory.
9. A leading `/` can make an absolute path and ignore the base.
10. Yes, without secrets.
