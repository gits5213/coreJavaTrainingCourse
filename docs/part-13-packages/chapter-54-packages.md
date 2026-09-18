# Chapter 54 — Packages

## 1. Today's Goal

By the end of this lesson, you will declare a **package**, **import** a class from another package, and sketch a standard SDET project tree:

```text
com.company.project.pages
com.company.project.api
com.company.project.models
com.company.project.utils
com.company.project.tests
```

You will understand why `package` is the first line of a `.java` file, and why the folder path must match.

## 2. Why It Matters

`User` is a popular name. Your app has `User`. A library has `User`. If both sit with no package, Java cannot tell them apart.

Packages also encode **who may talk to whom**. Chapter 45's package-private members are visible to classes in the same package. Tests in `com.company.project.tests` should not see package-private helpers in `pages` unless you intended that.

SDET frameworks that dump 80 classes into `src` become unownable. A tree is a map for the next teammate (including future you).

## 3. Real-Life Analogy

A company mailroom.

```text
Company / Shop / QA / Pages / LoginPage

Address:
  com.company.project.pages.LoginPage
```

You do not store legal contracts in the kitchen. You do not store page objects in `utils` because "it is a helper."

A library:

```text
Fiction / Mystery / Author / Title
```

The path is the classification. The book still has a title (`LoginPage`). The path says where it lives.

Shipping: country.company.project.area. The dots are nested folders, like nested addresses.

## 4. Illustrated Explanation

```text
src/main/java/
  com/
    company/
      project/
        pages/
          LoginPage.java      package com.company.project.pages;
        models/
          User.java           package com.company.project.models;
        tests/
          LoginTest.java      package com.company.project.tests;
```

Full type name (FQN — fully qualified name):

```text
com.company.project.models.User
 │    │        │      │      │
 │    │        │      │      └── class
 │    │        │      └── package segment
 │    │        └── project
 │    └── organization
 └── reverse domain style
```

Why reverse domain? To keep names unique worldwide. `com.google...` vs `com.yourcompany...`. You may not own a domain yet. Still use a stable prefix: `com.company.project` in this course.

```text
LoginTest wants User

Option A: import com.company.project.models.User;
          then write User john = ...

Option B: write the FQN every time
          com.company.project.models.User john = ...
```

Import is not copying code. It is a short name for a long address.

Default package: no `package` line. Fine for tiny class-period demos. Not fine for a project. You cannot import a default-package class into a named package in a clean way. Graduate to named packages.

## 5. Syntax / Concept

First line (except comments):

```java
package com.company.project.models;
```

Folder must be `com/company/project/models/`.

Import:

```java
package com.company.project.tests;

import com.company.project.models.User;
import com.company.project.pages.LoginPage;
```

Wildcard:

```java
import com.company.project.models.*;
```

This course prefers explicit imports so you can see dependencies. Wildcards are not evil; they hide the list.

Classes in the **same package** do not need imports.

`java.lang` (`String`, `System`, `Math`) is imported automatically.

Access reminder:

```text
public class        → other packages can use it
package-private class → only the same package
public method       → if the class is visible
package-private method → same package only
```

Typical SDET tree (IntelliJ / Maven style later):

```text
com.company.project
  pages      LoginPage, HomePage, CheckoutPage
  api        UserClient, OrderClient
  models     User, Order, Product
  utils      WaitTimes, Json
  tests      LoginTest, CheckoutTest
```

Rules of traffic:

```text
tests  →  pages, api, models
pages  →  models (sometimes utils)
api    →  models
utils  →  almost nothing (no pages, no tests)
models →  nothing in pages/tests
```

Do not put tests under `pages`. Do not put `User` under `utils`.

Package names: all lowercase, no hyphens, no Java keywords. `com.company.project.pages` not `com.company.project.Pages`.

## 6. Simple Example

Imagine three files. You cannot actually keep three public classes in one teaching snippet as separate files, so each block is one file.

**File:** `src/main/java/com/company/project/models/User.java`

```java
package com.company.project.models;

public class User {
    private final String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
```

**File:** `src/main/java/com/company/project/pages/LoginPage.java`

```java
package com.company.project.pages;

import com.company.project.models.User;

public class LoginPage {
    public void login(User user) {
        System.out.println("Login as " + user.getUsername());
    }
}
```

**File:** `src/main/java/com/company/project/tests/LoginDemo.java`

```java
package com.company.project.tests;

import com.company.project.models.User;
import com.company.project.pages.LoginPage;

public class LoginDemo {
    public static void main(String[] args) {
        User user = new User("standard_user");
        LoginPage page = new LoginPage();
        page.login(user);
    }
}
```

Expected output when you run `LoginDemo`:

```text
Login as standard_user
```

If the `package` line and the folder disagree, IntelliJ will warn. Believe it. Move the file or fix the line.

## 7. Real-World Example

Bank project:

```text
com.javabank.ledger.models.BankAccount
com.javabank.ledger.services.TransferService
com.javabank.ledger.api.AccountClient
com.javabank.ledger.utils.Money
com.javabank.ledger.tests.TransferTest
```

Shop:

```text
com.shop.catalog.models.Product
com.shop.checkout.pages.CheckoutPage
com.shop.checkout.pages.components.PaymentComponent
```

Notice `components` nested under `pages` when the team is large. Nested packages are still just folders.

A collision story:

```text
com.shop.models.User
com.otherlib.User

import com.shop.models.User;  // you choose
```

