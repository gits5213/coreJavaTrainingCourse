# Chapter 10 — JDK, JRE, and JVM: What Did You Actually Install?

## 1. Today's Goal

By the end of this lesson, you will draw this nesting from memory:

```
  JDK  (Development Kit)  = the whole toolbox you install for this course
    |
    +-- compiler javac
    +-- launcher java
    +-- debugger and other tools
    +-- standard libraries (String, List, ...)
    +-- a runtime that includes the JVM
          |
          JRE  (Runtime Environment)  = run-only kit (historical / conceptual)
                |
                JVM  = the engine that executes bytecode
```

**This course: install a JDK 25 LTS.** SDETs compile. A run-only install is the wrong hat.

---

## 2. Why It Matters

Installers on the internet still say "Get Java" as if there were one button. Then:

- `java -version` works but `javac -version` does not → you cannot compile.
- IntelliJ says "JDK not found."
- A tester installs a browser plugin and thinks they installed a JDK.

Knowing the three-letter soup saves a day of career.

---

## 3. Real-Life Analogy

Think of a **restaurant kitchen kit**.

```
  JDK = professional kitchen
        knives, stove, recipes library, AND the ability to cook
        (you can create new dishes and serve them)

  JRE = a dining room with a microwave and pre-made meals
        (you can heat and serve; you cannot develop new recipes well)

  JVM = the stove's fire
        (the heat that actually cooks)
```

A food critic who only eats (end user of a Java app) might have needed a JRE in older times.

A **chef** (developer, SDET) needs the **JDK**.

Today, Oracle and others have simplified packaging: you mostly download a **JDK**. The JRE as a separate consumer download is less central than it was in 2008. The **concept** still helps you think.

---

## 4. Illustrated Explanation

### Nesting

```
  +======================================================+
  | JDK 25                                               |
  |  +------------------+  +---------------------------+ |
  |  | Tools            |  | Libraries (API)           | |
  |  |  javac           |  |  java.lang.String         | |
  |  |  java            |  |  collections, I/O, ...    | |
  |  |  jshell          |  +---------------------------+ |
  |  |  jdb (debugger)  |                                |
  |  |  javadoc         |                                |
  |  +------------------+                                |
  |                                                      |
  |  +------------------------------------------------+  |
  |  | Runtime  (what people used to call JRE)        |  |
  |  |    +----------------------------------------+  |  |
  |  |    | JVM                                    |  |  |
  |  |    |  class loader, interpreter, JIT, GC... |  |  |
  |  |    +----------------------------------------+  |  |
  |  +------------------------------------------------+  |
  +======================================================+
```

### Two version commands

```
  java -version     →  the runtime / JVM launcher
  javac -version    →  the compiler  (JDK tool)
```

For this course, **both should report 25**.

If `java` is 25 and `javac` is missing, you do not have a usable JDK on your PATH.

If `java` is 17 and `javac` is 25 (or the reverse), you have a **PATH salad**. IntelliJ might use one JDK while your terminal uses another. SDETs get bitten here weekly.

### Where IntelliJ fits

```
  IntelliJ (IDE)
      |
      |  you point it at a JDK on disk
      v
  JDK 25 folder
      |
      +-- javac compiles your module
      +-- java runs your main / tests
```

IntelliJ is **not** the JDK. It **uses** a JDK.

---

## 5. Syntax / Concept

| Acronym | Stands for | You need it to... |
|---------|------------|-------------------|
| **JDK** | Java Development Kit | Write, compile, debug, run |
| **JRE** | Java Runtime Environment | Run already-built Java (conceptually) |
| **JVM** | Java Virtual Machine | Execute bytecode (inside the runtime) |

**Libraries:** the huge set of classes that come with the platform. `System.out.println` works because the JDK's library contains `System`.

**Tool vs engine:** `javac` is a tool. The JVM is an engine. `java` is the tool that **starts** the engine.

---

## 6. Simple Example

You cannot install from a Java file, but you can print the home of the JDK/runtime you launched.

```java
public class JdkInventory {

    public static void main(String[] args) {
        System.out.println("java.home (runtime folder): " + System.getProperty("java.home"));
        System.out.println("java.version: " + System.getProperty("java.version"));
        System.out.println("java.vendor: " + System.getProperty("java.vendor"));
        System.out.println("class.version (bytecode format this JVM knows): "
                + System.getProperty("java.class.version"));
    }
}
```

After Chapter 13, also run in a terminal:

```text
java -version
javac -version
```

Write both outputs in your notebook. If they disagree, stop and fix PATH / IntelliJ project SDK before learning loops. Otherwise every later chapter will be haunted.

---

## 7. Real-World Example

A bank laptop image includes **JDK 25** because developers compile. Production servers include a runtime capable of running the bank's services (often a JDK in modern deployments, because ops simplicity beats tiny installers).

