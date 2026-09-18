# Chapter 18 — Naming Rules

## 1. Today's Goal

By the end of this lesson, you will know which names Java allows, which names Java rejects, and which names are legal but still a bad idea.

You will also learn **camelCase**, the naming style professional Java developers use for variables and methods.

The goal is not to memorize a legal document. The goal is to write names that are correct **and** readable.

## 2. Why It Matters

Java will stop you if a name is illegal:

```java
int 1retry = 3;   // Java rejects this
```

Java will **not** stop you if a name is legal but useless:

```java
int x = 3;
boolean stuff = true;
String abc = "john";
```

That second group compiles. Six months later, nobody knows what `x`, `stuff`, and `abc` mean. In a test class with 200 lines, mystery names become bugs.

SDET work is shared work. Another engineer will read your test. A reviewer will ask, "What does `n` mean?" If you cannot answer instantly, the name is too weak.

```text
Legal name
   ↓
Java accepts it

Good name
   ↓
Java accepts it
AND a teammate understands it
```

You need both.

## 3. Real-Life Analogy

Think about file names on a computer.

Bad:

```text
doc1
newnew
final-final-really-final
```

Good:

```text
invoice-march-2026
login-test-notes
order-history
```

A label on a box in a warehouse works the same way. "Box 7" is legal. "Fragile glassware - Aisle B" is useful.

Names are not decoration. Names are how humans find meaning.

## 4. Illustrated Explanation

Java looks at a name like a security guard at a door.

```text
Proposed name: retryCount
        ↓
Does it start with a letter, $, or _ ?
        ↓
Does it contain only letters, digits, $, or _ after that?
        ↓
Is it not a reserved Java keyword?
        ↓
Then Java allows it.
```

Then a second, quieter question appears:

```text
If a teammate reads retryCount
in a login test...
        ↓
Do they immediately understand it?
        ↓
Yes. Keep it.
```

Compare two versions of the same idea:

```text
BAD                         GOOD
┌─────────────┐             ┌─────────────────────┐
│ x           │             │ retryCount          │
│ abc         │             │ expectedStatusCode  │
│ stuff       │             │ loginSuccessful     │
│ n           │             │ customerFirstName   │
└─────────────┘             └─────────────────────┘
```

Both columns can compile. Only the right column teaches the reader.

Java variable names use **camelCase**:

```text
first word starts lowercase
later words start with a capital letter

firstName
expectedResult
testExecutionCount
loginSuccessful
```

Picture it as one word made of smaller words glued together:

```text
expected + Status + Code
            ↓
expectedStatusCode
```

There are no spaces. Java names cannot contain spaces.

## 5. Syntax / Concept

### Rules Java enforces

A variable name:

- must start with a letter, `$`, or `_`
- may continue with letters, digits, `$`, or `_`
- cannot be a reserved keyword such as `int`, `class`, `public`, `true`, or `if`
- cannot contain spaces
- is case-sensitive: `age` and `Age` are different names

Legal examples:

```java
int age;
int retryCount;
int expectedStatusCode;
boolean loginSuccessful;
String firstName;
```

Illegal examples:

```java
int 2retry;          // cannot start with a digit
int retry count;     // cannot contain a space
int int;             // cannot use a keyword
int expected-status; // hyphen is not allowed
```

Professionals almost never start names with `$` or `_`. Those are legal, but they look strange in ordinary Java code. Prefer letters.

### Rules humans enforce

A good name:

- describes the value
- uses full words, not secret abbreviations
- uses camelCase for variables
- is a noun or short noun phrase: `username`, `retryCount`, `orderTotal`
- is a boolean that reads like a yes/no question: `isValid`, `loginSuccessful`, `testPassed`

```java
// GOOD
String firstName;
int retryCount;
boolean loginSuccessful;

// BAD
String x;
int abc;
boolean stuff;
```

### camelCase vs other styles

```text
camelCase     firstName          ← Java variables and methods
PascalCase    FirstName          ← Java class names
snake_case    first_name         ← common in other languages, not Java style
SCREAMING     MAX_RETRY          ← later, for constants
```

