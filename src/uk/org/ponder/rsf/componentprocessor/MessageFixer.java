/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.rsf.componentprocessor;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIMessage;

public class MessageFixer implements ComponentProcessor {

  private MessageLocator messagelocator;

  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }

  public void processComponent(UIComponent toprocesso) {
    if (toprocesso instanceof UIMessage) {
      UIMessage toprocess = (UIMessage) toprocesso;
      toprocess.setValue(messagelocator.getMessage(toprocess.messagekeys,
          toprocess.arguments));
    }
  }

}
