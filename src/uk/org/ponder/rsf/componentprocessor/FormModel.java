/*
 * Created on Nov 1, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.rsf.components.UIBound;
import uk.org.ponder.rsf.components.UIForm;
/** An interface recording the relationship between a "Form" (scope for a 
 * set of submitting controls) and its children. This interface will probably
 * become disused since UIForm now collects IDs of children separately.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface FormModel {
  /** Registers a component as one that will be submitted
   * by the specified form control. 
   */
  public void registerChild(UIForm parent, UIBound submittingchild);
}
