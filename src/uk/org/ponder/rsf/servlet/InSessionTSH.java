/*
 * Created on 28-Feb-2006
 */
package uk.org.ponder.rsf.servlet;

import javax.servlet.http.HttpServletRequest;
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
 // NB - this is a proxy of the request
  private HttpServletRequest request;
  private int expiryseconds;

  public void setHttpRequest(HttpServletRequest request) {
    this.request = request;
  }
  
  public Object getTokenState(String tokenID) {
    HttpSession session = request.getSession(false);
    return session == null? null : session.getAttribute(tokenID);
  }

  public void putTokenState(String tokenID, Object trs) {
    HttpSession session = request.getSession(true);
    session.setAttribute(tokenID, trs);
  }

  public void clearTokenState(String tokenID) {
    try {
      HttpSession session = request.getSession(false);
      session.removeAttribute(tokenID);
    }
    catch (Exception e) {
      // We really don't care if the "session has been invalidated".
    }
  }

  public void setExpirySeconds(int seconds) {
    this.expiryseconds = seconds;
  }

  public String getId() {
    HttpSession session = request.getSession(false);
    return session == null? null : session.getId();
  }
  
}
