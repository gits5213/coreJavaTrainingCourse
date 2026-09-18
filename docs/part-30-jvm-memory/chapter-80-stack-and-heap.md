# Chapter 80 — Stack and Heap

## 1. Today's Goal

By the end of this lesson, you will explain:

```java
User user = new User("John");
```

as a **reference on the stack** (or in a stack frame) pointing at a **User object on the heap**.

You will not mix "the variable" with "the object."

## 2. Why It Matters

`NullPointerException` is "I followed an arrow that pointed nowhere." `==` on objects compares references (arrows), not always the contents. Passing an object into a method passes the **arrow** (the reference), so the method can change the same heap object.

SDET: a `WebDriver driver` variable is a reference. Two variables can point at the same browser. Closing via one makes the other stale. That is a heap object with two arrows.

Memory talk in interviews is this diagram, not memorizing heap sizes.

## 3. Real-Life Analogy

A coat check.

```text
Ticket in your hand     →  stack reference (the variable)
Coat on the rack        →  heap object
Ticket #17              →  the arrow
```

Losing the ticket does not instantly teleport the coat out of existence (GC is next chapter). Photocopying the ticket (copying the reference) does not clone the coat.

A house address on a sticky note (stack) vs the house (heap). Two sticky notes can have the same address.

Primitives (`int count = 3`) are more like a number written **on** the sticky note, not an address of a house. (Local primitives live in the stack frame.)

## 4. Illustrated Explanation

```text
User user = new User("John");

STACK (method's frame)              HEAP
┌──────────────┐                    ┌─────────────────┐
│ user  ●──────┼───────────────────►│ User            │
└──────────────┘                    │  name: "John"   │
                                    └─────────────────┘
```

`new User("John")` allocates the house. `user` stores the address.

```text
User a = new User("John");
User b = a;

STACK                 HEAP
 a ●────┐
        ├──► User John
 b ●────┘
```

`a == b` is true (same arrow target). `equals` might also be true if you wrote it; different question.

```text
User a = new User("John");
User b = new User("John");

 a ●──► User John (object 1)
 b ●──► User John (object 2)

a == b is false  (two houses, similar furniture)
```

Method call:

```text
main frame:  user ●──► heap User
call print(user)
print frame:  u    ●──► SAME heap User
```

When `print` returns, its frame is popped. The heap object remains if `main` still has `user`.

```text
int n = 5;

STACK
 n = 5     (the 5 lives here, not a User house)
```

## 5. Syntax / Concept

```java
User user = new User("John");
```

| Piece | Where |
| --- | --- |
| `user` | Variable holding a reference |
| `new User("John")` | Object allocated on the heap |
| `"John"` | A String object (also heap; pool details optional) |

`user = null;` clears the arrow. The object may still sit on the heap until GC if nothing else points to it.

```java
user.name = "Ada"; // follow the arrow, change the house (if field is accessible)
```

Reassignment:

```java
user = new User("Ada"); // old John object may become unreachable
```

Stack overflow: too many nested method frames (`StackOverflowError`) — infinite recursion, not "heap full." Heap full: `OutOfMemoryError`. Different rooms, different disasters.

Each **thread** has its own stack. The heap is shared. That sentence matters in Part 31.

## 6. Simple Example

```java
public class StackHeapDemo {

    public static void main(String[] args) {
        User user = new User("John");
        System.out.println(user.name);
        User alias = user;
        alias.name = "Jonathan";
        System.out.println(user.name);
    }
}

class User {
    String name;

    User(String name) {
        this.name = name;
    }
}
```

Expected:

```text
John
Jonathan
```

Two variables, **one** object. Changing via `alias` changed what `user` sees.

## 7. Real-World Example

Shop: `Order order = new Order(...)`. Passing `order` to `applyDiscount(order)` can mutate the same order on the heap.

Bank: `Account a = customer.getAccount(); Account b = a;` — two references, one balance. Debiting through `b` affects `a`.

This is why immutability (records) is calming: you cannot change the house, only point to a new one.

## 8. SDET Example

```java
class FakeDriver {
    boolean open = true;
}

public class DriverReferences {

    public static void close(FakeDriver driver) {
        driver.open = false;
    }

    public static void main(String[] args) {
        FakeDriver driver = new FakeDriver();
        FakeDriver also = driver;
        close(also);
        if (driver.open) {
            throw new AssertionError("TEST FAILED — browser still open");
        }
        System.out.println("Same heap object was closed");
    }
}
```

Page objects holding the same driver reference: expected. Creating `new ChromeDriver()` twice by accident: two heap browsers, leak.

`User expected = actual;` then mutate expected — you mutated actual. Tests should copy values or use immutable records.

## 9. Break the Code

```java
User user = null;
System.out.println(user.name); // NPE — no house, you followed a blank ticket
```

Thinking `User user2 = user1` copies the object. It copies the arrow.

```java
if (user1 == user2) { // identity, not always equal content
```

Using `==` on strings from different sources. Heap + String pool stories confuse beginners. Prefer `equals` for content.

## 10. Debug

NPE: the reference is null. In the debugger, `user` shows `null`. The object is not "empty User"; there is no object.

Wrong sharing: inspect both variables' **id** in IntelliJ (identity). Same id → same heap object.

`OutOfMemoryError`: heap. `StackOverflowError`: stack. Read the error name.

```text
Variable  →  arrow
new       →  heap object
null      →  arrow to nothing
```

## 11. Student Exercise

Create `User` with `name`. `User user = new User("John")`. Print `user.name`.

Create `User alias = user`, change `alias.name`, print `user.name` again.

Create `User other = new User("John")`. Print `user == alias` and `user == other`.

## 12. Challenge

Write `rename(User user, String newName)` that sets the name. Prove from `main` that the caller's object changed.

Then write a version that uses a **record** `UserRecord(String name)` and returns a new record instead of mutating. Print that the original record is unchanged (you must reassign to see the new name).

## 13. Knowledge Check

1. Where does `new User("John")` live?
2. What does the variable `user` store?
3. What does `User b = a` copy?
4. Why does changing `alias.name` change `user.name` in the demo?
5. What is `user = null`?
6. `==` on two references compares what?
7. StackOverflow vs OutOfMemory — which is heap?
8. True or false: each thread has its own heap.
9. Why do two page objects sharing a driver both see a close?
10. Empty-catch NPE instead of fixing a null reference: allowed?

## 14. Interview Question

**Question:** Where do objects and variables live in the JVM?

A strong answer:

> User user = new User("John") puts a User object on the heap and stores a reference in the local variable user, in the current stack frame. Copying the variable copies the reference, not the object, so two variables can share one heap object. Primitives like int live in the frame. Null means the reference points nowhere; using it is NullPointerException. Threads have separate stacks and a shared heap. Testers care because the same WebDriver object can be referenced from many pages.

## 15. Homework

Draw the stack/heap diagram for `User user = new User("John")` without looking. Then compare to section 4.

Draw `a` and `b` sharing, then two `new` objects.

Notes: "Arrow vs house."

---

## Answer Key

1. Heap.
2. A reference (arrow) to the heap object.
3. The reference, not a new object.
4. They point at the same heap object.
5. The variable holds no target; object may become unreachable.
6. Whether they point at the same object (identity).
7. OutOfMemory — heap. StackOverflow — stack.
8. False. Heap is shared. Stacks are per thread.
9. Same heap object; `open` or session is shared state.
10. No.
