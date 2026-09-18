# Chapter 21 — String

## 1. Today's Goal

By the end of this lesson, you will use `String` to store text and call common methods that testers use every day:

- `length()`
- `toUpperCase()`
- `toLowerCase()`
- `contains(...)`
- `startsWith(...)`
- `endsWith(...)`
- `trim()`

You will read a piece of text, clean it, and ask questions about it.

## 2. Why It Matters

Software is full of text:

- usernames
- error messages
- button labels
- API response bodies
- browser names
- file names
- email addresses

A test often needs to ask:

```text
Does this message contain "successful"?
Does this URL start with "https"?
Is this email missing extra spaces?
How long is this token?
```

If you can only print a `String`, you are stuck looking with your eyes. Methods let the program inspect the text for you.

## 3. Real-Life Analogy

A `String` is a sentence written on a card.

Methods are questions you can ask about the card, or machines that produce a **new** card:

```text
How many characters are on it?     → length()
Make a louder copy                 → toUpperCase()
Make a quieter copy                → toLowerCase()
Does it mention "error"?           → contains("error")
Does it begin with "Login"?        → startsWith("Login")
Does it end with "successful"?     → endsWith("successful")
Trim the messy edges               → trim()
```

`trim()` is like cutting extra blank paper off the left and right of the sentence. The words stay the same. The leftover space goes away.

## 4. Illustrated Explanation

```java
String message = "Login successful";
```

```text
Index idea, just to see the characters:

 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
 L o g i n   s u c c  e  s  s  f  u  l
```

You do not need to count indexes by hand yet. Arrays will go deeper on indexes. For `String`, the useful idea is:

```text
message
   │
   ▼
┌──────────────────────────────────┐
│ "Login successful"               │
└──────────────────────────────────┘
   │
   ├── length()         → 16
   ├── toUpperCase()    → "LOGIN SUCCESSFUL"
   ├── toLowerCase()    → "login successful"
   ├── contains("Login")→ true
   ├── startsWith("Log")→ true
   └── endsWith("ful")  → true
```

Extra spaces are real characters:

```text
" Login successful "
 ↑                 ↑
space              space

trim() removes those outer spaces:

" Login successful "  →  "Login successful"
```

A method call looks like:

```text
variable.method()
   │        │
   │        └── the question or transformation
   └── the String you are asking about
```

## 5. Syntax / Concept

Create a `String` with double quotes:

```java
String message = "Login successful";
```

### `length()`

Returns how many characters are in the text, including spaces.

```java
int size = message.length();
```

### `toUpperCase()` and `toLowerCase()`

Return a new `String` in uppercase or lowercase.

```java
String loud = message.toUpperCase();
String quiet = message.toLowerCase();
```

### `contains(...)`

Returns `true` if the text contains the given piece.

```java
boolean hasWord = message.contains("successful");
```

### `startsWith(...)` and `endsWith(...)`

```java
boolean startsOk = message.startsWith("Login");
boolean endsOk = message.endsWith("successful");
```

### `trim()`

Removes leading and trailing whitespace.

```java
String messy = " Login successful ";
String cleaned = messy.trim();
```

These methods are case-sensitive unless you normalize case first. `"Login"` is not the same as `"login"` for `contains`, `startsWith`, and `endsWith`.

A useful pattern in tests:

```java
String cleaned = actualMessage.trim().toLowerCase();
```

That reads: first trim, then make lowercase. You will learn more about chaining later. Using two steps is also fine.

## 6. Simple Example

```java
public class StringMethodsDemo {

    public static void main(String[] args) {
        String message = "Login successful";

        System.out.println(message.length());
        System.out.println(message.toUpperCase());
        System.out.println(message.toLowerCase());
        System.out.println(message.contains("successful"));
        System.out.println(message.startsWith("Login"));
        System.out.println(message.endsWith("successful"));
    }
}
```

Expected output:

```text
16
LOGIN SUCCESSFUL
login successful
true
true
true
```

Now with `trim()`:

```java
public class TrimDemo {

    public static void main(String[] args) {
        String actualMessage = " Login successful ";
        String cleaned = actualMessage.trim();

        System.out.println("Before trim: [" + actualMessage + "]");
        System.out.println("After trim: [" + cleaned + "]");
        System.out.println("Starts with Login? " + cleaned.startsWith("Login"));
    }
}
```

The square brackets make hidden spaces visible.

## 7. Real-World Example

An e-commerce confirmation email subject can be checked with `String` methods.

```java
public class OrderEmailCheck {

    public static void main(String[] args) {
        String subject = "  Your order #4412 has shipped  ";
        String cleaned = subject.trim();

        System.out.println("Length before trim: " + subject.length());
        System.out.println("Length after trim: " + cleaned.length());
        System.out.println("Contains shipped? " + cleaned.contains("shipped"));
        System.out.println("Starts with Your? " + cleaned.startsWith("Your"));
        System.out.println("Uppercase: " + cleaned.toUpperCase());
    }
}
```

A banking example:

```java
public class BankSmsCheck {

    public static void main(String[] args) {
        String sms = "Transfer of $250 completed";

        boolean looksLikeTransfer = sms.startsWith("Transfer");
        boolean completed = sms.contains("completed");
        boolean mentionsWire = sms.toLowerCase().contains("wire");

        System.out.println("Starts as transfer: " + looksLikeTransfer);
        System.out.println("Completed: " + completed);
        System.out.println("Mentions wire: " + mentionsWire);
    }
}
```

