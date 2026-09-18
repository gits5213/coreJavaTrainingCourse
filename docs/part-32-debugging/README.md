# Part 32 — Debugging

A **bug** is when the program **behaves differently from what you intended**.

The computer did what the code said. The code did not say what you meant.

```text
You meant:  total = price * quantity
You wrote:  total = price - quantity
```

That is a bug. IntelliJ's debugger is how you watch the lie happen in slow motion.

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 88](chapter-88-what-is-a-bug.md) | Bug definition, IntelliJ debug tools, the debug flow |

## The Flow (memorize)

```text
Run  →  Breakpoint  →  Pause  →  Inspect  →  Understand  →  Fix
```

Do not guess-and-change five lines at once. Inspect first.

## Prerequisite

You can run a program in IntelliJ. You have seen exceptions. You can read a stack trace from the top.

## SDET Connection

A failing test is a bug in the product **or** a bug in the test. Debugging is how you tell which. The debugger works on test code too: breakpoint the assertion, inspect expected vs actual.
