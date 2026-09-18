# HTTP and Status Codes

## Goal

By the end of this lesson, you will draw the client-to-database path, name GET POST PUT PATCH DELETE, and interpret 200, 201, 400, 401, 403, 404, and 500 like an SDET — including what to assert and what the bug might be.

## Why It Matters

Most business rules live behind APIs. UI tests are slow. API tests are usually cheaper and more stable. You cannot write them if "the request failed" is your only diagnosis.

Interviews: "Difference between 401 and 403?" If you freeze, they assume you copy-pasted REST Assured.

## Real-Life Analogy

Ordering at a restaurant by phone.

```text
You (client) dial
You speak an order (HTTP request: method, path, body, headers)
Kitchen API takes it
Chef (service) cooks
Pantry (database) provides ingredients
Voice on the phone (HTTP response: status + body)
```

```text
200   "here is your soup"
201   "we created a new reservation"
400   "you ordered in nonsense language"
401   "who is this? identify yourself"
403   "we know you; you may not enter the wine cellar"
404   "we have no item 999"
500   "the kitchen caught fire" (server bug)
```

## Illustrated Explanation

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

A request has:

```text
Method     GET / POST / PUT / PATCH / DELETE
URL        https://api.shop.com/users/42
Headers    Authorization, Content-Type
Body       JSON (often) for POST/PUT/PATCH
```

A response has:

```text
Status     200
Headers    Content-Type: application/json
Body       {"id":42,"name":"Aisha"}
```

Methods:

```text
GET      read          no body usually; should not change data
POST     create        body is the new thing
PUT      replace       send the full resource
PATCH    partial update send only changed fields
DELETE   remove
```

Status codes (curriculum list):

```text
200 OK
201 Created
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
500 Internal Server Error
```

Families:

```text
2xx   success
4xx   client problem (your test data, auth, URL)
5xx   server problem (app bug, database down)
```

`401 Unauthorized` is historically a misleading name: it usually means **unauthenticated**. `403 Forbidden` means **authenticated but not allowed**.

## Concept

REST is a style: resources (users, orders) manipulated with HTTP methods. Real APIs are messy. Still, SDETs assert:

- status code
- important JSON fields
- sometimes headers (e.g. `Location` on 201)

Do not assert the entire giant payload unless you must. Assert what the business cares about.

Idempotency (interview bonus): GET should be safe to retry. DELETE often too. POST of "create order" may create two orders if retried. Tests that retry POST blindly can create duplicate data.

## Simple Example (Java model of a response)

You are not calling the network yet. You are modeling the idea.

```java
public record HttpExchange(
        String method,
        String path,
        int status
) {
    public boolean isSuccess() {
        return status >= 200 && status < 300;
    }
}

public class HttpExchangeTest {
    // JUnit or mental test
    public static void main(String[] args) {
        HttpExchange getUser = new HttpExchange("GET", "/users/42", 200);
        HttpExchange missing = new HttpExchange("GET", "/users/0", 404);
        System.out.println(getUser.isSuccess()); // true
        System.out.println(missing.isSuccess()); // false
    }
}
```

Status helper you already know:

```java
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
}
```

Use it in API tests: `statusMatches(201, response.statusCode())`.

## Real-World Example

Banking: `POST /transfers` with JSON `{from, to, amount}`.

```text
201 or 200   transfer accepted
400          amount negative
401          missing token
403          user cannot transfer from that account
404          account id does not exist
500          ledger service crashed
```

E-commerce: `GET /orders/123` → 200 with status `SHIPPED`. `GET /orders/999` → 404.

## SDET Example

What to test at API level:

```text
Create user POST /users     201 + id returned
Login POST /auth            200 + token (never log token)
Get user GET /users/{id}    200 + name
Bad password                401
User cannot see other user  403
Unknown id                  404
```

UI login might still be needed for cookie/CSRF. Many rules should not wait for Selenium.

```text
BAD
"API is down" as a bug title

GOOD
POST /users returned 500
body: {"error":"NullPointerException at UserService.java:88"}
request id: abc
```

## Break the Code (thinking bugs)

```text
Test expects 200
API correctly returns 201 Created
```

The API is fine. The test is wrong. Know 201.

```text
Test expects 401 for missing token
API returns 403
```

Might be a product bug or a gateway quirk. Do not "fix" the test until you know the contract.

```text
GET /users accidentally creates users
```

Wrong method used on server. Your test should catch unexpected data change.

## Debug

When an API test fails:

1. Status code first.
2. Then body error message.
3. Then headers (WWW-Authenticate, request id).
4. Then ask: is QA environment down (500 on every call)?

```text
401   token missing/expired/wrong — test data or clock
403   role wrong — factory created a guest
404   id wrong or data not seeded
400   JSON schema, missing field
500   not your locator; it is server-side — still a valid fail
```

You will use HttpClient and REST Assured to *see* these. Today you learn to *read* them.

## Student Exercise

Fill a table with method, example path, and one success status:

| Action | Method | Path | Success |
| --- | --- | --- | --- |
| List users | | | |
| Create user | | | |
| Replace user | | | |
| Change email only | | | |
| Remove user | | | |

Then write one sentence each for 401 vs 403 vs 404.

## Challenge

Design test cases (no code) for `POST /orders` for an online shop. Include happy path, 400, 401, 404 product, 500. Write expected status and one JSON field to assert.

## Knowledge Check

1. Draw the six-step flow from client to response.
2. Name the five HTTP methods from this part.
3. What does GET mean?
4. POST vs PUT vs PATCH?
5. List the seven status codes taught.
6. 401 vs 403?
7. Is 500 a test bug or an app bug (usually)?
8. What should an SDET assert on a create-user call?
9. Why learn this before REST Assured?
10. Why are API tests often cheaper than UI tests?

## Interview Question

**Question:** How does an HTTP API call work, and which status codes do you care about?

A strong answer:

> A client sends an HTTP request to an API. The API calls a service, which may read or write a database, then returns an HTTP response. Methods include GET, POST, PUT, PATCH, DELETE. I assert status codes: 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized (not authenticated), 403 Forbidden (not allowed), 404 Not Found, 500 Internal Server Error. 4xx often means my test data or auth. 5xx is usually a server defect. I diagnose status then body, not by rerunning Selenium.

## Homework

Using a public demo API if you have network (for example httpbin.org or a training API your instructor names), use a browser or `curl` to GET a URL and read the status. If you cannot, stay offline: memorize the table and write flashcards. Part 44 will call HTTP from Java.

---

## Answer Key

1. Client → HTTP Request → API → Service → Database → HTTP Response
2. GET POST PUT PATCH DELETE
3. Read a resource; should not modify.
4. POST create; PUT replace whole; PATCH partial.
5. 200 201 400 401 403 404 500
6. 401 not authenticated; 403 authenticated but not permitted.
7. Usually app/infrastructure; the test is correctly failing.
8. Status 201/200, id present, critical fields; not secrets in logs.
9. So libraries do not become magic.
10. Faster, more stable, closer to business rules.