```java
public class EnvironmentGuard {

    public static void main(String[] args) {
        String home = System.getProperty("java.home");
        System.out.println("Running from: " + home);

        // A real service might refuse to boot if the major version is wrong.
        String spec = System.getProperty("java.specification.version");
        if (!spec.equals("25")) {
            System.out.println("Wrong platform version: " + spec);
        } else {
            System.out.println("Bank service JDK/JVM spec 25 OK.");
        }
    }
}
```

E-commerce CI image:

- layer 1: OS
- layer 2: **JDK 25**
- layer 3: Chrome + ChromeDriver (for UI tests)
- layer 4: your test project

Missing layer 2: nothing Java runs. Missing layer 3: API tests still run. That is why knowing **what you installed** matters for failure triage.

---

## 8. SDET Example

A new hire says, "Java is installed. I can open Minecraft / a browser applet museum / some random JRE." Their `javac` fails.

Your checklist (you will use this on the job):

```text
1. java -version
2. javac -version
3. echo PATH  (or Windows equivalent)
4. IntelliJ: File → Project Structure → Project SDK
```

```java
public class SdetJdkChecklist {

    public static void main(String[] args) {
        System.out.println("If this program runs, a JVM launched it.");
        System.out.println("That does NOT prove javac is on your PATH.");
        System.out.println("SDET still needs a JDK to compile tests.");
    }
}
```

You can run a program without being able to compile a new one — if someone else compiled it. SDETs are not "someone else."

---

## 9. Break the Code

There is nothing wrong with this source. The break is in the **environment**.

```java
public class CompilerMissing {

    public static void main(String[] args) {
        System.out.println("I run, therefore a JVM exists.");
    }
}
```

On the broken machine:

```text
java CompilerMissing     →  works (if class was compiled elsewhere)
javac CompilerMissing.java  →  'javac' is not recognized
```

**Predict:** JDK, JRE, or JVM — which piece is missing from the PATH story?

---

## 10. Debug

**Diagnosis:** Runtime is present. **Compiler tool is not** on PATH. Typical of a JRE-only install or a JDK installed but terminal not refreshed / PATH pointing at another Java.

**Fix (concept):**

1. Install JDK 25.
2. Ensure `JAVA_HOME` points at that JDK (tools often use this).
3. Put `JDK/bin` on PATH (`bin` contains `java` **and** `javac`).
4. Close and reopen the terminal.
5. Align IntelliJ Project SDK with the same folder.

**Verify:**

```text
java -version
javac -version
```

Both 25. Same vendor if possible.

**IntelliJ-only trap:** IntelliJ can compile with its configured JDK while your terminal uses another. Debug by printing `java.home` from a small program **and** checking the terminal.

---

## 11. Student Exercise

Without looking, sketch the nesting boxes: JDK contains tools + libraries + runtime; runtime contains JVM.

Label:

- `javac`
- `java`
- `String` class library
- garbage collector (inside JVM, even if you only write the letters "GC")

Write which box this course asks you to download.

---

## 12. Challenge

A teammate has:

```text
java -version   →  17
javac -version  →  25
```

List:

1. What weird bugs this can cause.
2. How an SDET would notice (class version errors, IDE vs CI).
3. The desired end state for this course.

Write it as a troubleshooting runbook of 8–12 lines.

---

## 13. Knowledge Check

1. Expand JDK, JRE, JVM.
2. Which one includes `javac`?
3. Which one is the engine that executes bytecode?
4. Why does this course say "install a JDK," not "install a JRE"?
5. What should `java -version` and `javac -version` show here?
6. True or false: IntelliJ is a JDK.
7. Where do `java` and `javac` live inside a JDK folder?
8. What is `java.home`?
9. If a program runs, does that prove you can compile?
10. Why can terminal Java and IntelliJ Java differ?

---

## 14. Interview Question

**Question:** "Difference between JDK, JRE, and JVM?"

**Strong answer:**

> "The JVM is the virtual machine that executes bytecode. The JRE is the runtime package: JVM plus libraries to run Java programs. The JDK is the development kit: it includes a runtime and also javac, a debugger, and other tools. Developers and SDETs install a JDK. I use JDK 25 LTS, and I check both java -version and javac -version so the compiler and runtime match."

That is a classic screening question. You are now prepared.

---

## 15. Homework

1. Recite the kitchen analogy once.
2. Add a notes page titled "If javac is missing."
3. Read Chapter 11 for the full run pipeline (class loading, verify, interpreter, JIT, GC).
4. Do not skip Chapter 11 because the acronyms felt like enough. The next chapter is the movie; this one was the cast list.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. Java Development Kit; Java Runtime Environment; Java Virtual Machine.
2. JDK.
3. JVM.
4. We compile our own programs and tests.
5. Version 25 (LTS), ideally matching each other.
6. False. It is an IDE that uses a JDK.
7. In the `bin` directory.
8. The filesystem folder of the runtime that started.
9. No.
10. They can be configured independently (PATH vs Project SDK).

</details>
