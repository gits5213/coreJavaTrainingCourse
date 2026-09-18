# Part 51 — Test Data Model

Tests should not pass a pile of unlabelled strings forever.

```java
login("a", "b");
```

Better: a type that means login credentials.

```java
public record LoginData(
    String username,
    String password
) {
}
```

or a POJO if the student has not reached records yet.

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [Test Data Models](test-data-models.md) | record vs POJO, factories, secrets, SDET usage |

## Prerequisite

OOP. Records if you completed Part 26; otherwise use a class. POM so you have somewhere to send the data.
