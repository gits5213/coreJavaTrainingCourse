# Java Written Exam

**Level:** beginner through intermediate  
**Format:** closed book, paper or instructor-provided sheet  
**Timebox:** 45–60 minutes  
**Tools:** pen, paper. No IntelliJ, no notes, no internet.

This exam checks mental models. Handwriting is not graded. Ideas are.

Suggested weight in the [final exam](final-exam-guide.md): **10%**.

---

## Instructions

- Answer in complete sentences unless a diagram or a short identifier is enough.
- When you are asked to trace code, show the printed lines in order.
- If you do not know, write "I do not know yet" rather than a guess that contradicts the course.

---

## Part A — How Java works (10 points)

1. Draw `.java` → bytecode → JVM. Label `javac` and `java`.
2. What is bytecode?
3. JDK vs JVM in one line each. Which JDK does this course use?
4. Why can the same `.class` file run on Windows and macOS (assuming a JVM exists on each)?

## Part B — Language basics (20 points)

5. Declare an `int` named `expectedStatus` with value `200`, and a `String` named `username` with value `"aisha"`.
6. Primitive vs reference type: one example each.
7. Why is `String` immutable? What is wrong with `name.toLowerCase();` as the only line?
8. When `==` vs `.equals()`?
9. Write a boolean expression: status is 200 **and** the message equals `"OK"` (message is a `String`).
10. Trace this loop. What prints?

```java
for (int i = 1; i <= 3; i++) {
    System.out.println("Test " + i);
}
```

11. What is the difference between `while` and `do-while`?
12. Can you nest a method inside `main`? What is `void`?

## Part C — OOP and collections (20 points)

13. Class vs object, with a `LoginPage` example.
14. What is encapsulation? Why private fields?
15. Inheritance vs composition (`is-a` / `has-a`).
16. Polymorphism in one sentence using `WebDriver` or `Browser`.
17. List vs Set vs Map: pick one for (a) ordered test users, (b) unique order ids, (c) HTTP headers.
18. Why `List<Integer>` and not `List<int>`?

## Part D — Exceptions, Maven, HTTP, waits (20 points)

19. Checked vs unchecked: one example each. Why is empty `catch (Exception e) {}` forbidden in tests?
20. Maven: where do test classes live? What does `mvn test` do?
21. HTTP: 401 vs 403 vs 404.
22. Why is `Thread.sleep(5000)` a bad default in UI tests? What do you wait for instead?

## Part E — Short design (10 points)

23. A teammate wants a `LoginPage` that also queries the database, sends email, and parses JSON. Which SOLID letter is in trouble, and what would you split out?
24. Write the method signature and body of `statusMatches(int expected, int actual)` from memory.

---

## Scoring

| Part | Points |
| --- | --- |
| A | 10 |
| B | 20 |
| C | 20 |
| D | 20 |
| E | 10 |
| **Total** | **80** (scale to 100 if needed: score × 1.25) |

Pass guideline for this paper alone: **70%**. In the final battery you still cannot skip live coding.

---

## Answer key

1. `File.java` → `javac` → `File.class` (bytecode) → `java` / JVM executes.
2. Portable JVM instructions in `.class` files; not native CPU code for one OS.
3. JDK: compiler + tools + libraries (this course: **25 LTS**). JVM: engine that runs bytecode.
4. Each OS has a JVM that executes the same bytecode.
5. `int expectedStatus = 200;` `String username = "aisha";`
6. Primitive: `int` / `boolean`. Reference: `String` / `User`.
7. Methods return new strings. Without assignment, `toLowerCase()` does not change `name`.
8. `==` for `int` status codes; `.equals` for String content.
9. `status == 200 && message.equals("OK")` (or `equalsIgnoreCase` if specified).
10. `Test 1` then `Test 2` then `Test 3`.
11. `do-while` runs the body at least once, then checks. `while` checks first.
12. No nested methods. `void` means no return value.
13. Class is the blueprint; `new LoginPage(driver)` is one object for one session.
14. Hide data; expose safe operations. Callers should not set `username = ""` on a public field.
15. Inheritance: LoginPage is-a BasePage (use carefully). Composition: page has-a WebDriver / component.
16. `WebDriver driver = factory.create(type); driver.get(url);` works for Chrome or Firefox.
17. (a) List (b) Set (c) Map.
18. Collections hold objects; autoboxing uses `Integer`.
19. Checked: `IOException`. Unchecked: `NullPointerException`. Empty catch hides failures; tests lie.
20. `src/test/java`. Maven compiles tests and runs the test plugin (Surefire).
21. 401: not authenticated. 403: not authorized. 404: not found.
22. Sleep is slow and still racy. Wait until clickable/visible/URL — state-based synchronization.
23. Single Responsibility. Split `UserApiClient`, mail sender, JSON mapper; page keeps locators and UI actions.
24. `public static boolean statusMatches(int expected, int actual) { return expected == actual; }`
