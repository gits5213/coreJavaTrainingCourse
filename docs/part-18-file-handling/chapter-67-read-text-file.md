# Chapter 67 — Read a Text File

## 1. Today's Goal

By the end of this lesson, you will read an entire text file into a `String` with the modern API:

```java
String text = Files.readString(Path.of("data.txt"));
```

You will print the text, talk about `IOException`, and treat a missing file as a failed test, not as "maybe empty."

## 2. Why It Matters

Hard-coded test users in `main` do not scale. Teams keep users, expected messages, and payloads in files.

If you cannot read a file, you cannot:

- load test data
- load environment config
- open a saved API response for comparison

Old tutorials start with `FileReader` loops. Those still exist. Your default as a new SDET should be `Files.readString` for whole text files that fit in memory (config, small JSON, small CSV).

## 3. Real-Life Analogy

A recipe card on the counter.

```text
Path.of("data.txt")  →  where the card is
Files.readString     →  reading every word into your mind (a String)
```

If the card is missing, you do not cook an imaginary cake and tell the guest it is fine. You stop and say the recipe is missing.

A letter in an envelope: the path is the address. `readString` is opening the envelope and reading the whole letter at once. A very long novel might need line-by-line reading later. Today we read short letters.

## 4. Illustrated Explanation

```text
disk
 ┌─────────────────┐
 │ data.txt        │
 │ john,tester     │
 └────────┬────────┘
          │ Files.readString(Path.of("data.txt"))
          ▼
     String text
     "john,tester\n"
          │
          ▼
     your assertions / printing
```

Missing file:

```text
Path points at nothing
        │
        ▼
 NoSuchFileException  (an IOException)
        │
        ▼
 catch and TEST FAILED
 or let it fail the test run
```

Working directory:

```text
IntelliJ runs your main
working directory often = project folder

project/
  data.txt          ← Path.of("data.txt") looks here
  src/main/java/...
```

If you put `data.txt` next to the `.java` file inside `src`, relative `data.txt` may **not** find it. Path is not "same folder as the source file" unless you make it so.

## 5. Syntax / Concept

Imports:

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
```

Read all text (UTF-8 by default in modern Java):

```java
String text = Files.readString(Path.of("data.txt"));
```

`Path.of("data.txt")` creates a path. `Files.readString` performs the read. They are partners.

The method throws **checked** `IOException`. Handle or declare.

Exists check (optional, still can race):

```java
Path path = Path.of("data.txt");
if (Files.notExists(path)) {
    throw new IllegalStateException("Missing test data: " + path.toAbsolutePath());
}
```

For huge files, do not slurp everything; use buffered line reading later. Test data files should be small on purpose.

`readString` includes newline characters as they exist in the file. `trim()` if you need to drop edges. Do not `trim()` if whitespace is the thing you are testing.

## 6. Simple Example

Create a file named `data.txt` in the **project working directory** with:

```text
hello from a file
```

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadTextFileDemo {

    public static void main(String[] args) throws IOException {
        String text = Files.readString(Path.of("data.txt"));
        System.out.println(text);
    }
}
```

Expected: the console shows `hello from a file` (and maybe a newline).

If you see `NoSuchFileException`, print the absolute path and move the file or fix the name.

## 7. Real-World Example

A shop greeting file `banner.txt`:

```text
Spring sale this week
```

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ShopBannerFile {

    public static void main(String[] args) {
        Path path = Path.of("banner.txt");
        try {
            String banner = Files.readString(path).trim();
            System.out.println("Homepage: " + banner);
        } catch (IOException e) {
            System.out.println("Cannot load banner from " + path.toAbsolutePath());
            System.out.println(e.getMessage());
        }
    }
}
```

A bank might read `holidays.txt` with one date per line. You would split the string. Same read, more processing.

## 8. SDET Example

`testdata/users.txt`:

```text
standardUser
lockedUser
admin
```

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadUsersFile {

    public static void main(String[] args) throws IOException {
        Path path = Path.of("testdata", "users.txt");
        String text = Files.readString(path);
        String[] users = text.split("\\R"); // any newline style

        for (String user : users) {
            if (user.isBlank()) {
                continue;
            }
            System.out.println("Would log in as: " + user.trim());
        }
    }
}
```

