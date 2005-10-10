/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.Map;

import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.util.ReflectiveCache;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class TokenStateHolder {
 
// a map of String error token IDs to ErrorStateEntries.
// the idea is that this be a timeout cache which is cleared out every
// hour or so... all this functionality to be moved to another class.
  private Map errorcache = ReflectiveCache.getConcurrentMap(16);
  
  public TokenRequestState getTokenState(String tokenID) {
    return (TokenRequestState) errorcache.get(tokenID);
  }
  
  public void putTokenState(TokenRequestState trs) {
    errorcache.put(trs.tokenid, trs);
  }
  
}
