/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.errorutil.TargettedMessageList;

public class ErrorTokenState extends TokenState{
  int redirectcount = 0;
  // We are HERE: If this is a Multi-Request GET, in order to locate the
  // submitting control for errors correctly, we MUST have stored here
  // which it was. 
  public String globaltargetid;
  // These are only stored for an erroneous POST. For a correct one, 
  // they fuse with the StatePreservationStrategy.
  public RequestSubmittedValueCache rsvc;
  public TargettedMessageList errors;
}