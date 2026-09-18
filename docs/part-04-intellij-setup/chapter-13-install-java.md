# Chapter 13 — Install Java (JDK 25 LTS)

## 1. Today's Goal

By the end of this lesson, you will have **JDK 25 LTS** on your computer, and you will **prove** it with two commands:

```text
java -version
javac -version
```

Both should mention **25**. If they do, you have a compiler and a runtime, not a mystery.

We will not compile a huge app today. We will install and verify.

---

## 2. Why It Matters

Every later chapter assumes these commands work. "I'll install it later" becomes "I skipped homework and now Chapter 15 is a wall."

SDET CI machines need the same proof. `java -version` in a pipeline log is how adults start a debugging session.

---

## 3. Real-Life Analogy

Installing a JDK is like **installing a professional stove** before cooking school.

```
  Stove installed?     JDK installed?
  Heat works?          java -version
  Oven works too?      javac -version
  Wrong gas type?      Java 8 still hanging around on PATH
```

If you skip the oven test, you will blame the recipe (your code) when the oven (old Java 8) is the villain.

---

## 4. Illustrated Explanation

### What you are putting on disk

```
  JDK 25 folder (example shape)
    bin/
      java     ← run
      javac    ← compile
      jshell
      ...
    lib/
      (libraries)
    conf/
    ...
```

### PATH (the hallway)

```
  You type: javac
       |
       v
  The OS walks the PATH hallway looking for a door named javac
       |
       +-- finds JDK 25/bin/javac   →  good
       +-- finds an old Java 8 first →  surprise
       +-- finds nothing             →  "not recognized"
```

**PATH order matters.** The first match wins.

### `JAVA_HOME`

Many tools (IntelliJ, Maven later, CI) ask: "Where is the JDK root folder?" That location is **JAVA_HOME**. It should point at JDK 25, **not** at `bin` itself, and **not** at a JRE-only folder if you can help it.

```
  JAVA_HOME = C:\...\jdk-25.x     or  /Library/Java/JavaVirtualMachines/.../Contents/Home
  PATH      = ... + JAVA_HOME/bin
```

Exact clicks differ on Windows, macOS, and Linux. The **proof commands do not**.

---

## 5. Syntax / Concept

| Item | Meaning |
|------|---------|
| **JDK 25 LTS** | This course's standard development kit. |
| **LTS** | Long-Term Support — the train companies stay on. |
| **`java -version`** | Prints runtime/JVM version. |
| **`javac -version`** | Prints compiler version. |
| **Distribution** | Who built your JDK: Eclipse Temurin, Oracle, Amazon Corretto, Microsoft, Azul, ... |

**Where to get it (lawful, normal options):**

- [Eclipse Temurin (Adoptium)](https://adoptium.net/) — popular OpenJDK builds
- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) — read license for work use
- OS package managers (`brew`, `winget`, `sdkman`) if you know them

Avoid random "Free Java Download" ads with extra toolbars.

**This course does not require you to pay.** Temurin Community use is a common classroom path.

---

## 6. Simple Example

After install, you still write Java the same way. Verification is the "program" of this chapter.

In a **new** terminal window:

```text
java -version
javac -version
```

Healthy example (numbers after 25 may differ):

```text
openjdk version "25.0.x" ...
javac 25.0.x
```

A tiny program you can compile **from the terminal** once, to complete the loop (optional if IntelliJ is next):

```java
public class VersionOk {

    public static void main(String[] args) {
        System.out.println("JDK appears to work.");
        System.out.println(System.getProperty("java.version"));
    }
}
```

```text
javac VersionOk.java
java VersionOk
```

---

## 7. Real-World Example

A bank laptop setup script installs Temurin 25 and sets `JAVA_HOME` so every clerk-developer compiles the same way.

An e-commerce CI YAML (shape, not a copy-paste exam):

```text
- uses: actions/setup-java
  with:
    distribution: temurin
    java-version: "25"
- run: java -version
- run: javac -version
```

If those lines print 17, the next million test lines are untrustworthy relative to your laptop.

