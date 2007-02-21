/*
 * Created on 22 Sep 2006
 */
package uk.org.ponder.rsf.evolvers;

import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;

/** The interface to a family of evolvers for text controls, with the same
 * binding structure as UIInput. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface TextInputEvolver {
  /** The standard ID of the "seed" component when positioned within the 
   * branch created by the evolver.
   */
  public String SEED_ID = "input";
  /** Evolve the supplied seed into a branch with the same ID and parent, 
   * containing a component subtree implementing the "widget"/component.
   */
  public UIJointContainer evolveTextInput(UIInput toevolve);
}
