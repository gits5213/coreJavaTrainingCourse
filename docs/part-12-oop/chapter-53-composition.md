# Chapter 53 — Composition

## 1. Today's Goal

By the end of this lesson, you will model **HAS-A** relationships: an object **owns or uses** another object.

You will write `CheckoutPage` that **HAS-A** `PaymentComponent`, and you will prefer composition when it is simpler than inheritance.

## 2. Why It Matters

Inheritance is a strong claim: the child **is** the parent. Teams overuse it because `extends` looks like reuse.

Composition is quieter and usually healthier:

```text
CheckoutPage HAS-A PaymentComponent
CheckoutPage HAS-A Header
User HAS-A Address
Test HAS-A TestDataProvider
```

If the payment UI is reused on checkout *and* on a "saved cards" page, it should be a component those pages **have**, not a parent they **are**.

SDET page objects rot when everything extends a 2,000-line `BasePage`. Small helpers held as fields stay testable and swappable.

## 3. Real-Life Analogy

A car HAS-A engine. A car is not an engine.

```text
Car
  engine.start()
  wheels.roll()
```

You do not write `class Car extends Engine`. When the engine is replaced, the car is still a car.

A checkout counter HAS-A card reader. The counter is not a card reader.

A backpack HAS-A water bottle. The backpack is not a bottle. If you `extend Bottle`, you will have a very strange backpack.

## 4. Illustrated Explanation

```text
INHERITANCE (IS-A)              COMPOSITION (HAS-A)

LoginPage is a BasePage         CheckoutPage has a PaymentComponent

     BasePage                        CheckoutPage
         ▲                            /         \
         │                           /           \
    LoginPage                   Header        PaymentComponent
```

```text
CheckoutPage object
  ┌──────────────────────────────────┐
  │  url                             │
  │  payment  ─────► PaymentComponent│
  │  header   ─────► Header          │
  └──────────────────────────────────┘
```

Delegation: the page forwards work to the part that knows it.

```text
checkoutPage.payWithCard(...)
        │
        └── payment.typeCard(...)
            payment.clickPay()
```

The test can still call one method on the page. Internals stay composed.

When to prefer composition:

```text
- the IS-A sentence feels fake
- you only wanted to reuse a chunk of UI or a helper
- the extra type might be used in more than one parent
- the hierarchy would go three levels deep "just in case"
```

When inheritance still wins:

```text
- LoginPage really is a BasePage
- ChromeBrowser really is a Browser
- you need polymorphism on that IS-A type
```

You will use **both**. This chapter is not "inheritance is evil." It is "do not extend your way out of every duplication."

## 5. Syntax / Concept

A field holds the helper:

```java
class CheckoutPage {
    private final PaymentComponent payment;

    public CheckoutPage() {
        this.payment = new PaymentComponent();
    }
}
```

Or accept it in the constructor (easier to test — you can pass a fake):

```java
public CheckoutPage(PaymentComponent payment) {
    this.payment = payment;
}
```

Delegate:

```java
public void payWithCard(String cardNumber) {
    payment.typeCardNumber(cardNumber);
    payment.clickPay();
}
```

The page does not `extend PaymentComponent`.

You can combine: `CheckoutPage extends BasePage` **and** HAS-A `PaymentComponent`. That is normal:

```text
CheckoutPage IS-A BasePage          (clicks, open)
CheckoutPage HAS-A PaymentComponent (card fields)
```

Do not force `PaymentComponent extends BasePage` unless it truly is a page. A component is a piece.

## 6. Simple Example

```java
class PaymentComponent {
    public void typeCardNumber(String cardNumber) {
        System.out.println("Type card " + cardNumber);
    }

    public void clickPay() {
        System.out.println("Click pay");
    }
}

class CheckoutPage {
    private final PaymentComponent payment;

    public CheckoutPage(PaymentComponent payment) {
        this.payment = payment;
    }

    public void payWithCard(String cardNumber) {
        System.out.println("On checkout page");
        payment.typeCardNumber(cardNumber);
        payment.clickPay();
    }
}

public class CompositionDemo {

    public static void main(String[] args) {
        CheckoutPage checkout = new CheckoutPage(new PaymentComponent());
        checkout.payWithCard("4111111111111111");
    }
}
```

Expected output:

```text
On checkout page
Type card 4111111111111111
Click pay
```

## 7. Real-World Example

A user HAS-A address (they are not an address):

```java
class Address {
    private final String city;
    private final String zip;

    public Address(String city, String zip) {
        this.city = city;
        this.zip = zip;
    }

    public String line() {
        return city + " " + zip;
    }
}

class User {
    private final String username;
    private final Address address;

    public User(String username, Address address) {
        this.username = username;
        this.address = address;
    }

    public void printShipping() {
        System.out.println(username + " ships to " + address.line());
    }
}

public class UserHasAddress {

    public static void main(String[] args) {
        User user = new User("john", new Address("Boston", "02101"));
        user.printShipping();
    }
}
```

A bank account HAS-A owner `User`. An account is not a user.

## 8. SDET Example

Page plus component, plus a page that IS-A `BasePage`:

