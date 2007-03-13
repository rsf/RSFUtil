/*
 * Created on 13-Feb-2006
 */
package uk.org.ponder.rsf.viewstate;

import uk.org.ponder.rsf.view.ViewComponentProducer;

/** Implemented by a {@link ViewComponentProducer} to report the exact class of
 * ViewParameters that it expects to receive when dispatched.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ViewParamsReporter {
  /** Determines the ViewParameters class in use for this view.
   * 
   * @return A ViewParameters object with the same class that this
   *         {@link ViewComponentProducer} expects to receive as its 2nd
   *         argument to {@link ViewComponentProducer#fillComponents}. In general 
   *         applications only the class of this object will be significant and it can 
   *         be default-constructed. However, any fields set on these returned parameters
   *         and not overwritten during parsing will be preserved.
   */
  public ViewParameters getViewParameters();
}
