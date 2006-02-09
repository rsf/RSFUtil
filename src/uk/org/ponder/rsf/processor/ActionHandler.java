/*
 * Created on 15-Dec-2005
 */
package uk.org.ponder.rsf.processor;

import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * The interface from the RootHandlerBean to the ActionHandler, responsible
 * for handling for handling an HTTP POST
 * request, or other non-idempotent web service "action" cycle. The default
 * implementation is RSFActionHandler.
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public interface ActionHandler {
  /** Handle this request, and return a ViewParameters object representing
   * the navigation state which should be redirected to when this cycle 
   * finishes.
   * @return The resulting view state, which will be presented to the
   * RenderHandler on the next cycle.
   */
  public ViewParameters handle();
  public ARIResult getARIResult();
}