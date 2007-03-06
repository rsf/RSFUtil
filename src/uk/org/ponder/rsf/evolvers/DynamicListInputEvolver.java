/*
 * Created on 6 Mar 2007
 */
package uk.org.ponder.rsf.evolvers;

import uk.org.ponder.rsf.components.UIBoundString;
import uk.org.ponder.rsf.components.UIInputMany;
import uk.org.ponder.rsf.components.UIJointContainer;

/** 
 * The interface to a family of evolvers allowing input of a dynamically 
 * adjustable list.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface DynamicListInputEvolver {
  /** Evolve the supplied seed component into a subtree representing the list
   * input function.
   * @param toevolve The seed component to be evolved. Must be already connected
   * to the component tree being constructed, and have a colon ID.
   * @param removelabel The label to be supplied for the controls which remove an
   * item from the list.
   * @param addlabel The label to be supplied for the control which adds an item
   * to the list.
   */
  public UIJointContainer evolve(UIInputMany toevolve, UIBoundString removelabel, 
      UIBoundString addlabel);
}
