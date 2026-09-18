# Chapter 70 — Why Generics?

## 1. Today's Goal

By the end of this lesson, you will explain why Java has **generics**, and you will read and write a tiny envelope type:

```java
public class ApiResponse<T> {
    public int status;
    public T data;
}
```

You will use `ApiResponse<User>` and `ApiResponse<Order>` so `data` is a `User` or an `Order`, not a vague `Object`.

## 2. Why It Matters

Without generics, a list or a response box holds `Object`. You cast:

```java
User u = (User) response.data;
```

If `data` is actually an `Order`, you get `ClassCastException` **when the test runs**. Generics make `ApiResponse<User>` a different type from `ApiResponse<Order>`. The compiler argues with you **before** CI.

SDET code is full of envelopes: HTTP status plus body. Modeling that once with `<T>` avoids copy-paste `UserResponse`, `OrderResponse`, `TokenResponse` that differ by one field.

## 3. Real-Life Analogy

A labeled box.

```text
Box<Shoes>     you expect shoes
Box<Books>     you expect books
Box            unlabeled. you reach in. it might be a fish.
```

A restaurant "combo meal" is generic: the combo always has a drink slot and a food slot. `Combo<Burger>` vs `Combo<Salad>` changes the food type, not the idea of a combo.

Shipping envelope: the envelope is the same shape. The contents type is the generic parameter.

## 4. Illustrated Explanation

Without generics:

```text
ApiResponse
  status: 200
  data: Object  →  could be User, Order, String, Integer...
                    you cast and hope
```

With generics:

```text
ApiResponse<User>
  status: 200
  data: User     compiler knows

ApiResponse<Order>
  status: 200
  data: Order    compiler knows
```

```text
T is a placeholder

When you write ApiResponse<User>, T becomes User
When you write ApiResponse<Order>, T becomes Order
```

```text
        ApiResponse<T>
        ┌─────────────┐
        │ status: int │
        │ data:   T   │
        └─────────────┘
           /        \
          /          \
 ApiResponse<User>   ApiResponse<Order>
 data is User        data is Order
```

Raw type (avoid):

```text
ApiResponse response;  // raw. compiler warns. you went back to Object land.
```

## 5. Syntax / Concept

Class with a type parameter:

```java
public class ApiResponse<T> {
    public int status;
    public T data;

    public ApiResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}
```

Use:

```java
ApiResponse<User> userResponse = new ApiResponse<>(200, user);
ApiResponse<Order> orderResponse = new ApiResponse<>(200, order);
User u = userResponse.data; // no cast
```

`T` is a name by convention (Type). You will also see `E` (element), `K` (key), `V` (value).

Generics on lists (you may have seen):

```java
List<String> names = new ArrayList<>();
```

Diamond `<>` tells the compiler to infer the type from the left side.

Constraints (preview, not homework): `T extends Number` would limit T. Skip for this chapter.

You cannot use primitive `int` as `T`. Use `Integer`. `ApiResponse<int>` does not compile. `ApiResponse<Integer>` does.

## 6. Simple Example

```java
public class WhyGenericsDemo {

    public static void main(String[] args) {
        User user = new User("john", "tester");
        ApiResponse<User> response = new ApiResponse<>(200, user);

        System.out.println(response.status);
        System.out.println(response.data.username);
    }
}

class ApiResponse<T> {
    int status;
    T data;

    ApiResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}

class User {
    String username;
    String role;

    User(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
```

Expected:

```text
200
john
```

## 7. Real-World Example

Shop:

```java
Order order = new Order("A-1", "PAID");
ApiResponse<Order> response = new ApiResponse<>(201, order);
```

Bank:

```java
ApiResponse<User> profile = new ApiResponse<>(200, new User("ada", "customer"));
```

Same envelope class. Different payload. The HTTP layer at work is this idea: status plus body.

A 404 might be `ApiResponse<User>` with `data == null` and `status == 404`. Then you assert on status first. Optional (Part 23) can model missing data more honestly later.

## 8. SDET Example

