# Part 20 — Generics

A **generic** type is a class or method with a **type parameter** — a placeholder for another type.

```text
List<String>   a list of strings
List<Integer>  a list of integers
ApiResponse<User>   a response whose data is a User
ApiResponse<Order>  a response whose data is an Order
```

Without generics, everything becomes `Object` and you cast. Casts fail at **runtime**. Generics push many mistakes to **compile time**.

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 70](chapter-70-why-generics.md) | Why generics exist; `ApiResponse<T>` |

## Prerequisite

Classes, fields, methods. Collections (`List`) if you already have them; if not, `ApiResponse<T>` is still readable.

## SDET Connection

API wrappers return `ApiResponse<User>` from GET /users/1 and `ApiResponse<Order>` from GET /orders/1. Same envelope: status plus data. Different payload type.

```text
{ "status": 200, "data": { "username": "john", "role": "tester" } }
```

The envelope is generic. The inner object is not a mystery `Object`.