If the file is missing, do **not** catch empty and test with a fake user. CI must go red.

```text
BAD
catch (Exception e) { users = new String[] { "john" }; }

GOOD
Missing testdata/users.txt → TEST FAILED, print absolute path
```

## 9. Break the Code

```java
String text = Files.readString(Path.of("data.txt"));
```

Bugs:

1. File is named `Data.txt` on a case-sensitive OS (Linux CI). Works on your Mac maybe; fails in CI.
2. File sits in `src`, program looks at project root.
3. No `throws` / `catch` — does not compile.
4. Catch `Exception` and print TEST PASSED.

```java
try {
    System.out.println(Files.readString(Path.of("data.txt")));
} catch (Exception e) {
    System.out.println("TEST PASSED");
}
```

That prints PASSED when the file is missing. Criminal.

## 10. Debug

`NoSuchFileException`:

1. Print `Path.of("data.txt").toAbsolutePath()`.
2. In IntelliJ: Run → Edit Configurations → Working directory.
3. Confirm the real file name, including `.txt`.
4. Confirm you are not looking at an old `out/` folder.

`IOException` about permissions: the file exists but cannot be read. Rare on your laptop homework; common on locked CI agents.

Debugger: after `readString`, inspect `text`. Look for `\r\n` vs `\n`. Windows files in Git can surprise `equals` later. `split("\\R")` is your friend.

```text
problem → understand (where is the file?) → print absolute path
        → fix location or Path → run → still fail? debug
```

## 11. Student Exercise

Create `message.txt` with two lines.

Write `ReadExercise` that reads with `Files.readString(Path.of("message.txt"))` and prints:

- the raw string
- the length
- each line after split

Use `throws IOException` on `main`.

## 12. Challenge

Write `readRequired(Path path)` that:

- if `Files.notExists(path)`, throw `IllegalStateException` including `toAbsolutePath()`
- otherwise return `Files.readString(path)` and declare `throws IOException`

`main` catches `IllegalStateException` and `IOException` separately, both as TEST FAILED with different labels: `MISSING` vs `READ ERROR`.

Point it at a file you have and a file you do not.

## 13. Knowledge Check

1. What does `Files.readString` return?
2. How do you point at `data.txt` with `Path`?
3. Why is this a checked-exception situation?
4. Does `Path.of("data.txt")` mean "next to the `.java` file"?
5. What exception is typical when the file is missing?
6. Why print `toAbsolutePath()` when debugging?
7. Should a missing test-data file become an empty user list?
8. What does `split("\\R")` help with?
9. True or false: `Files.readString` is a good default for small text files.
10. Recite the empty-catch rule in one sentence.

## 14. Interview Question

**Question:** How do you read a text file in modern Java?

A strong answer:

> I use Files.readString(Path.of("data.txt")) or Path.of with several names for folders. It returns the whole file as a String and throws IOException, which is checked, so I catch at a boundary or declare throws. A missing file is NoSuchFileException. In SDET work I fail the test when data is missing. I do not empty-catch Exception. I debug location with toAbsolutePath because relative paths use the working directory, not the source folder.

## 15. Homework

Create `testdata/expected-welcome.txt` with a welcome sentence.

Write a program that reads it and compares to a hard-coded `actual` string (simulate UI text). Print TEST PASSED or TEST FAILED with expected and actual.

If the file is missing, TEST FAILED — missing expected file — plus absolute path.

Do not empty-catch.

---

## Answer Key

1. A `String` of the whole file contents.
2. `Path.of("data.txt")`
3. `readString` throws `IOException`.
4. No. It is relative to the working directory.
5. `NoSuchFileException` (an `IOException`).
6. So you see where the program is actually looking.
7. No. That hides missing data.
8. Splitting lines across Windows and Unix newline styles.
9. True.
10. Never catch Exception empty to hide a test failure.
