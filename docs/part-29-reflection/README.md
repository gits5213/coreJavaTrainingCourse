# Part 29 — Reflection

**Reflection** is when a running program **inspects** (and sometimes calls) classes, methods, and annotations by name.

```text
Normal code     login()           you typed the name; compiler checks it
Reflection      getMethod("login")  a string; compiler cannot check it
```

Frameworks use this: JUnit finds `@Test`, Jackson maps fields, dependency injection creates objects. You rarely need to write reflection in a beginner test. You need to know **why** those tools feel like magic.

## Warning

Reflection **increases complexity**, loses compile-time safety, and can break with renaming. **Do not use it without a reason.** Curiosity in this chapter is a reason. Production SDET code is usually not.

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Reflection](chapter-reflection.md) | Runtime inspection; frameworks; when not to |

## Prerequisite

Classes, methods, annotations. Optional: `@SmokeTest` from Chapter 79.
