# Chapter 8 — Compilation: From `Hello.java` to `Hello.class`

## 1. Today's Goal

By the end of this lesson, you will be able to explain **compilation** in Java:

> The **Java compiler** (`javac`) reads source (`.java`) and produces **bytecode** (`.class`). Bytecode is not English, and it is not your CPU's native machine code yet. It is a portable instruction set for the JVM.

You will know the two commands:

```text
javac Hello.java    →  creates Hello.class
java  Hello         →  runs the class (no .class in the command)
```

---

## 2. Why It Matters

When IntelliJ hides this, students think programs "just run." Then CI says `cannot find symbol` or `class file has wrong version`, and there is no mental model.

SDETs compile constantly. A test that is not compiled is a diary entry. A test that compiled on JDK 25 and ran on JDK 11 is a crime scene.

---

## 3. Real-Life Analogy

You write a play in English (source). A **translator** produces a **universal stage script** that many theaters know (bytecode). Each theater's director (JVM) turns that script into local actors' movements (machine code for *this* CPU).

```
  English play          Translator           Universal script         Local performance
  Hello.java     -->    javac         -->    Hello.class      -->    java / JVM
```

If the English has a grammar error, the translator **refuses**. That refusal is a **compile error**. Nothing runs. This is a gift. The gift feels like an insult on day one.

---

## 4. Illustrated Explanation

### The two artifacts

```
  +----------------+          javac           +------------------+
  | Hello.java     |  --------------------->  | Hello.class      |
  | text, braces,  |                          | bytecode bytes   |
  | for humans     |                          | for the JVM      |
  +----------------+                          +------------------+
         ^                                            |
         | you edit this                              |
         +--------------------------------------------+
                    you do NOT usually edit this
```

### What `javac` checks (cartoon)

```
  1. Can I read the file?
  2. Do the characters form legal Java?
  3. Do names exist (methods, classes, variables)?
  4. Do types make sense (don't add a boolean to a giraffe)?
  5. If yes: write .class
  6. If no: print errors, write nothing useful (or incomplete output you should not trust)
```

### Bytecode is still not the CPU

```
  .java     human language-ish
  .class    JVM language (bytecode)
  native    CPU language (comes later, inside the JVM: interpreter/JIT)
```

C compilers often go source → machine code for **one** OS/CPU. `javac` goes source → **bytecode**. That extra stop is the WORA trick from Chapter 4.

### Command line picture

```
  folder:
    Hello.java

  $ javac Hello.java

  folder now:
    Hello.java
    Hello.class     ← new

  $ java Hello      ← name of the class, not Hello.class
```

IntelliJ clicks **Run** and does a compile-if-needed plus `java` for you. You still must know the two steps exist.

---

## 5. Syntax / Concept

| Word | Meaning |
|------|---------|
| **Compile** | Translate source to another form (here, bytecode). |
| **Compiler** | The program that does that (`javac` lives in the JDK). |
| **Bytecode** | JVM instructions stored in `.class` files. |
| **Compile error** | The source was not legal Java (or types/names failed). |
| **Runtime error** | It compiled, then failed while running. |

**Syntax error example:** missing `;`  
**Type error example:** putting `"hello"` into an `int` box  
**Runtime example:** dividing by zero in some cases, or calling a method on `null` later in the course

Beginner rule: **red errors in IntelliJ at the editor = usually compile-time. Explosions after you press Run = runtime.**

The class file also stores a **bytecode version number**. Newer `javac` produces newer stamps. Older JVMs reject them.

---

## 6. Simple Example

Source `Hello.java`:

```java
public class Hello {

    public static void main(String[] args) {
        System.out.println("Compiled, then run.");
    }
}
```

Commands (you will type these once in Part 4 even if you live in IntelliJ):

```text
javac Hello.java
java Hello
```

Expected output of the second command:

```text
Compiled, then run.
```

If you skip `javac` and there is no `Hello.class` yet, `java Hello` cannot run what does not exist (IntelliJ may auto-compile; the terminal will not invent the file).

---

## 7. Real-World Example

A bank builds a fee calculator. The source is reviewed. CI runs `javac` (or Maven, which calls the compiler) on a clean machine.

```java
public class WireFee {

    public static void main(String[] args) {
        double amount = 1000.00;
        double fee = 15.00;
        double total = amount + fee;
        System.out.println("Customer pays: " + total);
    }
}
```

