# Week 3 — OOP and Collections

**Coverage:** Parts 12–16 (OOP, packages, collections, wrapper classes, enums)

**Suggested timebox:** 80 minutes

| Activity | Time |
| --- | --- |
| Quiz (closed book) | 15 minutes |
| Coding assignment | 35 minutes |
| Debugging problem | 15 minutes |
| Explanation exercise | 10 minutes |
| Buffer | 5 minutes |

Score: Theory 20%, Coding 35%, Problem Solving 20%, Debugging 15%, Explanation 10%. Pass bar: 70% weighted.

**Background:** Part 11 (arrays) is assumed. Collections hold objects; you will use `List<User>` and `List<Integer>` (wrappers).

---

## Quiz (10 questions)

1. What is the difference between a class and an object? Give an SDET example.
2. Name the four common OOP pillars and composition. Give a one-line job for each.
3. Why are fields usually `private`? What are getters and setters for?
4. Inheritance vs composition: `is-a` vs `has-a`. When would you prefer composition?
5. What is polymorphism in one sentence, using `WebDriver` or a `Browser` type?
6. Interface vs abstract class: when would you choose an interface for a contract?
7. What is a package, and why do SDET projects split `pages`, `api`, `models`, and `tests`?
8. List vs Set vs Map vs Queue: which do you pick for ordered users, unique ids, HTTP headers, and jobs to process?
9. Why can't you write `List<int>`? What is autoboxing?
10. Why use an `enum` for browsers instead of `String browser = "Chorme"`?

---

## Coding assignment

**Title:** `User` roster with validation

This is practice for the OOP design exam.

### Requirements

1. Package `com.sdet.week03` (or your course package). Do not leave this in the default package.

2. Class `User`:
   - private fields: `username` (String), `email` (String)
   - constructor that requires both
   - **validation:** if `username` is `null` or blank, throw `IllegalArgumentException` with a clear message
   - getters for both fields
   - setter for `email` only (`username` stays fixed after construction — `final` is welcome)

3. Class `UserDirectory`:
   - a `List<User>` field
   - `void add(User user)`
   - `User findByUsername(String username)` — return the matching user, or `null` if missing (Optional is next week; `null` is acceptable here if you document it)
   - `int size()`

4. Enum `BrowserType` with `CHROME`, `FIREFOX`, `EDGE`. Print all values from `main` using `BrowserType.values()`.

5. `main` (in `WeekThreeDemo` or `UserDirectory`):
   - add two valid users
   - try to add a user with a blank username inside `try/catch` and print that validation worked
   - find one user by username and print the email

### Acceptance criteria

- [ ] Fields are private. No public `username` field.
- [ ] Blank username is rejected with `IllegalArgumentException`.
- [ ] `List<User>` stores the roster (not a raw `List`).
- [ ] Find-by-username works for an existing user.
- [ ] `BrowserType` cannot represent a typo the way a free `String` can.
- [ ] Files declare a `package`.

### Problem-solving stretch

Store unique usernames in a `Set<String>` as you add users. Reject a second user with the same username. State why a `Set` is the right tool.

---

## Debugging problem

```java
public class User {
    public String username;
    public String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}

public class Demo {
    public static void main(String[] args) {
        User u = new User("aisha", "aisha@example.com");
        u.username = "";
        System.out.println("user is " + u.username);
    }
}
```

Assume both classes were pasted into one file, or `Demo` compiles against a public-field `User`.

### Your job

1. This code may compile. That is not the same as being correct. What is the *design* bug?
2. What can a caller do that a well-encapsulated `User` should prevent?
3. Write the corrected `User` (private fields, constructor validation, getters, email setter).
4. Show the line in `Demo` that must change after your fix.

### Expected diagnosis

**Encapsulation failure, not a compiler error.** Public fields let `Demo` set `username` to blank after construction. Validation in a constructor is useless if fields are public. Two public classes in one file is also illegal if both are `public` — mention that if the snippet was one file.

**Fix:** `private` fields, validate in the constructor, no setter for `username` (or a setter that validates). `Demo` must use the constructor / getters. Empty username should throw, not print as a valid user.

---

## Explanation exercise

**Prompt (90 seconds):**

> Explain encapsulation to someone who has never written Java. Then say why page objects hide locators the same way.

**Strong answer hits:**

- A class is a blueprint; an object is one instance with its own data.
- Private fields + methods mean callers cannot put the object in an illegal state as easily.
- `LoginPage` hides `By.id("username")` and exposes `enterUsername`. Tests do not scatter locators. When the id changes, one class changes.

---

## Answer key

1. Class = blueprint. Object = one instance (`new LoginPage(driver)`). Two objects do not automatically share field values (unless `static`).
2. Encapsulation: hide data, expose safe operations. Inheritance: is-a. Polymorphism: one API, many types. Abstraction: say what, hide how. Composition: has-a (page has a driver / component).
3. So callers cannot break invariants. Getters read; setters change with rules. Not every field needs a setter.
4. Prefer composition when it is has-a (`CheckoutPage` has `PaymentComponent`). Inheritance towers (`BaseTest` of BaseTest) are easy to misuse.
5. `WebDriver driver = factory.create(type);` then `driver.get(...)` works for Chrome or Firefox.
6. Interface = contract (`WebDriver`, `TestDataProvider`). Abstract class = shared code plus identity; cannot `new` it. Prefer interfaces for pure contracts.
7. A package is a named folder for related types. Splitting pages/api/models/tests prevents name clashes and junk-drawer repos.
8. List: ordered users. Set: unique ids. Map: headers / JSON-like keys. Queue: jobs in processing order. Map is not a Collection; it is a sibling idea.
9. Collections hold objects. Use `List<Integer>`. Autoboxing converts `int` → `Integer` (and unboxing the other way).
10. Enums are a fixed set of named values. A typo like `"Chorme"` compiles as a String and fails later. `BrowserType.CHROME` cannot be misspelled that way.

**Debugging key:** public fields bypass validation; encapsulate and validate.
