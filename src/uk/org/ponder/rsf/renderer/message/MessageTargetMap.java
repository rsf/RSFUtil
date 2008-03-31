/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.renderer.message;

import java.util.HashMap;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.template.XMLLump;

public class MessageTargetMap {
  private HashMap map = new HashMap();
  public void setTarget(XMLLump forlump, TargettedMessage message) {
    TargettedMessageList messages = (TargettedMessageList) map.get(forlump);
    if (messages == null) {
      messages = new TargettedMessageList();
      map.put(forlump, messages);
    }
    messages.addMessage(message);
  }
  
  public TargettedMessageList getMessages(XMLLump forlump) {
    return (TargettedMessageList) map.get(forlump);
  }
  
}
