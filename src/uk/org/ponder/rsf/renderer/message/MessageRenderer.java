/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.renderer.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.ponder.arrayutil.MapUtil;
import uk.org.ponder.errorutil.CoreMessages;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.util.UniversalRuntimeException;

/** Renders {@link TargettedMessage} and {@link TargettedMessageList} items into 
 * RSF component trees. 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class MessageRenderer {
  private MessageLocator messagelocator;
  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }

  private boolean productionMode;
  
  public void setProductionMode(boolean productionMode) {
    this.productionMode = productionMode;
  }
  
  
  /** Render a template message into an "ad hoc" UIMessage component **/
  public UIMessage renderMessage(UIContainer basecontainer, String id, String key) {
    // attach to base container so that full ID can be computed by default algorithm - RSF-71
    UIMessage togo = UIMessage.make(basecontainer, id == null? "" : id, key);
    
    String message = messagelocator.getMessage(togo.messagekeys,
        togo.arguments);
    if (message == null) {
      message = MessageUtil.renderDefaultMessage(key);
    }
    togo.setValue(message);
    return togo;
  }
  
  /** Renders a {@link TargettedMessageList} into a structured form in the components tree,
   * in such a way as it will peer with the builtin/Messages.html template file or a 
   * user-contributed replacement. Messages of different severities are separated into
   * different branches.
   */
  
  public UIBranchContainer renderMessageList(TargettedMessageList messageList) {
    UIBranchContainer togo = new UIBranchContainer();
    Map severityMap = condenseList(messageList);
    renderSeverityMessages(togo, severityMap, TargettedMessage.SEVERITY_ERROR, "error-messages:");
    renderSeverityMessages(togo, severityMap, TargettedMessage.SEVERITY_INFO, "info-messages:");
    renderSeverityMessages(togo, severityMap, TargettedMessage.SEVERITY_CONFIRM, "confirm-messages:");
    return togo;
  }
  
  private Map condenseList(TargettedMessageList messagelist) {
   Map togo = new HashMap();
   boolean hasGeneral = messagelist.findGeneralError() != null;
    
    for (int i = 0; i < messagelist.size(); ++ i) {
      TargettedMessage message = messagelist.messageAt(i);
      boolean placeholder = message.message == CoreMessages.RAW_EXCEPTION_PLACEHOLDER;
      
      if (placeholder && message.exception != null) {
        message.message = UniversalRuntimeException.unwrapException(message.exception).getMessage();
      }
      String resolved = message.resolve(messagelocator);
      if (resolved == null && message.isError()) {
        if (productionMode && placeholder) {
          if (!hasGeneral ) {
            // if in production mode, and the message arose through raw exception conversion,
            // convert it to at most one instance of G_A_E
            message = new TargettedMessage(CoreMessages.GENERAL_ACTION_ERROR);
            hasGeneral = true;
          }
          else {
            message = null;
          }
        }
      }
      if (message != null) {
        MapUtil.putMultiMap(togo, new Integer(message.severity), message);
      }
    }
    return togo;
  }
  
  
  private void renderSeverityMessages(UIBranchContainer tofill, Map severityMap, int severity, String branchId) {
    renderMessages(tofill, (List) severityMap.get(new Integer(severity)), branchId);
  }


  public void renderMessages(UIBranchContainer tofill, List messagelist, String branchId) {
    if (messagelist == null || messagelist.size() == 0) return;
    UIBranchContainer togo = UIBranchContainer.make(tofill, branchId);
    
    for (int i = 0; i < messagelist.size(); ++ i) {
      TargettedMessage message = (TargettedMessage) messagelist.get(i);
      String resolved = message.resolve(messagelocator);
      if (resolved == null) {
        resolved = MessageUtil.renderDefaultMessage(message.messagecodes[0]);
      }
      UIOutput.make(togo, "message:" , resolved);
    }
  }
}
