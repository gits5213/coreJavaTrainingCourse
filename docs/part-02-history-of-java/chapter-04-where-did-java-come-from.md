# Chapter 4 — Where Did Java Come From?

## 1. Today's Goal

By the end of this lesson, you will tell Java's origin story in six beats:

1. It started as a language called **Oak**.
2. **James Gosling** led the work.
3. The company was **Sun Microsystems**.
4. The public Java moment is **1995**.
5. The big promise was **Write Once, Run Anywhere (WORA)**.
6. That promise is kept (mostly) by compiling to **bytecode** and running on a **JVM**.

You do not need dates beyond 1995 for interviews. You need the *problem they were solving*.

---

## 2. Why It Matters

Tools without history feel arbitrary. Students memorize "JVM" as a vocabulary word and cannot answer:

> "Why does my test run on my Mac and also on the Linux machine in CI?"

Because a group of engineers in the 1990s were tired of rewriting software for every device and every operating system. They bet on a **virtual machine**.

When an SDET job says "Java + Selenium," you are standing on that bet. Your `.class` files are portable in a way a raw Windows `.exe` often is not.

---

## 3. Real-Life Analogy

Imagine you write a cookbook.

**The old way:** you must rewrite the cookbook for every kitchen.

```
  Gas stove kitchen   →  rewrite every recipe
  Electric kitchen    →  rewrite every recipe
  Campfire            →  rewrite every recipe
```

**The Java way:** you write the cookbook once in a **standard recipe language**. Each kitchen hires a **translator cook** who knows that language *and* the local stove.

```
  Your cookbook (Java source)
           |
           v
  Standard cards (bytecode)
      /         |         \
     v          v          v
  Translator  Translator  Translator
  (JVM on     (JVM on     (JVM on
   Windows)    macOS)      Linux)
```

The translator cook is the **Java Virtual Machine**. The standard cards are **bytecode**. The cookbook is **your `.java` file**.

"Write Once, Run Anywhere" means: stop rewriting the cookbook for every stove. Train the kitchens instead.

---

## 4. Illustrated Explanation

### The problem in the early 1990s

Software was exploding. Chips and gadgets were multiplying. C and C++ were powerful but **tied to compiling for each platform**. Write a program for one operating system, and another OS often needed a painful port.

