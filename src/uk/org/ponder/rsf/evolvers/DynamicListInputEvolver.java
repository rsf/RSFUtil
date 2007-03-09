/*
 * Created on 6 Mar 2007
 */
package uk.org.ponder.rsf.evolvers;

import uk.org.ponder.rsf.components.UIBoundString;

/** 
 * The interface to a family of evolvers allowing input of a dynamically 
 * adjustable list.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface DynamicListInputEvolver extends ListInputEvolver {
  /** @param removelabel The label to be supplied for the controls which remove an
   * item from the list.
   * @param addlabel The label to be supplied for the control which adds an item
   * to the list.
   */
  public void setLabels(UIBoundString removelabel, UIBoundString addlabel);
}
