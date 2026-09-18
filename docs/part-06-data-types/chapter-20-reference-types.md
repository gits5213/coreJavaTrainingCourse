# Chapter 20 — Reference Types

## 1. Today's Goal

By the end of this lesson, you will understand the difference between a **primitive type** and a **reference type**.

You will know that some variables hold a simple value themselves, while others hold an address that points to an object.

You will meet `String` as your first everyday reference type, and you will see a preview of classes such as `User` that you will build later.

## 2. Why It Matters

Beginners often think every variable works like `int age = 25;`. That mental model breaks as soon as text, lists, browsers, and page objects appear.

If you do not learn the primitive vs reference split now, later topics will feel like magic:

- why two `String` values can look the same but need `.equals()`
- why `null` exists
- why `new User()` is required
- why changing an object through one variable can be visible through another variable

You do not need the full memory model today. You need the picture:

```text
Primitive  → the box contains the value
Reference  → the box contains a pointer to an object
```

## 3. Real-Life Analogy

A primitive is a sticky note with the answer written on it.

```text
Sticky note labeled age
Written on the note: 25
```

A reference is a sticky note with an address, not the house itself.

```text
Sticky note labeled user
Written on the note: "House #42"

House #42 contains:
  username = john
  role     = admin
```

If you copy a primitive sticky note, you copy the number. Each note can change independently.

If you copy a reference sticky note, you copy the address. Both notes can still point at the same house.

Another analogy: a phone contact named "Bank" is not the bank. It is a way to reach the bank.

## 4. Illustrated Explanation

### Primitive

```java
int actualStatusCode = 200;
```

```text
VARIABLE
┌────────────────────┐
│ actualStatusCode   │
│ 200                │  ← value lives in the variable
└────────────────────┘
```

### Reference

```java
String name = "John";
```

A simplified picture:

```text
VARIABLE                         OBJECT
┌─────────────┐                  ┌─────────────┐
│ name        │   points to      │ "John"      │
│  ●──────────┼─────────────────►│             │
└─────────────┘                  └─────────────┘
```

The variable `name` does not contain the letters in the same way `int` contains `200`. It refers to a `String` object that contains the text.

Later, objects you design work the same way:

```java
User user = new User();
```

```text
VARIABLE                         OBJECT
┌─────────────┐                  ┌──────────────────┐
│ user        │   points to      │ User             │
│  ●──────────┼─────────────────►│ username = ?     │
└─────────────┘                  │ password = ?     │
                                 └──────────────────┘
```

`new` means:

> Create a new object in memory, then give me a reference to it.

### Side-by-side

```text
PRIMITIVE                         REFERENCE
int count = 3;                    String title = "Login";

┌────────────┐                    ┌────────────┐     ┌────────────┐
│ count      │                    │ title      │     │ "Login"    │
│ 3          │                    │  ●─────────┼────►│            │
└────────────┘                    └────────────┘     └────────────┘
```

### `null`

A reference can point to nothing:

```java
String message = null;
```

```text
┌────────────┐
│ message    │
│  null      │  → not pointing at any object
└────────────┘
```

Primitives such as `int` cannot be `null`. That difference will matter when tests fail with `NullPointerException` later.

## 5. Syntax / Concept

### Primitive types you already know

```java
int expected = 200;
double responseTime = 1.2;
boolean passed = true;
char grade = 'A';
```

No `new` is required.

### `String` is a reference type

```java
String name = "John";
```

Java lets you create many strings with quotes. That shortcut is special for `String`. Behind the scenes, you still have an object.

You can also write:

```java
String name = new String("John");
```

For now, prefer the quote form. The `new String(...)` form is shown so you see that `String` is an object type.

### Other reference types

Anything created from a class is a reference type:

```java
String message = "Login successful";
User user = new User();
```

You have not designed `User` yet. The idea is enough:

```text
Class  = blueprint
Object = the actual thing built from the blueprint
Variable = the name that refers to that object
```

### What the variable stores

```text
Primitive variable  → stores the actual simple value
Reference variable  → stores a reference (an address) to an object
```

We will compare two references with `==` versus `.equals()` in the operators part, especially for `String`. Do not use `==` to check whether two pieces of text have the same characters. That lesson is coming. The reason is this chapter: `==` on references is about the pointer, not always about the readable content.

## 6. Simple Example

```java
public class ReferenceIntro {

    public static void main(String[] args) {
        int age = 30;
        String name = "John";

        System.out.println("Primitive age: " + age);
        System.out.println("Reference name: " + name);

        String greeting = new String("Hello");
        System.out.println(greeting);
    }
}
```

Expected output:

```text
Primitive age: 30
Reference name: John
Hello
```

The printed results look similar. The storage model is not similar. `age` holds `30`. `name` refers to a `String` object whose content is `John`.

## 7. Real-World Example

An order in an online shop is not one number. It is an object with several pieces of data.

We will use `String` for names and statuses today. Later, `Order` will be its own class.

