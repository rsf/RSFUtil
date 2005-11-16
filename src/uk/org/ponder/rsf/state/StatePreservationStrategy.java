/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.WriteableBeanLocator;

public interface StatePreservationStrategy {
 
  public void preserve(BeanLocator source, String tokenid);
  public void restore(WriteableBeanLocator target, String tokenid);
  public void clear(String tokenid);
}
