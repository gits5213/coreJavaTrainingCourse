# Project 8 — REST API Client

## Goal

Call a public HTTP API with **Java `HttpClient` only** (no REST Assured yet). Parse a JSON body into a `User` record and print `id` plus name/username. Treat any status other than 200 as a failure.

## Network required

**This project needs network access.** It calls `https://jsonplaceholder.typicode.com/users/1` at runtime. Compile still works offline. The live GET will fail without the internet (timeout, DNS, or connection refused).

No API keys. No secrets. Do not add Authorization headers.

## Concepts this practices

- HTTP (`GET`, status codes, response body)
- JSON as text mapped into a Java model
- Records
- Exception handling for transport errors and non-200 responses

## How to run

From this project directory, **online**:

```bash
mvn -q compile exec:java
```

JSON parsing tests do not need the network:

```bash
mvn test
```

## Expected output

```text
GET https://jsonplaceholder.typicode.com/users/1
status=200
id=1
name=Leanne Graham
username=Bret
GET https://jsonplaceholder.typicode.com/users/999
Non-200 response: 404
```

The public demo API can change names rarely. Success is: status 200, a numeric id, and both name fields printed. The second call must not crash the JVM; it must report the non-200 status.

## What success looks like

You used `java.net.http.HttpClient`, `HttpRequest`, and `HttpResponse`. A `User` record holds the parsed fields. Non-200 is handled explicitly. REST Assured is not on the classpath.

## Stretch challenge

Add a timeout (`Duration.ofSeconds(10)`), send `Accept: application/json`, and parse `email` as well. Then fetch `/users` (the list) and print how many users came back.
