/*
 * Created on 27 Jul 2006
 */
package uk.org.ponder.rsf.preservation;

import uk.org.ponder.rsf.state.TokenStateHolder;

public interface TSHPreservationStrategy extends StatePreservationStrategy {
  public TokenStateHolder getTokenStateHolder();
}
