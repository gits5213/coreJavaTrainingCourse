# API Coding Exam

**Level:** after Parts 43–45 (HTTP, HttpClient, REST Assured)  
**Format:** IDE, network allowed to the training host only  
**Timebox:** 25 minutes

Suggested weight in the [final exam](final-exam-guide.md): **10%**.

---

## Prompt

Using Java `HttpClient` **or** REST Assured (student's choice; examiner may require one):

1. Send an HTTP **GET** to the URL the instructor writes on the board (default practice host: `https://example.com/` or a local training API).
2. Assert the status code is **200** using either:
   - `StatusCodes.statusMatches(200, actual)`, or
   - REST Assured `.statusCode(200)`
3. If the training API returns JSON, assert **one body field** (for example `username` or `title`). Do not parse JSON with `split`.
4. If you use REST Assured, say out loud what `given`, `when`, and `then` mean.
5. Show the habit: **do not log secrets** (Authorization headers, tokens, passwords), even if this API has none.

Optional stretch (if the API supports it): one POST with a small JSON body and assert **201** or **200**, still no secret logging.

---

## HttpClient sketch (acceptable)

```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://example.com/"))
        .GET()
        .build();
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
int actual = response.statusCode();
if (!StatusCodes.statusMatches(200, actual)) {
    throw new AssertionError("expected 200 but was " + actual);
}
```

Prefer JUnit:

```java
assertTrue(StatusCodes.statusMatches(200, actual));
```

## REST Assured sketch (acceptable)

```java
given()
    .baseUri("https://example.com")
.when()
    .get("/")
.then()
    .statusCode(200);
```

`given` = setup, `when` = action, `then` = validation (arrange / act / assert).

Wrap in a small `ExampleApiClient` if the student has time — tests should not paste URLs in 20 places, but **do not** fail the 25-minute exam for a missing client wrapper if the request and assertion are correct.

---

## HTTP knowledge the examiner may ask

| Code | Meaning |
| --- | --- |
| 200 | OK |
| 201 | Created |
| 400 | Bad request |
| 401 | Unauthenticated |
| 403 | Forbidden |
| 404 | Not found |
| 500 | Server error |

"The UI looks fine but GET /orders/1 is 500" — the bug may be the API or DB, not Selenium.

---

## Acceptance criteria

- [ ] A real GET is sent (not a hard-coded `int actual = 200` with no HTTP)
- [ ] Status is asserted
- [ ] Failure would print or assert expected vs actual
- [ ] No tokens in `System.out` or logs
- [ ] Student can explain 401 vs 403 vs 404 in one minute

---

## Rubric (10 points)

| Look for | Points |
| --- | --- |
| Request sent correctly | 3 |
| Status assertion | 3 |
| Body field **or** clear reason there is no JSON | 2 |
| Secret hygiene + given/when/then or HttpClient pipeline | 2 |

Fail: fake status without I/O; empty catch around the call; printing `Authorization`.
