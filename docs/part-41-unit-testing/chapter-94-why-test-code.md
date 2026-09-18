# Chapter 94 — Why Test Code?

## 1. Today's Goal

By the end of this lesson, you will write a JUnit 5 test with `@Test`, follow **Arrange / Act / Assert**, and assert that `calculator.add(2, 3)` equals `5`.

## 2. Why It Matters

Code that is never tested is a rumor. You ran `main` once. That is not a suite. Tomorrow you will change `add` and break a discount engine. A unit test is a tripwire.

SDET work is writing tests. Start with tests for *your* Java, not for a whole website. If you skip this, you will treat Selenium as the only way to "test," and your pipeline will be slow and flaky.

## 3. Real-Life Analogy

A scale in a kitchen.

```text
Arrange    put the 5 lb bag on the scale
Act        read the display
Assert     it should say 5, not 4.7
```

You do not ship cookies to 200 customers to "test" the scale. You check the scale. Unit tests are the scale.

A fire alarm is a test that should stay quiet until smoke. A failing test is the alarm. A flaky test is an alarm that rings when you toast bread. Do not ship flaky alarms.

## 4. Illustrated Explanation

```text
Production
calculator.add(2, 3)  →  5

Test
shouldAddTwoNumbers
  arrange  calculator
  act      add(2, 3)
  assert   equals 5
```

```text
src/main/java/.../Calculator.java
src/test/java/.../CalculatorTest.java

mvn test
  Surefire runs @Test methods
  green = evidence
  red   = signal
```

JUnit 5 (Jupiter) method:

```java
@Test
void shouldAddTwoNumbers() {
    int actual = calculator.add(2, 3);
    assertEquals(5, actual);
}
```

```text
@Test           this method is a test, not production
void            tests usually return nothing; they throw on failure
should...       a sentence: expected behavior
assertEquals    expected first, actual second in JUnit
```

Arrange-Act-Assert:

```text
Arrange     create Calculator, prepare inputs
Act         one call: add
Assert      one main claim: 5
```

Keep Act small. If Act logs in, clicks five pages, and calls two APIs, it is not a unit test.

## 5. Syntax / Concept

JUnit 5 imports:

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
```

Lifecycle later (`@BeforeEach`) is optional today. One test class can have many `@Test` methods. Each should be independent. JUnit may run them in an order you do not control.

`assertEquals(expected, actual)` — memorize the order. Reversing it makes failure messages lie.

Other asserts you will meet: `assertTrue`, `assertFalse`, `assertThrows`, `assertNotNull`. Start with `assertEquals`.

Test class names: `CalculatorTest`. Method names: `shouldAddTwoNumbers` or `add_twoPositiveNumbers_returnsSum`. Pick a style and keep it.

## 6. Simple Example

`src/main/java/com/training/sdet/Calculator.java`:

```java
package com.training.sdet;

public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }
}
```

`src/test/java/com/training/sdet/CalculatorTest.java`:

```java
package com.training.sdet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void shouldAddTwoNumbers() {
        Calculator calculator = new Calculator();

        int actual = calculator.add(2, 3);

        assertEquals(5, actual);
    }
}
```

Blank lines between arrange, act, assert are a kindness. Run `mvn test`. You want `Tests run: 1, Failures: 0`.

## 7. Real-World Example

Banking interest (simplified):

```java
public class Interest {
    public double simple(double principal, double rate) {
        return principal * rate;
    }
}
```

```java
@Test
void shouldComputeTenPercentOfOneHundred() {
    Interest interest = new Interest();

    double actual = interest.simple(100.0, 0.10);

    assertEquals(10.0, actual);
}
```

For `double`, later you will use `assertEquals(10.0, actual, 0.0001)` because binary floating point is sneaky. Today, know that money in production uses `BigDecimal`. Tests should follow the type you actually use.

E-commerce: unit test a discount function before you click through a browser.

## 8. SDET Example

The course's status helper is a perfect unit:

```java
package com.training.sdet;

