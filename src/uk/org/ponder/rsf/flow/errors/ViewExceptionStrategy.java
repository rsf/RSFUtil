/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.flow.errors;

import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Implemented by a bean representing a strategy for dealing with exceptions
 * which occur during rendering of a view. The bean is expected to return
 * the ViewParameters of a new (and hopefully more valid view) that the
 * client is to be redirected to in the event of this level-1 error. If an
 * error occurs whilst rendering this page, a fatal error will be declared
 * (customisable by {@link uk.org.ponder.rsf.processor.FatalErrorHandler}).
 * </p>
 * A strategy may return <code>null</code> to indicate it defers to some 
 * later strategy.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public interface ViewExceptionStrategy {
  /** 
   * Handle an exception raised from a ViewProducer.
   * @param e The raised exception.
   * @param viewparams The ViewParameters of the view being rendered when the
   * exception was encountered.
   * @return ViewParameters to which the client is to be redirected.
   */
  public AnyViewParameters handleException(Exception e, ViewParameters viewparams);
}