`mentionsWire` is `false` because the SMS never contains the word `wire`. That is the method doing its job.

## 8. SDET Example

UI and API tests constantly inspect messages.

```java
public class LoginMessageTest {

    public static void main(String[] args) {
        String actualMessage = " Login successful ";
        String cleaned = actualMessage.trim();

        System.out.println("Cleaned: [" + cleaned + "]");
        System.out.println("Length: " + cleaned.length());
        System.out.println("Upper: " + cleaned.toUpperCase());
        System.out.println("Lower: " + cleaned.toLowerCase());
        System.out.println("Contains successful: " + cleaned.contains("successful"));
        System.out.println("Starts with Login: " + cleaned.startsWith("Login"));
        System.out.println("Ends with successful: " + cleaned.endsWith("successful"));
    }
}
```

A status-line example from an HTTP-like message:

```java
public class ResponseLineCheck {

    public static void main(String[] args) {
        String responseLine = "HTTP/1.1 404 Not Found";

        boolean notFound = responseLine.contains("404");
        boolean startsAsHttp = responseLine.startsWith("HTTP");
        boolean endsWithFound = responseLine.endsWith("Found");

        System.out.println("Has 404: " + notFound);
        System.out.println("Starts with HTTP: " + startsAsHttp);
        System.out.println("Ends with Found: " + endsWithFound);
    }
}
```

These checks are the same idea as later assertions in JUnit or TestNG. First you learn to ask the question in plain Java.

## 9. Break the Code

This test is supposed to confirm that a trimmed message starts with `Login`. It prints `false` even though the words look right.

```java
public class BrokenStartsWith {

    public static void main(String[] args) {
        String actualMessage = " Login successful ";
        boolean startsWithLogin = actualMessage.startsWith("Login");
        System.out.println(startsWithLogin);
    }
}
```

The engineer forgot that leading spaces are characters.

## 10. Debug

`startsWith("Login")` means: do the **first** characters match `Login`?

The actual first character is a space.

```text
" Login successful "
 ↑
space, not 'L'
```

Fix by trimming first:

```java
public class FixedStartsWith {

    public static void main(String[] args) {
        String actualMessage = " Login successful ";
        String cleaned = actualMessage.trim();
        boolean startsWithLogin = cleaned.startsWith("Login");

        System.out.println("[" + cleaned + "]");
        System.out.println(startsWithLogin);
    }
}
```

Expected output:

```text
[Login successful]
true
```

In IntelliJ, print the `String` with brackets or quotes around it whenever spaces are suspected. Do not trust your eyes with trailing whitespace.

Another common bug is case:

```java
actualMessage.contains("Successful")
```

If the real text is `successful` with a lowercase `s`, that check is `false`. Use the exact case, or compare a lowercase copy.

## 11. Student Exercise

Store this message:

```text
  Password reset email sent  
```

Write a program that prints:

- the original length
- the trimmed text
- the trimmed length
- the uppercase version of the trimmed text
- whether it contains `email`
- whether it starts with `Password`
- whether it ends with `sent`

## 12. Challenge

You received this actual banner from a page:

```text
   WELCOME BACK, JOHN   
```

Write checks that would still pass if the product later changed the letters to lowercase. Hint: trim, then convert to lowercase, then use `contains` / `startsWith`.

Print each boolean result with a label:

- contains `welcome`
- starts with `welcome`
- ends with `john`

## 13. Knowledge Check

1. Which quotes create a `String`: single or double?
2. What does `length()` return?
3. Does `contains("ok")` return a `String` or a `boolean`?
4. What does `trim()` remove?
5. Why can `" Login".startsWith("Login")` be `false`?
6. What is the difference between `toUpperCase()` and `toLowerCase()`?
7. Are `contains`, `startsWith`, and `endsWith` case-sensitive?
8. Write a line that checks whether `message` contains `error`.
9. Why do testers trim UI text before asserting?
10. True or false: `endsWith("successful")` looks at the beginning of the text.

## 14. Interview Question

**Question:** Which `String` methods do you use when validating a UI or API message?

A strong answer:

> I store the actual text in a String, trim extra spaces, and then use contains, startsWith, or endsWith depending on what the requirement says. length is useful when a field has a size constraint. toUpperCase and toLowerCase help when the business rule is case-insensitive. I am careful because these methods are case-sensitive unless I normalize the text first.

## 15. Homework

Create `HomeworkStringChecks` for a fake checkout message:

```text
  Order #9001 confirmed  
```

Print a report with every method from today's goal. Then change the stored message to include a failing case, such as `Order #9001 failed`, and run the program again. Notice which booleans flip. That is the beginning of test thinking: one value should pass, another should fail.

---

## Answer Key

1. Double quotes.
2. The number of characters, including spaces.
3. A `boolean`.
4. Leading and trailing whitespace.
5. Because the first character is a space.
6. One returns an uppercase copy, the other a lowercase copy.
7. Yes.
8. `message.contains("error")`
9. Because pages and APIs often add extra spaces that are not part of the requirement.
10. False. It looks at the end.
