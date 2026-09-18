# Project 6 — File Analyzer

## Goal

Read a text log of test names plus `PASS` / `FAIL`. Count lines, count failures, and collect unique status tags with a `Set`. Handle a missing file with a clear message.

## Concepts this practices

- Files (`Path`, `Files`)
- Exceptions (`IOException`, `NoSuchFileException`)
- Collections (`List`, `Set`)

## How to run

From this project directory (so the relative path to `src/main/resources` resolves):

```bash
mvn -q compile exec:java
```

Optional tests:

```bash
mvn test
```

To point at another file:

```bash
mvn -q compile exec:java -Dexec.args="path/to/other-log.txt"
```

## Expected output

```text
File: src/main/resources/sample-log.txt
Lines: 7
FAIL count: 3
Status tags: [FAIL, PASS]
Unique words: [FAIL, PASS, cartTest, checkoutTest, loginTest, logoutTest, paymentApiTest, profileTest, searchTest]
```

Word order inside a `Set` is not guaranteed. Matching counts and the two status tags is success.

## What success looks like

The analyzer uses `Path.of` and `Files.readAllLines`. A missing file prints a readable error instead of a stack-trace dump as the only output. `FAIL` lines are counted even if extra spaces appear.

## Stretch challenge

Group test names by status in a `Map<String, List<String>>` and write a summary file to `target/analysis-summary.txt`.
