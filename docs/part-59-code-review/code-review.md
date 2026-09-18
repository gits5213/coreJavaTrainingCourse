# Code Review

## Goal

By the end of this lesson, you will review a Java/automation PR using the curriculum checklist, leave comments that address risk (not taste alone), and accept that test code is production-adjacent.

## Why It Matters

CI does not catch a God `LoginPage`, a singleton driver, or a password in a log. Humans do. SDET teams without review accumulate a museum of sleeps and XPaths.

Interviews: "What do you look for in a PR?" Recite the list with examples.

## Real-Life Analogy

A second pilot's checklist before takeoff.

```text
one purpose          is this lever doing two jobs?
names                is the switch labeled?
duplication          two altimeters disagreeing?
exceptions           do we ignore engine fire lights?
resources            fuel cap closed?
shared state         two captains on one yoke?
independent tests    can we abort one check without aborting all?
secrets              passenger passports not on the PA
inheritance          do we need a 747 to deliver a letter?
composition          bolt on a radio instead of rebuilding the plane?
testable             can we verify the warning light?
```

Be a copilot, not a bully. The goal is a safer flight, not a higher comment count.

## Illustrated Explanation

```text
PR opened
  CI runs
  human reads diff with questions
  comments
  author fixes
  approve + merge
```

Review **diff + context**. A 2000-line PR cannot be reviewed. Ask to split (KISS).

SDET extras while you walk the list:

```text
waits vs sleep
locators stability
quit/ThreadLocal
assertions present
data independence
Allure/PII
```

## The Review Questions (with Java)

### Does this method have one purpose?

Reject `loginAndSeedDatabaseAndEmail`. Split.

### Are names meaningful?

Reject `doIt(String x)`. Want `createTestUser(String username)`.

### Is duplication justified?

Two identical login sequences: extract page method. Two similar but *accidentally* same lines: maybe do not couple them. Ask.

### Is exception handling correct?

Empty `catch (Exception e) {}` is a defect. Catching too broad and returning null hides bugs. Tests should fail on unexpected exceptions.

### Are resources closed?

`WebDriver.quit()`, JDBC try-with-resources, streams. AfterMethod with try/finally.

### Is shared state safe?

Static `WebDriver`, mutable static maps of users, tests writing the same account. Parallel?

### Are tests independent?

Must not require `test1` before `test2`. No order dependency.

### Are secrets exposed?

Passwords in source, logs, Allure, screenshots, `testng.xml`.

### Is inheritance necessary?

`AbstractBaseSuperTestHelper` 6 levels deep. Prefer composition: a `LoginPage` field, not extending `SeleniumJungle`.

### Could composition be simpler?

Yes, usually. "is-a BaseTest" for sharing a driver manager is a thin exception; still prefer a `DriverManager` object.

### Is code testable?

Hard `new ChromeDriver()` inside a private method you cannot override. Hard `DriverManager.get()` scattered vs inject `WebDriver`. Time `new Date()` vs clock. Static everything.

## Simple Example — Review This Diff

```java
public void doIt(String x) {
    try {
        driver.findElement(By.xpath("/html/body/div[1]/input")).sendKeys(x);
        Thread.sleep(5000);
        System.out.println("password=" + x);
    } catch (Exception e) {
    }
}
```

A review comment (kind, specific):

> This method mixes navigation, a brittle absolute XPath, a sleep, and it logs the password. Empty catch will hide failures. Suggest: rename to enterPassword, By.id, WebDriverWait, no logging of x, let exceptions fail the test.

## Real-World Example

Banking PR adds `TransferPage`. Reviewer checks: amount parsing, no sleep, no production account numbers, API vs UI split, tests independent, Grid quit.

## SDET Example Checklist Card

Copy into PR templates:

```text
[ ] One purpose per method
[ ] Meaningful names
[ ] Duplication justified or extracted
[ ] Exceptions not swallowed
[ ] Driver/JDBC closed
[ ] No unsafe static mutable state
[ ] Tests independent
[ ] No secrets
[ ] Inheritance not used as a junk drawer
[ ] Composition considered
[ ] Testable (inject driver, no hidden sleeps)
```

## Break the Code (as a reviewer)

Approve because CI is green while the diff adds `Thread.sleep(5000)` and a password print. You failed the review.

Nitpick braces for 40 comments and miss the singleton driver. You failed the review.

## Debug (review process)

If reviews are slow: PRs too big. If reviews are toxic: switch to the checklist and examples, not "this is dumb." If reviews rubber-stamp: pair on one PR a week as training.

## Student Exercise

Review the `doIt` snippet above. Write 5 comments mapped to checklist questions. Then rewrite the method cleanly.

## Challenge

Review a classmate's PR (or your old commit). Fill the 11 questions with yes/no + one sentence. Open comments on GitHub if possible.

## Knowledge Check

1. Recite the 11 questions.
2. Why review test code?
3. Give a swallowed-exception example.
4. Give a shared-state example.
5. Give a secrets example.
6. Inheritance vs composition in tests?
7. What makes a good review comment?
8. Should you block on formatting only?
9. Can green CI skip review?
10. Why small PRs?

## Interview Question

**Question:** How do you review automation code?

A strong answer:

> I treat tests as production-adjacent. I ask: does this method have one purpose? Are names meaningful? Is duplication justified? Is exception handling correct? Are resources closed? Is shared state safe? Are tests independent? Are secrets exposed? Is inheritance necessary? Could composition be simpler? Is code testable? I look for sleeps, absolute XPath, singleton WebDriver, empty catches, and logged tokens. Comments are specific and kind. CI passing is not enough.

## Homework

Add the 11 questions to `.github/pull_request_template.md`. Use them on your next PR. Commit `Add code review checklist to PR template`.

---

## Answer Key

1. The curriculum list
2. Tests fail pipelines and leak secrets too
3. `catch (Exception e) {}`
4. static WebDriver
5. password in log or repo
6. Prefer page objects as fields; avoid deep BaseTest trees
7. Specific, about risk, suggests a direction
8. No — use a formatter; block on risk
9. No
10. Humans can actually read them
