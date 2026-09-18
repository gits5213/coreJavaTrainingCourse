# Chapter 77 — Regular Expressions

## 1. Today's Goal

By the end of this lesson, you will treat regex as its **own language**, then embed it in Java strings.

You will match a 5-digit id:

```java
Pattern.matches("\\d{5}", "12345");  // true
Pattern.matches("\\d{5}", "abcde");  // false
```

You will know why Java source writes `\\d` to mean regex `\d`.

You will think about IDs, logs, emails, phones, dynamic text, and file names as pattern problems.

## 2. Why It Matters

UIs show `"Order #18372 confirmed"`. The number changes. `equals` on the whole string fails every run. You need: digits in a known place.

Logs: `"ERROR 2026-09-18 ..."`. File names: `"report-001.txt"`. Test data: US ZIP `12345`.

SDET assertions on **dynamic** text are regex or extract-then-assert. If you only `contains("1")`, everything matches.

## 3. Real-Life Analogy

A stencil.

```text
Stencil:  five square holes for digits
Paper:    12345 fits
          hello does not
```

Regex is the stencil. The string is the paper.

A lost-and-found description: "a 5-digit locker code" — not "exactly 38471" if you forgot the code but know the shape.

A fishing net with hole size: `\d` catches digits. Java is the boat. The net pattern is not the boat.

**Two languages:**

```text
Regex  \d{5}
Java   "\\d{5}"   because \ in a Java string starts an escape
```

In a regex tester website you type `\d{5}`. In `.java` files you type `"\\d{5}"`.

## 4. Illustrated Explanation

```text
Regex:   \d {5}
Meaning: digit, five times

\d \d \d \d \d
 1  2  3  4  5     matches 12345
 a  b  c  d  e     fails
```

```text
Common pieces (regex language)

.        any one char (careful)
\d       digit 0-9
\w       letter, digit, or _
\s       whitespace
{5}      exactly five of the previous
{2,4}    two to four
[A-Z]    one uppercase letter
^ $      start / end (often implicit in matches() on the whole string)
+        one or more
*        zero or more
?        optional
```

Java embedding:

```text
Regex wants:  \d
Java string:  "\\d"
Because:      "\d" is not a valid simple Java escape
              \\  →  one \  in the actual regex
```

```text
"Order #18372 confirmed"
        │
        ▼
Pattern.compile("Order #\\d+ confirmed")
        │
        ▼
matcher.matches()  or  text.matches("Order #\\d+ confirmed")
```

Emails/phones: simple patterns for tests (`^\\S+@\\S+\\.\\S+$` is weak but teaching-sized). Do not ship a 200-character email regex you copied and cannot read.

## 5. Syntax / Concept

```java
import java.util.regex.Pattern;
import java.util.regex.Matcher;
```

Whole-string match:

```java
boolean zip = Pattern.matches("\\d{5}", "12345");
boolean also = "12345".matches("\\d{5}");  // String.matches = whole string
```

`String.matches` is convenient. For loops, **compile once**:

```java
Pattern zip = Pattern.compile("\\d{5}");
boolean ok = zip.matcher("12345").matches();
```

Find inside a longer string:

```java
Matcher m = Pattern.compile("\\d{5}").matcher("id=12345 end");
if (m.find()) {
    System.out.println(m.group()); // 12345
}
```

`matches()` vs `find()`: whole vs substring. Beginners fail here.

In Java source, every regex backslash is doubled. A literal backslash in regex is `\\\\` in Java. Painful. Prefer `\d` classes over literal `\` when possible.

Invalid regex throws `PatternSyntaxException` at compile of the pattern (runtime). Catch specifically if the pattern comes from config. If **you** wrote a broken pattern, fix it; do not empty-catch.

## 6. Simple Example

```java
import java.util.regex.Pattern;

public class RegexZipDemo {

