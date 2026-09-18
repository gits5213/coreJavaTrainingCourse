# Chapter 61 — Enums

## 1. Today's Goal

By the end of this lesson, you will write an **`enum`** and use it instead of uncontrolled `String` values.

You will model:

```java
enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE
}
```

and pass `BrowserType.CHROME` into methods the way a real test config should.

## 2. Why It Matters

Strings are too free.

```java
launch("chrome");
launch("Chrome");
launch("CHROME");
launch("Chorme");
```

Four strings, one intention, three bugs.

An enum makes illegal browsers **unrepresentable**. IntelliJ will autocomplete `CHROME`, `FIREFOX`, `EDGE`. A typo does not compile.

SDET configs are full of small closed lists: browser, environment (`QA`, `STAGING`, `PROD`), user role, test status (`PASSED`, `FAILED`, `SKIPPED`). Enums are the right type.

## 3. Real-Life Analogy

A traffic light is not any string. It is RED, YELLOW, GREEN.

A t-shirt size chart: S, M, L, XL — not `"mediumish"`.

A restaurant menu: you order an item that exists. You do not whisper a dish the kitchen never listed.

A browser dropdown in a CI tool: Chrome / Firefox / Edge. The dropdown **is** an enum in the UI.

## 4. Illustrated Explanation

```text
class String        any text in the universe
enum BrowserType    only the names you listed
```

```text
BrowserType
  ├── CHROME
  ├── FIREFOX
  └── EDGE
```

Each name is a **constant object** of type `BrowserType`. There are not two different `CHROME` values. There is one.

```text
BrowserType.CHROME
     │         │
     │         └── one allowed value
     └── the type
```

Switch (you know `switch` from Part 8):

```text
switch (browser) {
    case CHROME  → launch chrome
    case FIREFOX → launch firefox
    case EDGE    → launch edge
}
```

If you add `SAFARI` later, the compiler can help you find switches that must be updated (depending on how you write them). A string `if` will not.

```text
UNCONTROLLED STRING          ENUM
"qa" / "QA" / "Qa"           Environment.QA
typos compile                typos do not compile
compare with equals          compare with ==
```

Enums may use `==` because each constant is a single instance. `equals` also works. `==` is common and fine for enums.

## 5. Syntax / Concept

Simple enum (own file `BrowserType.java` in real projects):

```java
public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE
}
```

Constants are `UPPER_SNAKE` by convention.

Use:

```java
BrowserType browser = BrowserType.CHROME;
```

Method parameter:

```java
static void launch(BrowserType browser) {
    System.out.println("Launch " + browser);
}
```

`name()` and `toString()` default to `"CHROME"`.

`valueOf` parses a **exact** name:

```java
BrowserType parsed = BrowserType.valueOf("FIREFOX");
```

`"firefox"` throws `IllegalArgumentException`. If you read config files in lowercase, convert:

```java
BrowserType.valueOf(text.trim().toUpperCase());
```

`values()` returns all constants — useful to loop browsers:

```java
for (BrowserType type : BrowserType.values()) {
    launch(type);
}
```

Enums are classes with a fixed instance list. You can add fields and methods later (`CHROME("chrome")` with a `driverName`). This lesson stays with the named list. That already beats Strings.

Do not enum things that are not closed: usernames, order ids, JSON blobs. Those stay `String` or objects.

## 6. Simple Example

```java
enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE
}

public class EnumDemo {

    public static void main(String[] args) {
        BrowserType browser = BrowserType.CHROME;
        System.out.println(browser);

        launch(browser);
        launch(BrowserType.FIREFOX);
    }

    static void launch(BrowserType browser) {
        System.out.println("Would launch " + browser);
    }
}
```

Expected output:

```text
CHROME
Would launch CHROME
Would launch FIREFOX
```

`launch("Chorme")` would not compile if the parameter is `BrowserType`.

## 7. Real-World Example

Order status:

```java
enum OrderStatus {
    NEW,
    PAID,
    SHIPPED,
    CANCELLED
}

class Order {
    private final String id;
    private OrderStatus status;

    public Order(String id) {
        this.id = id;
        this.status = OrderStatus.NEW;
    }

    public void markPaid() {
        this.status = OrderStatus.PAID;
    }
}
```

You cannot set status to `"PAID "` with a trailing space.

Bank account type: `CHECKING`, `SAVINGS`. Not `"cheking"`.

## 8. SDET Example

Browsers plus a tiny factory idea (prints only):

```java
enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE
}

public class SdetEnum {

    public static void launch(BrowserType type) {
        switch (type) {
            case CHROME -> System.out.println("Launch Chrome");
            case FIREFOX -> System.out.println("Launch Firefox");
            case EDGE -> System.out.println("Launch Edge");
        }
    }

    public static void main(String[] args) {
        for (BrowserType type : BrowserType.values()) {
            launch(type);
        }
    }
}
```

