# Chapter 39 — What Is OOP?

## 1. Today's Goal

By the end of this lesson, you will explain object-oriented programming as a way to model **things** that have **data** and **behavior** — not as a pile of fancy words.

You will be able to say, in your own sentences:

- a **class** is a blueprint
- an **object** is one real thing built from that blueprint
- **encapsulation**, **inheritance**, **polymorphism**, and **abstraction** are jobs those things do

You will not write a large program today. You will build the map the next fourteen chapters fill in.

## 2. Why It Matters

A login test is not only a sequence of `println` calls. It is about a **user**, a **page**, a **browser**, and a **result**.

Without OOP, beginners keep adding variables and methods to `main` until the file is a kitchen drawer. With OOP, you group related data and related actions:

```text
NOT: username, password, loginSteps, clickLogin, checkUrl
     all floating in main

YES: a User who has a username and can log in
     a LoginPage that can type and click
     a Browser that can open and close
```

SDET frameworks are full of classes because tests are full of *things*. If you understand the ideas as work, `LoginPage` will feel obvious later. If you only memorize definitions, every new class name will feel like a new language.

## 3. Real-Life Analogy

A house blueprint vs a real house.

```text
Blueprint (class)              Real houses (objects)
  3 bedrooms                     12 Oak Street  — painted blue
  2 bathrooms                    14 Oak Street  — painted yellow
  a kitchen                      16 Oak Street  — painted green
```

The blueprint is not a house. You cannot sleep in a PDF. You can build many houses from one blueprint. Each house has its own paint, its own furniture, its own mess.

A cookie cutter is a class. Each cookie is an object.

A job description for "Bank Teller" is a class. Each hired teller is an object: same role, different name, different cash drawer.

## 4. Illustrated Explanation

Think of software as a neighborhood of cooperating objects.

```text
                    ┌──────────────┐
                    │   User       │  ← one object
                    │  username    │
                    │  login()     │
                    └──────┬───────┘
                           │ uses
                           v
                    ┌──────────────┐
                    │  LoginPage   │  ← another object
                    │  type()      │
                    │  click()     │
                    └──────┬───────┘
                           │ uses
                           v
                    ┌──────────────┐
                    │   Browser    │  ← another object
                    │  open()      │
                    │  close()     │
                    └──────────────┘
```

The four classic ideas, as jobs:

```text
ENCAPSULATION
  The User keeps password private.
  Outsiders ask through methods, they do not reach into pockets.

INHERITANCE  (IS-A)
  LoginPage IS-A BasePage.
  The child gets the parent's tools, then adds its own.

POLYMORPHISM
  Code says: Browser browser = ...
  Today ChromeBrowser, tomorrow FirefoxBrowser.
  The rest of the test still says browser.open().

ABSTRACTION
  PaymentService says: you must be able to pay().
  CardPayment and WalletPayment each decide how.
```

Composition (HAS-A), which you will prefer when it is simpler:

```text
CheckoutPage HAS-A PaymentComponent
  The page does not "become" a payment form.
  It owns a helper that knows payment fields.
```

## 5. Syntax / Concept

OOP in Java is still Java. You already know pieces of it:

| You already know | OOP name |
| --- | --- |
| A `.java` file with `public class ...` | a **class** |
| Variables inside that class (coming next) | **fields** |
| Methods inside that class | **behavior** |
| `new` (you have seen it with arrays and `String`) | creating an **object** |

A class groups:

```text
class Name {
    fields     →  data the thing remembers
    methods    →  what the thing can do
    constructors → how to start a new object  (Chapter 42)
}
```

The four pillars, in working language (not interview slogans yet):

1. **Encapsulation** — data is protected; methods are the door.
2. **Inheritance** — a specialized type reuses a general type (`extends`).
3. **Polymorphism** — one type name, many actual objects.
4. **Abstraction** — hide the messy how; show the clear what.

Java also has **interfaces** (Chapter 52) and **abstract classes** (Chapter 51). Those are tools for abstraction. They are not extra pillars you must stack on every program.

**IS-A vs HAS-A:**

```text
LoginPage extends BasePage     → LoginPage IS-A BasePage
CheckoutPage has a PaymentComponent → CheckoutPage HAS-A payment helper
```

If you cannot finish the sentence "this IS a that," do not force inheritance. Use composition.

## 6. Simple Example

Here is the smallest picture: one class, two objects, different data.

```java
class Dog {
    String name;

    void bark() {
        System.out.println(name + " says woof");
    }
}

public class OopPicture {

    public static void main(String[] args) {
        Dog first = new Dog();
        first.name = "Rex";

        Dog second = new Dog();
        second.name = "Bella";

        first.bark();
        second.bark();
    }
}
```

Expected output:

```text
Rex says woof
Bella says woof
```

Same blueprint (`Dog`). Two objects. Two names. Same method, different data. That is the heart of OOP.

We will make fields private soon. Today, notice the shape.

## 7. Real-World Example

A bank does not store "balance" as a lonely variable in `main`. It stores accounts.

```text
BankAccount (class)
  owner
  balance
  deposit()
  withdraw()
```

Two customers, two objects:

```text
aliceAccount.balance = 100.00
bobAccount.balance   = 5.00
```

If Alice deposits, Bob's money must not change. That only works if each object has its **own** fields.

