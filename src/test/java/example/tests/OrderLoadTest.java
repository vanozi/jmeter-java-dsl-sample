package example.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.testPlan;
import static us.abstracta.jmeter.javadsl.JmeterDsl.threadGroup;

import java.io.IOException;
import java.time.Duration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import common.TestConfig;
import example.flows.UserFlows;

public class OrderLoadTest {

  @Disabled("needs the test API running; start it, set -DbaseUrl if needed and remove this")
  @Test
  public void browsersAndBuyers() throws IOException {
    TestPlanStats stats = testPlan(
        TestConfig.defaults(),
        // each call to a factory method creates a new sampler, so reusing them across thread
        // groups is safe
        threadGroup("browsers", 10, 20,
            UserFlows.browseItem()
        ),
        threadGroup("buyers", 2, 10,
            UserFlows.loginOnce(),
            UserFlows.placeOrder(),
            UserFlows.placePriorityOrder()
        ),
        threadGroup("uploaders", 1, 5,
            UserFlows.loginOnce(),
            // any file under src/test/resources
            UserFlows.placeOrderWithDocument("example/uploads/scan.bin"),
            UserFlows.placeOrderWithDocument("example/uploads/scan.bin", "scan-001")
        )
    ).run();

    assertThat(stats.overall().errorsCount()).isZero();
    assertThat(stats.byLabel("login").sampleTime().perc90())
        .isLessThan(Duration.ofSeconds(1));
    assertThat(stats.byLabel("place order").sampleTime().perc90())
        .isLessThan(Duration.ofSeconds(2));
    assertThat(stats.byLabel("GET /api/items/{item_id}").sampleTime().perc90())
        .isLessThan(Duration.ofMillis(500));
    assertThat(stats.byLabel("POST /api/orders/{order_id}/documents").sampleTime().perc90())
        .isLessThan(Duration.ofSeconds(1));
  }

}
