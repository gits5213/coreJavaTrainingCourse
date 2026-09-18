# Part 19 — JSON

APIs do not send Java objects over the wire. They send **text** in a format called **JSON** (JavaScript Object Notation).

```text
{
  "username": "john",
  "role": "tester"
}
```

That looks a bit like JavaScript. It is a **data format**, not a Java class.

## Hard Truth About Java's Standard Library

**Java's standard library does not give you a JS-like JSON object model** (`json.username` with no extra library). There is no `JSON.parse` in `java.lang` that returns a magic map with dots.

To turn JSON into a Java object, teams use a library. This course introduces **Jackson** (`ObjectMapper`) as the industry default you will see in SDET API work. You will add it with Maven later when the project grows. In this part we show the **idea** and the code shape so you are not shocked.

```text
JSON file or String
        │
        ▼
   ObjectMapper (Jackson)
        │
        ▼
   Java object  (User with username and role)
```

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [JSON Introduction](chapter-json-introduction.md) | What JSON is, why Java needs a library, `User` mapping |

## Prerequisite

You understand classes/fields (OOP parts of the course), `String`, and reading files. If records (Part 26) are new, a simple `User` class is enough.

## SDET Connection

Almost every REST test asserts on JSON. Status `200` is not enough. You must prove `"role": "admin"` arrived.

Do not parse JSON with random `split` and `indexOf` as a career strategy. That breaks when the server adds a field or whitespace.
