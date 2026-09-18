# Chapter 5 — Who Owns Java?

## 1. Today's Goal

By the end of this lesson, you will stop using the word "Java" as if it were one object.

You will be able to separate:

| Word | Meaning |
|------|---------|
| **Java the language** | The grammar you write (`class`, `if`, `new`...) |
| **Java the specification** | The official rules of how that language and the VM must behave |
| **Java the trademark** | The legal name "Java" as a brand |
| **JDK** | A **Java Development Kit** you install to compile and run |
| **OpenJDK** | The open-source reference implementation family |
| **Oracle JDK** | Oracle's JDK build (rules/licenses have changed over years) |
| **JCP** | **Java Community Process** — how the language evolves in public proposals |

You will also know, at a headline level, that **Sun Microsystems created Java**, and **Oracle acquired Sun** (2010). You do not need to become a lawyer.

---

## 2. Why It Matters

Beginners install "Java" from a random website, then say:

- "I have Java but `javac` is missing."
- "The job says OpenJDK 25, is that different from Java?"
- "Can I use Oracle JDK at work?"

Those questions are identity questions. If you mix up **language**, **spec**, **brand**, and **the folder on your disk**, you will install the wrong thing and lose a day.

SDETs must know **which JDK the pipeline uses**. A test that passes on your Oracle or Microsoft or Eclipse Temurin build of 25 should be discussed as **bytecode + spec**, not as "the Java."

---

## 3. Real-Life Analogy

Think of **soccer** (football).

```
  THE GAME'S RULES          =  Java Specification
  (what a goal is)

  THE WORD "FIFA" / brand   =  Java trademark
  (you cannot casually sell
   a drink named like the
   official brand)

  HOW PEOPLE PLAY           =  Java the language
  (kicking, passing —
   the actual practice)

  A BALL + FIELD YOU BOUGHT =  a JDK
  (the kit that lets you play today)

  A PUBLIC PARK LEAGUE      =  OpenJDK builds
  (open implementation)

  A BRANDED OFFICIAL BALL   =  Oracle JDK
  (a vendor's kit; still soccer)
```

Many companies make balls. If they follow the rules of the game, you can play a match. If someone sells a "ball" that is a cube, it is not soccer, even if they print "Java" on it illegally.

**Java Community Process** is like a rules committee: people propose changes (JEPs / JSRs in real life), discuss, and the spec moves forward. You do not vote from your sofa as a beginner. You just need to know the language is not one person's secret diary anymore.

---

## 4. Illustrated Explanation

### The split that confuses everyone

```
                    +----------------------+
                    |  "JAVA" as a word    |
                    +----------------------+
                      /        |         \
                     v         v          v
              +----------+ +--------+ +-----------+
              | Language | |  Spec  | | Trademark |
              | you type | | rules  | |   brand   |
              +----------+ +--------+ +-----------+
                     \
                      v
              +------------------+
              | Implementations  |
              | (JDKs you install|
              |  that try to     |
              |  follow the spec)|
              +------------------+
                 /     |      \
                v      v       v
           OpenJDK  Oracle   Other builds
           (Temurin, JDK     (Amazon Corretto,
            Microsoft, ...)   Azul, ... )
```

### Ownership story (headline only)

```
  Sun Microsystems  ----creates---->  Java
         |
         |  2010 acquisition (remember as "Oracle bought Sun")
         v
  Oracle Corporation  — steward of the trademark and a major JDK
  OpenJDK             — the open implementation the industry standardizes on
```

People say "Oracle Java" in casual speech. Professionals ask: **which distribution, which version, which license for this company?**

