# Reflection — When the Program Looks at Itself

## 1. Today's Goal

By the end of this lesson, you will explain **reflection**: the program inspects classes and methods **at runtime**.

You will see why **frameworks**, **dependency injection**, **test discovery**, and **serialization** use it.

You will treat reflection as a sharp tool: **it increases complexity; do not use it without a reason.**

## 2. Why It Matters

You write:

```java
@Test
void login() { }
```

You never call `login()` from `main`. JUnit still runs it. How?

The engine asks the JVM: "What methods does this class have? Which have `@Test`?" That questioning is reflection.

If you do not know this, "magic" becomes your mental model. Magic cannot be debugged. Reflection can: "the method is private," "the annotation retention is SOURCE," "the name string is wrong."

SDET: you still should not reflect to click a private page method in daily tests. Test the public behavior.

## 3. Real-Life Analogy

A museum with a catalog.

```text
Normal visit     →  you walk into Room Login (you know the door)
Reflection visit →  you ask the clerk "list all rooms tagged TEST"
```

The clerk is `Class`, `getDeclaredMethods()`, `getAnnotation()`.

A robot in a warehouse that reads barcodes on boxes instead of being hard-wired to "box A." Flexible. Slower. If the barcode is wrong, the compiler does not save you.

## 4. Illustrated Explanation

```text
Compile time
    login();           compiler checks login exists

Runtime reflection
    Class<?> c = Demo.class;
    Method[] methods = c.getDeclaredMethods();
    for each method
        if method has @Test or @SmokeTest
            method.invoke(...)
```

```text
Who uses this?

Test discovery     JUnit scans @Test
Serialization      Jackson looks at fields / constructors
DI                 Spring creates beans by type
ORMs               map tables to classes

Your beginner test
    usually NONE of this in YOUR code
```

```text
Cost
    no compiler check on "login" the string
    slower than a direct call
    can ignore private (with extra steps)  →  breaks encapsulation
    security managers / modules may deny access
    harder to read
```

```text
Use reflection?
    writing a tiny framework / learning     maybe
    mapping JSON (use Jackson)              library already did
    asserting a private field in a test     usually no — test observable behavior
    because it looks advanced               no
```

## 5. Syntax / Concept

```java
Class<LoginService> type = LoginService.class;
Method[] methods = type.getDeclaredMethods();
```

```java
Method m = type.getDeclaredMethod("login", String.class, String.class);
Object result = m.invoke(service, "john", "Test123");
```

Checked exceptions: `ReflectiveOperationException` and friends. Handle or declare. Do not empty-catch.

Annotations at runtime:

```java
if (method.isAnnotationPresent(SmokeTest.class)) {
    method.invoke(null); // static example only
}
```

Requires `@Retention(RUNTIME)` on `SmokeTest`.

`setAccessible(true)` on private members is a red flag in application tests. Modules (Java 9+) may block it.

Prefer:

- Jackson over hand-rolled field reflection for JSON
- JUnit over writing your own discovery (unless learning)

## 6. Simple Example

```java
import java.lang.reflect.Method;

public class ReflectionListMethods {

    public static void greet() {
        System.out.println("hello");
    }

    public static void main(String[] args) {
        for (Method method : ReflectionListMethods.class.getDeclaredMethods()) {
            System.out.println(method.getName());
        }
    }
}
```

You will see `greet`, `main`, and maybe compiler-generated extras. This is inspection, not a framework.

Calling by name:

```java
Method greet = ReflectionListMethods.class.getDeclaredMethod("greet");
greet.invoke(null);
```

If you rename `greet` to `sayHi` and forget the string, **runtime** failure (`NoSuchMethodException`). Direct `sayHi()` would fail at **compile** time. That contrast is the lesson.

## 7. Real-World Example

Shop: a plugin folder of pricing rules. The app loads classes by name from config. Reflection. Power and pain.

Bank: serializers turn `User` into JSON without you listing every field. Library reflection (or code generation).

You do not reimplement that in a homework login test.

## 8. SDET Example

Tiny discovery (learning only):

