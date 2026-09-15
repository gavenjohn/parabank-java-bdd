# ParaBank BDD Automation Suite

![ci](https://github.com/gavenjohn/parabank-java-bdd/actions/workflows/ci.yml/badge.svg)

Cucumber, Selenium 4, and RestAssured test suite for ParaBank, a demo online banking
application. Companion project to [parabank-playwright-ts](https://github.com/gavenjohn/parabank-playwright-ts) -
same application, deliberately different stack, built to compare Playwright/TypeScript
against Selenium/Cucumber/Java directly rather than abstractly.

## Run it

    docker compose up -d
    mvn test

## Stack

Java 17 · Maven · Cucumber 7 via the JUnit 5 Platform Engine (not TestNG - actively
maintained, and Maven Surefire 3.6+ unified the ecosystem around the JUnit Platform) ·
Selenium 4 (Selenium Manager, no WebDriverManager) · RestAssured 6

## Notes on the approach

- **The application under test runs in a container**, the same digest-pinned ParaBank
  image used by the companion Playwright suite, so both projects test an identical
  target.
- **Schema initialisation runs in a JUnit `@BeforeAll` hook**, not a separate CI step -
  a lesson carried directly from the Playwright project, where a CI-only init step let
  local and CI environments drift out of sync.
- **Customer registration happens over the API** via RestAssured, not through the UI.
  Unlike Playwright's request context, RestAssured does not share cookies across
  separate `given()` calls automatically - the session from the GET has to be captured
  and attached to the POST explicitly, or the request behaves as if no session exists.
- **Waits are explicit**, via `WebDriverWait`. Selenium does not auto-wait for
  navigation the way Playwright does; an assertion read immediately after a form
  submission can catch stale page state intermittently. Caught as a real, reproducible
  flake during development.
- **Hooks are tagged** (`@needsCustomer`, `@ui`), so an API-only scenario doesn't pay
  for a browser launch it doesn't need.

## Coverage

- Login: a registered customer reaches the accounts overview (UI, Selenium)
- Accounts API: a request for a non-existent account returns the correct status and
  message (API, RestAssured)

Early stage. More scenarios and a written Playwright/Selenium comparison are in
progress.