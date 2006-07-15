/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.state;

import java.io.Serializable;
import java.util.Date;

/** A base class for all state stored under a token */
public class TokenState implements Serializable {
  /** The names of the two fields below, so that they may be avoided by
   * client-based storage schemes.
   */
  public static final String TOKEN_ID = "tokenID";
  public static final String EXPIRY = "expiry";
  /** The token ID under which this state is stored (for server-side storage) **/
  public String tokenID;
  /** The expiry date of this state (for server-side storage) **/
  public Date expiry;
  public Object payload;
}
