/*
 * Created on Nov 12, 2005
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.rsf.preservation.StatePreservationStrategy;

/** The repository of all inter-request state in RSF, certainly on the
 * server side. Some client-side storage strategies may bypass TSH and
 * simply implement {@link StatePreservationStrategy} directly. */
public interface TokenStateHolder {
  /** Returns any TokenRequestState object with the specified ID.
   * @return The required TRS object, or <code>null</code> if none is stored.
   */
  public Object getTokenState(String tokenID);
  
  /** Stores the supplied TokenRequestState object in the repository */
  public void putTokenState(String tokenID, Object trs);
  public void clearTokenState(String tokenID);
  /** Returns a (probably request-variable) key by which the specific
   * storage for this TSH may be indexed.
   */
  public String getId();
}