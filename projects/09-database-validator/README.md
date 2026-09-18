# Project 9 — Database Validator

## Goal

Stand up an in-memory H2 database, create a `users` table, insert sample rows, then `SELECT` and validate row count plus a known username. This is the JDBC skill you will later use after an API create.

## Concepts this practices

- JDBC (`DriverManager`, `Connection`, `PreparedStatement`, `ResultSet`)
- SQL (`CREATE TABLE`, `INSERT`, `SELECT`, `COUNT`)
- Models (`UserRow` record)
- try-with-resources (always close DB resources)

No production database. No passwords in Git. H2 runs inside the JVM.

## How to run

From this project directory:

```bash
mvn -q compile exec:java
```

Tests (also use H2 in-memory):

```bash
mvn test
```

## Expected output

```text
users row count: 3 (expected 3) PASS
username 'aisha' exists: true PASS
Validation: PASS
```

## What success looks like

Schema lives in `src/main/resources/schema.sql`. Inserts use `PreparedStatement` placeholders, not string concatenation. The program fails loudly if the count is wrong or `aisha` is missing.

## Stretch challenge

Add an email uniqueness check and a `findByUsername` method that returns `Optional<UserRow>`. Assert that a missing user is empty, not null.
