# Chapter 58 — Map

## 1. Today's Goal

By the end of this lesson, you will use **`Map`** and **`HashMap`** to store **keys and values**.

You will `put`, `get`, and loop entries. You will treat Map as a first-class SDET tool for **JSON objects, API headers, test data, and config**.

## 2. Why It Matters

A JSON object is a map:

```text
{
  "username": "john",
  "role": "admin"
}
```

HTTP headers are a map:

```text
Content-Type  →  application/json
Authorization →  Bearer ...
```

Test config is a map:

```text
env      → qa
baseUrl  → https://qa.shop.example
browser  → chrome
```

If you only know List, you will invent parallel lists (`keys` and `values`) and they will go out of sync. Map keeps the pair together.

This chapter is the one you will reuse most in API automation.

## 3. Real-Life Analogy

A dictionary: word → definition.

A locker room: locker number → backpack.

A phone contact list: name → number. You do not search the list of numbers to find "Alice." You look up Alice.

A coat check: ticket stub → coat.

If two people try to use locker 12, the second coat replaces the first (in a typical HashMap, duplicate keys overwrite). Unique keys, replaceable values.

## 4. Illustrated Explanation

```java
Map<String, String> user = new HashMap<>();
user.put("username", "john");
user.put("role", "admin");
```

```text
KEY            VALUE
username   →   john
role       →   admin

get("username") → john
get("email")    → null  (missing key)
```

```text
Map is not a Collection
You do not add a single element.
You put a pair.
```

Overwrite:

```text
put("role", "admin")
put("role", "guest")

role → guest     (one key, last value wins)
```

Views:

```text
keySet()     → Set of keys
values()     → Collection of values
entrySet()   → Set of key+value entries
```

`HashMap` does not promise key order. If you print keys, do not assert they come out `username` then `role`. (`LinkedHashMap` keeps insertion order — useful for stable fixtures; know the name.)

## 5. Syntax / Concept

```java
import java.util.HashMap;
import java.util.Map;

Map<String, String> headers = new HashMap<>();
headers.put("Content-Type", "application/json");
headers.put("Accept", "application/json");

String type = headers.get("Content-Type");
boolean hasAuth = headers.containsKey("Authorization");
int n = headers.size();
```

Loop entries:

```java
for (Map.Entry<String, String> entry : headers.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```

Loop keys:

```java
for (String key : headers.keySet()) {
    System.out.println(key + " → " + headers.get(key));
}
```

Missing keys: `get` returns `null`. That is easy to confuse with a value that is actually `null`. Prefer `containsKey` when you must know.

`putIfAbsent` adds only if missing (handy for default config).

Program to `Map`:

```java
Map<String, String> config = new HashMap<>();
```

`Map.of("env", "qa", "browser", "chrome")` is an unmodifiable map (even number of arguments, unique keys).

Values can be other maps or lists in real JSON. Nested structures come with parsers later. Mentally:

```text
Map<String, Object>  or nested Map / List
```

For this lesson, `Map<String, String>` is enough to build the muscle.

## 6. Simple Example

```java
import java.util.HashMap;
import java.util.Map;

public class MapDemo {

    public static void main(String[] args) {
        Map<String, String> user = new HashMap<>();
        user.put("username", "john");
        user.put("role", "admin");

        System.out.println(user.get("username"));
        System.out.println(user.get("role"));
        System.out.println("Has email? " + user.containsKey("email"));

        for (Map.Entry<String, String> entry : user.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }
}
```

Expected output (entry order may vary):

```text
john
admin
Has email? false
username=john
role=admin
```

## 7. Real-World Example

Product stock by SKU:

```java
Map<String, String> stock = new HashMap<>();
stock.put("TEA-1", "12");
stock.put("MUG-4", "0");
System.out.println("Tea left: " + stock.get("TEA-1"));
```

Later this is `Map<String, Integer>`. Same idea.

Bank: currency → rate.

```java
Map<String, String> rates = new HashMap<>();
rates.put("USD", "1.00");
rates.put("EUR", "0.92");
```

Customer id → last login time as strings for a demo.

## 8. SDET Example

**Config**

```java
Map<String, String> config = new HashMap<>();
config.put("env", "qa");
config.put("baseUrl", "https://qa.shop.example");
config.put("browser", "chrome");
System.out.println("Run against " + config.get("baseUrl"));
```

**Headers**

