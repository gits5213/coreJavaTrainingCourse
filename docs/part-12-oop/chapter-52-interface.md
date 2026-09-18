# Chapter 52 — Interface

## 1. Today's Goal

By the end of this lesson, you will write an **interface** as a contract, then implement it.

You will create `TestDataProvider` and `JsonDataProvider` so tests can ask for data without caring whether it came from JSON, CSV, or a hard-coded list.

## 2. Why It Matters

A test needs user data. Today that data lives in a JSON file. Tomorrow it lives in an API. If tests call `JsonFileReader.readUser()` everywhere, switching sources is a rewrite.

An interface says **what** you can ask:

```text
TestDataProvider
  User getUser(String key)
```

Implementations say **how**:

```text
JsonDataProvider
CsvDataProvider
FakeDataProvider   ← perfect for unit tests
```

This is abstraction you can feel. It is also how Selenium's `WebDriver` works: your test talks to `WebDriver`, Chrome or Firefox implements it.

A class can **implement multiple interfaces**. It can **extend only one class**. That is a major reason interfaces exist.

## 3. Real-Life Analogy

A power plug standard.

```text
Interface:  three pins, 120 volts
Implementations:  toaster, lamp, phone charger
```

The wall does not know about toasters. It knows the plug contract.

A job contract: "You will provide test data for a key." JSON specialist and CSV specialist both sign it.

A translator interface: `translate(text)`. Human translator and machine translator both implement it. The travel app stores a `Translator`, not a brand.

## 4. Illustrated Explanation

```text
        TestDataProvider  (interface)
           getUser(key)
                 ▲
      ┌──────────┴──────────┐
      │                     │
JsonDataProvider      CsvDataProvider
  reads .json           reads .csv
```

```text
class JsonDataProvider implements TestDataProvider
```

The implementing class must provide every abstract method of the interface (unless the class is abstract).

```text
Test talks to the interface

TestDataProvider data = new JsonDataProvider();
User user = data.getUser("standard");
```

Swap implementation:

```text
TestDataProvider data = new CsvDataProvider();
```

Tests that used `getUser` do not change.

Multiple contracts:

```text
class JsonDataProvider implements TestDataProvider, AutoCloseable
```

(You do not need `AutoCloseable` today. See the shape.)

Interface vs abstract class:

```text
INTERFACE
  contract
  a class can implement many
  no object construction of the interface itself
  fields: only constants (public static final) in classic use

ABSTRACT CLASS
  shared code + holes
  a class can extend only one
  can have instance fields and constructors
```

Java later added default methods on interfaces. You may see `default` in frameworks. This lesson stays with a clean contract: methods to implement.

## 5. Syntax / Concept

```java
interface TestDataProvider {
    User getUser(String key);
}
```

Methods in an interface are `public abstract` by default. You may write only `User getUser(String key);`.

Implement:

```java
class JsonDataProvider implements TestDataProvider {
    @Override
    public User getUser(String key) {
        // fake JSON for the lesson
        if (key.equals("standard")) {
            return new User("standard_user", "secret_sauce");
        }
        throw new IllegalArgumentException("unknown key: " + key);
    }
}
```

Use `@Override` on implementations too.

A class that implements an interface **is a** that type for polymorphism:

```java
TestDataProvider provider = new JsonDataProvider();
```

Naming: interfaces are often adjectives or capability names (`Runnable`, `TestDataProvider`, `PaymentGateway`). Avoid `ITestDataProvider` unless your workplace standard requires the `I` prefix. This course does not.

One interface, one idea. Do not make `GodProvider` with fifty methods. Split if tests only need a slice.

## 6. Simple Example

```java
class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

interface TestDataProvider {
    User getUser(String key);
}

class JsonDataProvider implements TestDataProvider {
    @Override
    public User getUser(String key) {
        System.out.println("Pretend to parse JSON for " + key);
        if ("standard".equals(key)) {
            return new User("standard_user", "secret_sauce");
        }
        if ("locked".equals(key)) {
            return new User("locked_out_user", "secret_sauce");
        }
        throw new IllegalArgumentException("No JSON user: " + key);
    }
}

public class InterfaceDemo {

    public static void main(String[] args) {
        TestDataProvider data = new JsonDataProvider();
        User user = data.getUser("standard");
        System.out.println(user.getUsername());
    }
}
```

Expected output:

```text
Pretend to parse JSON for standard
standard_user
```

## 7. Real-World Example

Notifications:

```java
interface Notifier {
    void send(String to, String message);
}

class EmailNotifier implements Notifier {
    @Override
    public void send(String to, String message) {
        System.out.println("Email to " + to + ": " + message);
    }
}

class SmsNotifier implements Notifier {
    @Override
    public void send(String to, String message) {
        System.out.println("SMS to " + to + ": " + message);
    }
}

public class BankAlerts {

    public static void notifyOverdraft(Notifier notifier, String customer) {
        notifier.send(customer, "Account overdrawn");
    }

    public static void main(String[] args) {
        notifyOverdraft(new EmailNotifier(), "alice@example.com");
        notifyOverdraft(new SmsNotifier(), "+15551212");
    }
}
```

