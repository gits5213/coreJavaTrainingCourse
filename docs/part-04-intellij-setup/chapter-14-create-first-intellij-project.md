# Chapter 14 — Create Your First IntelliJ Project (`java-learning`)

## 1. Today's Goal

By the end of this lesson, you will have an IntelliJ project named **`java-learning`** that uses the **IntelliJ build system** (sometimes shown as a plain Java project / IntelliJ IDEA build).

We **do not** add Maven or Gradle yet. Those are excellent. They are also extra moving parts. First we want:

```
  a folder + src + a JDK 25 SDK + a class you can run
```

---

## 2. Why It Matters

A "project" is not a random `.java` file on the Desktop. It is a **workshop with walls**: source folders, an SDK, output folders, and settings.

SDET frameworks are projects. If you cannot create one empty Java project, you cannot later import a test framework without fear.

---

## 3. Real-Life Analogy

Creating a project is **renting a workshop**, not buying a single screwdriver and leaving it on the bus.

```
  Desktop\Hello.java          = screwdriver on the bus
  java-learning/              = workshop
     src/                     = workbench
     .idea/                   = IntelliJ's notebook about this shop
     out/                     = finished pieces (generated)
```

Maven later is like adding a **supplies catalog** (dependencies). You do not need a catalog to hammer the first nail.

---

## 4. Illustrated Explanation

### Target shape

```
  java-learning/
  |
  +-- .idea/                 (IntelliJ settings — do not hand-edit yet)
  +-- src/
  |     (your .java files will live here, maybe in packages later)
  +-- out/                   (appears after you compile/run)
  +-- java-learning.iml      (IntelliJ module file; may exist)
```

### Clicks (Community edition, wording close enough if JetBrains moves a menu)

```
  1. Open IntelliJ IDEA
  2. New Project
  3. Language: Java
  4. Build system: IntelliJ  (NOT Maven, NOT Gradle)
  5. JDK: 25  (the one you installed)
  6. Name: java-learning
  7. Location: a folder you can find, e.g. Desktop or a coding folder
  8. Create
```

If the wizard says "Add sample code," you may check it **or** wait for Chapter 15 and add `HelloWorld` yourself. Either is fine if you understand the file.

### After create

```
  Left: Project tool window
        java-learning
          src

  Top-right / bottom: if SDK missing, a banner will nag you. Listen to the nag.
```

### Why not Maven today?

```
  Maven adds:
    pom.xml
    dependencies
    a different folder convention (src/main/java)
    a way to fail that is not your Java syntax

  We will learn it when we need libraries (JUnit as a dependency, Selenium, ...).
```

---

## 5. Syntax / Concept

| Word | Meaning |
|------|---------|
| **Project** | The top-level thing IntelliJ opened. |
| **Module** | A piece of the project (beginners often have one module). |
| **Content root** | The folder IntelliJ treats as the project base. |
| **Source root (`src`)** | Where `.java` files are compiled from. |
| **Output (`out`)** | Where `.class` files go (generated). |
| **`.idea`** | IntelliJ's project settings directory. |

**Do not** copy `.class` files around as your "project." **Do** keep source.

**Git later:** we usually commit source and build files we intend to share; we do not commit `out/` junk. This chapter does not require Git.

---

## 6. Simple Example

After the project exists, create a class (Chapter 15 goes deeper). The **project** is the container; this is a smoke file.

`src/ProjectSmoke.java`:

```java
public class ProjectSmoke {

    public static void main(String[] args) {
        System.out.println("java-learning project is alive.");
        System.out.println("SDK Java version: " + System.getProperty("java.specification.version"));
    }
}
```

Right-click the file → **Run 'ProjectSmoke.main()'**.

If you see the two lines in the Run window, the workshop has electricity.

---

## 7. Real-World Example

A bank does not keep 400 classes as 400 desktop files. They have a project (often Maven/Gradle in real life). Your `java-learning` is the baby form of that.

E-commerce `checkout-tests` will later look like:

```
  checkout-tests/
    src/
      tests...
      pages...
```

Today's empty `src` is the same idea with fewer rooms.

