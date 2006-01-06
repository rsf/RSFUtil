/*
 * Created on 05-Jan-2006
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.rsf.flow.lite.FlowIDHolder;
import uk.org.ponder.rsf.state.FlowLockGetter;
import uk.org.ponder.util.RunnableWrapper;
/** An alteration wrapper designed to prevent simultaneous access to flow-scoped data.
 * Only incurs a synchronized overhead if there is actually an active flow for the
 * current request. If another request in in the flow, this wrapper will simply 
 * block - in practice this is no real burden since this condition could only be triggered
 * by multiple simultaneous requests from the same user/browser. Could be a liability
 * however if under some (possibly failure) condition, normal request handling might take
 * an unbounded time - in this case you may want to replace this wrapper with an implementation
 * performing wait/notify with a timeout.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class FlowAlterationWrapper implements RunnableWrapper {
  private FlowLockGetter flowlockgetter;
  private FlowIDHolder flowidholder;
  public void setFlowLockGetter(FlowLockGetter flowlockgetter) {
    this.flowlockgetter = flowlockgetter;
  }
  public void setFlowIDHolder(FlowIDHolder flowidholder) {
    this.flowidholder = flowidholder;
  }
  
  public Runnable wrapRunnable(final Runnable towrap) {
    String flowtoken = flowidholder.flowtoken;
    final Object flowlock = flowtoken == null? null : flowlockgetter.getFlowLock(flowtoken);
    
    if (flowlock == null) {
      return towrap;
    }
    else return new Runnable() {
      public void run() {
        synchronized(flowlock) {
          towrap.run();
        }
      }
    };
  }

}
