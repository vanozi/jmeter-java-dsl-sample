package common.invoker;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Minimal replacement for the generator's ApiClient. Generated models only use these two methods
 * (in toUrlQueryString), and generating the full client would pull in an HTTP client we don't need.
 */
public final class ApiClient {

  private ApiClient() {
  }

  public static String urlEncode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  public static String valueToString(Object value) {
    return value == null ? "" : String.valueOf(value);
  }

}
