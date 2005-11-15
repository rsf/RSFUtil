/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.errorutil.MessageLocator;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIOutputMultiline;
import uk.org.ponder.stringutil.StringList;

public class MessageRenderer {
  private MessageLocator messagelocator;
  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }
  
  public UIComponent renderMessageList(TargettedMessageList messagelist) {
    UIOutputMultiline togo = new UIOutputMultiline();
    StringList renderered = messagelist.render(messagelocator);
    togo.setValue(renderered);
    return togo;
  }
}
