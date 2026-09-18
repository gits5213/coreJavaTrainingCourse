# Chapter 2 — What Is Programming?

## 1. Today's Goal

By the end of this lesson, you will be able to say what **programming** is in one honest sentence:

> Programming is writing **precise instructions** that a computer can follow, in an order that produces a result you can check.

You will also write an **algorithm** (a plan in ordinary language) for a login, and you will see how that plan becomes Java later.

---

## 2. Why It Matters

Beginners think programming is:

- memorizing curly braces
- being "good at math"
- typing fast
- knowing secret words

Professionals think programming is:

- understanding a problem
- breaking it into steps
- naming things clearly
- checking that the result is right

If you start with braces, you will panic when the braces are perfect and the *idea* is wrong. SDETs meet that disaster every week: a test that compiles, runs, and measures the wrong thing.

This chapter trains the part of your brain that comes **before** Java.

Remember the course rule:

```
problem → understand → algorithm → Java → code → run → fail → debug → ...
```

Today we live in the first three arrows.

---

## 3. Real-Life Analogy

Imagine you leave a note for a house-sitter:

> "Feed the cat."

A human might succeed. A computer-quality house-sitter needs:

```
  1. Open the left cupboard.
  2. Take the bag labeled "Cat food".
  3. Put 70 grams in the blue bowl.
  4. Put the bowl on the kitchen floor.
  5. If the bag is empty, send me a message. Do not use people food.
```

That second list is closer to **programming**.

- Vague goals are for humans.
- **Precise, ordered, complete** steps are for machines.
- Missing the "if the bag is empty" path is how pets (and payments) get hurt.

Programming is writing the second kind of note, in a language the machine already knows how to read.

---

## 4. Illustrated Explanation

### From wish to program

```
  WISH (too vague)
  "Make login work."
           |
           v
  PROBLEM (one sentence)
  "If email and password match a stored user, show Home; else show error."
           |
           v
  UNDERSTAND
  Given: email, password, stored users
  Need: decision + screen
  Success looks like: Home + session
  Failure looks like: error, no session
           |
           v
  ALGORITHM (human language steps)
  1. Read email
  2. Read password
  3. Find user with that email
  4. If missing -> error
  5. If password mismatch -> error
  6. Otherwise -> create session, show Home
           |
           v
  PROGRAM (Java, later)
  if / else, methods, classes...
```

### The computer is not a mind-reader

```
  You think:     "Obviously skip empty emails."
        |
        |   (this thought is NOT in the machine)
        v
  Computer:      follows only the instructions that were written
```

If you did not write the empty-email path, the computer may:

- crash,
- accept garbage,
- or do something surprising.

That surprise is not the computer "being creative." It is you leaving a hole.

### Algorithm vs program

```
  ALGORITHM                         PROGRAM
  ----------                        -------
  Language: English (or your        Language: Java, Python, ...
            native language)
  Run by: a human checking          Run by: the computer
          the plan
  Goal: be clear and complete       Goal: be clear, complete, AND
                                          legal in that language
```

Never skip the algorithm because you are eager to look like a coder. The algorithm is the coding.

---

## 5. Syntax / Concept

| Word | Meaning |
|------|---------|
| **Programming** | Writing instructions a computer can execute. |
| **Programmer / developer** | A person who writes those instructions (SDETs are programmers too). |
| **Instruction** | One action the machine can perform ("add these numbers", "print this line"). |
| **Sequence** | Order matters. Socks then shoes. Not shoes then socks. |
| **Decision** | "If this, then that." |
| **Repetition** | "Do this for every item in the list." |
| **Algorithm** | A finite, ordered, unambiguous plan to solve a problem. |
| **Bug** | When the instructions do not match the intended result. |
| **Debugging** | Finding and fixing that mismatch. |

A good algorithm is:

1. **Finite** — it stops.
2. **Ordered** — step 3 after step 2.
3. **Unambiguous** — two readers would do the same thing.
4. **Complete enough** — includes the unhappy paths you care about.

---

## 6. Simple Example

