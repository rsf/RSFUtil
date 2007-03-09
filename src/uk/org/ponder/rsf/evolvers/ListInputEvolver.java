/*
 * Created on 9 Mar 2007
 */
package uk.org.ponder.rsf.evolvers;

import uk.org.ponder.rsf.components.UIInputMany;
import uk.org.ponder.rsf.components.UIJointContainer;

/** 
 * The interface to a family of evolvers allowing input of a list of 
 * values.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ListInputEvolver {
  /** Evolve the supplied seed component into a subtree representing the list
   * input function.
   * @param toevolve The seed component to be evolved. Must be already connected
   * to the component tree being constructed, and have a colon ID.
   */
  public UIJointContainer evolve(UIInputMany toevolve);
}
