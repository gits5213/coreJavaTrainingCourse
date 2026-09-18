# Project 17 — CI/CD Framework

## Goal

Put quality gates on every pull request: **compile → unit → smoke**. Tests that only run on a laptop are a hobby. The GitHub Actions workflow lives at the **repository root** (`.github/workflows/ci.yml`), not only inside this folder.

## Concepts this practices

```text
Developer
 ↓
Git Push / Pull Request
 ↓
GitHub Actions
 ↓
Compile
 ↓
Unit Tests
 ↓
Smoke
```

This module is the small, always-green piece of that pipeline: a `Calculator` (real production code) plus a smoke test that always passes. Heavier Selenium jobs stay opt-in and out of the default PR workflow.

## How to run

Locally (same commands CI uses for this module):

```bash
mvn test
```

From the repository root, a subset that does not need a browser or the public internet:

```bash
mvn -f lessons/pom.xml -q test compile
mvn -f projects/10-junit-tests/pom.xml -q test
mvn -f projects/11-testng-project/pom.xml -q test
mvn -f projects/13-rest-assured-api/pom.xml -q test
mvn -f projects/16-parallel-framework/pom.xml -q test
mvn -f projects/17-cicd-framework/pom.xml -q test
```

## PR pipeline

| Stage | What runs | Why |
| --- | --- | --- |
| Compile | `lessons` + selected project modules | Broken Java never ships |
| Unit | JUnit / TestNG that need no browser | Fast feedback on every PR |
| Smoke | `SmokeTest` in this module | Proves the pipeline itself is wired |

Nightly or manual jobs can add live Selenium later. Do not block every PR on a real Chrome install.

## Expected output

```text
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## What success looks like

A PR that breaks `Calculator.add` fails CI. A PR that only changes docs still runs the workflow. No secrets are stored in YAML or this README.

## Stretch challenge

Add a `workflow_dispatch` input that runs Project 12 live Selenium only when a maintainer opts in.
