/*
 * Created on Apr 15, 2006
 */
package uk.org.ponder.rsf.processor;

/**
 * On discovering a bean implementing this interface in the model as the
 * penultimate path component of an action binding, RSF will use this interface
 * to handle the action rather than invoking it reflectively.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */

public interface ActionTarget {
  /**
   * @param actionname The final component of the method binding holding the
   *          action name to be invoked.
   * @param knownresult An "already known" result, possible arrived at through
   *          an ActionErrorStrategy which has acted on a error caused during
   *          the "Apply Request Values".
   * @return The return for the action - either a String code (which should
   *         probably agree with <code>actionname</code> or a fully processed
   *         ARIResult.
   */
  public Object invokeAction(String actionname, String knownresult);
}