**Problem:** Greet a user by name. If the name is missing, do not pretend.

**Algorithm:**

1. Read the name.
2. If the name is empty, output `Please type your name.`
3. Otherwise, output `Hello, ` plus the name.

**Java that follows the algorithm** (read it; run later):

```java
public class PreciseGreeting {

    public static void main(String[] args) {
        String name = "Sam"; // try changing this to "" later

        if (name.isEmpty()) {
            System.out.println("Please type your name.");
        } else {
            System.out.println("Hello, " + name + ".");
        }
    }
}
```

The `if` is not decoration. It is the algorithm's decision, written in Java.

Change `name` to `""` in your head. What should print? If you can answer, you are programming already — you are tracing.

---

## 7. Real-World Example

**Problem:** An e-commerce checkout should apply free shipping at $50.

**Understand:**

- Given: cart total
- Need: shipping cost
- Success: total 50 or more → shipping 0; below 50 → shipping 7.99

**Algorithm:**

1. Read cart total.
2. If total is less than 0, treat it as invalid (do not calculate shipping as if the world is nonsense).
3. If total is 50 or more, shipping is 0.
4. Otherwise shipping is 7.99.
5. Output shipping and grand total (cart + shipping) for valid carts.

```java
public class ShippingRules {

    public static void main(String[] args) {
        double cartTotal = 49.99;

        if (cartTotal < 0) {
            System.out.println("Invalid cart total.");
            return;
        }

        double shipping;
        if (cartTotal >= 50.00) {
            shipping = 0.00;
        } else {
            shipping = 7.99;
        }

        double grandTotal = cartTotal + shipping;
        System.out.println("Shipping: " + shipping);
        System.out.println("Grand total: " + grandTotal);
    }
}
```

Business people said "free shipping over fifty." Programmers must ask: *exactly 50? what about 49.999? negative totals?* Programming is the art of making "obvious" rules **checkable**.

---

## 8. SDET Example

**Problem:** Prove that login rejects a wrong password.

**Algorithm for the test itself:**

1. Arrange: a user `alex@bank.com` with password `correct-horse`.
2. Act: try login with `alex@bank.com` and `wrong-password`.
3. Assert: result is `REJECTED`, and no session is created.

```java
public class LoginRejectionTestIdea {

    public static void main(String[] args) {
        String storedEmail = "alex@bank.com";
        String storedPassword = "correct-horse";

        String typedEmail = "alex@bank.com";
        String typedPassword = "wrong-password";

        boolean accepted = storedEmail.equals(typedEmail)
                && storedPassword.equals(typedPassword);

        boolean expectedAccepted = false;

        if (accepted == expectedAccepted) {
            System.out.println("PASS: wrong password was rejected.");
        } else {
            System.out.println("FAIL: wrong password was accepted.");
        }
    }
}
```

Programming for SDET is still programming. The "user" of your program is often **you looking for a pass/fail line**.

---

## 9. Break the Code

A junior writes a login algorithm in Java and uses the wrong comparison style. They think "it compiled, so it is programmed."

```java
public class BrokenLoginCompare {

    public static void main(String[] args) {
        String storedPassword = "correct-horse";
        String typedPassword = "correct-horse";

        // BUG: this does not compare the text the way beginners expect
        if (storedPassword == typedPassword) {
            System.out.println("Login OK");
        } else {
            System.out.println("Login failed");
        }
    }
}
```

In Java, `==` on two `String` values does **not** reliably mean "the letters are the same." It often means "are these the same box in memory?" Sometimes it looks like it works. Then one day it fails, and people say Java is haunted.

The algorithm said "if the passwords match." The code did not implement that algorithm.

**Predict:** Why might this print `Login failed` even when the words look identical? (If you are not sure, that is fine. Chapter 10 of your career is "strings are objects.")

---

## 10. Debug

**Symptom:** Login fails even with the right password, or worse, behaves differently in tests vs production.

**Do not** start by rewriting the whole program.

**Ask:** What did the algorithm demand? "The text is the same."

