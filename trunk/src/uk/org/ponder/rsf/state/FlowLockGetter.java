/*
 * Created on 05-Jan-2006
 */
package uk.org.ponder.rsf.state;

/** If a given flow is in progress, returns a unique object that may be used as a lock.
 * Used by the FlowAlterationWrapper in order to prevent simultaneous requests being
 * served within the same flow.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public interface FlowLockGetter {
  public Object getFlowLock(String flowtoken);
  public void returnFlowLock(String flowtoken);
}
