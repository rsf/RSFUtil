/*
 * Created on 5 Jul 2006
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.streamutil.write.PrintOutputStream;

/** A bean handling "double-fault" rendering errors implements this
 * interface. A double-fault is declared when an unexpected exception is
 * thrown rendering the default view for an application, when this view
 * is already being rendered as a result of a redirect from a level-1 error.
 * This redirect condition is by default signalled by setting the 
 * <code>errorredirect</code> request attribute.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface FatalErrorHandler {
  /** A return code from FatalErrorHandler indicating that this exception 
   * should be propagated to a handler outside the RSF system.
   */
  public static final String HANDLE_EXCEPTION_UPSTAIRS = "Handle Exception Upstairs";
  
  /** A return code from FatalErrorHandler indicating that this exception
   * has been correctly handled.
   */
  public static final String HANDLED = "Handled";
  
  /** Attempt to handle the supplied fatal error, by, if possible, rendering
   * a suitable error message. If the implementation returns <code>false</code>
   * or throws any kind of exception, the hard-wired RSF default message will
   * be printed instead.
   * @param t The exception causing the double fault.
   * @param pos A PrintOutputStream where an error message may be rendered.
   * @return One of the above Strings if the error has been successfully handled,
   * or <code>null</code> if it has not.
   */
  public String handleFatalError(Throwable t, PrintOutputStream pos);
}
