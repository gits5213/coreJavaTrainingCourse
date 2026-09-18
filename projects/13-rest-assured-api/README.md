# Project 13 — REST Assured API Framework

## Goal

Write `given` / `when` / `then` API tests against a **local** HTTP server. Tests hit `localhost` only — no public internet, no API tokens.

## Concepts this practices

- REST Assured fluent API
- HTTP status and JSON body assertions
- A `User` record as the response model
- An optional `ApiClient` wrapper so tests do not scatter URLs
- Never logging passwords, tokens, or secrets (there are none here)

## How to run

From this project directory:

```bash
mvn test
```

`@BeforeAll` starts `com.sun.net.httpserver.HttpServer` on an ephemeral port and serves `{"username":"john","role":"admin"}` at `/users/1`.

## Expected output

```text
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## What success looks like

You can map `given` → setup, `when` → HTTP call, `then` → assertions. The client returns a `User` record. If this were a real environment, tokens would live in env vars, never in Git and never in logs.

## Stretch challenge

Add `/users/2` as 404 and assert REST Assured `statusCode(404)` from the same local server.
