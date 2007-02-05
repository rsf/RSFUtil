/*
 * Created on 5 Feb 2007
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/** A "user-level" interface for clients who need more fine-grained control
 * over allocating outgoing URL state in action cycles.
 * 
 * Unlike {@link ActionResultInterpreter}, ALL registered ActionResultInterceptors
 * are polled during the cycle. Also, a ViewProducer may directly implement
 * ActionResultInterceptor to indicate it is happy to be constructed during
 * an action cycle to serve this role.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface ActionResultInterceptor {
  /**
   * @param result The required result from this action cycle, which is in the
   * process of determination. May already contain some determined URL state,
   * for example a ViewParams with viewId, but may be overwritten by this
   * interceptor.
   * @param incoming The URL state for the view which generated this 
   * action cycle.
   * @param actionReturn The return from any action method invoked on this
   * cycle. Should be a String, although may be <code>null</code> if there was
   * no return or no method.
   */
  public void interceptActionResult(ARIResult result, ViewParameters incoming, 
     Object actionReturn);
}
