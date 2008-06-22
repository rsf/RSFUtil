/*
 * Created on 8 Jan 2008
 */
package uk.org.ponder.rsf.test.cancellation;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

/** 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class Canceller {

  private TargettedMessageList targettedMessageList;
  
  /**
   * @param targettedMessageList the targettedMessageList to set
   */
  public void setTargettedMessageList(TargettedMessageList targettedMessageList) {
    this.targettedMessageList = targettedMessageList;
  }

  public void act() {
    targettedMessageList.addMessage(new TargettedMessage("Request has been cancelled"));
  }
}
