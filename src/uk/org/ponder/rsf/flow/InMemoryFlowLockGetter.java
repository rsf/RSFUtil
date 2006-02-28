/*
 * Created on 15-Jan-2006
 */
package uk.org.ponder.rsf.flow;

import java.util.HashMap;
import java.util.Map;

import uk.org.ponder.rsf.state.FlowLockGetter;

/** An application scope bean designed as a FlowLockGetter suitable for
 * a single-JVM application. Will cooperate, for example, 
 * with {@link uk.org.ponder.rsf.flow.BasicFlowAlterationWrapper}
 * UNFINISHED - this implementation locks unnecessarily, do not use in
 * performance applications.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class InMemoryFlowLockGetter implements FlowLockGetter { 
  // Maps flow tokens onto lock objects, while they are active
  private Map flowlockmap = new HashMap();
   
  public Object getFlowLock(String flowtoken) {
    
  // TODO: this needs ReflectiveCache upgraded to return a shielded version
  // of either 1.5 ConcurrentHashMap or backport version, so we can use the
  // putIfAbsent method.
    synchronized(flowlockmap) {
    Object lock = flowlockmap.get(flowtoken);
    if (lock != null) {
      lock = new Object();
      flowlockmap.put(flowtoken, lock);
    }
    return lock;
    }
  }
    
  public void returnFlowLock(String flowtoken) {
    flowlockmap.remove(flowtoken);
  }
}
