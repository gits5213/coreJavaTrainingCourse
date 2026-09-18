# Part 14 — Collections

An **array** has a fixed length. Real tests grow: more users, more header keys, more browsers, more retry attempts.

The **Java Collections Framework** gives you ready-made boxes for groups of objects.

```text
                    Iterable / Collection
                           │
           ┌───────────────┼───────────────┐
           ▼               ▼               ▼
         List             Set            Queue
      ordered           unique         processing
      (allow dups)      (no dups)        order

Map is a separate family: keys → values
```

**Map is not a Collection.** It is a sibling idea, and for SDET work it is often the most important of the four.

## Chapters in This Part

| Chapter | Topic | You will be able to... |
| --- | --- | --- |
| [Chapter 55](chapter-55-why-collections.md) | Why collections? | See the family tree and why arrays are not enough |
| [Chapter 56](chapter-56-list.md) | `List` / `ArrayList` | Keep ordered test users and browsers |
| [Chapter 57](chapter-57-set.md) | `Set` / `HashSet` | Store unique ids and tags |
| [Chapter 58](chapter-58-map.md) | `Map` / `HashMap` | Hold JSON-like keys and values, headers, config |
| [Chapter 59](chapter-59-queue.md) | `Queue` | Process jobs in order |
| [Chapter 60](chapter-60-collection-choice.md) | Choosing | Pick List / Set / Map / Queue on purpose |

## The Choice Card (memorize)

| If you need... | Use... |
| --- | --- |
| Order, index, duplicates OK | **List** |
| Uniqueness | **Set** |
| Key → value | **Map** |
| Processing order (line / jobs) | **Queue** |

## SDET Connection

```text
List  → browsers, steps, users to try
Set   → unique order ids, unique failed tests
Map   → JSON body, API headers, test config, environment values
Queue → jobs to retry, messages to consume
```

When an API returns JSON, you are looking at nested maps and lists. Learn Map well.

## Prerequisite

Arrays (Part 11) and objects (Part 12). Collections hold **objects**. `List<int>` is illegal; `List<Integer>` comes with wrapper classes in Part 15. This part will use `List<String>` and `Map<String, String>` first so you can think without wrappers.
