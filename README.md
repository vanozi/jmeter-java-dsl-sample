# jmeter-java-dsl-sample

A starting point for API load tests with
[jmeter-java-dsl](https://abstracta.github.io/jmeter-java-dsl), where request and response
classes are generated from an OpenAPI spec.

The project has two sides:

- **`example`**: a complete, working example against a small test API (items, orders, file upload,
  form login). Read it, copy from it, delete it when you no longer need it.
- **Your own code**: empty packages and an `openapi/api.yaml` slot to build your own endpoints and
  flows in.

Check the [jmeter-java-dsl user guide](https://abstracta.github.io/jmeter-java-dsl/guide) for
everything the DSL can do.

## Pre requisites

* Java 11+
* Maven 3.5+

## Structure

```
openapi/
├── api.yaml                      ← put YOUR spec here (generated into package api.model)
└── example/test-api.yaml         spec of the example (generated into example.api.model)

src/test/java/
├── common/                       shared helpers, used by the example and by your code
│   ├── TestConfig.java           base URL (-DbaseUrl=...) and httpDefaults()
│   ├── Json.java                 DTO → request body, response type check
│   ├── TestResources.java        path of a file in src/test/resources (for uploads)
│   ├── JmeterVars.java           ref("X") → ${X}
│   └── invoker/ApiClient.java    needed by the generated classes, don't remove
│
├── endpoints/                    ┐
├── flows/                        │ YOUR code. Each package has a package-info.java
├── payloads/                     │ explaining what goes there and what to copy from
├── tests/                        ┘
│
└── example/                      THE EXAMPLE
    ├── endpoints/                one method per endpoint, returning a new sampler
    ├── flows/                    endpoints grouped into named steps (transactions)
    ├── payloads/                 request DTOs with default values
    └── tests/                    OrderLoadTest (thread groups of flows), PerformanceTest

src/test/resources/
└── example/uploads/scan.bin      file uploaded by the example
```

The layers, from bottom to top:

1. **Models** (generated): request and response classes from the spec, rebuilt on every build in
   `target/generated-test-sources/`.
2. **Payloads**: valid request objects with default values, e.g.
   `Payloads.createOrder().priority(true)`.
3. **Endpoints**: one method per API call, returning a new `httpSampler` with its body, headers,
   checks and extractors.
4. **Flows**: endpoints grouped into business steps with `transaction(...)`, each reported as its
   own line in the results.
5. **Tests**: thread groups of flows, plus assertions on the results.

## Getting started with your own API

1. Copy your spec to `openapi/api.yaml` and run `mvn clean test-compile`. Your schemas are now
   classes in package `api.model`. See [openapi/README.md](openapi/README.md) for other locations
   and known generator quirks.
2. Write a payload method per request body in `payloads/` (see `example/payloads/Payloads.java`).
3. Write an endpoint class per API area in `endpoints/` (see `example/endpoints/`). It shows a GET
   with query params, a JSON POST, a raw `application/octet-stream` file upload with an optional
   header, and an `application/x-www-form-urlencoded` login.
4. Group endpoints into flows in `flows/` (see `example/flows/UserFlows.java`).
5. Write a load test in `tests/` (see `example/tests/OrderLoadTest.java`) and run it against your
   environment:

   ```bash
   mvn clean test -DbaseUrl=https://my-environment -Dusername=... -Dpassword=...
   ```

Files to upload go anywhere under `src/test/resources`; pass their path relative to that folder.

## Removing the example

Delete `src/test/java/example`, `src/test/resources/example`, `openapi/example`, and in `pom.xml`
the `example-api-models` execution, the `normalize-example-openapi-spec-path` execution and the
`openapi.example.spec` property.

## Executing tests

`example/tests/OrderLoadTest` is `@Disabled` because it needs the example API running. Run all
other tests with:

```bash
mvn clean test
```
