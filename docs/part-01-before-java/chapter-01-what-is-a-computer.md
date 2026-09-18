# Chapter 1 — What Is a Computer?

## 1. Today's Goal

By the end of this lesson, you will be able to describe a computer as a machine that follows a four-step pattern:

**input → process → store → output**

You will also be able to tell **hardware** from **software**, and you will see that a Java program is just software: a list of instructions a machine can follow.

You do **not** need to install Java for this chapter. Read the programs as stories. We will type them in Part 4.

---

## 2. Why It Matters

If you do not know what a computer is doing, programming feels like superstition.

People say things like:

- "The computer is being dumb."
- "It should know I meant the other button."
- "It worked on my machine."

A computer is not rude, clever, or loyal. It is **obedient and literal**. It does what the current instructions say, with the current data, on the current hardware.

SDET work is the professional version of that insight. When a payment fails, you do not shrug. You ask: *What input arrived? What processing ran? What was stored? What output did the user see?*

---

## 3. Real-Life Analogy

Imagine a tiny **restaurant**.

```
  Customer                 Kitchen                  Receipt printer
  (you)                    (cook + recipe)          (paper)
     |                            |                      |
     |  "One grilled cheese"      |                      |
     |--------------------------->|                      |
     |                            | cook, assemble       |
     |                            | maybe write order    |
     |                            |     in a notebook    |
     |                            |--------------------->|
     |<---------------------------|----------------------|
              sandwich + bill
```

| Restaurant piece | Computer piece |
|------------------|----------------|
| Your spoken order | **Input** |
| The cook following a recipe | **Process** |
| The notebook of orders, or the freezer of ingredients | **Store** |
| The sandwich and the printed bill | **Output** |
| Stove, fridge, plates | **Hardware** (the body) |
| The recipe | **Software** (the instructions) |

If the recipe says "add two slices of cheese" and you wanted one, the cook is not psychic. The recipe is wrong, or your order was unclear.

Java programs are recipes. The computer is the kitchen.

---

## 4. Illustrated Explanation

Every useful computer, from a phone to a bank's giant server, repeats this loop:

```
                    +-----------+
                    |   YOU     |
                    | (human)   |
                    +-----------+
                          |
          types, clicks, taps, camera, files
                          v
                  +---------------+
                  |    INPUT      |
                  | keyboard,     |
                  | mouse,        |
                  | network,      |
                  | files         |
                  +---------------+
                          |
                          v
                  +---------------+
           +----> |   PROCESS     | <----+
           |      | CPU follows   |      |
           |      | instructions  |      |
           |      +---------------+      |
           |              |              |
           |              v              |
           |      +---------------+      |
           |      |    STORE      |      |
           |      | RAM (short)   |------+
           |      | disk (long)   |
           |      +---------------+
           |              |
           |              v
           |      +---------------+
           |      |    OUTPUT     |
           |      | screen, file, |
           |      | speaker,      |
           |      | network       |
           |      +---------------+
           |              |
           +--------------+  (the next click starts the loop again)
```

### Hardware vs software

```
  +----------------------------------------+
  |              COMPUTER                  |
  |                                        |
  |   HARDWARE (you can touch)             |
  |   +--------+ +--------+ +--------+     |
  |   | CPU    | | RAM    | | Disk   |     |
  |   | brain  | | desk   | | cabinet|     |
  |   +--------+ +--------+ +--------+     |
  |   +--------+ +--------+                |
  |   | Screen | | Keyboard|               |
  |   +--------+ +--------+                |
  |                                        |
  |   SOFTWARE (you cannot touch)          |
  |   +----------------------------------+ |
  |   | Operating system (Windows/macOS) | |
  |   | Browser, IntelliJ, Java, games   | |
  |   | YOUR future programs and tests   | |
  |   +----------------------------------+ |
  +----------------------------------------+
```

- **CPU**: the cook. It executes steps very quickly.
- **RAM**: the kitchen counter. Fast, but it is cleared when power is gone (mostly).
- **Disk / SSD**: the pantry. It keeps files when you shut the lid.
- **Software**: the recipes, including the operating system that runs the whole restaurant.

A phone is a computer. An ATM is a computer. The chip in a modern car is a computer. Same four-step pattern.

---

## 5. Syntax / Concept

You do not need Java syntax yet. You need these words, used precisely:

| Word | Meaning in this course |
|------|------------------------|
| **Input** | Data coming in from the outside (user, file, network, another system). |
| **Process** | Instructions transforming that data (compare, add, decide, sort). |
| **Store** | Keeping data for later, in memory or on disk. |
| **Output** | Data going out (screen text, a saved file, a network response). |
| **Hardware** | Physical parts. |
| **Software** | Instructions. A Java program is software. A test is software. |
| **Program** | A named set of instructions the computer can run. |

