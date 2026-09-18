# JSON Introduction — From Text to a User Object

## 1. Today's Goal

By the end of this lesson, you will explain JSON as a **text format** for data, explain that **Java's standard library does not provide a JavaScript-style JSON object**, and describe the Jackson path:

```text
JSON file  →  ObjectMapper  →  Java object
```

You will map:

```json
{
  "username": "john",
  "role": "tester"
}
```

to a `User` with `username` and `role`.

You do not need to master every Jackson annotation today. You need the mental model.

## 2. Why It Matters

UI tests click. API tests read bodies. Bodies are often JSON.

If you treat JSON as "some string," you will write brittle `contains("john")` checks. `"johnny"` also contains `"john"`. A real assertion uses a **User** object and `equals` on fields.

SDET interviews expect: "I deserialize JSON to objects" (or to a tree of nodes). They do not expect you to invent a parser with `substring`.

```text
Status code 200     →  the HTTP envelope looked OK
JSON body           →  the business data
Java User           →  what your assertions speak
```

## 3. Real-Life Analogy

A shipping label printed as text vs a filled-out form object.

```text
JSON     →  the printed label (portable text)
Java User →  the structured form in your warehouse system
Mapper   →  the clerk who copies fields into the form
```

JSON is the envelope language many systems agreed on. Java objects are how your program thinks.

A menu in a foreign restaurant: JSON is the menu language. Jackson is the translator. `User` is your native language of fields.

Java does **not** come with that translator built into the JDK the way JavaScript's `JSON.parse` exists in browsers. You bring Jackson (or Gson, or others). This course standardizes on Jackson because API testing and Spring shops use it constantly.

## 4. Illustrated Explanation

JSON structure:

```text
{
  "username": "john",     ← string field
  "role": "tester"        ← string field
}

object  { }
array   [ 1, 2, 3 ]
string  "text"
number  200
boolean true / false
null    null
```

Java side:

```text
class User {
    String username;
    String role;
}
```

Pipeline:

```text
user.json
   │  Files.readString  (you already know this)
   ▼
String json
   │  objectMapper.readValue(json, User.class)
   ▼
User object
   username = john
   role     = tester
   │
   ▼
assert role is tester
```

Without a mapper:

```text
Java stdlib
    │
    └── no json.username
    └── no built-in ObjectMapper
    └── you could treat JSON as String only (weak)
```

Field names must match (by default). `"userName"` in JSON vs `username` in Java is a classic mapping bug.

## 5. Syntax / Concept

**JSON** is text. Quotes around keys and string values are required in JSON (unlike some JavaScript looseness).

**POJO** (Plain Old Java Object): a class with fields (and usually getters/setters, or public fields, or a record later) that Jackson can fill.

Jackson core type:

```java
ObjectMapper mapper = new ObjectMapper();
User user = mapper.readValue(json, User.class);
```

From a file:

```java
User user = mapper.readValue(Path.of("testdata", "user.json").toFile(), User.class);
```

Or:

```java
String json = Files.readString(Path.of("testdata", "user.json"));
User user = mapper.readValue(json, User.class);
```

`readValue` throws checked exceptions (`JsonProcessingException` / `IOException` depending on overload). Handle or declare. Do not empty-catch.

Writing Java → JSON (preview):

```java
String json = mapper.writeValueAsString(user);
```

**JavaScript vs Java:** In JS you might `JSON.parse` and use `obj.username`. In Java you define a type. That is more ceremony and more safety.

Adding Jackson belongs in Maven (`com.fasterxml.jackson.core:jackson-databind`). Until the course's Maven part, read the examples as the target shape. Typing them requires the dependency.

For a `User` Jackson can bind:

```java
public class User {
    public String username;
    public String role;
}
```

Public fields are the shortest teaching version. Real code often uses private fields plus getters/setters or a **record** (Part 26).

## 6. Simple Example

`testdata/user.json`:

```json
{
  "username": "john",
  "role": "tester"
}
```

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonUserDemo {

    public static void main(String[] args) throws Exception {
        String json = Files.readString(Path.of("testdata", "user.json"));
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(json, User.class);
        System.out.println(user.username);
        System.out.println(user.role);
    }
}

class User {
    public String username;
    public String role;
}
```

Expected output:

```text
john
tester
```

If Jackson is not on the classpath, IntelliJ cannot resolve `ObjectMapper`. That is expected until you add the library. You can still type the JSON file and the `User` class today.

## 7. Real-World Example

An online store `Order` JSON:

```json
{
  "id": "A-100",
  "status": "PAID"
}
```

A bank user:

```json
{
  "username": "ada",
  "role": "customer"
}
```

Checkout does not send a Java `User` over HTTP. It sends JSON text. Each language on each side maps to its own objects.

If the shop adds `"role": "admin"` by accident on a customer payload, tests that only check HTTP 200 will miss a security bug. Field asserts matter.

## 8. SDET Example

```java
public class UserJsonTest {

