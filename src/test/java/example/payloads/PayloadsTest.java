package example.payloads;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import common.Json;
import example.api.model.CreateOrderResponseDto;

public class PayloadsTest {

  @Test
  public void createOrderSerializesWithSpecFieldNames() {
    assertThat(Json.write(Payloads.createOrder().priority(true)))
        .isEqualTo("{\"customer_id\":\"customer-123\","
            + "\"lines\":[{\"item_id\":1001,\"quantity\":2}],"
            + "\"priority\":true}");
  }

  @Test
  public void createOrderReturnsNewInstanceOnEachCall() {
    Payloads.createOrder().customerId("changed");
    assertThat(Payloads.createOrder().getCustomerId()).isEqualTo(Payloads.DEFAULT_CUSTOMER_ID);
  }

  @Test
  public void readsResponseIgnoringUnknownFields() {
    CreateOrderResponseDto response = Json.read("{\"order_id\":\"ord-1\",\"accepted\":true,"
        + "\"line_count\":1,\"total_quantity\":2,\"processing_ms\":5,\"new_field\":1}",
        CreateOrderResponseDto.class);
    assertThat(response.getOrderId()).isEqualTo("ord-1");
  }

}
