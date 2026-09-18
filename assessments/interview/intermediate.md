# Intermediate Interview

Questions from Parts 12–19 (OOP, packages, collections, wrappers, enums, exceptions, files, JSON). Speak 60–90 seconds. Check the model after.

---

### What is OOP?

**Model answer:** Object-oriented programming organizes code as objects that combine data and behavior. A class is the blueprint; an object is an instance. I model a `User`, a `LoginPage`, an `Order`. The working ideas are encapsulation, inheritance, polymorphism, and abstraction. In SDET work I also use composition constantly: a page *has* a driver.

---

### Class versus object?

**Model answer:** `class LoginPage` is the blueprint. `new LoginPage(driver)` is one page object for one browser session. Two objects from one class can hold different field values. They do not share instance fields unless those fields are `static`.

---

### What is encapsulation?

**Model answer:** I keep fields private and let callers use methods. That way a `User` cannot be left with a blank username just because someone wrote `user.username = ""`. Getters read; setters change with rules. Page objects encapsulate locators: tests call `enterUsername`, they do not scatter `By.id` in every class.

---

### What is inheritance? When is it the wrong tool?

**Model answer:** Inheritance is an *is-a* relationship: `ChromeDriver` is a `WebDriver` implementation; `LoginPage extends BasePage` if that is honest. It is easy to misuse for reuse — a tower of `BaseTest` classes. If the relationship is *has-a*, I use composition instead.

---

### What is polymorphism?

**Model answer:** I talk to a general type and plug in a specific type. `WebDriver driver = factory.create(type);` then `driver.get(url)` works for Chrome or Firefox. Tests depend on the interface, not on `ChromeDriver` methods scattered everywhere.

---

### What is abstraction?

**Model answer:** I say *what* must happen and let each type decide *how*. `PaymentService.pay()` might be card or wallet underneath. Interfaces and abstract classes are how Java writes that down. I should still be able to explain the idea without the keyword `abstract`.

---

### Interface versus abstract class?

**Model answer:** An interface is a contract of methods. `WebDriver` is an interface; a class `implements` it. An abstract class is a partial implementation; I cannot `new` it. I use it when subclasses share code *and* identity. For a pure contract in modern Java I prefer an interface. I do not make `AbstractEverything`.

---

### What is composition?

**Model answer:** *Has-a*. `LoginPage` has a `WebDriver`. `CheckoutPage` has a `PaymentComponent`. `TestClass` has a `LoginPage`. Composition is usually clearer than inheriting eight base classes. SOLID and page objects both push me here.

---

### What are `this`, `static`, and `final`?

**Model answer:** `this` is the current object; it distinguishes a field from a parameter with the same name. `static` belongs to the class, not an instance — counters, factories, `statusMatches`. Making *everything* static is a trap; I then have no objects to isolate. `final` on a variable means it cannot be reassigned; on a method it cannot be overridden; on a class it cannot be extended. Those are three different jobs.

---

### Access modifiers?

**Model answer:** `private` — this class only. Default (package) — the package. `protected` — package plus subclasses. `public` — anyone. I start private and open up on purpose. Public fields skip encapsulation.

---

### What is a package?

**Model answer:** A named folder for related types: `com.company.project.pages`, `.api`, `.models`, `.tests`. Without packages, names collide and the repo becomes a junk drawer. Tests call pages and API clients; pages and clients use models; utils are shared tools. That tree is already architecture.

---

### List versus Set versus Map versus Queue?

**Model answer:** List: ordered, duplicates allowed — steps, users in a file. Set: unique — ids I already saw. Map: key to value — headers, JSON-like fields, config. Queue: processing order — jobs. Map is not a Collection; it is a sibling idea and very important for API work. I pick on purpose, not "ArrayList for everything."

---

### What is a HashMap in SDET work?

**Model answer:** `Map<String, String> headers` then `get("Authorization")`. Average fast lookup. Keys need `equals` and `hashCode`. I do not use a Map as a fake object when a `LoginData` record or class exists — named fields are clearer and safer.

---

### Wrapper classes and autoboxing?

**Model answer:** Collections hold objects, not primitives. `int` becomes `Integer`, `boolean` becomes `Boolean`. Autoboxing converts primitive to wrapper; unboxing goes back. That is how `List<Integer>` of status codes works. `List<int>` is illegal.

---

### Why enums?

**Model answer:** An enum is a fixed set of named values: `BrowserType.CHROME`, `FIREFOX`, `EDGE`. `String browser = "Chorme"` compiles and fails later. Enums catch that at compile time. I use them for browsers, environments, and test status — not for unbounded text like a user's comment.

---

### What is an exception?

**Model answer:** An error object thrown when a path cannot continue honestly: `NumberFormatException`, `IOException`, later `NoSuchElementException`. Unexpected exceptions escaping a test should fail the test. My job is to notice, report, and fail — not to hide.

---

### Checked versus unchecked?

**Model answer:** Checked exceptions (`IOException`) must be declared or caught; the compiler enforces that. Unchecked (`RuntimeException`, `NullPointerException`, most Selenium exceptions) do not have to be declared. I do not swallow either with an empty `catch`. `Error` means the JVM is in serious trouble; I almost never catch it.

---

### `try`, `catch`, `finally`, `throw`, `throws`?

**Model answer:** `try` is the risky work. `catch` handles a *specific* type I can handle — and in a test I still fail if the product is wrong. `finally` (or try-with-resources) cleans up either way: files, connections, `driver.quit()`. `throw` creates an exception on purpose (validation). `throws` on a method declares a checked exception for the caller.

---

### Why is empty `catch (Exception e) {}` forbidden?

**Model answer:** The suite stays green while the product is broken. That catch is a liar. If I catch, I log, and I fail the test — or I let the exception escape.

---

### How do you read a file in modern Java?

**Model answer:** `Files.readString(Path.of("data.txt"))` and `Files.writeString` for reports. I join with `Path.of("tests", "data.txt")` instead of hardcoding `C:\\`. Missing files throw `IOException`. Relative paths depend on the working directory; printing `toAbsolutePath()` saves time. I do not invent empty users when the file is missing.

---

### What is JSON, and how does Java handle it?

**Model answer:** JSON is a data format APIs send as text, not a Java class. The JDK does not give me `json.username` with dots. Teams use a library; this course uses Jackson `ObjectMapper` to map JSON to a `User`. Status 200 is not enough — I must assert fields. I do not parse JSON with `split` as a career strategy.

---

### Constructor and `this` — why both?

**Model answer:** The constructor gives the object its starting values and is the right place for validation. `this.username = username` assigns the field, not the parameter. Without `this`, a parameter can shadow the field and the assignment does nothing useful.

---

## Extra practice

- Draw class (blueprint) with two objects underneath.
- Explain why public fields make constructor validation useless.
- Pick List/Set/Map for browsers, unique failed test names, and HTTP headers in 15 seconds.