Today, use camelCase for variables.

### Case sensitivity

```java
int age = 25;
int Age = 40;
```

Those are two different variables. Do not create both. Pick one style and stay consistent. For variables, start with a lowercase letter.

## 6. Simple Example

```java
public class NamingDemo {

    public static void main(String[] args) {
        String firstName = "Chris";
        int retryCount = 3;
        boolean loginSuccessful = true;

        System.out.println(firstName);
        System.out.println(retryCount);
        System.out.println(loginSuccessful);
    }
}
```

Now look at a version that Java may accept but a reviewer should reject:

```java
public class PoorNamingDemo {

    public static void main(String[] args) {
        String x = "Chris";
        int n = 3;
        boolean flag = true;

        System.out.println(x);
        System.out.println(n);
        System.out.println(flag);
    }
}
```

The second program works. It is still a poor program. `flag` does not tell us *what* is true. `n` does not tell us *what was counted*.

## 7. Real-World Example

An e-commerce checkout should use names that match the business.

```java
public class CheckoutNames {

    public static void main(String[] args) {
        String customerEmail = "sam@example.com";
        int itemQuantity = 4;
        double unitPrice = 12.50;
        double shippingCost = 5.99;
        boolean paymentApproved = true;
        String deliveryCity = "Boston";

        System.out.println("Email: " + customerEmail);
        System.out.println("Quantity: " + itemQuantity);
        System.out.println("Unit price: " + unitPrice);
        System.out.println("Shipping: " + shippingCost);
        System.out.println("Payment approved: " + paymentApproved);
        System.out.println("City: " + deliveryCity);
    }
}
```

A banking example:

```java
public class TransferNames {

    public static void main(String[] args) {
        String fromAccountId = "CHK-1001";
        String toAccountId = "SAV-2002";
        double transferAmount = 250.00;
        boolean transferComplete = false;

        System.out.println("From: " + fromAccountId);
        System.out.println("To: " + toAccountId);
        System.out.println("Amount: " + transferAmount);
        System.out.println("Complete: " + transferComplete);
    }
}
```

If those variables were named `a`, `b`, `c`, and `d`, a mistake in the from/to accounts would be much harder to see.

## 8. SDET Example

Test code is read more often than it is written. Names should describe the test data.

```java
public class LoginNamingExample {

    public static void main(String[] args) {
        String validUsername = "john";
        String validPassword = "Test123";
        String invalidUsername = "john";
        String invalidPassword = "wrong-password";
        int expectedStatusCode = 200;
        int actualStatusCode = 401;
        boolean credentialsAccepted = false;

        System.out.println("Valid user: " + validUsername);
        System.out.println("Expected status: " + expectedStatusCode);
        System.out.println("Actual status: " + actualStatusCode);
        System.out.println("Credentials accepted: " + credentialsAccepted);
    }
}
```

Notice the difference between `expectedStatusCode` and `actualStatusCode`. Those two names prevent a classic tester mistake: mixing up which number is the requirement and which number came from the system.

A retry example:

```java
public class RetryNamingExample {

    public static void main(String[] args) {
        int maxRetryCount = 3;
        int currentAttempt = 1;
        boolean pageLoaded = false;

        System.out.println("Attempt " + currentAttempt + " of " + maxRetryCount);
        System.out.println("Page loaded: " + pageLoaded);
    }
}
```

`i` might be acceptable later as a tiny loop counter. It is a weak name for business meaning.

## 9. Break the Code

This class is trying to store test information, but several names are illegal or confusing.

```java
public class BrokenNames {

    public static void main(String[] args) {
        int 1stAttempt = 1;
        String user-name = "john";
        boolean class = true;
        int ExpectedStatusCode = 200;
        int x = 404;

        System.out.println(1stAttempt);
        System.out.println(user-name);
        System.out.println(class);
        System.out.println(ExpectedStatusCode);
        System.out.println(x);
    }
}
```

