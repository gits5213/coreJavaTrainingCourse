# Part 9 — Loops

A **loop** repeats work without copying the same lines over and over.

```text
Without loops
Print Test 1
Print Test 2
Print Test 3
Print Test 4
Print Test 5

With a loop
Count from 1 to 5
    print Test plus the count
```

Testers live in loops:

- run the same check for many users
- retry a flaky page load up to 3 times
- wait until a condition becomes true, with a safety limit
- open Chrome, then Firefox, then Edge

## The Three Loop Styles

```text
for        you know how many times, or you walk a range
while      keep going as long as a condition is true
do-while   do the work once, then maybe repeat
```

Chapter 33 adds two extra tools that work inside loops:

```text
break       leave the loop immediately
continue    skip the rest of this round, start the next round
```

## Chapters in This Part

| Chapter | Topic | SDET picture |
| --- | --- | --- |
| [Chapter 30](chapter-30-for.md) | `for` | Print Test 1 through Test 5 |
| [Chapter 31](chapter-31-while.md) | `while` | Retry attempts 1, 2, 3 |
| [Chapter 32](chapter-32-do-while.md) | `do-while` | Try at least once |
| [Chapter 33](chapter-33-break-and-continue.md) | `break` and `continue` | Stop on first failure, skip known ignored tests |

## Prerequisite

Part 8. Loops often contain `if` statements.

## Safety Rule

Every loop needs a way to finish.

```text
A loop without an ending
        ↓
repeats forever
        ↓
your program appears frozen
```

If IntelliJ never reaches the next `println` after a loop, you may have an infinite loop. We will practice stopping conditions carefully.
