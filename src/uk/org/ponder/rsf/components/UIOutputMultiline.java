/*
 * Created on Sep 22, 2005
 */
package uk.org.ponder.rsf.components;

import uk.org.ponder.stringutil.StringList;

/** A multi-line output control. In HTML will render a list of Strings
 * separated by </br> tags - consider use of repetitive leaves or a
 * BranchContainer for general use.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class UIOutputMultiline extends UIBound {
  public static UIOutputMultiline make(UIContainer parent, String ID,
      String binding, StringList value) {
    UIOutputMultiline togo = new UIOutputMultiline();
    togo.ID = ID;
    togo.valuebinding = ELReference.make(binding);
    // note that StringList is not a UIType, and hence can never give rise
    // to input, and hence has no placeholder type, and hence must always
    // be set here.
    if (value != null) {
      togo.setValue(value);
    }
    parent.addComponent(togo);
    return togo;
  }

  public StringList getValue() {
    return (StringList) value;
  }

  public void setValue(StringList value) {
    this.value = value;
  }
}
