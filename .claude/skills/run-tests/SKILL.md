---
name: run-tests
description: Run the QKart Selenium/TestNG suite via Maven with optional env/browser overrides, then summarize pass/fail results and point to the generated reports (Extent, Allure, logs, screenshots). Use when the user asks to run tests, run the suite, execute the regression/sanity suite, or check if tests pass.
---

# Run Selenium test suite

This project's active suite is fixed in `pom.xml` (surefire `suiteXmlFiles`) to
`src/test/resources/testrunners/testng_sanity.xml`. There is no separate "which suite"
choice to make — running `mvn test` always runs that file's `<test>` blocks.

## 1. Determine env and browser

- `env`: one of `qa` (default if omitted), `stage`, `uat`, `prod` — selects
  `src/test/resources/config/<env>.config.properties`.
- `browser`: `chrome`, `firefox`, or `safari`. Note: `edge` is NOT implemented in
  `DriverFactory.init_driver` (it will throw `RuntimeException: Invalid browser name`),
  even though Jenkins/Docker Compose reference it — don't pass `edge`.
- If the browser isn't overridden via `-Dbrowser`, each `<test>` in
  `testng_sanity.xml` already pins its own browser via `<parameter name="browser">`
  (currently `chrome` for HomePage, `firefox` for Login).

If the user didn't specify env/browser and it's not obvious from context, ask before
running rather than silently defaulting — `prod` in particular should never be run
without explicit confirmation since it exercises a real environment.

## 2. Run

```
mvn clean test -Denv=<env> [-Dbrowser=<browser>]
```

Omit `-Dbrowser` to let the suite XML's per-`<test>` browser parameters apply.

This is a real, potentially slow (browser-driving) command — run it in the foreground
and wait for it to finish; do not background it unless the user asks.

## 3. Summarize results

After the run, report:

- Pass/fail/skip counts. Maven prints a `Tests run: X, Failures: Y, Errors: Z, Skipped: W`
  summary per test class and a `Results:` roll-up at the end — read that from the command
  output rather than re-parsing files.
- For any failure, name the failing test method(s) and the one-line reason if visible in
  the console output (don't dump full stack traces unless asked).
- Point to where deeper detail lives, without opening/serving anything automatically:
  - `build/TestExecutionReport.html` — Extent report
  - `allure-results/` — raw Allure results (view with `allure serve allure-results`,
    only run this if the user asks to see the Allure report)
  - `screenshots/` — failure/skip screenshots, timestamp-prefixed
  - `logs/test-execution.log` (plus daily rolled files) — Log4j2 output

Keep the summary short: counts + failing test names + report pointers. Don't proactively
open the Allure report or the Extent HTML file — just tell the user where they are.