    public static void main(String[] args) throws Exception {
        String json = """
                {"username":"john","role":"tester"}
                """;
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(json, User.class);

        if (!"john".equals(user.username) || !"tester".equals(user.role)) {
            throw new AssertionError(
                    "TEST FAILED — username=" + user.username + " role=" + user.role);
        }
        System.out.println("TEST PASSED");
    }
}
```

Text blocks `"""` (modern Java) keep JSON readable in tests. Files are better for larger fixtures.

Bad SDET:

```java
if (json.contains("tester")) { // "protester" would also pass
    System.out.println("TEST PASSED");
}
```

Worse:

```java
try {
    mapper.readValue(json, User.class);
} catch (Exception e) {
    // invalid JSON, test still green
}
```

Invalid JSON is TEST FAILED.

## 9. Break the Code

JSON:

```json
{
  "username": "john",
  "role": "tester",
}
```

Trailing comma is **invalid JSON** (unlike some JavaScript). Jackson throws. The test should fail.

Mismatched names:

```java
public String userName; // JSON has "username"
```

`userName` stays `null`. Then you assert on null and panic. Or you NPE.

Treating JSON as Java:

```java
User u = { username: "john" }; // not Java
```

People coming from JS mix the grammars. JSON is data. Java is program.

Catching parse errors empty: the API returned HTML error page. You "parsed" nothing. CI green.

## 10. Debug

If `username` is null after mapping:

1. Print the raw JSON string.
2. Check key spelling and quotes.
3. Check Java field names.
4. Invalid JSON: trailing commas, single quotes `'john'` (JSON wants double quotes).

`JsonMappingException` / `JsonParseException`: read the message. It often has a line number.

Debugger: inspect the `User` after `readValue`. If the whole `readValue` throws, the text is not JSON or not an object matching `User`.

```text
Is it JSON at all?  (starts with { or [ )
Do keys match fields?
Did we empty-catch a parse failure?
```

## 11. Student Exercise

Write a `user.json` with your own username and role `student`.

Write a `User` class with public `username` and `role`.

If you have Jackson on the classpath, map and print both fields. If not, write the JSON file, the `User` class, and a comment showing the `ObjectMapper` two lines. Still split the JSON with your eyes: which part is username?

## 12. Challenge

Two files: `testdata/users/john.json` and `testdata/users/admin.json` (`role` tester vs admin).

Write `loadUser(String name)` using `Path.of("testdata", "users", name + ".json")`.

Assert admin's role is `admin`. Print TEST FAILED with expected/actual if not.

Handle missing file vs invalid JSON with **separate** messages. No empty catch.

## 13. Knowledge Check

1. What is JSON in one sentence?
2. Does the JDK provide a JS-like `json.username` object model?
3. What library does this course introduce for mapping?
4. What is `ObjectMapper.readValue` for?
5. Map the sample JSON to which two `User` fields?
6. Why is `json.contains("john")` a weak assertion?
7. Why do trailing commas break JSON?
8. What should happen if the body is not JSON?
9. True or false: JSON is a Java class.
10. File → mapper → object: name the middle step's job.

## 14. Interview Question

**Question:** How do you work with JSON in Java tests?

A strong answer:

> JSON is a text format for objects, arrays, strings, numbers, booleans, and null. Java's standard library does not give me a JavaScript-style JSON object, so I use a library, usually Jackson ObjectMapper. I read the JSON from a string or file and deserialize it to a Java class, for example User with username and role. Then I assert on fields. I do not rely on contains() for correctness, and I never swallow parse exceptions. Invalid JSON or a missing fixture is a failed test.

## 15. Homework

Draw the pipeline `JSON file → ObjectMapper → User` on paper.

Write example JSON for a `User` and for an `Order` with `id` and `status` (text is enough).

In notes: "Java is not JavaScript. JSON is data. Jackson maps data to objects."

When Maven arrives in the course, add `jackson-databind` and run the simple example for real.

---

## Answer Key

1. A portable text format for structured data.
2. No.
3. Jackson (`ObjectMapper`).
4. Turning JSON text into a Java object of a given class.
5. `username`, `role`
6. Other strings can contain the same letters; structure is ignored.
7. JSON spec does not allow them; parsers throw.
8. The test fails; you report parse error plus raw body.
9. False.
10. Translate / bind fields from text into the Java object.
