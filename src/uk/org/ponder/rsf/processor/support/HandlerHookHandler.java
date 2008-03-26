/*
 * Created on 15 Dec 2006
 */
package uk.org.ponder.rsf.processor.support;

import java.util.List;

import uk.org.ponder.rsf.processor.HandlerHook;
import uk.org.ponder.rsf.processor.RedirectingHandlerHook;
import uk.org.ponder.rsf.viewstate.AnyViewParameters;
import uk.org.ponder.rsf.viewstate.NoViewParameters;

/** Collected target of HandlerHooks within the request **/

public class HandlerHookHandler implements RedirectingHandlerHook {
  private List handlers;
  private List redirectingHandlers;

  public void setHandlers(List handlers) {
    this.handlers = handlers;
  }
  
  public void setRedirectingHandlers(List redirectingHandlers) {
    this.redirectingHandlers = redirectingHandlers;
  }

  public AnyViewParameters handle() {
    if (handlers != null) {
      for (int i = 0; i < handlers.size(); ++i) {
        if (((HandlerHook) handlers.get(i)).handle())
          return NoViewParameters.instance;
      }
    }
    if (redirectingHandlers != null) {
      for (int i = 0; i < redirectingHandlers.size(); ++i) {
        AnyViewParameters redirect = (((RedirectingHandlerHook) redirectingHandlers.get(i)).handle());
        if (redirect != null) {
          return redirect;
        }
      }
    }
    return null;
  }

}
