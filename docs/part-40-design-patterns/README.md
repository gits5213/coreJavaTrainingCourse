# Part 40 — Design Patterns

Do **not** teach patterns as a shopping list to sprinkle on every class.

> Do not teach until students understand the underlying problem.

A pattern is a name for a solution you needed twice. If you have not felt the problem, the pattern is costume jewelry.

```text
Factory     Builder     Strategy     Facade
Adapter     Observer    Singleton
```

Also teach:

> Singleton is often overused.

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [Design Patterns](design-patterns.md) | Factory, Builder, Strategy, Facade, Adapter, Observer, Singleton — with SDET examples |

## Prerequisite

SOLID and real classes. Factory after you hated `new ChromeDriver()` in 15 tests. Builder after you hated constructors with 9 parameters.

## SDET Connection

`DriverFactory.create(BrowserType.CHROME)` and `UserBuilder` are the two patterns you will actually type soon. The others appear in frameworks. Singleton appears too often; be suspicious.
