# Part 13 — Packages

A **package** is a named folder for related Java types. It is how professional projects stay navigable when you have more than one class.

```text
com.company.project
 ├── pages
 ├── api
 ├── models
 ├── utils
 └── tests
```

Without packages, everything lives in one pile. Names collide. Testers import the wrong `User`. IntelliJ's tree becomes a junk drawer.

## Chapter in This Part

| Chapter | Topic | You will be able to... |
| --- | --- | --- |
| [Chapter 54](chapter-54-packages.md) | Packages | Declare `package`, import types, and sketch an SDET folder tree |

## Prerequisite

You can write several classes (`User`, `LoginPage`). Packages are how those classes live in a real repo.

## SDET Connection

Page objects, API clients, models, and tests must not sit in one folder. A clean tree is already architecture:

```text
tests call pages and api
pages and api use models
utils are shared tools
```

## After This Part

You will still write small examples in the default package when a lesson is tiny. For homework and real projects, you will start files with a `package` line.
