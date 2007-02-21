/*
 * Created on 20 Feb 2007
 */
package uk.org.ponder.rsf.components.decorators;

import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIBoundString;

public class UIBoundStringDecorator implements UIBoundDecorator {
  public UIBoundString text;

  public UIBound acquireBound() {
    return text;
  }

  public void updateBound(UIBound toupdate) {
    this.text = (UIBoundString) toupdate;
  }
}
