/*
 * Created on 1 Apr 2008
 */
package uk.org.ponder.rsf.renderer.message;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;

/** This "flyweight" structure is required to render each message batch, because of the
 * intertwined workflows of MessageTargetter and BranchResolver. We cannot really
 * supply the correct branch structure ahead of time, since all messages resolved have
 * not yet been seen in the template.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class MessageFlyweight {
  public static final String RSF_MESSAGES = "rsf-messages:";
  public UIBranchContainer rsfMessages;
  
  public UIBranchContainer errorMessages;
  public UIBranchContainer infoMessages;
  public UIBranchContainer confirmMessages;
  
  public MessageFlyweight(UIContainer parent) {
    rsfMessages = UIBranchContainer.make(parent, RSF_MESSAGES);
    errorMessages = UIBranchContainer.make(rsfMessages, "error-messages:");
    infoMessages = UIBranchContainer.make(rsfMessages, "info-messages:");
    confirmMessages = UIBranchContainer.make(rsfMessages, "confirm-messages:");
  }
  
  public void detachAll() {
    rsfMessages.remove(errorMessages);
    rsfMessages.remove(infoMessages);
    rsfMessages.remove(confirmMessages);
  }
}
