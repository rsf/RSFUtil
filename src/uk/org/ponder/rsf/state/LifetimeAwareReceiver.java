/*
 * Created on 15-Jan-2006
 */
package uk.org.ponder.rsf.state;

public interface LifetimeAwareReceiver {
  public void addLifetimeListener(TokenLifetimeAware listener);
}
