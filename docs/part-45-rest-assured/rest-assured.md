# REST Assured

## Goal

By the end of this lesson, you will write a REST Assured test using **given / when / then**, assert a status code, and explain each word as setup, action, and validation.

## Why It Matters

Java SDET jobs list REST Assured constantly. It is not the only HTTP library. It is the one you will read in most automation repos.

Used badly, it becomes a 200-line chain in a test method with no reuse. Used well, tests stay short and `UserApiClient` hides the path strings.

## Real-Life Analogy

A lab form.

```text
given    ingredients and equipment on the bench (URI, headers, body)
when     you perform the experiment (GET/POST)
then     you record measurements (status, JSON fields)
```

Same as cooking: mise en place, cook, taste.

## Illustrated Explanation

```text
given
=
setup

when
=
action

then
=
validation
```

```text
given()                 arrange: base URI, auth, content type, body
when()                  act: get/post/put/patch/delete
then()                  assert: status, body, headers
```

```text
Test
  → REST Assured
      → HTTP
          → API
```

REST Assured still uses HTTP. A 404 is still a 404.

## Syntax / Concept

Static import (common style):

```java
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
```

GET:

```java
given()
        .baseUri("https://example.com")
.when()
        .get("/")
.then()
        .statusCode(200);
```

POST JSON:

```java
given()
        .baseUri("https://api.shop.example")
        .contentType("application/json")
        .body("{\"name\":\"Aisha\"}")
.when()
        .post("/users")
.then()
        .statusCode(201)
        .body("name", equalTo("Aisha"));
```

Extract if you need the id for the next call (careful: that can couple tests — prefer independent tests + factory):

```java
String id = given()
        .baseUri("https://api.shop.example")
        .contentType("application/json")
        .body("{\"name\":\"Aisha\"}")
.when()
        .post("/users")
.then()
        .statusCode(201)
        .extract()
        .path("id");
```

Logging (never log secrets):

```java
given().log().uri()
```

Do not `.log().all()` if headers contain tokens.

## Simple Example

```java
package com.training.sdet;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ExampleDotComTest {

    @Test
    public void shouldReturnOkFromExampleDotCom() {
        given()
                .baseUri("https://example.com")
        .when()
                .get("/")
        .then()
                .statusCode(200);
    }
}
```

JUnit 5 equivalent uses `@Test` from Jupiter. Same REST Assured chain.

## Real-World Example

Banking get account:

```java
given()
        .baseUri(config.apiBaseUrl())
        .header("Authorization", "Bearer " + token)
.when()
        .get("/accounts/{id}", accountId)
.then()
        .statusCode(200)
        .body("currency", equalTo("USD"));
```

Path params `{id}` keep URLs readable.

E-commerce create order: `POST /orders` expect 201, body `status` equal to `NEW`.

## SDET Example — Client Wrapper (KISS)

Do not paste `given()` into 80 tests. Hide paths:

```java
public class UserApiClient {
    private final String baseUri;

    public UserApiClient(String baseUri) {
        this.baseUri = baseUri;
    }

    public void assertUserExists(String id) {
        given()
                .baseUri(baseUri)
        .when()
                .get("/users/{id}", id)
        .then()
                .statusCode(200);
    }
}
```

Tests speak domain language. When the path changes to `/v2/users`, one class changes.

```text
given = setup
when  = action
then  = validation
```

Say it out loud until it is boring.

## Break the Code

```java
given()
.when()
        .get("https://example.com")
.then()
        .statusCode(201);
```

Wrong expected status. Failure is a test bug if the server is 200.

```java
.body("$.name", equalTo("Aisha"));
```

REST Assured Groovy path is not JSONPath `$` the same way as some tools. Use `"name"` for a top-level field.

Swallowing:

```java
try {
    given()...then().statusCode(200);
} catch (Exception e) {}
```

Never.

## Debug

REST Assured failures print expected vs actual status. Read them.

If you get SSL errors, do not blindly `relaxedHTTPSValidation()` in production-like tests without asking security.

If JSON assert fails, print the body in a failing test with a controlled log (no tokens).

`Connection refused`: environment, not REST Assured.

## Student Exercise

Write a TestNG or JUnit test that GETs `https://example.com` and asserts 200 using given/when/then. In comments, label each section setup/action/validation.

## Challenge

Write `UserApiClient` with `createUser(String json)` expecting 201 *or* skip live POST if you have no API. Mock the idea with comments. Add a method `assertNotFound(String id)` expecting 404. Explain why 404 tests need a guaranteed-missing id.

## Knowledge Check

1. What does `given` mean?
2. `when`?
3. `then`?
4. Write a GET 200 example.
5. How do you set JSON content type?
6. Why wrap REST Assured in a client class?
7. Why not `log().all()`?
8. given/when/then maps to which unit-test words?
9. Path param benefit?
10. Does REST Assured replace HTTP knowledge?

## Interview Question

**Question:** How do you use REST Assured?

A strong answer:

> REST Assured is a fluent HTTP test library. given is setup: base URI, headers, body. when is the action: get or post. then is validation: statusCode(200) and JSON fields. That is arrange-act-assert. I keep HTTP knowledge: 401 vs 404. I wrap calls in an API client so tests do not duplicate paths. I never log tokens. I learned Java HttpClient first so I know what the library abstracts.

## Homework

Add the REST Assured dependency. Green GET test against example.com (or instructor API). Commit `Add REST Assured smoke GET`. Sketch UserApiClient in notes even if the real API is not ready.

---

## Answer Key

1. Setup (headers, URI, body).
2. Action (HTTP method).
3. Validation (asserts).
4. See Simple Example.
5. `.contentType("application/json")`
6. DRY paths, one place to add auth, tests stay readable.
7. Tokens and PII in CI logs.
8. Arrange, act, assert.
9. Readable URLs, safer encoding, reuse.
10. No.
