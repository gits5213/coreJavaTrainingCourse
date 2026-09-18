# Project 14 — Data-Driven Framework

## Goal

Climb the data-driven ladder: hardcoded values → a `LoginData` model → JSON on the classpath → a data factory → one parameterized test. There is **no live UI**; rows describe valid and invalid logins for an in-memory validator.

## Concepts this practices

```text
Hardcoded Data
      ↓
Method Parameters
      ↓
Model Object (LoginData)
      ↓
JSON / Data Factory
      ↓
Parameterized test
```

Independent rows. One failing credential pair must not poison the next.

## How to run

From this project directory:

```bash
mvn test
```

## Expected output

```text
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

(3 JSON rows plus a factory-load sanity check.)

## What success looks like

`src/test/resources/testdata/login-users.json` is the source of truth. The test method does not hardcode usernames. Passwords in the JSON are dummy classroom values, not secrets.

## Stretch challenge

Add a CSV factory next to the JSON factory and run the same test method from both sources.
