# Part 12 — Object-Oriented Programming

Until now, your programs have been lists of steps: variables, `if`, loops, methods, arrays. That is useful. Real software is not only a list of steps. Real software is about **things** that have **data** and **behavior**.

A user has a name and a password, and can log in.  
A bank account has a balance, and can deposit.  
A login page has fields, and can type into them.

**Object-oriented programming (OOP)** is how Java lets you model those things.

```text
Class  = the blueprint
Object = one real thing built from that blueprint

User (class)                user1, user2 (objects)
  username                    john / admin
  password                    **hidden**
  login()                     each user can try to log in
```

## The Four Working Ideas

Do not treat these as vocabulary flash cards. Treat them as jobs.

| Idea | Job in plain English |
| --- | --- |
| **Encapsulation** | Keep data private. Talk to the object through methods. |
| **Inheritance** | A more specific thing *is a* more general thing (`LoginPage` is a `BasePage`). |
| **Polymorphism** | Talk to the general type. Plug in the specific type (`Browser browser = new ChromeBrowser()`). |
| **Abstraction** | Say *what* must happen (`pay()`). Let each type decide *how*. |

A fifth idea you will use constantly in SDET work:

| Idea | Job in plain English |
| --- | --- |
| **Composition** | A thing *has a* helper (`CheckoutPage` has a `PaymentComponent`). Prefer this when it is simpler than inheritance. |

## Chapters in This Part

| Chapter | Topic | You will be able to... |
| --- | --- | --- |
| [Chapter 39](chapter-39-what-is-oop.md) | What is OOP? | Explain class, object, and the four ideas as *work*, not slogans |
| [Chapter 40](chapter-40-class.md) | Class | Write a `User` class as a blueprint |
| [Chapter 41](chapter-41-object.md) | Object | Create two different `User` objects from one class |
| [Chapter 42](chapter-42-constructor.md) | Constructor | Give an object its starting values |
| [Chapter 43](chapter-43-this.md) | `this` | Tell field and parameter apart |
| [Chapter 44](chapter-44-encapsulation.md) | Encapsulation | Hide fields; use getters and setters |
| [Chapter 45](chapter-45-access-modifiers.md) | Access modifiers | Choose `public`, `private`, `protected`, or package-private |
| [Chapter 46](chapter-46-static.md) | `static` | Know what belongs to the class, not the object — without making everything static |
| [Chapter 47](chapter-47-final.md) | `final` | Lock a variable, a method, or a whole class — three different jobs |
| [Chapter 48](chapter-48-inheritance.md) | Inheritance | Write `LoginPage extends BasePage` (IS-A) |
| [Chapter 49](chapter-49-method-overriding.md) | Overriding | Replace a parent method with `@Override` |
| [Chapter 50](chapter-50-polymorphism.md) | Polymorphism | `Browser browser = new ChromeBrowser()` then `FirefoxBrowser` |
| [Chapter 51](chapter-51-abstract-class.md) | Abstract class | Force subclasses to implement `PaymentService.pay()` |
| [Chapter 52](chapter-52-interface.md) | Interface | Write `TestDataProvider` and `JsonDataProvider` |
| [Chapter 53](chapter-53-composition.md) | Composition | `CheckoutPage` HAS-A `PaymentComponent` |

## Prerequisite

Finish Parts 5–11. You should be comfortable with variables, methods, and arrays. OOP *uses* those tools. A class is a box that holds fields (variables) and methods (recipes).

Part 10 used `public static` methods so you could call them from `main` without objects. That was a teaching ramp. Starting here, most methods belong to objects. `static` still exists, and Chapter 46 will teach when it is honest — and when it is a trap.

## How This Connects to SDET Work

Page Object Model, API clients, test data objects, drivers, and reports are all classes:

```text
LoginPage          → a page
User               → a person in the system
ApiClient          → a helper that talks to HTTP
ChromeBrowser      → one kind of Browser
JsonDataProvider   → one way to load test data
```

If you skip OOP, later frameworks will look like magic words. If you learn OOP as working ideas, those frameworks will look like the same `User` class, grown up.

## Study Tip

Draw every example as:

```text
blueprint (class)
    └── object A
    └── object B
```

Two objects from one class can hold different data. That one sentence is half of OOP.

Do not rush to inheritance. A well-built `User` with private fields is more valuable than a tall family tree you cannot explain.
