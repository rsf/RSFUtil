/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.preservation;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.WriteableBeanLocator;

/** A state preservation strategy that requires no token to be specified.
 * It automatically locates its own storage, for example either because it is 
 * specified through an external dependency (e.g. bean scope) or because it 
 * is stored in the client.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface AutonomousStatePreservationStrategy {
  
  public void restore(WriteableBeanLocator target);
  
  public void preserve(BeanLocator source);
}