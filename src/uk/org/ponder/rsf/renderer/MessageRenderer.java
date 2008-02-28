/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.renderer;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.stringutil.StringList;

public class MessageRenderer {
  private MessageLocator messagelocator;
  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }
  
  public UIMessage renderMessage(UIContainer basecontainer, String id, String key) {
    // attach to base container so that full ID can be computed by default algorithm - RSF-71
    UIMessage togo = UIMessage.make(basecontainer, id == null? "" : id, key);
    togo.setValue(messagelocator.getMessage(togo.messagekeys,
        togo.arguments));
    return togo;
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
