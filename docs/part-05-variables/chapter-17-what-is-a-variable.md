# Chapter 17 — What Is a Variable?

## 1. Today's Goal

By the end of this lesson, you will be able to create a Java variable, put a value inside it, print that value, and change it later.

You will understand this sentence:

> A variable is a named location in memory that holds a value.

You do not need to become a memory expert today. You only need to understand that Java can remember information for you if you give that information a name.

## 2. Why It Matters

Imagine a login test. The expected status code is `200`. The actual status code from the server is also a number. If you hard-code both numbers into a sentence, the program is rigid:

```java
System.out.println("Expected 200, actual 200");
```

That line only works for one situation. Tomorrow the actual code might be `401`. The day after that it might be `500`. Software must store values so they can change.

Without variables, every number and every name is stuck inside the sentence. With variables, you can store the values first and use them many times:

```text
Store expected = 200
Store actual   = 401
Print both
Compare both
Decide PASS or FAIL
```

Every later topic in this course — conditions, loops, methods, tests — depends on variables.

## 3. Real-Life Analogy

Think of a labeled storage box.

```text
┌────────────────────┐
│ Label: age         │
│                    │
│ Inside the box: 25 │
└────────────────────┘
```

The **label** is the variable name.  
The **thing inside the box** is the value.  
The **kind of thing the box is allowed to hold** is the type.

You can look in the box later. You can replace the contents. You cannot put a bicycle into a box that was built only for numbers.

A kitchen example also helps:

```text
Jar labeled "sugar"
     ↓
You put sugar in it
     ↓
Later you use that sugar
     ↓
You can refill the jar
```

You do not remember "there is sugar in the third jar from the left." You remember the **name**. Computers work the same way. They need names.

## 4. Illustrated Explanation

When you write:

```java
int age = 25;
```

Java does something like this:

```text
You write:  int age = 25;

Java creates a named box:

MEMORY
┌──────────────┐
│ name : age   │
│ type : int   │
│ value: 25    │
└──────────────┘
```

A variable has three parts:

```text
Type  +  Name  +  Value
 │        │        │
int      age      25
```

You can read the value later:

```text
age
  ↓
look inside the box
  ↓
find 25
```

You can replace the value:

```text
age = 26;

BEFORE                AFTER
┌──────────┐          ┌──────────┐
│ age = 25 │    →     │ age = 26 │
└──────────┘          └──────────┘
```

The **name** stays the same. The **value** changes. That is why it is called a *variable*: the contents can vary.

A program can have many boxes at once:

```text
┌─────────────────────┐   ┌─────────────────────┐
│ expectedStatusCode  │   │ actualStatusCode    │
│ 200                 │   │ 404                 │
└─────────────────────┘   └─────────────────────┘

┌─────────────────────┐   ┌─────────────────────┐
│ username            │   │ retryCount          │
│ "john"              │   │ 3                   │
└─────────────────────┘   └─────────────────────┘
```

## 5. Syntax / Concept

The common pattern is:

```java
type name = value;
```

Examples:

```java
int age = 25;
int expectedStatusCode = 200;
String username = "john";
boolean testPassed = true;
```

### Declare, then assign

You can create the box first and put a value in later:

```java
int age;
age = 25;
```

The first line **declares** the variable. The second line **assigns** a value.

### Declare and assign together

This is the style you will use most often:

```java
int age = 25;
```

### Reassign

After a value exists, you can replace it:

```java
int retryCount = 1;
retryCount = 2;
retryCount = 3;
```

`=` is **not** the math equals sign here. In Java, `=` means:

> Take the value on the right and store it in the variable on the left.

```text
retryCount = 3;

   left side              right side
   the box                the new value
        ←─────────────────────────
```

### Printing a variable

Use the variable name, not quotes around the name:

```java
int age = 25;
System.out.println(age);
```

That prints `25`.

If you write `"age"` in quotes, Java prints the word `age`, not the number.

## 6. Simple Example

Create a class named `VariableDemo` and run this program.

```java
public class VariableDemo {

    public static void main(String[] args) {
        int age = 25;
        String firstName = "Amina";
        boolean isStudent = true;

        System.out.println(age);
        System.out.println(firstName);
        System.out.println(isStudent);

        age = 26;
        System.out.println("Updated age:");
        System.out.println(age);
    }
}
```

Expected output:

```text
25
Amina
true
Updated age:
26
```

Read that output slowly. The first printed age is `25`. After the assignment `age = 26`, the same variable name now holds a new value.

## 7. Real-World Example

An online store needs to remember a customer's order.

```java
public class OrderVariables {

    public static void main(String[] args) {
        String customerName = "Sam Rivera";
        int quantity = 2;
        double itemPrice = 49.99;
        String orderStatus = "Processing";

        System.out.println("Customer: " + customerName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Item price: " + itemPrice);
        System.out.println("Status: " + orderStatus);

        orderStatus = "Shipped";
        System.out.println("Updated status: " + orderStatus);
    }
}
```

Expected output:

```text
Customer: Sam Rivera
Quantity: 2
Item price: 49.99
Status: Processing
Updated status: Shipped
```

Notice how `orderStatus` changed from `"Processing"` to `"Shipped"`. The customer's name did not need a new variable. The same labeled box received a new value.

A bank account example works the same way:

```java
public class BankBalance {

    public static void main(String[] args) {
        String accountHolder = "Jordan Lee";
        double balance = 100.00;

        System.out.println(accountHolder + " starts with " + balance);

        balance = 80.00;
        System.out.println("After a $20 withdrawal, balance is " + balance);
    }
}
```

