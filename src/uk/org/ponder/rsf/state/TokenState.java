/*
 * Created on Nov 15, 2005
 */
package uk.org.ponder.rsf.state;

import java.util.Date;

/** A base class for all state stored under a token */
public class TokenState {
  public String tokenID;
  public Date expiry;
  //public Object payload;
}
