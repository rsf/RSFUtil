/*
 * Created on 28 Mar 2008
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.messageutil.TargettedMessage;

/** Process a {@link TargettedMessage} item, at the close of an action cycle, where
 * messages have been collected as generated from the bean model, and are about to be
 * fixed up on their way to being rendered in a following render cycle. 
 * </p>The framework has just determined any relevant component id matching the EL
 * path that the message was generated from, and is about to rewrite the message target.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface MessageProcessor {
  /** Optionally rewrite the supplied message entry.
   * @param message The message to be rewritten. This may be modified in place in any
   * suitable way.
   * @param target The component ID (full ID) of any component which has been determined
   * to be the correct UI-level destination of the message (that is, one which has
   * submitted data which gave rise to this message). After this method returns, the
   * framework will overwrite the {@link TargettedMessage#targetid} field of the message
   * to hold this value, unless a different value is returned from this method.
   * @return An alternative desired value for the {@link TargettedMessage#targetid} field
   * than <code>target</code>, or else <code>null</code> if the value is acceptable.
   */
  public String processMessage(TargettedMessage message, String target);
}
