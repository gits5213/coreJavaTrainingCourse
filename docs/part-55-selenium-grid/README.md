# Part 55 — Selenium Grid

When browsers must run on other machines (or containers), tests talk to Grid, not to a local ChromeDriver.

```text
Tests
 ↓
RemoteWebDriver
 ↓
Selenium Grid
 ↓
Browser Nodes
```

## Lesson in This Part

| Lesson | Topic |
| --- | --- |
| [Selenium Grid](selenium-grid.md) | Hub/nodes (Grid 4), RemoteWebDriver, capabilities, when not to use Grid |

## Prerequisite

Driver factory. ThreadLocal if parallel. Docker is optional; understand the diagram even if you never start Grid locally.
