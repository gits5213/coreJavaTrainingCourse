# Part 16 — Enums

An **enum** is a type with a **fixed set of named values**.

```text
BAD
String browser = "Chorme";   // typo compiles, fails later

GOOD
BrowserType browser = BrowserType.CHROME;
```

You will use enums for browsers, environments, HTTP method names in small tools, test statuses, and roles — anywhere a handful of legal options should not be free-typed strings.

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 61](chapter-61-enums.md) | `enum BrowserType { CHROME, FIREFOX, EDGE }` |