Sun Microsystems was a company famous for servers and networking. A small team, including **James Gosling**, worked on language ideas for devices that might be everywhere — not only desktop PCs. The language was nicknamed **Oak** (there was an oak tree outside Gosling's office; this is the kind of detail you can forget and still be employed).

Oak became **Java**. In **1995**, Java was shown to the world in a big way, riding the new web: applets in browsers were the demo that made people stare. Applets later faded. The **language + JVM idea** did not.

### Timeline cartoon (not a museum tour)

```
  early 1990s     Oak at Sun
       |
       v
     1995         Java announced / public wave
       |
       v
     WORA         bytecode + JVM as the portability trick
       |
       v
     2000s+       servers, phones (Android uses a related VM story),
                  enterprise, and YES: test automation
```

### Write Once, Run Anywhere — honest version

```
  YOUR SOURCE          COMPILER           BYTECODE         YOUR OS
  Hello.java   ---->   javac      ---->   Hello.class -->  JVM --> CPU
```

WORA is a **goal**, not a magic spell.

- The **same bytecode** can run on any machine that has a compatible JVM.
- You can still write code that accidentally depends on Windows file paths, or a font that exists only on one computer. Then WORA gets holes.
- SDETs learn this when a test passes locally and fails in CI because of a hardcoded `C:\` path.

So: **architecture enables portability. Discipline keeps it.**

---

## 5. Syntax / Concept

| Name | What to remember |
|------|------------------|
| **Oak** | Java's earlier name. |
| **James Gosling** | Often called the father of Java; led the language creation. |
| **Sun Microsystems** | The company that created Java. (Ownership later moved; that is Chapter 5.) |
| **1995** | The year to keep for the public Java story. |
| **WORA** | Write Once, Run Anywhere. |
| **Bytecode** | The portable instruction form stored in `.class` files. |
| **JVM** | The program that reads bytecode and runs it on this computer. |

You will still write:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, Java history!");
    }
}
```

Gosling's team wanted a language that felt familiar to C/C++ programmers (curly braces, classes) but safer (automatic memory management, no wild pointer arithmetic in everyday code).

That "safer C-like language for a VM" is the conceptual syntax of Java's birth.

---

## 6. Simple Example

A tiny program that *states* the origin story when it runs. History in output, not in trivia night.

```java
public class JavaOriginStory {

    public static void main(String[] args) {
        String oldName = "Oak";
        String creator = "James Gosling";
        String company = "Sun Microsystems";
        int publicYear = 1995;
        String promise = "Write Once, Run Anywhere";

        System.out.println("Earlier name: " + oldName);
        System.out.println("Led by: " + creator);
        System.out.println("Company: " + company);
        System.out.println("Public wave: " + publicYear);
        System.out.println("Promise: " + promise);
    }
}
```

When this runs on Windows or macOS, you do not rewrite it. That *is* the origin story, demonstrated.

---

## 7. Real-World Example

An e-commerce company in 1995–2005 might have had:

- cashiers on Windows PCs
- warehouses on Unix/Linux
- a website on yet another stack

Rewriting "calculate discount" three times is how discounts disagree and customers get angry.

```java
public class DiscountOnce {

    public static void main(String[] args) {
        double price = 80.00;
        boolean isMember = true;

        double discountRate = isMember ? 0.10 : 0.00;
        double finalPrice = price - (price * discountRate);

        System.out.println("Final price: " + finalPrice);
        // Same rule can run on the cashier JVM, the warehouse JVM, the server JVM.
    }
}
```

Banks loved this even more. Interest math that differs by operating system is a scandal. One language, many machines, one rule.

---

## 8. SDET Example

Your CI system is Linux. Your laptop is macOS. Your teammate is on Windows.

If tests were native machine code compiled only on your laptop, CI would be a different product.

```java
public class WoraForTests {

    public static void main(String[] args) {
        String os = System.getProperty("os.name");
        int expectedAdd = 2 + 2;
        int actualAdd = 4;

        System.out.println("This test JVM is running on: " + os);

        if (expectedAdd == actualAdd) {
            System.out.println("PASS: arithmetic is the same idea on every OS.");
        } else {
            System.out.println("FAIL: the universe is broken.");
        }
    }
}
```

`System.getProperty("os.name")` is input from the machine. The **assertion** is about the business rule. Good SDETs use the JVM's portability and still avoid OS-specific traps (paths, slashes, drivers).

---

## 9. Break the Code

A student "implements WORA" by hardcoding a Windows path in a test that must also run on a Mac CI agent.

```java
public class BrokenWoraPath {

    public static void main(String[] args) {
        // BUG: this only makes sense on some Windows machines
        String reportFile = "C:\\qa\\reports\\today.txt";

        System.out.println("Will save report to: " + reportFile);
        System.out.println("On macOS/Linux this path is usually nonsense.");
    }
}
```

The language is portable. The **instructions you wrote** are not.

**Predict:** Will this still *compile* on a Mac? Will it *mean* a real file location there?

---

## 10. Debug

**Compile?** Yes. Java will compile a string that contains `C:\...`. The compiler does not know your disk layout.

**Run?** It runs. It may print a path that does not exist. Later, when you try to write the file, it fails.

**Root cause:** You confused **language portability** with **your code's assumptions**.

**Fix idea:** use relative paths, user home, or a config value.

```java
public class FixedPortablePath {

    public static void main(String[] args) {
        String userHome = System.getProperty("user.home");
        String reportFile = userHome + "/qa-reports/today.txt";

        System.out.println("Portable-ish report location: " + reportFile);
        System.out.println("OS: " + System.getProperty("os.name"));
    }
}
```

(We will learn `Path` APIs later. The lesson today is conceptual.)

**WORA debugging question:** "Is this failing because Java cannot run here, or because I assumed a kitchen that is not this kitchen?"

---

## 11. Student Exercise

Write a one-page origin story in your own words with these headers:

- The problem before Java
- Oak / Gosling / Sun
- 1995
- WORA in the cookbook analogy
- One way a programmer can still break portability

Do not copy this chapter sentence-for-sentence. If you can only copy, you do not own it yet. Close the file and write from memory, then peek.

---

## 12. Challenge

A teammate says: "Java is portable, so our tests cannot fail on CI if they pass on my PC."

Write a short reply (6–10 sentences) that:

1. Agrees with the JVM part.
2. Gives two counterexamples (path is one; think of another: browser driver, screen size, missing env variable, test data file not committed).
3. Ends with what an SDET should do instead of believing in magic.

---

## 13. Knowledge Check

1. What was Java's earlier name?
2. Who is James Gosling in this story?
3. Which company created Java?
4. Which year should you keep for the public Java wave?
5. What does WORA stand for?
6. What two pieces make WORA possible in the Java design?
7. True or false: applets are the main reason people still learn Java in 2026.
8. True or false: any Java program is automatically portable no matter what you write.
9. Why do SDET suites like the JVM model?
10. Name one non-portability bug that is still "valid Java."

---

## 14. Interview Question

**Question:** "What is special about Java compared to compiling C for one operating system?"

**Strong answer:**

> "Java compiles to bytecode, which a JVM executes. The same bytecode can run on Windows, macOS, or Linux if a compatible JVM is installed. That was the Write Once, Run Anywhere idea from Sun and James Gosling in the 1990s. C programs are typically compiled to a specific platform. Java's portability still depends on not baking in OS-specific assumptions."

Keep Oak as optional flavor. Keep WORA and JVM as the meat.

---

## 15. Homework

1. Draw the cookbook / translator diagram from memory.
2. Run (in Part 4, when you can) a program that prints `os.name`. Write the result in your notes.
3. Read Chapter 5. It answers a confusing modern question: "If Sun made Java, why do people say Oracle? And what is OpenJDK?"

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. Oak.
2. The lead designer / "father of Java."
3. Sun Microsystems.
4. 1995.
5. Write Once, Run Anywhere.
6. Bytecode and the JVM (a VM on each platform).
7. False. Applets faded; servers, Android-related ecosystems, enterprise, and automation remain.
8. False. You can write non-portable instructions in a portable language.
9. Tests written once can run on developer machines and Linux CI with a JVM.
10. Hardcoded Windows paths, assuming a file exists, depending on a local browser, etc.

</details>
