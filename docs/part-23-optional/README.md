# Part 23 — Optional

`Optional<User>` is a box that **might** contain a `User` and might be **empty**.

```text
Optional.of(user)      →  there is a user
Optional.empty()       →  there is not
```

It makes "no result" visible in the type system instead of a surprise `null`.

## Warning

**Do not use Optional everywhere.** It is not a new `null` for every field. It is best as a **return type** when a method might not find something: `findUser(id)`, stream `.max()`, "first matching test."

Do not write `Optional<String> username` as a field on every class "for safety." That adds noise. Do not `Optional.ofNullable` every parameter.

## Chapter in This Part

| Chapter | Topic |
| --- | --- |
| [Chapter 75](chapter-75-optional.md) | `Optional<User>`, empty vs present, when not to use it |

## Prerequisite

Methods, objects, `null` as a concept. Streams' `.max()` returning Optional will click better after this.
