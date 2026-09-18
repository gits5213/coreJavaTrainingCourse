# Project 1 — Hello Java

## Goal

Create a class with a `main` method and print a welcome line. This is your first Maven project in the course.

## Concepts this practices

- Classes
- `public static void main(String[] args)`
- Packages
- Running a program with Maven

## How to run

From this project directory:

```bash
mvn -q compile exec:java
```

## Expected output

```text
Hello, Java!
```

## What success looks like

The program compiles and prints exactly `Hello, Java!`. You can explain each word in `public static void main`.

## Stretch challenge

Print your name on a second line, then add a third line with today's date using `java.time.LocalDate.now()`.
