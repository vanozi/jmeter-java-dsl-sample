package org.example.performance;

import static org.assertj.core.api.Assertions.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import us.abstracta.jmeter.javadsl.core.threadgroups.BaseThreadGroup.SampleErrorAction;

import java.time.Duration;
import org.junit.jupiter.api.Test;

public class GeneratedPerformanceTest {

  private static final int THREADS = 10;
  private static final int ITERATIONS = 10;
  private static final long P90_LIMIT_MS = 1500;
  private static final double MAX_ERROR_RATE_PCT = 1.0;
  private static final double MIN_RPS = 1.0;

  @Test
  public void testPerformance() throws Exception {
    TestPlanStats stats = testPlan(
        threadGroup(THREADS, ITERATIONS,
            httpSampler("https://example.com/")
                .children(
                    responseAssertion().containsSubstrings("expected content")
                )
        )
        .sampleErrorAction(SampleErrorAction.START_NEXT_ITERATION)
    ).run();

    assertThat(stats.overall().sampleTime().perc90())
        .isLessThan(Duration.ofMillis(P90_LIMIT_MS));

    double errorRate = stats.overall().samplesCount() > 0
        ? stats.overall().errorsCount() * 100.0 / stats.overall().samplesCount()
        : 0.0;
    assertThat(errorRate).isLessThan(MAX_ERROR_RATE_PCT);

    Duration testDuration = stats.duration();
    double seconds = testDuration.toMillis() / 1000.0;
    double rps = seconds > 0 ? stats.overall().samplesCount() / seconds : 0.0;
    assertThat(rps).isGreaterThanOrEqualTo(MIN_RPS);
  }
}
