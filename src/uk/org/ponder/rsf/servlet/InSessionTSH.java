/*
 * Created on 28-Feb-2006
 */
package uk.org.ponder.rsf.servlet;

import javax.servlet.http.HttpSession;

import uk.org.ponder.rsf.state.TokenStateHolder;

public class InSessionTSH implements TokenStateHolder {

  private HttpSession session;

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

}
