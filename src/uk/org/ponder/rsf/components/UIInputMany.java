/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UIInputMany extends UIBound {
  public String[] values;

  public Object uncastValue() {
    return values;
  }

  public void castValue(Object value) {
    values = (String[]) value;
  }
}
