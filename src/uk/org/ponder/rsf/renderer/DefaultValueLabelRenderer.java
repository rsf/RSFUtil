/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.renderer;

public class DefaultValueLabelRenderer implements ValueLabelRenderer {
  public DefaultValueLabelRenderer instance = new DefaultValueLabelRenderer();
  
  public String renderLabel(Object value) {
    return value.toString();
  }

}