You will learn how to subtract properly in Part 7. Today, the lesson is only that `balance` can change.

## 8. SDET Example

Testers store expected results and actual results.

```java
public class StatusCodeVariables {

    public static void main(String[] args) {
        int expectedStatusCode = 200;
        int actualStatusCode = 200;
        String testName = "Get user by id";

        System.out.println("Test: " + testName);
        System.out.println("Expected: " + expectedStatusCode);
        System.out.println("Actual: " + actualStatusCode);
    }
}
```

Expected output:

```text
Test: Get user by id
Expected: 200
Actual: 200
```

Change `actualStatusCode` to `404` and run it again. You did not rewrite the print statements. You only changed the stored value. That is the power of a variable.

A login example:

```java
public class LoginTestData {

    public static void main(String[] args) {
        String username = "john";
        String password = "Test123";
        boolean loginSuccessful = true;
        int retryCount = 0;

        System.out.println("Username: " + username);
        System.out.println("Password stored, but we will not print it in real reports.");
        System.out.println("Login successful: " + loginSuccessful);
        System.out.println("Retries so far: " + retryCount);
    }
}
```

In professional work, avoid printing real passwords. Even in practice programs, get used to treating secrets carefully.

## 9. Break the Code

This program is supposed to print a user's age after a birthday. It does not work as intended.

```java
public class BrokenAgeUpdate {

    public static void main(String[] args) {
        int age = 25;
        System.out.println("Before birthday: " + age);

        int age = 26;

        System.out.println("After birthday: " + age);
    }
}
```

If you try to run this, IntelliJ will complain. Something is wrong with the second `age` line.

## 10. Debug

The first line `int age = 25;` already created a variable named `age`.

The later line `int age = 26;` tries to create **another** variable with the same name in the same method. Java does not allow two local variables with the same name in the same scope.

You do not need a new box. You need to put a new value in the existing box.

Fix:

```java
public class FixedAgeUpdate {

    public static void main(String[] args) {
        int age = 25;
        System.out.println("Before birthday: " + age);

        age = 26;

        System.out.println("After birthday: " + age);
    }
}
```

How to notice this in IntelliJ:

1. Look at the red underline on the second `int age`.
2. Read the error. It will mention that `age` is already defined.
3. Remove the extra `int`. Keep only `age = 26;`.

```text
WRONG
int age = 25;
int age = 26;   ← trying to create a second box named age

RIGHT
int age = 25;
age = 26;       ← reuse the same box
```

## 11. Student Exercise

Create a class named `MyFirstVariables`.

Store these values:

- your first name as text
- your age as a whole number
- whether you are learning Java as `true`
- the number of tests you want to write this week as a whole number

Print each value on its own line with a clear label.

Then change the number of tests to a different number and print it again.

Do not copy a classmate's names and numbers. Use your own.

## 12. Challenge

Create a tiny order summary for a bookstore.

Use variables for:

- book title
- customer name
- quantity
- unit price
- order status

Print a receipt-style message. Then update `orderStatus` from `"Placed"` to `"Delivered"` and print the new status.

Example shape of the output (your values can differ):

```text
Customer: Priya
Book: Clean Tests
Quantity: 1
Unit price: 29.99
Status: Placed
Status: Delivered
```

## 13. Knowledge Check

Answer these without looking back if you can. Then check the Answer Key at the end of the chapter.

1. In one sentence, what is a variable?
2. What are the three parts of a variable?
3. In `int age = 25;`, which word is the type, which is the name, and which is the value?
4. What does `=` mean in Java assignment?
5. Why is `int age = 25;` followed later by `int age = 26;` a problem in the same method?
6. What is the difference between printing `age` and printing `"age"`?
7. Can the value inside a variable change after it is created?
8. Why would an SDET store `expectedStatusCode` and `actualStatusCode` in variables instead of typing the numbers only inside a sentence?
9. Declare a boolean variable named `testPassed` with the value `false`.
10. True or false: a variable name is only for Java, so humans never need to understand it.

## 14. Interview Question

**Question:** What is a variable in Java, and why do programs need variables?

Practice answering out loud in 30 to 45 seconds. A strong beginner answer sounds like this:

> A variable is a named location that stores a value. It has a type, a name, and a value. Programs need variables so they can remember data, reuse it, and change it later. For example, a test can store an expected status code and an actual status code, then compare them.

Do not recite a textbook sentence you do not understand. Use the box analogy if it helps you stay clear.

## 15. Homework

Write one Java class named `HomeworkVariables`.

It must contain at least five variables that could appear in a real application or a real test:

1. a user name
2. an account balance or item price
3. an HTTP status code
4. a retry count
5. a true/false value for whether a test passed

Print all five values. Then change the retry count and the test-passed value, and print those two again.

Bring your program to the next class ready to explain each variable's type, name, and value.

---

## Answer Key

1. A variable is a named location used to hold a value.
2. Type, name, and value.
3. Type is `int`, name is `age`, value is `25`.
4. Store the value on the right into the variable on the left.
5. `age` already exists. Writing `int age` again tries to declare a second variable with the same name.
6. `age` prints the stored number. `"age"` prints the word age.
7. Yes. That is why it is called a variable.
8. Because those values can change, and the same names can be printed, compared, and reused.
9. `boolean testPassed = false;`
10. False. Names exist for humans as much as for the compiler. Clear names make code readable.