The bank alert does not care about SMTP vs SMS gateways.

## 8. SDET Example

Two providers, one test helper. `User` is the same class as in the simple example (username, password, getters). Include it in the same file when you type this.

```java
class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
}

interface TestDataProvider {
    User getUser(String key);
}

class JsonDataProvider implements TestDataProvider {
    @Override
    public User getUser(String key) {
        System.out.println("JSON lookup " + key);
        return new User(key + "_json", "Pass1234");
    }
}

class FakeDataProvider implements TestDataProvider {
    @Override
    public User getUser(String key) {
        System.out.println("Fake lookup " + key);
        return new User("fake_" + key, "fake");
    }
}

public class DataDrivenShape {

    public static void loginTest(TestDataProvider data) {
        User user = data.getUser("standard");
        System.out.println("Login as " + user.getUsername());
    }

    public static void main(String[] args) {
        loginTest(new JsonDataProvider());
        loginTest(new FakeDataProvider());
    }
}
```

Use `FakeDataProvider` when you test the login *flow* and do not want files on disk. Use `JsonDataProvider` in the suite that must match real fixtures.

This is the same idea as `WebDriver driver = new ChromeDriver();` — interface (or parent type) on the left, concrete engine on the right.

## 9. Break the Code

Forgetting `implements` and hoping methods magically count:

```java
class JsonDataProvider {
    public User getUser(String key) { ... }
}
// JsonDataProvider is not a TestDataProvider
TestDataProvider data = new JsonDataProvider(); // does not compile
```

Not implementing a method:

```java
class JsonDataProvider implements TestDataProvider {
    // missing getUser
}
```

Putting test-only methods on the interface that only JSON needs (`parseFile(Path)`). Then CSV cannot implement honestly. Keep the interface small. Put extra methods on the concrete class.

Using `extends` for an interface on a class: classes `implements` interfaces. Interfaces `extend` other interfaces. Classes `extend` classes.

```java
class JsonDataProvider extends TestDataProvider { } // wrong if TestDataProvider is an interface
```

## 10. Debug

`does not override abstract method getUser` — implement it, match the signature, make it `public`.

`incompatible types: JsonDataProvider cannot be converted to TestDataProvider` — you forgot `implements`.

If every implementation copy-pastes the same helper, you may want an abstract class **plus** the interface, or a small utility. Do not bloat the interface with that helper unless every implementor needs it as part of the contract.

Debugger: the variable type is the interface; the object is `JsonDataProvider`. Stepping into `getUser` lands in the concrete class.

## 11. Student Exercise

Write `User` (username, password).

Write `interface TestDataProvider { User getUser(String key); }`

Write `JsonDataProvider` that returns standard and locked users.

In `main`, declare `TestDataProvider data = new JsonDataProvider();` and print both users.

## 12. Challenge

Add `CsvDataProvider implements TestDataProvider` (you can fake CSV with `if` as well).

Write `static User loadStandard(TestDataProvider data)` that only calls `getUser("standard")`.

Call it with JSON and CSV providers.

Then write `interface ScreenshotTaker { void take(String name); }` and a `DummyScreenshotTaker`. Optional stretch: a class that implements **both** `TestDataProvider` and `ScreenshotTaker` — only if that class honestly does both jobs. If that feels forced, write a comment saying you would keep two objects (composition). That honesty is the challenge.

## 13. Knowledge Check

1. What is an interface in working language?
2. What keyword does a class use to take on an interface?
3. Are interface methods `public` by default?
4. Can a class implement more than one interface?
5. Can a class extend more than one class?
6. Write `TestDataProvider data = new JsonDataProvider();` — why is the left side the interface?
7. Why is `FakeDataProvider` useful?
8. True or false: you can `new TestDataProvider()` if it is an interface.
9. When do you prefer an abstract class over an interface?
10. How does this relate to `WebDriver driver = new ChromeDriver()`?

## 14. Interview Question

**Question:** What is an interface? How does it differ from an abstract class?

A strong answer:

> An interface is a contract: it names methods a class must provide. A class implements the interface and can implement several interfaces, while it can extend only one class. I cannot instantiate the interface; I store it as the variable type and construct a concrete class, like TestDataProvider data = new JsonDataProvider(). Abstract classes are for shared fields and method bodies plus optional abstract holes. If I mainly need a capability — getUser, pay, takeScreenshot — I start with an interface. In Selenium, tests code against WebDriver so Chrome and Firefox can be swapped.

## 15. Homework

Type the simple example plus `FakeDataProvider`.

Write `loginTest(TestDataProvider data)` and run it with both providers.

In notes, list three SDET interfaces you expect to meet later (`WebDriver`, a wait, a reporter). For each, write the method you think tests call.

---

## Answer Key

1. A contract of methods a class agrees to provide.
2. `implements`
3. Yes (they are public abstract unless you use later `default`/`static` forms).
4. Yes.
5. No.
6. So the rest of the code depends on the contract, not the JSON details.
7. Tests can run without files; they supply known users.
8. False.
9. When you have shared instance state and shared method bodies to inherit.
10. Same shape: program to the contract, plug in Chrome or Firefox.
