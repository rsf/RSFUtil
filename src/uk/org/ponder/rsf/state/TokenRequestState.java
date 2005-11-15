/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.Date;

import uk.org.ponder.errorutil.TargettedMessageList;

/**
 * Represents ALL the state stored (BETWEEN requests) under a particular
 * token.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class TokenRequestState {
  public Date created = new Date();
  public Date expiry;
  public String tokenid;
  int redirectcount = 0;
  public String globaltargetid;
  // We are HERE: If this is a Multi-Request GET, in order to locate the
  // submitting control for errors correctly, we MUST have stored here
  // which it was. 
  public RequestSubmittedValueCache rsvc;
  public TargettedMessageList errors;
}
