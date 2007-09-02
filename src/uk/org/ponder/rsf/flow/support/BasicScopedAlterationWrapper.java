/*
 * Created on 05-Jan-2006
 */
package uk.org.ponder.rsf.flow.support;

import java.util.Collections;

import uk.org.ponder.rsf.flow.FlowIDHolder;
import uk.org.ponder.rsf.state.LockGetter;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.RunnableInvoker;

/**
 * An alteration wrapper designed to prevent simultaneous access to flow or
 * scoped data. Only incurs a synchronized overhead if there is actually an
 * active flow or scope for the current request, i.e. in the case of a scope, it
 * successfully restored a bean from the scope. If another request in in the
 * flow, this wrapper will simply block - in practice this is no real burden
 * since this condition could only be triggered by multiple simultaneous
 * requests from the same user/browser. Could be a liability however if under
 * some (possibly failure) condition, normal request handling might take an
 * unbounded time - in this case you may want to replace this wrapper with an
 * implementation performing wait/notify with a timeout.
 * <p>
 * If operating in a clustered environment where handling of different requests
 * belonging to the same flow cannot be guaranteed to be passed to the same JVM,
 * you will certainly require to replace this implementation.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */

public class BasicScopedAlterationWrapper implements RunnableInvoker {
  private LockGetter lockgetter;
  private FlowIDHolder flowidholder;
  private StringList scopelocks;

  public void setLockGetter(LockGetter flowlockgetter) {
    this.lockgetter = flowlockgetter;
  }

  public void setFlowIDHolder(FlowIDHolder flowidholder) {
    this.flowidholder = flowidholder;
  }

  public void setScopeLocks(StringList scopelocks) {
    this.scopelocks = scopelocks;
  }

  public void invokeRunnable(Runnable towrap) {
    String flowtoken = flowidholder.getFlowToken();
    final StringList completelocks = new StringList();
    if (flowtoken != null) {
      completelocks.add(flowtoken);
    }
    completelocks.addAll(scopelocks);
    if (completelocks.size() == 0) {
      towrap.run();
      return;
    }
    Collections.sort(completelocks); // Avoid potential Dedlocks!
    try {
      lockUUPP(completelocks, towrap, 0);
    }
    finally {
      lockgetter.returnLock(flowtoken);
    }
  }

  private void lockUUPP(StringList completelocks, Runnable towrap, int i) {
    if (i >= completelocks.size()) {
      towrap.run();
    }
    else {
      String lockname = completelocks.stringAt(i);
      Object lock = lockgetter.getLock(lockname);
      Logger.log.info("Acquiring lock " + lock + " for scope name " + lockname);
      synchronized (lock) {
        lockUUPP(completelocks, towrap, i + 1);
      }
    }
  }

}