The `->` switch form is valid on the JDK this course uses. The classic `case CHROME: ... break;` form is also fine.

Environment:

```java
enum Environment {
    QA,
    STAGING,
    PROD
}

Map<Environment, String> urls = new HashMap<>();
urls.put(Environment.QA, "https://qa.shop.example");
```

Test result:

```java
enum TestStatus {
    PASSED,
    FAILED,
    SKIPPED
}
```

Compare to `String status = "pass"` vs `"PASSED"` vs `"Pass"`. Enums end that argument.

This pairs with Chapter 50: you might still have `Browser` objects. The **choice** of which family to construct can be an enum from config:

```text
config browser=chrome
   → BrowserType.CHROME
      → new ChromeBrowser()
```

String at the system boundary (property file). Enum as soon as you enter Java. Parse once.

## 9. Break the Code

Keeping strings internally:

```java
void launch(String browser) {
    if (browser.equals("chrome")) { ... }
}
```

Every caller must spell `"chrome"` correctly.

`valueOf("chrome")` when the constant is `CHROME` — throws.

Using enum for unbounded data:

```java
enum Username { JOHN, ADMIN } // will you add every customer?
```

That is a `User` class, not an enum.

Forgetting that `switch` on enum should cover all constants (or have `default`). A new constant might fall through silently if you used `default: throw` poorly or forgot cases without compiler help.

Comparing enums with `equalsIgnoreCase` as if they were strings — they are not. Compare constants.

## 10. Debug

`IllegalArgumentException: No enum constant ... chrome` — uppercase it, `trim()`, check spelling.

If IntelliJ cannot find `BrowserType`, the enum is in another package: import it.

If a config file has `"Chrome"`, normalize before `valueOf`, or write a small `fromString` that maps aliases. Keep aliases in one method, not in every test.

Debugger: you will see `CHROME` as a named constant, not a random string.

## 11. Student Exercise

Write `enum BrowserType { CHROME, FIREFOX, EDGE }`.

Write `launch(BrowserType type)` that prints a line per type using `switch`.

In `main`, call `launch` three times, then loop `BrowserType.values()`.

## 12. Challenge

Write `enum Environment { QA, STAGING, PROD }`.

Write `Map<Environment, String>` of base urls.

Write `String urlFor(Environment env)` that `get`s the url or throws if missing.

Parse a fake config string `"qa"` into `Environment` by uppercasing and `valueOf`. Print the url.

Add `enum TestStatus { PASSED, FAILED }` and a method `void print(TestStatus status)` — no strings for the status itself.

## 13. Knowledge Check

1. What is an enum?
2. Why is `BrowserType` better than `String browser`?
3. How do you write the Chrome value?
4. How do you loop all values?
5. What does `BrowserType.valueOf("EDGE")` do?
6. What happens with `valueOf("edge")`?
7. True or false: you may compare enums with `==`.
8. Name two other SDET lists that should be enums.
9. Should usernames be an enum?
10. Where may strings still appear?

## 14. Interview Question

**Question:** What is an enum in Java? When do you use one in automation?

A strong answer:

> An enum is a type with a fixed set of named constants, like BrowserType.CHROME, FIREFOX, EDGE. It is better than free Strings because typos do not compile, IDEs autocomplete, and switches can be exhaustive. I use enums for browsers, environments, roles, and test statuses. I still read strings from config files, but I convert once with valueOf after normalizing case. I do not use enums for unbounded data like user ids. Enum constants are single instances, so == is OK. Looping BrowserType.values() is a clean cross-browser loop.

## 15. Homework

Write `HomeworkEnums` with `BrowserType` and `Environment`.

Parse `"firefox"` from a fake property into `BrowserType`. Launch using `switch`.

Print all environments with `values()`.

Write five lines in a comment:

```text
Uncontrolled String fails because...
Enum helps because...
I still parse config Strings at the border because...
```

You have finished Part 16 when you refuse to pass `"Chorme"` as a browser in Java code.

---

## Answer Key

1. A type whose values are a named, fixed list of constants.
2. Only legal browsers exist; typos fail at compile time; autocomplete; no case-variant bugs.
3. `BrowserType.CHROME`
4. `for (BrowserType t : BrowserType.values()) { ... }`
5. Returns the `EDGE` constant.
6. `IllegalArgumentException` (name must match, usually uppercase).
7. True.
8. Environment, test status, HTTP method, user role (closed set).
9. No. There are too many; use `User` / `String`.
10. Config files, CSV, JSON, command line — parse into enum as soon as you enter your code.
