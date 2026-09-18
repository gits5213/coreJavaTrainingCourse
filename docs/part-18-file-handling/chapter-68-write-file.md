# Chapter 68 — Write a File

## 1. Today's Goal

By the end of this lesson, you will write text to a file with:

```java
Files.writeString(Path.of("report.txt"), "TEST PASSED\n");
```

You will understand overwrite vs append, and why SDET jobs write reports, logs, and saved responses.

## 2. Why It Matters

A test that only prints to the console loses history when the terminal scrolls. Teams need **artifacts**:

- summary reports
- failed response bodies
- lists of slow tests

Writing files is how evidence survives after CI finishes.

If you cannot write, you also cannot create a tiny fixture file from a program (less common than reading, still useful).

## 3. Real-Life Analogy

Writing a lab notebook.

```text
Path            →  which notebook page
writeString     →  putting words on the page
overwrite       →  erasing the page and writing again
append          →  adding a new line at the bottom
```

A receipt printer: each checkout writes a receipt. If the printer overwrites the same one-inch paper, you lose history. Reports usually create a file per run, or append to a log.

## 4. Illustrated Explanation

```text
String report = "Passed: 3\nFailed: 1\n"
        │
        │  Files.writeString(path, report)
        ▼
 ┌──────────────────┐
 │ report.txt       │
 │ Passed: 3        │
 │ Failed: 1        │
 └──────────────────┘
```

Default write:

```text
If file exists → replace entire contents
If not         → create file
```

Append:

```text
old lines stay
new text added at end
```

```text
try {
    write report
} catch (IOException e) {
    TEST FAILED — cannot write artifact
} finally {
    // writing is not "the test passed"
}
```

Parent folders: `Files.writeString(Path.of("target", "reports", "summary.txt"), text)` will **not** create `target/reports` for you unless you create directories first. Missing parent → `IOException`.

## 5. Syntax / Concept

```java
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
```

Overwrite / create:

```java
Files.writeString(Path.of("report.txt"), "hello\n");
```

Append:

```java
Files.writeString(
        Path.of("report.txt"),
        "another line\n",
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
);
```

Create parent directories:

```java
Path path = Path.of("target", "reports", "summary.txt");
Files.createDirectories(path.getParent());
Files.writeString(path, "ok\n");
```

`writeString` throws checked `IOException` (disk full, permission, missing parent).

Charset: default UTF-8 is what you want for JSON and reports.

Do not write secrets (real passwords) into reports that get uploaded to chat. Write `"********"` or a test user id.

## 6. Simple Example

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WriteFileDemo {

    public static void main(String[] args) throws IOException {
        Path path = Path.of("report.txt");
        Files.writeString(path, "hello report\n");
        String roundTrip = Files.readString(path);
        System.out.println(roundTrip);
        System.out.println("Wrote to " + path.toAbsolutePath());
    }
}
```

Expected: console shows `hello report`, and a `report.txt` appears in the working directory.

Run twice. The file still contains one hello, not two — overwrite.

## 7. Real-World Example

A shop writes yesterday's order count. A bank writes a settlement line.

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DailyTotalFile {

    public static void main(String[] args) throws IOException {
        int orders = 17;
        String line = "orders=" + orders + "\n";
        Files.writeString(Path.of("daily-total.txt"), line);
        System.out.println("Saved daily total");
    }
}
```

If accounting **appends** every hour, they need `APPEND`. If they replace the official daily number, overwrite is correct. Choose with intention.

