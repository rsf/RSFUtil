/*
 * Created on Dec 3, 2005
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/** A simple, serializable implementation of the ActionErrorStrategy interface.
 * Any of the fields that are non-null will be matched against the information
 * incoming to the interface.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class StaticActionErrorStrategy implements ActionErrorStrategy {
  public String returncode;
  public Class exceptionclass;
  public String flowstate;
  public String viewID;

  public boolean propagateexception;
  public String messagetarget;
  public String messagekey;

  public boolean handleError(String returncode, Exception exception,
      String flowstate, String viewID) {
    if ((this.returncode == null || this.returncode.equals(returncode))
        && (this.exceptionclass == null || exception == null || exception
            .getClass().isAssignableFrom(exceptionclass))
        && (this.flowstate == null || this.flowstate.equals(flowstate))
        && (this.viewID == null || this.viewID == viewID)) {
      if (messagekey != null) {
        TargettedMessage tmessage = new TargettedMessage(messagekey,
            messagetarget);
        ThreadErrorState.addError(tmessage);
        if (exception != null) {
          if (propagateexception) {
            throw UniversalRuntimeException.accumulate(exception);
          }
          else {
            Logger.log.warn("Error invoking action", exception);
          }
        }
        return true;
      }
    }
    return false;
  }

}
