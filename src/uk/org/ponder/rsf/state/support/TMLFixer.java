/*
 * Created on 28 Mar 2008
 */
package uk.org.ponder.rsf.state.support;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.request.RequestSubmittedValueCache;
import uk.org.ponder.rsf.request.SubmittedValueEntry;
import uk.org.ponder.rsf.state.MessageProcessor;
import uk.org.ponder.util.Logger;

public class TMLFixer {

  private MessageProcessor messageProcessorManager;
  
  /**
   * @param messageProcessorManager the messageProcessorManager to set
   */
  public void setMessageProcessorManager(MessageProcessor messageProcessorManager) {
    this.messageProcessorManager = messageProcessorManager;
  }

  // Convert errors which are currently referring to bean paths back onto
  // their fields as specified in RSVC. Called at the END of a POST cycle.
  public void fixupMessages(TargettedMessageList tml,
      RequestSubmittedValueCache rsvc) {

    for (int i = 0; i < tml.size(); ++i) {
      TargettedMessage tm = tml.messageAt(i);
      if (!tm.targetid.equals(TargettedMessage.TARGET_NONE)) {
        // Target ID refers to bean path. We need somewhat more flexible
        // rules for locating a component ID, which is defined as something
        // which follows "message-for". These IDs may actually be "synthetic",
        // at a particular level of containment, in that they refer to a
        // specially
        // instantiated genuine component which has the same ID.
        SubmittedValueEntry sve = rsvc.byPath(tm.targetid);
        String rewritten = TargettedMessage.TARGET_NONE;
        if (sve != null && sve.componentid == null) {
          sve = rsvc.getUpstreamComponent(sve.valuebinding);
        }
        if (sve == null || sve.componentid == null) {
          // TODO: We want to trace EL reference chains BACKWARDS to figure
          // "ultimate source" of erroneous data. For now we will default to
          // TARGET_NONE
          Logger.log.warn("Message queued for non-component path "
              + tm.targetid);
        }
        else {
          rewritten = sve.componentid;
        }
        String newtarget = messageProcessorManager.processMessage(tm, rewritten);
        if (newtarget != null) {
          rewritten = newtarget;
        }
        tm.targetid = rewritten;
      }
    }
  }
  
}
