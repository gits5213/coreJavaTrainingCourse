# Part 37 — Clean Code

Clean code is not fancy code. It is code a teammate can read at 8 a.m. after a night of failing pipelines.

BAD:

```java
public void doIt(String x) {
}
```

GOOD:

```java
public void createTestUser(String username) {
}
```

This part is a professional habit, not a library.

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 93](chapter-93-clean-code.md) | Meaningful names, small methods, readable logic, little duplication, clear responsibility |

## Prerequisite

You can write methods, classes, and tests. Clean code without those tools is just wishful naming.

## SDET Connection

Test names and method names *are* documentation. `doIt(x)` cannot be reviewed. `createTestUser(username)` can.
