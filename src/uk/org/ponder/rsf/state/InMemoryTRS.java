/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.Map;

import uk.org.ponder.util.ReflectiveCache;

/**
 * The repository of all inter-request state in RSF. This is held in entries of
 * TokenRequestState in a (probably very large) ConcurrentHashMap which is
 * perpetually expired on a TTL basis. More rapid expiry may occur through
 * explicit session-closing and wizard-ending procedures.
 * <p> High-requirement applications would presumably reimplement this class
 * to push this state into a database or some sort of clustered broadcast.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class InMemoryTRS implements TokenStateHolder {
 
// a map of String error token IDs to ErrorStateEntries.
// the idea is that this be a timeout cache which is cleared out every
// hour or so... all this functionality to be moved to another class.
  private Map errorcache = ReflectiveCache.getConcurrentMap(16);
  
  /** Returns any TokenRequestState object with the specified ID.
   * @return The required TRS object, or <code>null</code> if none is stored.
   */
  public TokenRequestState getTokenState(String tokenID) {
    return (TokenRequestState) errorcache.get(tokenID);
  }
  
  /** Stores the supplied TokenRequestState object in the repository */
  public void putTokenState(TokenRequestState trs, String aricode) {
    // TODO: use aricode to set expiry time.
    errorcache.put(trs.tokenid, trs);
  }
  
}
