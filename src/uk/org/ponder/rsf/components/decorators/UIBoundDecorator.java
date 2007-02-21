/*
 * Created on 20 Feb 2007
 */
package uk.org.ponder.rsf.components.decorators;

import uk.org.ponder.rsf.components.UIBound;

public interface UIBoundDecorator extends UIDecorator {
  public UIBound acquireBound();
  public void updateBound(UIBound toupdate);
}