If someone writes `double total = amount + "15";` the **compiler** stops the build. The bad fee never reaches production. That is compilation as a seatbelt.

E-commerce: a misspelled method `pritnln` never goes live if CI compiles. Users never see it. Testers never hunt it. The compiler already hunted it.

---

## 8. SDET Example

Your test source has a typo in a helper method name. `javac` fails. CI is red.

This is **good red**. Good red is "we did not ship nonsense." Bad red is a flake at 2 a.m.

```java
public class CheckoutTestIdea {

    public static void main(String[] args) {
        int actualTotal = 42;
        int expectedTotal = 42;
        assertEqual(expectedTotal, actualTotal);
    }

    static void assertEqual(int expected, int actual) {
        if (expected != actual) {
            System.out.println("FAIL");
        } else {
            System.out.println("PASS");
        }
    }
}
```

If you call `assertEquals` (with an `s`) but defined `assertEqual`, compilation fails: **cannot find symbol**. The SDET skill is to read that as "name mismatch," not as "Jenkins hates me."

---

## 9. Break the Code

```java
public class Hello {

    public static void main(String[] args) {
        System.out.println("I forgot a semicolon")
    }
}
```

**Predict:** Will `Hello.class` be produced? Will `java Hello` show the message?

Another compile break:

```java
public class Hello {
    public static void main(String[] args) {
        int price = "free";
    }
}
```

---

## 10. Debug

**Symptom:** `';' expected`

**Meaning:** The compiler's grammar teacher caught a missing semicolon (sometimes the *real* mistake is one line above).

**Fix:** Add `;` after the `println(...)` call.

**Symptom:** `incompatible types: String cannot be converted to int`

**Meaning:** You tried to store text in a number box.

**Fix:** Use `String price = "free";` or use a numeric price.

**Debug order:**

```
  1. Read the FIRST error.
  2. Open the file and line it names.
  3. Fix one thing.
  4. Compile again.
  5. Do not "fix" 12 errors that were chain reactions from error 1.
```

SDETs who fix the last error first waste lives.

---

## 11. Student Exercise

Write a short table in your notes:

| Command | Input | Output artifact | What errors look like |
|---------|--------|-----------------|------------------------|
| `javac Hello.java` | | | |
| `java Hello` | | | |

Then write one compile error you could cause on purpose, and what you would expect to see.

---

## 12. Challenge

Explain this CI log in a paragraph to a Level 0 student:

```text
[ERROR] /home/runner/CheckoutTestIdea.java:5: error: cannot find symbol
[ERROR]     assertEquals(expectedTotal, actualTotal);
[ERROR]     ^
[ERROR]   symbol:   method assertEquals(int,int)
```

Then write the likely fix (name alignment). No need to run it yet.

---

## 13. Knowledge Check

1. What program compiles Java source?
2. What file does `javac Hello.java` produce (if successful)?
3. Is bytecode the same as Intel/AMD machine code?
4. Why does `java Hello` omit `.class`?
5. True or false: compile errors happen while the program is already painting a window.
6. What is a type error, in one sentence?
7. Why can compilation on 25 and running on 11 fail even if grammar is perfect?
8. Should you start at the last compiler error or the first?
9. How does IntelliJ relate to `javac`?
10. Why is a failed compile in CI often a *good* signal?

---

## 14. Interview Question

**Question:** "What happens when you compile a Java program?"

**Strong answer:**

> "javac reads .java source and produces .class bytecode if the source is legal. Bytecode targets the JVM, not a specific operating system CPU. Then the java launcher loads that class. Compile errors stop this before runtime. That's different from a C compiler emitting a native executable for one platform."

---

## 15. Homework

1. Draw `.java → javac → .class → java`.
2. Add to glossary: compile error vs runtime error.
3. Read Chapter 9 on the JVM — the theater that performs the universal script.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. `javac` (the Java compiler in the JDK).
2. `Hello.class`
3. No. Bytecode is for the JVM.
4. You pass the class name; the JVM adds the class file details.
5. False. Compilation is before a successful run.
6. Using a value in a way its type does not allow.
7. Newer bytecode version stamp; old JVM refuses it.
8. First.
9. IntelliJ invokes compilation for you when you build/run.
10. It blocked invalid source from pretending to be a test run.

</details>
