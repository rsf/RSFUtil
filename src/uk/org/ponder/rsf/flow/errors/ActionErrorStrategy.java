/*
 * Created on Dec 3, 2005
 */
package uk.org.ponder.rsf.flow.errors;

/** Called after each application invocation to determine whether any error
 * strategy will be followed. The implementor may choose to swallow or propagate
 * any exception, generate a new exception, and/or add a targetted message to the
 * ThreadErrorState for the current thread. A list of these will be called in
 * sequence until one is found to return a non-null result.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface ActionErrorStrategy {
  /** Called after each action is invoked on the application during the action
   * cycle. By being a request-scope bean, the implementor of this interface
   * can also intercept any other contextual information they require to make
   * their decision. The default implementation though is StaticActionErrorStrategy
   * which is based on a collection of easily serializable fields.
   * @param returncode The String value returned from the action (may be <code>null</code>)
   * @param exception Any Exception caught on its way out of the action. 
   * The implementor has the choice to swallow this and log it (in which case they 
   * MUST return <code>true</code>, or to repropagate it (almost certainly by
   * means of UniversalRuntimeException.accumulate). This argument may well
   * be <code>null</code>
   * @param flowstate The ID of any flow state which the action cycle is 
   * currently a part of. <code>null</code> if no flow is active.
   * @param viewID The current viewID. Never <code>null</code>
   * @return An action return code representing the new return code for
   * this action cycle, if the error was successfully handled, OR a 
   * TargettedMessage if the error was converted into another error.
   * Else <code>null</code> if the error was not handled by this strategy.
   */
  public Object handleError(String returncode, Exception exception, String flowstate,
      String viewID);
}
