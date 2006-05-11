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
  /** An identifier for a "group" for this control, in the case it is one of a 
   * set of mutually exclusive choices, e.g. radio buttons. If this is 
   * <code>null</code>, this value is assumed to be independent.
   */
  // NB, this will be used to replace "ID" for fullID computation for
  // BINDINGS, but not for "ID".
  public String group;
  
  public boolean getValue() {
    return ((Boolean)value).booleanValue();
  }

  public void setValue(boolean value) {
    this.value = value? Boolean.TRUE : Boolean.FALSE;
  }
  public UIBoundBoolean() {
    value = BooleanUIType.instance.getPlaceholder();
    fossilize = true;
    willinput = true;
  }
  
  public static UIBoundBoolean make(UIContainer parent, String ID,
      String binding, Boolean initvalue) {
    UIBoundBoolean togo = new UIBoundBoolean();
    togo.valuebinding = ELReference.make(binding);
    togo.fossilize = binding != null;
    if (initvalue != null) {
      togo.setValue(initvalue.booleanValue());
    }
    togo.ID = ID;
    parent.addComponent(togo);
    return togo;
  }
  
  public static UIBoundBoolean make(UIContainer parent, String ID,
       Boolean initvalue) {
    return make(parent, ID, null, initvalue);
  }
  
  public static UIBoundBoolean make(UIContainer parent, String ID,
      boolean initvalue) {
   return make(parent, ID, null, initvalue? Boolean.TRUE : Boolean.FALSE);
 }
}