If you need both in one file, import one and FQN the other.

## 8. SDET Example

Full sketch:

```text
com.company.project
├── pages
│   ├── BasePage.java
│   ├── LoginPage.java
│   ├── HomePage.java
│   └── CheckoutPage.java
├── api
│   ├── UserClient.java
│   └── OrderClient.java
├── models
│   ├── User.java
│   └── Order.java
├── utils
│   └── StatusCodes.java
└── tests
    ├── LoginTest.java
    └── CheckoutApiTest.java
```

**utils/StatusCodes.java**

```java
package com.company.project.utils;

public final class StatusCodes {
    private StatusCodes() {
    }

    public static boolean isSuccess(int code) {
        return code >= 200 && code < 300;
    }
}
```

**api/UserClient.java**

```java
package com.company.project.api;

import com.company.project.models.User;

public class UserClient {
    public User getUser(String id) {
        System.out.println("GET /users/" + id);
        return new User("standard_user");
    }
}
```

**tests/CheckoutApiTest.java** idea:

```java
package com.company.project.tests;

import com.company.project.api.UserClient;
import com.company.project.models.User;
import com.company.project.utils.StatusCodes;

public class CheckoutApiTest {
    public static void main(String[] args) {
        UserClient client = new UserClient();
        User user = client.getUser("1");
        int status = 200;
        System.out.println(user.getUsername());
        System.out.println("OK? " + StatusCodes.isSuccess(status));
    }
}
```

Tests depend on api, models, utils. Utils do not depend on tests.

## 9. Break the Code

Package line that does not match the folder:

```java
package com.company.project.pages;
// file actually sits in models/
```

IntelliJ: "Package name does not correspond to the file path."

Forgetting `public` on `User` then importing from tests:

```java
package com.company.project.models;

class User { } // package-private
```

`LoginDemo` in `tests` cannot use `User`.

Importing tests from pages (inverted dependency):

```java
package com.company.project.pages;

import com.company.project.tests.LoginDemo; // pages should not know tests
```

Using the default package for `User`, then trying to import it from a named package — pain. Put `User` in `models`.

Naming a package `package` or `Pages` (capital P) — illegal or against convention.

## 10. Debug

`package ... does not match` — move the file in IntelliJ with drag-and-drop so the IDE rewrites the package line, or fix both to agree.

`cannot find symbol: class User` — missing import, or wrong package, or class is not public.

`User is not public in com.company.project.models; cannot be accessed from outside package` — add `public` to the class (if it should be shared) or move the caller into that package (usually wrong for tests).

Cyclic imports (pages import tests import pages) — fix the traffic rules. Tests at the top of the dependency arrow.

If two `User` types confuse you, hover in IntelliJ. Expand the import. Delete unused imports.

## 11. Student Exercise

In IntelliJ, create packages (even in `java-learning`):

- `com.company.project.models`
- `com.company.project.pages`
- `com.company.project.tests`

Put `User` in models, `LoginPage` in pages, a small class with `main` in tests. Import correctly. Run `main`.

Draw the folder tree on paper including `api` and `utils` even if you leave those empty for now.

## 12. Challenge

Add:

- `com.company.project.api.UserClient` that returns a `User`
- `com.company.project.utils.WaitTimes` with `public static final int SHORT = 5;`
- a test class that uses **UserClient**, **LoginPage**, **User**, and **WaitTimes**

Then violate a rule on purpose in a *comment* (do not leave it compiling): pages should not import tests. Write why that inversion hurts.

## 13. Knowledge Check

1. What is a package?
2. Where does the `package` statement go?
3. How does the folder relate to the package name?
4. What does `import` do?
5. Do classes in the same package need to import each other?
6. Why use reverse-domain style (`com.company.project`)?
7. List the five SDET packages from this lesson.
8. True or false: `java.lang` requires an import.
9. Why should `User` be `public` if tests live in another package?
10. Why should `utils` not depend on `pages`?

## 14. Interview Question

**Question:** What are packages in Java, and how would you structure an automation project?

A strong answer:

> A package is a namespace and a folder for related types. The first line of the file is package com.company.project.models, and the folder path must match. Other packages import the public types they need. I structure SDET code as com.company.project with pages, api, models, utils, and tests. Tests depend on pages and api; models are plain data; utils stay generic. Packages also control package-private access, so I do not make everything public by accident. Unique prefixes avoid class name clashes like two User types.

## 15. Homework

Create the five packages in your learning project (they may contain one class each).

Write `HomeworkPackages` notes (markdown or comments) mapping:

```text
LoginPage     → pages
User          → models
UserClient    → api
StatusCodes   → utils
LoginTest     → tests
```

Type at least the three-file simple example so you feel IntelliJ's package refactor.

On the next part we collect *many* values again — this time with Lists, Sets, Maps, and Queues.

---

## Answer Key

1. A named namespace/folder grouping related classes.
2. First statement in the file (after comments).
3. Dots become nested folders: `com.company.project.models` → `com/company/project/models`.
4. It lets you use the short class name instead of the fully qualified name. It does not copy source.
5. No.
6. Globally unique names, usually matching an organization domain.
7. `pages`, `api`, `models`, `utils`, `tests`.
8. False. It is imported automatically.
9. Tests are a different package; they can only see public types (and public members).
10. Utils should stay reusable and independent; pages are product-specific UI. A cycle or inverted dependency makes simple tools depend on browsers and locators.