When we finally write Java, we are writing **software** that:

1. may read **input**,
2. **processes** it,
3. may **store** results in variables or files,
4. produces **output**.

That is not a Java idea. That is a computer idea. Java is one language for writing it down.

---

## 6. Simple Example

Here is the four-step pattern as a tiny Java program. We will not run it until Part 4. Read it like a recipe.

```java
public class GreetingMachine {

    public static void main(String[] args) {
        // INPUT: a name we received (later this could come from a keyboard)
        String name = "Amina";

        // PROCESS: build a message from the input
        String message = "Hello, " + name + "!";

        // STORE: the variable "message" holds the result in memory (RAM)
        // (We already stored it by assigning it.)

        // OUTPUT: show it on the screen
        System.out.println(message);
    }
}
```

What the computer does, in restaurant language:

1. It is given the name `Amina` (input).
2. It follows the instruction to glue words together (process).
3. It keeps the sentence in a box called `message` (store, in RAM).
4. It prints the sentence (output).

If the name were `Ben`, the same program would output `Hello, Ben!`. **Same recipe, different ingredient.**

---

## 7. Real-World Example

Think of an **online bank transfer**.

```
  INPUT                         PROCESS                      STORE
  --------                      -------                      -----
  fromAccount = 111             Is there enough money?       New balances
  toAccount   = 222             Subtract 50 from 111         Transaction record
  amount      = 50.00           Add 50 to 222                Audit log

                                OUTPUT
                                ------
                                Screen: "Transfer complete"
                                Email: receipt
                                Other bank's computer: a network message
```

A Java-shaped sketch of the *idea* (not a real bank system):

```java
public class BankTransferDemo {

    public static void main(String[] args) {
        // INPUT
        double fromBalance = 200.00;
        double toBalance = 80.00;
        double amount = 50.00;

        // PROCESS
        boolean enoughMoney = fromBalance >= amount;
        if (enoughMoney) {
            fromBalance = fromBalance - amount;
            toBalance = toBalance + amount;
        }

        // STORE (in this tiny demo, we store in variables in RAM)
        // A real bank would also write to a database on disk.

        // OUTPUT
        if (enoughMoney) {
            System.out.println("Transfer complete.");
            System.out.println("Sender now has: " + fromBalance);
            System.out.println("Receiver now has: " + toBalance);
        } else {
            System.out.println("Transfer rejected: not enough money.");
        }
    }
}
```

Notice the failure path. Real systems must output something when the process *cannot* succeed. Beginners often design only the happy sandwich. Banks cannot.

---

## 8. SDET Example

An SDET looks at the same transfer and asks, "What evidence would prove it works?"

Typical checks:

| What we control (input) | What we look at (output / store) |
|-------------------------|----------------------------------|
| amount 50, balance 200 | sender 150, receiver increased by 50 |
| amount 50, balance 10 | rejection message, balances unchanged |
| amount 0 or negative | rejection, no silent "success" |

```java
public class TransferTestIdea {

    public static void main(String[] args) {
        double fromBalance = 10.00;
        double amount = 50.00;

        boolean shouldReject = fromBalance < amount;

        // This is the seed of a test: expected vs actual.
        boolean actualRejected = shouldReject;
        boolean expectedRejected = true;

        if (actualRejected == expectedRejected) {
            System.out.println("TEST PASS: poor account cannot send 50.");
        } else {
            System.out.println("TEST FAIL: system allowed a bad transfer.");
        }
    }
}
```

You just saw the SDET heartbeat: **expected vs actual**. We will grow this into JUnit later. The idea does not wait for a framework.

---

## 9. Break the Code

Here is a program that *looks* like a transfer helper. It has a thinking error, not a spelling error.

```java
public class BrokenAtmDisplay {

    public static void main(String[] args) {
        double balance = 100.00;
        double withdrawal = 40.00;

        // PROCESS: we forgot to actually subtract!
        // balance = balance - withdrawal;

        System.out.println("Please take your cash: " + withdrawal);
        System.out.println("Your balance is still listed as: " + balance);
    }
}
```

If a tester only looks at the first line of output ("Please take your cash: 40.0"), they might think the ATM worked. The **stored** balance never changed. The restaurant served a sandwich and forgot to bill — or billed and forgot to cook. Either way, the four steps did not agree.

**Predict before you read the next section:** What two outputs will print? Which step was skipped?

---

## 10. Debug

**What we expected**

- Output cash 40
- Stored balance 60
- Display balance 60

**What we observed**

- Output cash 40 (looks fine)
- Display balance 100 (wrong)

