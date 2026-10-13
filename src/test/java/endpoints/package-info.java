/**
 * Your endpoint samplers: one class per API area, one static method per endpoint, each returning a
 * new {@code DslHttpSampler}. Put the request (path, method, body, headers) and the checks and
 * extractors that always belong to the endpoint here.
 *
 * <p>Start from {@code example.endpoints}: {@code ItemEndpoints} (GET with query params),
 * {@code OrderEndpoints} (JSON body, raw file upload), {@code AuthEndpoints} (form body) and
 * {@code Vars} (names of variables passed between requests).
 */
package endpoints;
