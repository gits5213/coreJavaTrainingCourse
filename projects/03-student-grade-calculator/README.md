# Project 3 — Student Grade Calculator

## Goal

Read an array of scores, compute the average with a loop, and assign a letter grade A–F.

## Concepts this practices

- Conditions (`if` / `else if`)
- Loops (`for-each`)
- Methods
- Arrays

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
Scores: 88, 92, 79, 95, 84
Average: 87.6
Letter grade: B
```

## What success looks like

The report prints every score, a correct average, and the letter that matches the scale below. Empty input is rejected.

| Average | Letter |
| --- | --- |
| 90–100 | A |
| 80–89 | B |
| 70–79 | C |
| 60–69 | D |
| below 60 | F |

## Stretch challenge

Support weighted scores (labs 40%, exams 60%) and print a failing list of any individual score below 60.
