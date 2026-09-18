# Part 43 — API Fundamentals

Teach HTTP **before** REST Assured. If you only memorize `given().when().then()`, you cannot debug a 401.

```text
Client
 ↓
HTTP Request
 ↓
API
 ↓
Service
 ↓
Database
 ↓
HTTP Response
```

Methods: GET POST PUT PATCH DELETE. Status codes: 200 201 400 401 403 404 500.

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [HTTP and Status Codes](http-and-status-codes.md) | Requests, methods, status codes, SDET assertions |

## Prerequisite

You know methods return values and tests assert. You do not need a live server for the ideas; we will still write Java that *names* the concepts.

## After This Part

Java HttpClient (Part 44) then REST Assured (Part 45).