**Where to look**

Do not start at the printer. Start at the four-step diagram.

```
  INPUT: 100 and 40   →  seems fine
  PROCESS: subtract   →  the subtract line is commented out / missing
  STORE: balance      →  still 100
  OUTPUT: prints      →  faithfully shows the wrong stored value
```

The output is honest. The process is incomplete.

**Fix**

```java
public class FixedAtmDisplay {

    public static void main(String[] args) {
        double balance = 100.00;
        double withdrawal = 40.00;

        if (withdrawal <= balance) {
            balance = balance - withdrawal;
            System.out.println("Please take your cash: " + withdrawal);
            System.out.println("Your new balance is: " + balance);
        } else {
            System.out.println("Withdrawal rejected.");
            System.out.println("Balance unchanged: " + balance);
        }
    }
}
```

SDET lesson: **never trust a single output**. Check the stored result too. That is why good tests look at balances, database rows, and response bodies — not only a green checkmark on the screen.

---

## 11. Student Exercise

On paper, pick one action you did today on a phone or laptop (for example: sending a message, searching, paying).

Write four headings:

1. Input
2. Process
3. Store
4. Output

Fill each with at least two bullets. Then add:

5. Hardware used (at least two parts)
6. Software used (at least two programs or apps)

Example starter if you choose "search for a recipe":

- Input: the words you typed, the Enter key
- Process: the search engine ranks pages
- Store: your search might be saved in history
- Output: a list of links on the screen

Do not worry about being technically perfect. Worry about being specific.

---

## 12. Challenge

An e-commerce site places an order.

- Input: user id `17`, product `shoes`, quantity `2`, price `60` each, paid `120`
- The warehouse has only `1` pair of shoes

Write, in numbered steps (ordinary language, not Java yet):

1. What should processing do?
2. What should be stored if the order **fails**?
3. What should the user see?
4. What should an SDET check so a "success email" cannot be sent when stock was missing?

Then, if you want a stretch, copy this starter and fill in the comments in your notes:

```java
public class OrderStockChallenge {

    public static void main(String[] args) {
        int stock = 1;
        int quantityWanted = 2;
        boolean paid = true;

        // TODO: decide accepted or rejected
        // TODO: print a customer message
        // TODO: print what an SDET would verify
    }
}
```

---

## 13. Knowledge Check

Answer in your notebook. Then scroll to the Answer Key at the end of this chapter.

1. Name the four steps of the computer pattern used in this course.
2. Is a keyboard hardware or software? Is IntelliJ hardware or software?
3. Where does a program usually keep values *while it is running*: RAM or the printed paper on your desk?
4. If a program prints "Payment successful" but the bank database still shows the old balance, which step is lying relative to which step?
5. Why is a phone considered a computer in this course?
6. In the restaurant analogy, what is the recipe?
7. True or false: the CPU "knows what you meant" if you click the wrong button.
8. What is the SDET heartbeat mentioned in this chapter?
9. Give one input, one process, and one output for logging into email.
10. Why did we show Java before you install Java?

---

## 14. Interview Question

**Question:** "Can you explain, simply, what a computer does when a user clicks Login?"

**How to think (out loud):**

Do not start with Java. Start with the four steps.

**A strong beginner answer:**

> "The click is input. The software processes it: it reads the email and password, checks them against stored account data, and decides success or failure. It may store a session so the user stays logged in. The output is either the home page or an error message. If any of those steps disagree — for example a success page but no session stored — the product is wrong, even if the screen looks fine."

If they ask a follow-up about testing, add: you would verify both the output and the stored session.

---

## 15. Homework

1. Teach the four-step diagram to a person or to a rubber duck. Use a real example (ATM, cart, login).
2. In your notes, write five hardware items and five software items you already use.
3. Copy `GreetingMachine` by hand into your notebook, and label each line INPUT, PROCESS, STORE, or OUTPUT.
4. Read Chapter 2 tomorrow, not immediately if your brain is cooked. Sleep is part of compiling *you*.

---

## Answer Key

<details>
<summary>Click to reveal after you have tried</summary>

1. Input, process, store, output.
2. Keyboard is hardware. IntelliJ is software.
3. RAM (memory). Variables live there while the program runs.
4. Output (the message) does not match store (the database). The process may have skipped the real update, or stored in the wrong place.
5. It takes input, processes, stores, and outputs. It is a computer you can put in a pocket.
6. Software / the program / the instructions.
7. False. It follows instructions and input, not intentions.
8. Compare expected result to actual result.
9. Example: input = email + password; process = check credentials; output = inbox or error. (Store might be a session.)
10. So you can see that Java is just a written form of the same four-step idea, not magic.

</details>
