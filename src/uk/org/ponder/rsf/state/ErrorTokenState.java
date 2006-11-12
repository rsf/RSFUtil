/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.state;

import java.io.Serializable;

import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.rsf.request.RequestSubmittedValueCache;

public class ErrorTokenState implements Serializable {
  /** The error token for this state **/
  public String tokenID;
  int redirectcount = 0;
  // We are HERE: If this is a Multi-Request GET, in order to locate the
  // submitting control for errors correctly, we MUST have stored here
  // which it was. 
  public String globaltargetid;
  // These are only stored for an erroneous POST. For a correct one, 
  // they fuse with the StatePreservationStrategy.
  public RequestSubmittedValueCache rsvc;
  public TargettedMessageList messages = new TargettedMessageList();
}
