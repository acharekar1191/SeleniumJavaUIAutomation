# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

A Selenium/Java UI automation framework (Page Object Model) that tests the QKart demo e-commerce app (`https://crio-qkart-frontend-qa.vercel.app/`). Built with Maven, TestNG, and Java 21. Despite package names referencing "opencart", the actual application under test is QKart.

## Commands

Run from the repo root (Windows/PowerShell environment).

```
mvn clean test                          # runs the default suite (testng_sanity.xml), QA env, no explicit env flag
mvn clean test -Denv=qa                 # run against a specific environment: qa | stage | uat | prod
mvn clean test -Dbrowser=firefox        # override browser (chrome | firefox | safari); also settable per-<test> in the suite XML
mvn clean install -Denv="qa"            # build + test together
```

- Suite selection is fixed in `pom.xml` via the surefire plugin's `suiteXmlFiles` (`src/test/resources/testrunners/testng_sanity.xml`). To run `testng_regression.xml` instead, edit that `suiteXmlFile` path (it currently references classes/package names that don't match the current `com.qa.opencart.tests` package, so treat it as stale/WIP before using it).
- To run a single test class or method, either temporarily edit the active suite XML's `<classes>` block, or point surefire at a different suite XML.
- Environment config files live in `src/test/resources/config/{qa,stage,uat,prod}.config.properties` — properties include `browser`, `url`, `username`/`password`, `huburl` (Selenium Grid), `remote`, `headless`, `incognito`, `highlight`.
- View the Allure report after a run: `allure serve allure-results` (run from the framework directory).
- Test reports: Extent report at `build/TestExecutionReport.html`; Allure raw results in `allure-results/`; logs in `logs/`; failure screenshots in `screenshots/`.

### Remote/Grid execution

`docker-compose.yml` spins up a Selenium Grid (hub + chrome/edge/firefox nodes). Start with `docker compose -f docker-compose.yml up -d`, then set `remote=true` and `huburl` in the active config properties file to route through the grid instead of a local driver.

### CI

`Jenkinsfile` defines a pipeline with `ENV` (qa/uat/prod) and `BROWSER` (chrome/edge/firefox) build parameters, running `mvn clean test -Denv=... -Dbrowser=...`, then publishing JUnit results, Allure report, logs, and screenshots as artifacts.

## Architecture

**Layering:** `factory` (driver lifecycle) → `pages` (Page Object Model) → `tests` (TestNG test classes) → `listeners` (cross-cutting reporting/retry hooks), with `utils` as shared helpers used across layers.

### Driver management (`com.qa.opencart.factory`)

- `DriverFactory` owns a `ThreadLocal<WebDriver>` (`tlDriver`) so parallel TestNG execution (the sanity suite runs `parallel="methods"` with `thread-count="4"`) gets isolated driver instances. Always get the current thread's driver via the static `DriverFactory.getDriver()`, never store/share a raw `WebDriver` reference across threads.
- `init_prop()` picks the properties file based on the `-Denv` system property (defaults to `qa` when absent).
- `init_driver(prop)` reads `browser`/`remote`/`highlight` from properties, builds options via `OptionsManager`, and either launches locally (Chrome/Firefox/Safari) or via `RemoteWebDriver` against `huburl` when `remote=true`.
- `takeScreenshot(testName)` is used by listeners on failure/skip; screenshots land in `./screenshots/` with a timestamp prefix.

### Page Object Model (`com.qa.opencart.pages`)

Follow the existing "Golden Rules" (see `src/test/java/com/qa/opencart/base/Rules_POM`) when adding pages or tests:

1. `BaseTest` initializes only the landing page (`HomePage`) in `@BeforeTest setup()`.
2. Navigation methods on a page return the *next* page object (e.g. `HomePage.navigateToLoginPageFromHomePage()` returns a `LoginPage`; `LoginPage.doLogin(...)` returns a `HomePage`).
3. Tests must never call `new SomePage(driver)` directly — a page instance is only obtained by navigating from another page.
4. Locators (`By` fields) are always `private`; only actions/assertions are exposed as public methods.

`ElementUtil` wraps all raw Selenium calls (element lookup, explicit waits via `WebDriverWait`/`ExpectedConditions`, dropdowns, alerts, Actions-based interactions, toast/text waiting). Page objects should go through `ElementUtil`, not call `driver.findElement` directly, so that the highlight-on-find debug feature (`DriverFactory.highlight` property) and consistent waiting behavior keep working.

### Tests (`com.qa.opencart.tests`)

- All test classes extend `BaseTest`, which exposes `driver`, `prop`, and already-navigated page fields (`homePage`, `loginPage`, etc.) plus a `SoftAssert` instance.
- Load config values (`prop.getProperty(...)`) inside `@BeforeClass`/`@Test`, not at class-field-initialization time — `BaseTest`'s `@BeforeTest setup()` (which populates `prop`) has not necessarily run yet when instance fields are initialized.
- `ExcelUtil.getData(sheetName)` reads `src/test/resources/testdata/TestData_QKart.xlsx` for `@DataProvider`-driven tests.
- Constants (expected titles, URL fragments, success/error message text) live centrally in `com.qa.opencart.utils.Constants` — reuse/extend these rather than hardcoding strings in tests or pages.

### Listeners (`com.qa.opencart.listeners`)

Registered in the suite XML's `<listeners>` block (currently `testng_sanity.xml`):
- `ExtentReportListener` — builds the Extent HTML report (`build/TestExecutionReport.html`), attaches failure/skip screenshots.
- `TestAllureListener` — attaches screenshots/logs to the Allure report on failure.
- `AnnotationTransformer` (currently commented out in the suite XML) — would wire `Retry` (max 3 retries) as the retry analyzer for every `@Test` if enabled.

Both report listeners extend `DriverFactory` to reuse `getDriver()`/`takeScreenshot()` — keep that inheritance in mind if refactoring driver access.

## Notes on repo hygiene

- `allure-results/`, `screenshots/`, `logs/`, and `build/` are generated test-run artifacts, not source — don't hand-edit them.
- `Notes.txt` and `src/test/java/com/qa/opencart/base/Commands_Notes.txt` are the maintainer's personal scratch notes (Docker/Grid setup, Jenkins, git commands) — useful background, not part of the framework itself.
