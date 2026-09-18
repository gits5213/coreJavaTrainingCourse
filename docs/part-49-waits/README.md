# Part 49 — Waits

Never default to:

```java
Thread.sleep(5000);
```

Teach `WebDriverWait` and **state-based synchronization**: wait until something is true (visible, clickable, URL changed), not until a wall clock says 5 seconds passed.

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [Waits](waits.md) | Why sleep is harmful, implicit vs explicit, WebDriverWait, ExpectedConditions |

## Prerequisite

Locators. You can find an element when it is already there.
