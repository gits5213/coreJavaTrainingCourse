# Part 5 — Variables

Welcome to the first part of the course where you write Java that *remembers* things.

Until now, a program could print a message and stop. That is useful, but it is not enough. Real software stores numbers, names, passwords, prices, and test results. The way Java stores a piece of information is called a **variable**.

```text
Before variables
     ↓
A program can only print fixed text

After variables
     ↓
A program can store values,
   change them,
   compare them,
   and make decisions
```

## What You Will Learn

In this part you will learn:

- what a variable is
- why a variable has a type, a name, and a value
- how to declare a variable
- how to assign a value
- how to change a value later
- how to name variables so other people can understand your code

You do **not** need to memorize every Java data type yet. That is Part 6. Here, the goal is simpler:

> A variable is a named box that holds one value at a time.

## Why SDET Students Need This Now

A software tester who later becomes an SDET will constantly store values such as:

```text
expectedStatusCode = 200
actualStatusCode   = 404
username           = "john"
retryCount         = 3
testPassed         = false
```

If you cannot store those values in variables, you cannot compare them. If you cannot compare them, you cannot decide whether a test passed or failed.

```text
Store expected result
        ↓
Store actual result
        ↓
Compare them
        ↓
Print PASS or FAIL
```

That entire chain starts with variables.

## Chapters in This Part

| Chapter | Topic | You will be able to... |
| --- | --- | --- |
| [Chapter 17](chapter-17-what-is-a-variable.md) | What is a variable? | Create a named storage place and put a value in it |
| [Chapter 18](chapter-18-naming-rules.md) | Naming rules | Choose names that Java accepts and humans understand |

## How to Study This Part

1. Read Chapter 17 slowly. Type every example yourself. Do not only look at the code.
2. Change the values and run the program again. Watch the output change.
3. Read Chapter 18 and rewrite one of your programs using better names.
4. Complete the student exercise, the challenge, and the homework in each chapter.

## Prerequisite

You should already be able to:

- create a Java class in IntelliJ IDEA
- write a `main` method
- run a program
- print text with `System.out.println`

If those steps still feel new, return to Part 4 and practice one more Hello World program first.

## After This Part

Part 6 will teach **data types**. You will then understand *what kind* of value a box can hold: whole numbers, decimal numbers, true/false answers, single characters, and text.

For now, keep this picture in your mind:

```text
┌──────────────────────────┐
│ Variable                 │
│                          │
│  type : int              │
│  name : age              │
│  value: 25               │
└──────────────────────────┘
```
