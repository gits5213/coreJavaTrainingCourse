# Chapter 93 — Clean Code

## 1. Today's Goal

By the end of this lesson, you will rewrite unclear Java so names, method size, logic, duplication, and responsibility are obvious. You will reject `doIt(String x)` in your own work.

## 2. Why It Matters

SDET code is read more than it is written. A flake at 2 a.m. is debugged by a person who did not write the test. If they cannot see what the method is for, they will "fix" the wrong thing or add `Thread.sleep(5000)`.

Clean code is a reliability feature.

## 3. Real-Life Analogy

A kitchen labeled `Stuff` vs drawers labeled `Knives`, `Spices`, `Towels`.

```text
doIt(x)                 drawer named Stuff
createTestUser(name)    drawer named Test Users
```

A recipe that is 400 steps in one paragraph is a method that does everything. You burn the sauce because you cannot find the salt line.

## 4. Illustrated Explanation

Five habits this course hammers:

```text
Meaningful names
Small focused methods
Readable logic
Minimal duplication
Clear responsibility
```

```text
BAD name          GOOD name
x                 username
flag              isExpired
doIt              createTestUser
handle            submitLoginForm
data              LoginData
```

```text
BAD method
400 lines
clicks, SQL, HTTP, email, screenshot, asserts

GOOD methods
enterUsername
enterPassword
submit
assertDashboardVisible
```

Duplication:

```text
BAD: same login typed in 20 tests
GOOD: login(username, password) in one place (and later, a page object)
```

Responsibility:

```text
A class named LoginPage should log in
It should not also send email and query the database
```

## 5. Syntax / Concept

Java does not force clean names. You do.

Rules of thumb:

- Methods are verbs: `createTestUser`, `statusMatches`, `openLoginPage`
- Booleans read as questions: `isValid`, `hasExpired`, `statusMatches`
- Classes are nouns: `User`, `Order`, `LoginPage`
- Avoid `Util`, `Manager`, `Stuff`, `Helper` unless the class really is a thin helper — and even then, name the *kind* of help: `ScreenshotService`
- One method, one job. If you need `and` to describe it, split it
- Do not copy-paste a block three times; extract a method
- Do not hide `boolean` meaning in `int flag`

Clean is not "as few lines as possible." A clear `if` beats a clever one-liner.

## 6. Simple Example

BAD:

```java
public class T {
    public void doIt(String x) {
        System.out.println(x);
    }
}
```

GOOD:

```java
public class TestUserPrinter {

    public void createTestUser(String username) {
        System.out.println("Creating test user: " + username);
    }
}
```

The good version still does something tiny. The name is the lesson.

A slightly more real clean-up:

```java
// BAD
public static boolean f(int a, int b) {
    if (a == b) {
        return true;
    } else {
        return false;
    }
}

// GOOD
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
}
```

## 7. Real-World Example

Banking transfer method:

```java
// BAD
public void go(String a, String b, double c) {
    // 80 lines: UI, DB, email, log
}

// GOOD
public void transfer(String fromAccount, String toAccount, double amount) {
    validateAccounts(fromAccount, toAccount);
    validateAmount(amount);
    submitTransfer(fromAccount, toAccount, amount);
}
```

Each private method can be understood. The public method reads like a story.

E-commerce checkout: `placeOrder(Cart cart)` not `process(Object o)`.

## 8. SDET Example

```java
// BAD
@Test
void test1() {
    driver.findElement(By.id("u")).sendKeys("x");
    driver.findElement(By.id("p")).sendKeys("y");
    driver.findElement(By.id("b")).click();
    // no assertion name, magic ids
}

// GOOD
@Test
void shouldReachDashboardAfterValidLogin() {
    loginPage.enterUsername("standard_user");
    loginPage.enterPassword(testPassword());
    loginPage.submit();
    assertTrue(dashboardPage.isLoaded(), "Dashboard should be visible after login");
}
```

Password still should not be logged. Clean names are not an excuse to print secrets.

BAD vs GOOD (from the curriculum):

```java
// BAD
public void doIt(String x) {
}

// GOOD
public void createTestUser(String username) {
}
```

## 9. Break the Code

```java
public void createTestUser(String username) {
    openAdminPage();
    fillUserForm(username);
    save();
    sendWelcomeEmail();
    queryDatabaseForUser(username);
    takeScreenshot();
    writeExcelReport();
}
```

The *name* is good. The *body* is a lie. The method does seven jobs. Reviewers will miss the email part. Tests will fail when SMTP is down even if user creation worked.

Another break: renaming to `createTestUser` but leaving parameter `x`. Half-clean is still dirty.

## 10. Debug

When you cannot debug a failure:

1. Read the method name. Does the body match?
2. If the stack trace is 12 methods deep of `doIt` / `process` / `handle`, rename first, then fix.
3. If you see duplicated login, extract. Do not add a 13th copy.

IntelliJ: **Refactor → Rename**. Use it. Do not fear renaming. Git history + commit message explain the rename.

Smell checklist:

```text
Can I say this method's job in one breath without "and"?
Would a new hire guess the parameter from its name?
Is the boolean inverted (if (!isNotInvalid))?
```

## 11. Student Exercise

Rewrite this class. Keep behavior. Fix names and split methods.

```java
public class X {
    public String run(String a, String b) {
        if (a == b) {
            return "OK";
        }
        return "BAD " + a + " " + b;
    }
}
```

Assume `a` is expected status as String and `b` is actual. Choose better types if you want (`int`), but names matter most.

## 12. Challenge

Take any test or `main` you already wrote (Test Result Evaluator is perfect). Rename until a stranger could guess behavior from names alone. Extract methods until `main` is a short story. Commit with message `Clarify names in test result evaluator`.

## 13. Knowledge Check

1. Name the five clean-code habits from this chapter.
2. Why is `doIt(String x)` a problem?
3. Verb vs noun: methods vs classes?
4. When should you split a method?
5. Is shorter always cleaner?
6. Why do test method names matter?
7. What is wrong with a well-named method that does seven jobs?
8. True or false: duplication is always evil (hint: three copies vs accidental coupling).
9. Why avoid logging passwords while "cleaning" a login method?
10. What IntelliJ tool helps you rename safely?

## 14. Interview Question

**Question:** What does clean code mean to you as an SDET?

A strong answer:

> Clean code is readable by the next engineer. I use meaningful names like createTestUser(String username) instead of doIt(String x). Methods stay small and do one job. Logic reads top to bottom. Duplication of login or locators is extracted. A class has a clear responsibility: LoginPage does UI login, not email and database. Clean test names are specifications. Clean is not clever. It is kind to the person debugging a flake at 2 a.m.

## 15. Homework

Hunt your training repo for `temp`, `data`, `flag`, `doIt`, `test1`. Rename them. Open a PR (even solo) titled `Clean names in training project`.

---

## Answer Key

1. Meaningful names, small methods, readable logic, minimal duplication, clear responsibility.
2. Nobody knows the action or the data.
3. Methods verbs; classes nouns.
4. When you need "and" to describe it, or it hides mixed levels (UI + SQL).
5. No. Clarity beats golfing.
6. They document the spec; they show up in reports.
7. The name lies; callers cannot predict side effects.
8. False if you copy once by accident vs coupling two unrelated flows — but login copied 20 times is not "justified." Prefer extract. Tiny duplication can be OK if extraction would create a bad abstraction. The curriculum still wants minimal duplication.
9. Reports and CI logs leak.
10. Refactor Rename (and Extract Method).
