/*
 * Created on Nov 11, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.rsf.uitype.StringUIType;
/** A bound value whose type is <code>java.lang.String</code> **/

public class UIBoundString extends UIBound {
  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return (String) value;
  }
  
  public UIBoundString() {
    value = StringUIType.instance.getPlaceholder();
  }

}
