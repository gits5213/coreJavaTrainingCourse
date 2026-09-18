# Part 45 — REST Assured

REST Assured is a Java library for testing HTTP APIs with a fluent style:

```java
given()
    .baseUri(...)
.when()
    .get(...)
.then()
    .statusCode(200);
```

```text
given   = setup
when    = action
then    = validation
```

That is Arrange / Act / Assert in BDD clothing. You already know HTTP. This library saves typing.

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [REST Assured](rest-assured.md) | given/when/then, status, body, headers, SDET client wrapper |

## Prerequisite

Parts 43–44. If you skip them, `given()` is magic and you cannot debug 401.

## Maven

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.5.0</version>
    <scope>test</scope>
</dependency>
```

If API clients live in `src/main/java`, you may need compile scope. Prefer tests using REST Assured, production-like clients optionally using HttpClient. Teams vary. Be consistent.
