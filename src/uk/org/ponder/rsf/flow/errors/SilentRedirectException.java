/*
 * Created on 5 Feb 2007
 */
package uk.org.ponder.rsf.flow.errors;

import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.RedirectViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsInterceptor;

/** Thrown from a render cycle in order to cause a "silent" (that is, with no 
 * logging, and with no allocation of error token) redirection to a different
 * view, rather than a rendered result. If no target view is specified in the
 * constructor, the resulting view will be the default computed by the
 * {@link ViewExceptionStrategy}. This is thrown in the event no 
 * template can be found for the decoded view, and may also be thrown directly
 * from producer code. If in a {@link ViewParamsInterceptor}, 
 * use {@link RedirectViewParameters} instead.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class SilentRedirectException extends RuntimeException {
  public AnyViewParameters redirectTo;
  public SilentRedirectException() {}
  public SilentRedirectException(AnyViewParameters redirectTo) {
    this.redirectTo = redirectTo;
  }
}