```java
import java.lang.reflect.Method;

public class TinyRunner {

    @SmokeTest
    public static void smokeLogin() {
        System.out.println("SMOKE login");
    }

    public static void notATest() {
        System.out.println("should not run");
    }

    public static void main(String[] args) throws Exception {
        for (Method method : TinyRunner.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(SmokeTest.class)) {
                System.out.println("Discovering " + method.getName());
                method.invoke(null);
            }
        }
    }
}
```

This only works if `@SmokeTest` exists with RUNTIME retention. If `isAnnotationPresent` is always false, check retention.

JUnit is this idea with more rules (instance lifecycle, assertions, reports).

Do not reflect into the **application** to set a private `isAdmin = true` to make a test pass. That tests your reflection, not the product.

Empty catch around `invoke` hiding `InvocationTargetException`: the real test failure is in `getCause()`. Swallowing it is the SDET crime again.

## 9. Break the Code

```java
getDeclaredMethod("Login"); // wrong name, capital L
```

`NoSuchMethodException`.

```java
invoke(null); // instance method, null target → NPE or IllegalArgumentException
```

Annotation not RUNTIME: discovery finds nothing. You think tests ran. They did not.

```java
try {
    method.invoke(null);
} catch (Exception e) {
}
```

The test method threw. You hid it. Forbidden.

## 10. Debug

`NoSuchMethodException`: print all `getDeclaredMethods()` names and parameter types. Overloads need the parameter `Class` list.

`IllegalAccessException`: method not accessible. Beginner fix is not immediately `setAccessible`. Make the method public for a learning runner.

`InvocationTargetException`: unwrap `getCause()` — that is the exception from **inside** the method.

`isAnnotationPresent` false: retention, wrong annotation type (imported a different `@Test`), or method not the one you think.

```text
Reflection bugs are runtime.
Direct calls are compile-time.
That is why we avoid reflection in ordinary tests.
```

## 11. Student Exercise

List method names of one of your classes with `getDeclaredMethods()`.

Invoke a public static `ping()` that prints `pong` using `getDeclaredMethod` + `invoke`.

Then call `ping()` normally. Write a comment: compiler checks the normal call.

## 12. Challenge

If you built `@SmokeTest`, write a 15-line runner that invokes static annotated methods and **rethrows** `getCause()` if `invoke` wraps a failure.

If you did not, write a paragraph: "I will let JUnit discover tests. I will not use reflection to reach private fields to pass a test."

## 13. Knowledge Check

1. What is reflection?
2. Name three framework uses.
3. Why does `getMethod("login")` lose compile-time safety?
4. Why must `@SmokeTest` be `RUNTIME` for discovery?
5. Should beginner SDET tests use reflection daily?
6. What is `InvocationTargetException` wrapping?
7. True or false: listing methods with reflection is enough to "understand Spring fully."
8. Why not set private `isAdmin` in tests via reflection?
9. Does Jackson mean you must write reflection?
10. Empty-catch around `invoke`: allowed?

## 14. Interview Question

**Question:** What is reflection, and when should you use it?

A strong answer:

> Reflection is inspecting and invoking classes and methods at runtime using Class, Method, and annotations. Frameworks use it for test discovery, dependency injection, and serialization. It costs safety and clarity: method names are strings, and failures move to runtime. I do not use it without a reason. In SDET work I let JUnit and Jackson do it. I do not use reflection to poke private fields to force a pass, and I never empty-catch invoke failures. If a test fails inside invoke, I rethrow the cause.

## 15. Homework

Notes only if you prefer: draw "compiler-checked call" vs "string method name."

If you code: the list-and-ping exercise.

Write one sentence you could say in an interview: "I know what reflection is so frameworks are not magic; I still avoid it in my tests."

---

## Answer Key

1. Runtime inspection/invocation of classes, methods, fields, annotations.
2. Test discovery, DI, serialization (ORM also OK).
3. The compiler does not check the string.
4. Otherwise it is not visible via `isAnnotationPresent` at runtime.
5. No.
6. The exception thrown by the invoked method.
7. False. That is a start, not mastery.
8. It bypasses product rules and tests the wrong thing.
9. No. The library uses it (or codegen); you use the mapper API.
10. No.
