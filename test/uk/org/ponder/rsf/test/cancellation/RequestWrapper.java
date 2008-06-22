/*
 * Created on 22 Jun 2008
 */
package uk.org.ponder.rsf.test.cancellation;

import uk.org.ponder.util.RunnableInvoker;
import uk.org.ponder.util.UniversalRuntimeException;

public class RequestWrapper implements RunnableInvoker {
  public boolean cancelled = false;
  
  public void invokeRunnable(Runnable torun) {
    try {
      torun.run();
    }
    catch (Exception e) {
      cancelled = true;
      throw UniversalRuntimeException.accumulate(e);
    }
  }

}
