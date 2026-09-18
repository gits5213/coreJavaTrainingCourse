# Chapter 88 — What Is a Bug?

## 1. Today's Goal

By the end of this lesson, you will define a **bug** as: the program behaves differently from what was intended.

You will study:

```java
double total = price - quantity; // you meant *
```

You will use IntelliJ: **Breakpoint**, **Step Over**, **Step Into**, **Step Out**, **Resume**, **Variables**, **Evaluate Expression**.

You will follow:

```text
Run  →  Breakpoint  →  Pause  →  Inspect  →  Understand  →  Fix
```

## 2. Why It Matters

Beginners treat bugs as shame. Professionals treat bugs as **mismatches**.

If you cannot debug, you cannot be an SDET. Automation fails. You must decide: product defect, test defect, environment, race, bad data.

Printing `System.out.println` is a valid first tool. The debugger is the professional microscope: pause, look at `price` and `quantity` without scattering prints.

The course rule still applies:

```text
problem → understand → algorithm → Java → code → run → fail → debug → ...
```

"Fail" is expected. "Debug" is a skill, not a personality trait.

## 3. Real-Life Analogy

A recipe says "multiply servings by guests." You subtract guests from servings. The oven did not fail. The recipe (your code) did not match the intention (the algorithm).

A GPS: you intended "go to the bank." You typed the bakery address. The car followed instructions perfectly. **Bug = intention vs behavior.**

A detective: do not rearrange the whole crime scene. Put a yellow marker (breakpoint), pause, look at the evidence (variables), then change one thing.

## 4. Illustrated Explanation

```text
Intention:  total = price × quantity
Code:       total = price - quantity
Input:      price 10, quantity 2
Behavior:   total 8
Expected:   total 20
            └── BUG
```

```text
IntelliJ debug flow

  1. Click left gutter  →  red Breakpoint on a line
  2. Debug (not just Run)  →  JVM starts with debugger
  3. Pause when that line is about to run
  4. Variables panel  →  see price, quantity, total
  5. Step Over  →  run this line, stay in this method
  6. Step Into  →  go inside a method call
  7. Step Out   →  finish this method, return to caller
  8. Resume     →  run until the next breakpoint (or the end)
  9. Evaluate Expression  →  try price * quantity without editing yet
 10. Understand, then Fix, then Run (not Debug) to confirm
```

```text
Run  →  Breakpoint  →  Pause  →  Inspect  →  Understand  →  Fix
```

```text
Step Over     stay at this floor, next line
Step Into     take the stairs into the called method
Step Out      climb back to the caller
Resume        take the elevator until the next stop
```

Exceptions: if you pause after a throw, you may already be in a catch. Breakpoint **on** the suspicious line before it throws. Break on exception (view breakpoints) is advanced and useful for NPE.

## 5. Syntax / Concept

There is no Java keyword `debug`. Debugging is **how you run**.

Bug categories you now have names for:

| Kind | Example |
| --- | --- |
| Wrong operator | `price - quantity` vs `*` |
| Wrong index | `array[1]` when you wanted first item `[0]` |
| Wrong type handling | `parseInt("hello")` |
| Null reference | `user.name` when `user` is null |
| Race | lost increment |
| Intention vs test | test expected 201, product correctly returns 200 |

IntelliJ actions (Community edition):

- **Toggle breakpoint**: click gutter, or Ctrl+F8 (Windows/Linux) / Cmd+F8 (macOS) — shortcuts can vary; the gutter always works.
- **Debug**: bug icon, or Shift+F9 commonly.
- **Step Over**: F8
- **Step Into**: F7
- **Step Out**: Shift+F8
- **Resume**: F9
- **Variables**: Debug tool window
- **Evaluate Expression**: in the debug window, calculator-like; type `price * quantity`

Conditional breakpoint (advanced): pause only if `quantity == 0`. Right-click the breakpoint.

Do not debug by commenting out half the program randomly. Inspect.

Never "fix" by empty-catching the symptom. That hides the bug.

## 6. Simple Example

```java
public class BugTotalDemo {

    public static double total(double price, int quantity) {
        return price - quantity; // BUG: meant *
    }

    public static void main(String[] args) {
        double price = 10.0;
        int quantity = 2;
        double actual = total(price, quantity);
        double expected = 20.0;
        if (actual != expected) {
            System.out.println("TEST FAILED");
            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + actual);
        } else {
            System.out.println("TEST PASSED");
        }
    }
}
```

Expected **current** behavior: TEST FAILED, actual `8.0`.

That failure is **good**. It is a signal. Debug `total`, see `-`, fix to `*`, rerun, TEST PASSED.

## 7. Real-World Example

Shop checkout: 10% discount applied twice. Intention: once. Behavior: twice. Bug.