```java
public class ShopProjectStory {

    public static void main(String[] args) {
        System.out.println("Orders, users, and tests will be classes in src.");
        System.out.println("The project name is the shop building; classes are rooms.");
    }
}
```

---

## 8. SDET Example

When you join a team, they say `git clone` then **Open** the existing project in IntelliJ. Creating `java-learning` teaches you what "Open" is looking at.

```java
public class OpenExistingProjectIdea {

    public static void main(String[] args) {
        System.out.println("On a real job I will Open a cloned repo.");
        System.out.println("Today I created my own so I know what a project feels like.");
    }
}
```

SDET check: **Run** works without Maven. If it does, your SDK and `src` are marked correctly.

---

## 9. Break the Code

**Break A:** You chose Maven by accident, panicked at `pom.xml`, and thought you failed Java.

**Break B:** Project name `java learning` with a **space**. Tools trip. Use `java-learning`.

**Break C:** You created the project with no JDK selected. Editor says "Project SDK is not defined."

**Break D:** You put `ProjectSmoke.java` **outside** `src`. IntelliJ will not compile it as part of the module.

**Predict:** Which break is "wrong folder"? Which is "wrong build system for this chapter"?

---

## 10. Debug

**Maven by accident:** File → New → Project again, or start over. You can delete the folder if nothing precious is there. Do not keep a half-wizard you do not understand.

**SDK not defined:** Project Structure (Ctrl+Alt+Shift+S on Windows/Linux, on macOS: IntelliJ IDEA → Settings / File → Project Structure depending on version) → Project → SDK → 25.

**File outside src:** drag it under `src`, or mark the folder as Sources Root (right-click folder → Mark Directory as → Sources Root). Beginners: just use `src`.

**Cannot find project:** you saved it in a forgotten directory. Use IntelliJ's recent projects list or search your disk for `java-learning`.

**Name with spaces:** recreate with a hyphen. Hyphens are friendlier than spaces for tools.

---

## 11. Student Exercise

1. Create **java-learning** with IntelliJ build system and JDK 25.
2. Confirm `src` exists.
3. Add `ProjectSmoke` and run it.
4. Write down the full folder path of the project in your notes (example: `/Users/you/Desktop/java-learning`).
5. Find the `out` folder after running. Do not edit files inside `out`.

---

## 12. Challenge

Explain in a short paragraph, as if to a Level 0 student, the difference between:

- the **repository** `coreJavaTrainingCourse` (this course's docs)
- the **IntelliJ project** `java-learning` (your practice workshop)

Should `java-learning` live inside the course repo or beside it? Pick one for *your* machine and give a reason. (Both can work; the reason matters.)

---

## 13. Knowledge Check

1. What is the required project name?
2. Which build system do we use first?
3. When will we use Maven in this course's philosophy?
4. What is `src` for?
5. What is `out` for?
6. True or false: you should hand-edit `.class` files in `out` to fix bugs.
7. What happens if the Project SDK is empty?
8. Why avoid spaces in the project name?
9. Where must `ProjectSmoke.java` live?
10. Is IntelliJ the same as the `java-learning` folder on disk?

---

## 14. Interview Question

**Question:** "What's the difference between a Java file and a Java project?"

**Strong answer:**

> "A Java file is one compilation unit of source. A project is the container: source roots, JDK, output path, and later dependencies and test runners. In IntelliJ I created java-learning as a plain IntelliJ Java project so I could run main methods before introducing Maven."

---

## 15. Homework

1. Finish the student exercise.
2. Leave Maven for later even if a blog screams "always Maven."
3. Chapter 15: write Hello World and understand **every piece** without forced memorization.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. `java-learning`
2. IntelliJ's built-in Java / IntelliJ build system.
3. When we need dependency management (libraries).
4. Source `.java` files.
5. Generated `.class` (and related) output.
6. False.
7. You cannot compile/run reliably; IntelliJ warns.
8. Some tools and scripts break on spaces.
9. Under `src` (source root).
10. IntelliJ is the IDE program; `java-learning` is the project folder it opens.

</details>
