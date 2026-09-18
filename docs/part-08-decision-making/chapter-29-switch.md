# Chapter 29 — `switch`

## 1. Today's Goal

By the end of this lesson, you will write a **traditional** `switch` statement with `case`, `break`, and `default`.

You will choose a browser by name:

```java
switch (browser) {
    case "chrome":
        break;
    case "firefox":
        break;
    default:
        break;
}
```

You will also learn why `break` matters.

Modern **switch expressions** exist in later Java versions. They will be taught later. Do not skip the classic form. You will still read it in real codebases.

## 2. Why It Matters

`else if` chains work. When you are matching **one variable against several exact values**, `switch` can be easier to scan.

Testers match:

- browser names
- environment names
- HTTP methods in simple tools
- test types: smoke, regression, api

`switch` is not better than `if` in every case. It is another tool for "pick the matching label."

## 3. Real-Life Analogy

A mailroom with pigeonholes:

```text
Look at the label on the package
    │
    ├── chrome  → chrome bin
    ├── firefox → firefox bin
    ├── edge    → edge bin
    └── other   → default bin
```

You look at one label and drop the package in the matching slot.

If you forget to close a slot (`break`), packages can spill into the next bin. That spill is called **fall-through**.

## 4. Illustrated Explanation

```text
browser value
      │
      ▼
┌─────────────┐
│ switch      │
└─────────────┘
      │
      ├── case "chrome"   → Chrome actions, then break
      ├── case "firefox"  → Firefox actions, then break
      ├── case "edge"     → Edge actions, then break
      └── default         → unknown browser, then break
```

Traditional flow of one case:

```text
Enter matching case
        ↓
Run statements
        ↓
Hit break
        ↓
Leave the switch
```

Without `break`:

```text
Enter matching case
        ↓
Run statements
        ↓
Fall into the next case
        ↓
Run those statements too
```

That is sometimes intentional. For beginners, missing `break` is usually a bug.

## 5. Syntax / Concept

Traditional switch:

```java
switch (browser) {
    case "chrome":
        System.out.println("Starting Chrome");
        break;
    case "firefox":
        System.out.println("Starting Firefox");
        break;
    case "edge":
        System.out.println("Starting Edge");
        break;
    default:
        System.out.println("Unsupported browser");
        break;
}
```

Parts:

```text
switch (value)     the variable you inspect
case label:        if the value equals this label
break;             leave the switch
default:           if no case matched
```

Traditional `switch` compares with equality for the value in parentheses. For `String`, this is value matching of the case labels, which is what we want here. Still use `equals` in `if` conditions elsewhere. Do not write `name1 == name2` in your own comparisons.

`default` is like the final `else`. Always consider including it.

What beginners can switch on now:

- `int`
- `char`
- `String`
- later: enums

`boolean` and `long` have more history and restrictions; stick to `int`, `char`, and `String` in this course for traditional switch.

Modern Java also supports **switch expressions** that return a value and do not need `break` in the same way. We will study those in a later part. Today, write `break` in every case unless your instructor shows a planned fall-through.

## 6. Simple Example

```java
public class SwitchBrowser {

    public static void main(String[] args) {
        String browser = "chrome";

        switch (browser) {
            case "chrome":
                System.out.println("Starting Chrome");
                break;
            case "firefox":
                System.out.println("Starting Firefox");
                break;
            default:
                System.out.println("Unsupported browser");
                break;
        }
    }
}
```

Expected output:

```text
Starting Chrome
```

Change `browser` to `"safari"` and you should see `Unsupported browser`.

## 7. Real-World Example

Order status with `switch`:

```java
public class SwitchOrderStatus {

    public static void main(String[] args) {
        String status = "shipped";

        switch (status) {
            case "placed":
                System.out.println("Order placed");
                break;
            case "shipped":
                System.out.println("Order shipped");
                break;
            case "delivered":
                System.out.println("Order delivered");
                break;
            default:
                System.out.println("Unknown status");
                break;
        }
    }
}
```

Bank menu as a `char`:

```java
char choice = 'W';

switch (choice) {
    case 'D':
        System.out.println("Deposit");
        break;
    case 'W':
        System.out.println("Withdraw");
        break;
    case 'B':
        System.out.println("Balance");
        break;
    default:
        System.out.println("Invalid menu choice");
        break;
}
```

## 8. SDET Example

Environment selection:

