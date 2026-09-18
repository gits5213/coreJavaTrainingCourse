# OOP Design Exam

**Level:** intermediate  
**Format:** IDE, examiner may ask "why private?" while you type  
**Timebox:** 30–40 minutes

Suggested weight in the [final exam](final-exam-guide.md): **10%**.

---

## Prompt

Create:

```text
User class
Constructor
private fields
getters/setters
validation
List<User>
```

You are modeling users for a test roster (the same idea as a future `List<User>` of accounts).

### Required type

- Class name: `User`
- Fields: `username`, `email` (both `String`). `username` must not change after construction (`final` is expected or justified).
- Constructor: `User(String username, String email)`
- Validation: `username` must not be `null` or blank. Throw `IllegalArgumentException` with a message a teammate can read. Email: reject `null` or require a very small rule (contains `@`) — pick one and apply it in the constructor and in `setEmail`.
- Getters for both fields.
- Setter for `email` only (re-validate).
- A small demo or test that:
  - builds a `List<User>`
  - `add`s at least two users
  - finds a user by username (loop is fine; streams are a bonus)
  - shows that blank username is rejected

Package name required (`com.sdet.exam` or your project package).

### Bar (shape, not a script to copy blindly)

```java
public class User {
    private final String username;
    private String email;

    public User(String username, String email) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username required");
        }
        this.email = requireEmail(email);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = requireEmail(email);
    }

    private static String requireEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("email required");
        }
        return email;
    }
}
```

```java
List<User> users = new ArrayList<>();
users.add(new User("aisha", "aisha@example.com"));
users.add(new User("ben", "ben@example.com"));
```

### Optional exceed

A `record User(String username, String email)` with a compact constructor for validation is acceptable **if** the student defends immutability (no `setEmail`, or a `withEmail` that returns a new record). Do not fail a correct mutable `User` that met the brief.

---

## Examiner questions (ask at least two)

- Why private fields? Why not `public String username`?
- Why is validation in the constructor not enough if fields are public?
- Why `List<User>` and not a raw `List`?
- Find-by-username: what do you return when missing? (`null` vs later `Optional`)
- Would you put JDBC inside `User`? Why not?

---

## Acceptance criteria

- [ ] Private fields; username stable after construction
- [ ] Constructor validates username
- [ ] Getters; email setter validates
- [ ] `List<User>` used in demo/test
- [ ] Blank username throws; program still demonstrates the happy path
- [ ] Student can explain encapsulation in one minute

---

## Rubric (10 points)

| Look for | Points |
| --- | --- |
| Private fields + constructor | 3 |
| Validation actually throws | 2 |
| Getters/setters with rules | 2 |
| `List<User>` find/add | 2 |
| Oral defense | 1 |

Fail the station if fields are public "to save time" or validation is only a `println`.

---

## What this is not

This is not a Spring Boot user service. No database. No passwords in `toString`. If you store a password for a login model, mask it — but this exam's `User` only needs username and email.
