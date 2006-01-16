/*
 * Created on 15-Jan-2006
 */
package uk.org.ponder.rsf.state;

/** An interface for beans wanting to be informed of flow lifecycle events.
 * Note that these may arise through the flow being actively launched or
 * terminated, or through the flow's passive expiration through some means
 * or other.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public interface TokenLifetimeAware {
  public void lifetimeStart(String tokenid);
  public void lifetimeEnd(String tokenid);
}