```java
class BasePage {
    public void open(String url) {
        System.out.println("Open " + url);
    }
}

class PaymentComponent {
    public void pay(String lastFour) {
        System.out.println("Pay with card ending " + lastFour);
    }
}

class HeaderComponent {
    public void openCart() {
        System.out.println("Click cart icon");
    }
}

class CheckoutPage extends BasePage {
    private final PaymentComponent payment;
    private final HeaderComponent header;

    public CheckoutPage(PaymentComponent payment, HeaderComponent header) {
        this.payment = payment;
        this.header = header;
    }

    public void completePurchase(String lastFour) {
        open("https://qa.shop.example/checkout");
        header.openCart();
        payment.pay(lastFour);
        System.out.println("Expect order confirmation");
    }
}

public class SdetComposition {

    public static void main(String[] args) {
        CheckoutPage checkout = new CheckoutPage(
                new PaymentComponent(),
                new HeaderComponent()
        );
        checkout.completePurchase("1111");
    }
}
```

`SavedCardsPage` can HAS-A the same `PaymentComponent` without being a checkout page.

Tests construct the page with real components. Later, fakes can stand in.

## 9. Break the Code

Inheritance for HAS-A:

```java
class CheckoutPage extends PaymentComponent { // checkout is not a payment form
}
```

Now checkout *is* a payment component. `HomePage` cannot reuse the component without also becoming a payment form. You painted yourself into a type corner.

God BasePage:

```java
class BasePage {
    void pay() { }
    void search() { }
    void openAdmin() { }
}
```

Every page inherits payment, search, and admin. Composition would give those only to pages that have them.

Creating the helper inside twenty methods instead of storing a field — you lose a single place to swap fakes.

Circular composition (`Page` has `Component` has `Page` has `Component`) until you cannot construct anything. Keep ownership one-way: page owns component.

## 10. Debug

If you are about to extend, write the IS-A sentence. If you wrote HAS-A in English, use a field.

If two unrelated pages need the same clicks, extract a component class. Do not insert a fake parent both must extend.

If tests cannot replace JSON with a fake, you `new JsonDataProvider()` inside the page instead of receiving a `TestDataProvider` (interface + composition). Pass it in.

Debugger: expand `checkout`. You should see `payment` and `header` as nested objects, not as "CheckoutPage is a PaymentComponent."

## 11. Student Exercise

Write `PaymentComponent` with `typeCard` and `clickPay`.

Write `CheckoutPage` that HAS-A `PaymentComponent` (constructor injection).

Write `payWithCard` that delegates.

In `main`, run a checkout.

Then write one comment line: `CheckoutPage is not a PaymentComponent.`

## 12. Challenge

Design a `LoginPage` that:

- IS-A `BasePage` (open, click, type)
- HAS-A `FlashMessage` component (`String read()`)

`login` uses BasePage tools. After login, `readError()` delegates to `FlashMessage`.

Write `main` that demonstrates both relationships.

Write a short paragraph: when you would *not* make `FlashMessage extend BasePage`.

## 13. Knowledge Check

1. What is composition in working language?
2. What English sentence should be true for composition?
3. Write a field that shows `CheckoutPage` HAS-A `PaymentComponent`.
4. What is delegation here?
5. Why not `class CheckoutPage extends PaymentComponent`?
6. Can a class use inheritance and composition together?
7. True or false: composition means you must never use `extends`.
8. How does constructor injection help tests?
9. Why is a huge `BasePage` a smell?
10. Give one SDET IS-A and one SDET HAS-A example.

## 14. Interview Question

**Question:** Inheritance vs composition. Which should you prefer?

A strong answer:

> Inheritance models IS-A: LoginPage extends BasePage because a login page is a page. Composition models HAS-A: CheckoutPage has a PaymentComponent because the page owns a payment section; it is not itself a payment form. I prefer composition when it is simpler, when the piece is reusable on unrelated pages, or when extending would fake an IS-A relationship. I still use inheritance for real taxonomies and polymorphism, like Browser and ChromeBrowser. In frameworks I often do both: pages extend BasePage and contain components. Favoring composition keeps trees shallow and helpers swappable.

## 15. Homework

Rebuild `SdetComposition` by typing it.

Add `SavedCardsPage extends BasePage` that also HAS-A `PaymentComponent`. Show that two pages share the component type without sharing a silly parent like `class PagesThatPay`.

Write your rule in a comment:

```text
If I cannot say IS-A, I will not extend.
If I can say HAS-A, I will use a field.
```

You have finished Part 12 when you can design a small `User`, a `LoginPage extends BasePage`, a `Browser` polymorphic variable, a `TestDataProvider` interface, and a `CheckoutPage` that has a `PaymentComponent` — and explain each choice.

---

## Answer Key

1. Building a type by holding other objects as parts/helpers.
2. "A has a B."
3. `private final PaymentComponent payment;`
4. The page calls methods on the component instead of copying those steps.
5. Checkout is not a payment form; it has one. Extending locks the type and blocks reuse.
6. Yes. Very common for page objects.
7. False. Use `extends` when IS-A is true.
8. Tests can pass a fake component instead of the real UI piece.
9. Pages inherit actions they do not have; changes become risky; the class becomes a junk drawer.
10. IS-A: `LoginPage extends BasePage`. HAS-A: `CheckoutPage` has `PaymentComponent`.
