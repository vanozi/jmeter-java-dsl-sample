package common;

public final class JmeterVars {

  private JmeterVars() {
  }

  /**
   * JMeter reference to a variable, e.g. {@code ref("ORDER_ID")} gives {@code ${ORDER_ID}}.
   */
  public static String ref(String varName) {
    return "${" + varName + "}";
  }

}
