/*
 * Created on 22 Jul 2006
 */
package uk.org.ponder.rsf.state.scope;

import java.util.Map;

import org.springframework.beans.factory.BeanNameAware;

import uk.org.ponder.rsf.state.TokenStateHolder;
import uk.org.ponder.stringutil.StringList;

/** Manages a set of beans within a named scope. The beans will be 
 * automatically preserved to the nominated TokenStateHolder at the end of
 * every action request, and restored at the beginning of every request.
 * 
 * The beans in storage will expire on the natural expiry of the TSH, or on
 * triggering of destruction through one of the <code>destroy</code> methods
 * on this bean, whichever is sooner.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ScopedBeanManager implements BeanDestroyer, BeanNameAware {
  private String scopeName;
  private String copyPreservingBeans;

  private TokenStateHolder tokenStateHolder;
  private Map destroyed;
  private boolean exclusive;
  private boolean alwaysPreserve;

  public void setDestroyedScopeMap(Map destroyed) {
    this.destroyed = destroyed;
  }
  
  public void destroy() {
    getTokenStateHolder().clearTokenState(scopeName);
    destroyed.put(scopeName, scopeName);
  }

  public void setScopeName(String scopeName) {
    this.scopeName = scopeName;
  }
  public String getScopeName() {
    return scopeName;
  }

  /** Set to <code>true</code> if multiple requests are to be prevented
   * from accessing this scope simultaneously. A second request trying to
   * access the same scope will block in its alterationWrapper until 
   * the first has concluded. 
   */
  public void setExclusive(boolean exclusive) {
    this.exclusive = exclusive;
  }
  
  public boolean getExclusive() {
    return exclusive;
  }
  /** Set to <code>true</code> if this is a "bad" kind of scope (with the
   * potential to violate HTTP semanatics) that requires to be preserved
   * on <code>RENDER</code> cycles as well as <code>ACTION</code> cycles.
   */
  public void setAlwaysPreserve(boolean alwaysPreserve) {
    this.alwaysPreserve = alwaysPreserve;
  }
  
  public boolean getAlwaysPreserve() {
    return alwaysPreserve;
  }
  
  public void setCopyPreservingBeans(String copyPreservingBeans) {
    this.copyPreservingBeans = copyPreservingBeans;
  }

  public StringList getCopyPreservingBeanList() {
    return StringList.fromString(copyPreservingBeans);
  }

  public void setTokenStateHolder(TokenStateHolder tokenStateHolder) {
    this.tokenStateHolder = tokenStateHolder;
  }

  public TokenStateHolder getTokenStateHolder() {
    return tokenStateHolder;
  }

  public void setBeanName(String name) {
    scopeName = name;
  }

}
