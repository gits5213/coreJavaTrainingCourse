# Chapter 7 — Source Code: The `.java` File

## 1. Today's Goal

By the end of this lesson, you will know what **source code** is:

> Source code is the human-written text of a program. In Java, we usually save it in a file ending with **`.java`**.

You will meet a file called `Hello.java`, and you will understand that it is **not** what the CPU runs directly. It is what *you* write, read, review, and put in Git.

---

## 2. Why It Matters

If you do not know which file is the source, you will edit the wrong thing, commit the wrong thing, or panic when IntelliJ shows `Hello` and `Hello.class`.

SDETs live in source: page objects, test classes, utilities. Bytecode is a byproduct. Interviews ask "what is source code?" to see if you know the difference between **what humans maintain** and **what the machine executes**.

---

## 3. Real-Life Analogy

Source code is a **recipe card written in ink**.

```
  Recipe card (Hello.java)
  "Mix 2 cups flour..."
           |
           |  (later chapters: cook / compiler)
           v
  Baked cake (the running program)
```

- You fix a cake by changing the **card**, not by whispering at the baked slice.
- The card must follow a language (Java), like a recipe must use amounts, not vibes.
- Many bakers can read the same card. That is why we share `.java` files, not mystery binaries, in this course.

`.class` files are more like a **pre-measured ingredient kit** the oven understands. Useful. Not where you write comments for teammates.

---

## 4. Illustrated Explanation

### A project is folders of source

```
  java-learning/
    src/
      Hello.java          <-- SOURCE  (you type this)
      BankTransfer.java   <-- SOURCE
    out/  or  build/
      Hello.class         <-- NOT source (produced later)
```

### What is inside source?

```
  +--------------------------------------+
  | Hello.java   (plain text)            |
  |                                      |
  |  keywords   public class ...         |
  |  names      Hello, main              |
  |  punctuation { } ( ) ; " "           |
  |  comments   // notes for humans      |
  +--------------------------------------+
           |
           |  still just text, like a .txt file with rules
           v
  You can open it in IntelliJ, Notepad, or print it.
```

If you renamed `Hello.java` to `Hello.txt`, a human could still read it. The **compiler** looks at the `.java` ending and the text rules to decide if it is a Java compilation unit.

### Source vs what you see on screen when it runs

```
  SOURCE TEXT                     RUNTIME OUTPUT
  System.out.println("Hi");  -->  Hi
```

Beginners mix these up and say "the source is Hi." No. The source is the instruction to print. **Hi** is output.

---

## 5. Syntax / Concept

| Word | Meaning |
|------|---------|
| **Source code** | The program as written by a human. |
| **`.java` file** | A typical Java source file. |
| **Compilation unit** | One source file the compiler reads (usually one public class matching the file name). |
| **Identifier** | A name you choose (`Hello`, `amount`). |
| **Keyword** | A word Java already owns (`public`, `class`, `if`). |
| **Statement** | An instruction, often ending with `;`. |

**File name rule you should know early:** if you declare `public class Hello`, the file should be `Hello.java`. Mismatch is a classic first-week error.

The source is encoded as text (almost always UTF-8 in modern tools). Emoji in comments is allowed; emoji as a class name is a bad idea.

---

## 6. Simple Example

Create this as source (in Part 4). File name: `Hello.java`.

```java
public class Hello {

    public static void main(String[] args) {
        System.out.println("Hello, source code.");
    }
}
```

What is source here?

- Every character in that box.
- Including spaces and braces.
- Including the string `"Hello, source code."` — the quotes and letters are source; the printed line is output after running.

A `.java` file is **plain text**. It is not a picture. It is not a Word document. If Word wraps funny quotes (`“ ”`) into your code, the compiler will hate you. IntelliJ is safer.

---

## 7. Real-World Example

A shop keeps user registration rules in source so the team can review them.

```java
public class RegisterUser {

    public static void main(String[] args) {
        String email = "user@shop.com";
        String password = "long-enough-password";

        boolean emailOk = email.contains("@");
        boolean passwordOk = password.length() >= 8;

        if (emailOk && passwordOk) {
            System.out.println("Account can be created.");
        } else {
            System.out.println("Registration rejected.");
        }
    }
}
```