```java
public class SwitchEnvironment {

    public static void main(String[] args) {
        String environment = "qa";

        switch (environment) {
            case "dev":
                System.out.println("Using DEV url");
                break;
            case "qa":
                System.out.println("Using QA url");
                break;
            case "prod":
                System.out.println("Using PROD url carefully");
                break;
            default:
                System.out.println("Unknown environment");
                break;
        }
    }
}
```

HTTP method:

```java
String method = "GET";

switch (method) {
    case "GET":
        System.out.println("Read data");
        break;
    case "POST":
        System.out.println("Create data");
        break;
    case "PUT":
        System.out.println("Replace data");
        break;
    case "DELETE":
        System.out.println("Delete data");
        break;
    default:
        System.out.println("Unsupported method");
        break;
}
```

`switch` is a poor fit for ranges such as `statusCode >= 500`. Use `if / else if` for ranges. Use `switch` for exact labels.

## 9. Break the Code

This switch is missing `break`. It prints too much.

```java
public class BrokenSwitch {

    public static void main(String[] args) {
        String browser = "chrome";

        switch (browser) {
            case "chrome":
                System.out.println("Starting Chrome");
            case "firefox":
                System.out.println("Starting Firefox");
            case "edge":
                System.out.println("Starting Edge");
            default:
                System.out.println("Unsupported browser");
        }
    }
}
```

Expected mistaken output:

```text
Starting Chrome
Starting Firefox
Starting Edge
Unsupported browser
```

That is fall-through.

## 10. Debug

Add `break;` at the end of each case.

```java
public class FixedSwitch {

    public static void main(String[] args) {
        String browser = "chrome";

        switch (browser) {
            case "chrome":
                System.out.println("Starting Chrome");
                break;
            case "firefox":
                System.out.println("Starting Firefox");
                break;
            case "edge":
                System.out.println("Starting Edge");
                break;
            default:
                System.out.println("Unsupported browser");
                break;
        }
    }
}
```

Now only `Starting Chrome` prints.

Debugger tip: put a breakpoint on the `switch` line, then Step Over into the case. If execution keeps going into the next case, you forgot `break`.

If nothing matches and you have no `default`, the switch silently does nothing. That can look like a dead program. Add `default`.

Traditional `case` labels are case-sensitive for `String`. `"Chrome"` will not match `"chrome"`. Normalize first if needed:

```java
String browser = "Chrome".toLowerCase();
```

## 11. Student Exercise

Write `SwitchExercise` for test types:

- `smoke`
- `regression`
- `api`

Print a one-line description of what each suite does. Include `default`.

Run all four values, including `"unit"` for default.

## 12. Challenge

Support two names for the same browser without duplicating the print statement, using planned fall-through **on purpose**:

```text
case "chrome":
case "google-chrome":
    print Starting Chrome
    break;
```

Then still handle firefox and default. In a comment, explain that this fall-through is intentional, unlike the broken example.

## 13. Knowledge Check

1. What does `switch` inspect?
2. What does `case` mean?
3. What does `break` do?
4. What is fall-through?
5. What is `default` similar to in an `if` chain?
6. Should you use `switch` for `statusCode >= 500`?
7. Are traditional `String` case labels case-sensitive?
8. Do we teach modern switch expressions in this chapter?
9. True or false: every case should usually end with `break` for beginners.
10. Name two SDET values that fit `switch` well.

## 14. Interview Question

**Question:** How does a traditional Java `switch` work, and why is `break` important?

A strong answer:

> switch compares one value against case labels. When a case matches, Java runs those statements. In a traditional switch, if I omit break, execution falls through into the next case. That is why missing break is a common bug. default handles unmatched values. Modern switch expressions are different and come later. I use switch for exact values like browser names, and if / else if for ranges.

## 15. Homework

Write `HomeworkSwitchStatus` that switches on an `int statusCode` for `200`, `201`, `404`, and `500`, plus `default`. Print tester-friendly messages.

Then write a second switch on a `String environment` of `dev`, `qa`, and `prod`.

Remember `break` every time.

---

## Answer Key

1. One value in parentheses.
2. A possible matching label.
3. It exits the switch so later cases do not run.
4. Running into the next case because `break` was missing.
5. The final `else`.
6. No. That is a range. Use `if`.
7. Yes.
8. No. They are mentioned only as a later topic.
9. True, unless fall-through is deliberate and commented.
10. Browser names and environment names, among others.
