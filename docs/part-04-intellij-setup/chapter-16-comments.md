# Chapter 16 — Comments: Notes for Humans

## 1. Today's Goal

By the end of this lesson, you will write three kinds of Java comments:

| Kind | Looks like |
|------|------------|
| **Single-line** | `// like this` |
| **Multi-line** | `/* like this */` |
| **Javadoc** | `/** like this */` above a class or method |

You will also know the most important comment rule:

> Explain **why** (or a warning) when the code cannot. Do not narrate **what** the next line already says.

---

## 2. Why It Matters

Code is read more than it is written. SDET suites live for years. A future teammate — or you in six months — will open a test and ask "Why do we wait for this popup?"

A good comment saves an incident. A noisy comment trains people to ignore comments, including the important ones.

Compilers mostly skip comments. They are not for the JVM. They are for **humans** (and for Javadoc tools).

---

## 3. Real-Life Analogy

Comments are **sticky notes on a recipe**.

```
  GOOD STICKY NOTE
  "Use the blue salt — the white one is sugar from a mislabeled jar."

  BAD STICKY NOTE
  "Now add salt."   (the next line already says Add 1 tsp salt)
```

The good note is **why**. The bad note is a robot reading the recipe aloud.

Javadoc is more like the **printed header** of a cookbook chapter: what this recipe is for, what it expects, what it returns — for people who do not want to reverse-engineer every pot.

---

## 4. Illustrated Explanation

```
  SOURCE FILE
  +--------------------------------------------------+
  | /** Javadoc: what this class is for */           |
  | public class RefundPolicy {                      |
  |     // single-line: why we use cents not dollars |
  |     int refundCents = 1500;                      |
  |                                                  |
  |     /*                                           |
  |      * multi-line: a short note that             |
  |      * needs more than one line                  |
  |      */                                          |
  |     public static void main(String[] args) {     |
  |         System.out.println(refundCents);         |
  |     }                                            |
  | }                                                |
  +--------------------------------------------------+
           |
           |  javac throws comments away (Javadoc can be extracted)
           v
  BYTECODE: the notes are gone from execution
```

Comments do not make the program faster or slower in any way you should care about today. They can make the team faster.

### Comments vs strings

```
  System.out.println("Hello");  // this prints. The words in quotes ARE data.
  // Hello                      // this does nothing at runtime.
```

Beginners comment out a line to "delete" it temporarily. That is a valid debug trick. Do not leave a museum of commented-out code in tests. Use Git history.

---

## 5. Syntax / Concept

### Single-line: `//`

Everything after `//` on that line is a comment.

```java
int quantity = 2; // number of pairs of shoes
```

You can also comment out a whole instruction:

```java
// System.out.println("temporary debug");
```

### Multi-line: `/* ... */`

Can span lines. Do not nest them (`/* /* */ */` gets confusing fast).

```java
/*
 * Temporary note:
 * Warehouse still uses old SKU list until Friday.
 */
```

### Javadoc: `/** ... */`

Goes **immediately above** a class, method, or field. Tools can turn it into HTML docs. Tags like `@param` and `@return` describe inputs and outputs.

```java
/**
 * Prints a greeting for a shop customer.
 *
 * @param name the customer's display name
 */
```

You do not need to Javadoc every `main` in this course. Learn the shape so you can read professional code.

### Good vs obvious

**Obvious (avoid):**

```java
// increment i by 1
i = i + 1;
```

**Useful:**

```java
// Retry once because the payment API returns 409 on the first duplicate click.
```

If you can rename a variable to kill the comment, **rename** (`numberOfShoes` instead of `n` plus a comment). That is the course rule's **refactor** step.

---

## 6. Simple Example

```java
/**
 * Tiny starter program that greets the class.
 * We use this to practice comments, not to greet production users.
 */
public class CommentDemo {

    public static void main(String[] args) {
        // TODO: later, read the name from args[0] if present
        String name = "class";

        System.out.println("Hello, " + name + ".");
    }
}
```

`TODO` is a conventional marker. IntelliJ lists TODOs. Do not lie with fake TODOs you will never do.

---

## 7. Real-World Example

Banking interest is a magnet for **why** comments.

```java
/**
 * Demonstrates a simple interest display.
 * Real banks use more precise decimal types; this is a teaching model.
 */
public class SimpleInterest {

    public static void main(String[] args) {
        double principal = 1000.00;

        // Product rule 2024-11: savings promo is 5% even if marketing says "up to 7%".
        // The 7% is a different product code, not this one.
        double rate = 0.05;

        double interest = principal * rate;
        System.out.println("Interest: " + interest);
    }
}
```

Without the comment, a new developer "fixes" the rate to 0.07 because a banner said so. That is how money bugs are born.

E-commerce shipping:

```java
public class ShippingComment {

    public static void main(String[] args) {
        double cart = 50.00;

        // Business: free shipping at exactly 50, not "more than 50".
        double shipping = cart >= 50.00 ? 0.00 : 7.99;

        System.out.println("Shipping: " + shipping);
    }
}
```

