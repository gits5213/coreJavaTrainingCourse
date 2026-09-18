# Part 10 — Methods

A **method** is a named recipe. You write the steps once. You call the name whenever you need those steps.

```text
BAD
login steps copied 20 times

GOOD
login();
login();
login();
```

If the login steps change, you fix them in one place.

This part stays with `public static` methods so you can call them from `main` without objects. Object methods come with OOP later. Learning to pass data in and get data out is the skill that transfers.

## Chapters in This Part

| Chapter | Topic | You will be able to... |
| --- | --- | --- |
| [Chapter 34](chapter-34-why-methods.md) | Why methods? | Create and call a simple method |
| [Chapter 35](chapter-35-parameters.md) | Parameters | Send username and password into `login` |
| [Chapter 36](chapter-36-return-values.md) | Return values | Get back `true` or `false` from a status check |
| [Chapter 37](chapter-37-method-overloading.md) | Overloading | Same name, different parameter lists |

## Prerequisite

You can write `main`, variables, `if`, and loops. Methods will contain those tools.

## SDET Connection

Test frameworks are mountains of methods: `click`, `type`, `getStatusCode`, `assertEquals`, `loginAsAdmin`. If you cannot write a method, you cannot build a page object later. Start small:

```java
printWelcome();
login("john", "Test123");
boolean ok = statusMatches(200, 200);
```
