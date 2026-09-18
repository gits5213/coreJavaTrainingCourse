# Chapter 51 — Abstract Class

## 1. Today's Goal

By the end of this lesson, you will write an **abstract class** with an **abstract method** that subclasses must implement.

You will model `PaymentService` so that **`pay()` must exist**, while `CardPaymentService` and `WalletPaymentService` each decide *how*.

You will not be able to write `new PaymentService()` — and that is the point.

## 2. Why It Matters

A parent class like `Payment` with a fake `pay()` that prints "somehow" is dangerous. Someone will `new Payment()` and think they charged a card.

An abstract class says:

```text
This type is a category, not a finished product.
You may inherit it.
You may not construct it.
If I declare pay() abstract, every concrete child must implement pay().
```

SDET use: `BaseTest` that cannot be run by itself, `DriverManager` categories, `BasePage` that still allows construction... wait. Not every base should be abstract. Use abstract when a method **has no honest generic body**.

`Browser.launch()` as "Launch generic" was a teaching lie. In real code, launching generic is meaningless. `abstract void launch();` forces Chrome and Firefox to write a real launch.

## 3. Real-Life Analogy

A job posting: "Payment processor."

You cannot hire the posting. You hire a card processor or a wallet processor. The posting requires: "You must be able to pay."

A blank tax form labeled "Vehicle." You cannot register "Vehicle." You register a car or a truck. Both must provide a VIN.

A recipe titled "Bake dessert" with a line: `[insert dessert-specific steps here]`. That line is abstract. Chocolate cake and apple pie fill it in. You do not serve the blank recipe.

## 4. Illustrated Explanation

```text
abstract class PaymentService
        │
        │  abstract pay(amount)
        │  concrete receiptText()   ← shared, real code
        │
        ├── CardPaymentService     must write pay()
        └── WalletPaymentService   must write pay()

new PaymentService()     ILLEGAL
new CardPaymentService() legal
```

```text
ABSTRACT CLASS
  can have:
    fields
    constructors (for children to call)
    concrete methods
    abstract methods
  cannot:
    be instantiated with new
```

If **every** method is abstract and there is no shared code, an **interface** (next chapter) is often clearer. Abstract class shines when there is shared implementation **plus** holes to fill.

```text
Shared kitchen tools     → concrete methods on abstract class
Required dish            → abstract method
You cannot cook "Food"   → cannot new the abstract type
```

## 5. Syntax / Concept

```java
abstract class PaymentService {
    public abstract void pay(double amount);

    public String receiptText(double amount) {
        return "Receipt: " + amount;
    }
}
```

`abstract` on the class is required if any method is abstract.

Child:

```java
class CardPaymentService extends PaymentService {
    @Override
    public void pay(double amount) {
        System.out.println("Charging card " + amount);
    }
}
```

If the child does not implement `pay`, the child must also be `abstract`.

Constructors: the abstract class can have one. Children call `super(...)`. You still never `new` the abstract type.

Abstract methods have **no body**:

```java
public abstract void pay(double amount); // semicolon, no { }
```

Polymorphism still works:

```java
PaymentService payment = new CardPaymentService();
payment.pay(20.00);
```

The variable can be the abstract type. The object cannot.

## 6. Simple Example

```java
abstract class PaymentService {
    public abstract void pay(double amount);

    public void printReceipt(double amount) {
        System.out.println("Paid " + amount);
    }
}

class CardPaymentService extends PaymentService {
    @Override
    public void pay(double amount) {
        System.out.println("Charge card " + amount);
    }
}

class WalletPaymentService extends PaymentService {
    @Override
    public void pay(double amount) {
        System.out.println("Debit wallet " + amount);
    }
}

public class AbstractPaymentDemo {

    public static void checkout(PaymentService payment, double amount) {
        payment.pay(amount);
        payment.printReceipt(amount);
    }

    public static void main(String[] args) {
        checkout(new CardPaymentService(), 19.99);
        checkout(new WalletPaymentService(), 5.00);
        // PaymentService x = new PaymentService(); // does not compile
    }
}
```

Expected output:

```text
Charge card 19.99
Paid 19.99
Debit wallet 5.00
Paid 5.00
```

## 7. Real-World Example

Bank accounts that must implement `withdraw` differently (savings may block, checking may allow overdraft fee):

```java
abstract class BankAccount {
    private double balance;

    public BankAccount(double opening) {
        this.balance = opening;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        setBalance(getBalance() + amount);
    }

    public abstract boolean withdraw(double amount);
}

class CheckingAccount extends BankAccount {
    public CheckingAccount(double opening) {
        super(opening);
    }

    @Override
    public boolean withdraw(double amount) {
        setBalance(getBalance() - amount); // allows overdraft for the demo
        return true;
    }
}

class SavingsAccount extends BankAccount {
    public SavingsAccount(double opening) {
        super(opening);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > getBalance()) {
            return false;
        }
        setBalance(getBalance() - amount);
        return true;
    }
}
```

