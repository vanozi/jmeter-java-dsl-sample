package common;

import static us.abstracta.jmeter.javadsl.JmeterDsl.jsr223PostProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UncheckedIOException;
import org.apache.jmeter.samplers.SampleResult;
import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor;

public final class Json {

  // ObjectMapper is thread safe once configured, so all JMeter threads can share it
  private static final ObjectMapper MAPPER = new ObjectMapper()
      // new fields in responses should not break the load test
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  private Json() {
  }

  public static String write(Object value) {
    try {
      return MAPPER.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static <T> T read(String json, Class<T> type) {
    try {
      return MAPPER.readValue(json, type);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Marks the sample as failed when the response body can't be read as the given response type.
   * Add as a child of a sampler. Parsing every response costs load generator CPU, so for high
   * load tests prefer jsonAssertion on the fields you need.
   */
  public static DslJsr223PostProcessor matchesType(Class<?> type) {
    return jsr223PostProcessor(s -> {
      SampleResult result = s.prev;
      if (!result.isSuccessful()) {
        return;
      }
      try {
        MAPPER.readValue(result.getResponseDataAsString(), type);
      } catch (JsonProcessingException e) {
        result.setSuccessful(false);
        result.setResponseMessage(
            "Response is not a valid " + type.getSimpleName() + ": " + e.getOriginalMessage());
      }
    });
  }

}
