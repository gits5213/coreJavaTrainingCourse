# Part 18 — File Handling

Tests do not live only in memory. They read **data files**, write **reports**, and load **config**.

This part teaches the modern Java file API:

```java
String text = Files.readString(Path.of("data.txt"));
Files.writeString(Path.of("report.txt"), text);
```

You will use `java.nio.file.Path` and `java.nio.file.Files`. You will **not** build paths by gluing `"C:\\"` or `"/Users/"` by hand as your first habit.

## Why Path, Not Raw Strings

```text
BAD  (OS-dependent in your head)
"C:\\tests\\data.txt"     Windows
"/Users/me/tests/data.txt"  macOS

GOOD
Path.of("tests", "data.txt")
```

`Path` is an object that represents a location. `Files` is the toolbox that reads and writes. The operating system still matters at runtime, but your **code** stays portable.

## Chapters in This Part

| Chapter | Topic | You will be able to... |
| --- | --- | --- |
| [Chapter 67](chapter-67-read-text-file.md) | Read a text file | `Files.readString(Path.of("data.txt"))` |
| [Chapter 68](chapter-68-write-file.md) | Write a text file | `Files.writeString` for reports |
| [Chapter 69](chapter-69-path.md) | `Path` | Join folders without OS-specific slashes as a lifestyle |

## Prerequisite

Part 17. Reading a missing file throws checked `IOException`. You will declare `throws` or catch and fail the test. Empty `catch (Exception e)` is still forbidden.

## SDET Connection

```text
testdata/users.csv          →  read
config/base-url.txt         →  read
target/reports/summary.txt  →  write
screenshots/                →  later, still Path
```

If the file is missing, the test failed. Do not invent empty users.

## Working Directory Warning

`Path.of("data.txt")` is relative to the **working directory** when the program runs (often the project root in IntelliJ). If you cannot find the file, print:

```java
System.out.println(Path.of("data.txt").toAbsolutePath());
```

That one line saves an evening.
