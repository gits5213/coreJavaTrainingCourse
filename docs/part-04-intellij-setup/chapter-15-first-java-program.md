# Chapter 15 — Your First Java Program (Every Piece Explained)

## 1. Today's Goal

By the end of this lesson, you will type, run, and **explain** a Hello World program in `java-learning`.

You will **not** be required to memorize it like a poem. You will know **what job each piece is doing**, so when a piece is missing, you can guess the pain.

We use the **classic** form you will see at work:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, Java.");
    }
}
```

Modern JDK 25 also allows shorter "compact" programs. We will mention them. We teach the classic form first because 95% of bank and SDET code still looks like this.

---

## 2. Why It Matters

Hello World is not a toy because the message is short. It is a **complete pipeline test**:

source → compile → load → `main` → output

If Hello World fails, your workshop is unplugged. If it succeeds, you have earned the right to learn variables next.

SDETs still write small `main` methods to try an API client. The shape never dies.

---

## 3. Real-Life Analogy

A **light switch**.

```
  public class HelloWorld     →  the switch plate on the wall (the named thing)
  main                        →  "start here when someone flips the switch"
  System.out.println(...)     →  the bulb lighting up
  "Hello, Java."              →  the visible light (output)
```

You do not need to recite electrical code to use a switch. You do need to know the switch is the **entry**. `main` is the entry.

---

## 4. Illustrated Explanation

### File and class

```
  File:   src/HelloWorld.java
  Class:  HelloWorld

  They match. The public class name = file name (minus .java).
```

### The skeleton

```
  public class HelloWorld {          // 1. a public blueprint named HelloWorld
                                     //
      public static void main(...) { // 2. the official front door
          ...                        // 3. instructions
      }                              //
  }                                  // 4. end of class
```

### What happens at Run

```
  IntelliJ: Run HelloWorld
        |
        v
  javac HelloWorld.java     (if needed)
        |
        v
  java HelloWorld
        |
        v
  JVM looks for: public static void main(String[] args)
        |
        v
  runs the body
        |
        v
  Hello, Java.
```

If `main` is misspelled `mian`, the class may compile but the JVM will say it cannot find the main method. That error is now readable to you.

---

## 5. Syntax / Concept — every piece, slowly

We will walk **one token at a time**. Your job is understanding, not reciting.

```java
public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("Hello, Java.");
    }
}
```

### `public` (on the class)

"This class is visible to other code and to the JVM launcher." Beginners: treat it as **the normal way to start**.

### `class`

"What follows is a **class** — a named blueprint." Java programs live in classes (classic model).

### `HelloWorld`

The **name**. You chose it. Style: Class names use `PascalCase` (each word capitalized). `HelloWorld` not `helloWorld` for the class.

### `{` `}`

**Braces** mark a block: "this stuff belongs together." The class block wraps `main`. The method block wraps the print.

Every `{` needs a matching `}`. IntelliJ helps. Count them if you get lost.

### `public` (on `main`)

The JVM must be allowed to call `main` from outside your class. So `main` is public.

### `static`

"This method belongs to the class itself, not to one object instance." The JVM has not built a `new HelloWorld()` yet when it starts. It calls `main` without an object. `static` means "callable without `new`."

If this paragraph is foggy: **for Hello World, `static` is required on `main`. We will earn objects in Level 3.**

### `void`

"This method does not return a value to the caller." `main` does work (printing). It does not give back a number.

### `main`

The **name the launcher looks for**. Do not rename it if you want `java HelloWorld` to start here.

### `(String[] args)`

**Parameters:** input from the command line, as an array of text pieces.

```text
java HelloWorld red blue
```

Then `args[0]` is `red`, `args[1]` is `blue`. You may ignore `args` today. You still must write the parameter list the JVM expects (classic form).

`String` = text. `[]` = array (a row of them). `args` = a conventional name (arguments). You could name it `arguments`, but teammates expect `args`.

### `System.out.println`

- `System` — a library class
- `out` — the standard output stream (usually the Run window / terminal)
- `println` — print a line, then move to the next line

The dots mean "look inside." `System` has `out` has `println`.

### `"Hello, Java."`

A **string literal**: text data. The quotes are Java's quotes, not Word's curly quotes.

### `;`

End of this statement. Java uses semicolons like a period in a sentence — but you still need braces for blocks.

### Indentation

Spaces at the start of lines are for **humans**. Java mostly cares about braces, not Python-style indentation. Indent anyway. Unindented code is legal and cruel.

### Compact form (recognition, JDK 21+/25)

You may see:

```java
void main() {
    System.out.println("Hello, Java.");
}
```

This is a **modern compact source** style. Fine for tiny experiments. This course still wants you fluent in `public class` + `public static void main(String[] args)` because work code and interviewers use it.

---

## 6. Simple Example

Create `src/HelloWorld.java` in `java-learning`.

Type this **by hand**:

```java
public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("Hello, Java.");
    }
}
```

Run it. Confirm the Run window shows:

```text
Hello, Java.
```

Change the text to your name, run again. You just did **edit → compile → run**. That is the whole job, in miniature.

---

## 7. Real-World Example

Banks do not print Hello World in production. They still have a start method somewhere (a `main`, a framework starter, a test runner).

A tiny "who am I" utility a support engineer might run:

```java
public class WhoAmI {

