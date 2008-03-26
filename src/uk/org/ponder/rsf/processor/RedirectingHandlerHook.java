/*
 * Created on 26 Mar 2008
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.rsf.viewstate.AnyViewParameters;

/** 
 * A version of {@link HandlerHook} which signals handling by returning a ViewParameters
 * instance to be interpreted as a redirect, rather than a boolean.  
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface RedirectingHandlerHook {
  /** A ViewParameters instance to be interpreted as a redirect, if the request was 
   * handled by this HandlerHook, else <code>null</code> 
   */
  public AnyViewParameters handle();
}
