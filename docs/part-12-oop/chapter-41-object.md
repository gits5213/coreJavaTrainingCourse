# Chapter 41 — Object

## 1. Today's Goal

By the end of this lesson, you will create **objects** from a class with `new`, store them in variables, and see that two objects from the same class can hold different data.

You will treat this line as a complete thought:

```java
User john = new User();
```

## 2. Why It Matters

A class on disk does nothing by itself. The JVM needs objects in memory.

Your test suite does not log in as "the idea of User." It logs in as **john** in test 1 and **admin** in test 2. Those are two objects.

Bugs happen when beginners think there is only one User in the program because there is only one `User` class. The class is shared. The objects are not.

## 3. Real-Life Analogy

A cookie cutter and cookies.

```text
Cutter  = class User
Cookies = objects   (john, admin, guest)
```

Stamping twice does not make one cookie with two names. It makes two cookies.

A house blueprint vs two houses on the same street: same plan, different mailboxes, different furniture.

A form vs filled forms: one blank template, many completed applications in a filing cabinet.

## 4. Illustrated Explanation

```text
class User { String username; }

In memory after:

User john  = new User();
john.username = "john";

User admin = new User();
admin.username = "admin";
```

```text
Stack (variable names)          Heap (objects)

john  ──────────────────────►  ┌──────────────┐
                               │ username=john│
                               └──────────────┘

admin ──────────────────────►  ┌──────────────┐
                               │ username=admin│
                               └──────────────┘
```

`john` is not the object. `john` is a **reference** (an arrow) pointing at the object.

```text
User john = new User();
 │     │         │
 │     │         └── create a new User object in memory
 │     └── variable that will point at it
 └── type of that variable
```

Two references can point at the **same** object (two arrows, one house). Changing through one name changes the one house. That surprises people. We will show it in Break the Code.

`null` means the variable currently points at nothing:

```text
User missing = null;

missing  ──►  (nowhere)
```

Calling a method on `null` crashes with `NullPointerException`.

## 5. Syntax / Concept

Create:

```java
User john = new User();
```

The no-argument constructor `User()` is provided by Java if you have not written one yet. Chapter 42 takes over constructors.

Fill fields (still public-style for learning; Chapter 44 will hide them):

```java
john.username = "john";
john.role = "standard";
```

Use:

```java
john.introduce();
```

The dot `.` means "go through this reference into the object."

Many objects:

```java
User john = new User();
User admin = new User();
User guest = new User();
```

Each `new` is a separate object.

You can also declare, then assign:

```java
User john;
john = new User();
```

Until you assign, `john` is not pointing at a usable object (for a local variable, Java will not even let you use it).

Objects are reference types. The variable holds the arrow, not a copy of all fields. That is the same family as `String` and arrays from Part 6 and Part 11.

## 6. Simple Example

```java
class User {
    String username;
    String role;

    void introduce() {
        System.out.println("I am " + username + " (" + role + ")");
    }
}

public class TwoUsersDemo {

    public static void main(String[] args) {
        User john = new User();
        john.username = "john";
        john.role = "standard";

        User admin = new User();
        admin.username = "admin";
        admin.role = "admin";

        john.introduce();
        admin.introduce();
    }
}
```

Expected output:

```text
I am john (standard)
I am admin (admin)
```

Same class. Two objects. Two introductions. Changing `admin.username` would not change `john`.

## 7. Real-World Example

Two bank accounts:

```java
class BankAccount {
    String owner;
    double balance;

    void print() {
        System.out.println(owner + ": " + balance);
    }
}

public class TwoAccountsDemo {

    public static void main(String[] args) {
        BankAccount alice = new BankAccount();
        alice.owner = "Alice";
        alice.balance = 100.00;

        BankAccount bob = new BankAccount();
        bob.owner = "Bob";
        bob.balance = 5.00;

        alice.balance = alice.balance + 20.00;

        alice.print();
        bob.print();
    }
}
```

Expected output:

```text
Alice: 120.0
Bob: 5.0
```

Alice's deposit did not enrich Bob. That is the whole point of separate objects.

## 8. SDET Example

Two test users, one login helper idea:

