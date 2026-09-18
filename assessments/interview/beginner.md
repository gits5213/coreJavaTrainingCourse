# Beginner Interview

Questions from Parts 1–10 of the curriculum. Study the **model answers**, then speak them in your own words. Do not memorize as a script.

**How to practice:** 10 seconds think, 60–90 seconds speak, then check the model. Add one concrete example each time.

---

### What is a computer, in the sense this course uses?

**Model answer:** A computer follows exact instructions. It does not infer what a human meant. If the steps are wrong, the result is wrong in a precise way. Testers live in the gap between what a person asked for and what those steps actually did.

---

### What is programming?

**Model answer:** Programming is writing those exact instructions in a language a machine can execute through tools. An algorithm is the plan we write before Java. The course rule is: problem → understand → algorithm → Java → run → debug.

---

### Why do programming languages exist?

**Model answer:** English is ambiguous. Hardware needs precise steps. A programming language sits between a human plan and the instructions a compiler and runtime can run. We do not talk to the chip in English.

---

### What is Java?

**Model answer:** Java is a programming language and a platform. We write `.java` source, compile to bytecode, and a JVM runs it. It was created so we could write a program once and run it on many operating systems. Banks, server applications, and a lot of SDET tooling use it. Selenium, Rest Assured, and JUnit run *on* Java; they are not a replacement for Java.

---

### Why was Java created? Who was involved?

**Model answer:** It started as Oak at Sun Microsystems, led by James Gosling, and became public around 1995. C and C++ binaries were tied to specific hardware. The goal was Write Once, Run Anywhere: compile to bytecode, run on a JVM on each machine. Memory management is also safer than manual C pointers for many application teams.

---

### What is bytecode?

**Model answer:** Bytecode is the portable instruction format stored in `.class` files. It is not machine code for one CPU. The JVM interprets it and can JIT-compile hot parts. That is why the same compiled program can run on Windows, macOS, and Linux.

---

### What is the JVM?

**Model answer:** The Java Virtual Machine is the engine that loads bytecode and executes it. Picture: `.class` file → class loader → JVM → CPU. It is not the same word as JDK. You can install a JDK and still be running a specific JVM version; when a test "does not run," the cause is often the wrong JDK, code that did not compile, or a class not on the classpath — not Selenium.

---

### What is the JDK? What about the JRE?

**Model answer:** The JDK is the Java Development Kit: `javac`, the `java` launcher, libraries, and tools. This course installs **JDK 25 LTS**. Historically the JRE was a smaller runtime-only bundle. In modern practice developers install a JDK. The JVM is the engine inside the runtime you actually launch.

---

### What happens when you press Run?

**Model answer:** IntelliJ (or `javac`) compiles `Hello.java` into `Hello.class` bytecode. The `java` launcher starts a JVM, which loads the class, verifies bytecode, and runs `main`. Later, a JIT may speed up hot methods, and garbage collection reclaims unused objects. I do not need to be a JVM engineer to draw that pipeline correctly.

---

### What is an IDE, and why IntelliJ?

**Model answer:** An IDE is an editor plus compiler, runner, debugger, and project navigation. Notepad can write text; it will not show stack traces, breakpoints, or Maven well. Our future test framework is an IntelliJ project. Learning the shop floor now means JUnit and Selenium later are not twice as hard.

---

### What is a variable?

**Model answer:** A named box with a type and a value. `int expected = 200;` stores a number I can compare later. Tests are full of expected versus actual. If I cannot store those two values, I cannot decide PASS or FAIL.

---

### Primitive type versus reference type?

**Model answer:** Primitives (`int`, `boolean`, `double`, `char`) hold the value in the variable. Reference types (`String`, `WebDriver`, `User`) hold a pointer to an object on the heap. `==` on references is identity. For `String` content I use `equals`. Status codes stored as `int` use `==`.

---

### Why is `String` immutable?

**Model answer:** Methods like `trim()` and `toUpperCase()` return a new `String`. They do not change the original unless I assign the result. If I write `message.trim();` and then assert on `message`, the spaces are still there. That bug shows up constantly in UI and API text.

---

### `==` versus `.equals()`?

**Model answer:** `==` compares primitive values, or whether two references point at the same object. `.equals()` is content equality when the class defines it — always for `String`. I compare status codes with `==`. I compare usernames and error messages with `equals` or `equalsIgnoreCase`. Relying on interned literals with `==` on strings is a trap.

---

### Arithmetic, comparison, and logical operators?

**Model answer:** Arithmetic (`+ - * / %`) calculates. Comparison (`== != > < >= <=`) asks questions. Logical (`&& || !`) combines true/false. A typical check is `actual == 200 && responseTimeMs < 2000`.

---

### `if` versus `switch`?

**Model answer:** `if` is for general conditions: numbers, `equals` on strings, combined booleans. `if/else` is two paths (PASS/FAIL). `else if` is many doors in order (200, 404, 500, else). `switch` picks among discrete values of one variable — status families or a browser enum. This course taught traditional `switch` with `break` first so I can read older code.

---

### `for` versus `while`? What about `break` and `continue`?

**Model answer:** `for` when I know the count or I walk a range. `while` when I repeat until a condition changes, such as retries with a limit. `do-while` runs at least once. Every loop needs a way to finish or the program looks frozen. `break` leaves the loop; `continue` skips the rest of this round. For Selenium, a raw `while` plus sleep is still a poor wait; that comes later.

---

### What is a method? What is `void`? What is overloading?

**Model answer:** A method is a named recipe. I write the steps once and call the name. I cannot declare a method inside `main`. `void` means it does not return a value. Overloading is the same name with different parameter lists. SDET frameworks are mountains of methods: `login`, `click`, `statusMatches`. If login steps change, I want one place to fix them, not twenty copied tests.

---

### Write `statusMatches` and say how you would test it.

**Model answer:**

```java
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
}
```

I would assert `statusMatches(200, 200)` is true and `statusMatches(200, 404)` is false, with JUnit or a small `main`. If they differ, I print expected and actual once each. I would not start a framework for this method.

---

### Why comments? What do you avoid?

**Model answer:** Comments are notes for humans: `//`, `/* */`, and Javadoc `/** */`. I explain why, or the purpose of a class. I do not write `// increment i` on `i++`.

---

## Extra practice set (answer yourself, then peek)

1. Oak, Gosling, Sun, ~1995 — can you say it in 20 seconds?
2. Draw source → `javac` → bytecode → JVM without looking.
3. JDK 25 — can you check `java -version` on your machine?
4. `&&` versus `||` with a status code example.
5. Why nested methods in `main` fail to compile.