```java
Map<String, String> headers = new HashMap<>();
headers.put("Content-Type", "application/json");
headers.put("Accept", "application/json");
headers.put("X-Test-Run", "local");
```

**JSON-like body**

```java
Map<String, String> body = new HashMap<>();
body.put("username", "standard_user");
body.put("password", "secret_sauce");
System.out.println("POST login " + body);
```

**Test data table** (username → expected URL)

```java
Map<String, String> expectedLanding = new HashMap<>();
expectedLanding.put("standard_user", "/inventory");
expectedLanding.put("locked_out_user", "/login");
```

A test can `get(username)` instead of a long `if/else`.

**Status text lookup**

```java
Map<String, String> meanings = new HashMap<>();
meanings.put("200", "OK");
meanings.put("201", "Created");
meanings.put("404", "Not Found");
System.out.println(meanings.get("404"));
```

When you later parse JSON with a library, it will often give you `Map` and `List`. You are practicing the real shape now.

## 9. Break the Code

Parallel lists:

```java
List<String> keys = ...
List<String> values = ...
// get out of sync when you remove from one list
```

Using `get` and NPE when you assumed the key existed:

```java
String url = config.get("baseURL"); // capital URL — typo
url.length(); // NullPointerException
```

Keys are case-sensitive: `"Content-Type"` vs `"content-type"`. HTTP libraries often normalize; your HashMap will not unless you do.

```java
user.put("role", "admin");
user.put("role", "guest");
// you did not have two roles; you replaced
```

Asserting HashMap iteration order in a test.

`Map.of("a", "1", "a", "2")` — duplicate keys throw.

## 10. Debug

If `get` is `null`, print `keySet()` and look for typos and extra spaces.

If size is 1 when you put twice, you used the same key.

If you need a default:

```java
String browser = config.get("browser");
if (browser == null) {
    browser = "chrome";
}
```

Or `config.getOrDefault("browser", "chrome")`.

Debugger: expand the map; you will see key/value table, not indexes.

Never use `==` to compare string keys you `get`. Use `equals`. The key lookup itself already uses `equals`.

## 11. Student Exercise

Build `Map<String, String> headers` with `Content-Type` and `Accept`.

Print `get("Content-Type")`.

Print `containsKey("Authorization")`.

Loop `entrySet` and print `key=value`.

Then `put` `Authorization` and print size 3.

## 12. Challenge

Create a login test-data map:

- keys: `standard_user`, `locked_out_user`, `problem_user`
- values: expected message (`"ok"`, `"locked"`, `"inventory glitch"`)

Write a method `String expected(String username)` that `get`s from the map and throws `IllegalArgumentException` if the user is unknown (`containsKey` first).

In `main`, look up all three, then try an unknown user.

This is a tiny data-driven engine.

## 13. Knowledge Check

1. What does a Map store?
2. Is Map a Collection?
3. What implementation do we use here?
4. How do you insert a pair?
5. How do you read a value?
6. What does `get` return for a missing key?
7. What happens if you `put` the same key twice?
8. How do you loop keys and values together?
9. Give three SDET uses of Map.
10. True or false: `HashMap` always iterates in insertion order.

## 14. Interview Question

**Question:** What is a `HashMap`? Why do testers care?

A strong answer:

> A Map stores key/value pairs. HashMap is the usual implementation. I put a key and get the value in roughly constant time. Keys are unique; a second put on the same key replaces the value. get on a missing key returns null, so I use containsKey or getOrDefault. HashMap does not promise order. Maps are everywhere in SDET work: JSON objects, HTTP headers, config, environment properties, and tables of expected results. I declare Map<String, String> headers = new HashMap<>(); and program to the Map interface. Nested JSON is maps and lists together.

## 15. Homework

Write `HomeworkMap` with:

1. a config map (`env`, `baseUrl`, `browser`)
2. a header map
3. a user JSON-like map (`username`, `password`)

Print each with `entrySet`.

Look up `baseUrl`. In a comment, write: `JSON object ≈ Map. JSON array ≈ List.`

Optional: `getOrDefault("timeout", "10")`.

---

## Answer Key

1. Keys mapped to values.
2. No.
3. `HashMap`
4. `put(key, value)`
5. `get(key)`
6. `null`
7. The value is replaced; still one entry for that key.
8. `for (Map.Entry<K,V> e : map.entrySet())`
9. JSON bodies, headers, config/test data (also status dictionaries, env vars).
10. False.
