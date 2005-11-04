/*
 * Created on Sep 22, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.stringutil.StringList;

public class UIOutputMultiline extends UIBound {
  public StringList value;
  public UIOutputMultiline(UIContainer parent, String ID, String binding, StringList value) {
    this.ID = ID;
    this.valuebinding = binding;
    this.value = value;
    parent.addComponent(this);
  }
  public StringList queryValue() {
    return value;
  }
  public void applyValue(StringList value) {
    this.value = value;
  }
}
