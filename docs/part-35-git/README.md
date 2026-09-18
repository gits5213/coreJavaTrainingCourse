# Part 35 — Git

Without Git, projects rot into filename archaeology:

```text
project-final.java
project-final2.java
project-final-new.java
project-final-really-final.java
```

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

**Git** is version control. It records snapshots of your project so you can see what changed, when, and why. It is not GitHub. Git is the tool on your computer. GitHub is a website that hosts Git repositories (Part 36).

## Chapter in This Part

| Chapter | Topic | You will be able to... |
| --- | --- | --- |
| [Chapter 92](chapter-92-version-control.md) | Version control | Use `git status`, `add`, `commit`, `push`, `pull` with intent |

## Prerequisite

You have a Maven project folder you are willing to put under Git. You can use a terminal.

## SDET Connection

A test that is not in Git does not exist for the team. CI clones Git. Code review happens on Git history. "I fixed it locally" is not a delivery.
