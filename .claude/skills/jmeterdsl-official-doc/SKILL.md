---
name: jmeterdsl-official-doc
description: Use JMeterDSL official documentation to answer user questions about JMeterDSL Java or JMeterDSL .NET syntax, setup, test plan construction, recorder usage, JMX conversion, scaling, reporting, response processing, protocols, and engine integrations. Trigger this skill whenever the user asks about JMeterDSL or `jmeter-java-dsl`; JMeterDSL .NET or `jmeter-dotnet-dsl`; writing or running performance tests with JMeter DSL in Java or .NET; DSL test plans, samplers, listeners, controllers, or engines; recorder usage; JMX conversion; advanced JMeterDSL features; how to translate JMeter concepts into JMeterDSL code; how to run JMeter from Java or .NET code; `testPlan()`, `httpSampler()`, or any JMeterDSL API method — even if the user doesn't say "JMeterDSL" explicitly but is clearly working with performance test code in a DSL style.
---

# JMeterDSL Official Documentation Skill

Answer all JMeterDSL questions by fetching and reading the official live documentation. Never answer from memory alone — the docs evolve with each release and memorized answers will break test code.

## Step 1: Determine Java or .NET context

Before fetching, identify which flavor the user needs:
- **Java** (`jmeter-java-dsl`): https://abstracta.github.io/jmeter-java-dsl/guide/
- **.NET** (`jmeter-dotnet-dsl`): https://abstracta.github.io/jmeter-dotnet-dsl/guide/

If the user's message doesn't make this clear, ask before fetching. Don't guess — the APIs differ significantly.

## Step 2: Discover the relevant section dynamically

Fetch the appropriate guide URL. The response includes a navigation sidebar or table of contents listing the available sections and their anchors. Read it to identify which section covers the user's question, then fetch that specific section or anchor URL.

This keeps the skill aligned with current docs at all times — no local index to go stale.

If the question is narrow and you're confident which section applies (e.g., "how do I add a cookie manager" → clearly the HTTP section), you can skip the discovery fetch and go directly to the relevant anchor.

## Step 3: Fetch the section and answer

Use `WebFetch` to retrieve the relevant documentation content before composing your answer. If the first fetch doesn't contain the answer, follow internal doc links or try adjacent sections — don't fall back to training data.

If a feature genuinely isn't in the docs, move to **Step 4** before giving up.

## Step 4: Check the source code (when docs don't answer)

When the documentation is silent on a specific behavior, fetch the relevant source file directly from GitHub — but stay surgical: read only what's needed to answer the question.

**Java repo:** `https://github.com/abstracta/jmeter-java-dsl`
**.NET repo:** `https://github.com/abstracta/jmeter-dotnet-dsl`

Strategy:
1. Identify the most likely class or file (e.g., `AzureEngine.java` for Azure behavior).
2. Fetch that single file via `WebFetch` using the raw GitHub URL:
   `https://github.com/abstracta/jmeter-java-dsl/blob/master/<path-to-file>`
3. Extract only the relevant method or block — do not analyze the full file.
4. If the first file doesn't have the answer, follow one level of delegation (e.g., a called method in another class), then stop.

If the source code also doesn't clarify the behavior, say so and recommend the user test empirically or open a GitHub issue.

Note in your answer when the response is based on source code rather than official docs, so the user knows it may change across versions.

## Output style

- Short, precise explanations backed by the fetched docs.
- Code examples must be minimal but complete: always include imports, the `testPlan()` wrapper, and a `run()` or `show()` call. A snippet without context forces the user to guess the surrounding structure.
- Match the code flavor (Java or .NET) to the user's context.
- Cite the documentation section or URL anchor you used, so the user can read further.
- When recommending between approaches, prefer the one the official guide recommends.
