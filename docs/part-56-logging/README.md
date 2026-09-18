# Part 56 — Logging

Use a coherent logging framework (SLF4J + Logback is the usual Java choice). `System.out.println` is not a strategy.

```text
TRACE
DEBUG
INFO
WARN
ERROR
```

Never log:

```text
Passwords
Tokens
Secrets
Sensitive customer data
```

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [Logging](logging.md) | Levels, SLF4J, what to log in tests, secrets |

## Prerequisite

You have tests that fail. You want evidence without leaking PII.
