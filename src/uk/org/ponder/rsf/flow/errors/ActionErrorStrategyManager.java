/*
 * Created on Dec 3, 2005
 */
package uk.org.ponder.rsf.flow.errors;

import java.util.ArrayList;
import java.util.List;

import uk.org.ponder.errorutil.CoreMessages;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * A collection point for ActionErrorStrategies. Tries each strategy in turn,
 * and if none match the criteria for the current error, adopts a default
 * strategy. This passes through any exception, and queues a general error
 * message.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class ActionErrorStrategyManager implements ActionErrorStrategy {
  private List strategies = new ArrayList();
  private TargettedMessageList messages;

  public void setMergeStrategies(ActionErrorStrategyManager strategies) {
    this.strategies.addAll(strategies.getStrategies());
  }

  public void setStrategyList(List newstrategies) {
    this.strategies.addAll(newstrategies);
  }

  public void addStrategy(ActionErrorStrategy toadd) {
    strategies.add(toadd);
  }

  public ActionErrorStrategy strategyAt(int i) {
    return (ActionErrorStrategy) strategies.get(i);
  }

  public List getStrategies() {
    return strategies;
  }
  
  public void setTargettedMessageList(TargettedMessageList tml) {
    this.messages = tml;
  }

  public Object handleError(String returncode, Exception exception,
      String flowstate, String viewID, TargettedMessage message) {
    Object code = null;
    Throwable tohandlet = UniversalRuntimeException.unwrapException(exception);
    if (tohandlet != null && !(tohandlet instanceof Exception)) {
      // If it is an Error, throw it out immediately
      throw UniversalRuntimeException.accumulate(tohandlet);
    }
    Exception tohandle = (Exception) tohandlet;
    for (int i = 0; i < strategies.size(); ++i) {
      code = strategyAt(i).handleError(returncode, tohandle, flowstate, viewID,
          message);
      if (code != null)
        return code;
    }
    if (exception != null && code == null) {
      // Logger.log.warn("Error invoking action", exception);
      if (!messages.isError()) {
        if (findGeneralError(messages) == null) {
        messages.addMessage(new TargettedMessage(
            CoreMessages.GENERAL_ACTION_ERROR, new Object[] { "token-placeholder" }, exception));
        }
      }
      throw UniversalRuntimeException.accumulate(exception,
          "Error invoking action");
    }
    return null;
  }

  public static TargettedMessage findGeneralError(TargettedMessageList messages) {
    for (int i = 0; i < messages.size(); ++ i) {
      TargettedMessage message = messages.messageAt(i); 
      if (CoreMessages.GENERAL_ACTION_ERROR.equals(message.message)) return message;
    }
    return null;
  }

}
