# Part 44 — Java HTTP Client

Before REST Assured, see that **Java itself** can call HTTP.

```java
HttpClient client = HttpClient.newHttpClient();
```

Students should understand what automation libraries abstract: building a request, sending it, reading status and body.

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [Java HttpClient](java-http-client.md) | `HttpClient`, `HttpRequest`, `HttpResponse`, GET/POST, status, body |

## Prerequisite

Part 43. JDK 11+ includes `java.net.http` (this course uses JDK 25).

## After This Part

REST Assured will feel like a fluent layer on the same ideas, not a different universe.
