/*
 * Created on 24-Jan-2006
 */
package uk.org.ponder.rsf.processor;

/** Register a hook in the "handler chain" from the RootBeanHandler.
 * The hook returns <code>true</code> if it has successfully handled this
 * request, in which case further processing should terminate.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */
// cf. HandlerInterceptor!!
public interface HandlerHook {
  public boolean handle();
}