An online store has `User`, `Product`, `Cart`, `Order`. Those are classes because they are things the business talks about.

## 8. SDET Example

A test is easier to read when the things have names:

```text
User standardUser
LoginPage loginPage
Browser browser
```

You will later write (idea, not full Selenium yet):

```text
browser.open()
loginPage.typeUsername(standardUser.username)
loginPage.typePassword(standardUser.password)
loginPage.clickLogin()
```

Frameworks did not invent this because they like long words. They invented it because **login is a thing**, **the page is a thing**, and **the user is a thing**.

Compare:

```text
WITHOUT OOP (steps only)
  find box
  type john
  find other box
  type secret
  click button

WITH OOP (things cooperating)
  a User knows who they are
  a LoginPage knows how to fill itself
  a test asks them to work together
```

## 9. Break the Code

A beginner hears "OOP" and dumps everything into `main` anyway:

```java
public class BrokenOopThinking {

    public static void main(String[] args) {
        String user1Name = "john";
        String user1Password = "Test123";
        String user2Name = "admin";
        String user2Password = "Admin!23";

        System.out.println("Login as " + user1Name + " with " + user1Password);
        System.out.println("Login as " + user2Name + " with " + user2Password);
    }
}
```

This runs. Running is not the same as modeling. The moment you add email, role, last login time, and "locked account," you will drown in `user3Email`.

Another broken idea: treating the four pillars as a checklist you must use in every file.

```text
"I used inheritance, so this is good OOP."
```

A `LoginPage extends Animal` would compile if you forced the types to match. It would still be nonsense. Inheritance is for IS-A, not for showing off.

## 10. Debug

When OOP feels foggy, ask four questions on paper:

1. **What is the thing?** (User, Page, Account)
2. **What data does it remember?** (username, balance)
3. **What can it do?** (login, deposit)
4. **Is this IS-A or HAS-A?** (page is a page; page has a header)

If you cannot name the thing, you are not ready to invent a class. Stay with methods a little longer, then come back.

If two piles of variables keep traveling together (`username` and `password` always as a pair), that is a clue: they want to be one object.

If you are copying the same five fields for `user1`, `user2`, `user3`, you already needed a class yesterday.

## 11. Student Exercise

On paper (or in a comment in a tiny Java file), design three things from a banking app:

1. `User`
2. `BankAccount`
3. `Transaction`

For each, write:

- two pieces of data it should remember
- one action it should be able to do
- whether it IS-A something else, HAS-A something else, or stands alone for now

Do **not** write a full program yet unless you want extra practice. Naming the model is the skill today.

## 12. Challenge

Pick a login test you can describe in English.

List the objects you would want:

- at least one `User`
- at least one page
- at least one result (`LoginResult` with `success` and `message`)

Write a short paragraph:

> I would not put all of this in `main` because...

Then write a second paragraph:

> I would **not** make `LoginPage extend User` because...

That second paragraph is you protecting yourself from fake inheritance.

## 13. Knowledge Check

1. What is a class, in one sentence?
2. What is an object, in one sentence?
3. Can one class produce many objects?
4. What job does encapsulation do?
5. What does IS-A mean? Give the `LoginPage` example.
6. What does HAS-A mean? Give the checkout example.
7. What job does polymorphism do for browsers?
8. What job does abstraction do for `pay()`?
9. True or false: every good program must use inheritance.
10. Why do SDET frameworks use classes like `LoginPage`?

## 14. Interview Question

**Question:** What is OOP, and what are the main ideas?

A strong answer:

> Object-oriented programming models software as objects created from class blueprints. Each object has its own data and its own behavior. Encapsulation keeps data private and exposes methods. Inheritance models IS-A relationships, like LoginPage extending BasePage. Polymorphism lets me talk to a general type, such as Browser, while the actual object is ChromeBrowser or FirefoxBrowser. Abstraction focuses on what something must do, like pay(), without hard-coding every how. I also use composition — HAS-A — when a page should own a helper instead of extending it. I do not force all four ideas into every class.

Notice: the answer uses ideas as jobs. It does not recite a textbook list and stop.

## 15. Homework

Create a notes file `notes/oop-map.md` (or paper). Draw:

```text
Class → Object
Encapsulation
Inheritance (IS-A)
Polymorphism
Abstraction
Composition (HAS-A)
```

Under each heading, write one SDET sentence. Example under polymorphism: "My test holds a Browser. Today it is Chrome. Tomorrow it can be Firefox without rewriting the test steps."

Read Chapter 40 next. You will write a real `User` class.

---

## Answer Key

1. A class is a blueprint that describes data and behavior for a kind of thing.
2. An object is one real instance built from that class.
3. Yes.
4. It protects data and lets outsiders work through methods.
5. A more specific type is a kind of a more general type. `LoginPage` IS-A `BasePage`.
6. A thing owns or uses another thing. `CheckoutPage` HAS-A `PaymentComponent`.
7. You write code against `Browser`, then plug in `ChromeBrowser` or `FirefoxBrowser`.
8. You declare that payment must happen (`pay()`). Each payment type implements the details.
9. False. Inheritance is for real IS-A relationships. Many good designs use composition instead.
10. Because a page is a thing with locators and actions, not a pile of loose steps in `main`.
