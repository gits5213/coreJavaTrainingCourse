# Reporting

## Goal

By the end of this lesson, you will describe the execution → results → evidence → report pipeline, list the evidence fields a professional report needs, and know how Allure fits — without treating Allure as architecture.

## Why It Matters

CI is red. A product manager asks "is checkout broken?" If your report says `test17 failed`, you have not done the job. If it says expected vs actual, screenshot, browser, env, time, exception, they can decide.

Reports are communication. They are also leak surfaces (screenshots of account numbers).

## Real-Life Analogy

A lab report.

```text
Execution     we ran the experiment
Results       pass/fail counts
Evidence      photos, instrument readings
Report        the PDF someone else can read
```

A photo of the lab bench that includes a patient's chart is a HIPAA/privacy incident. Crop your screenshots; use test accounts.

## Illustrated Explanation

```text
Test Execution
      ↓
Results
      ↓
Evidence
      ↓
Report
```

Evidence (curriculum):

```text
screenshot
exception
browser
environment
timestamps
```

Also useful: test name, thread, URL, status code, Grid session id, git commit SHA.

```text
Allure
  listeners capture steps
  attachments: png, txt
  history across runs (if stored)
```

Allure is not required to *understand* reporting. It is a common SDET tool. Surefire XML is a report too, just ugly.

## Syntax / Concept

On failure, a TestNG listener:

```java
public class ScreenshotOnFailure implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = DriverManager.get();
        File shot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // attach to Allure or copy to target/evidence/
        log.error("Failed {} at {}", result.getName(), Instant.now());
    }
}
```

Allure (sketch):

```java
Allure.addAttachment("screenshot", new FileInputStream(shot));
Allure.step("Logged in as standard_user");
```

Do not Allure.step the password.

Timestamps: `Instant.now()` in UTC in logs. Humans in local time in HTML if the tool supports it.

Environment: `qa`, `chrome`, `grid-us-east`, `git sha`.

## Simple Example

Without Allure, write a tiny evidence file:

```java
public class Evidence {
    public static void write(String testName, String message) throws Exception {
        Path dir = Path.of("target", "evidence");
        Files.createDirectories(dir);
        Files.writeString(
                dir.resolve(testName + ".txt"),
                Instant.now() + "\n" + message
        );
    }
}
```

This teaches the pipeline. Allure is a nicer viewer of the same idea.

## Real-World Example

Banking nightly: Allure published to an internal site. Failed transfer test includes screenshot of confirmation, exception, Chrome 125, env=qa, 02:14 UTC. No account numbers in the shot because the test used a masked UI or test account.

## SDET Example

```text
Execution     mvn test / GitHub Actions
Results       480 passed, 3 failed, 1 skipped
Evidence      3 pngs + stack traces
Report        Allure HTML
```

Flakes: if the report shows different exceptions each night, you have isolation or wait bugs, not a product bug. Reports help you classify.

## Break the Code

```java
Allure.addAttachment("response", fullHttpDumpWithToken);
```

```java
screenshot of production user PII
```

```java
report only in target/ and never published — CI "failed" with zero evidence for humans
```

## Debug

Missing screenshots: listener not registered in testng.xml; driver already quit before listener (quit *after* screenshot in AfterMethod order — capture then quit).

Empty Allure: results directory not archived in CI.

## Student Exercise

On test failure, write `target/evidence/<test>.txt` with timestamp, test name, exception message, browser enum, env string. Do a screenshot if you have a driver. No Allure required.

## Challenge

Add Allure TestNG adapter to Maven (look up current artifact). Attach a screenshot on failure. Document the `mvn` command to generate the report. Confirm no secrets in attachments.

## Knowledge Check

1. Recite execution → ... → report.
2. List five evidence types from the curriculum.
3. Name a reporting tool from the curriculum.
4. When do you take a screenshot?
5. Why quit after screenshot?
6. What extra fields help debugging?
7. Why are reports a security topic?
8. Is Surefire a report?
9. What should a PM see in a failure?
10. Can Allure replace logging?

## Interview Question

**Question:** What do you include in an automation report?

A strong answer:

> Execution produces results, then evidence, then a report. Evidence includes screenshot, exception, browser, environment, and timestamps. I also want test name, URL, and git SHA. Allure is a common HTML report with attachments. I capture screenshots on failure before quit. I never attach tokens or PII. A report should let a human decide if the product broke. Pretty graphs without stack traces are not enough.

## Homework

Produce at least one failure evidence file in `target/evidence`. Add `target/evidence/` to gitignore if it contains screenshots. Commit the listener code, not the PNGs of secrets.

---

## Answer Key

1. Test Execution → Results → Evidence → Report
2. screenshot, exception, browser, environment, timestamps
3. Allure
4. Typically on failure (sometimes on demand)
5. Listener needs a live session
6. URL, SHA, thread, session id, status code
7. Attachments leak
8. Yes, XML/HTML
9. What failed, expected/actual, env, evidence
10. No — complementary
