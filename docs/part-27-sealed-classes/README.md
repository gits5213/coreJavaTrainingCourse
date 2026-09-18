# Part 27 — Sealed Classes

**Sealed** types restrict which classes may implement or extend them.

```java
public sealed interface TestResult permits PassedResult, FailedResult {}
```

This is **advanced**. It is not an early-course topic. You should already be comfortable with interfaces, inheritance, records, and `switch` before this is more than vocabulary.

## When to Wait

Wait if any of these are true:

- You still mix up class vs object
- You have not written an `interface`
- You have not used `record`
- You are rushing to Selenium

Come back when you design a small domain of **known result types** and you want the compiler to force you to handle all of them.

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Sealed classes](chapter-sealed-classes.md) | `sealed interface TestResult permits PassedResult, FailedResult` |

Do not start the SDET career here. Start with exceptions, files, and tests that fail honestly.