    public static void main(String[] args) {
        System.out.println("User: " + System.getProperty("user.name"));
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("Java: " + System.getProperty("java.version"));
    }
}
```

E-commerce: a one-class `PriceTag` you run to check rounding before the big app exists.

```java
public class PriceTag {

    public static void main(String[] args) {
        double price = 19.99;
        System.out.println("Price: " + price);
    }
}
```

Same skeleton as Hello World. Different `println`. **You already know the skeleton.**

---

## 8. SDET Example

A tester's first "framework" is still `main` plus an expected/actual print.

```java
public class HelloWorldTestIdea {

    public static void main(String[] args) {
        String actual = "Hello, Java.";
        String expected = "Hello, Java.";

        if (actual.equals(expected)) {
            System.out.println("PASS: greeting text");
        } else {
            System.out.println("FAIL: greeting text");
            System.out.println("expected=" + expected);
            System.out.println("actual=" + actual);
        }
    }
}
```

Later JUnit will call methods for you, and you will not always write `main`. **Someone's `main` (the test runner) still exists.** You just stop seeing it.

---

## 9. Break the Code

**Break 1 — typo in main**

```java
public class HelloWorld {
    public static void mian(String[] args) {
        System.out.println("Hello, Java.");
    }
}
```

**Break 2 — missing braces**

```java
public class HelloWorld {
    public static void main(String[] args)
        System.out.println("Hello, Java.");
}
```

**Break 3 — class/file mismatch**

File: `HelloWorld.java`

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello, Java.");
    }
}
```

**Predict** the failure mode for each: compile vs "no main" vs file name.

---

## 10. Debug

**Break 1:** Compiles. Run says it cannot find `main`. Fix: `mian` → `main`.

**Break 2:** Compile error, `{` expected. Fix: add `{` after `)` and matching `}`.

**Break 3:** Compiler: public class `Hello` should be in `Hello.java`. Fix: rename class or file.

**Output wrong:** you ran a different class. Look at the run configuration name.

**Nothing happens:** you ran the project but the Run window is hidden. Open it.

**Course debug path:** problem (no output) → understand (which class ran?) → ... do not reinstall the JDK as step 1.

---

## 11. Student Exercise

1. Type `HelloWorld` yourself. No paste.
2. Run it.
3. In your notebook, explain in your own words: `class`, `main`, `static` (even if incomplete), `void`, `String[] args`, `println`, `;`.
4. Write a second class `GreetUser` that prints two lines.
5. Intentionally break `main`'s name, run, read the error, fix it.

---

## 12. Challenge

Add command-line arguments:

```java
public class HelloArgs {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Hello, stranger.");
        } else {
            System.out.println("Hello, " + args[0] + ".");
        }
    }
}
```

In IntelliJ: Run → Edit Configurations → Program arguments → type a name. Run again.

Then write how this is like a user providing **input** (Chapter 1).

---

## 13. Knowledge Check

1. Why must the public class name match the file name?
2. What is `main`'s job?
3. Why is `main` `static` in the beginner explanation?
4. What does `void` mean here?
5. What is `args`?
6. What do the dots in `System.out.println` suggest?
7. True or false: you must memorize Hello World this week or fail the course.
8. What happens if you name the method `mian`?
9. Why does this course still teach classic `main` on JDK 25?
10. Which character ends the `println` statement?

---

## 14. Interview Question

**Question:** "Explain the Hello World program line by line."

**Strong answer (spoken, not recited like a robot):**

> "The file HelloWorld.java declares a public class HelloWorld. The JVM starts at public static void main(String[] args). public so it can be called from outside, static so it can run without creating an object first, void because it doesn't return a value, and args holds optional command-line text. System.out.println writes a line to standard output. Braces group the class and method bodies. I don't treat it as magic; each piece has a job."

If you forget `static`'s deep meaning, say so and give the practical reason. Honesty plus the practical reason is better than a fake definition.

---

## 15. Homework

1. Finish the exercise including the intentional `mian` bug.
2. Draw the skeleton with arrows to your explanations.
3. Chapter 16: comments — how we talk to the next human, including future you.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. Java's rule for public classes; tools expect that pairing.
2. Entry point the launcher calls.
3. So it can run before any object exists; the JVM calls it on the class.
4. No return value.
5. Array of command-line argument strings.
6. Navigation: System contains out contains println.
7. False. Understand jobs; memory comes from use.
8. Compiles; runtime cannot find main.
9. Most professional and interview code still uses it; compact form is extra.
10. `;`

</details>