The `>=` is visible. The **business decision** is not visible without a sentence.

---

## 8. SDET Example

Tests need comments when the **scenario is weird**, not when the assertion is obvious.

**Noisy:**

```java
public class NoisyTestIdea {

    public static void main(String[] args) {
        // set expected to 200
        int expected = 200;
        // set actual to 200
        int actual = 200;
        // compare them
        if (expected == actual) {
            System.out.println("PASS");
        }
    }
}
```

**Useful:**

```java
public class UsefulTestIdea {

    public static void main(String[] args) {
        // Guest checkout uses HTTP 200 with body error="login_required"
        // Do not treat 200 as success without reading the body. Bug INC-4412.
        int status = 200;
        String bodyError = "login_required";

        boolean actuallyLoggedIn = status == 200 && bodyError == null;

        if (!actuallyLoggedIn) {
            System.out.println("PASS: we detected a fake success status.");
        } else {
            System.out.println("FAIL");
        }
    }
}
```

SDET comments often include **ticket numbers** and **traps** ("do not use sleep here; wait for the overlay").

---

## 9. Break the Code

**Break A — comment never ends**

```java
public class UnclosedComment {

    public static void main(String[] args) {
        /* I forgot to close this comment
        System.out.println("This line is still inside the comment.");
    }
}
```

**Break B — Javadoc in the wrong place does not explode, but helps no one**

A `/**` in the middle of a method is just a fancy multi-line comment. People think they documented an API. They did not.

**Break C — commenting out the closing brace**

```java
public class CommentedBrace {

    public static void main(String[] args) {
        System.out.println("Hello");
    //}
}
```

**Predict:** What does the compiler complain about in A and C?

---

## 10. Debug

**Unclosed `/*`:** The rest of the file is "eaten." Fix: close with `*/`. IntelliJ coloring often shows comments in a different color — if half your method is the comment color, you forgot to close.

**Missing `}`:** You commented out a brace. Look at brace matching (IntelliJ highlights pairs). Uncomment or add the brace back.

**"My print never runs":** it is inside a comment. Color is the clue.

**Stale comment:** the comment says 5% and the code says 0.07. **The comment is now a lie.** Delete or update it. Lying comments are worse than none.

---

## 11. Student Exercise

Take your `HelloWorld` and:

1. Add a Javadoc on the class: one sentence on what it is.
2. Add one `//` why-comment (for example, why you chose that greeting).
3. Add a `/* */` block that you then **remove** if it says nothing useful.
4. Find one comment you are tempted to write that only repeats the next line. Do not write it.

---

## 12. Challenge

Rewrite this class. Keep behavior. Remove bad comments. Add at most two good ones if needed. Rename if a name kills a comment.

```java
public class Cart {

    public static void main(String[] args) {
        // integer
        int a = 2;
        // price
        double b = 30.00;
        // multiply
        double c = a * b;
        // print
        System.out.println(c);
    }
}
```

Then write three sentences: what you renamed, what you deleted, whether a comment remains.

---

## 13. Knowledge Check

1. Name the three comment forms in this chapter.
2. Are comments executed by the JVM?
3. What is a good comment about?
4. Give an example of a bad obvious comment.
5. What is Javadoc for?
6. True or false: more comments always mean better code.
7. What is a `TODO` comment?
8. Why can a stale comment be dangerous in a bank?
9. How does IntelliJ help you spot an unclosed `/*`?
10. Should you keep 200 lines of commented-out old tests?

---

## 14. Interview Question

**Question:** "What makes a good comment?"

**Strong answer:**

> "A good comment explains intent, warnings, or business rules that the code cannot say clearly. A bad comment repeats the code or lies after the code changed. I prefer renaming variables and extracting methods over writing a paragraph. In tests I comment traps — like HTTP 200 that is not success — and ticket numbers when they help. Javadoc is for APIs other people call."

---

## 15. Homework

1. Finish the exercise and the challenge in `java-learning`.
2. Complete the Level 1 checklist in `docs/00-student-levels.md`.
3. Rest. The next parts of the course teach the Java language (types, decisions, loops). You now have a workshop and a mental pipeline.
4. Optional: reread the course rule and notice you already used it: fail on purpose (`mian`), debug, refactor comments.

```
problem → understand → algorithm → Java → code → run → fail
       → debug → refactor → test → improve → architect
```

You are no longer at "I have never seen a program." You are a Level 1 apprentice. Welcome.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. `//`, `/* */`, `/** */` (Javadoc).
2. No. They are stripped from execution (Javadoc may be extracted separately).
3. Why, warnings, non-obvious business rules, traps.
4. `// print the total` above `System.out.println(total);`
5. Documenting classes/methods for humans and doc tools (`@param`, `@return`, ...).
6. False. Noise trains people to skip comments.
7. A marker for unfinished work; use honestly.
8. People trust the comment and ship the wrong rate/rule.
9. Comment coloring / the rest of the file looks commented.
10. No. Use version control; deleted code can return.

</details>
