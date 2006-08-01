/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.preservation;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.stringutil.StringList;

/** A state preservation strategy that requires no token to be specified.
 * It automatically locates its own storage, for example either because it is 
 * specified through an external dependency (e.g. bean scope) or because it 
 * is stored in the client.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface AutonomousStatePreservationStrategy {
  /** Restores any beans preserved by this strategy into the target 
   * container.
   * @return A list of lock names that will need to be acquired by any
   * request proceeding to access the model, resulting from possible sharing
   * of the target of the storage. <code>null</code> where no locks are required. 
   */
  public StringList restore(WriteableBeanLocator target);
  
  public void preserve(BeanLocator source);
}