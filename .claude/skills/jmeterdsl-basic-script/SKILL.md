---
name: jmeterdsl-basic-script
description: Generate a JMeterDSL Java test script for load testing or continuous performance testing in CI pipelines. Use this skill when the user wants a JMeter test, a performance smoke test, a load test for a URL, or asks how to add performance testing to their project using Java, Maven, or JUnit. Also use it for any request for a basic JMeterDSL script — even without mentioning P90, error rate, or throughput by name.
compatibility: Java, JMeterDSL
---

# JMeterDSL Basic Script Generator

Generates a minimal Java test class using the JMeterDSL Java API, following best practices for continuous performance testing: named constants, response assertion, error handling, and three automated pass/fail criteria (P90, error rate, throughput).

## Before generating

Read `assets/GeneratedPerformanceTest.java` before writing any code. Use it as your starting point — adapt the URL and expected text for the user's target, but preserve the overall structure, all three assertions, and the error action setting.

**Clarify missing inputs before generating:**
- If the user has provided neither a target URL nor expected response text, ask for both before generating: "What URL should the test hit, and what text do you expect in the response?"
- If the user provided a URL but no expected response text, generate using the placeholder `"TODO: replace with expected response text"` and add an inline comment on that same line: `// TODO: set expected content before running`
- If the user provided both, use them directly — do not ask.

## What to generate

1. **Imports** — use the same imports as the template:
   - `assertThat` from AssertJ
   - `static us.abstracta.jmeter.javadsl.JmeterDsl.*`
   - `Duration`, `Test`, `TestPlanStats`

2. **Named constants** at class level for all tunable values:
   - `THREADS`, `ITERATIONS`, `P90_LIMIT_MS`, `MAX_ERROR_RATE_PCT`, `MIN_RPS`
   - If the user provides specific values, use them; otherwise use the template defaults.

3. **Test method** that:
   - Calls `testPlan(...)` and `.run()`
   - Uses `threadGroup(THREADS, ITERATIONS, httpSampler(...))`
   - Adds `responseAssertion().containsSubstrings(...)` under `.children(...)`
   - Sets `.sampleErrorAction(SampleErrorAction.START_NEXT_ITERATION)` — this prevents a single failure from stopping the whole thread group, which is essential for accurate load test stats.

4. **Three pass/fail assertions**:
   - P90: `stats.overall().sampleTime().perc90()` less than `Duration.ofMillis(P90_LIMIT_MS)`
   - Error rate: calculated from `errorsCount` / `samplesCount` × 100, asserted less than `MAX_ERROR_RATE_PCT`
   - Throughput: calculated from `samplesCount` / test duration in seconds, asserted ≥ `MIN_RPS`

## Output format

- One Java source file.
- Include a `package` declaration — use `org.example.performance` if the user hasn't specified one.
- Class name: reflect the target system if known (e.g., `CheckoutServicePerformanceTest`), otherwise use `GeneratedPerformanceTest`.
- If the user hasn't specified any additional features, do not add CSV data sources, loops, complex listeners, or other features not in the template — keep it minimal so it's easy to extend later.
