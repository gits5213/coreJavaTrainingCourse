# Part 8 — Decision Making

Until now, your programs mostly ran from top to bottom and did the same thing every time. Real software chooses.

```text
Is the password correct?
    /          \
  Yes           No
   ↓             ↓
 Login         Error

Is the status code 200?
    /          \
  Yes           No
   ↓             ↓
 PASS          FAIL
```

This part teaches Java's decision tools:

```text
if            one door that may open
if / else     two doors: this or that
else if       many doors, checked in order
switch        pick a case from a matching value
```

## Chapters and Project

| Item | Topic | Result |
| --- | --- | --- |
| [Chapter 26](chapter-26-if.md) | `if` | Run extra code only when a condition is true |
| [Chapter 27](chapter-27-if-else.md) | `if / else` | Always choose PASS or FAIL |
| [Chapter 28](chapter-28-else-if.md) | `else if` | Handle 200, 404, 500, and everything else |
| [Chapter 29](chapter-29-switch.md) | `switch` | Traditional switch with `break` |
| [Project 1](project-01-test-result-evaluator.md) | Test Result Evaluator | Print TEST FAILED with expected and actual |

## Switch Note

Chapter 29 teaches the **traditional** `switch` with `case` and `break`. Modern Java also has switch expressions. Those will come later. Learn the classic form first so you can read older code and understand why `break` exists.

## Prerequisite

Part 7. You should be comfortable with `==`, `equals`, `&&`, `||`, and `!`.

## SDET Connection

Almost every automated check is a decision:

```text
if actual equals expected
    TEST PASSED
else
    TEST FAILED
    print expected
    print actual
```

Project 1 is that idea as a complete tiny program.
