/*
 * Created on 11-Feb-2006
 */
package uk.org.ponder.rsf.components;

/** A bound object of unknown type. Useful for "non-user" components such as
 * renderers.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class UIBoundObject extends UIBound {
  public Object getValue() {
    return value;
  }
  
  public void setValue(Object value) {
    this.value = value;
  }
}
