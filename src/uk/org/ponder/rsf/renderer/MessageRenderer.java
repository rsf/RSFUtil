/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.stringutil.StringList;

public class MessageRenderer {
  private MessageLocator messagelocator;
  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }
  
  public UIBranchContainer renderMessageList(TargettedMessageList messagelist) {
    UIBranchContainer togo = new UIBranchContainer();
    StringList renderered = messagelist == null? new StringList() : 
      messagelist.render(messagelocator);
    for (int i = 0; i < renderered.size(); ++ i) {
      UIOutput.make(togo, "message:", renderered.stringAt(i));
    }
    return togo;
  }
}
