# Part 22 — Streams

A **stream** is a pipeline over data: not a new collection, but a way to **process** a collection in steps.

```text
Collection  →  Stream  →  Filter  →  Transform  →  Collect
```

Example you will type:

```java
statusCodes.stream()
        .filter(code -> code != 200)
        .toList();
```

That is "give me the status codes that are not 200."

## Chapters in This Part

| Chapter | Topic | Picture |
| --- | --- | --- |
| [Chapter 72](chapter-72-stream-api.md) | Stream pipeline, `filter`, `toList` | Failed status codes |
| [Chapter 73](chapter-73-map.md) | `map` | `String::toUpperCase` |
| [Chapter 74](chapter-74-reduce.md) | `reduce` | Sum execution durations |

Chapter 74 is **later conceptually** than filter/map. You still get a full lesson. If `reduce` feels abstract, finish 72 and 73, use streams at work, then return to 74. Do not skip the chapter forever; interviews ask it.

## Prerequisite

Lists, lambdas. Streams without lambdas are mostly unreadable.

## SDET Connection

- Filter non-200 responses
- Map JSON users to usernames
- Sum durations for a performance report

Prefer **readable** pipelines. A 12-step stream that nobody can debug is not clever.
