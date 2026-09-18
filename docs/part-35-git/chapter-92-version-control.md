# Chapter 92 — Version Control

## 1. Today's Goal

By the end of this lesson, you will explain Git as history for a project, and you will use:

```bash
git status
git add .
git commit -m "Add login validation"
git push
git pull
```

You will stop naming files `final2`.

## 2. Why It Matters

Software is edited for years. SDET code is software. You will break a locator. You will want yesterday's version. Your teammate will change `DriverFactory` while you change `LoginPage`. Git is how those stories do not destroy each other.

Interviews assume Git. "I do not use Git" is not a junior-friendly answer in 2026. It is a blocker.

## 3. Real-Life Analogy

Git is a lab notebook with timestamps, not a pile of photocopies labeled FINAL.

```text
Photocopy pile              Git
final.doc                   commit "Add login validation"
final2.doc                  commit "Fix null password"
really-final.doc            commit "Rename doIt to createTestUser"
```

A **commit** is a named snapshot plus a message. A **branch** is a line of work (your experiment) that can join `main` later. A **repository** (repo) is the project folder plus the hidden `.git` history.

Pushing is mailing a copy of your notebook to the team's library (GitHub). Pulling is updating your notebook from the library.

## 4. Illustrated Explanation

Without Git:

```text
project-final.java
project-final2.java
project-final-new.java
project-final-really-final.java
```

Which one is true? Nobody knows. Diffing them is pain.

With Git:

```text
Repository
 ↓
History
 ↓
Commits
 ↓
Branches
```

Daily loop:

```text
edit files
    ↓
git status          what changed?
    ↓
git add .           stage (choose what goes in the next snapshot)
    ↓
git commit -m "..." snapshot with a message
    ↓
git push            send commits to GitHub
```

Morning loop on a team:

```text
git pull            fetch teammates' commits and merge them into your branch
```

```text
Working copy     files you see and edit
Staging area     the shopping cart for the next commit
Commit           a snapshot in history
Remote           GitHub copy of the repo
```

```text
   edit
     │
     ▼
 working tree  --git add-->  staging  --git commit-->  local history  --git push-->  GitHub
                     git status talks about all of these
```

## 5. Syntax / Concept

**Initialize** (once per project, if not cloned):

```bash
git init
```

**Clone** (when the repo already lives on GitHub):

```bash
git clone <url>
```

**status** — look before you leap. Always.

```bash
git status
```

**add** — stage. `.` means "all changes in this folder." Later you will stage specific files. Beginners may use `.` in a small training repo. Never blindly add `.env` secrets.

```bash
git add .
```

**commit** — snapshot. Message should say *why*, in present or imperative tense:

```bash
git commit -m "Add login validation"
```

Bad messages: `update`, `fix`, `asdf`, `final`.

**push** — upload local commits to the remote.

```bash
git push
```

**pull** — download and integrate remote commits.

```bash
git pull
```

This course does not start with rebase gymnastics. Learn status, add, commit, push, pull, and honest messages.

## 6. Simple Example

```bash
cd status-checker
git init
git status
```

You should see untracked files: `pom.xml`, `src/...`

```bash
git add .
git status
```

Now they are staged.

```bash
git commit -m "Add Maven status-checker skeleton"
git status
```

Working tree clean. That is a good feeling.

If a GitHub remote exists:

```bash
git remote add origin <url>
git push -u origin main
```

Branch names may be `main` or `master`. Modern default is `main`. Follow the repo.

## 7. Real-World Example

Banking team:

```text
main            always releasable automation
feature/login   Aisha's work
fix/timeout     Ben's wait fix
```

They do not edit `main` with random uncommitted files. They commit, push a branch, and open a pull request (Part 36).

E-commerce: a hotfix for a broken checkout locator is a commit with a message a reviewer can search:

```bash
git commit -m "Fix checkout button locator after redesign"
```

Six months later, `git log` still explains the change. `final3.java` never will.

