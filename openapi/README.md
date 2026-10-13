# OpenAPI specs

| File | Used for |
|---|---|
| `api.yaml` | **Your API.** Put your spec here. On the next build its schemas are generated as Java classes in package `api.model`. |
| `example/test-api.yaml` | Spec of the example in `src/test/java/example`. Generated into package `example.api.model`. |

The generated classes are written to `target/generated-test-sources/` on every build, so they
always match the spec and are never committed.

To use a spec stored somewhere else:

```bash
mvn clean test "-Dopenapi.spec=C:\path\to\spec.yaml" -Pown-api
```

Notes:

- Request bodies of type `multipart/form-data` or `application/x-www-form-urlencoded` don't get a
  generated class. Keep their field names as constants in your endpoint class (see
  `example/endpoints/AuthEndpoints.java`).
- If the generator creates duplicate classes such as `SomethingDto1` (common with OpenAPI 3.1
  specs), map them back with `inlineSchemaNameMappings` in the `own-api` profile of `pom.xml`, as
  the example does for `OrderLineDto_1`.
