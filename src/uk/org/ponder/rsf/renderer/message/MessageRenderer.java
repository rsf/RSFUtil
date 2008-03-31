/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.renderer.message;

import java.text.MessageFormat;

import uk.org.ponder.errorutil.CoreMessages;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;

public class MessageRenderer {
  private MessageLocator messagelocator;
  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }
  public static final String NOT_FOUND = "[Message for key {0} not found]";
  
  public String renderDefaultMessage(String key) {
    MessageFormat mf = new MessageFormat(NOT_FOUND);
    return mf.format(new Object[] {key});
  }
  
  public UIMessage renderMessage(UIContainer basecontainer, String id, String key) {
    // attach to base container so that full ID can be computed by default algorithm - RSF-71
    UIMessage togo = UIMessage.make(basecontainer, id == null? "" : id, key);
    String message = messagelocator.getMessage(togo.messagekeys,
        togo.arguments);
    if (message == null) {
      message = renderDefaultMessage(key);
    }
    togo.setValue(message);
    return togo;
  }
  
  
  public UIBranchContainer renderMessageList(TargettedMessageList messagelist) {
    UIBranchContainer togo = new UIBranchContainer();
    boolean hasGeneral = messagelist.findGeneralError() != null;
    
    for (int i = 0; i < messagelist.size(); ++ i) {
      TargettedMessage message = messagelist.messageAt(i);
      String resolved = message.resolve(messagelocator);
      if (resolved == null && !hasGeneral) {
        message = new TargettedMessage(CoreMessages.GENERAL_ACTION_ERROR);
        resolved = message.resolve(messagelocator);
        if (message == null) {
          resolved = renderDefaultMessage(CoreMessages.GENERAL_ACTION_ERROR);
        }
        hasGeneral = true;
      }
      UIOutput.make(togo, "message:" + (message.isError()?"ERROR":"INFO"), resolved);
    }
    return togo;
  }
}
