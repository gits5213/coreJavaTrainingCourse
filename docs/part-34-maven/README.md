# Part 34 — Maven

**Maven** is a Java build tool. It reads a file named `pom.xml` and uses that file to download libraries, compile code, run tests, and package the project.

```text
Java Project
   ↓
pom.xml
   ↓
Dependencies
   ↓
Plugins
   ↓
Build Lifecycle
```

POM means **Project Object Model**. It is XML. You do not need to love XML. You need to read it without panic.

## Standard Layout

Maven is opinionated. That is a gift. Almost every Java job uses this shape:

```text
project/
├── src/
│   ├── main/
│   │   └── java/          production code (the app / the framework)
│   └── test/
│       └── java/          test code
└── pom.xml
```

If you put tests next to production code randomly, Maven will not treat them as tests. Folders are part of the contract.

## Chapters in This Part

| Chapter | Topic | You will be able to... |
| --- | --- | --- |
| [Chapter 90](chapter-90-what-is-maven.md) | What is Maven? | Explain the lifecycle and the standard directory layout |
| [Chapter 91](chapter-91-pom-xml.md) | pom.xml | Read groupId, artifactId, version, properties, dependencies, plugins; run `mvn` commands |

## Prerequisite

Part 33. You know *why* a build tool exists.

## SDET Connection

`mvn test` is how your future CI job will run. If you only ever click the green triangle in IntelliJ, you have not yet joined the team workflow.