**Find the mismatch:** `==` is the wrong instruction for that demand. The correct instruction for text content is `.equals(...)`.

```java
public class FixedLoginCompare {

    public static void main(String[] args) {
        String storedPassword = "correct-horse";
        String typedPassword = "correct-horse";

        if (storedPassword.equals(typedPassword)) {
            System.out.println("Login OK");
        } else {
            System.out.println("Login failed");
        }
    }
}
```

**SDET extra:** also test a wrong password, an empty password, and different capitalization if the product rules say passwords are case-sensitive (they usually are).

Debugging is not guessing. Debugging is **algorithm vs actual instructions**.

---

## 11. Student Exercise

Write an algorithm (numbered steps, on paper) for **withdrawing cash from an ATM**.

Must include:

- card / account identity (you can keep it simple)
- amount requested
- not enough money
- enough money
- what the user sees in both cases
- what is stored in both cases

Then mark each step with **S** (sequence), **D** (decision), or **R** (repetition) if you have any repetition (you might not).

You are not writing Java yet. If you catch yourself writing `if (` with braces, switch back to English.

---

## 12. Challenge

**Problem:** A user places an order for a t-shirt.

Rules:

- Sizes allowed: `S`, `M`, `L`
- Quantity must be at least 1
- If size is wrong, reject
- If quantity is wrong, reject
- If both are wrong, you must still be clear (do not only mention the first mistake if you can mention both — decide and write it down)

Write:

1. The problem in one sentence.
2. What is given / what success looks like.
3. The algorithm.
4. Three example inputs and the expected outputs (a tiny test table).

Optional Java sketch to fill later:

```java
public class TshirtOrderChallenge {

    public static void main(String[] args) {
        String size = "M";
        int quantity = 2;

        // Follow YOUR algorithm. Do not invent new rules while typing.
    }
}
```

---

## 13. Knowledge Check

1. In one sentence, what is programming?
2. What is an algorithm, and when do you write it relative to Java?
3. Why is "make it work" a weak problem statement?
4. Name the three ingredients of many algorithms: sequence, ________, and ________.
5. True or false: if a program compiles, the algorithm must be correct.
6. In the house-sitter story, what was missing from "Feed the cat"?
7. What should you do if a step might fail (empty name, empty food bag, wrong password)?
8. How does an SDET program differ in *purpose* from a checkout program, if both are Java?
9. Put these in order: code, understand, problem, algorithm.
10. Why can `==` on passwords be a bug even if the file "looks right"?

---

## 14. Interview Question

**Question:** "You have to automate login. What do you do first?"

**Weak answer:** "I open Selenium and inspect the email box."

**Strong answer:**

> "First I understand the problem: valid credentials should enter the app; invalid should not. I write the steps — an algorithm — including locked accounts or empty fields if those are in scope. Then I choose the layer: API login vs UI login. Then I write Java. If I start with locators, I may automate the wrong behavior very quickly."

They are listening for **problem → understand → algorithm**, not for a tool brand.

---

## 15. Homework

1. Write algorithms for: (a) logging into a bank app, (b) adding one item to a cart, (c) a tester checking that a cart total is correct.
2. For each algorithm, add one **failure path**.
3. Teach someone the difference between a wish and an algorithm, using the cat food story or your own.
4. Preview Chapter 3. Notice that languages are just agreed-upon ways to write algorithms so a machine can execute them.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. Writing precise instructions a computer can follow (in some programming language).
2. A clear, finite, ordered plan; write it before Java.
3. It does not define inputs, outputs, or what "work" looks like.
4. Decision (or selection), repetition (or iteration/loops).
5. False. Compiling means the language rules were followed, not that the idea is right.
6. Precision: how much, which food, what to do if empty, etc.
7. Write that path in the algorithm (and later in code). Do not assume the happy path.
8. Purpose: checkout *does* the business action; the SDET program *checks* that the action is correct. Both are programs.
9. problem → understand → algorithm → code (Java sits with code, after the plan).
10. `==` may compare object identity, not password text. The algorithm wanted text match; `.equals` is the usual tool.

</details>
