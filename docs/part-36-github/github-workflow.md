# GitHub Workflow

## Goal

By the end of this lesson, you will describe the professional path from a local commit to `main` on GitHub: **push a branch, open a pull request, take review, wait for CI, then merge**.

You will treat GitHub as a team process, not as a backup disk.

## Why It Matters

SDET code changes production risk. A broken locator merged to `main` can fail the nightly suite and hide real bugs. Pull requests (PRs) exist so another human and a machine (CI) look before that happens.

Hiring managers ask: "How do you get code into main?" They want this diagram, not "I upload a ZIP."

## Real-Life Analogy

A pull request is a proposed edit to a shared cookbook.

```text
You cook a new recipe at home          local branch
You submit it to the restaurant        pull request
Head chef tastes it                    code review
Health inspector checks the kitchen    CI (compile, tests)
Recipe is printed in the book          merge to main
```

You do not sneak a page into the printed book. You propose, you wait, you fix comments, then it prints.

## Illustrated Explanation

```text
Local Repository
       ↓
GitHub Repository
       ↓
Pull Request
       ↓
Code Review
       ↓
CI
       ↓
Merge
```

More detail:

```text
main (protected)
   │
   │  git checkout -b feature/login-validation
   ▼
local branch  --git push-->  GitHub branch
                                │
                                ▼
                         Pull Request
                         /          \
                   human review     CI pipeline
                         \          /
                          both green
                                │
                                ▼
                         merge to main
```

Typical CI on a PR (you will implement this in Part 58):

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
Merge (allowed)
```

## Concept

**Remote:** GitHub's copy, often named `origin`.

**Pull request:** a request to pull your branch into `main` (or another base). GitHub also calls it a merge request in spirit; GitLab uses that name. Same idea.

**CODEOWNERS / reviewers:** people who must look. Be kind. Review the code, not the person.

**CI:** GitHub Actions (or Jenkins, etc.) runs commands like `mvn test` on a clean machine.

**Merge:** the commits join `main`. Delete the feature branch afterward to keep the repo tidy.

**Never push secrets.** GitHub is often cloned by many systems.

## Simple Example

```bash
git checkout -b feature/status-matches
# edit StatusChecker.java
git add src/main/java/com/training/sdet/StatusChecker.java
git commit -m "Add statusMatches helper"
git push -u origin feature/status-matches
```

On GitHub: **Compare & pull request**. Title: `Add statusMatches helper`. Description:

```text
What: boolean helper for expected vs actual HTTP status
Why: tests will reuse this instead of copying ==
How to test: mvn test
```

Ask a classmate or future-you to review. Merge only when checks are green.

## Real-World Example

Banking: `main` is protected. You cannot push to it directly. A junior SDET opens a PR that changes `TransferPage`. A senior reviews for locators, waits, and secrets. CI runs smoke tests against QA. Merge happens after both pass.

E-commerce: a Black Friday hotfix still uses a PR. Emergency is not an excuse to skip history. It is an excuse to review faster.

## SDET Example

Automation PRs should include:

- what user flow or API this covers
- how to run it (`mvn test -Dgroups=smoke`)
- screenshots or logs if UI changed (not passwords)
- test data notes (use fixtures, not production customers)

```text
BAD PR
"fix stuff"
5000-line framework dump
no tests run

GOOD PR
small
one purpose
CI green
reviewer can run one command
```

## Examples You Can Type

A small `.github/pull_request_template.md` you may add later:

```markdown
## What
-

## Why
-

## How to test
- [ ] `mvn test`

## Risk
- [ ] No secrets committed
- [ ] Tests independent
```

## Exercise

1. Create a GitHub repository (private is fine for training).
2. Push `main`.
3. Create a branch, change a comment in Java, push, open a PR.
4. Write a PR description with What / Why / How to test.
5. Merge it yourself (solo training) or wait for a review (class).

## Challenge

Write a policy in 10 lines: when is it acceptable to merge your own PR? Include CI, size, and secrets. Then list three reasons PRs get too big (framework dump, mixed formatting, "while I was here").

## Knowledge Check

1. What is a pull request?
2. Why protect `main`?
3. What is CI's job on a PR?
4. Git vs GitHub, one sentence each.
5. What belongs in a PR description?
6. True or false: a red CI check is OK if it passed on your laptop.
7. Why delete feature branches after merge?
8. What should you never push?
9. Name the workflow steps from local to merge.
10. Why do SDET PRs need run instructions?

## Interview Question

**Question:** Walk me through your GitHub workflow.

A strong answer:

> I commit on a feature branch, push to GitHub, and open a pull request into main. The PR describes what, why, and how to test. A reviewer checks design and risk. CI compiles, runs quality checks, unit tests, and smoke tests. I only merge when review and CI pass. That same path later includes nightly regression. Git is the history tool; GitHub is the collaboration and CI host.

## Homework

Complete one real PR for your training repo, even if you are the only reviewer. Enable a simple GitHub Action later in Part 58. For now, the habit is the lesson: nothing important goes to `main` without a PR.

---

## Answer Key

1. A proposal to merge one branch into another, with discussion and checks.
2. So broken or unreviewed code cannot land silently.
3. Prove the branch builds and tests on a clean machine.
4. Git records history locally; GitHub hosts remotes, PRs, and Actions.
5. What changed, why, how to test, risks.
6. False. CI is the shared source of truth.
7. Reduces clutter and accidental commits to dead branches.
8. Secrets, passwords, tokens, private customer data.
9. Local repo → GitHub → PR → review → CI → merge.
10. Reviewers and CI must reproduce the run; automation is easy to "pass" only locally.
