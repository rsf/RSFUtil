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
  public String tokenid;
  int redirectcount = 0;
  
  public RequestSubmittedValueCache rsvc;
  public TargettedMessageList errors;
}
