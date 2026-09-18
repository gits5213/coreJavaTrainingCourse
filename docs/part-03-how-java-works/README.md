# Part 3 — How Java Works

This part answers the question students are afraid to ask:

> When I press Run, what actually happens?

If you skip this, IntelliJ will feel like a microwave: you push a button, food appears, and you cannot fix it when it explodes.

---

## What This Part Is For

By the end of Part 3 you should be able to draw this from memory:

```
  Hello.java          javac           Hello.class         java
  (source you   -->  (compiler) -->  (bytecode)   -->   (JVM runs it)
   type)
```

You should also be able to say what **JDK**, **JRE**, and **JVM** are for, and preview the inside of a run: class loading, bytecode verification, interpreter, JIT, garbage collection.

You still do not need to become a JVM engineer. You need a correct cartoon that later chapters can hang details on.

---

## Chapters

| Chapter | File | Question it answers |
|---------|------|---------------------|
| 7 | [chapter-07-source-code.md](chapter-07-source-code.md) | What is a `.java` file, really? |
| 8 | [chapter-08-compilation.md](chapter-08-compilation.md) | What does `javac` do, and what is bytecode? |
| 9 | [chapter-09-jvm.md](chapter-09-jvm.md) | What is the JVM, and why does it make Java portable? |
| 10 | [chapter-10-jdk-jre-jvm.md](chapter-10-jdk-jre-jvm.md) | What did I actually install? |
| 11 | [chapter-11-what-happens-when-java-runs.md](chapter-11-what-happens-when-java-runs.md) | The full trip from file to pixels on your screen |

---

## How to Study Part 3

- Redraw every pipeline on paper. If you cannot draw it, you do not own it yet.
- When Java code appears, type it later in Part 4. Here, focus on the **pipeline**.
- Words to keep (not to fear): source, compile, bytecode, JVM, class loader, verify, interpret, JIT, GC.

---

## SDET Thread

When a test "does not run," the cause is often not Selenium. It is:

- wrong JDK
- code that did not compile
- a class not on the classpath
- a different JVM than you thought

Part 3 is how you stop guessing.

---

## When You Are Done

Go to [Part 4 — IntelliJ Setup](../part-04-intellij-setup/README.md) and build the workshop.
