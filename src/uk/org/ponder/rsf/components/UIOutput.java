/*
 * Created on Jul 27, 2005
 */
package uk.org.ponder.rsf.components;

/**
 * The central prototype of all components. A UIOutput holds a single String
 * value, which may or may not be bound to an EL value binding. It may peer
 * with essentially any tag, since its operation is to replace the tag body
 * with the <code>text</code> value it holds, if this value is not null.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UIOutput extends UIBound {
  public String text;

  public Object uncastValue() {
    return text;
  }

  public void castValue(Object value) {
    text = (String) value;
  }
}