## 8. SDET Example

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestReportWriter {

    public static void writeSummary(int passed, int failed) throws IOException {
        Path path = Path.of("target", "reports", "summary.txt");
        Files.createDirectories(path.getParent());
        String body = "Passed: " + passed + "\nFailed: " + failed + "\n";
        Files.writeString(path, body);
        System.out.println("Report: " + path.toAbsolutePath());
    }

    public static void main(String[] args) throws IOException {
        int actual = 200;
        int expected = 200;
        int passed = 0;
        int failed = 0;
        if (actual == expected) {
            System.out.println("TEST PASSED");
            passed++;
        } else {
            System.out.println("TEST FAILED");
            failed++;
        }
        writeSummary(passed, failed);
    }
}
```

Save a failing API body:

```java
Files.writeString(Path.of("target", "failures", "create-user.json"), rawJson);
```

That artifact is gold in code review: "here is what the server returned."

Never:

```java
} catch (Exception e) {
    // skip report
}
```

If the test failed **and** the report failed, you need both signals.

## 9. Break the Code

```java
Files.writeString(Path.of("target", "reports", "summary.txt"), "ok");
```

Without `createDirectories`, this often throws because `target/reports` does not exist.

Second bug: appending by writing the whole file in a loop **without** append option — you only keep the last write.

Third: writing then not flushing because you used a different older API and closed wrong. `writeString` completes the write. Do not mix random `FileWriter` snippets from the internet without closing them. Prefer `Files.writeString`.

Fourth: catching `IOException` and printing TEST PASSED because "at least we tried."

## 10. Debug

Cannot see the file:

1. Print `toAbsolutePath()`.
2. Refresh the IntelliJ project view.
3. Check working directory.

`NoSuchFileException` on write: parent directory missing. `createDirectories`.

`AccessDeniedException`: file open in another program, or CI workspace is read-only for that folder. Write under `target/` or a reports folder you own.

Round-trip debug:

```java
Files.writeString(path, content);
System.out.println(Files.readString(path).equals(content));
```

If false, you have newline or charset surprises.

## 11. Student Exercise

Write `WriteExercise` that writes your name and today's study topic to `notes.txt`, then reads it back and prints it.

Use `throws IOException`.

## 12. Challenge

Simulate three tests with known pass/fail. For each failure, **append** a line to `target/reports/failures.txt` with the test name. Overwrite `target/reports/summary.txt` once at the end with counts.

Create directories. Print both absolute paths.

If writing throws, catch `IOException` and print TEST FAILED — report not written. Do not hide the original test results; print those first.

## 13. Knowledge Check

1. What does `Files.writeString(path, text)` do to an existing file by default?
2. How do you append?
3. Why call `Files.createDirectories(path.getParent())`?
4. Is `writeString` a checked-exception API?
5. Why do SDET pipelines write report files?
6. Should reports contain real production passwords?
7. True or false: successful write means the product under test passed.
8. What should you print when you cannot find the file you just wrote?
9. What option constants are used for append?
10. Empty catch on write failure: allowed?

## 14. Interview Question

**Question:** How do you write a report file in Java, and what mistakes do testers make?

A strong answer:

> Files.writeString(Path.of("report.txt"), content) creates or overwrites a UTF-8 text file. For logs I append with StandardOpenOption.CREATE and APPEND. Parent folders must exist; I use Files.createDirectories. writeString throws IOException. Testers should write summaries and failed payloads as artifacts. A write is not a test pass. Missing parent directories and wrong working directory are the usual bugs. I never empty-catch IOException.

## 15. Homework

Read `testdata/expected-welcome.txt` from the previous homework (or create it). Compare to a simulated actual string. Write `target/reports/welcome-check.txt` with either `PASSED` or `FAILED` and both strings.

Create directories. Print absolute path.

If the expected file is missing, do not write PASSED. Fail loudly.

---

## Answer Key

1. Replaces the entire contents (creates if needed).
2. `StandardOpenOption.CREATE` and `APPEND`.
3. `writeString` does not create missing parent folders.
4. Yes. `IOException`.
5. So evidence remains after the console is gone.
6. No.
7. False. Writing is I/O, not an assertion.
8. `toAbsolutePath()` and working directory.
9. `StandardOpenOption.CREATE`, `StandardOpenOption.APPEND`.
10. No.
