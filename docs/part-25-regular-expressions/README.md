# Part 25 — Regular Expressions

A **regular expression** (regex) is a tiny language for describing **text patterns**.

It is **not** Java. Java *hosts* regex with `String.matches`, `Pattern`, and `Matcher`.

```text
Regex language     \d{5}     five digits
Java string        "\\d{5}"  backslashes doubled in Java source
```

You will use regex for IDs, logs, emails, phones, dynamic UI text, and file names.

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 77](chapter-77-regular-expressions.md) | Pattern vs Java syntax, `\\d{5}`, testers' text |

## Prerequisite

`String` methods. Regex does not replace `equals` and `contains`. It is for **patterns**.

## Study Warning

Regex can become unreadable. If `startsWith` and `length() == 5` will do, use them. Interviewers still expect `\d` and a humble attitude toward emails (real email regex is a monster; do not claim yours validates the entire RFC).
