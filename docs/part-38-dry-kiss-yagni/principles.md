# DRY, KISS, and YAGNI

## Goal

By the end of this lesson, you will apply **DRY**, **KISS**, and **YAGNI** to Java and to automation design. You will refuse to build a "world-class framework" on day one of a two-test project.

## Why It Matters

Juniors copy 40-layer architectures from blogs. Then they cannot write a login test. Seniors delete those layers.

SDET interviews listen for: "I extract duplication when I see it, I keep tests readable, I do not add Grid + Kafka + custom annotation processors before I have a flake problem those tools solve."

## Real-Life Analogy

**DRY:** one master ingredients list, not twelve photocopies with different salt amounts.

**KISS:** a bicycle lock vs a bank vault on a bicycle. The vault is impressive. You never ride.

**YAGNI:** packing a snow shovel for a beach day "just in case we get transferred to Alaska at lunch."

```text
Kitchen
DRY     one recipe for pasta sauce, reused
KISS    a pot, not a molecular gastronomy lab
YAGNI   do not buy a pizza oven because you might cook pizza in 2029
```

## Illustrated Explanation

```text
DRY     same knowledge in one place
KISS    the simplest design that works and can be read
YAGNI   do not build for imaginary future scale
```

Tension (honest teaching):

```text
DRY taken too far     a "generic" method with 12 flags nobody understands
KISS taken too far    copy-paste forever, never extract LoginPage
YAGNI taken too far   refuse to add a second environment until production burns
```

Professionals balance. The curriculum's warning is still the main one:

> Do not create complex frameworks for problems you do not have.

```text
Problem you HAVE              Reasonable response
Login typed 5 times           extract a method / page object
Tests on 1 browser            Chrome only is OK
CI not running                add a simple GitHub Action
5000 tests, 50 people         THEN architecture (Part 60)
```

## DRY — Don't Repeat Yourself

Duplication of *knowledge* is the enemy. If the password field id lives in 20 tests, a UI change is 20 bugs.

```java
// Repeated knowledge: how we decide pass/fail
if (actual == expected) {
    System.out.println("TEST PASSED");
} else {
    System.out.println("TEST FAILED");
}
```

Better:

```java
public static boolean statusMatches(int expected, int actual) {
    return expected == actual;
}

public static void printResult(int expected, int actual) {
    if (statusMatches(expected, actual)) {
        System.out.println("TEST PASSED");
    } else {
        System.out.println("TEST FAILED expected=" + expected + " actual=" + actual);
    }
}
```

SDET DRY:

```java
// BAD — password field knowledge copied
driver.findElement(By.id("password")).sendKeys(password);

// GOOD — one place
loginPage.enterPassword(password);
```

Do not DRY coincidences. Two tests both using the string `"Chrome"` is not always duplication of knowledge. Two tests both encoding the login *algorithm* is.

## KISS — Keep It Simple

Simple means a new hire can follow the path without a treasure map.

```java
// KISS
public int add(int a, int b) {
    return a + b;
}

// Not KISS (showing off)
public int add(int a, int b) {
    return IntStream.of(a, b).sum();
}
```

Both work. The stream helps nobody in a two-integer add.

SDET KISS:

```text
KISS
Test calls LoginPage
LoginPage uses WebDriver
Assert dashboard

NOT KISS (yet)
Custom DSL
Reflection to inject locators
XML-driven keyword engine
Inheritance tree 8 levels deep named AbstractBaseGenericTest
```

Keyword frameworks are sometimes justified in huge orgs. They are not your week-1 homework.

## YAGNI — You Aren't Gonna Need It

Do not add Selenium Grid because a blog mentioned Grid. Do not add Kafka to a UI test framework. Do not write `FutureProofDriverManagerV2` for one Chrome test.

```java
// YAGNI violation
public class DriverFactory {
    public WebDriver create(BrowserType type, Proxy proxy, Locale locale,
                            byte[] customProfile, BlockchainNotary notary) {
        // you have one test and zero proxies
    }
}

// Honest start
public class DriverFactory {
    public static WebDriver chrome() {
        return new ChromeDriver();
    }
}
```

When a second browser is a *real* requirement, extend. Architecture grows from pain (Part 61).

## SDET Example Combined

A student with two API tests:

```text
YAGNI  skip Grid, skip parallel, skip custom report portal
KISS   Rest Assured given/when/then in the test, maybe one client class
DRY    base URI and auth header in one place, not in both tests
```

A team with 5000 tests:

```text
NOW you need factories, config, parallel, CI, reporting
That is not YAGNI — that is the problem you have
```

## Break / Debug the Anti-Patterns

**Symptom:** 15 interfaces for one `click`.  
**Cause:** copied Spring-style architecture.  
**Fix:** delete until a test is readable again.

**Symptom:** changing a locator requires 40 file edits.  
**Cause:** not enough DRY.  
**Fix:** page object.

**Symptom:** nobody can add a test without a 2-hour orientation.  
**Cause:** not enough KISS.  
**Fix:** flatten layers that do not earn their keep.

## Student Exercise

You have this duplication:

```java
System.out.println("Opening Chrome");
WebDriver driver = new ChromeDriver();
driver.get("https://example.com");
System.out.println("Opening Chrome");
WebDriver driver2 = new ChromeDriver();
driver2.get("https://example.com/login");
```

Rewrite with DRY (a method) and KISS (no factory hierarchy). Do **not** add Grid. List one YAGNI temptation you refused.

## Challenge

Write a one-page "framework constitution" with three columns: we DRY this, we keep simple this, we postpone this. Include: locators, reporting, parallel, Docker, custom annotations.

## Knowledge Check

1. What does DRY stand for?
2. What knowledge should not be repeated in UI tests?
3. What does KISS stand for in this course?
4. Give a KISS vs clever example in Java.
5. What does YAGNI stand for?
6. Quote the critical lesson about frameworks.
7. When is a DriverFactory not YAGNI?
8. How can DRY go wrong?
9. True or false: YAGNI means never design ahead.
10. How do these three talk to Part 61 (evolution)?

## Interview Question

**Question:** How do you apply DRY, KISS, and YAGNI in automation?

A strong answer:

> DRY: locators, login, and credentials-handling live in one place, not in every test. KISS: a test should read arrange-act-assert; I avoid keyword engines and deep inheritance until they solve a real pain. YAGNI: I do not build Grid, parallel ThreadLocal, and a custom report portal for two tests. I do not create complex frameworks for problems I do not have. When the team has 5000 tests and three environments, those tools stop being YAGNI and become the work.

## Homework

Find duplication in your training code and extract one method. Find an over-designed idea you were about to add (a 4th layer of helpers) and write a note: "YAGNI until ___ happens." Fill in the blank with a real trigger (second browser, second environment, 20 tests).

---

## Answer Key

1. Don't Repeat Yourself.
2. How to find elements, how to log in, environment URLs, header construction.
3. Keep It Simple.
4. `return a + b` vs an unnecessary stream or reflection.
5. You Aren't Gonna Need It.
6. Do not create complex frameworks for problems you do not have.
7. When more than one browser or driver setup is an actual requirement.
8. A generic mega-method with flags; wrong abstraction.
9. False. It means do not build unused speculation. Real upcoming requirements can be designed.
10. Architecture is added when problems appear, in that order.
