/*
 * Created on 15 Dec 2006
 */
package uk.org.ponder.rsf.processor;

import java.util.List;

/** Collected target of HandlerHooks within the request **/

public class HandlerHookHandler implements HandlerHook {
  private List handlers;

  public void setHandlers(List handlers) {
    this.handlers = handlers;
  }

  public boolean handle() {
    if (handlers != null) {
      for (int i = 0; i < handlers.size(); ++i) {
        if (((HandlerHook) handlers.get(i)).handle())
          return true;
      }
    }
    return false;
  }

}
