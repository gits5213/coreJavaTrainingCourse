# Project 5 — Employee Management System

## Goal

Store employees in memory behind an interface. Add people, list them, and find one by id. The rest of the app talks to `EmployeeRepository`, not to `ArrayList` directly.

## Concepts this practices

- Collections (`List`)
- OOP (model class with encapsulated fields)
- Interfaces and a concrete implementation
- `Optional` for "found or not"

## How to run

From this project directory:

```bash
mvn -q compile exec:java
```

Optional tests:

```bash
mvn test
```

## Expected output

```text
All employees:
  1 | Aisha Khan | SDET | 95000.0
  2 | Ben Ortiz | QA Lead | 110000.0
  3 | Chen Li | Automation Engineer | 88000.0
Found id 2: Ben Ortiz
Missing id 99: not found
```

## What success looks like

`InMemoryEmployeeRepository` implements `EmployeeRepository`. `findById` returns the matching employee or empty. Duplicate ids are rejected. The demo never reaches into the private list.

## Stretch challenge

Add `deleteById` and `findByRole`. Then write a second implementation that still satisfies the same interface (even if it is a `Map` behind the scenes).