public class StatusCodes {

    public static boolean statusMatches(int expected, int actual) {
        return expected == actual;
    }
}
```

```java
@Test
void shouldMatchWhenExpectedEqualsActual() {
    boolean actual = StatusCodes.statusMatches(200, 200);

    assertEquals(true, actual);
    // or assertTrue(StatusCodes.statusMatches(200, 200));
}

@Test
void shouldNotMatchOnNotFound() {
    assertEquals(false, StatusCodes.statusMatches(200, 404));
}
```

This is cheaper than launching Chrome to "see if 404 is not 200."

BAD vs GOOD:

```text
BAD
No assert (test only prints)
One test that covers 40 behaviors
Depends on test2 running first

GOOD
Independent @Test methods
Named like specs
assertEquals(5, calculator.add(2, 3))
```

## 9. Break the Code

```java
@Test
void shouldAddTwoNumbers() {
    Calculator calculator = new Calculator();
    calculator.add(2, 3);
}
```

This test never asserts. It is a green liar.

```java
assertEquals(actual, 5);
```

Arguments reversed. When it fails, JUnit says expected `7` but was `5` and you will debug the wrong side.

```java
public int add(int a, int b) {
    return a - b;
}

@Test
void shouldAddTwoNumbers() {
    assertEquals(5, new Calculator().add(2, 3));
}
```

Red test. Good. That is the tripwire working.

## 10. Debug

When `mvn test` fails:

1. Read the assertion message: expected vs actual.
2. Click the test in IntelliJ. Use the debugger. Step into `add`.
3. Do not delete the test to "make the build green."

If tests are not discovered:

- class is not in `src/test/java`
- method is not `@Test` from JUnit 5 (`org.junit.jupiter.api.Test`, not JUnit 4's `org.junit.Test` unless you configured that)
- method is `private`
- Surefire plugin missing for JUnit 5

```text
JUnit 4   org.junit.Test
JUnit 5   org.junit.jupiter.api.Test
```

This course uses JUnit 5.

## 11. Student Exercise

Add `subtract` to `Calculator`. Write `shouldSubtractTwoNumbers` with arrange-act-assert. Then write a test you expect to fail (`assertEquals(0, calculator.add(2, 3))`), watch it fail, then fix the assert. Feeling a red test on purpose is required.

## 12. Challenge

Write tests for `statusMatches`. Cover 200/200, 200/404, and a boundary you choose (0, 500). Then explain why these are unit tests and a Selenium login is not.

## 13. Knowledge Check

1. What is a unit test?
2. What are Arrange, Act, Assert?
3. What does `@Test` mean?
4. Write the `assertEquals` line for add(2,3) → 5.
5. Expected vs actual order in JUnit?
6. Why is a test without assertions dangerous?
7. Where do test classes live in Maven?
8. Should unit tests open Chrome? Why?
9. Why independent tests?
10. What command runs them in this course?

## 14. Interview Question

**Question:** How do you structure a unit test?

A strong answer:

> I use arrange-act-assert. For a calculator I create the object, call add(2, 3), and assertEquals(5, actual). I mark the method with JUnit @Test. The name says the behavior. Tests are independent and live in src/test/java. A test without an assert is not a test. Unit tests should not need a browser. I run them with mvn test so CI can run the same suite.

## 15. Homework

Put `Calculator` and `CalculatorTest` in your Maven project. `mvn test` must be green. Add one more method (`multiply` or `statusMatches`) with two tests: happy path and mismatch. Commit: `Add JUnit tests for Calculator`.

---

## Answer Key

1. An automated check of a small piece of code.
2. Setup, call, check.
3. JUnit should run this method as a test.
4. `assertEquals(5, actual);` after `add(2, 3)`.
5. Expected first, actual second.
6. It can pass while the code is wrong.
7. `src/test/java`
8. No. Too slow, too many moving parts, not a unit.
9. Order is not guaranteed; failures must be diagnosable alone.
10. `mvn test`