```java
public class ApiResponseAsserts {

    public static <T> void assertOk(ApiResponse<T> response) {
        if (response.status != 200) {
            throw new AssertionError("TEST FAILED — status " + response.status);
        }
        if (response.data == null) {
            throw new AssertionError("TEST FAILED — data was null");
        }
        System.out.println("TEST PASSED — status 200 with data");
    }

    public static void main(String[] args) {
        ApiResponse<User> users = new ApiResponse<>(200, new User("john", "tester"));
        ApiResponse<Order> orders = new ApiResponse<>(200, new Order("A-1", "PAID"));
        assertOk(users);
        assertOk(orders);
    }
}
```

`assertOk` is a **generic method**. It works for any `T`. You write the assertion once.

Without generics you would cast inside and accidentally accept an Order when you wanted a User.

Empty catch around `assertOk` is still forbidden.

## 9. Break the Code

```java
ApiResponse response = new ApiResponse(200, "oops");
User u = (User) response.data;
```

Raw type. `data` is a String. Cast throws `ClassCastException`.

Another:

```java
ApiResponse<User> r = new ApiResponse<User>(200, new Order("A-1", "PAID"));
```

Does not compile if `Order` is not a `User`. That is the feature. People "fix" it by removing `<User>` and casting. Do not.

Another: `ApiResponse<int>` — will not compile.

## 10. Debug

`ClassCastException` after a cast from `data`: you used a raw type or `ApiResponse<Object>`. Put generics back.

If IntelliJ says incompatible types, read both sides of `=`. Left `ApiResponse<User>`, right `ApiResponse<Order>` — those are not the same.

Debugger: inspect `response.data` and look at the **runtime class** shown in Variables.

```text
Compile error on assignment  →  generics doing their job
Runtime ClassCastException   →  someone bypassed generics
```

## 11. Student Exercise

Copy `ApiResponse<T>`, `User`, and `Order` (`id`, `status` strings).

In `main`, build `ApiResponse<User>` with 200 and a user, `ApiResponse<Order>` with 201 and an order. Print status and one field from `data` for each.

## 12. Challenge

Write `assertStatus(ApiResponse<?> response, int expected)` using a **wildcard** `?` meaning "some type, I only care about status."

Write `assertUserRole(ApiResponse<User> response, String expectedRole)`.

Happy path plus a failing status (AssertionError). Do not catch empty.

If `?` feels too new, write `assertStatus` as a generic method `<T>` instead. Either is a win.

## 13. Knowledge Check

1. What problem do generics reduce?
2. What is `T` in `ApiResponse<T>`?
3. What is `data`'s type in `ApiResponse<User>`?
4. Why not use `Object` for `data` always?
5. Can `T` be the primitive `int`?
6. What is a raw type?
7. Why does SDET like one envelope class?
8. True or false: `ApiResponse<User>` and `ApiResponse<Order>` are the same type.
9. What exception appears when a bad cast from `Object` fails?
10. Should you empty-catch `ClassCastException` to keep tests green?

## 14. Interview Question

**Question:** Why does Java have generics? Give an SDET example.

A strong answer:

> Generics let a class or method work with a type parameter checked at compile time. Instead of storing Object and casting, I write ApiResponse<T> with int status and T data. Then ApiResponse<User> and ApiResponse<Order> share the envelope but keep a safe payload type. That models API responses. Raw types and casts fail late with ClassCastException. I want those mistakes at compile time. I still fail tests loudly; I do not hide exceptions.

## 15. Homework

Draw `ApiResponse<T>` as a box with `status` and `data: T`. Draw two instances: User and Order.

Write a `Token` class with a `String value`. Create `ApiResponse<Token>` with status 200. Print the token.

Notes: "T is a placeholder. The compiler fills it in."

---

## Answer Key

1. Runtime cast errors; duplicated envelope classes.
2. A type parameter (placeholder).
3. `User`
4. You lose compile-time checks and must cast.
5. No. Use `Integer`.
6. Using the generic class without `<...>`, which behaves like `Object`.
7. Status plus body repeats on every endpoint; `<T>` is the body.
8. False.
9. `ClassCastException`
10. No.
