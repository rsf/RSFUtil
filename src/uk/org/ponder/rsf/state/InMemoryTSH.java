/*
 * Created on Aug 5, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import uk.org.ponder.iocevent.ListenerGetter;
import uk.org.ponder.reflect.ReflectiveCache;
import uk.org.ponder.util.Logger;

/**
 * The repository of all inter-request state in RSF. This is held in entries of
 * TokenRequestState in a (probably very large) ConcurrentHashMap which is
 * perpetually expired on a TTL basis. More rapid expiry may occur through
 * explicit session-closing and wizard-ending procedures.
 * <p>
 * High-requirement applications would presumably reimplement this class to push
 * this state into a database or some sort of clustered broadcast.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public class InMemoryTSH implements TokenStateHolder {

  // a map of String error token IDs to ErrorStateEntries.
  // the idea is that this be a timeout cache which is cleared out every
  // hour or so... all this functionality to be moved to another class.
  private Map tokencache = ReflectiveCache.instance.getConcurrentMap(16);

  private int expiryseconds;

  TokenState getTokenStateRaw(String tokenID) {
    return (TokenState) tokencache.get(tokenID);
  }

  private List listeners;
  private ListenerGetter listenergetter;
  private Object targetkey;

  public void setListenerTargetKey(Object targetkey) {
    this.targetkey = targetkey;
  }

  public void setListenerGetter(ListenerGetter listenergetter) {
    this.listenergetter = listenergetter;
  }

  public void init() {
    if (targetkey == null) {
      targetkey = this;
    }
    if (listenergetter != null) {
      listeners = listenergetter.getListeners(TokenLifetimeAware.class,
          targetkey);
    }
  }

  // TODO: The plan is that this thread is JVM-wide, and InMemoryTSH
  // are in charge of attaching and detaching their caches to it on init and
  // destruction. Managing the necessary IPC will be an annoyance. ANDY!!
  private void launchExpirralThread() {

  }

  /**
   * Returns any TokenRequestState object with the specified ID.
   * 
   * @return The required TRS object, or <code>null</code> if none is stored.
   */
  public Object getTokenState(String tokenID) {
    TokenState state = getTokenStateRaw(tokenID);
    return state == null ? null
        : state.payload;
  }

  /** Stores the supplied TokenRequestState object in the repository */
  public void putTokenState(String tokenID, Object payload) {
    if (getTokenStateRaw(tokenID) == null) {
      startLifetime(tokenID);
    }
    TokenState trs = new TokenState();
    trs.payload = payload;
    trs.tokenID = tokenID;
    Date current = new Date();
    long forwardsecs = current.getTime() + expiryseconds * 1000;
    trs.expiry = new Date(forwardsecs);
    tokencache.put(trs.tokenID, trs);
  }

  public void clearTokenState(String tokenID) {
    endLifetime(tokenID);
    Logger.log
        .info("Token state cleared from InMemoryTSH for token " + tokenID);
    tokencache.remove(tokenID);
  }

  private void startLifetime(String tokenID) {
    if (listeners != null) {
      for (int i = 0; i < listeners.size(); ++i) {
        ((TokenLifetimeAware) listeners.get(i)).lifetimeStart(tokenID);
      }
    }
  }

  private void endLifetime(String tokenID) {
    if (listeners != null) {
      for (int i = 0; i < listeners.size(); ++i) {
        ((TokenLifetimeAware) listeners.get(i)).lifetimeEnd(tokenID);
      }
    }
  }

  public void setExpirySeconds(int seconds) {
    this.expiryseconds = seconds;
  }

}
