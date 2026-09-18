# Logging

## Goal

By the end of this lesson, you will log with levels TRACE DEBUG INFO WARN ERROR, prefer SLF4J, and refuse to log passwords, tokens, secrets, or sensitive customer data.

## Why It Matters

CI logs are copied into tickets, Slack, and Allure. A password in a log is a security incident. A missing status code in a log is a wasted hour.

SDET logs should answer: what test, what env, what URL, what status, what assertion failed — not what the user's SSN was.

## Real-Life Analogy

A ship's log.

```text
INFO     "Left port at 09:00"
WARN     "Fog; reduced speed"
ERROR    "Engine failure"
DEBUG    extra detail for the mechanic
TRACE    every bolt — too noisy at sea
```

You do not write the captain's home address in the ship's log. That is PII.

## Illustrated Explanation

```text
TRACE   extremely fine (loop internals)
DEBUG   diagnostic for developers
INFO    normal milestones (started Chrome, GET /users/1)
WARN    recoverable oddity (retry, deprecated API)
ERROR   failure (exception, unexpected status)
```

```text
Too little
  "failed"

Too much
  full JSON including password, token, card number

Just right
  POST /users status=500 test=shouldCreateUser env=qa
```

Framework: code depends on **SLF4J** API; Logback (or Log4j2) is the implementation. Tests and main code should `LoggerFactory.getLogger`. Do not mix `System.out` and three logging libraries.

## Syntax / Concept

Maven:

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.16</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.12</version>
</dependency>
```

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserApiClient {
    private static final Logger log = LoggerFactory.getLogger(UserApiClient.class);

    public void create(String username) {
        log.info("Creating user username={}", username);
        // never log password
    }
}
```

Placeholders `{}` are preferred: if the level is OFF, SLF4J can skip string concat.

```java
log.debug("Response body={}", body); // only if body cannot contain secrets
```

## Simple Example

```java
public class StatusLogger {
    private static final Logger log = LoggerFactory.getLogger(StatusLogger.class);

    public static void compare(int expected, int actual) {
        if (StatusCodes.statusMatches(expected, actual)) {
            log.info("Status match expected={} actual={}", expected, actual);
        } else {
            log.error("Status mismatch expected={} actual={}", expected, actual);
        }
    }
}
```

## Real-World Example

Banking: log account **ids** only if policy allows; never log full PAN (card numbers). Many banks forbid even account numbers in CI. When in doubt, log a hashed id or a test-generated id you already own.

E-commerce: log order id and status, not customer email if not required.

## SDET Example

```java
log.info("Starting test {} on thread {}", testName, Thread.currentThread().getName());
log.info("GET {} -> {}", path, status);
log.warn("Retrying click on {}", locator);
log.error("Test failed", exception);
```

Attach stack traces with the throwable argument: `log.error("Failed", ex);` so you get the stack.

Never:

```java
log.info("Login with {}", data.password());
log.info("Authorization: Bearer {}", token);
log.info("SSN {}", ssn);
```

Override model `toString` as in Part 51. Assume someone will log the object.

## Break the Code

```java
System.out.println(requestHeaders);
```

Headers include Authorization.

```java
log.debug(user.toString()); // record default toString includes password
```

```java
catch (Exception e) {
    log.error("error"); // no exception, no context
}
```

## Debug

If you see no logs: logback.xml missing, or level too high (INFO hides DEBUG). For CI, INFO is a good default; DEBUG on failed tests only if you can filter secrets.

If logs explode: a TRACE in a wait loop. Remove it.

## Student Exercise

Add SLF4J to the project. Log INFO when opening a URL (the URL of example.com is fine). Unit-test that `LoginData.toString()` does not contain the password string.

## Challenge

Write a `log.info` policy of 10 allowed fields and 10 forbidden fields for your training app. Include tokens, cookies, Authorization, passwords, PAN, session ids.

## Knowledge Check

1. List the five levels in order.
2. What must you never log?
3. Why SLF4J instead of only System.out?
4. How do you log an exception properly?
5. Why `{}` placeholders?
6. What is a safe login log line?
7. Why mask `toString`?
8. TRACE vs INFO in CI?
9. Can Allure reports leak if you log secrets?
10. Is retry a WARN or ERROR typically?

## Interview Question

**Question:** How do you log in an automation framework?

A strong answer:

> I use SLF4J with levels TRACE, DEBUG, INFO, WARN, ERROR. INFO for milestones: test name, URL, status code. ERROR with the exception on failure. I never log passwords, tokens, secrets, or sensitive customer data. Models mask passwords in toString. CI logs and Allure are public enough to treat as leak surfaces. println is not a logging strategy.

## Homework

Replace leftover `System.out` in framework code with SLF4J. Grep the repo for `password`, `token`, `Bearer`. Fix leaks. Commit `Add SLF4J and remove secret logging`.

---

## Answer Key

1. TRACE DEBUG INFO WARN ERROR
2. Passwords, tokens, secrets, sensitive customer data
3. Levels, files, CI, one API
4. `log.error("msg", throwable)`
5. Lazy formatting; readable
6. username + env, not password
7. Accidental logging of objects
8. INFO default; TRACE too noisy
9. Yes
10. WARN if you continue; ERROR if you fail
