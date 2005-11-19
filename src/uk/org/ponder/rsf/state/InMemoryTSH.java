/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.Date;
import java.util.Map;

import uk.org.ponder.reflect.ReflectiveCache;

/**
 * The repository of all inter-request state in RSF. This is held in entries of
 * TokenRequestState in a (probably very large) ConcurrentHashMap which is
 * perpetually expired on a TTL basis. More rapid expiry may occur through
 * explicit session-closing and wizard-ending procedures.
 * <p> High-requirement applications would presumably reimplement this class
 * to push this state into a database or some sort of clustered broadcast.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class InMemoryTSH implements TokenStateHolder {
 
// a map of String error token IDs to ErrorStateEntries.
// the idea is that this be a timeout cache which is cleared out every
// hour or so... all this functionality to be moved to another class.
  private Map tokencache = ReflectiveCache.instance.getConcurrentMap(16);
  
  private int expiryseconds;
  
  /** Returns any TokenRequestState object with the specified ID.
   * @return The required TRS object, or <code>null</code> if none is stored.
   */
  public TokenState getTokenState(String tokenID) {
    return (TokenState) tokencache.get(tokenID);
  }
  
  /** Stores the supplied TokenRequestState object in the repository */
  public void putTokenState(TokenState trs) {
    Date current = new Date();
    long forwardsecs = current.getTime() + expiryseconds;
    trs.expiry = new Date(forwardsecs);
    tokencache.put(trs.tokenID, trs);
  
  }

  public void clearTokenState(String tokenID) {
    tokencache.remove(tokenID);
  }
  
  public void setExpirySeconds(int seconds) {
    this.expiryseconds = seconds;
  }
  
}
