# Week 2 — Variables to Methods

**Coverage:** Parts 5–10 (variables, data types, operators, decisions, loops, methods)

**Suggested timebox:** 75 minutes

| Activity | Time |
| --- | --- |
| Quiz (closed book) | 15 minutes |
| Coding assignment | 30 minutes |
| Debugging problem | 15 minutes |
| Explanation exercise | 10 minutes |
| Buffer | 5 minutes |

Score: Theory 20%, Coding 35%, Problem Solving 20%, Debugging 15%, Explanation 10%. Pass bar: 70% weighted. Coding cannot be skipped.

Part 11 (arrays) is next in the book. You do not need arrays for this week's coding task.

---

## Quiz (10 questions)

1. What is a variable? Name its three pieces (type, name, value).
2. Primitive vs reference type: give one example of each. Where does the object live for a reference type (one word is enough if you know it)?
3. Why is `String` immutable? What happens if you call `trim()` and do not assign the result?
4. When do you use `==`, and when do you use `.equals()`?
5. Write a boolean expression that is true only when `actualStatus` is 200 **and** `responseTimeMs` is less than 2000.
6. What is the difference between `if / else` and a chain of `else if`? When is a traditional `switch` a better fit?
7. `for` vs `while`: when would you choose each? What must every loop have?
8. What do `break` and `continue` do inside a loop?
9. What is a method? Can you declare a method *inside* `main`?
10. What does `void` mean on a method? What is overloading?

---

## Coding assignment

**Title:** `statusMatches` and a small test-result reporter

This is the course's core method. You will see it again on the live-coding exam.

### Requirements

Create a class `StatusChecker` with:

1. A method:

   ```java
   public static boolean statusMatches(int expected, int actual)
   ```

   Return `true` when the two numbers are equal, otherwise `false`.

2. A method:

   ```java
   public static void printResult(int expected, int actual)
   ```

   - If they match, print `TEST PASSED`.
   - If they do not, print `TEST FAILED`, then print the expected value once, then the actual value once.

3. A `main` method that demonstrates:
   - expected `200`, actual `200`
   - expected `201`, actual `200`
   - expected `404`, actual `404`

### Acceptance criteria

- [ ] `statusMatches(200, 200)` is `true`.
- [ ] `statusMatches(200, 404)` is `false`.
- [ ] Failure output includes both expected and actual, each once (not duplicated in a confusing way).
- [ ] Methods are declared on the class, not nested inside `main`.
- [ ] Names are readable (`expected`, `actual`, not `x` and `y`).
- [ ] The program compiles and runs.

### Problem-solving stretch (Problem Solving 20%)

Add a third method `describeFamily(int statusCode)` that uses `if / else if` or `switch` to print a short family name:

- 200–299 → `success`
- 400–499 → `client error`
- 500–599 → `server error`
- anything else → `other`

Call it from `main` for `200`, `404`, and `500`.

---

## Debugging problem

```java
public class StatusChecker {
    public static void main(String[] args) {
        public static boolean statusMatches(int expected, int actual) {
            return expected == actual;
        }

        int expected = 200;
        int actual = 404;
        if (statusMatches(expected, actual)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }
}
```

### Your job

1. Reproduce: paste, compile, read the first error.
2. State the cause in one or two sentences.
3. Apply a **minimal** fix (move the method; do not delete the idea).
4. Re-run. Then say whether the printed result is correct for 200 vs 404.

### Expected diagnosis

Java does not allow a method to be declared inside another method. `statusMatches` is nested inside `main`, so the compiler rejects the `public static` declaration there.

**Fix:** move `statusMatches` to class level (sibling of `main`), then call it from `main`.

After the compile fix, the runtime behavior should print `TEST FAILED` because 200 is not 404. If a student "fixes" the compile error by hard-coding `true`, that is not a correct diagnosis.

---

## Explanation exercise

**Prompt (60–90 seconds spoken, or a short paragraph):**

> Why do SDET frameworks use methods? Give a login example. Mention what happens when the login steps change.

**Strong answer hits:**

- A method is a named recipe written once and called many times.
- Copy-pasting login into 20 tests means 20 places to fix when the password field's id changes.
- Parameters send data in (`username`, `password`); return values send answers out (`boolean` from `statusMatches`).
- Page objects later are methods on objects; this week's `public static` methods are the same skill without objects yet.

---

## Answer key

1. A named box that holds one value at a time. Type, name, value (for example `int expected = 200`).
2. Primitive: `int`, `boolean`, `double`, `char`, … Reference: `String`, arrays, future `User`. The object is on the **heap**; the variable holds a reference.
3. `String` methods return a new `String`. The original is unchanged. `text.trim();` without assignment looks like it did nothing.
4. `==` for primitives (status codes as `int`). `.equals()` (or `equalsIgnoreCase`) for `String` content. `==` on `String` is identity, not everyday text equality.
5. `actualStatus == 200 && responseTimeMs < 2000`
6. `if / else` is two paths. `else if` checks conditions in order (200, then 404, then 500, then else). `switch` is a good fit when one variable has discrete values (status families, browser names). This course taught traditional `switch` with `break` first.
7. `for` when the count or range is known. `while` when you repeat until a condition changes. Every loop needs a way to finish (or it is infinite).
8. `break` leaves the loop immediately. `continue` skips the rest of this round and starts the next.
9. A named recipe. **No** — you cannot nest methods inside `main`.
10. `void` means the method does not return a value. Overloading: same method name, different parameter lists.

**Coding key:** `return expected == actual;`

**Debugging key:** nested method inside `main`; move to class scope.
