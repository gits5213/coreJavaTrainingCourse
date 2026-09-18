# Project 7 — Test Result Analyzer

## Goal

Model HTTP-style test results as a Java record. Use streams to filter non-200 statuses, map remaining names, and reduce durations into a total.

## Concepts this practices

- Records
- Collections (`List`)
- Streams (`filter`, `map`, `reduce`)

This demo treats **any status other than 200** as a failure, including `201`. That is intentional: it matches a strict "exactly 200" check you will see in API tests.

## How to run

From this project directory:

```bash
mvn -q compile exec:java
```

Optional tests:

```bash
mvn test
```

## Expected output

```text
Failed names: [createUser, deleteUser, login]
Total duration ms: 285
Failure count: 3
```

## What success looks like

`TestResult` is a record. Failure names come from `.filter(status != 200).map(name)`. Total duration comes from `.mapToLong(...).reduce(0L, Long::sum)` (or an equivalent reduce). You can explain why 201 is in the failed list.

## Stretch challenge

Print the slowest result with `max(Comparator.comparingLong(TestResult::durationMs))` and group results by status code with `Collectors.groupingBy`.