## 8. SDET Example

Test code deserves the same Git hygiene as product code.

```text
BAD
Do not commit
Keep tests only on the laptop
Email LoginTest.java

GOOD
Commit page objects and tests
Push
Let CI run mvn test
```

Also BAD: committing `target/`, `.idea/` junk, screenshots of passwords, `application-secrets.properties` with real tokens.

Use a `.gitignore`:

```text
target/
.idea/
*.iml
.DS_Store
*.env
```

BAD vs GOOD:

```text
BAD
git commit -m "update"

GOOD
git commit -m "Add login validation"
```

## 9. Break the Code

```bash
git commit -m Add login validation
```

Without quotes, the shell splits words. Git may reject extra arguments.

Another break:

```bash
git add .
git commit
```

and then panic in an editor you did not expect. For this course, always `-m "message"`.

Worse:

```bash
git push
```

when you never `git pull` and the remote has new commits. Git refuses. That is protection, not hatred.

Worst: committing a password, then making a second commit that "deletes" it. History still contains the secret. Rotate the password. Treat it as leaked.

## 10. Debug

Read `git status` like a stack trace. It tells you the branch, staged files, unstaged files, and untracked files.

| Message | Meaning |
| --- | --- |
| `not a git repository` | You are in the wrong folder, or never `init`/`clone` |
| `nothing to commit` | No changes, or you forgot `git add` |
| `rejected non-fast-forward` | Remote has commits you do not; `git pull` then push |
| `permission denied` | SSH/token problem for GitHub, not a Java problem |

Do not `rm -rf .git` unless you understand you are deleting history.

If you staged a secret file:

```bash
git reset HEAD secrets.env
```

before committing. If you already committed locally and have not pushed, you can amend only under strict rules (this course: make a new commit that removes the file, and **rotate the secret**). If you pushed a secret, rotate immediately.

## 11. Student Exercise

In your Maven project:

1. Create `.gitignore` with `target/` and `.idea/`.
2. `git status` — confirm `target` is ignored after a compile.
3. `git add .` then `git commit -m "Ignore Maven target directory"`.
4. Change `StatusChecker` by adding a comment.
5. `git status`, then commit with a real message.

## 12. Challenge

Write five commit messages you would accept in a code review, and five you would reject. Then explain the difference in four sentences.

Sketch a diagram of working tree → staging → commit → GitHub. Label `status`, `add`, `commit`, `push`, `pull`.

## 13. Knowledge Check

1. What is a repository?
2. What is a commit?
3. Git vs GitHub?
4. What does `git status` tell you?
5. What does `git add .` do?
6. Why write good commit messages?
7. What does `git push` do?
8. What does `git pull` do?
9. Why ignore `target/`?
10. Why is `project-final2.java` a smell?

## 14. Interview Question

**Question:** How do you use Git in your daily work?

A strong answer:

> I work in a repository with history made of commits. I run git status before I add files. I stage related changes, commit with a message that explains the change, such as Add login validation, then git push to the remote. I git pull before I start or when push is rejected so I integrate teammates' work. I do not commit target/, secrets, or junk. Git is local history; GitHub is where the team hosts it and reviews pull requests.

## 15. Homework

If you have a GitHub account, create a private repo for training and push this Maven project (Part 36 will deepen the PR flow). If you do not, stay local: make three commits that tell a story (skeleton, ignore file, small Java change). Run `git log --oneline` and read your own history.

---

## Answer Key

1. A project plus its Git history (the `.git` directory).
2. A snapshot of staged files plus a message.
3. Git is the tool; GitHub is a hosting service for Git repos.
4. Branch, staged/unstaged/untracked files, whether you are in sync.
5. Stages all changes in the current directory (respecting `.gitignore`).
6. History becomes searchable; reviewers understand why.
7. Sends local commits to the remote.
8. Brings remote commits into your local branch.
9. It is generated output; it bloats the repo and causes fights.
10. It means history was managed with filenames instead of commits.