For this course: **JDK 25 LTS**, preferably a current OpenJDK build you trust (Eclipse Temurin is a common classroom choice; Oracle's installer is also widely used). We care that `java` and `javac` are 25, not about winning a vendor argument.

### Language vs JDK on your computer

```
  You write language  -->  javac (from the JDK)  -->  bytecode
                                                    -->  java (from the JDK)
                                                    -->  JVM runs it
```

If you only install a runtime-ish package and not a full **JDK**, you may run programs but not compile them. SDETs compile all day. **Install a JDK, not "just Java" from a vague banner ad.**

---

## 5. Syntax / Concept

**Java the language** is what this looks like:

```java
public class OwnershipDemo {
    public static void main(String[] args) {
        System.out.println("This file is Java source.");
    }
}
```

**The specification** says what that *must* mean: how `main` is found, how `+` works on numbers, how strings live, how threads behave. If a vendor's JDK disagrees with the spec in a visible way, that is a bug in the implementation (or you are using a preview feature incorrectly).

**The trademark** is why not every company can sell a product named as if it were official Java without rules.

**JDK** = compiler (`javac`) + runtime (`java` + JVM) + libraries (`String`, collections, ...) + extra tools (debugger, `jshell`, ...).

**OpenJDK** = the open-source project / source line most builds come from.

**Oracle JDK** = Oracle's binary distribution of a JDK, historically aligned with OpenJDK for the language itself, with vendor-specific packaging and license terms that you must read for *work* (classrooms: follow your school's or this course's install guide).

**Java Community Process (JCP)** = the process for evolving standard Java through community specifications. When someone says "this is in the Java spec now," they mean it went through standardization, not that a blog invented it.

You will not recite JSR numbers. You will say: "Java is standardized; my JDK implements that standard."

---

## 6. Simple Example

Ask *this computer's JDK* who it is. That is the practical version of "who owns what I installed?"

```java
public class WhichJdkAmI {

    public static void main(String[] args) {
        String version = System.getProperty("java.version");
        String vendor = System.getProperty("java.vendor");
        String home = System.getProperty("java.home");
        String vmName = System.getProperty("java.vm.name");

        System.out.println("Language: Java (the source I wrote)");
        System.out.println("Running version: " + version);
        System.out.println("Vendor: " + vendor);
        System.out.println("VM name: " + vmName);
        System.out.println("Java home folder: " + home);
    }
}
```

When you run this in Part 4, write the four values in your notebook. That notebook line is more useful than a meme about Oracle.

---

## 7. Real-World Example

A bank's security team says: "Production runs **Eclipse Temurin 25**. Laptops must match."

Why they care:

- Same language spec → same program meaning
- Same JDK major version → fewer "works on 17, fails on 25" surprises
- Known vendor → known update channel and support story

```java
public class JdkGuardrail {

    public static void main(String[] args) {
        String version = System.getProperty("java.version"); // e.g. 25.0.x
        boolean looksLike25 = version.startsWith("25");

        if (!looksLike25) {
            System.out.println("REFUSE TO RUN: expected JDK 25, found " + version);
        } else {
            System.out.println("JDK looks like 25. Continue starting the bank batch job.");
        }
    }
}
```

E-commerce checkout and user-service teams do the same in CI: the pipeline image pins a JDK. "I have Java" is not a pin.

---

## 8. SDET Example

A UI suite fails on Jenkins with `unsupported class file version`. Locally it passed.

Translation from adult Java:

- You compiled with a **newer JDK** than the CI machine's **runtime**.
- Bytecode has a version stamp. Old JVMs refuse new stamps.

```java
public class ClassFileVersionStory {

    public static void main(String[] args) {
        String specVersion = System.getProperty("java.specification.version");
        System.out.println("This JVM's language spec version: " + specVersion);
        System.out.println("If CI is 17 and you compiled on 25, CI may refuse your .class files.");
    }
}
```

SDET checklist when "Java is broken":

1. `java -version` on the failing machine
2. `javac -version` on the compiling machine
3. Are they both 25?
4. Which vendor? (useful for support, rarely the first bug)

Ownership knowledge saves hours of blaming Selenium.

---

## 9. Break the Code

A student prints the vendor and panics.

```java
public class BrokenOwnershipPanic {

    public static void main(String[] args) {
        String vendor = System.getProperty("java.vendor");

        if (!vendor.equals("Sun Microsystems")) {
            System.out.println("This is not real Java. Delete everything.");
        } else {
            System.out.println("Real Java.");
        }
    }
}
```

**Predict:** What will a modern JDK print, and is the panic justified?

---

## 10. Debug

**Symptom:** The program says "This is not real Java" on a perfectly good JDK 25.

**Cause:** The student treated a **1995 company name** as the definition of Java. Sun does not ship your laptop's JDK in 2026. Oracle, Microsoft, Eclipse Adoptium, Amazon, Azul, and others do.

**The spec and language are "real Java."** The vendor string is *who built this binary*.

```java
public class FixedOwnershipCheck {

    public static void main(String[] args) {
        String vendor = System.getProperty("java.vendor");
        String version = System.getProperty("java.version");

        System.out.println("Vendor (who built this JDK): " + vendor);
        System.out.println("Version: " + version);
        System.out.println("If version is 25 and javac works, you can take this course.");
    }
}
```

**Interview-quality debug line:** "I confused trademark history with the implementation I installed."

---

## 11. Student Exercise

Fill this from memory, then correct it:

1. Language vs specification vs trademark vs JDK — one sentence each.
2. Sun's role vs Oracle's role — two sentences total.
3. Why SDETs install a **JDK**, not a random "Java player."
4. What JCP is, in one sentence.

Then, after Part 4 install, paste the output of `WhichJdkAmI` into your notes.

---

## 12. Challenge

Write a short email (yes, an email) to a fictional manager:

> Subject: Which Java we should standardize on for the new automation team

Requirements:

- Recommend **JDK 25 LTS**
- Explain OpenJDK vs "the Java language"
- Mention CI and laptops should match major version
- Do **not** pretend to give legal license advice; tell them to have legal read Oracle vs other distributions if they care
- Keep it under 200 words

This is architect practice at Level 1 volume.

---

## 13. Knowledge Check

1. What is the difference between the Java language and a JDK?
2. What is a specification in this context?
3. Who created Java, and who acquired that company?
4. What does OpenJDK refer to?
5. What does Oracle JDK refer to?
6. What is the JCP?
7. True or false: if `java.vendor` is not Oracle, your code is not Java.
8. Why might `javac` be missing even if a browser can do... nothing useful for compiling?
9. What does "unsupported class file version" usually mean?
10. For this course, which JDK version is the baseline?

---

## 14. Interview Question

**Question:** "What's the difference between JDK, OpenJDK, and Oracle JDK?"

**Strong answer:**

> "The JDK is the development kit: compiler, runtime, libraries, tools. OpenJDK is the open-source implementation of the Java spec that most vendors build from. Oracle JDK is Oracle's distribution of a JDK. They all implement Java the language if they follow the spec. Teams pick a vendor and pin a version — for example JDK 25 LTS — so laptops and CI match."

If they ask about JRE: preview Chapter 10 (runtime vs full kit).

---

## 15. Homework

1. Draw the "language / spec / trademark / JDK builds" diagram.
2. Add a glossary entry in your notebook: JDK, OpenJDK, Oracle JDK, JCP, WORA, JVM.
3. Do not download random "Free Java Fast" ads. Wait for Chapter 13's install path.
4. Read Chapter 6 for the *evolution* of the language — features, not vendors.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. Language = grammar you write. JDK = the installed kit that compiles and runs it.
2. The official rules for the language and platform behavior.
3. Sun Microsystems created it; Oracle acquired Sun.
4. Open-source Java implementation / project that vendors build from.
5. Oracle's JDK distribution.
6. Java Community Process — how standard Java is evolved through community specs.
7. False.
8. You installed something that is not a full JDK, or the compiler is not on your PATH.
9. Bytecode was compiled for a newer JVM than the one trying to run it.
10. JDK 25 LTS.

</details>
