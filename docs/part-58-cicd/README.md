# Part 58 — CI/CD

Tests that only run on a laptop are a hobby. **CI** runs them on every relevant change. **CD** is how software (and sometimes tests) get deployed; this course focuses on CI for automation quality gates.

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

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [CI/CD](cicd.md) | Pipeline stages, GitHub Actions PR vs nightly, quality gates |

## Prerequisite

GitHub workflow, Maven, unit tests, optional smoke Selenium. You do not need to be a DevOps engineer.
