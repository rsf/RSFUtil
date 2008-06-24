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
import uk.org.ponder.rsf.components.UIComponent;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;

/**
 * Renders {@link TargettedMessage} and {@link TargettedMessageList} items into RSF
 * component trees.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class MessageRenderer {
  private MessageLocator messagelocator;

  public void setMessageLocator(MessageLocator messagelocator) {
    this.messagelocator = messagelocator;
  }

  /** Render a template message into an "ad hoc" UIMessage component * */
  public UIMessage renderMessage(UIContainer basecontainer, String id, String key) {
    // attach to base container so that full ID can be computed by default algorithm -
    // RSF-71
	String addId = id == null? "" : id;
	UIComponent existing = basecontainer.getComponent(addId);
	if (existing != null) {
      basecontainer.remove(existing);
	}
    UIMessage togo = UIMessage.make(basecontainer, addId, key);

    String message = messagelocator.getMessage(togo.messagekeys, togo.arguments);
    if (message == null) {
      message = MessageUtil.renderDefaultMessage(key);
    }
    togo.setValue(message);
    return togo;
  }

  private Map condenseList(TargettedMessageList messagelist) {
    Map togo = new HashMap();
    boolean doneGeneral = false;

    for (int i = 0; i < messagelist.size(); ++i) {
      TargettedMessage message = messagelist.messageAt(i);
      boolean isGeneral = CoreMessages.GENERAL_ACTION_ERROR.equals(message.acquireMessageCode());
      if (isGeneral) {
        if (doneGeneral) {
          message = null;
        }
        else {
          doneGeneral = true;
        }
      }
     
      if (message != null) {
        MapUtil.putMultiMap(togo, new Integer(message.severity), message);
      }
    }
    return togo;
  }

  /**
   * Renders a {@link TargettedMessageList} into a structured form in the components tree,
   * in such a way as it will peer with the builtin/Messages.html template file or a
   * user-contributed replacement. Messages of different severities are separated into
   * different branches.
   */

  public void renderMessageList(UIContainer basecontainer, MessageFlyweight flyweight,
      TargettedMessageList messageList) {
    flyweight.detachAll();
    
    Map severityMap = condenseList(messageList);
    renderSeverityMessages(flyweight, flyweight.errorMessages, severityMap,
        TargettedMessage.SEVERITY_ERROR);
    renderSeverityMessages(flyweight, flyweight.infoMessages, severityMap,
        TargettedMessage.SEVERITY_INFO);
    renderSeverityMessages(flyweight, flyweight.confirmMessages, severityMap,
        TargettedMessage.SEVERITY_CONFIRM);
    
  }

  private void renderSeverityMessages(MessageFlyweight flyweight,
      UIBranchContainer member, Map severityMap, int severity) {
    renderMessages(flyweight, member, (List) severityMap.get(new Integer(severity)));
  }

  public void renderMessages(MessageFlyweight flyweight, UIBranchContainer member,
      List messagelist) {
    if (messagelist == null || messagelist.size() == 0)
      return;
    flyweight.rsfMessages.addComponent(member);

    for (int i = 0; i < messagelist.size(); ++i) {
      TargettedMessage message = (TargettedMessage) messagelist.get(i);
      String resolved = message.resolve(messagelocator);
      if (resolved == null) {
        resolved = MessageUtil.renderDefaultMessage(message.messagecodes[0]);
      }
      UIOutput.make(member, "message:", resolved);
    }
  }
}
