# Part 41 — Unit Testing

A **unit test** checks a small piece of production code — a method, a class — without a browser and without a network if you can help it.

Production function:

```java
public int add(int a, int b) {
    return a + b;
}
```

The test proves `2 + 3` is `5`. That sounds toy-like. It is the same shape as `statusMatches(200, 200)` and every assertion you will ever write.

```text
Arrange    set up
Act        call the method
Assert     check the result
```

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 94](chapter-94-why-test-code.md) | Why test code? JUnit `@Test`, arrange-act-assert, `assertEquals` |

## Prerequisite

Maven (so JUnit can run with `mvn test`), methods, and honest naming. You do not need Selenium.

## SDET Connection

If you cannot unit test a calculator, you cannot later unit test a `StatusMatcher` or a `UserFactory`. UI tests are expensive. Unit tests are cheap truth.
