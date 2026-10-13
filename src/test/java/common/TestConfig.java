package common;

import static us.abstracta.jmeter.javadsl.JmeterDsl.httpDefaults;

import java.nio.charset.StandardCharsets;
import us.abstracta.jmeter.javadsl.http.DslHttpDefaults;

/**
 * Environment settings shared by all test plans. Override with system properties, e.g.
 * {@code mvn clean test -DbaseUrl=https://staging.myservice}.
 */
public final class TestConfig {

  public static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8000");

  private TestConfig() {
  }

  /**
   * Add this to every test plan so endpoint samplers can use relative paths.
   */
  public static DslHttpDefaults defaults() {
    return httpDefaults()
        .url(BASE_URL)
        .encoding(StandardCharsets.UTF_8);
  }

}
