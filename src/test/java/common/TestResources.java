package common;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public final class TestResources {

  private TestResources() {
  }

  /**
   * Absolute path of a file in src/test/resources, for samplers that send files (e.g.
   * {@code bodyFile(TestResources.path("uploads/scan.bin"))}). Resolved from the classpath, so it
   * works whatever directory the test runs from, and fails while building the test plan when the
   * file doesn't exist.
   */
  public static String path(String resourcePath) {
    URL url = TestResources.class.getClassLoader().getResource(resourcePath);
    if (url == null) {
      throw new IllegalArgumentException(
          "File not found in src/test/resources: " + resourcePath);
    }
    try {
      return Paths.get(url.toURI()).toString();
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid resource path: " + resourcePath, e);
    }
  }

}
