/*
 * Created on Nov 1, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIForm;

public interface FormModel {
  /** Registers a component as one that will be submitted
   * by the specified form control. 
   */
  public void registerChild(UIForm parent, UIBound submittingchild);
}