Bank: transfer uses `from` and `to` swapped. Intention: debit Alice. Behavior: debit Bob. The JVM did not scramble names. Your arguments were in the wrong order.

ATM: balance shown is yesterday's because you printed a cached field. Intention: live balance.

## 8. SDET Example

The same `total` method is what a test would catch:

```java
if (actual != expected) {
    throw new AssertionError("TEST FAILED expected " + expected + " but was " + actual);
}
```

Debug the **test** the same way: breakpoint on the assertion, inspect `actual` and `expected`. If `expected` is wrong, the test is the bug. If `actual` is wrong, follow Step Into the product method.

UI test: breakpoint after `getText()`, Evaluate `text.trim()`. See the space you forgot.

Empty catch:

```java
try {
    if (actual != expected) throw new AssertionError("fail");
} catch (Exception e) {
}
```

Now there is a bug **and** no signal. You made debugging harder. Forbidden.

Race flakes: debugger pauses change timing. The bug may **hide** while debugging. That is a clue you have a race (Part 31).

## 9. Break the Code

The `price - quantity` method **is** the broken code.

Second break: fixing the wrong line.

```java
// you change expected to 8 so the test passes
```

The test now documents the bug. That is not a fix. Intention was still multiply.

Third: breakpoint on the wrong method, Step Over forever, never inspect `total`.

Fourth: Evaluate Expression with a typo, think the program is wrong. Evaluate is a sandbox; it does not always change your variables unless you assign carefully. Prefer watching Variables.

## 10. Debug

Walkthrough for the total bug:

1. **Run** the program. See TEST FAILED. Read expected 20 actual 8.
2. Set **Breakpoint** on `return price - quantity;`
3. Start **Debug**. It **pauses**.
4. **Inspect** Variables: `price` 10.0, `quantity` 2.
5. **Evaluate** `price * quantity` → 20.0. Now you **understand**.
6. **Fix** to `*`. Resume or stop. Run again. TEST PASSED.

If you **Step Into** `println`, you fall into JDK code. **Step Out** or use Step Over on printlns. Step Into your methods, not every library call, until you need to.

`==` on doubles can be flaky for fractions. This homework uses `20.0` exactly. For money, later BigDecimal. If `10 * 0.1` surprises you, that is a different bug family (binary floating point).

```text
Signal (failed test)  →  pause  →  see values  →  match to intention
```

## 11. Student Exercise

Type `BugTotalDemo` with the minus bug on purpose.

Debug it with a breakpoint. Write the Variable values in notes.

Fix to `*`. Confirm TEST PASSED.

Practice Step Into `total` from `main` and Step Out back.

## 12. Challenge

Write `lineTotal(price, quantity, discountPercent)` intending `(price * quantity) * (1 - discountPercent / 100)`.

Put a bug in (wrong parentheses or plus instead of minus on the discount). Write a failing check in `main`. Debug with Evaluate to try the correct formula. Fix.

Do not change the expected value to match the bug.

## 13. Knowledge Check

1. What is a bug in this course's definition?
2. Why is `price - quantity` a bug if you meant multiply?
3. Recite the debug flow.
4. What does a breakpoint do?
5. Step Over vs Step Into?
6. What is Step Out?
7. What does Resume do?
8. Where do you inspect `price` while paused?
9. What is Evaluate Expression for?
10. Is changing expected from 20 to 8 a fix? Empty catch the AssertionError?

## 14. Interview Question

**Question:** How do you debug a failing test in IntelliJ?

A strong answer:

> A bug is when behavior does not match intention. I do not guess five changes. I Run until I see the failure, set a Breakpoint on the suspicious line, Debug until Pause, Inspect Variables, and Evaluate Expression to test a hypothesis like price * quantity. I Step Over, Into, and Out to move. Then I Understand and Fix. For price - quantity vs multiply, the debugger shows 8 instead of 20. I never hide the failure with an empty catch, and I never "fix" by changing expected to the wrong actual.

## 15. Homework

Pick any earlier program that confused you. Debug it even if it works: set a breakpoint, Step Over three lines, watch Variables.

Write in notes: "The JVM obeyed the code. I must make the code match the algorithm."

Draw the six-step flow without looking.

You have finished Part 32 when you can define a bug, operate the IntelliJ debug buttons named in this chapter, and refuse to hide failures.

---

## Answer Key

1. Behavior differs from what was intended.
2. Intention was multiply; code subtracted; 10 and 2 became 8 not 20.
3. Run → Breakpoint → Pause → Inspect → Understand → Fix.
4. Pauses just before that line when debugging.
5. Over: execute the line without entering calls. Into: enter the call.
6. Finish the current method and pause in the caller.
7. Continue until the next breakpoint or the program ends.
8. Variables panel (and the editor inline values).
9. Trying an expression (like `price * quantity`) while paused.
10. No, and no.