```java
class User {
    String username;
    String password;

    void printLoginAttempt() {
        System.out.println("Would log in as " + username);
    }
}

public class SdetObjects {

    public static void main(String[] args) {
        User standard = new User();
        standard.username = "standard_user";
        standard.password = "secret_sauce";

        User locked = new User();
        locked.username = "locked_out_user";
        locked.password = "secret_sauce";

        standard.printLoginAttempt();
        locked.printLoginAttempt();
    }
}
```

You can put objects in an array (you already know arrays):

```java
User[] users = { standard, locked };
for (User user : users) {
    user.printLoginAttempt();
}
```

That loop is the seed of data-driven tests: one action, many user objects.

## 9. Break the Code

Using the class as if it were the object:

```java
User.introduce(); // does not compile for an instance method
```

Forgetting `new`:

```java
User john;
john.username = "john"; // local variable might not be initialized
```

The two-arrows-one-house surprise:

```java
public class AliasBug {

    public static void main(String[] args) {
        User a = new User();
        a.username = "john";

        User b = a; // NOT a second object — second arrow
        b.username = "admin";

        System.out.println(a.username); // prints admin — same object
    }
}
```

```text
a ──┐
    ├──► [username=admin]
b ──┘
```

`NullPointerException`:

```java
User john = null;
john.introduce();
```

## 10. Debug

If you see `NullPointerException`, a reference is `null`. Print the variable before the call:

```java
System.out.println(john);
```

If it prints `null`, you never assigned `new User()`, or you assigned `null` on purpose.

If two variables seem to "telepathically" change together, you aliased them (`User b = a`) instead of `User b = new User()`.

If IntelliJ says `variable john might not have been initialized`, you declared the reference and used it before `new`.

Debugger habit:

1. Breakpoint on `new User()`.
2. Step over.
3. Inspect `john` in Variables. Expand it. You should see fields.
4. Create `admin`. Confirm it is a different object id in the debugger.

## 11. Student Exercise

Using your `User` class (username + role + `introduce()`):

1. Create three objects: `john`, `admin`, `guest`.
2. Give them different data.
3. Call `introduce()` on each.
4. Then write a method in the demo class:

```java
static void introduceAll(User first, User second) {
    first.introduce();
    second.introduce();
}
```

Call it with `john` and `admin`. This proves objects can be passed as parameters — the parameter is another arrow, not a photocopy of the house unless you build a new house.

## 12. Challenge

Write `CartItem` with `String name` and `int quantity`.

Create `itemA`. Set name `"Mug"`, quantity `2`.

Create `itemB = itemA`. Change `itemB.quantity` to `99`.

Print both quantities. Write a comment explaining why they match.

Then fix the design of the experiment: create a **true** second object `itemC = new CartItem()` with name `"Mug"` and quantity `2`. Change `itemC.quantity` to `1`. Print all three. Explain the difference in comments.

## 13. Knowledge Check

1. What keyword creates a new object?
2. What does the variable `john` hold — the whole object, or a reference?
3. Can two objects of class `User` have different usernames?
4. What does `User b = a` do if `a` already points at an object?
5. What is `null`?
6. What exception do you get if you call a method on `null`?
7. Write one line that creates a `User` object named `admin`.
8. True or false: the class is copied into memory once per object as a separate class definition.
9. Why did Alice's deposit not change Bob's balance?
10. How do objects help data-driven tests?

## 14. Interview Question

**Question:** What is the difference between a class and an object?

A strong answer:

> A class is the blueprint. An object is a concrete instance created with new. User is the class; john and admin are objects. Each object has its own field values, but they share the method code from the class. The variable holds a reference, not a copy of the object. If I write User b = a, I have two names for one object. In tests, I create many User objects from one User class so each test can log in as a different person.

## 15. Homework

Write `HomeworkObjects` with `User` (username, password) and create:

- `standard`
- `locked`
- `problem` (a "problem_user")

Print a line per user: `Attempt N: username`.

Add a fourth variable `alsoStandard = standard` (alias). Change `alsoStandard.username` and print `standard.username` again. In a comment, explain the arrow picture.

Optional: put the three real users in a `User[]` and loop.

---

## Answer Key

1. `new`
2. A reference (an arrow) to the object.
3. Yes. That is the usual case.
4. It copies the reference. Both names point at the same object.
5. "This reference points at no object."
6. `NullPointerException`
7. `User admin = new User();`
8. False. There is one class blueprint; each `new` makes a new object.
9. They were two different objects, each with its own `balance` field.
10. One class, many user (or data) objects, one loop of test actions.
