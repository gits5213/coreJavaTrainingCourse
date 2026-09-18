# Part 21 — Lambdas

A **lambda** is a short way to write a function you can pass around: "given this input, do that."

```java
items.forEach(item -> System.out.println(item));
```

Read `item ->` as **given item, then**.

This part comes **after** methods and interfaces. A lambda is not a replacement for learning those. It is a compact implementation of an interface that has **one abstract method** (a functional interface).

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 71](chapter-71-lambda-expressions.md) | Lambda syntax, `forEach`, `Validator` |

## Prerequisite

Methods, parameters, interfaces. If `interface` still feels shaky, reread that chapter before this one. Lambdas will feel like magic tricks otherwise.

## SDET Connection

Streams (next part), wait conditions, and many test APIs take lambdas: "retry until this is true," "map each row." You will also write small validators: `username -> username != null && !username.isBlank()`.