This file should not compile. Even if you "fixed" only enough to compile, some names would still be poor style.

## 10. Debug

Walk through each problem:

| Line | Problem | Why | Fix |
| --- | --- | --- | --- |
| `1stAttempt` | Starts with a digit | Java forbids this | `firstAttempt` |
| `user-name` | Contains a hyphen | Java sees subtraction, not a name | `userName` |
| `class` | Reserved keyword | `class` already means a Java class | `loginSuccessful` or `inClass` |
| `ExpectedStatusCode` | Compiles, but starts with a capital | Looks like a class name | `expectedStatusCode` |
| `x` | Compiles, but meaning is hidden | Readers must guess | `actualStatusCode` |

Corrected program:

```java
public class FixedNames {

    public static void main(String[] args) {
        int firstAttempt = 1;
        String userName = "john";
        boolean loginSuccessful = true;
        int expectedStatusCode = 200;
        int actualStatusCode = 404;

        System.out.println(firstAttempt);
        System.out.println(userName);
        System.out.println(loginSuccessful);
        System.out.println(expectedStatusCode);
        System.out.println(actualStatusCode);
    }
}
```

In IntelliJ, illegal names usually get a red underline immediately. Vague names do not. That is why style is a human skill, not only a compiler skill.

## 11. Student Exercise

Rewrite this messy program. Keep the same values. Improve every name. Use camelCase.

```java
public class MessyStore {

    public static void main(String[] args) {
        String n = "Maya";
        int q = 3;
        double p = 19.99;
        boolean f = true;
        int c = 201;
    }
}
```

Those values represent:

- a customer name
- a quantity of items
- a price
- whether checkout finished
- an HTTP status code from a create-order API

Print each renamed variable with a label.

## 12. Challenge

Create a class named `NamingChallenge`.

Invent names for a failed password-reset test. You need variables for:

- the email used
- the expected confirmation message
- the actual confirmation message
- the number of times the user clicked Send
- whether the test passed

None of your names may be a single letter. None may start with an uppercase letter. Print a short test report.

## 13. Knowledge Check

1. Can a Java variable name start with a number?
2. Is `retryCount` camelCase?
3. Why is `stuff` a bad variable name even if Java accepts it?
4. Which is the better boolean name: `flag` or `loginSuccessful`? Why?
5. What is wrong with `String user name = "john";`?
6. Are `status` and `Status` the same variable?
7. Should ordinary Java variables start with an uppercase letter?
8. Why do SDET engineers distinguish `expectedStatusCode` from `actualStatusCode`?
9. Name one reserved keyword you must not use as a variable name.
10. True or false: if the code compiles, the names are good enough.

## 14. Interview Question

**Question:** How do you choose good variable names in Java?

A strong answer:

> Java names must follow identifier rules: no spaces, no hyphens, no starting digit, and no reserved keywords. Beyond that, names should explain the meaning. I use camelCase for variables, full words instead of vague letters, and boolean names that read like a condition, such as `loginSuccessful`. In testing, I name expected and actual values clearly so assertions are easy to read.

## 15. Homework

Open any program you wrote in Chapter 17. Rename every variable so that:

- each name uses camelCase
- no name is a single letter
- a stranger could guess the meaning from the name alone

Then write a new class named `NamedTestData` with at least six well-named variables for a login API test. Print them as a data sheet:

```text
Test data
username: ...
expectedStatusCode: ...
actualStatusCode: ...
```

Be ready to defend every name in class. If you cannot explain a name, rename it.

---

## Answer Key

1. No.
2. Yes. The first word is lowercase and later words start with a capital letter.
3. It does not describe the stored value. Readers have to guess.
4. `loginSuccessful`, because it states what is true or false.
5. Variable names cannot contain spaces.
6. No. Java names are case-sensitive.
7. No. Class names often start uppercase. Variable names start lowercase.
8. So nobody confuses the requirement with the observed result.
9. Examples include `int`, `class`, `public`, `if`, `true`, and `false`.
10. False. Compiling only means the name is legal, not that it is clear.