```java
public class PinTwentyFive {

    public static void main(String[] args) {
        String spec = System.getProperty("java.specification.version");
        if (!"25".equals(spec)) {
            throw new IllegalStateException("Need Java 25, found " + spec);
        }
        System.out.println("Pinned 25 OK.");
    }
}
```

---

## 8. SDET Example

Onboarding an SDET:

```text
[ ] JDK 25 installed
[ ] java -version → 25
[ ] javac -version → 25
[ ] IntelliJ Project SDK → same folder
[ ] git clone the test repo (later)
```

```java
public class OnboardingSmoke {

    public static void main(String[] args) {
        System.out.println("vendor  = " + System.getProperty("java.vendor"));
        System.out.println("version = " + System.getProperty("java.version"));
        System.out.println("home    = " + System.getProperty("java.home"));
    }
}
```

Paste that output into your study notes. Future you will thank present you when CI differs.

---

## 9. Break the Code

The "bug" is environmental.

**Break A:** Java 8 appears because an old install is first on PATH.

```text
java -version
java version "1.8.0_xxx"
```

**Break B:** `java` works, `javac` does not.

**Break C:** You installed 25 but IntelliJ still uses 11.

**Predict:** Which break is "JRE-only / compiler not on PATH"? Which is "hallway order"?

---

## 10. Debug

**Old version wins:** find all Java installs. Put JDK 25 `bin` **earlier** on PATH. Restart the terminal. On macOS, `/usr/libexec/java_home -V` lists VMs.

**javac missing:** install a JDK, not a runtime-only package. Check `dir %JAVA_HOME%\bin\javac.exe` (Windows) or `ls $JAVA_HOME/bin/javac` (macOS/Linux).

**IntelliJ disagrees:** Project Structure → SDK → add JDK 25 folder → set as project SDK.

**Permission / company machine:** you may need admin or a sanctioned installer. Do not pirate JDKs.

**Verify after every fix with a fresh terminal.** Old terminals keep old PATH.

---

## 11. Student Exercise

Perform and check off:

1. Download a JDK 25 LTS from a trusted vendor.
2. Install it.
3. Open a **new** terminal.
4. Run `java -version` and `javac -version`.
5. Write the exact output in your notebook.
6. Write the JDK folder path (the one you would call JAVA_HOME).

If you cannot install (locked laptop), write what blocker you hit and what you will ask IT. That is professional behavior.

---

## 12. Challenge

Write a one-page "JDK 25 proof" you could Slack a teammate:

- commands
- healthy output
- three unhealthy outputs and the next step for each

No screenshots required; text is enough and more searchable.

---

## 13. Knowledge Check

1. What two commands prove a JDK, not only a runtime?
2. What version does this course want?
3. What does LTS mean?
4. Why can `java -version` show 8 after you installed 25?
5. What is JAVA_HOME supposed to point at?
6. True or false: any website offering Java is equally safe.
7. Why open a new terminal after install?
8. Name one trusted distribution mentioned in this chapter.
9. If `javac` is missing, what did you probably install?
10. Should IntelliJ's SDK match the terminal's JDK?

---

## 14. Interview Question

**Question:** "How do you check which Java is installed?"

**Strong answer:**

> "I run java -version and javac -version. For SDET work I need both, on the same major version — we use 25 LTS. I also check JAVA_HOME and, in IntelliJ, the Project SDK, because the IDE can point at a different JDK than the terminal."

---

## 15. Homework

1. Finish the student exercise proof.
2. If versions mismatch, do not "hope." Fix PATH/SDK now.
3. Chapter 14: create **java-learning** in IntelliJ with the IntelliJ build system (not Maven yet).

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. `java -version` and `javac -version`.
2. 25 (LTS).
3. Long-Term Support.
4. An older `java` appears first on PATH.
5. The JDK root folder, not necessarily `bin`.
6. False.
7. PATH and environment variables refresh.
8. Temurin / Oracle / Corretto / Microsoft / Azul — any one mentioned.
9. A runtime-only (JRE-like) package, or JDK `bin` not on PATH.
10. Yes, preferably.

</details>