The **source** is the policy, written down. If the business later says "password must be 12 characters," you change the source (`12` instead of `8`), then compile and run again. You do not hex-edit a `.class` file with a tutorial from 2004.

Banks treat source as an **audit artifact**: Git history shows who changed interest rounding.

---

## 8. SDET Example

Test source is still source. File: `LoginTestIdea.java`.

```java
public class LoginTestIdea {

    public static void main(String[] args) {
        String actualMessage = "Invalid password";
        String expectedMessage = "Invalid password";

        if (actualMessage.equals(expectedMessage)) {
            System.out.println("TEST PASS");
        } else {
            System.out.println("TEST FAIL");
        }
    }
}
```

SDET habits with source:

- Name files after behavior (`LoginTest`, not `Stuff2`).
- Put tests in source control with the product, or in a dedicated test repo — but **as source**.
- Do not commit secrets (real passwords) in source. Use fake users in examples.

---

## 9. Break the Code

File is saved as `Hello.java` but the public class has a different name.

```java
public class Hullo {

    public static void main(String[] args) {
        System.out.println("This class name does not match the file name.");
    }
}
```

**Predict:** What does the compiler say? (You will see the real message in Part 4; guess the *idea*.)

Another source disaster:

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println(“Hello curly quotes”);
    }
}
```

Those quotes may look pretty. They are not Java's `"` character.

---

## 10. Debug

**Symptom 1:** `class Hullo is public, should be declared in a file named Hullo.java`

**Fix:** Rename the file to `Hullo.java`, or rename the class to `Hello` to match `Hello.java`. Pick one public class per file as a beginner rule.

**Symptom 2:** strange errors on a `println` line that "looks fine."

**Fix:** Delete fancy quotes. Re-type `"` in IntelliJ. Never draft Java in a phone email.

**Debug question for source problems:** "Is the text actually Java characters in a `.java` file whose name matches the public class?"

---

## 11. Student Exercise

On paper, write `Hello.java` in full. Circle:

- the file name you would use
- the class name
- the line that is an instruction to produce output
- a piece that is **not** output yet (the source of the message)

Then write two sentences: "Source is for humans. Bytecode is for the JVM."

---

## 12. Challenge

An e-commerce team has `Price.java` (source) and `Price.class` (compiled). A bug shows prices without tax.

Who should you edit, and why? Write a paragraph. Include what happens if someone emails only the `.class` file to a tester with no source.

---

## 13. Knowledge Check

1. What is source code?
2. What suffix do Java source files usually have?
3. Is `Hello.class` source?
4. Why should a public class name match the file name?
5. True or false: source is a picture of the running window.
6. Why is Microsoft Word a bad Java editor?
7. What should SDETs put in Git: source tests or only compiled tests?
8. If business rules change, what do we change first?
9. Name two things that live in a `.java` file besides keywords.
10. What is the file name for `public class RegisterUser`?

---

## 14. Interview Question

**Question:** "What is the difference between source code and bytecode?"

**Strong answer:**

> "Source code is the Java text humans write and maintain, usually in .java files. A compiler turns that into bytecode in .class files, which the JVM runs. We debug and review source. Bytecode is an intermediate form for the machine side of Java."

Chapter 8 deepens bytecode. This answer is already hireable.

---

## 15. Homework

1. If you already have a computer notes folder, create a fake listing: `Hello.java`, `Hello.class`, and write which one you would open to change a message.
2. Practice typing the `Hello` class once slowly. Speed is not the goal. Character accuracy is.
3. Read Chapter 8: compilation.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. The human-written text of the program.
2. `.java`
3. No. It is compiled bytecode (usually).
4. Java requires that pairing for public classes; tools and compilers expect it.
5. False.
6. It inserts formatting and fancy quotes that are not Java.
7. Source tests.
8. The source (then recompile).
9. Names, comments, punctuation, literals/strings, etc.
10. `RegisterUser.java`

</details>
