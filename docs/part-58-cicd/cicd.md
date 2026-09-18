# CI/CD for Java SDET

## Goal

By the end of this lesson, you will draw Developer → Git Push → GitHub → CI → Compile → Unit Tests → Automation → Report, design a **PR pipeline** (compile, quality, unit, smoke, merge) and a **nightly** (regression + cross browser + API), and write a small GitHub Actions workflow that runs `mvn test`.

## Why It Matters

Without CI, people forget to run tests. `main` rots. With CI, a red PR is a conversation. SDET value is often the pipeline, not a new locator.

Architect interviews: "What runs on PR vs nightly?" If everything is a 4-hour UI suite on every commit, engineers will skip the gate.

## Real-Life Analogy

A factory quality line.

```text
Worker finishes a part     git push
Inspector at the door      PR CI: fast checks
Night audit of the plant   nightly regression
Report on the wall         Allure / GitHub checks
```

You do not do a full fire drill for every box that leaves a workstation. You do a smoke. You do the full drill at night.

## Illustrated Explanation

Main flow:

```text
Developer
 ↓
Git Push
 ↓
GitHub
 ↓
CI
 ↓
Compile
 ↓
Unit Tests
 ↓
Automation
 ↓
Report
```

PR (curriculum):

```text
Pull Request
     ↓
Compile
     ↓
Code Quality
     ↓
Unit Tests
     ↓
Smoke Tests
     ↓
Merge
```

Nightly:

```text
Regression
+
Cross Browser
+
API
```

```text
PR      minutes     compile + unit + tiny smoke
Nightly hours       full UI + browsers + API + maybe DB
```

CD in this course: merging to `main` may deploy the *application* elsewhere. Automation still runs as a gate. Do not deploy if unit tests fail.

## Syntax / Concept — GitHub Actions

`.github/workflows/pr.yml`:

```yaml
name: pr-checks

on:
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: "25"
          cache: maven
      - name: Compile and unit tests
        run: mvn -B -q test
```

Later split: `mvn -B -q compile` then `mvn -B -q -Dtest=Unit* test` then smoke profile.

Secrets: GitHub Actions secrets, not in yaml.

Quality: Spotless/Checkstyle/SpotBugs as a step. Keep it reasonable (KISS).

Fail the job if tests fail. Do not `|| true`.

## Simple Example

A workflow that only compiles:

```yaml
- name: Compile
  run: mvn -B -q -DskipTests compile
```

Then add tests. Skipping tests in CI as a habit is how `main` dies.

## Real-World Example

Banking:

```text
PR      unit + API contract smoke + one UI login
Nightly full UI Chrome/Firefox/Edge + API regression + a few DB checks
```

E-commerce Black Friday: extra load is not this pipeline; still keep the PR gate fast.

## SDET Example

```text
mvn test                    unit + fast API
mvn test -Psmoke            tagged UI
mvn test -Pregression       nightly
```

TestNG groups or JUnit tags map to profiles. The pipeline calls profiles. Humans do not SSH to click IntelliJ.

Publish Allure as an artifact. Keep artifacts free of secrets.

## Break the Code

```yaml
run: mvn test || true
```

Always green. Useless.

```yaml
on: push
# 4-hour regression on every commit to a feature branch
```

People stop pushing.

Hardcoded password in yaml.

Using `ubuntu-latest` with headed Chrome and no Grid/headless: job fails. Headless or Grid.

## Debug

| Symptom | Check |
| --- | --- |
| works laptop, fails CI | JDK version, headless, missing secrets, timezones |
| Maven cannot download | network, settings.xml |
| tests skipped | wrong directory, `-DskipTests` left on |
| flake only on CI | waits, parallel isolation, shared QA data |

Read the **first failed step**. Download logs. Do not rerun 15 times as a strategy.

## Student Exercise

Add `.github/workflows/pr.yml` that checks out, sets up JDK 25, runs `mvn -B test`. Open a PR and watch the check. If you cannot use GitHub yet, write the YAML in the repo anyway.

## Challenge

Document a second workflow `nightly.yml` on `schedule` (cron) that would run regression. You may leave it disabled. Write which Maven profile it would call and why it is not on every PR.

## Knowledge Check

1. Draw Developer → ... → Report.
2. List PR stages from the curriculum.
3. What is nightly for?
4. Why not full regression on every PR?
5. What does GitHub Actions run in this course's simple job?
6. Where do secrets live?
7. Why is `|| true` a crime?
8. CI vs CD in one sentence each?
9. What should block merge?
10. How do reports connect?

## Interview Question

**Question:** How would you design CI for an automation suite?

A strong answer:

> Developer pushes to GitHub. CI compiles, runs code quality, unit tests, then smoke tests on the pull request. Merge is blocked if those fail. Nightly runs regression, cross-browser, and API tests. That is Developer → Git Push → GitHub → CI → Compile → Unit Tests → Automation → Report. PR must stay fast. I use GitHub Actions with JDK 25 and Maven. Secrets stay in the vault. I publish reports. I do not run a four-hour UI suite on every commit.

## Homework

Green PR workflow for unit tests. README badge optional. Commit `Add GitHub Actions PR workflow for mvn test`.

---

## Answer Key

1. Developer → Git Push → GitHub → CI → Compile → Unit Tests → Automation → Report
2. Compile → Code Quality → Unit Tests → Smoke Tests → Merge
3. Regression + cross browser + API
4. Feedback too slow; people bypass
5. checkout, JDK, mvn test
6. GitHub secrets / vault
7. Hides failure
8. CI: verify every change; CD: deploy
9. PR gates: compile, quality, unit, smoke
10. CI publishes evidence from Part 57
