# SDET Java Lessons

Runnable beginner examples that match the course chapters. This Maven module is **lessons**, not a Selenium or API project.

- **Group:** `com.sdet`
- **Artifact:** `java-lessons`
- **Version:** `1.0.0`
- **Java:** examples compile with **JDK 17+**. The classroom baseline is **JDK 25 LTS**.

Do not add Selenium, REST Assured, or network calls here. Those belong in later projects.

## Open in IntelliJ

1. **File → Open** and choose `lessons/pom.xml` (or open the whole course repo and let IntelliJ import this Maven module).
2. Trust the project if IntelliJ asks, and wait until Maven finishes indexing.
3. Confirm **Project SDK** is JDK 17 or newer (**File → Project Structure → Project**). Classroom machines should use JDK 25.
4. In the Project tool window, open a class, for example:

   `src/main/java/com/sdet/lessons/chapter15/HelloWorld.java`

5. Click the green **Run** triangle next to `public static void main`, or right-click the class → **Run 'HelloWorld.main()'**.
6. Read the output in the **Run** tool window at the bottom.

To run a different lesson, open that class and run its `main` the same way.

## Run with Maven

From the course repository root:

```bash
mvn -f lessons/pom.xml -q exec:java -Dexec.mainClass=com.sdet.lessons.chapter15.HelloWorld
```

Other examples (same pattern, change the main class):

```bash
mvn -f lessons/pom.xml -q exec:java -Dexec.mainClass=com.sdet.lessons.chapter25.LogicalOperatorsDemo
mvn -f lessons/pom.xml -q exec:java -Dexec.mainClass=com.sdet.lessons.chapter28.TestResultEvaluator
mvn -f lessons/pom.xml -q exec:java -Dexec.mainClass=com.sdet.lessons.chapter67.ReadFileDemo
mvn -f lessons/pom.xml -q exec:java -Dexec.mainClass=com.sdet.lessons.wrappers.WrapperDemo
```

Compile everything first if you want a syntax check:

```bash
mvn -f lessons/pom.xml -q compile
```

`WriteFileDemo` writes under `lessons/output/`. That folder is gitignored. Do not commit generated files. Maven `target/` is also gitignored.

## Package map

Each chapter lives in `com.sdet.lessons.chapterXX`. Wrapper-class examples live in `com.sdet.lessons.wrappers`.
