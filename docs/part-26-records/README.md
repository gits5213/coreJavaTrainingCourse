# Part 26 — Records

A **record** is a concise, **immutable** data carrier.

```java
public record LoginData(String username, String password) {}
```

Java generates the constructor, accessors `username()` / `password()`, `equals`, `hashCode`, and `toString`.

SDET use: test-data models. You do not want a test to change `login.password` halfway through a run.

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 78](chapter-78-records.md) | `record LoginData(username, password)` |

## Prerequisite

Classes, fields, constructors. Records are "classes with opinionated defaults," not a reason to skip OOP.

## When to Wait

If you are still shaky on `class` vs object, finish those chapters first. Then this part will feel like a gift, not a third object model.
