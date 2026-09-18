# Part 46 — Database Fundamentals

Many bugs are not in the UI. The API wrote the wrong row. SDETs who can read SQL and JDBC can prove it.

```text
Application
 ↓
API
 ↓
Database
```

SQL: SELECT FROM WHERE INSERT UPDATE DELETE.

```text
Java
 ↓
JDBC
 ↓
Database Driver
 ↓
Database
```

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [JDBC and SQL](jdbc-and-sql.md) | SQL basics, JDBC flow, try-with-resources, SDET validation |

## Prerequisite

Exceptions, try-with-resources if you have it from Part 17. APIs from Part 43 so you see where the database sits.

## Safety

Training databases only. Never point destructive SQL at production. Never commit DB passwords.
