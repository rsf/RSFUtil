/*
 * Created on 15-Jan-2006
 */
package uk.org.ponder.rsf.state;

import java.util.HashMap;
import java.util.Map;

/**
 * An application scope bean designed as a FlowLockGetter suitable for a
 * single-JVM application. Will cooperate, for example, with
 * {@link uk.org.ponder.rsf.flow.BasicScopedAlterationWrapper}
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */

public class InMemoryLockGetter implements LockGetter {
  // Maps flow tokens onto lock objects, while they are active
  private Map flowlockmap = new HashMap();

  public Object getLock(String flowtoken) {
    // It appears it is completely impossible to avoid a genuine lock here, so
    // no point waiting for a Doug Lea solution. The point is that we expect
    // that by far the most likely case is where there is a single lock, and
    // the map is empty.
    synchronized (flowlockmap) {
      Object lock = flowlockmap.get(flowtoken);
      if (lock == null) {
        lock = new Object();
        flowlockmap.put(flowtoken, lock);
      }
      return lock;
    }
  }

  public void returnLock(String flowtoken) {
    flowlockmap.remove(flowtoken);
  }
}
