# Selenium Grid

## Goal

By the end of this lesson, you will explain Tests → RemoteWebDriver → Grid → Nodes, point `DriverFactory` at a Grid URL when needed, and refuse Grid on day one of a two-test project (YAGNI).

## Why It Matters

CI machines may not have Chrome with a GUI. Cross-browser nightly needs many nodes. Grid (or a cloud vendor that speaks the same protocol) is how tests still use `WebDriver` while browsers live elsewhere.

Interviews: "What is Selenium Grid?" Draw the diagram. "When do you need it?" When local browsers cannot meet scale or OS matrix.

## Real-Life Analogy

A call center.

```text
You (test) dial one number (Grid URL)
Switchboard (Grid) assigns an agent
Agent (node) has a phone type: Chrome, Firefox, Edge
You still speak the same language (WebDriver commands)
```

You do not sit at every agent's desk. You call the switchboard.

## Illustrated Explanation

```text
Tests
 ↓
RemoteWebDriver
 ↓
Selenium Grid
 ↓
Browser Nodes
```

Grid 4 often runs as a **standalone** or **hub + nodes**. The idea is the same: a router plus browsers.

```text
DriverFactory
  if local  → new ChromeDriver()
  if grid   → new RemoteWebDriver(gridUrl, options)
```

Tests do not change. Factory does.

```text
Capabilities / Options
  browserName = chrome
  platform    = linux
```

The Grid matches a node that can provide that browser.

## Syntax / Concept

```java
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

public static WebDriver remoteChrome(String gridUrl) throws Exception {
    ChromeOptions options = new ChromeOptions();
    return new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
}
```

Grid URL is often `http://localhost:4444/` for local Docker Grid, or a vendor URL.

Still `quit()` — that releases the node session. Forgetting quit occupies a Grid slot until timeout. Parallel + forgotten quit = "no available slot" for everyone.

`RemoteWebDriver` **is a** `WebDriver`. Page objects should not care.

## Simple Example (factory fragment)

```java
public static WebDriver create(BrowserType type) {
    String grid = System.getenv("GRID_URL");
    if (grid == null || grid.isBlank()) {
        return local(type);
    }
    return remote(type, grid);
}
```

Local default keeps laptops simple. CI sets `GRID_URL`.

## Real-World Example

Banking: PR pipeline uses a small Grid with 4 Chrome nodes. Nightly adds Firefox and Edge nodes. Same tests.

Cloud (BrowserStack, Sauce): still RemoteWebDriver + options. You are renting nodes. Secrets for their API keys go in CI, not Git.

## SDET Example

```text
Tests (CI)
  RemoteWebDriver
    Grid in Kubernetes
      Chrome nodes
      Firefox nodes
```

Logging: record session id from RemoteWebDriver for vendor video. Still no passwords.

## Break the Code

```java
new ChromeDriver(); // on CI with no Chrome installed
```

```java
new RemoteWebDriver(url, options);
// never quit — Grid fills up
```

```java
gridUrl = "http://localhost:4444"
// CI agent is not your laptop; localhost is the agent, not your Grid
```

Use a real Grid hostname from config.

## Debug

| Symptom | Check |
| --- | --- |
| `Could not start a new session` | Grid down, no matching node, wrong browserName |
| tests hang | no free slots; sessions not quit |
| works local, fails CI | GRID_URL missing; still starting local Chrome |
| SSL / timeout | network between CI and Grid |

Open Grid console (Grid 4 UI) and watch sessions appear and disappear. If they never disappear, quit is broken.

## Student Exercise

Draw the four-line architecture from memory. Add `GRID_URL` support in factory as a stub that throws `UnsupportedOperationException("Grid not configured")` when set, *or* run a Docker Grid if your instructor provides commands. Do not require Docker to pass the course conceptually.

## Challenge

Write a one-page decision: local vs Grid vs cloud. Include cost, flake debugging (video), data privacy (can customer data hit a cloud browser?).

## Knowledge Check

1. Draw Tests → ... → Nodes.
2. What class talks to Grid?
3. Who should know the Grid URL?
4. Why quit still matters more on Grid?
5. Is RemoteWebDriver usable as WebDriver?
6. When is Grid YAGNI?
7. Why is localhost Grid URL wrong on CI?
8. What are nodes?
9. How do you choose Chrome vs Firefox on Grid?
10. Cloud vendors vs self-hosted Grid?

## Interview Question

**Question:** Explain Selenium Grid.

A strong answer:

> Tests use RemoteWebDriver against a Grid URL. Grid assigns a browser node: Chrome, Firefox, or Edge. Architecture is Tests → RemoteWebDriver → Selenium Grid → Browser Nodes. DriverFactory returns local drivers or remote ones from config. Tests and page objects stay the same. I always quit to free sessions. I do not introduce Grid until I need scale or a browser I cannot run on the CI agent. Isolation and ThreadLocal still apply: each test still needs its own session.

## Homework

Document in README how your factory will read `GRID_URL`. Do not commit a fake cloud key. Optional: follow an official Grid Docker example on a personal machine.

---

## Answer Key

1. Tests → RemoteWebDriver → Selenium Grid → Browser Nodes
2. `RemoteWebDriver`
3. Factory/config, not every test
4. Occupied slots starve the team
5. Yes
6. Few tests, local Chrome is enough
7. localhost is the CI machine, not your PC
8. Machines/containers that actually run browsers
9. Options / capabilities
10. Same protocol; cloud is rented nodes + extras (video)
