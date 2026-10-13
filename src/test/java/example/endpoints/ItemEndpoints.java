package example.endpoints;

import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;
import static us.abstracta.jmeter.javadsl.JmeterDsl.jsonAssertion;
import static example.endpoints.Vars.ITEM_ID;
import static common.JmeterVars.ref;

import us.abstracta.jmeter.javadsl.http.DslHttpSampler;
import example.api.model.ItemDto;

public final class ItemEndpoints {

  private ItemEndpoints() {
  }

  /**
   * Gets the item in {@link Vars#ITEM_ID}, e.g. filled from a csvDataSet column.
   */
  public static DslHttpSampler getItem() {
    return getItem(ref(ITEM_ID), true);
  }

  public static DslHttpSampler getItem(Object itemId, boolean includeTags) {
    // fixed label so all item ids are aggregated in the same stats entry
    return httpSampler("GET /api/items/{item_id}", "/api/items/" + itemId)
        .param("include_tags", String.valueOf(includeTags))
        .children(
            jsonAssertion(ItemDto.JSON_PROPERTY_SKU)
        );
  }

}
