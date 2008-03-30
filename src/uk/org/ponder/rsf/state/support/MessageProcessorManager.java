/*
 * Created on 28 Mar 2008
 */
package uk.org.ponder.rsf.state.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.rsf.state.MessageProcessor;

public class MessageProcessorManager implements MessageProcessor {

  private List messageProcessorList;
  
  private Map messageProcessorMap = new HashMap();
  
  public void setMessageProcessorList(List messageProcessorList) {
    this.messageProcessorList = messageProcessorList;
  }

  public void registerMessageProcessor(String path, MessageProcessor processor) {
    messageProcessorMap.put(path, processor);
  }
  
  public String processMessage(TargettedMessage message, String target) {
    List list = new ArrayList();
    if (messageProcessorList != null) {
      list.addAll(messageProcessorList);
    }
    for (Iterator mit = messageProcessorMap.keySet().iterator(); mit.hasNext(); ) {
      String key = (String) mit.next();
      if (message.targetid.startsWith(key)) {
        list.add(messageProcessorMap.get(key));
      }
    }
    String newtarget = null;
    for (int i = 0; i < list.size(); ++ i) {
      MessageProcessor processor = (MessageProcessor) list.get(i);
      newtarget = processor.processMessage(message, target);
    }
    return newtarget;
  }

}
