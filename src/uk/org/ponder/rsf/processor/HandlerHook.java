/*
 * Created on 24-Jan-2006
 */
package uk.org.ponder.rsf.processor;

/** Register a hook in the "handler chain" from the RootBeanHandler.
 * The hook returns <code>true</code> if it has successfully handled this
 * request, in which case further processing should terminate.
 * <p>A HandlerHook should of course in turn defer to any upstream handler. 
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */
// cf. HandlerInterceptor!!
public interface HandlerHook {
  public boolean handle();
  public void setHandlerHook(HandlerHook handlerhook);
}
