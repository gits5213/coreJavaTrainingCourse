# Advanced Java Interview

Questions from Parts 20–31 (generics, lambdas, streams, Optional, date-time, regex, records, JVM memory, concurrency). Speak 60–90 seconds unless the question is a design follow-up.

---

### What are generics, and why `List<User>` not a raw `List`?

**Model answer:** A generic type takes a type parameter: `List<User>`, `ApiResponse<Order>`. Without generics, everything is `Object` and I cast. Casts fail at runtime. Generics move many mistakes to compile time. API wrappers return `ApiResponse<User>` from one endpoint and `ApiResponse<Order>` from another — same envelope, different payload. I use wildcards when I must; I do not sprinkle `?` to look advanced.

---

### What is a lambda?

**Model answer:** A short way to write a function I can pass around: given this input, do that. `item -> System.out.println(item)` or `r -> r.failed()`. A lambda implements a functional interface — an interface with one abstract method. I still learned methods and interfaces first; a lambda is not a replacement for that. Selenium waits often take a lambda: `wait.until(d -> d.getTitle().contains("Dash"))`.

---

### What is the Stream API?

**Model answer:** A stream is a pipeline over data, not a new collection: collection → stream → filter → map → collect. Example: `results.stream().filter(r -> "FAIL".equalsIgnoreCase(r.status())).map(TestResult::name).toList()`. I use streams when they read clearer than a loop, not to show off on `add(int, int)`. A 12-step stream nobody can debug is not clever. `reduce` can sum durations; I can return to that chapter if it still feels abstract, but interviews do ask it.

---

### What is `Optional`? When do you not use it?

**Model answer:** `Optional<User>` is a box that may be empty. It makes "no result" visible as a return type: `findUser(id)`, stream `.max()`. I do not use Optional as a field on every class, I do not wrap every parameter in `Optional.ofNullable`, and I do not replace every `null` with Optional for fashion. Empty vs present is the point.

---

### `java.time` — which types, and what do you avoid?

**Model answer:** `LocalDate` is a date, `LocalTime` a time of day, `LocalDateTime` date and time without a zone, `Instant` a moment on the timeline, `Duration` an amount of time. I do not start new code with `java.util.Date` and `Calendar`. `LocalDate.now()` depends on the machine clock; tests that freeze "today" should inject a date. Tokens expire; reports need timestamps. Sleeping two seconds is a `Duration`, but UI tests should wait on conditions, not on a wall clock.

---

### Regular expressions in Java?

**Model answer:** Regex is a tiny language for text patterns, hosted by Java via `String.matches`, `Pattern`, and `Matcher`. In a Java string I double backslashes: `"\\d{5}"` for five digits. I use regex for ids, logs, and dynamic text. If `startsWith` and `length() == 5` will do, I use them. I do not claim a short email regex implements the whole RFC.

---

### What is a record? Why for test data?

**Model answer:** A record is a concise immutable data carrier: `record LoginData(String username, String password) {}`. Java generates the constructor, accessors `username()` / `password()`, `equals`, `hashCode`, and `toString`. Test data should not change mid-run. I override `toString` to mask secrets. Records are classes with opinionated defaults, not a reason to skip OOP. A compact constructor can still validate.

---

### Stack versus heap?

**Model answer:** Each method call pushes a stack frame: locals, primitives, references. Objects created with `new` live on the heap. The variable `User user` on the stack holds a reference; the `User` object is on the heap. Each thread has its own stack. The heap is shared. Stack overflow is too much depth; `OutOfMemoryError` is heap (or native) exhaustion. `WebDriver` objects live on the heap; leftover browsers are also a native resource problem.

---

### Garbage collection?

**Model answer:** The JVM reclaims heap objects that are no longer reachable. Life cycle: created → used → unreachable → eligible. I do not call `System.gc()` as a test strategy. GC does **not** close files or Chrome. If a suite opens browsers and never `quit()`, I can leak processes and memory. `quit()` is required.

---

### What does the JIT compiler do?

**Model answer:** The JVM first interprets bytecode. The JIT watches what is hot and compiles that to optimized machine code. Long-running JVMs speed up. "Java is slow" is often a cold JVM or quadratic code, not "bytecode cannot be fast."

---

### Process versus thread? Concurrency versus parallelism?

**Model answer:** A process is a running program — one JVM. Threads are workers inside it. Concurrency means many tasks in progress, interleaved; one CPU can juggle them. Parallelism means many tasks at the same instant on many cores. Virtual threads help lots of waiting (I/O). They are not a magic "use all cores for CPU math" button.

---

### What is a race condition?

**Model answer:** The result depends on timing. Two threads increment the same `int` and lose updates. Two tests sharing one `WebDriver`: who clicked last wins. The fix is isolation and safe sharing, not a longer sleep hoping to win the race. Flaky tests are often races.

---

### `synchronized`, ExecutorService, virtual threads — what is the SDET takeaway?

**Model answer:** `synchronized` can protect a critical section; it is easy to overuse and deadlock. I prefer thread pools (`ExecutorService`) over hundreds of `new Thread`. For tests, I prefer framework-managed parallelism (Surefire, TestNG parallel) over homemade threads. I learn races so I can recognize them. Shared mutable state is the danger. Immutable config is easier to share.

---

### Thread safety and immutability?

**Model answer:** Thread-safe means correct under concurrent access. Immutable objects (`record`, `final` fields) are easier to share. A live browser session is mutable and must not be shared. `ThreadLocal<WebDriver>` is per-thread isolation; it does not mean ChromeDriver is thread-safe. I still `quit` and `remove`.

---

### How would you process a list of test results? (whiteboard)

**Model answer:** `record TestResult(String name, String status, int durationMs)`. `List<TestResult>` in, streams to filter `FAIL` and map names, `average()` of pass durations with `orElseThrow` if none passed. No raw lists, no empty catch, no `==` on status strings.

---

### Annotations and reflection (if asked from nearby parts)?

**Model answer:** Annotations like `@Test` and `@Override` are metadata the compiler or runtime uses. Reflection can inspect classes at runtime; frameworks use it. I do not use reflection to poke private test fields as a daily habit. Sealed classes restrict who may extend a type; I can mention them without forcing them into every design.

---

## Extra practice

- Explain ThreadLocal vs singleton driver using heap + race language.
- Write a four-line stream that lists failed test names.
- When is Optional the wrong tool?