```java
public class OrderReferences {

    public static void main(String[] args) {
        int quantity = 2;
        double price = 49.99;
        String customerName = "Sam Rivera";
        String productName = "Wireless Mouse";
        String orderStatus = "Processing";

        System.out.println(customerName + " ordered " + productName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: " + price);
        System.out.println("Status: " + orderStatus);
    }
}
```

```text
quantity and price     → primitives
customerName, product  → String references
```

A banking customer is also a natural object:

```text
Customer object
  name        → String
  accountId   → long (primitive)
  balance     → double (primitive)
  active      → boolean (primitive)
```

The customer as a whole is a reference. The simple fields inside can be primitives.

## 8. SDET Example

Test automation is full of reference types: messages, usernames, browser names, page objects, API clients.

```java
public class SdetReferences {

    public static void main(String[] args) {
        int expectedStatusCode = 200;
        int actualStatusCode = 200;
        String actualMessage = "Login successful";
        String browserName = "Chrome";

        System.out.println("Status expected: " + expectedStatusCode);
        System.out.println("Status actual: " + actualStatusCode);
        System.out.println("Message: " + actualMessage);
        System.out.println("Browser: " + browserName);
    }
}
```

Status codes are primitives. Messages and browser names are `String` objects.

Later you will write things like:

```java
User user = new User();
LoginPage loginPage = new LoginPage();
```

Those will also be reference types. The skill is the same: the variable points to an object.

A null example that testers meet early:

```java
public class MissingMessage {

    public static void main(String[] args) {
        String errorMessage = null;
        System.out.println("Error message is: " + errorMessage);
    }
}
```

This prints `Error message is: null`. The variable exists. The object does not. Calling a method on `null` later will crash. Chapter 21 will call methods on real `String` objects, so keep your variables pointing at actual text unless you are practicing `null` on purpose.

## 9. Break the Code

This program tries to model a user, but it mixes up primitives and objects.

```java
public class BrokenReferenceThinking {

    public static void main(String[] args) {
        int user = "john";
        String age = 25;
        String status = null;
        System.out.println(status.length());
    }
}
```

There are type errors and a crash waiting to happen.

## 10. Debug

1. `int user = "john";`  
   `"john"` is text, so it needs `String`, not `int`.

2. `String age = 25;`  
   `25` is a whole number. Use `int age = 25;` unless you truly want the text `"25"`.

3. `status.length()`  
   `status` is `null`. You cannot ask a missing object for its length. That causes `NullPointerException`.

Fixed version:

```java
public class FixedReferenceThinking {

    public static void main(String[] args) {
        String username = "john";
        int age = 25;
        String status = "ACTIVE";

        System.out.println("Username: " + username);
        System.out.println("Age: " + age);
        System.out.println("Status length: " + status.length());
    }
}
```

Debug habit:

```text
Red underline at assignment  → type mismatch
Program compiles but crashes → maybe the reference is null
```

## 11. Student Exercise

Create `PrimitiveVsReference`.

Declare:

- one `int`
- one `boolean`
- two `String` values
- one `String` that is `null`

Print all of them. Then, in comments, label each variable as primitive or reference.

Do not call a method on the `null` `String`.

## 12. Challenge

Write a short "test data object on paper" in comments, then code the fields you can code today.

Comment picture:

```text
TestCase
  name              String
  expectedStatus    int
  actualStatus      int
  passed            boolean
  owner             String
```

Create those five variables and print a report. Then write two sentences in comments:

- which variables are primitives
- which variables are references

## 13. Knowledge Check

1. What does a primitive variable store?
2. What does a reference variable store?
3. Is `String` a primitive type?
4. Name three primitive types and one reference type.
5. What keyword creates a new object from a class?
6. What does `null` mean for a reference variable?
7. Can an `int` variable be `null`?
8. Why might `User user = new User();` be described as a reference?
9. True or false: if two variables print the same text, they must be primitives.
10. Why will reference vs value comparison for `String` be taught carefully later?

## 14. Interview Question

**Question:** What is the difference between primitive types and reference types in Java?

A strong answer:

> Primitive types store simple values directly, such as int, boolean, and char. Reference types store a reference to an object, such as String or a User object created with new. A reference can be null, meaning it points to no object. That is why two pieces of text that look the same are not compared with == in ordinary testing code. We will use equals for text content.

Keep the last sentence honest if you have not practiced `.equals()` yet: say you know `String` is a reference and content comparison is a later/operators lesson.

## 15. Homework

Draw the memory picture for this code in a notebook or a comment block, then implement the code:

```java
int expectedStatusCode = 200;
String actualMessage = "Created";
```

Your drawing should show:

- `expectedStatusCode` containing `200`
- `actualMessage` pointing at an object containing `Created`

Then add a `String failedMessage = null;` and draw that too. Bring the drawing to class.

---

## Answer Key

1. The simple value itself.
2. A reference (address) to an object.
3. No. It is a reference type.
4. Examples: `int`, `boolean`, `double`; reference: `String`.
5. `new`.
6. The variable currently points to no object.
7. No.
8. Because `user` holds a reference to a `User` object, not a single primitive value.
9. False. `String` values are references and can still print text.
10. Because `==` on references can compare pointers, while testers usually care about text content.
