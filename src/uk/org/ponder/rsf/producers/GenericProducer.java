/*
 * Created on 4 Jun 2007
 */
package uk.org.ponder.rsf.producers;

import uk.org.ponder.rsf.components.UIContainer;

/** A generic producer which fills a supplied container based on an Object
 * as initialisation data. This is suitable, for example, as an argument to
 * a "meta-evolver" which will render one branch for each member of a 
 * collection.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface GenericProducer {
  public void fillComponents(UIContainer parent, Object source);
}
