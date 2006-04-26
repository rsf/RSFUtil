/*
 * Created on 25-Jan-2006
 */
package uk.org.ponder.rsf.processor;

public class NullHandlerHook implements HandlerHook {

  public boolean handle() {
    return false;
  }

  public void setHandlerHook(HandlerHook handlerhook) {
  }

}
