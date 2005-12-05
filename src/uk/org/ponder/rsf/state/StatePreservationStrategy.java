/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.WriteableBeanLocator;
/** A core RSF interface specifying a "strategy" for saving and restoring
 * segments of a request-scope bean model. In general each strategy will be
 * separately configured in terms of i) which bean paths it acts on, and
 * ii) which target storage the state will be preserved in (generally
 * a TokenStateHolder). These strategies are managed by an app-global
 * {@link StatePreservationManager}
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface StatePreservationStrategy {
  public void preserve(BeanLocator source, String tokenid);
  public void restore(WriteableBeanLocator target, String tokenid);
  public void clear(String tokenid);
}
