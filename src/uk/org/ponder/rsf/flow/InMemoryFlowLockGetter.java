/*
 * Created on 15-Jan-2006
 */
package uk.org.ponder.rsf.flow;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.iocevent.ListenerReporter;
import uk.org.ponder.rsf.state.FlowLockGetter;
import uk.org.ponder.rsf.state.TokenLifetimeAware;

/** An application scope bean designed as a FlowLockGetter suitable for
 * a single-JVM application. Will cooperate, for example, 
 * with {@link uk.org.ponder.rsf.flow.BasicFlowAlterationWrapper}
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class InMemoryFlowLockGetter implements FlowLockGetter, TokenLifetimeAware {
  public void setListenerReporter(ListenerReporter reporter) {
    reporter.reportListener(this, TokenLifetimeAware.class, listenertargetkey);
  }

  // Maps flow tokens onto lock objects, while they are active
  private Map flowlockmap = new HashMap();
  public void lifetimeStart(String tokenid) {
    if (!flowlockmap.containsKey(tokenid)) {
      flowlockmap.put(tokenid, new Object());
    }    
  }
  public void lifetimeEnd(String tokenid) {
    flowlockmap.remove(tokenid);
  }
  
  private Object listenertargetkey;
  public void setListenerTargetKey(Object listenertargetkey) {
    this.listenertargetkey = listenertargetkey;
  }
  public Object getFlowLock(String flowtoken) {
    return flowlockmap.get(flowtoken);
  }
  
}
