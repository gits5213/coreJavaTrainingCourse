# Java HttpClient

## Goal

By the end of this lesson, you will send an HTTP GET (and a simple POST) with `java.net.http.HttpClient`, print the status code and body, and explain what REST Assured will later hide.

## Why It Matters

If REST Assured breaks, you still have the JDK. Architects who cannot call HTTP without a library cannot debug CI when the library version shifts.

SDET interviews: "Have you used Java's HttpClient?" A yes plus a 10-line GET is enough. A no plus only `given()` is weaker.

## Real-Life Analogy

HttpClient is writing a letter yourself: envelope (URL), stamp (headers), paper (body), mailbox (send), reply (response).

REST Assured is a secretary who formats the letter in a house style (`given/when/then`). The postal system is still HTTP.

```text
You
  HttpClient.newHttpClient()
       ↓
   mailbox (network)
       ↓
   server
       ↓
   HttpResponse status + body
```

## Illustrated Explanation

```text
HttpClient     the browser-less caller (reusable)
HttpRequest    method + URI + headers + body
HttpResponse   statusCode + body
```

```java
HttpClient client = HttpClient.newHttpClient();
```

```text
client.send(request, bodyHandler)
        ↓
   waits for response (synchronous send)
```

There is also `sendAsync`. This course uses `send` so the story is linear.

What libraries abstract:

```text
Base URI composition
JSON parsing
Fluent assertions
Logging filters
Auth helpers
```

Underneath: still request in, response out.

## Syntax / Concept

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
```

Create a client (reuse it; do not create one per micro-call in a tight loop):

```java
HttpClient client = HttpClient.newHttpClient();
```

GET:

```java
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://example.com"))
        .GET()
        .build();

HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
);

int status = response.statusCode();
String body = response.body();
```

POST JSON:

```java
HttpRequest post = HttpRequest.newBuilder()
        .uri(URI.create("https://httpbin.org/post"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Aisha\"}"))
        .build();
```

`BodyHandlers.ofString()` means "give me the body as a `String`." Later you may use bytes or files.

Exceptions: `send` throws `IOException`, `InterruptedException`. A real method should not swallow them empty.

## Simple Example

```java
package com.training.sdet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetExample {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://example.com"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("status=" + response.statusCode());
        System.out.println(response.body().substring(0, Math.min(200, response.body().length())));
    }
}
```

If you are offline, compile it anyway. Run when you have network. `example.com` should return 200.

## Real-World Example

Banking: GET account (with a token header you do not print).

```java
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://qa.bank.example/api/accounts/123"))
        .header("Authorization", "Bearer " + token)
        .GET()
        .build();
```

```java
if (!StatusCodes.statusMatches(200, response.statusCode())) {
    throw new IllegalStateException("GET account failed: " + response.statusCode());
}
```

Never log `token`. Log status and a request id if the API sends one.

## SDET Example

A tiny API check without REST Assured:

```java
public class HealthChecker {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String baseUrl;

    public HealthChecker(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isHealthy() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/health"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );
        return StatusCodes.statusMatches(200, response.statusCode());
    }
}
```

This is what `UserApiClient` will grow into. REST Assured is optional sugar on this design.

## Break the Code

```java
URI.create("example.com"); // missing scheme — fails
```

```java
client.send(request, null); // NPE
```

```java
System.out.println(token); // security bug, not a compiler bug
```

```java
catch (Exception e) {
    // swallowed — test passes, production is on fire
}
```

Wrong URL path: 404. Your code "works." Assert the status.

## Debug

| Symptom | Check |
| --- | --- |
| `UnknownHostException` | DNS / offline / typo |
| `ConnectException` | server down, wrong port |
| 301/302 | you may need to follow redirects (`HttpClient` can be configured) |
| 401 | header missing |
| Body empty | HEAD vs GET; wrong handler |

Print `statusCode()` before parsing JSON. Parsing HTML error pages as JSON is a common second failure that hides the first.

## Student Exercise

Write `GetExample` against `https://example.com`. Assert in `main` (or a JUnit test) that status is 200 using `statusMatches`. If network is blocked, write the test with a small wrapper you can fake later.

## Challenge

POST JSON to a training endpoint your instructor provides, or to httpbin.org `/post`. Print status only (not authorization headers). Parse nothing fancy; look for `"name"` in the returned JSON string with `contains` as a crude check. Then list three reasons a JSON library is better than `contains`.

## Knowledge Check

1. What class creates the client?
2. What three types make a call?
3. Which method sends synchronously?
4. How do you read the status?
5. How do you read a string body?
6. Why reuse `HttpClient`?
7. What does REST Assured abstract?
8. Why not log Authorization?
9. What exceptions can `send` throw?
10. Why assert status before parsing JSON?

## Interview Question

**Question:** How can Java call an HTTP API without REST Assured?

A strong answer:

> Java 11+ has java.net.http.HttpClient. I create HttpClient.newHttpClient(), build an HttpRequest with URI and method, then client.send(request, BodyHandlers.ofString()). I read response.statusCode() and body(). That is what REST Assured abstracts: request building, JSON, fluent asserts. I still think in HTTP methods and status codes. I never log tokens. I reuse the client.

## Homework

Add `HealthChecker` (or `GetExample`) to `src/main/java`. Optional JUnit test that is `@Disabled` if you cannot hit a URL in CI yet. Write four bullets: request, send, status, body.

---

## Answer Key

1. `HttpClient.newHttpClient()` (or a builder).
2. `HttpClient`, `HttpRequest`, `HttpResponse`.
3. `send`
4. `response.statusCode()`
5. `BodyHandlers.ofString()` then `response.body()`
6. Connection pooling / overhead; it is designed to be reused.
7. Fluent DSL, JSON path, matchers, filters.
8. Secrets leak into CI logs and reports.
9. `IOException`, `InterruptedException`
10. Error pages are not the JSON you expect; status is the first truth.
