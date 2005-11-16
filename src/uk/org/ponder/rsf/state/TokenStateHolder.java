/*
 * Created on Nov 12, 2005
 */
package uk.org.ponder.rsf.state;

/** The repository of all inter-request state in RSF. */
public interface TokenStateHolder {
  /** Returns any TokenRequestState object with the specified ID.
   * @return The required TRS object, or <code>null</code> if none is stored.
   */
  public TokenState getTokenState(String tokenID);

  /** Stores the supplied TokenRequestState object in the repository */
  public void putTokenState(TokenState trs);
  public void clearTokenState(String tokenID);
}