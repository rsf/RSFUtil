/*
 * Created on 28-Feb-2006
 */
package uk.org.ponder.rsf.servlet;

import javax.servlet.http.HttpSession;

import uk.org.ponder.rsf.state.TokenStateHolder;

/** A TokenStateHolder that stores flow state in the HTTP Session.
 *
 * <p>This is, unexpectedly, an application scope bean. The HttpSession object
 * stored inside is an AOP alliance proxy for the actual request-scope session.
 * It is app-scope since alternative TSH implementations are also app-scope,
 * and cross-scope overriding is not supported. Also having one less request bean
 * is desirable.
 * <p>NB Expiryseconds not yet implemented. Would require *extra* server-side
 * storage of map of tokens to sessions, in order to save long-term storage 
 * within sessions - awaiting research from performance clients.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class InSessionTSH implements TokenStateHolder {
 // NB - this is a proxy of the request-scope session!!!
  private HttpSession session;
  private int expiryseconds;

  public void setSession(HttpSession session) {
    this.session = session;
  }
  
  public Object getTokenState(String tokenID) {
    return session.getAttribute(tokenID);
  }

  public void putTokenState(String tokenID, Object trs) {
    session.setAttribute(tokenID, trs);
  }

  public void clearTokenState(String tokenID) {
    session.removeAttribute(tokenID);
  }

  public void setExpirySeconds(int seconds) {
    this.expiryseconds = seconds;
  }
  
}
