/*
 * Created on Jul 28, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.rsf.uitype.BooleanUIType;

/** Component holding a single boolean value, which will peer with a component
 * like a checkbox or radio button.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class UIBoundBoolean extends UIBound {
  public boolean getValue() {
    return ((Boolean)value).booleanValue();
  }

  public void setValue(boolean value) {
    this.value = new Boolean(value); 
  }
  public UIBoundBoolean() {
    value = BooleanUIType.instance.getPlaceholder();
  }
}
