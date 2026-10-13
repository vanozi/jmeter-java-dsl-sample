package example.payloads;

import java.util.ArrayList;
import java.util.List;
import example.api.model.CreateOrderRequestDto;
import example.api.model.OrderLineDto;

/**
 * Valid request DTOs with default values. Each method returns a new instance, and the generated
 * DTOs are fluent, so tests only override what matters to them:
 * {@code Payloads.createOrder().priority(true)}.
 */
public final class Payloads {

  public static final String DEFAULT_CUSTOMER_ID = "customer-123";
  public static final int DEFAULT_ITEM_ID = 1001;
  public static final int DEFAULT_QUANTITY = 2;

  private Payloads() {
  }

  public static OrderLineDto orderLine() {
    return new OrderLineDto()
        .itemId(DEFAULT_ITEM_ID)
        .quantity(DEFAULT_QUANTITY);
  }

  public static CreateOrderRequestDto createOrder() {
    // mutable list so callers can still use addLinesItem
    return new CreateOrderRequestDto()
        .customerId(DEFAULT_CUSTOMER_ID)
        .lines(new ArrayList<>(List.of(orderLine())))
        .priority(false);
  }

}
