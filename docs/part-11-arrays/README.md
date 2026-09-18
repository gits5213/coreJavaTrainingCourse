# Part 11 — Arrays

An **array** is a single variable that holds **many values of the same type**, in a numbered row.

```text
One variable, many boxes

browsers
┌─────────┬─────────┬─────────┐
│ Chrome  │ Firefox │ Edge    │
│  [0]    │  [1]    │  [2]    │
└─────────┴─────────┴─────────┘
```

Until now, one variable held one status code, one name, one price. Tests rarely have one browser. They have a list. Arrays are your first list structure.

Later, the Collections framework (`List`, `Set`, `Map`) will feel more flexible. Arrays still matter because:

- they appear in `main(String[] args)`
- they are the classic way to learn indexes
- many APIs still return arrays

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 38](chapter-38-arrays.md) | Create arrays, index from 0, loop with `for` and the enhanced `for` |

## Critical Rule

```text
Indexes start at 0.
```

The first browser is `browsers[0]`, not `browsers[1]`.

## After This Part

You can store a row of browsers and print each one. That is enough to start thinking about data-driven tests. Object-oriented programming comes next in the course, where those browsers and users become objects.
