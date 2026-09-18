# Part 30 — JVM Memory

Your Java program does not dump every variable into one pile. The JVM uses (among other areas) a **stack** and a **heap**.

```text
User user = new User("John");

stack:  user  →  (reference / arrow)
heap:   User object { name: John }
```

This part also covers **garbage collection** (reclaiming unreachable objects) and the **JIT compiler** (speeding up hot bytecode). You met the JVM in Part 3. Now we look inside the engine.

## Chapters in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 80](chapter-80-stack-and-heap.md) | Stack references, heap objects |
| [Chapter 81](chapter-81-garbage-collection.md) | Created → used → unreachable → eligible |
| [Chapter 82](chapter-82-jit-compiler.md) | Bytecode → observe → hot → optimized machine code |

## SDET Connection

- `OutOfMemoryError` in a suite that opens browsers and never closes them is a **heap** story plus a resource story.
- GC does not close files or browsers for you.
- "Java is slow" is often "cold JVM" or "we did something quadratic," not "bytecode cannot be fast." JIT is why long-running servers (and long test JVMs) speed up.

## Prerequisite

Objects, `new`, methods, Part 3 JVM/bytecode.