    public static void main(String[] args) {
        System.out.println(Pattern.matches("\\d{5}", "12345"));
        System.out.println(Pattern.matches("\\d{5}", "1234"));
        System.out.println(Pattern.matches("\\d{5}", "123456"));
        System.out.println(Pattern.matches("\\d{5}", "abcde"));
    }
}
```

Expected:

```text
true
false
false
false
```

`{5}` means exactly five, not "at least" and not "contains five."

## 7. Real-World Example

Shop order id `A` plus 5 digits: `"A\\d{5}"` for `A18372`.

Bank last four: `"\\d{4}"` on a field that should only be four digits.

Phone teaching pattern (US 10 digits, no formatting): `"\\d{10}"`. Real phones have dashes. Decide if you strip non-digits first (`replaceAll("\\D", "")`) then match. Two steps often beat one heroic regex.

## 8. SDET Example

```java
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexSdetDemo {

    public static void main(String[] args) {
        String banner = "Order #18372 confirmed";
        Pattern p = Pattern.compile("Order #(\\d+) confirmed");
        Matcher m = p.matcher(banner);
        if (!m.matches()) {
            throw new AssertionError("TEST FAILED — banner shape: " + banner);
        }
        String id = m.group(1);
        System.out.println("Extracted id: " + id);
        if (!id.matches("\\d{5}")) {
            throw new AssertionError("TEST FAILED — id not 5 digits: " + id);
        }
        System.out.println("TEST PASSED");

        String file = "report-001.txt";
        if (!file.matches("report-\\d{3}\\.txt")) {
            throw new AssertionError("TEST FAILED — file name " + file);
        }
        System.out.println("File name OK");
    }
}
```

Note `\\.` in Java for a regex literal dot. Regex `.` means any character. File names need `\\.txt`.

Log line starts with ERROR:

```java
log.matches("ERROR .*")  // whole string
log.startsWith("ERROR")  // often enough — simpler
```

Choose simple.

Email (humble):

```java
Pattern.matches("[^@]+@[^@]+\\.[^@]+", email)
```

This rejects many bad strings and also rejects some real-world emails. Document that it is a **smoke** check.

## 9. Break the Code

```java
Pattern.matches("\d{5}", "12345"); // Java compile error: illegal escape
```

Must be `"\\d{5}"`.

```java
"id=12345".matches("\\d{5}"); // false — matches() is the WHOLE string
```

Use `find()` or `.*\\d{5}.*` if you must (the `.*` is greedy and sloppy). Prefer `find()`.

```java
file.matches("report-\\d{3}.txt")  // the . matches ANY char: report-001Xtxt would pass
```

Forgot to escape the dot.

Empty catch of `PatternSyntaxException` then `matches` everything as true: all file names pass. Forbidden.

## 10. Debug

False when you expected true:

1. Test the regex in a regex playground **as regex**, not as Java.
2. Put it in Java with doubled backslashes.
3. `matches` vs `find`.
4. Print the string with brackets `[" + s + "]"` to see spaces.
5. Remember `{5}` is exact.

IntelliJ: check regex language injection if you use certain comments; optional.

```text
Step 1: what should the pattern mean in regex?
Step 2: how is that spelled in a Java string?
Do not mix the steps.
```

## 11. Student Exercise

Assert with prints (boolean):

- `"12345"` vs `\\d{5}`
- `"1234"` vs `\\d{5}`
- `"hello@shop.com"` vs a humble email pattern you write
- `"report-001.txt"` vs `report-\\d{3}\\.txt`

## 12. Challenge

Extract all 5-digit numbers from `"ids 11111 and 22222 done"` using `Matcher.find()` in a loop. Collect them. Assert two ids.

Then a banner `"Order #12 confirmed"` should TEST FAILED because id is not 5 digits (define your product rule).

No empty catch.

## 13. Knowledge Check

1. Is regex a Java feature or a separate pattern language hosted by Java?
2. Why `"\\d{5}"` not `"\d{5}"` in Java source?
3. What does `\d` mean in regex?
4. What does `{5}` mean?
5. `String.matches` — whole string or substring?
6. Name four SDET places regex appears (from the lesson).
7. Why escape `.` as `\\.` for `.txt`?
8. True or false: a short email regex proves RFC-complete validation.
9. `matches("\\d{5}")` on `"id=12345"`?
10. Empty-catch invalid pattern and pass tests: allowed?

## 14. Interview Question

**Question:** How do regular expressions work in Java, and when do testers use them?

A strong answer:

> Regex is a pattern language. Java uses Pattern and Matcher, or String.matches. In regex, \\d{5} means five digits, but in a Java string I write "\\d{5}" because backslash is an escape in Java. String.matches checks the whole string; Matcher.find finds a substring. Testers use regex for ids, logs, emails, phones, dynamic UI text, and file names. I keep patterns small. I do not empty-catch PatternSyntaxException. If startsWith is enough, I use that.

## 15. Homework

Write `isZip5(String s)` using `Pattern.matches("\\d{5}", s)`.

Write `isReportFile(String name)` for `report-` + 3 digits + `.txt`.

Test true and false cases. List five real strings from a project (invent them) you would **not** use regex for, because `equals` is enough.

Notes: two columns, Regex vs Java string, for `\d+` and `\\.txt`.

---

## Answer Key

1. A separate pattern language; Java provides `Pattern`/`Matcher`/`matches`.
2. Java strings use `\` for escapes; `\\` produces one `\` for regex.
3. A digit.
4. Exactly five of the previous token.
5. Whole string.
6. IDs, logs, emails, phones, dynamic text, file names (any four+).
7. Unescaped `.` means any character.
8. False.
9. False (`matches` is the entire string).
10. No.
