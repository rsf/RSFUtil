/*
 * Created on 1 Jun 2007
 */
package uk.org.ponder.rsf.flow.errors;

import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.util.UniversalRuntimeException;

/** A ViewExceptionStrategy that will "pass through" any errors by rethrowing
 * them. This will upgrade them to the category of fatal errors and hence
 * pass them to a {@link uk.org.ponder.rsf.processor.FatalErrorHandler}.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class PropagatingViewExceptionStrategy implements ViewExceptionStrategy {

  private Class[] propagated;

  public void setPropagatedExceptions(Class[] propagated) {
    this.propagated = propagated;
  }
  
  public ViewParameters handleException(Exception e, ViewParameters incoming) {
    Exception unwrapped = (Exception) UniversalRuntimeException.unwrapException(e);
    if (propagated != null) {
      for (int i = 0; i < propagated.length; ++ i) {
        if (propagated[i].isInstance(unwrapped)) 
          throw UniversalRuntimeException.accumulate(e);
      }
    }
    return null;
  }

}