You cannot `new BankAccount(50)` — "a generic account" is not a product the bank sells.

## 8. SDET Example

Abstract browser: no generic launch.

```java
abstract class Browser {
    public abstract void launch();

    public void quit() {
        System.out.println("Quit session");
    }
}

class ChromeBrowser extends Browser {
    @Override
    public void launch() {
        System.out.println("Launch Chrome");
    }
}

class FirefoxBrowser extends Browser {
    @Override
    public void launch() {
        System.out.println("Launch Firefox");
    }
}

public class AbstractBrowserDemo {

    public static void run(Browser browser) {
        browser.launch();
        System.out.println("Run smoke steps");
        browser.quit();
    }

    public static void main(String[] args) {
        run(new ChromeBrowser());
        run(new FirefoxBrowser());
    }
}
```

Abstract `BaseTest` idea (still no JUnit required):

```java
abstract class BaseTest {
    public abstract String testName();

    public void run() {
        System.out.println("START " + testName());
        steps();
        System.out.println("END " + testName());
    }

    public abstract void steps();
}
```

Every concrete test must name itself and provide steps. The start/end logging is shared.

## 9. Break the Code

Instantiating the abstract type:

```java
PaymentService payment = new PaymentService(); // does not compile
```

Forgetting `abstract` on the class while declaring an abstract method: does not compile.

Leaving `pay` unimplemented in a concrete child: does not compile.

Giving an abstract method a body:

```java
public abstract void pay(double amount) {
    System.out.println("no"); // illegal
}
```

Making everything abstract in a class that has useful shared code — then you duplicated the idea of an interface and lost the shared methods. Keep shared methods concrete.

Using abstract when a normal class with a real default would do. If generic `quit()` is fine, it does not need to be abstract.

## 10. Debug

`PaymentService is abstract; cannot be instantiated` — construct a child, or if you truly wanted a generic object, the type should not have been abstract.

`Class must either be declared abstract or implement abstract method pay` — write `pay` in the child, or mark the child abstract too.

If you implemented `pay` but still get the error, the signature differs (`pay(int)` vs `pay(double)`). Use `@Override`.

Debugger: you will never stop on `new PaymentService`. You stop on `new CardPaymentService`. The variable type may still show as `PaymentService`.

## 11. Student Exercise

Write `abstract class PaymentService` with `abstract void pay(double amount)` and concrete `void log(String message)`.

Write `CardPaymentService` and `WalletPaymentService`.

Write `static void purchase(PaymentService payment, double amount)` that logs and pays.

Call `purchase` twice from `main` with the two children.

Keep the illegal `new PaymentService()` in a comment.

## 12. Challenge

Write `abstract class Browser` with:

- `abstract void launch();`
- `abstract String name();`
- concrete `void smoke(String url)` that launches, prints `Open url with name()`, quits
- concrete `void quit()` 

Implement Chrome and Firefox.

In `main`, store both as `Browser` and call `smoke`.

Then try to think of a third browser. Add `EdgeBrowser` without changing `smoke`.

## 13. Knowledge Check

1. What is an abstract class?
2. Can you write `new PaymentService()` if `PaymentService` is abstract?
3. What is an abstract method?
4. What must a concrete subclass do with abstract methods?
5. Can an abstract class have constructors and concrete methods?
6. Why make `pay()` abstract instead of printing "pay somehow"?
7. Write the header of an abstract `pay` method.
8. True or false: a variable can have an abstract type.
9. When is an interface a better fit than an abstract class? (Preview.)
10. Why is `abstract class Browser` a good model for `launch()`?

## 14. Interview Question

**Question:** What is an abstract class? How is it different from a concrete class and from an interface?

A strong answer:

> An abstract class cannot be instantiated. It can mix concrete methods, fields, constructors, and abstract methods. Abstract methods have no body; subclasses must implement them unless they are also abstract. I use it when types share code but one or more operations have no honest default — PaymentService.pay() or Browser.launch(). A concrete class can be new'ed and usually has bodies for all methods. An interface (next topic) is a contract, often with no shared fields; a class can implement many interfaces but extend only one class. I pick an abstract class when I have shared implementation plus required holes.

## 15. Homework

Implement the payment example and the abstract browser example.

Write a 6-line comment:

```text
I use abstract when:
I still put shared methods on the abstract class because:
I will not new the abstract type because:
```

---

## Answer Key

1. A class you cannot instantiate; it may include abstract methods.
2. No.
3. A method with no body that subclasses must implement.
4. Implement them, or stay abstract.
5. Yes.
6. So nobody ships a fake payment that only prints.
7. `public abstract void pay(double amount);`
8. True. The object must be a concrete subclass.
9. When you mainly need a contract, especially multiple contracts, with little shared state.
10. There is no real generic browser to launch; each vendor fills in `launch`.
