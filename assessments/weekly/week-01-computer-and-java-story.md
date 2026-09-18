# Week 1 — Computer, Programming, and the Java Story

**Coverage:** Parts 1–4 (computer, programming, Java history, how Java works, JDK/JRE/JVM, IntelliJ, first program, comments)

**Suggested timebox:** 70 minutes

| Activity | Time |
| --- | --- |
| Quiz (closed book) | 15 minutes |
| Coding assignment | 25 minutes |
| Debugging problem | 15 minutes |
| Explanation exercise | 10 minutes |
| Buffer / review | 5 minutes |

Score using the course weights: Theory 20%, Coding 35%, Problem Solving 20%, Debugging 15%, Explanation 10%. Pass bar: 70% weighted. Coding cannot be skipped.

---

## Quiz (10 questions)

Answer in complete sentences unless a short phrase is enough. Closed book.

1. In one sentence, what does a computer actually do with a program?
2. What is programming, if it is not "talking to the computer in English"?
3. Why do programming languages exist? Why not give the chip English instructions?
4. Java started under another name at Sun Microsystems. What was that name, and who led the work?
5. What problem was Java created to solve? Use the phrase the course uses, or say it in your own accurate words.
6. Draw or list the pipeline from a `.java` file to a running program. Name the compiler command and the launcher command.
7. What is bytecode, and why does it make "write once, run anywhere" possible?
8. Distinguish JDK, JRE, and JVM in one line each. Which one did this course ask you to install, and which version?
9. What is an IDE, and why does this course start with IntelliJ instead of Notepad plus a terminal only?
10. Name the three comment styles in Java and give one honest use of comments (and one use to avoid).

---

## Coding assignment

**Title:** First program with a clear comment

### Requirements

1. In IntelliJ, in your `java-learning` project (or an equivalent), create a class named `WeekOneHello`.
2. The class must contain a `public static void main(String[] args)` method.
3. The program must print **exactly three lines**:
   - your first name
   - the text `JDK 25`
   - the text `Hello, SDET`
4. Add a short class-level comment (Javadoc or a block comment) that states what the program does. Do not narrate `System.out.println` line by line.
5. Run it from IntelliJ. Confirm `java -version` and `javac -version` still show 25 if you have not checked this week.

### Acceptance criteria

- [ ] The file compiles and runs without errors.
- [ ] Output is three lines, in the order above.
- [ ] `main` is spelled correctly (`String[] args`).
- [ ] There is a useful comment, not `// print name`.
- [ ] Class name matches the file name (`WeekOneHello.java`).

### Problem-solving stretch (counts toward Problem Solving 20%)

On paper, write the algorithm for "print name, JDK line, greeting" as numbered steps *before* you typed Java. Then write one sentence: where does the JVM enter that story?

---

## Debugging problem

A student pasted this into IntelliJ and clicked Run. It does not compile.

```java
public class WeekOneHello {
    public static void Main(String[] args) {
        System.out.println("Hello, SDET")
    }
}
```

### Your job

1. List every compile error you expect (there is more than one).
2. Say which error you would read first, and why.
3. Write the corrected class.

### Expected diagnosis

| Symptom | Cause |
| --- | --- |
| `Main` is not found as an entry point, or Run does not start the program as expected | Java looks for `main`, lowercase. `Main` is a different method. |
| `; expected` near `println` | Missing semicolon at the end of the statement. |

**Minimal fix:** rename `Main` to `main`, add the semicolon. Do not rewrite the class into something unrelated.

If the student also broke the class/file name, mention that a public class name must match the file name.

---

## Explanation exercise

**Prompt (spoken 60–90 seconds, or a short written paragraph):**

> When you press Run in IntelliJ, what actually happens to your `.java` file? Include source, `javac`, bytecode, JVM, and why the same `.class` can run on another operating system.

**Strong answer hits:**

- You type source in a `.java` file.
- The compiler (`javac`) produces bytecode in a `.class` file.
- The JVM loads and runs that bytecode (interpret / JIT are optional extra detail).
- Bytecode is not Windows machine code or macOS machine code; the local JVM translates it. That is portability.

Grade on accuracy first, then clarity to a non-developer (a "recipe book vs kitchen" or "PDF vs PDF reader" analogy is welcome if it stays accurate).

---

## Answer key

1. It follows exact instructions. It does not guess what a human meant.
2. Programming is writing those exact instructions in a form a machine (through a language and tools) can execute.
3. English is ambiguous. Chips need precise steps. Languages sit between human plans and machine instructions.
4. Oak; James Gosling (Sun Microsystems, public around 1995).
5. Write a program once, run it on many kinds of machines (Write Once, Run Anywhere), via bytecode and a JVM.
6. `Hello.java` → `javac` → `Hello.class` (bytecode) → `java` (JVM runs it).
7. Bytecode is the portable instruction format in `.class` files. It is not native code for one CPU. Each OS has a JVM that executes it.
8. JVM: engine that runs bytecode. JRE: runtime to run Java programs (historically; modern JDKs bundle what you need to run). JDK: development kit — compiler, launcher, libraries. This course: install **JDK 25 LTS**.
9. An IDE is an editor plus compiler, runner, debugger, and project tools. IntelliJ is the shop floor for later JUnit and Selenium work; Notepad hides those skills.
10. `//` single-line, `/* */` multi-line, `/** */` Javadoc. Use comments for *why* or for a short class purpose. Avoid restating the next line of code.

**Debugging key:** `main` not `Main`; missing semicolon.

**Explanation key:** source → compile → bytecode → JVM; portability is the JVM, not a separate `.exe` per OS as the first design.
