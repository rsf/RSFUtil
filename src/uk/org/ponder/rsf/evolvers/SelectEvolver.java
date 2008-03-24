/*
 * Created on 30 Nov 2006
 */
package uk.org.ponder.rsf.evolvers;

import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UISelect;

/** The interface to a family of evolvers for selection controls, with the same
 * binding structure as UISelect. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface SelectEvolver {
  /** Evolve the supplied seed into a branch with the same ID and parent, 
   * containing a component subtree implementing the "widget"/component.
   */
  public UIJointContainer evolveSelect(UISelect toevolve);
}
