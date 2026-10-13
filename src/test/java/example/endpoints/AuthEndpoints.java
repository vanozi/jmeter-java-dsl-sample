package example.endpoints;

import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;
import static us.abstracta.jmeter.javadsl.JmeterDsl.jsonExtractor;
import static example.endpoints.Vars.ACCESS_TOKEN;

import org.apache.jmeter.protocol.http.util.HTTPConstants;
import us.abstracta.jmeter.javadsl.http.DslHttpSampler;
import example.api.model.TokenResponseDto;
import common.Json;

public final class AuthEndpoints {

  // form field names from TokenRequestDto in the spec. The generator doesn't create a model for
  // form bodies, so there are no generated constants for them
  public static final String GRANT_TYPE_FIELD = "grant_type";
  public static final String USERNAME_FIELD = "username";
  public static final String PASSWORD_FIELD = "password";
  public static final String SCOPE_FIELD = "scope";

  // JMeter properties, so credentials stay out of the code: mvn test -Dusername=... -Dpassword=...
  public static final String DEFAULT_USERNAME = "${__P(username,test-user)}";
  public static final String DEFAULT_PASSWORD = "${__P(password,test-password)}";

  private AuthEndpoints() {
  }

  public static DslHttpSampler getToken() {
    return getToken(DEFAULT_USERNAME, DEFAULT_PASSWORD, null);
  }

  /**
   * Gets an access token with an application/x-www-form-urlencoded body and stores it in
   * {@link Vars#ACCESS_TOKEN}.
   *
   * @param scope optional scope, or null to leave the field out
   */
  public static DslHttpSampler getToken(String username, String password, String scope) {
    // POST with params sends them as a url-encoded form body. JMeter encodes the values and sets
    // the Content-Type header
    DslHttpSampler sampler = httpSampler("POST /api/auth/token", "/api/auth/token")
        .method(HTTPConstants.POST)
        .param(GRANT_TYPE_FIELD, "password")
        .param(USERNAME_FIELD, username)
        .param(PASSWORD_FIELD, password)
        .children(
            Json.matchesType(TokenResponseDto.class),
            jsonExtractor(ACCESS_TOKEN, TokenResponseDto.JSON_PROPERTY_ACCESS_TOKEN)
        );
    if (scope != null) {
      sampler.param(SCOPE_FIELD, scope);
    }
    return sampler;
  }

}
